package com.readutf.practice.spectator;

import com.readutf.practice.Practice;
import com.readutf.practice.match.Match;
import com.readutf.practice.profiles.GameState;
import com.readutf.practice.profiles.Profile;
import com.readutf.practice.spectator.listeners.SpectatorListeners;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.practice.utils.Utils;
import com.readutf.uLib.libraries.ItemBuilder;
import com.readutf.uLib.libraries.Players;
import com.readutf.uLib.libraries.clickables.Clickable;
import com.readutf.uLib.libraries.clickables.ClickableManager;
import com.readutf.uLib.libraries.menu.ItemClick;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class SpectatorManager {

    Practice practice;
    static SpectatorManager spectatorManager;

    public SpectatorManager(Practice practice) {
        this.practice = practice;
        spectatorManager = this;

        Bukkit.getPluginManager().registerEvents(new SpectatorListeners(), practice);

    }

    public static SpectatorManager get() {
        return spectatorManager;
    }

    public void setSpectator(Player player, Match match) {

        Profile profile = Profile.getUser(player.getUniqueId());
        Players.getOnlinePlayers().forEach(player1 -> {
            if(!Utils.isSpectator(player1)) {
                player1.hidePlayer(player);
            }

        });
        player.teleport(match.getArena().getSpawn1());
        Utils.resetPlayer(player);
        profile.setGameState(GameState.SPECTATING);
        profile.setSpectatingMatch(match);
        match.messageParticipents(SpigotUtils.color("&6" + player.getName() +" &eis now spectating."));
        player.sendMessage(SpigotUtils.color("&aYou are now spectating &c" + player.getName()));

        match.getSpectating().add(player);

        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[]{});

        ClickableManager.get().giveClickable(player, new Clickable(new ItemBuilder(Material.INK_SACK).setName(SpigotUtils.color("&cStop Spectating")).setDurability((short) 1).toItemStack(), new ItemClick(11) {
            @Override
            public void itemClick(Player player) {

                profile.setActiveMatch(null);
                profile.setGameState(GameState.LOBBY);
                Practice.get().getLobbyManager().spawn(player, true, true);
                player.setGameMode(GameMode.SURVIVAL);
                player.setFlying(false);
                match.getSpectating().remove(player);
            }
        }, Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK)), 0);


        player.setGameMode(GameMode.CREATIVE);
        player.setFlying(true);



    }


}
