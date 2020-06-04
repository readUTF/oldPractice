package com.readutf.practice.match.rewards;

import com.readutf.practice.match.MatchReward;
import com.readutf.practice.profiles.Profile;
import com.readutf.practice.utils.SpigotUtils;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class WagerReward extends MatchReward {

    int coins;

    @Override
    public void onWin(Player player) {
        player.sendMessage(SpigotUtils.color("&aYou won! " + coins + " have been added to your balance!"));
        Profile profile = Profile.getUser(player.getUniqueId());
        profile.addCoins(coins);
    }

    @Override
    public void onLose(Player player) {
        Profile profile = Profile.getUser(player.getUniqueId());
        player.sendMessage(SpigotUtils.color("&aYou were defeated! " + coins + " have been removed from your balance."));
        profile.removeCoins(coins);
    }
}
