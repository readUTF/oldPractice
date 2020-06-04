package com.readutf.practice.match.queue;

import com.readutf.practice.arena.Arena;
import com.readutf.practice.arena.ArenaManager;
import com.readutf.practice.kits.Kit;
import com.readutf.practice.match.MatchManager;
import com.readutf.practice.profiles.Profile;
import com.readutf.practice.utils.SpigotUtils;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.*;

public class Queue {

    @Getter
    Kit kit;
    @Getter
    List<Player> players;
    @Getter
    QueueType queueType;
    @Getter HashMap<Player, Long> startTimes;

    public Queue(Kit kit, QueueType queueType) {
        this.kit = kit;
        players = new ArrayList<>();
        this.queueType = queueType;
        startTimes = new HashMap<>();
    }

    public void addPlayer(Player player) {
        startTimes.put(player, System.currentTimeMillis());
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public void tick() {
        List<Player> players = new ArrayList<>(this.players);
        if (queueType == QueueType.RANKED) {
            players.sort(Comparator.comparingLong(player -> {
                Profile profile = Profile.getUser(player.getUniqueId());
                return profile.getElo().get(kit);
            }));

        }
        if (players.size() >= 2) {
            Player one = players.get(0);
            Player two = players.get(1);
            if (one == two) {
                players.remove(0);
                return;
            }
            players.remove(0);
            players.remove(0);

            this.players = players;

            Arena arena = ArenaManager.get().getNextArena(kit);
            if(arena == null) {
                removePlayer(one);
                removePlayer(two);
                one.sendMessage(SpigotUtils.color("&cCould not find an arena, please try again."));
                two.sendMessage(SpigotUtils.color("&cCould not find an arena, please try again."));
                return;
            }

            one.sendMessage(SpigotUtils.color("&eMatch found against &6" + two.getName()));
            two.sendMessage(SpigotUtils.color("&eMatch found against &6" + one.getName()));
            MatchManager.get().startMatch(Collections.singletonList(one), Collections.singletonList(two), arena, kit, queueType, null);
        }
    }



    public boolean isQueued(Player player) {
        return players.contains(player);
    }

}
