package com.readutf.practice.match.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.readutf.practice.Practice;
import com.readutf.practice.match.Match;
import com.readutf.practice.match.inventory.InventoryCache;
import com.readutf.practice.profiles.GameState;
import com.readutf.practice.profiles.Profile;
import com.readutf.practice.spectator.SpectatorManager;
import com.readutf.practice.utils.Cooldown;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.practice.utils.Utils;
import com.readutf.uLib.libraries.duration.DurationFormatter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class MatchListeners implements Listener {

    @EventHandler
    public void playerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();

        Profile profile = Profile.getUser(player.getUniqueId());
        e.setDeathMessage(null);
        if (profile == null) {
            return;
        }

        if (profile.getGameState() == GameState.FIGHTING && profile.getActiveMatch() != null) {
            Match match = profile.getActiveMatch();
            match.elminate(null, player);
            match.messageParticipents("&c" + player.getName() + " &ehas been elimated.");
        }
    }

    @EventHandler
    public void onXpChange(PlayerExpChangeEvent e) {
        e.setAmount(e.getPlayer().getExpToLevel());
    }

    @EventHandler
    public void onEnder(ProjectileLaunchEvent e) {

        if(!(e.getEntity().getShooter() instanceof Player)) return;
        Player shooter = (Player) e.getEntity().getShooter();
        Profile profile = Profile.getUser(shooter.getUniqueId());
        if (profile.getGameState() != GameState.FIGHTING) return;

        if(profile.getActiveMatch().inWarmup) {
            e.setCancelled(true);
            shooter.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
            return;
        }

        if(e.getEntity() instanceof EnderPearl) {

            if(Cooldown.isInCooldown(shooter.getUniqueId(), "enderpearl")) {
                e.setCancelled(true);
                shooter.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
                shooter.sendMessage(SpigotUtils.color("&eYou cannot enderpearl again for &6" + DurationFormatter.getRemaining(Cooldown.getTimeLeft(shooter.getUniqueId(), "enderpearl"), true)));
                return;
            }

            Cooldown cooldown = new Cooldown(shooter.getUniqueId(), "enderpearl", 16);
            cooldown.start();
        }
    }

    @EventHandler
    public void onHeal(PotionSplashEvent e) {
        if(!(e.getPotion().getShooter() instanceof  Player)) {
            return;
        }
        ThrownPotion potion = e.getPotion();
        Player thrower = (Player) e.getPotion().getShooter();
        Profile throwerProfile = Profile.getUser(thrower.getUniqueId());

        if(throwerProfile.getGameState() == GameState.FIGHTING) {
            Match match = throwerProfile.getActiveMatch();

            for(Entity entity : e.getAffectedEntities()) {
                Player hit = (Player) entity;
                if(!match.isPlaying(hit)) {
                    e.setCancelled(true);
                }

            }

        }


    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Practice.get().getLobbyManager().spawn(e.getPlayer(), true, true);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player && e.getDamager() instanceof  Player) {
            Player player = (Player) e.getEntity();
            Player damager = (Player) e.getDamager();
            Profile profile = Profile.getUser(player.getUniqueId());
            if(profile.getGameState() != GameState.FIGHTING) return;
            Match match = profile.getActiveMatch();

            Location location = player.getLocation();


            if(match.inWarmup) {
                e.setCancelled(true);
                return;
            }
            if((player.getHealth() - e.getFinalDamage()) <= 0) {
                e.setCancelled(true);
                match.elminate(damager, player);
                match.messageParticipents(SpigotUtils.color("&9" + damager.getName() + " &7has killed &6" + player.getName()));
            }

        }
    }




    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Profile profile = Profile.getUser(player.getUniqueId());
        if(profile.getGameState() != GameState.FIGHTING) return;
        Match match = profile.getActiveMatch();

        Location location = player.getLocation();

        if(e.getTo().getBlock().getType() == Material.WATER || e.getTo().getBlock().getType() == Material.STATIONARY_WATER) {
            if(!match.getKit().isSumo()) {
                return;
            }
            InventoryCache inventoryCache = new InventoryCache(player.getName(), player.getInventory().getContents(), player.getInventory().getArmorContents());
            player.getInventory().clear();
            e.setCancelled(true);
            int team = match.getTeam(player);
            SpectatorManager.get().setSpectator(player, match);


            match.getInventoryCaches().add(inventoryCache);

            match.messageParticipents(SpigotUtils.color("&c" + player.getName() + " has been eliminated."));
            if(match.isEliminated(team)) {
                match.matchEnd(team);
            }
        }
//        if(e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ()) {
//
//            if(match.inWarmup) {
//                player.teleport(e.getFrom());
//            }
//        }
    }

    @EventHandler
    public void playerTeleport(PlayerTeleportEvent e) {
        Player player = e.getPlayer();
        Match match = getMatch(player);
        if(match == null) return;
        if(e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            Location to = e.getTo().clone();
            to.setX(Math.round(to.getBlockX()) + 0.5);
            to.setZ(Math.round(to.getBlockZ()) + 0.5);
            e.setTo(to);
        }
    }


    @EventHandler
    public void playerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        Profile profile = Profile.getUser(player.getUniqueId());
        if (profile == null) {
            return;
        }

        if (profile.getGameState() == GameState.FIGHTING && profile.getActiveMatch() != null) {
            profile.getActiveMatch().elminate(null, player);
            profile.getActiveMatch().messageParticipents(SpigotUtils.color("&c" + player.getName() + "&7disconnected."));
        }
        profile.save();
    }


    @EventHandler
    public void onBreak(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Profile profile = Profile.getUser(player.getUniqueId());
        if(Utils.inMatch(player)) {
            Match match = profile.getActiveMatch();
            if(!match.getArena().isBuilding()) {
                e.setCancelled(true);
            }
        }
    }

//    @EventHandler
//    public void playerInteract(PlayerInteractEvent e) {
//        Player player = e.getPlayer();
//        if(Utils.inMatch(player)) {
//            Match match = getMatch(player);
//            if(!match.getKit().isBuilding()) {
//                e.setCancelled(true);
//            }
//        }
//    }

    public Match getMatch(Player player) {
        Profile profile = Profile.getUser(player.getUniqueId());
        if(profile == null) return null;
        if(profile.getGameState() != GameState.FIGHTING) return null;
        if(profile.getActiveMatch() == null) return null;
        return profile.getActiveMatch();
    }

}
