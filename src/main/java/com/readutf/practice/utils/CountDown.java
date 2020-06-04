package com.readutf.practice.utils;

import com.readutf.practice.Practice;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;

public class CountDown {

    @Getter @Setter boolean canceled = false;
    @Getter boolean ended = false;
    long start;

    public CountDown(int duration, List<Integer> intervals, String format) {
        start = System.currentTimeMillis();
        HashMap<Integer, Boolean> hasSent = new HashMap<>();

        for(Integer x : intervals) {
            hasSent.put(x, false);
        }

        new BukkitRunnable() {

            @Override
            public void run() {
                System.out.println("ran");
                int pastTime = (int) (System.currentTimeMillis() - start) / 1000;
                int time = duration - pastTime;
                System.out.println(time);

                for(Integer x : hasSent.keySet()) {
                    if(!hasSent.get(x) && time <= x) {
                        hasSent.put(x, true);
                        Bukkit.broadcastMessage(SpigotUtils.color(format.replace("{t}", x + "")));
                    }
                }

                if(time <= 0) {
                    ended = true;
                    this.cancel();;
                }



            }
        }.runTaskTimer(Practice.get(), 0L, 20L);


    }
}
