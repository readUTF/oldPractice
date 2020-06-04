package com.readutf.practice.party.command;

import com.readutf.practice.party.command.subs.PartyCreate;
import com.readutf.practice.party.command.subs.PartyJoin;
import com.readutf.practice.party.command.subs.PartyLeave;
import com.readutf.practice.party.command.subs.PartyOpen;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.command.CommandHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class PartyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String c, String[] args) {

        if(true) {
            sender.sendMessage(SpigotUtils.color("&cParties Disabled."));
            return true;
        }

        CommandHelper commandHelper = new CommandHelper(c,
                "Party Management commands.",
                ChatColor.GOLD,
                ChatColor.YELLOW,
                ChatColor.GRAY,
                Arrays.asList(new PartyCreate(), new PartyLeave(), new PartyOpen(), new PartyJoin()));
        if(args.length == 0) {
            commandHelper.sendTo(sender);
            return true;
        }
        if(args.length > 0) {
            commandHelper.handleSubs(sender, args);
        }


        return true;
    }
}
