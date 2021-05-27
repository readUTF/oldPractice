package com.readutf.practice.commands;

import com.readutf.practice.profiles.Profile;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class BuildingCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {

        if(!CommandUtils.playerOnly(sender)) {
            sender.sendMessage(SpigotUtils.color("&cyou must be palyer."));
            return true;
        }
        Player player = (Player) sender;


        if(!sender.hasPermission("practice.command.build")) {
            sender.sendMessage(SpigotUtils.color("&cno perm."));
            return true;
        }

        Profile profile = Profile.getUser(player.getUniqueId());
        profile.setBuilding(!profile.isBuilding());

        if(profile.isBuilding()) {
            player.sendMessage(SpigotUtils.color("&aYou have enabled building mode."));
            return true;
        } else {
            player.sendMessage(SpigotUtils.color("&cYou have disabled building mode."));
            return true;
        }
    }
}
