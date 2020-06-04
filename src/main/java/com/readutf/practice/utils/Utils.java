package com.readutf.practice.utils;

import com.readutf.practice.match.Match;
import com.readutf.practice.profiles.GameState;
import com.readutf.practice.profiles.Profile;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Utils {

    public static double lookAtPlayer(Player player, Player target) {


//        Player calculatingFrom =
//
//        double difx = Math.max(player.getLocation().getX(), target.getLocation().getBlockX()) - Math.min(player.getLocation().getX(), target.getLocation().getBlockX());
//        double difz = Math.max(player.getLocation().getX(), target.getLocation().getBlockX()) - Math.min(player.getLocation().getX(), target.getLocation().getBlockX());
//
//        double hyp = Math.sqrt(Math.pow(difx, 2) + Math.pow(difz, 2));
//
//        double angle = Math.asin(difx - hyp);
//
//        Location location = player.getLocation().clone();
//        location.setYaw((float) (location.getYaw() + angle));
//
//        player.teleport(location);

        Location one = player.getLocation();
        Location two = target.getLocation();

        return Math.atan2(one.getX() - two.getX(), one.getZ() - two.getZ()) * 180 / Math.PI;


    }


    public static void resetPlayer(Player player) {
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setFireTicks(0);
    }

    public static boolean inMatch(Player player) {
        Profile profile = Profile.getUser(player.getUniqueId());
        if(profile == null) return false;
        if(profile.getGameState() != GameState.FIGHTING) return false;
        if(profile.getActiveMatch() == null) return false;
        return true;
    }

    public static boolean isSpectator(Player player) {
        Profile profile = Profile.getUser(player.getUniqueId());
        if(profile == null) return false;
        if(profile.getGameState() != GameState.SPECTATING) return false;
        return true;
    }
    public static boolean inLobby(Player player) {
        Profile profile = Profile.getUser(player.getUniqueId());
        if(profile == null) return false;
        if(profile.getGameState() == GameState.LOBBY) return true;
        if(profile.getGameState() == GameState.QUEUEING) return true;
        return true;
    }

}
