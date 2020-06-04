package com.readutf.practice.party.command.subs;

import com.readutf.practice.party.Party;
import com.readutf.practice.profiles.Profile;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PartyInvite extends SubCommand {

    public PartyInvite() {
        super("invite", Collections.emptyList(), "Invite a player to your party.", 2, "<player>", true, "practice.command.party.invite");
    }

    @Override
    public void execute(CommandSender sender, String c, String[] args) {

        Player player = (Player) sender;
        Profile profile = Profile.getUser(player.getUniqueId());

        Party party = profile.getParty();
        if(party == null) {
            sender.sendMessage(SpigotUtils.color("&cYou are not in a party."));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);


        if(party.getLeader() == player) {
            sender.sendMessage(SpigotUtils.color(party + " &cYou must be the party leader to invite someone."));
            return;
        }




    }
}
