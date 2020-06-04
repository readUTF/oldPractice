package com.readutf.practice.match.listeners;

import com.readutf.practice.Practice;
import com.readutf.practice.match.Match;
import com.readutf.practice.profiles.GameState;
import com.readutf.practice.profiles.Profile;
import com.readutf.practice.utils.Utils;
import com.readutf.uLib.libraries.Players;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class VisibilityHandler implements Listener {


    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        Profile profile = Profile.getUser(player.getUniqueId());
        if(!Utils.inMatch(player)) return;

        Match match = profile.getActiveMatch();

        match.getItems().add(e.getItemDrop());

        for(Player all : Players.getOnlinePlayers()) {
            if(!match.getFightingAndWatching().contains(all)) {
                Practice.get().getEntityHider().hidePlayer(all, e.getItemDrop());
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        e.getDrops().clear();
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent e) {
        if(e.getEntity().getShooter() instanceof Player) {
            Player player = (Player) e.getEntity().getShooter();
            Profile profile = Profile.getUser(player.getUniqueId());
            if(profile == null || profile.getGameState() != GameState.FIGHTING || profile.getActiveMatch() == null) return;

            Match match = profile.getActiveMatch();
            Players.getOnlinePlayers().stream().filter(player1 -> !match.isPlaying(player1)).forEach(player1 -> {
                Practice.get().getEntityHider().hidePlayer(player1, e.getEntity());
            });


        }



    }

}
