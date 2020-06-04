package com.readutf.practice.tournament.commands.args;

import com.readutf.practice.Practice;
import com.readutf.practice.arena.Arena;
import com.readutf.practice.arena.ArenaManager;
import com.readutf.practice.kits.Kit;
import com.readutf.practice.kits.KitManager;
import com.readutf.practice.tournament.Tournament;
import com.readutf.practice.tournament.TournamentManager;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.command.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class TournamentStart extends SubCommand {


    //tournament start NoDebuff 2 8

    public TournamentStart() {
        super("start", Collections.singletonList("s"), "Start a tournament", 3, "start <kit> <min> <max>", false, "practice.command.tournament.start");
    }

    @Override
    public void execute(CommandSender sender, String s, String[] args) {

        if(TournamentManager.get().getActiveTournament() != null) {
            sender.sendMessage(SpigotUtils.color("&cA tournament is already active."));
            return;
        }

        Kit kit = KitManager.get().getKitByName(args[1]);
        if(kit == null) {
            sender.sendMessage(SpigotUtils.color("&cCould not find kit named '" + args[1] + "'"));
            return;
        }
        Integer min = SpigotUtils.parseInt(args[2]);
        if(min == null) {
            sender.sendMessage(SpigotUtils.color("&cInvalid Min Value"));
            return;
        }
        Integer max = SpigotUtils.parseInt(args[3]);
        if(max == null) {
            sender.sendMessage(SpigotUtils.color("&cInvalid Max Value"));
            return;
        }

        Arena arena = ArenaManager.get().getNextArena(kit);
        if(arena == null) {
            sender.sendMessage(SpigotUtils.color("&cCould not find a arena that supports the selected kit."));
            return;
        }

        TournamentManager.get().startTounament(new Tournament(Practice.get(), kit, arena, min, max));


    }
}
