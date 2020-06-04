package com.readutf.practice.commands.wager;

import com.readutf.practice.commands.wager.subs.Accept;
import com.readutf.practice.commands.wager.subs.Duel;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.practice.utils.Utils;
import com.readutf.uLib.libraries.command.CommandHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class WagerCommand implements CommandExecutor {


    //subs: duel, check, shop, pay
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String c, String[] args) {

        if(sender instanceof Player) {
            if(Utils.inMatch((Player) sender)) {
                sender.sendMessage(SpigotUtils.color("&cYou can not use this command whilst in a match."));
                return true;
            }
        }

        CommandHelper commandHelper = new CommandHelper(c,
                "Buy coins, wager coins, win money.",
                ChatColor.GOLD,
                ChatColor.YELLOW,
                ChatColor.GRAY,
                Arrays.asList( new Accept(), new Duel()));


        if(args.length == 0) {

            commandHelper.sendTo(sender);
            return true;
        }

        commandHelper.handleSubs(sender, args);



        return true;
    }
}
