package com.readutf.practice.match.quake;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.readutf.practice.match.Match;
import com.readutf.practice.match.inventory.InventoryCache;
import com.readutf.practice.profiles.GameState;
import com.readutf.practice.profiles.Profile;
import com.readutf.practice.spectator.SpectatorManager;
import com.readutf.practice.utils.Cooldown;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.practice.utils.Utils;
import com.readutf.uLib.libraries.ColorUtil;
import com.readutf.uLib.libraries.ItemBuilder;
import com.readutf.uLib.libraries.PlayerUtil;
import com.readutf.uLib.libraries.duration.DurationFormatter;
import lombok.Getter;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;
import org.inventivetalent.particle.ParticleEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QuakeManager implements Listener {

    @Getter
    private static ItemStack gun = new ItemBuilder(Material.DIAMOND_HOE).setName(ColorUtil.color("&eZapatron")).toItemStack();

    @EventHandler
    public void onClick(PlayerInteractEvent e) {

        Player player = e.getPlayer();

        if (e.getItem() == null) return;
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!e.getItem().isSimilar(getGun())) {
                return;
            }

            if(!Utils.inMatch(player)) return;
            Profile profile = Profile.getUser(player.getUniqueId());
            if(!profile.getActiveMatch().getKit().isQuake()) return;

            if(Cooldown.isInCooldown(player.getUniqueId(), "reloading")) {
                player.playSound(player.getLocation(), Sound.PISTON_RETRACT, 10, 1);
                player.sendMessage(SpigotUtils.color("&cYou are still reloading! (" + DurationFormatter.getRemaining(Cooldown.getTimeLeft(player.getUniqueId(), "reloading"), true)) + ")");
                return;
            }

            Cooldown cooldown = new Cooldown(player.getUniqueId(), "reloading", 3);
            cooldown.start();

            player.playSound(player.getLocation(), Sound.EXPLODE, 5, 1);


            Player lookingAt = getLookingAt(e.getPlayer());
            BlockIterator i = new BlockIterator(player, 30);
            while(i.hasNext()) {
                Block block = i.next();
                if(block.getType().isSolid()) {
                    break;
                }

                ParticleEffect.REDSTONE.send(profile.getActiveMatch().getFightingAndWatching() ,block.getLocation(),0,0,0, 0, 1);
            }
            if(lookingAt == null) {
                return;
            }
            Damageable damageable = (Damageable) lookingAt;
            lookingAt.setHealth(damageable.getHealth() - 10);
            if(damageable.getHealth() == 0) {
                Match match = profile.getActiveMatch();

                InventoryCache inventoryCache = new InventoryCache(lookingAt.getName(), lookingAt.getInventory().getContents(), lookingAt.getInventory().getArmorContents());
                lookingAt.getInventory().clear();
                e.setCancelled(true);
                int team = match.getTeam(lookingAt);
                SpectatorManager.get().setSpectator(lookingAt, match);



                match.getInventoryCaches().add(inventoryCache);

                match.messageParticipents(SpigotUtils.color("&9" + player.getName() + " &7has killed &6" + lookingAt.getName()));
                if(match.isEliminated(team)) {
                    match.matchEnd(team);
                }

            }
            ParticleEffect.EXPLOSION_HUGE.send(profile.getActiveMatch().getFightingAndWatching() ,lookingAt.getLocation(),0,0,0, 0, 1);
            player.playSound(player.getLocation(), Sound.EXPLODE, 10, 1);

        }

    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if(e.getEntity() instanceof  Player) {
            Player player = (Player) e.getEntity();
            Profile profile = Profile.getUser(player.getUniqueId());
            if(profile.getGameState() == GameState.FIGHTING) {
                Match match = profile.getActiveMatch();
                if(match.getKit().isQuake()) {
                    e.setCancelled(true);
                }
            }
        }
    }

    public Player getLookingAt(Player player) {
        List<Entity> nearbyE = player.getNearbyEntities(150,
                150, 150);
        ArrayList<LivingEntity> livingE = new ArrayList<LivingEntity>();

        for (Entity e : nearbyE) {
            if (e instanceof LivingEntity) {
                livingE.add((LivingEntity) e);
            }
        }

        Player target = null;

        BlockIterator bItr = new BlockIterator(player, 150);
        Block block;
        Location loc;
        int bx, by, bz;
        double ex, ey, ez;
        // loop through player's line of sight
        while (bItr.hasNext()) {
            block = bItr.next();
            if(block.getType().isSolid()) break;
            bx = block.getX();
            by = block.getY();
            bz = block.getZ();
            // check for entities near this block in the line of sight
            for (LivingEntity e : livingE) {
                loc = e.getLocation();
                ex = loc.getX();
                ey = loc.getY();
                ez = loc.getZ();
                if ((bx - .75 <= ex && ex <= bx + 1.75) && (bz - .75 <= ez && ez <= bz + 1.75) && (by - 1 <= ey && ey <= by + 2.5)) {
                    // entity is close enough, set target and stop
                    target = (Player) e;
                    break;
                }
            }
        }
        return target;
    }
}

