package com.readutf.practice.staff;

import com.readutf.practice.Practice;
import com.readutf.practice.profiles.GameState;
import com.readutf.practice.profiles.Profile;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.ItemBuilder;
import com.readutf.uLib.libraries.PlayerUtil;
import com.readutf.uLib.libraries.Players;
import com.readutf.uLib.libraries.clickables.Clickable;
import com.readutf.uLib.libraries.clickables.ClickableManager;
import com.readutf.uLib.libraries.menu.ItemClick;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

import java.util.Arrays;
import java.util.HashMap;

public class StaffManager {

    private static StaffManager staffManager;

    HashMap<Player, Integer> currentPlayer = new HashMap<>();
    String prefix = "&7[&eStaff&7] ";

    public StaffManager(Practice practice) {
        staffManager = this;
    }

    public void setStaffMode(Player player, boolean enabled) {
        Profile profile = Profile.getUser(player.getUniqueId());
        if(profile == null) {
            player.sendMessage(SpigotUtils.color("&cAn error occured whilst finding your profile."));
            return;
        }

        if(profile.getGameState() != GameState.LOBBY) {
            player.sendMessage(SpigotUtils.color("&cYou must be in the lobby to use this."));
            return;
        }

        if(enabled) {
            profile.setGameState(GameState.STAFF);
            ClickableManager.get().giveClickable(player, new Clickable(
                    new ItemBuilder(Material.RECORD_6).setName("&6Random Tp").toItemStack(),
                    new ItemClick(3) {
                        @Override
                        public void itemClick(Player player) {
                            if(Players.getOnlinePlayers().size() == 1) {
                                player.sendMessage(SpigotUtils.color(prefix + "&cThere are no other players online."));
                            }
                            currentPlayer.putIfAbsent(player, 0);
                            if(currentPlayer.get(player) >= Players.getOnlinePlayers().size()) {
                                currentPlayer.put(player, 0);
                            }
                            player.teleport(Players.getOnlinePlayers().toArray(new Player[Players.getOnlinePlayers().size()])[currentPlayer.get(player)]);
                        }
                    },
                    Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK)
            ), 8);
        } else {
            profile.setGameState(GameState.LOBBY);
            Practice.get().getLobbyManager().spawn(player, true, true);
        }


    }

    public static StaffManager get() {
        return staffManager;
    }

}
