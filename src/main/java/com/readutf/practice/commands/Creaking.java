package com.readutf.practice.commands;

import com.readutf.practice.utils.SpigotUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Creaking implements CommandExecutor  {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {

        if(sender.getName().equalsIgnoreCase("Creaking")) {
            sender.sendMessage("hello creaking");
        } else {
            sender.sendMessage(SpigotUtils.color("&cYou arn't creaking >:("));

        }



        return true;
    }
}
