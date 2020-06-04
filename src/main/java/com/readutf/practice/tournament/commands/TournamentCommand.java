package com.readutf.practice.tournament.commands;

import com.readutf.practice.tournament.commands.args.TournamentStart;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.command.CommandHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class TournamentCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(!sender.hasPermission("practice.command.tournament")) {
            sender.sendMessage(SpigotUtils.color("&cNo Perm."));
            return true;
        }

        CommandHelper commandHelper = new CommandHelper(s,
                "Tournament Commands",
                ChatColor.GOLD,
                ChatColor.YELLOW,
                ChatColor.GRAY, Arrays.asList(new TournamentStart()));

        if(args.length == 0) {
            commandHelper.sendTo(sender);
            return true;
        } else {
            commandHelper.handleSubs(sender, args);
        }




        return true;
    }
}
