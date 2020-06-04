package com.readutf.practice.commands;

import com.readutf.practice.arena.Arena;
import com.readutf.practice.arena.ArenaManager;
import com.readutf.practice.kits.Kit;
import com.readutf.practice.match.Match;
import com.readutf.practice.match.MatchManager;
import com.readutf.practice.match.queue.QueueType;
import com.readutf.practice.profiles.Profile;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.practice.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

public class AcceptCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String c, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(SpigotUtils.color("&cYou must be a player to use this command."));
            return true;
        }

        Player player = (Player) sender;
        Profile profile = Profile.getUser(player.getUniqueId());

        if(!Utils.inLobby(player)) {
            player.sendMessage(SpigotUtils.color("&cYou cannot accept a duel right now."));
            return true;
        }

        if(args.length == 0) {
            sender.sendMessage(SpigotUtils.color("&cUsage: /" + cmd + " <player>"));
            return true;
        }

        if(args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if(target == null) {
                sender.sendMessage(SpigotUtils.color("&cInvalid Player."));
                return true;
            }
            if(profile.getDuelRequests().containsKey(target)) {
                Kit kit = profile.getDuelRequests().get(target);
                Arena arena = ArenaManager.get().getNextArena(kit);
                if(arena == null) {
                    sender.sendMessage(SpigotUtils.color("&cCould not find arena."));
                    return true;
                }
                MatchManager.get().startMatch(Collections.singletonList(player), Collections.singletonList(target
                ), arena, kit, QueueType.UNRANKED, null);
                return true;
            }
            sender.sendMessage(SpigotUtils.color("&cYou do not have an invite from that player"));

        }


        return true;
    }
}
