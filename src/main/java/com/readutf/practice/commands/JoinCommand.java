package com.readutf.practice.commands;

import com.readutf.practice.tournament.Tournament;
import com.readutf.practice.tournament.TournamentManager;
import com.readutf.practice.utils.SpigotUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String c, String[] args) {

        if(!(sender instanceof  Player)) {
            sender.sendMessage(SpigotUtils.color("&cYou must a player to use this command."));
            return true;
        }

        Player player = (Player) sender;

        if(TournamentManager.get().getActiveTournament() == null) {
            sender.sendMessage(SpigotUtils.color("&cNo Tournament is currently active."));
            return true;
        }


        Tournament tournament = TournamentManager.get().getActiveTournament();

        if(tournament.isActive()) {
            sender.sendMessage(SpigotUtils.color("&cThe tournament has already started."));
            return true;
        }

        if(tournament.getCompetitors().contains(player)) {
            sender.sendMessage(SpigotUtils.color("&cYou are already in the tournament."));
            return true;
        }

        if(tournament.getMaxSize() <= tournament.getCompetitors().size()) {
            sender.sendMessage(SpigotUtils.color("&cThe tournament is full."));
            return true;
        }

        tournament.addPlayer(player);
        Bukkit.broadcastMessage(SpigotUtils.color(Tournament.getPrefix() + " &6" + sender.getName() + "&e has joined the tournament. &7(" + tournament.getCompetitors().size()  + "/" + tournament.getMaxSize() + ")"));
        return true;
    }
}
