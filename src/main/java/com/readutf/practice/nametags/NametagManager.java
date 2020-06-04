package com.readutf.practice.nametags;

import com.readutf.practice.Practice;
import com.readutf.practice.profiles.GameState;
import com.readutf.practice.profiles.Profile;
import com.readutf.practice.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class NametagManager {

    public NametagManager() {

//        new BukkitRunnable() {
//            @Override
//            public void run() {
//
//                for (Player p : Bukkit.getOnlinePlayers()) {
//                    try {
//                        Objective obj = null;
//                        Scoreboard sb = p.getScoreboard();
//                        Profile profile = Profile.getUser(p.getUniqueId());
//                        if (sb == null) {
//                            sb = Bukkit.getScoreboardManager().getNewScoreboard();
//                            p.setScoreboard(sb);
//                        }
//                        if ((obj = sb.getObjective("names")) == null) {
//                            sb.registerNewObjective("names", "dummy");
//                        }
//                        for (Player p2 : Bukkit.getOnlinePlayers()) {
//                            if (profile.getGameState() != GameState.LOBBY) {
//                                if (profile.checkFriendly(p2)) {
//                                    Team t = sb.getTeam(p2.getName());
//                                    if (t == null) {
//                                        t = sb.registerNewTeam(p2.getName());
//                                    }
//                                    t.setPrefix("§9");
//                                } else {
//                                    Team t = sb.getTeam(p2.getName());
//                                    if (t == null) {
//                                        t = sb.registerNewTeam(p2.getName());
//                                    }
//                                    t.setPrefix("§4");
//                                }
//                            } else {
//                                Team t = sb.getTeam(p2.getName());
//                                if (t == null) {
//                                    t = sb.registerNewTeam(p2.getName());
//                                }
//                                t.setPrefix("§4");
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.runTaskTimerAsynchronously(Practice.get(), 10L, 10L);





    }

    public Team getTeam(Scoreboard sb, String team) {
        if (sb.getTeam(team) == null) {
            return sb.registerNewTeam(team);
        } else {
            return sb.getTeam(team);
        }
    }

}
