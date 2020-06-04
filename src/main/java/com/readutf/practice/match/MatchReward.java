package com.readutf.practice.match;

import org.bukkit.entity.Player;

public abstract class MatchReward {

    public abstract void onWin(Player player);
    public abstract void onLose(Player player);

}
