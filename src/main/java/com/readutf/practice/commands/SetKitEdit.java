package com.readutf.practice.commands;

import com.readutf.practice.Practice;
import com.readutf.practice.utils.SpigotUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetKitEdit implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String c, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(SpigotUtils.color("&cYou must be a player to use this command."));
            return true;
        }

        Player player = (Player) sender;

        if (!sender.hasPermission("practice.command.setkitedit")) {
            sender.sendMessage(SpigotUtils.color("&cNo perm."));
            return true;
        }

        Practice.get().setSpawn(player.getLocation());
        sender.sendMessage(SpigotUtils.color("&cKit Edit Location"));
        return true;
    }
}
