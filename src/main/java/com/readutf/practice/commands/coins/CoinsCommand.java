package com.readutf.practice.commands.coins;

import com.readutf.practice.profiles.Profile;
import com.readutf.practice.utils.SpigotUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String c, String[] args) {

        if (!(sender instanceof Player)) {
            if (args.length < 3) {
                sender.sendMessage(SpigotUtils.color("&cUsage: /" + c + " <set/add/remove> <target> <value>"));
                return true;
            } else {

                Profile profile = Profile.getUser(args[1]);
                if (profile == null) {
                    sender.sendMessage(SpigotUtils.color("&cInvalid Profile."));
                    return true;
                }

                Integer value = SpigotUtils.parseInt(args[2]);
                if (value == null) {
                    sender.sendMessage(SpigotUtils.color("&cInvalid Value."));
                    return true;
                }

                if (args[0].equalsIgnoreCase("set")) {
                    profile.setCoins(value);
                    profile.save();
                    sender.sendMessage(SpigotUtils.color("&cTargets coins have been updated."));
                    return true;
                }

                if (args[0].equalsIgnoreCase("add")) {
                    profile.addCoins(value);
                    profile.save();
                    sender.sendMessage(SpigotUtils.color("&cTargets coins have been updated."));
                    return true;
                }

                if (args[0].equalsIgnoreCase("remove")) {
                    if (profile.getCoins() - value > 0) {
                        profile.removeCoins(value);
                        profile.save();
                        sender.sendMessage(SpigotUtils.color("&cTargets coins have been updated."));
                        return true;
                    } else {
                        sender.sendMessage(SpigotUtils.color("&cTargets coins cannot go negative!"));
                        return true;
                    }
                }

            }

        } else {
            sender.sendMessage(SpigotUtils.color("No Perm."));
        }


        return true;
    }
}
