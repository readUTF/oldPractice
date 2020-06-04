package com.readutf.practice.kits;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.IntStream;

@AllArgsConstructor
public class Kit {

    @Getter @Setter private String name;
    @Getter @Setter private ItemStack[] items;
    @Getter @Setter private ItemStack[] armour;
    @Getter @Setter private boolean building;
    @Getter @Setter private boolean combo;
    @Getter @Setter private boolean sumo;
    @Getter @Setter private boolean quake;
    @Getter @Setter private ItemStack fill;

    @Getter @Setter ItemStack icon;

    HashMap<UUID, ItemStack[]> customItems;
    HashMap<UUID, ItemStack[]> customArmour;

    public void applyPlayer(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[]{});
        player.getInventory().setContents(items);
        player.getInventory().setArmorContents(armour);

        IntStream.rangeClosed(0, 35).forEach(i -> {
            if(player.getInventory().getItem(i) == null)  player.getInventory().setItem(i, fill);;
        });
    }


}
