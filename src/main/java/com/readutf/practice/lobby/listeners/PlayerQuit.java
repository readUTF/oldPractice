package com.readutf.practice.lobby.listeners;

import com.readutf.practice.Practice;
import com.readutf.practice.match.MatchManager;
import com.readutf.practice.profiles.GameState;
import com.readutf.practice.profiles.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        e.setQuitMessage(null);

        Profile profile = Profile.getUser(player.getUniqueId());
        if(profile == null) return;

        if(profile.getGameState() == GameState.QUEUEING) {
            if(profile.getQueue() == null) {return;}
            MatchManager.get().getQueueManager().removeFromQueue(player);

            profile.setQueue(null);
            profile.setGameState(GameState.LOBBY);
            Practice.get().getLobbyManager().spawn(player, true, true);
        }
        profile.save();
        Profile.profiles.remove(profile);


    }

}
