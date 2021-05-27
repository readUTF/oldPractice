package com.readutf.practice.match.inventory;

import com.readutf.practice.match.Match;
import com.readutf.practice.utils.SpigotUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class InventoryCache {

    @Getter @Setter
    private String name;
    @Getter @Setter
    ItemStack[] items;
    @Getter @Setter
    ItemStack[] armour;


    public static BaseComponent getMessage(Player player, Match match) {
        BaseComponent baseComponent = new TextComponent(SpigotUtils.color("&bInventories: &7"));
//        int count = 0;
//        for (InventoryCache inventoryCache : match.getInventoryCaches()) {
//
//            TextComponent textComponent = new TextComponent(inventoryCache.getName());
//            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, TextClickable.get().getCommand(new Runnable() {
//                @Override
//                public void run() {
//                    Menu menu = new Menu(6, ColorUtil.color("&7Inventory Inspect."), true);
//                    ItemStack[] inv = inventoryCache.getItems();
//                    for (int x = 0; x < 9; x++) {
//                        menu.setItem(x + 27, inv[x]);
//                    }
//                    for (int x = 9; x < 36; x++) {
//                        menu.setItem(x - 9, inv[x]);
//                    }
//
//                    for (int x = 0; x < 9; x++) {
//                        menu.setItem(36 + x, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability((short) 7).toItemStack());
//                    }
//
////                                menu.setItem(45, inv.getHelmet());
////                                menu.setItem(46, inv.getChestplate());
////                                menu.setItem(47, inv.getLeggings());
////                                menu.setItem(48, inv.getBoots());
//
//                    ArrayList<String> playerInfoLore = new ArrayList<String>();
//                    menu.setItem(53, new ItemBuilder(Material.BOOK).setName(ColorUtil.color("&7Player Info")).setLore(playerInfoLore).toItemStack());
//                    Practice.get().getInventory().createMenu(UUID.randomUUID().toString(), menu).openMenu(player);
//                }
//            })));
//            count++;
//
//
//            baseComponent.addExtra(textComponent);
//            if (count < match.getInventoryCaches().size()) {
//                baseComponent.addExtra(", ");
//            }
//
//
//        }
        return baseComponent;
    }

}
