package com.readutf.practice.arena;

import com.readutf.practice.Practice;
import com.readutf.practice.kits.Kit;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import sun.reflect.generics.tree.Tree;

import java.util.*;

public class ArenaManager {

    JavaPlugin plugin;
    private  static ArenaManager arenaManager;

    @Getter  ArrayList<Arena> arenas;

    public ArenaManager(JavaPlugin plugin) {
        this.plugin = plugin;
        arenaManager = this;

        arenas = new ArrayList<Arena>();
    }

    public Arena createArena(String name) {
        Arena arena = new Arena(name);
        arenas.add(arena);
        return arena;
    }

    public void removeArena(String name) {
        if(arenaExists(name)) {
            arenas.remove(getArenaByName(name));
        }

        plugin.getConfig().set(name, null);
        plugin.saveConfig();

    }

    public Arena getArenaByName(String name) {
        for(Arena arena : arenas) {
            if(arena.getName().equalsIgnoreCase(name)) {
                return arena;
            }
        }
        return null;
    }

    public Arena getNextArena(Kit kit) {
        if(kit == null) return null;

        TreeMap<Integer, Arena> arenas = new TreeMap<>();

        for(Arena arena : ArenaManager.get().getArenas()) {
            arenas.put(arena.getActiveMatches(), arena);
        }

        System.out.println(arenas.firstEntry());
        System.out.println(arenas.lastEntry());


        return arenas.firstEntry().getValue();
    }

    public boolean arenaExists(String name) {
        return getArenaByName(name) != null;
    }

    public static ArenaManager get() {
        return arenaManager;
    }

    public void save() {
        FileConfiguration config = Practice.get().getArena();

        for(String s : config.getKeys(false)) {
            config.set(s, null);
        }


        for(Arena arena : arenas) {
            Location spawn1 = arena.getSpawn1();
            Location spawn2 = arena.getSpawn2();

            System.out.println("saving: " + arena.getName());


            config.set(arena.getName() + ".name", arena.getName());

            if(arena.getArenaRegion() != null) {
                Location max = arena.getArenaRegion().getMaximumPoint();
                Location min = arena.getArenaRegion().getMinimumPoint();

                if(max != null) {
                    config.set(arena.getName() + ".region.max.x", max.getBlockX());
                    config.set(arena.getName() + ".region.max.y", max.getBlockY());
                    config.set(arena.getName() + ".region.max.z", max.getBlockZ());
                    config.set(arena.getName() + ".region.max.world", max.getWorld().getName());
                }

                if(min != null) {
                    config.set(arena.getName() + ".region.min.x", min.getBlockX());
                    config.set(arena.getName() + ".region.min.y", min.getBlockY());
                    config.set(arena.getName() + ".region.min.z", min.getBlockZ());
                    config.set(arena.getName() + ".region.min.world", min.getWorld().getName());
                }
            }

            if(arena.getAllowedKits() != null) {
                config.set(arena.getName() + ".allowedkits", arena.getAllowedKits());
            }

            if(spawn1 != null) {
                config.set(arena.getName() + ".spawn1.x", spawn1.getBlockX());
                config.set(arena.getName() + ".spawn1.y", spawn1.getBlockY());
                config.set(arena.getName() + ".spawn1.z", spawn1.getBlockZ());
                config.set(arena.getName() + ".spawn1.world", spawn1.getWorld().getName());
            }

            if(spawn2 != null) {
                config.set(arena.getName() + ".spawn2.x", spawn2.getBlockX());
                config.set(arena.getName() + ".spawn2.y", spawn2.getBlockY());
                config.set(arena.getName() + ".spawn2.z", spawn2.getBlockZ());
                config.set(arena.getName() + ".spawn2.world", spawn2.getWorld().getName());
            }
        }
        Practice.get().getArenaConfig().saveConfig();
    }

    public void load() {
        FileConfiguration config = Practice.get().getArena();
        for(String s : config.getKeys(false)) {
            Arena arena = ArenaManager.get().createArena(s);

            Location max = null;
            if(config.getString(s + ".region.max.x") != null) {
                 max = new Location(Bukkit.getWorld(config.getString(s + ".region.max.world")),
                        config.getDouble(s + ".region.max.x"),
                        config.getDouble(s + ".region.max.y"),
                        config.getDouble(s + ".region.max.z"));
            }
            Location min = null;
            if(config.getString(s + ".region.min.x") != null) {
                 min = new Location(Bukkit.getWorld(config.getString(s + ".region.min.world")),
                        config.getDouble(s + ".region.min.x"),
                        config.getDouble(s + ".region.min.y"),
                        config.getDouble(s + ".region.min.z"));
            }
            if(max != null && min != null) {
                arena.setArenaRegion(new CuboidSelection(Bukkit.getWorld(config.getString(s + ".region.max.world")),
                        max,
                        min));
            }

            if(config.isSet(s + ".spawn1.x")) {
                Location location = new Location(Bukkit.getWorld(config.getString(s + ".spawn1.world")),
                        config.getInt(s + ".spawn1.x"),
                        config.getInt(s + ".spawn1.y"),
                        config.getInt(s + ".spawn1.z"));
                arena.setSpawn1(location);
            }

            if(config.isSet(s + ".allowedkits")) {
                arena.setAllowedKits(config.getStringList(s + ".allowedkits"));
            }

            if(config.isSet(s + ".spawn2.x")) {
                Location location = new Location(Bukkit.getWorld(config.getString(s + ".spawn1.world")),
                        config.getInt(s + ".spawn2.x"),
                        config.getInt(s + ".spawn2.y"),
                        config.getInt(s + ".spawn2.z"));
                arena.setSpawn2(location);
            }






        }
    }

}
