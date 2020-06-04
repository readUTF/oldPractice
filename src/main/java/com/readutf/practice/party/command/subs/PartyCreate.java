package com.readutf.practice.party.command.subs;

import com.readutf.practice.party.Party;
import com.readutf.practice.profiles.GameState;
import com.readutf.practice.profiles.Profile;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.PlayerUtil;
import com.readutf.uLib.libraries.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;

public class PartyCreate extends SubCommand {

    String prefix = "&7[&bParty&7] ";

    public PartyCreate() {
        super("create", Collections.singletonList("c"), "Create a party.", 1, "", true, "practice.command.party.create");
    }

    @Override
    public void execute(CommandSender sender, String c, String[] args) {

        Player player = (Player) sender;
        Profile profile = Profile.getUser(player.getUniqueId());

        if(Party.inParty(player)) {
            player.sendMessage(SpigotUtils.color(prefix + "&cYou are already in a party."));
            return;
        }

        Party party = new Party(player, new ArrayList<Player>(), true);
        profile.setGameState(GameState.PARTY);
        profile.setParty(party);
        PlayerUtil.clear(player);
        party.giveItems(player);
        party.getMembers().add(player);
        player.sendMessage(SpigotUtils.color(prefix + "&aParty has been created."));


    }

}
