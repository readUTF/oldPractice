package com.readutf.practice.commands.wager.subs;

import com.readutf.practice.arena.ArenaManager;
import com.readutf.practice.kits.Kit;
import com.readutf.practice.match.MatchManager;
import com.readutf.practice.match.queue.QueueType;
import com.readutf.practice.match.rewards.WagerReward;
import com.readutf.practice.profiles.Profile;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.practice.utils.Utils;
import com.readutf.uLib.libraries.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Accept extends SubCommand {
    public Accept() {
        super("accept", Collections.emptyList(), "Accept a wager request.", 2, "accept <player>", true, "practice.command.wager.accept");
    }

    @Override
    public void execute(CommandSender sender, String s, String[] args) {
        Player player = (Player) sender;
        //wager accept <player>

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(SpigotUtils.color("&cCould find player"));
            return;
        }

        if(!Utils.inLobby(target)) {
            sender.sendMessage(SpigotUtils.color("&cYou must be in the lobby to accept."));
            return;
        }



        Profile profile = Profile.getUser(player.getUniqueId());
        sender.sendMessage(profile.getWagerRequests().toString());
        if (profile.getWagerRequests().containsKey(target)) {
            Kit kit = profile.getWagerRequests().get(target);
            MatchManager.get().startMatch(Arrays.asList(player), Arrays.asList(target
            ), ArenaManager.get().getNextArena(kit), kit, QueueType.WAGER, new WagerReward(profile.getWagerAmount().get(target)));


            profile.getWagerRequests().remove(player);
            profile.getWagerAmount().remove(player);
            return;

        }
        sender.sendMessage(SpigotUtils.color("&cYou do not have an invite from that player"));
    }




}
