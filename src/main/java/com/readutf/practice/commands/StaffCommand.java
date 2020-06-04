package com.readutf.practice.commands;

import com.readutf.practice.profiles.GameState;
import com.readutf.practice.profiles.Profile;
import com.readutf.practice.staff.StaffManager;
import com.readutf.practice.utils.SpigotUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String c, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(SpigotUtils.color("&cYou must be a player to use this command."));
            return true;
        }

        Player player = (Player) sender;
        Profile profile = Profile.getUser(player.getUniqueId());
        StaffManager.get().setStaffMode(player, profile.getGameState() != GameState.STAFF);

        return true;
    }
}
