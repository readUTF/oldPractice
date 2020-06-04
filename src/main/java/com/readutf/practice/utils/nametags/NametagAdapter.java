package com.readutf.practice.utils.nametags;

import org.bukkit.entity.Player;

import java.util.List;

public interface NametagAdapter {

    List<BufferedNametag> getPlate(Player player);

    boolean showHealthBelowName(Player player);
}
