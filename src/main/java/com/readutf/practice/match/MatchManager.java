package com.readutf.practice.match;

import com.readutf.practice.Practice;
import com.readutf.practice.arena.Arena;
import com.readutf.practice.kits.Kit;
import com.readutf.practice.match.listeners.MatchListeners;
import com.readutf.practice.match.listeners.VisibilityHandler;
import com.readutf.practice.match.queue.QueueManager;
import com.readutf.practice.match.queue.QueueType;
import com.readutf.practice.profiles.Profile;
import com.readutf.practice.utils.Cooldown;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.duration.DurationFormatter;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Stream;

public class MatchManager implements Listener {

    public ArrayList<Match> matches = new ArrayList<>();
    private static MatchManager matchManager;
    @Getter
    private QueueManager queueManager;



    public MatchManager(Practice practice) {
        matchManager = this;
        queueManager = new QueueManager(practice);
        Arrays.asList(
                new MatchListeners(),
                new VisibilityHandler()
        ).forEach(o -> Bukkit.getPluginManager().registerEvents(o, practice));

        new BukkitRunnable() {

            @Override
            public void run() {
                Iterator<Match> matchIterator = matches.iterator();
                while (matchIterator.hasNext()) {
                    Match match = matchIterator.next();

                    Stream.concat(match.getTeam1().stream(), match.getTeam2().stream()).forEach(player -> {
                        if(Cooldown.isInCooldown(player.getUniqueId(), "enderpearl")) {

                           float seconds =  (Cooldown.getTimeLeft(player.getUniqueId(), "enderpearl") / 1000f);


                            player.setLevel((int) seconds);
                            player.setExp(seconds / 16f);


                        }
                    });
                }
            }
        }.runTaskTimer(practice, 0L, 2L);


    }

    public static MatchManager get() {
        return matchManager;
    }

    public int getInMatch(QueueType queueType, Kit kit) {
        int inmatch = 0;
        for (Match match : matches) {
            if (match.getQueueType() == queueType) {
                if (match.getKit() == kit) {
                    inmatch++;
                }
            }
        }
        return inmatch;
    }


    public Match startMatch(List<Player> team1, List<Player> team2, Arena arena, Kit kit, QueueType queueType, MatchReward matchReward) {
        if (team1 == null || team2 == null || arena == null || kit == null) return null;
        if (!arena.isSetup()) return null;

        //check if a player is playing against himself.
        team1.forEach(o -> {
            if (team2.contains(o)) {
                return;
            }
        });

        Stream.concat(team1.stream(), team2.stream()).forEach(player -> {
            Profile profile = Profile.getUser(player.getUniqueId());
            profile.setGamesPlayed(profile.getGamesPlayed() + 1);
        });

        Match match = new Match(team1, team2, arena, kit, queueType, matchReward);
        matches.add(match);

        return match;
    }

    public int getQueued() {
        return matches.size();
    }
}
