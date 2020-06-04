package com.readutf.practice.kits;

import com.readutf.practice.Practice;
import com.readutf.practice.utils.ItemSerializer;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.ColorUtil;
import com.readutf.uLib.libraries.ItemBuilder;
import com.readutf.uLib.libraries.menu.ItemClick;
import com.readutf.uLib.libraries.menu.Menu;
import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class KitManager {

    JavaPlugin plugin;
    private static KitManager instance;
    @Getter HashMap<String, Kit> kits;

    public KitManager(Practice plugin) {
        this.plugin = plugin;
        instance = this;
        kits = new HashMap<String, Kit>();

        FileConfiguration config = plugin.getKit();
        for(String s : config.getKeys(false)) {
            String name = config.getString(s + ".name");

            ItemStack[] kitItems = new ItemStack[36];
            ItemStack[] armour = new ItemStack[4];
            boolean combo = config.getBoolean(s + ".settings.combo");
            boolean building = config.getBoolean(s + ".settings.building");
            boolean sumo = config.getBoolean(s + ".settings.building");
            boolean quake = config.getBoolean(s + ".settings.quake");

            ItemStack icon = ItemSerializer.deserialise(config.getConfigurationSection(s + ".icon"));
            if(icon == null) {
                icon = new ItemBuilder(Material.PAPER)
                        .setName(ColorUtil.color("&cInvalid Icon"))
                        .toItemStack();
            }

            ItemStack fill = null;
            if(config.isSet(s + ".fill")) {
                fill = ItemSerializer.deserialise(config.getConfigurationSection(s + ".fill"));
            }

            for(int x = 0; x < 36; x++) {

                if(!config.isConfigurationSection(s + ".items." + x)) {
                    continue;
                }
                kitItems[x] = ItemSerializer.deserialise(config.getConfigurationSection( s + ".items." + x));
            }

            if(config.isConfigurationSection(s + ".armour.helmet")) {
                armour[3] = ItemSerializer.deserialise(config.getConfigurationSection(s + ".armour.helmet"));
            }
            if(config.isConfigurationSection(s + ".armour.chestplate")) {
                armour[2] = ItemSerializer.deserialise(config.getConfigurationSection(s + ".armour.chestplate"));
            }
            if(config.isConfigurationSection(s + ".armour.leggings")) {
                armour[1] = ItemSerializer.deserialise(config.getConfigurationSection(s + ".armour.leggings"));
            }
            if(config.isConfigurationSection(s + ".armour.boots")) {
                armour[0] = ItemSerializer.deserialise(config.getConfigurationSection(s + ".armour.boots"));
            }

            kits.put(name, new Kit(name, kitItems,armour, building, combo, sumo, quake, fill, icon, new HashMap<>(), new HashMap<>()));
        }
    }

    public Kit getKitByName(String s) {
        if(kits.containsKey(s)) {
            return kits.get(s);
        }
        return null;
    }

    public void openKitSelector(Player player, ItemClick click) {
        Menu menu = new Menu(SpigotUtils.roundToNine(KitManager.get().getKits().size()) / 9, ColorUtil.color("&eSelect A kit"), true);
        int x = 0;
        for (Kit kit : KitManager.get().getKits().values()) {

            menu.setItem(x, kit.getIcon(), click);
            x++;
        }
        Practice.get().getInventory().createMenu(UUID.randomUUID().toString(), menu).openMenu(player);
    }

    public static KitManager get() {
        return instance;
    }

    public Kit getFirstKit() {
        Collection<Kit> kits = getKits().values();
        Kit[] kitArray = kits.toArray(new Kit[kits.size()]);
        return kitArray[0];
    }

}
