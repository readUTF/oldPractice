package com.readutf.practice.tournament;

import com.readutf.practice.Practice;
import com.readutf.practice.arena.Arena;
import com.readutf.practice.kits.Kit;
import com.readutf.practice.match.Match;
import com.readutf.practice.match.MatchManager;
import com.readutf.practice.match.queue.QueueType;
import com.readutf.practice.utils.CountDown;
import com.readutf.practice.utils.SpigotUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Tournament {

    @Getter Practice practice;
    @Getter Kit kit;
    @Setter Arena arena;
    @Getter int round;
    @Getter ArrayList<Player> competitors;
    @Getter ArrayList<Match> tournamentMatches;
    @Getter
    HashMap<Player, AtomicInteger> gamesPlayed;
    @Getter boolean active;
    @Getter long startTime;
    @Getter @Setter boolean inRound;
    @Getter long roundStart;
    @Getter int maxSize;
    @Getter int minSize;

    CountDown countDown;

    @Getter static String prefix = "&7[&6Tournament&7]";


    public Tournament(Practice practice, Kit kit, Arena arena, int minSize, int maxSize) {
        this.practice = practice;
        this.kit = kit;
        this.arena = arena;
        this.maxSize = maxSize;
        this.minSize = minSize;
        competitors = new ArrayList<>();
        tournamentMatches = new ArrayList<>();
        startTime = System.currentTimeMillis();
        gamesPlayed = new HashMap<>();
        round = 0;
        countDown = new CountDown(60, Arrays.asList(60,30,20,10,5,4,3,2,1), SpigotUtils.color(prefix + " &eTournament will commence in {t} seconds."));

        new BukkitRunnable() {

            @Override
            public void run() {


                if(!countDown.isEnded()) {
                    if(competitors.size() >= maxSize) {
                        countDown.setCanceled(true);
                        System.out.println("skipping cooldown");
                    }
                    return;
                }

                if(!active) {
                    if(competitors.size() < minSize) {
                        Bukkit.broadcastMessage(SpigotUtils.color(prefix + " &c&lEvent cancelled because not enough players turned up."));
                        TournamentManager.get().setActiveTournament(null);
                        this.cancel();
                        return;
                    }
                }


                System.out.println(2);
                inRound = tournamentMatches.size() != 0;
                if(competitors.size() <= 1) {
                    Player winner = competitors.get(0);
                    Bukkit.broadcastMessage(SpigotUtils.color(prefix + " &6&l" + winner.getName() + " &ehas finessed everyone and &6won!"));
                    Bukkit.broadcastMessage(SpigotUtils.color(prefix + " &6&l" + winner.getName() + " &ehas finessed everyone and &6won!"));
                    Bukkit.broadcastMessage(SpigotUtils.color(prefix + " &6&l" + winner.getName() + " &ehas finessed everyone and &6won!"));
                    TournamentManager.get().setActiveTournament(null);
                    this.cancel();
                    return;
                }
                System.out.println(3);
                if(!inRound) {
                    System.out.println(4);
                    if(competitors.size() % 2 != 0) {
                        for(int x = 0; x < competitors.size() - 1; x+=2) {
                            Match match = MatchManager.get().startMatch(Arrays.asList(competitors.get(x)), Arrays.asList(competitors.get(x + 1)), arena, kit, QueueType.TOURNAMENT, null);
                            tournamentMatches.add(match);
                        }
                        Player leftover = competitors.get(competitors.size() - 1);
                        leftover.sendMessage(SpigotUtils.color("&cCould not find an opponent, please wait for the next round."));
                        Collections.shuffle(competitors);
                        inRound = true;
                        round++;
                        active = true;
                    } else {
                        System.out.println(5);
                        for(int x = 0; x < competitors.size(); x+=2) {
                            System.out.println("-->" + x);

                            Bukkit.broadcastMessage("fight - " + competitors.get(x).getName() + ", " + competitors.get(x + 1).getName());


                            Match match = MatchManager.get().startMatch(Arrays.asList(competitors.get(x)), Arrays.asList(competitors.get(x + 1)), arena, kit, QueueType.TOURNAMENT, null);
                            tournamentMatches.add(match);
                        }
                        Collections.shuffle(competitors);
                        inRound = true;
                        round++;
                        active = true;
                    }
                }






            }
        }.runTaskTimer(practice, 0L, 20L);

    }

    public boolean inTounament(Player player) {
        return competitors.contains(player);
    }

    public void addPlayer(Player player) {

        competitors.add(player);
        gamesPlayed.put(player, new AtomicInteger(0));
    }




}
