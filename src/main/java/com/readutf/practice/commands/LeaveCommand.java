package com.readutf.practice.commands;

import com.readutf.practice.Practice;
import com.readutf.practice.lobby.LobbyManager;
import com.readutf.practice.profiles.GameState;
import com.readutf.practice.profiles.Profile;
import com.readutf.practice.utils.BukkitSerialisation;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.command.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String c, String[] args) {
        if(!CommandUtils.playerOnly(sender)) {
            sender.sendMessage(SpigotUtils.color("&cYou must be a player to use this command."));
            return true;
        }



        Player player = (Player) sender;

        Profile profile = Profile.getUser(player.getUniqueId());
        player.sendMessage(profile.getGameState().name());



        if(profile.getGameState() == GameState.EDITING_KIT) {



            if(profile.getEditingKit().getName() != null) {
                Practice.get().getUserKit().set(
                        player.getUniqueId().toString() +
                                "." + profile.getEditingKit().getName(),
                        BukkitSerialisation.itemStackArrayToBase64(player.getInventory().getContents()));
                Practice.get().getUserKitConfig().saveConfig();
                System.out.println("updated kit");
            }
            profile.setEditingKit(null);
            Practice.get().getLobbyManager().spawn(player, true, false);
            return true;
        }

        return true;
    }
}
