package com.readutf.practice.party.command.subs;

import com.readutf.practice.party.Party;
import com.readutf.practice.profiles.Profile;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class PartyOpen extends SubCommand {


    public PartyOpen() {
        super("open", Arrays.asList("toggle"), "Open or close the party.", 1, "", true, "practice.command.party.open");
    }

    @Override
    public void execute(CommandSender sender, String s, String[] args) {

        Player player = (Player) sender;
        Profile profile = Profile.getUser(player.getUniqueId());
        Party party = profile.getParty();
        if(party == null) {
            sender.sendMessage(SpigotUtils.color("&cYou must be a player to use this command."));
            return;
        }

        party.setInviteOnly(!party.isInviteOnly());
        if(party.isInviteOnly()) {
            party.messageAll(party.getPrefix() + " &cThe party is now invite only.");
        } else {
            party.messageAll(party.getPrefix() + " &aThe party is now open.");
        }
    }
}
