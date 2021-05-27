package com.readutf.practice.lobby;

import com.readutf.practice.Practice;
import com.readutf.practice.kits.Kit;
import com.readutf.practice.kits.KitManager;
import com.readutf.practice.lobby.listeners.PlayerJoin;
import com.readutf.practice.lobby.listeners.ProtectionListeners;
import com.readutf.practice.match.MatchManager;
import com.readutf.practice.match.queue.Queue;
import com.readutf.practice.match.queue.QueueType;
import com.readutf.practice.party.Party;
import com.readutf.practice.profiles.GameState;
import com.readutf.practice.profiles.Profile;
import com.readutf.practice.settings.Setting;
import com.readutf.practice.settings.SettingsManager;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.ColorUtil;
import com.readutf.uLib.libraries.ItemBuilder;
import com.readutf.uLib.libraries.PlayerUtil;
import com.readutf.uLib.libraries.Players;
import com.readutf.uLib.libraries.clickables.Clickable;
import com.readutf.uLib.libraries.clickables.ClickableManager;
import com.readutf.uLib.libraries.menu.ItemClick;
import com.readutf.uLib.libraries.menu.Menu;
import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@Getter
public class LobbyManager {

    Practice practice;

    @Getter
    Location spawn = new Location(Bukkit.getWorld("world"), 0.5, 143, 0.5);

