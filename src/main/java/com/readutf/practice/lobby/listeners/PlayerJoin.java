package com.readutf.practice.lobby.listeners;


import com.keenant.tabbed.item.TextTabItem;
import com.keenant.tabbed.tablist.TableTabList;
import com.keenant.tabbed.tablist.TitledTabList;
import com.readutf.practice.Practice;
import com.readutf.practice.match.MatchManager;
import com.readutf.practice.profiles.GameState;
import com.readutf.practice.profiles.Profile;
import com.readutf.uLib.libraries.Players;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        e.setJoinMessage(null);

        Profile profile = Profile.getUser(e.getPlayer().getUniqueId());
        if(profile == null) {
            profile = new Profile(player.getUniqueId());
        }
        profile.load();
        profile.setUsername(e.getPlayer().getName());



        Practice.get().getLobbyManager().spawn(player, true, true);
        profile.setGameState(GameState.LOBBY);
        if(MatchManager.get().getQueueManager().getQueue(player) != null) {
            MatchManager.get().getQueueManager().removeFromQueue(player);
        }






    }

}
