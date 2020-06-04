package com.readutf.practice.utils;

import com.readutf.uLib.libraries.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class ItemStackSerializer {

    public static String serialise(ItemStack item) {

        if(item == null) {
            return "--";
        }

        StringBuilder str = new StringBuilder();

        str.append(item.getType().toString() + "@@");
        str.append(item.getAmount()).append("@@");
        str.append(item.getItemMeta().getDisplayName()).append("@@");
        str.append(item.getDurability()).append("@@");
        StringBuilder enchants = new StringBuilder();
        for(Enchantment enchantment : item.getEnchantments().keySet()) {
            enchants.append(enchantment.getName()).append(",,").append(item.getEnchantments().get(enchantment)).append("::");
        }
        str.append(enchants.toString());
        return str.toString();
    }

    public static ItemStack deseralise(String s) {

        if (s.equalsIgnoreCase("--")) {
            return new ItemBuilder(Material.AIR).toItemStack();
        }

        String[] parts = s.split("@@");

        ItemBuilder itemStack = new ItemBuilder(Material.valueOf(parts[0]))
                .setAmount(SpigotUtils.parseInt(parts[1]))
                .setDurability(Short.parseShort(parts[3]));

        if(!parts[2].equalsIgnoreCase("null")) {
            itemStack.setName(parts[2]);
        }

        if(parts.length > 4) {
            String enchantString = parts[4];
            String[] enchants = enchantString.split("::");
            HashMap<Enchantment, Integer> enchantMap = new HashMap<>();
            for(String enchant : enchants) {
                String[] values = enchant.split(",,");
                enchantMap.put(Enchantment.getByName(values[0]), SpigotUtils.parseInt(values[1]));
            }

            itemStack.setEnchants(enchantMap);
        }
        return itemStack.toItemStack();
    }

}
