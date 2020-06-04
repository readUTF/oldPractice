package com.readutf.practice.tournament;

import com.readutf.practice.Practice;
import com.readutf.practice.arena.Arena;
import com.readutf.practice.kits.Kit;
import lombok.Getter;
import lombok.Setter;

public class TournamentManager {

    private Practice practice;
    private static TournamentManager tournamentManager;
    @Getter @Setter private Tournament activeTournament;

    public TournamentManager(Practice practice) {
        this.tournamentManager = this;
        this.practice = practice;
    }

    public void startTounament(Tournament tournament) {
        if(activeTournament != null) {
            return;
        }

        activeTournament = tournament;
    }

    public static TournamentManager get() {
        return tournamentManager;
    }

}
