package com.readutf.practice.arena.menu;

import com.readutf.practice.Practice;
import com.readutf.practice.arena.Arena;
import com.readutf.practice.arena.ArenaManager;
import com.readutf.practice.kits.Kit;
import com.readutf.practice.profiles.Profile;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.ItemBuilder;
import com.readutf.uLib.libraries.menu.ItemClick;
import com.readutf.uLib.libraries.menu.Menu;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.stream.IntStream;

public class OpenMenu {

    public static void openMenu(Player player, Kit kit, Player target) {

        Menu menu = new Menu((SpigotUtils.roundToNine(ArenaManager.get().getArenas().size()) /9)+ 2, "&bSelect An Arena.", true);

        System.out.println(ArenaManager.get().getArenas());
        for(int x = 0; x < ArenaManager.get().getArenas().size(); x++) {
            Arena arena = ArenaManager.get().getArenas().get(x);
            menu.setItem(x, new ItemBuilder(Material.PAPER).setName(SpigotUtils.color("&7* &c" + arena.getName() + " &7*")).toItemStack(), new ItemClick() {
                @Override
                public void itemClick(Player player) {
                    BaseComponent baseComponent = new TextComponent(SpigotUtils.color("&7(Click To Accept)"));
                    baseComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duel accept " + player.getName()));

                    //get who dualing and add the senders name to

                    Profile targetProfile = Profile.getUser(target.getUniqueId());
                    targetProfile.inviteToDuel(player, kit, arena);


                    target.sendMessage(SpigotUtils.color("&6" + player.getName() + " &ehas requested to duel you in &6" + kit.getName() + "."));
                    target.spigot().sendMessage(baseComponent);
                    player.closeInventory();
                    player.sendMessage(SpigotUtils.color("&aDuel requests sent."));
                }
            });

            IntStream.rangeClosed(0, 8).forEach(i -> {
                menu.setItem(SpigotUtils.roundToNine(ArenaManager.get().getArenas().size()) + i, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability((short) 8).toItemStack());
            });

            menu.setItem(SpigotUtils.roundToNine(ArenaManager.get().getArenas().size()) + 13, new ItemBuilder(Material.FIREWORK).toItemStack(),
                    new ItemClick() {
                        @Override
                        public void itemClick(Player player) {
                            BaseComponent baseComponent = new TextComponent(SpigotUtils.color("&7(Click To Accept)"));
                            baseComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duel accept " + player.getName()));

                            //get who dualing and add the senders name to

                            Profile targetProfile = Profile.getUser(target.getUniqueId());
                            targetProfile.inviteToDuel(player, kit);


                            target.sendMessage(SpigotUtils.color("&6" + player.getName() + " &ehas requested to duel you in &6" + kit.getName() + "."));
                            target.spigot().sendMessage(baseComponent);
                            player.closeInventory();
                            player.sendMessage(SpigotUtils.color("&aDuel requests sent."));
                        }
                    });

        }
        Practice.get().getInventory().createMenu(UUID.randomUUID().toString(), menu).openMenu(player);
    }

}
