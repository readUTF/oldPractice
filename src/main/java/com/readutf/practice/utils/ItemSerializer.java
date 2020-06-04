package com.readutf.practice.utils;

import com.readutf.uLib.libraries.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemSerializer {

    public static void serialise(ItemStack itemStack, FileConfiguration config, String key, String path) {
        String s = path + "." + key;

        config.set(s + ".type", itemStack.getType().toString());
        if(itemStack.hasItemMeta()) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            if(itemStack.getItemMeta().getDisplayName() != null) {
                config.set(s + ".name", itemMeta.getDisplayName().replace("§", "&"));
            }
            if(itemMeta.hasLore()) {
                config.set(s + ".lore", itemMeta.getLore());
            }
            if(itemMeta.hasEnchants()) {
                List<String> enchants = new ArrayList<>();
                for(Enchantment enchantment : itemMeta.getEnchants().keySet()) {
                    enchants.add(enchantment.getName() + ":" + itemMeta.getEnchants().get(enchantment));
                }
                config.set(s + ".enchants", enchants);
            }
        }
        config.set(s + ".data", itemStack.getDurability());
        config.set(s + ".amount", itemStack.getAmount());
    }

    public static ItemStack deserialise(ConfigurationSection section) {
        ItemBuilder item = new ItemBuilder(Material.valueOf(section.getString("type")));
        if(section.contains("name")) {
            item.setName(SpigotUtils.color(section.getString("name")));
        }
        if(section.contains("lore")) {
            item.setLore(section.getStringList("lore"));
        }
        if(section.contains("enchants")) {
            for(String s : section.getStringList("enchants")) {
                String[] split = s.split(":");
                item.addEnchant(Enchantment.getByName(split[0]), Integer.parseInt(split[1]));
            }
        }
        if(section.contains("data")) {
            item.setDurability(Short.parseShort("" + section.getInt("data")));
        }
        if (section.contains("amount")) {
            item.setAmount(section.getInt("amount"));
        } else {
            item.setAmount(1);
        }
        return item.toItemStack();
    }

}
