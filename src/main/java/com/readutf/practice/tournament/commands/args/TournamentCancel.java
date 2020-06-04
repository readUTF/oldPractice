package com.readutf.practice.tournament.commands.args;

import com.readutf.practice.match.Match;
import com.readutf.practice.tournament.Tournament;
import com.readutf.practice.tournament.TournamentManager;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class TournamentCancel extends SubCommand {
    public TournamentCancel() {
        super("cancel", Collections.emptyList(), "Cancel the current event.", 1, "", false, "practice.command.tournament.cancel");
    }

    @Override
    public void execute(CommandSender sender, String c, String[] args) {
        if(TournamentManager.get().getActiveTournament() == null) {
            sender.sendMessage(SpigotUtils.color("&cNo Tournament is active."));
            return;
        }

        Tournament tournament = TournamentManager.get().getActiveTournament();

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(SpigotUtils.color(Tournament.getPrefix() + " &cThe tournament has been cancelled."));
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("");

        if(tournament.isActive()) {
            for(Match match : tournament.getTournamentMatches()) {
                match.matchEnd(3);
            }
        }

        TournamentManager.get().setActiveTournament(null);

    }
}
