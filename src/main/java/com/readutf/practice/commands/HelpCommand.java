package com.readutf.practice.commands;

import com.readutf.practice.utils.SpigotUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class HelpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {

        List<String> msg = Arrays.asList("&7&n-----------------------------------",
                "&4Server Help",
                "",
                "&4&lUseful Commands",
                "&c/Helpop {request}&7: &7Request Help",
                "&c/Report {name} {reason}&7: &7Report a Player",
                "&c/party&7: &7View the party command",
        "",
                "&4&lUseful Links",
                "&cDiscord&7: &7discord.railed.rip",
                "&cStore&7: &7store.railed.rip",
                "&cWebsite&7: &7Coming soon",
        "&eType /Settings or use the clock to optimise your experience",
                "&7&m-----------------------------------");
        
        
        for(String line : msg) {
            sender.sendMessage(SpigotUtils.color(line));
        }
        
        return true;
    }
}
