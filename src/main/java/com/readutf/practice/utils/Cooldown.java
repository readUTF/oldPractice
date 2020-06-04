package com.readutf.practice.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
public class Cooldown {

    private static Map<String, Cooldown> cooldowns = new HashMap<String, Cooldown>();
    private long start;
    private final int timeInSeconds;
    private final UUID id;
    private final String cooldownName;

    public Cooldown(UUID id, String cooldownName, int timeInSeconds){
        this.id = id;
        this.cooldownName = cooldownName;
        this.timeInSeconds = timeInSeconds * 1000;
    }

    public static boolean isInCooldown(UUID id, String cooldownName){
        if(getTimeLeft(id, cooldownName)>=1){
            return true;
        } else {
            stop(id, cooldownName);
            return false;
        }
    }

    private static void stop(UUID id, String cooldownName){
        Cooldown.cooldowns.remove(id+cooldownName);
    }

    private static Cooldown getCooldown(UUID id, String cooldownName){
        return cooldowns.get(id.toString()+cooldownName);
    }

    public static long getTimeLeft(UUID id, String cooldownName){
        Cooldown cooldown = getCooldown(id, cooldownName);
        long f = -1;
        if(cooldown!=null){
            long now = System.currentTimeMillis();
            long cooldownTime = cooldown.start;
            long totalTime = cooldown.timeInSeconds;
            long r = (now - cooldownTime);
            f = (r - totalTime) * (-1);
        }
        return f;
    }

    public void start(){
        this.start = System.currentTimeMillis();
        cooldowns.put(this.id.toString()+this.cooldownName, this);
    }

}