package com.readutf.practice.match;

import com.readutf.practice.Practice;
import com.readutf.practice.arena.Arena;
import com.readutf.practice.kits.Kit;
import com.readutf.practice.match.inventory.InventoryCache;
import com.readutf.practice.match.quake.QuakeManager;
import com.readutf.practice.match.queue.QueueType;
import com.readutf.practice.profiles.GameState;
import com.readutf.practice.profiles.Profile;
import com.readutf.practice.spectator.SpectatorManager;
import com.readutf.practice.tournament.Tournament;
import com.readutf.practice.tournament.TournamentManager;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.practice.utils.Utils;
import com.readutf.uLib.libraries.*;
import com.readutf.uLib.libraries.clickables.TextClickable;
import com.readutf.uLib.libraries.menu.Menu;
import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Match {

    public static ArrayList<Match> matches = new ArrayList<>();

    @Getter
    List<Player> team1;
    @Getter
    List<Player> team2;

    @Getter
    Arena arena;
    @Getter
    Kit kit;
    @Getter
    QueueType queueType;

    long matchStart;
    long matchEnd;

    @Getter
    ArrayList<Item> items = new ArrayList<>();

    public boolean inWarmup = true;
    @Getter
    boolean ended;
    @Getter
    private MatchReward matchReward;
    Match current = this;

    HashMap<Player, Profile> profiles1 = new HashMap<>();
    HashMap<Player, Profile> profiles2 = new HashMap<>();

    @Getter
    ArrayList<InventoryCache> inventoryCaches;

    List<Player> winners;


    @Getter
    ArrayList<Player> spectating;

    public Match(List<Player> team1, List<Player> team2, Arena arena, Kit kit, QueueType queueType, MatchReward matchReward) {
        this.team1 = team1;
        this.team2 = team2;
        this.arena = arena;
        this.kit = kit;
        this.queueType = queueType;
        this.matchReward = matchReward;

        spectating = new ArrayList<>();
        inventoryCaches = new ArrayList<>();

        startMatch();
        matches.add(this);
    }

    int warmupCountdown = 5;

    public void startMatch() {


        arena.setActiveMatches(arena.getActiveMatches() + (team1.size() + team2.size()));

        Stream.concat(team1.stream(), team2.stream()).forEach(player -> {
            Profile profile = Profile.getUser(player.getUniqueId());
            setupPlayer(player);
            profiles1.put(player, profile);
            profile.setGameState(GameState.FIGHTING);
            profile.setActiveMatch(this);
        });

        messageParticipents("&eYou are playing on &c" + arena.getName() + " &eArena");

        new BukkitRunnable() {

            @Override
            public void run() {
                if (warmupCountdown == 0) {
                    messageParticipents(SpigotUtils.color("&7Match has &9commenced."));
                    getFightingAndWatching().forEach(player -> player.playSound(player.getLocation(), Sound.NOTE_PLING, 3, 3));
                    matchStart = System.currentTimeMillis();
                    inWarmup = false;
                    this.cancel();
                    return;
                }
                messageParticipents(SpigotUtils.color("&eMatch will commence in &c" + warmupCountdown + " &eseconds..."));
                getFightingAndWatching().forEach(player -> player.playSound(player.getLocation(), Sound.NOTE_PLING, 3, 1));
                warmupCountdown--;



            }

        }.runTaskTimer(Practice.get(), 0L, 20L);


        //all
        Players.getOnlinePlayers().forEach(player -> {
            if (isPlaying(player)) return;
            team2.forEach(player1 -> player1.hidePlayer(player));
            team1.forEach(player1 -> player1.hidePlayer(player));
        });

    }

    List<Player> losers;

    public void matchEnd(int losingTeam) {

        arena.setActiveMatches(arena.getActiveMatches() - (team1.size() + team2.size()));

        matches.remove(this);
        if (losingTeam == 1) {
            losers = team1;
            winners = team2;

        } else if (losingTeam == 2) {
            losers = team2;
            winners = team1;
        }
        matchEnd = getDuration();
        ended = true;

        MatchManager.get().matches.remove(this);

        losers.forEach(player -> {
            if (matchReward != null) {
                matchReward.onLose(player);
            }
        });
        winners.forEach(player -> {
            if (matchReward != null) {
                matchReward.onWin(player);
                inventoryCaches.add(new InventoryCache(player.getName(), player.getInventory().getContents(), player.getInventory().getArmorContents()));
            }
        });




        MatchManager.get().matches.remove(this);
        if (losingTeam == 3) {
            Stream.concat(team1.stream(), team2.stream()).forEach(player -> {
                Profile profile = profiles2.get(player);
                Practice.get().getLobbyManager().spawn(player, true, true);
                profile.setActiveMatch(null);
                profile.setGameState(GameState.LOBBY);
                player.sendMessage(ColorUtil.color("&cYou're match has been cancelled."));

                player.setHealth(20);
                player.setFireTicks(0);
                player.getActivePotionEffects().clear();
                player.setFoodLevel(20);

            });
            items.forEach(Item::remove);
            return;


        }

        new BukkitRunnable() {

            @Override
            public void run() {
                Stream.concat(team1.stream(), team2.stream()).forEach(player -> {



                    if (queueType == QueueType.TOURNAMENT) {
                        Tournament tournament = TournamentManager.get().getActiveTournament();
                        tournament.getTournamentMatches().remove(current);
                        tournament.getCompetitors().removeAll(losers);
                        losers.forEach(player1 -> player1.sendMessage(SpigotUtils.color("&cYou have been eliminated from the tournament, better luck next time.")));
                        winners.forEach(player1 -> player1.sendMessage(SpigotUtils.color("&aYou have eliminated your opponent and have been moved to the next round.")));
                    }

                    Profile profile = profiles2.get(player);
                    if (profile == null) profile = profiles1.get(player);
                    Practice.get().getLobbyManager().spawn(player, true, true);
                    profile.setActiveMatch(null);
                    profile.setGameState(GameState.LOBBY);

                    if (getQueueType() == QueueType.UNRANKED || getQueueType() == QueueType.RANKED) {
                        if (getTeam(player) == 1) {
                            profile.setPreviousDuel(getTeam2().get(0));
                            profile.setPreviousDuelSet(System.currentTimeMillis());
                        }
                        if (getTeam(player) == 2) {
                            profile.setPreviousDuel(getTeam1().get(0));
                            profile.setPreviousDuelSet(System.currentTimeMillis());
                        }
                    }

                    sendEndMessage(player);

                    player.setHealth(20);
                    player.setFireTicks(0);
                    player.getActivePotionEffects().clear();
                    player.setFoodLevel(20);

                });
                spectating.forEach(o -> {
                    Profile profile = Profile.getUser(o.getUniqueId());
                    profile.setActiveMatch(null);
                    profile.setGameState(GameState.LOBBY);
                    Practice.get().getLobbyManager().spawn(o, true, true);
                    o.setGameMode(GameMode.SURVIVAL);
                    o.setFlying(false);
                    o.sendMessage(SpigotUtils.color("&eThe match you were spectating has ended."));
                });
            }
        }.runTaskLater(Practice.get(), 20 * 5);


    }

    public void setupPlayer(Player player) {
        Profile profile = Profile.getUser(player.getUniqueId());

        if (team1.contains(player)) {
            player.teleport(arena.getSpawn1());
            team2.forEach(player1 -> player.showPlayer(player1));
        } else {
            player.teleport(arena.getSpawn2());
            team1.forEach(player1 -> player.showPlayer(player1));
        }
        if (kit.isCombo()) {
            player.setNoDamageTicks(0);
        }

        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[]{});
        player.setHealth(player.getMaxHealth());

        player.setFireTicks(0);
        player.getActivePotionEffects().clear();
        player.setFoodLevel(20);

        if (kit.isQuake()) {
            player.getInventory().setItem(0, QuakeManager.getGun());
            return;
        }


        profile.getKit(kit.getName()).applyPlayer(player);
    }

    public List<Player> getOponent(Player player) {
        if (team1.contains(player)) return team2;
        if (team2.contains(player)) return team1;
        return null;
    }

    public long getDuration() {
        if (ended) return matchEnd;
        if ((Long) matchStart == null) return 0;
        return System.currentTimeMillis() - matchStart;
    }

    public void messageParticipents(String s) {
        Stream.concat(team1.stream(), team2.stream()).forEach(player -> player.sendMessage(SpigotUtils.color(s)));
    }

    public boolean isPlaying(Player player) {
        if (team1.contains(player) || team2.contains(player)) {
            return true;
        }
        return false;
    }

    public Collection<Player> getFightingAndWatching() {
        ArrayList<Player> players = new ArrayList<>();
        players.addAll(spectating);
        players.addAll(team1);
        players.addAll(team2);
        return players;
    }

    public void elminate(Player killer, Player died) {
        InventoryCache inventoryCache = new InventoryCache(died.getName(), died.getInventory().getContents(), died.getInventory().getArmorContents());
        died.getInventory().clear();
        int team = getTeam(died);
        SpectatorManager.get().setSpectator(died, this);


        getInventoryCaches().add(inventoryCache);
        if (isEliminated(team)) {
            matchEnd(team);
        }
    }

    public boolean isEliminated(int team) {


        if (team == 1) {
            int elimated = 0;
            for (Player player : team1) {
                if (spectating.contains(player)) {
                    elimated++;
                }
            }
            if (elimated == team1.size()) {

                return true;
            } else {
                return false;
            }
        }
        if (team == 2) {
            int elimated = 0;
            for (Player player : team2) {
                if (spectating.contains(player)) {
                    elimated++;
                }
            }
            if (elimated == team2.size()) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public void sendEndMessage(Player player) {
        player.sendMessage(ColorUtil.color("&7&m" + SpigotLine.CHAT.getText()));
        player.sendMessage("");
        player.sendMessage(ColorUtil.color("&aWinner(s): &f" + formatPlayers(winners)));
        player.spigot().sendMessage(InventoryCache.getMessage(player, current));
        player.sendMessage("");
        player.sendMessage(ColorUtil.color("&7&m" + SpigotLine.CHAT.getText()));
    }

    public String formatPlayers(List<Player> players) {
        List<String> names = new ArrayList<>();
        players.forEach(player -> names.add(player.getName()));
        return String.join(",", names);
    }

    public int getTeam(Player player) {
        if (team1.contains(player)) {
            return 1;
        } else if (team2.contains(player)) {
            return 2;
        }
        return 3;
    }


}
