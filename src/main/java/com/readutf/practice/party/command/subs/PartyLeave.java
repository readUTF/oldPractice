package com.readutf.practice.party.command.subs;

import com.readutf.practice.party.Party;
import com.readutf.practice.profiles.Profile;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class PartyLeave extends SubCommand {

    String prefix = "&8[&6Party&8] ";

    public PartyLeave() {
        super("leave", Arrays.asList("quit", "disband"), "Leave your current party, or disband it if your the leader!", 1, "", true, "practice.command.party.leave");
    }

    @Override
    public void execute(CommandSender sender, String c, String[] args) {
        Player player = (Player) sender;
        Profile profile = Profile.getUser(player.getUniqueId());
        Party party = profile.getParty();

        if(party == null) {
            player.sendMessage(SpigotUtils.color(prefix + "&cYou are not in a party."));
            return;
        }

        boolean disbanded = party.leave(player);
        if(disbanded) {
            sender.sendMessage(SpigotUtils.color(prefix + "&aParty has been disbanded."));
            return;
        }
        sender.sendMessage(SpigotUtils.color(prefix + "&aYou have left your party!"));
    }
}
