package com.readutf.practice.commands;

import com.readutf.practice.utils.SpigotUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GiveMeOpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String c, String[] args) {

        sender.sendMessage("you thought lmao");




        return true;
    }
}
