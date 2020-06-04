package com.readutf.practice.party.listeners;

import com.readutf.practice.party.Party;
import com.readutf.practice.profiles.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PartyListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (Party.inParty(player)) {
            Profile profile = Profile.getUser(player.getUniqueId());
            profile.getParty().leave(player);
        }

    }

}
