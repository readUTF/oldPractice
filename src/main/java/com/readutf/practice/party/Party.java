package com.readutf.practice.party;

import com.readutf.practice.Practice;
import com.readutf.practice.arena.Arena;
import com.readutf.practice.arena.ArenaManager;
import com.readutf.practice.kits.Kit;
import com.readutf.practice.kits.KitManager;
import com.readutf.practice.match.MatchManager;
import com.readutf.practice.match.queue.QueueType;
import com.readutf.practice.profiles.GameState;
import com.readutf.practice.profiles.Profile;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.ColorUtil;
import com.readutf.uLib.libraries.ItemBuilder;
import com.readutf.uLib.libraries.SpigotLine;
import com.readutf.uLib.libraries.clickables.Clickable;
import com.readutf.uLib.libraries.clickables.ClickableManager;
import com.readutf.uLib.libraries.clickables.TextClickable;
import com.readutf.uLib.libraries.menu.ItemClick;
import com.readutf.uLib.libraries.menu.Menu;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Party {

    @Getter public static ArrayList<Party> parties = new ArrayList<>();

    @Getter @Setter private Player leader;
    @Getter @Setter private ArrayList<Player> members;
    @Getter @Setter private boolean inviteOnly;

    @Getter @Setter HashMap<Party, Kit> fightInvites;
    @Getter @Setter ArrayList<Player> invites;

    @Getter @Setter String prefix = "&7[&bParty&7]";


    public Party(Player leader, ArrayList<Player> members, boolean inviteOnly) {
        this.leader = leader;
        this.members = members;
        this.inviteOnly = inviteOnly;
        invites = new ArrayList<>();
        fightInvites = new HashMap<>();
    }

    public Party createParty() {
        parties.add(this);
        return this;
    }

    public static boolean inParty(Player player) {
        for (Party party : parties) {
            if(party.getLeader() == player) {
                return true;
            }
            if(party.getMembers().contains(player)) {
                return true;
            }
        }
        return false;
    }

    public void giveItems(Player player) {
        ClickableManager.get().giveClickable(player, new Clickable(
                new ItemBuilder(Material.SKULL_ITEM, 1).setSkullOwner(leader.getName()).setName(SpigotUtils.color("&9Party Info")).toItemStack(), new ItemClick(12) {
            @Override
            public void itemClick(Player player) {

                player.sendMessage(SpigotUtils.color("&7&m" + SpigotLine.CHAT.getText()));
                player.sendMessage(SpigotUtils.color("&6&lParty"));
                player.sendMessage(SpigotUtils.color(" &6Leader: &f" + getLeader().getName()));
                player.sendMessage(SpigotUtils.color(" &6Invite-Only: &f" + isInviteOnly()));
                player.sendMessage(SpigotUtils.color(" &6Members:"));
                for(Player member: members) {
                    player.sendMessage(SpigotUtils.color("  &7- &f" + member.getName()));
                }
                player.sendMessage(SpigotUtils.color("&7&m" + SpigotLine.CHAT.getText()));


            }
        }, Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK)), 0);


        ClickableManager.get().giveClickable(player, new Clickable(new ItemBuilder(Material.BOOK).setName(SpigotUtils.color("&6Fight Other Parties")).toItemStack(), new ItemClick(77) {
            @Override
            public void itemClick(Player player) {
                openOtherPartyMenu(player);
            }
        }, Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_AIR)), 1);

        ClickableManager.get().giveClickable(player, new Clickable(new ItemBuilder(Material.REDSTONE).setName(SpigotUtils.color("&cLeave Party")).toItemStack(), new ItemClick(76) {
            @Override
            public void itemClick(Player player) {
                ClickableManager.get().clear(player);
                leave(player);
                player.updateInventory();
            }
        }, Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_AIR)), 8);
    }

    public void remove() {
        Profile leaderProfile = Profile.getUser(leader.getUniqueId());
        leaderProfile.setParty(null);
        leaderProfile.setGameState(GameState.LOBBY);
        Practice.get().getLobbyManager().spawn(leader, true, false);
        leader.sendMessage(SpigotUtils.color("&cParty disbanded."));

        members.forEach(player -> {
            Profile membersProfile = Profile.getUser(player.getUniqueId());
            membersProfile.setParty(null);
            membersProfile.setGameState(GameState.LOBBY);
            Practice.get().getLobbyManager().spawn(player, true, false);
            player.sendMessage(SpigotUtils.color("&cParty disbanded."));
        });
        parties.remove(this);
    }

    public boolean leave(Player player) {

        if(player == leader) {
            System.out.println("player is leader");
            remove();
            return true;
        }

        members.remove(player);
        Profile membersProfile = Profile.getUser(player.getUniqueId());
        membersProfile.setParty(null);
        membersProfile.setGameState(GameState.LOBBY);
        Practice.get().getLobbyManager().spawn(player, true, false);
        player.sendMessage(SpigotUtils.color("&cYou have left the party."));
        messageAll("&c" + player.getName() + " has left the party.");
        return false;
    }

    public String getMembers(Party party) {
        List<String> members = new ArrayList<>();
        for(Player player : party.getMembers()) {
            members.add(player.getName());
        }
        return String.join(",", members);

    }

    HashMap<Player, Integer> currentPage = new HashMap<>();
    HashMap<Player, Integer> slot = new HashMap<>();

    public void openOtherPartyMenu( Player player) {
        Menu menu = new Menu(6, ColorUtil.color("&6Other Parties."), true);
        Party opener = Profile.getUser(player.getUniqueId()).getParty();
        currentPage.put(player, 1);
        slot.put(player, 0);

        getPage(opener, currentPage.get(player)).forEach(party -> {
            if(party != this) {

                Party this1 = this;
                menu.setItem(slot.get(player), party.getIcon(), new ItemClick(78) {
                    @Override
                    public void itemClick(Player player) {
                        if(leader != player) {
                            player.sendMessage(SpigotUtils.color("&cYou must be the leader to do this."));
                            return;
                        }
                        System.out.println(2);

                        Menu menu = new Menu(SpigotUtils.roundToNine(KitManager.get().getKits().size()) / 9, ColorUtil.color("&eSelect A kit"), true);
                        int x = 0;
                        for (Kit kit : KitManager.get().getKits().values()) {

                            menu.setItem(x, kit.getIcon(),
                                    new ItemClick(44) {
                                        @Override
                                        public void itemClick(Player player) {
                                            System.out.println(3);

                                            BaseComponent baseComponent = new TextComponent(SpigotUtils.color("&7(Click To Accept)"));
                                            baseComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, TextClickable.get().getCommand(new Runnable() {
                                                @Override
                                                public void run() {

                                                    List<Player> team1 = Stream.concat(this1.getMembers().stream(), Stream.of(this1.getLeader())).collect(Collectors.toList());
                                                    List<Player> team2 = Stream.concat(party.getMembers().stream(), Stream.of(party.getLeader())).collect(Collectors.toList());

                                                    Arena arena = ArenaManager.get().getNextArena(kit);
                                                    if(arena == null) {
                                                        Stream.concat(team1.stream(), team2.stream()).forEach(player -> {
                                                            player.sendMessage(SpigotUtils.color("&cCould not find an arena. Please try again."));
                                                        });
                                                        return;
                                                    }

                                                    MatchManager.get().startMatch(team1,
                                                            team2,
                                                            arena,
                                                            kit,
                                                            QueueType.UNRANKED,
                                                            null);
                                                }
                                            })));
                                            party.getFightInvites().put(this1, kit);
                                            party.getLeader().sendMessage(SpigotUtils.color("&6" + player.getName() + " &ehas requested to duel you with &6" + kit.getName() + "."));
                                            party.getLeader().spigot().sendMessage(baseComponent);
                                            player.closeInventory();
                                            player.sendMessage(SpigotUtils.color("&aDuel requests sent."));
                                        }
                                    });
                            x++;
                        }
                        Practice.get().getInventory().createMenu("choose kit", menu).openMenu(player);


                    }
                });
                slot.put(player, slot.get(player) + 1);
            }
        });

        if(currentPage.get(player) != 1) {
            menu.setItem(48, new ItemBuilder(Material.ARROW).setName(SpigotUtils.color("&7Previous Page")).toItemStack(), new ItemClick(91) {
                @Override
                public void itemClick(Player player) {
                    System.out.println(currentPage.get(player));
                    currentPage.put(player, currentPage.get(player) -1);
                }
            });
        }
        menu.setItem(49, new ItemBuilder(Material.REDSTONE_BLOCK).setName(SpigotUtils.color("&cClose Inventory.")).toItemStack(), new ItemClick(92) {
            @Override
            public void itemClick(Player player) {
                player.closeInventory();
            }
        });
        if(currentPage.get(player) != getPages().size()) {
            menu.setItem(50, new ItemBuilder(Material.ARROW).setName(SpigotUtils.color("&7Next Page")).toItemStack(), new ItemClick(90) {
                @Override
                public void itemClick(Player player) {
                    currentPage.put(player, currentPage.get(player) + 1);
                    IntStream.rangeClosed(0, 45).forEach(i -> {
                        menu.setItem(i, new ItemStack(Material.AIR));
                    });
                    getPage(opener, currentPage.get(player)).forEach(party -> {
                        menu.setItem(currentPage.get(player), party.getIcon());
                        slot.put(player, slot.get(player) + 1);
                    });
                }
            });
        }
        Practice.get().getInventory().createMenu(UUID.randomUUID().toString(), menu).openMenu(player);
    }

    public List<Party> getPage(Party opener, int page) {
        HashMap<Integer, List<Party>> pages = getPages();
        int count = 0;
        List<Party> idk = new ArrayList<>();
        int currentPage = 1;
        int x = 0;
        for(Party party : parties) {
            idk.add(party);
            count++;
            if(count == 45) {
                currentPage++;
                count = 0;
                pages.put(currentPage, idk);
            }
            x++;
            if(x == parties.size()) {
                pages.put(currentPage, idk);
            }
        }


        if(pages.containsKey(page)) {

            while (pages.get(page).contains(opener)) {
                pages.get(page).remove(opener);
            }

            return pages.get(page);
        }
        return null;
    }

    public HashMap<Integer, List<Party>> getPages() {

        HashMap<Integer, List<Party>> pages = new HashMap<>();
        int count = 0;
        List<Party> idk = new ArrayList<>();
        int currentPage = 1;
        int x = 0;
        for(Party party : parties) {
            idk.add(party);
            count++;
            if(count == 45) {
                currentPage++;
                count = 0;
                pages.put(currentPage, idk);
            }
            x++;
            if(x == parties.size()) {
                pages.put(currentPage, idk);
            }
        }
        return pages;
    }

    public void messageAll(String s) {
        members.forEach(player -> {
            player.sendMessage(SpigotUtils.color(s));
        });
        leader.sendMessage(SpigotUtils.color(s));
    }

    public ItemStack getIcon() {
         return new ItemBuilder(Material.SKULL_ITEM)
                .setName(SpigotUtils.color("&6" + getLeader().getName())).setSkullOwner(leader.getName()).toItemStack();
    }



}