    public LobbyManager(Practice practice) {
        this.practice = practice;

        Arrays.asList(
                new ProtectionListeners(),
                new PlayerJoin()
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, practice));


    }

    public void spawn(Player player, boolean giveItems, boolean teleport) {
        if (teleport) player.teleport(Practice.get().getSpawn());
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[]{ new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), });
        player.updateInventory();
        player.getActivePotionEffects().clear();
        player.setGameMode(GameMode.SURVIVAL);
        player.setExp(0);

        Profile profile = Profile.getUser(player.getUniqueId());

        if(!player.hasPermission("practice.visibility") || !player.hasPermission("stark.donate")) {
            Players.getOnlinePlayers().forEach(player1 -> player1.hidePlayer(player));
        }

        for(Player all : Players.getOnlinePlayers()) {
            if(!all.hasPermission("practice.visibility") || !player.hasPermission("stark.donate")) {
                player.hidePlayer(all);
            }
        }

        if (!giveItems) return;

        ClickableManager.get().giveClickable(player, new Clickable(new ItemBuilder(Material.IRON_SWORD).setName(ColorUtil.color("&cUnranked Queue &7(Right-Click)")).toItemStack(),
                new ItemClick() {
                    @Override
                    public void itemClick(Player player) {
                        Menu menu = new Menu(SpigotUtils.roundToNine(KitManager.get().getKits().size()) / 9, ColorUtil.color("Select an Unranked queue..."), false);
                        int x = 0;


                        for (Kit kit : KitManager.get().getKits().values()) {

                            Queue queue = MatchManager.get().getQueueManager().getQueue(QueueType.UNRANKED, kit);
                            int ingame = MatchManager.get().getInMatch(QueueType.UNRANKED, kit);

                            menu.setItem(x, new ItemBuilder(kit.getIcon()).setLore(SpigotUtils.colorArray(Arrays.asList("&eIn queue: &a" + queue.getPlayers().size(), "&eIn Match: &a" + ingame, "", "&eClick to join the queue for &6" + kit.getName()))).toItemStack(),
                                    new ItemClick() {
                                        @Override
                                        public void itemClick(Player player) {
                                            if (!MatchManager.get().getQueueManager().addToQueue(kit, player, QueueType.UNRANKED)) {
                                                return;
                                            }
                                            player.sendMessage(SpigotUtils.color("&AYou have been added to the queue for unranked " + kit.getName()));
                                            player.closeInventory();
                                        }
                                    });
                            x++;
                            Practice.get().getInventory().createMenu(UUID.randomUUID().toString(), menu).openMenu(player);
                        }


                    }`
                }, Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK)), 0);


        ClickableManager.get().giveClickable(player, new Clickable(new ItemBuilder(Material.DIAMOND_SWORD).setName(ColorUtil.color("&4Ranked Queue &7(Right-Click)")).toItemStack(),
                new ItemClick() {
                    @Override
                    public void itemClick(Player player) {
                        if (profile.getGamesPlayed() < 10) {
                            player.sendMessage(SpigotUtils.color("&cYou need to have played 10 games to play ranked, you need to play " + (10 - profile.getGamesPlayed()) + " games."));
                            return;
                        }

                        Menu menu = new Menu(SpigotUtils.roundToNine(KitManager.get().getKits().size()) / 9, ColorUtil.color("Select an Ranked queue..."), false);
                        int x = 0;
                        for (Kit kit : KitManager.get().getKits().values()) {

                            Queue queue = MatchManager.get().getQueueManager().getQueue(QueueType.RANKED, kit);
                            int ingame = MatchManager.get().getInMatch(QueueType.RANKED, kit);

                            menu.setItem(x, new ItemBuilder(kit.getIcon()).setLore(SpigotUtils.colorArray(Arrays.asList("&eIn queue: &a" + queue.getPlayers().size(), "&eIn Match: &a" + ingame, "", "&eClick to join the queue for &6" + kit.getName()))).toItemStack(),
                                    new ItemClick() {
                                        @Override
                                        public void itemClick(Player player) {
                                            MatchManager.get().getQueueManager().addToQueue(kit, player, QueueType.RANKED);
                                            player.sendMessage(SpigotUtils.color("&AYou have been added to the queue for unranked " + kit.getName()));
                                            player.closeInventory();
                                        }
                                    });
                            x++;
                        }
                        Practice.get().getInventory().createMenu(UUID.randomUUID().toString(), menu).openMenu(player);


                    }
                }, Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK)), 1);

        if (profile.getPreviousDuel() != null) {
            ClickableManager.get().giveClickable(player, new Clickable(new ItemBuilder(Material.DIAMOND).setName(ColorUtil.color("&bRequest Rematch &7(Right-Click)")).toItemStack(), new ItemClick() {
                @Override
                public void itemClick(Player player) {
                    Bukkit.dispatchCommand(player, "duel " + profile.getPreviousDuel().getName());
                    player.setItemInHand(null);
                    profile.setPreviousDuel(null);
                }
            }, Arrays.asList(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR)), 2);
        }

//        ClickableManager.get().giveClickable(player, new Clickable(new ItemBuilder(Material.NAME_TAG).setName(ColorUtil.color("&bCreate Party &7(Right-Click)")).toItemStack(), new ItemClick(9) {
//            @Override
//            public void itemClick(Player player) {
//                if (Party.inParty(player)) {
//                    player.sendMessage(SpigotUtils.color("&cYou are already in a party."));
//                    return;
//                }
//                Party party = new Party(player, new ArrayList<Player>(), true).createParty();
//                profile.setGameState(GameState.PARTY);
//                profile.setParty(party);
//                PlayerUtil.clear(player);
//                party.giveItems(player);
//                player.updateInventory();
//            }
//        }, Arrays.asList(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR)), 4);

        ClickableManager.get().giveClickable(player, new Clickable(new ItemBuilder(Material.WATCH).setName(ColorUtil.color("&eSettings &7(Right-Click)")).toItemStack(), new ItemClick() {
            @Override
            public void itemClick(Player player) {

                Menu menu = new Menu(SpigotUtils.roundToNine(profile.getSettings().size()) / 9, "Edit Settings", true);
                int slot = 0;
                for (Setting setting : Setting.values()) {
                    menu.setItem(slot, new ItemBuilder(setting.icon).setLore(SpigotUtils.colorArray(setting.description)).setName(SpigotUtils.color(setting.getName())).toItemStack(),
                            new ItemClick() {
                                @Override
                                public void itemClick(Player player) {
                                    boolean value = SettingsManager.get().toggleValue(setting, profile);
                                    if (value) {
                                        player.sendMessage(SpigotUtils.color(setting.enabledMessage));
                                    } else {
                                        player.sendMessage(SpigotUtils.color(setting.disabledMessage));
                                    }
                                }
                            });
                    slot++;
                }

                Practice.get().getInventory().createMenu(UUID.randomUUID().toString(), menu).openMenu(player);

            }
        }, Arrays.asList(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR)), 7);

        ClickableManager.get().giveClickable(player, new Clickable(new ItemBuilder(Material.BOOK).setName(ColorUtil.color("&bEdit Kits &7(Right-Click)")).toItemStack(), new ItemClick() {
            @Override
            public void itemClick(Player player) {

                Menu menu = new Menu(SpigotUtils.roundToNine(KitManager.get().getKits().size()) / 9, ColorUtil.color("&eSelect A kit"), true);
                int x = 0;
                for (Kit kit : KitManager.get().getKits().values()) {

                    menu.setItem(x, kit.getIcon(),
                            new ItemClick() {
                                @Override
                                public void itemClick(Player player) {


                                    profile.setEditingKit(kit);


                                    player.sendMessage(SpigotUtils.color("&eYou are now editing " + kit.getName()));
                                    player.sendMessage(SpigotUtils.color(" &7* &eTo leave right click on the sign (or /leave)"));
                                    profile.setGameState(GameState.EDITING_KIT);
                                    player.teleport(Practice.get().getKitEdit());
                                    player.getInventory().setContents(profile.getKit(kit.getName()).getItems());
                                    player.closeInventory();
                                }
                            });
                    x++;
                }
                Practice.get().getInventory().createMenu(UUID.randomUUID().toString(), menu).openMenu(player);


            }
        }, Arrays.asList(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR)), 8);


        player.updateInventory();


    }


}
