package com.readutf.practice.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class LocationSerialiser {

    public static Location deserialize(ConfigurationSection cs) {
        double x = cs.getDouble("x");
        double y = cs.getDouble("y");
        double z = cs.getDouble("z");
        float yaw = cs.getInt("yaw");
        float pitch = cs.getInt("pitch");
        World world = Bukkit.getWorld(cs.getString("world"));
        return new Location(world, x,y,z,yaw,pitch);
    }

    public static void serialise(Location location, FileConfiguration config, String key) {
        config.set(key + ".world", location.getWorld().getName());
        config.set(key + ".x", location.getX());
        config.set(key + ".y", location.getY());
        config.set(key + ".z", location.getZ());
        config.set(key + ".yaw", location.getYaw());
        config.set(key + ".pitch", location.getPitch());
    }

}
