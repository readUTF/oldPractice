package com.readutf.practice.utils.nametags;

import com.readutf.uLib.libraries.Players;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter @Setter
public class NametagHandler {

    private JavaPlugin plugin;
    private NametagAdapter adapter;
    private Map<UUID, NametagBoard> boards;
    private NametagThread thread;
    private NametagListeners listeners;
    private long ticks = 2;
    private boolean hook = false;


    public NametagHandler(JavaPlugin plugin, NametagAdapter adapter) {
        if (plugin == null) {
            throw new RuntimeException("Nametag Handler can not be instantiated without a plugin instance!");
        }

        this.plugin = plugin;
        this.adapter = adapter;
        this.boards = new ConcurrentHashMap<>();

        this.setup();
    }

    public void setup() {
        //Register Events
        this.listeners = new NametagListeners(this);
        this.plugin.getServer().getPluginManager().registerEvents(this.listeners, this.plugin);

        // Ensure that the thread has stopped running
        if (this.thread != null) {
            this.thread.stop();
            this.thread = null;
        }

        for (Player player : Players.getOnlinePlayers()) {
            //Call Event
            getBoards().putIfAbsent(player.getUniqueId(), new NametagBoard(player, this));
        }

        this.thread = new NametagThread(this);
    }

    public void cleanup() {
        if (this.thread != null) {
            this.thread.stop();
            this.thread = null;
        }

        if (this.listeners != null) {
            HandlerList.unregisterAll(this.listeners);
            this.listeners = null;
        }

        for (UUID uuid : getBoards().keySet()) {
            Player player = Bukkit.getPlayer(uuid);

            if (player == null || !player.isOnline()) {
                continue;
            }

            getBoards().remove(uuid);
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
    }

}
