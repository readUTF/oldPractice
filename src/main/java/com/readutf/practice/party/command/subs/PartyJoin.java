package com.readutf.practice.party.command.subs;

import com.readutf.practice.party.Party;
import com.readutf.practice.profiles.GameState;
import com.readutf.practice.profiles.Profile;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.PlayerUtil;
import com.readutf.uLib.libraries.command.CommandHelper;
import com.readutf.uLib.libraries.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sun.security.provider.ConfigFile;

import java.util.Collections;
import java.util.List;

public class PartyJoin extends SubCommand {

    public PartyJoin() {
        super("join", Collections.emptyList(), "Join an existing party.", 2, "<player>", true, "practice.command.party.join");
    }

    @Override
    public void execute(CommandSender sender, String c, String[] args) {

        Player player = (Player) sender;
        Profile profile = Profile.getUser(player.getUniqueId());

        Player target = Bukkit.getPlayer(args[1]);
        if(target == null) {
            sender.sendMessage(SpigotUtils.color(CommandHelper.LANG.PLAYER_NOT_FOUND.getText().replace("{1}", args[1])));
            return;
        }
        Profile targetProfile = Profile.getUser(target.getUniqueId());
        Party party = targetProfile.getParty();
        if(party == null) {
            sender.sendMessage(SpigotUtils.color("&cPlayer is not in a party"));
            return;
        }
        if(profile.getParty() != null) {
            sender.sendMessage(SpigotUtils.color("&cYou are already in a party."));
        }


        if(party.isInviteOnly()) {
            if(party.getInvites().contains(player)) {
                profile.setParty(party);
                PlayerUtil.clear(player);
                party.giveItems(player);
                party.getMembers().add(player);
                party.messageAll(SpigotUtils.color(party.getPrefix() + " &c" + sender.getName() + " &7has joined the party."));
                return;
            } else {
                sender.sendMessage(SpigotUtils.color(party.getPrefix() + " &cYou have not been invited to this party."));
                return;
            }
        } else {
            profile.setGameState(GameState.PARTY);
            profile.setParty(party);
            PlayerUtil.clear(player);
            party.giveItems(player);
            party.getMembers().add(player);
            party.messageAll(SpigotUtils.color("&c" + sender.getName() + " &7has joined the party."));
            return;
        }

    }
}
