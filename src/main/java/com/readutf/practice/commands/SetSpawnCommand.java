package com.readutf.practice.commands;

import com.readutf.practice.Practice;
import com.readutf.practice.utils.SpigotUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String c, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(SpigotUtils.color("&cYou must be a player to use this command."));
            return true;
        }

        if(!sender.hasPermission("practice.command.setspawn")) {
            sender.sendMessage(SpigotUtils.color("&cNo perm."));
            return true;
        }

        Player player = (Player) sender;
        Practice.get().setSpawn(player.getLocation());
        player.sendMessage(SpigotUtils.color("&aSpawn location has been updated."));
        return true;
    }
}
