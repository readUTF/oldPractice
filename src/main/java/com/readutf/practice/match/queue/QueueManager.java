package com.readutf.practice.match.queue;

import com.readutf.practice.Practice;
import com.readutf.practice.arena.Arena;
import com.readutf.practice.arena.ArenaManager;
import com.readutf.practice.kits.Kit;
import com.readutf.practice.profiles.GameState;
import com.readutf.practice.profiles.Profile;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.ItemBuilder;
import com.readutf.uLib.libraries.clickables.Clickable;
import com.readutf.uLib.libraries.clickables.ClickableManager;
import com.readutf.uLib.libraries.menu.ItemClick;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class QueueManager {

    HashMap<QueueType, ArrayList<Queue>> queueTypes;

    public QueueManager(Practice practice) {
        this.queueTypes = new HashMap<>();
        for(QueueType queueType: QueueType.values()) {
            queueTypes.put(queueType, new ArrayList<>());
        }


        new BukkitRunnable() {
            @Override
            public void run() {
                queueTypes.keySet().forEach(queueType -> {
                    queueTypes.get(queueType).forEach(queue -> {
                        queue.tick();
                    });
                });
            }
        }.runTaskTimer(practice, 0L, 20L);

    }

    public Queue getQueue(Player player) {
        Queue foundqQueue = null;
        for(ArrayList<Queue> queueType : queueTypes.values()) {
            for(Queue queue : queueType) {
                if(queue.isQueued(player)) return queue;
            }
        }
        return null;
    }

    public void removeFromQueue(Player player) {
        Queue queue = getQueue(player);
        if(queue == null) return;

        queue.removePlayer(player);
        Practice.get().getLobbyManager().spawn(player, true, false);

    }


    public boolean addToQueue(Kit kit, Player player, QueueType type) {

        Arena arena = ArenaManager.get().getNextArena(kit);

        if (arena == null) {
            player.sendMessage(SpigotUtils.color("&cCould not find an arena."));
            return false;
        }

        Profile profile = Profile.getUser(player.getUniqueId());

        Queue queue = getQueue(type, kit);
        if(queue == null) {
            player.sendMessage(SpigotUtils.color("&cAn error occured finding a queue."));
            return false;
        }

        System.out.println("adding player to queue....");
        queue.addPlayer(player);
        System.out.println(SpigotUtils.color("player added to queue"));

        profile.setGamesPlayed(profile.getGamesPlayed() + 1);
        profile.setQueue(queue);
        profile.setGameState(GameState.QUEUEING);

        player.getInventory().clear();

        ClickableManager.get().giveClickable(player, new Clickable(new ItemBuilder(Material.REDSTONE).setName(SpigotUtils.color("&cLeave Queue")).toItemStack(),
                new ItemClick(99) {
                    @Override
                    public void itemClick(Player player) {
                        if (profile.getQueue() == null) {
                            return;
                        }

                        profile.setQueue(null);
                        profile.setGameState(GameState.LOBBY);
                        Practice.get().getLobbyManager().spawn(player, true, false);
                        getQueue(player).getPlayers().remove(player);

                    }
                }, Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK)), 0);

        return true;
    }

    public Queue getQueue(QueueType queueType, Kit kit) {
        for(Queue queue : queueTypes.get(queueType)) {
            if(queue.getKit() == kit) {
                return queue;
            }
        }
        ArrayList<Queue> queues = queueTypes.get(queueType);
        Queue queue = new Queue(kit, queueType);
        queues.add(queue);
        queueTypes.put(queueType, queues);
        return queue;
    }

}
