package com.readutf.practice.commands;

import com.readutf.practice.profiles.GameState;
import com.readutf.practice.profiles.Profile;
import com.readutf.practice.settings.Setting;
import com.readutf.practice.settings.SettingsManager;
import com.readutf.practice.spectator.SpectatorManager;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpectateCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String c, String[] args) {

        if (!CommandUtils.checkPerm(sender, "practice.command.spectate")) {
            sender.sendMessage(SpigotUtils.color("&cYou do not have permission for this command."));
            return true;
        }
        if (!CommandUtils.playerOnly(sender)) {
            sender.sendMessage(SpigotUtils.color("&cYou must be a player to use this command."));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(SpigotUtils.color("&cUsage: /" + c + " <target>"));
            return true;
        }

        if (args.length == 1) {
            Player player = (Player) sender;
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(SpigotUtils.color("&cCould not find player."));
                return true;
            }


            Profile targetProfile = Profile.getUser(target.getUniqueId());
            if (targetProfile.getGameState() != GameState.FIGHTING && targetProfile.getActiveMatch() == null) {
                sender.sendMessage(SpigotUtils.color("&c" + args[0] + " is not currently in a match."));
                return true;
            }

            if (!SettingsManager.get().getSettingValue(Setting.ALLOW_SPECTATORS, targetProfile)) {
                sender.sendMessage(SpigotUtils.color("&cThis player has spectators disabled."));
                return true;
            }

            SpectatorManager.get().setSpectator(player, targetProfile.getActiveMatch());

        }


        return true;
    }
}
