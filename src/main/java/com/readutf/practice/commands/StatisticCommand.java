package com.readutf.practice.commands;

import com.readutf.practice.profiles.Profile;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.ItemBuilder;
import com.readutf.uLib.libraries.menu.Menu;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class StatisticCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String c, String[] args) {



        return true;
    }



    public void openMenu(Player player) {

        Menu menu = new Menu(4, "Statistics", true);
        Profile profile = Profile.getUser(player.getUniqueId());


        List<String> lore1 = new ArrayList<>();
        for(String s : profile.getElo().keySet()) {
            lore1.add("&c" +s + " &7(" + profile.getElo().get(s) + ")");
        }
        menu.setItem(4, new ItemBuilder(Material.SKULL_ITEM)
                .setName("&b&o" + player.getName() + " &rStatistics")
                .setSkullOwner(player.getName())
                .setLore(SpigotUtils.colorArray(lore1))
                .toItemStack());


        List<String> lore2 = new ArrayList<>();
        for(String s : profile.getElo().keySet()) {
            lore2.add("&c" +s + " &7(" + profile.getElo().get(s) + ")");
        }
        menu.setItem(4, new ItemBuilder(Material.SKULL_ITEM)
                .setName("&b&o" + player.getName() + " &rStatistics")
                .setSkullOwner(player.getName())
                .setLore(SpigotUtils.colorArray(lore2))
                .toItemStack());



    }

}
