package com.readutf.practice.profiles;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.readutf.practice.Practice;
import com.readutf.practice.arena.Arena;
import com.readutf.practice.kits.Kit;
import com.readutf.practice.kits.KitManager;
import com.readutf.practice.match.Match;
import com.readutf.practice.match.queue.Queue;
import com.readutf.practice.party.Party;
import com.readutf.practice.settings.Setting;
import com.readutf.practice.utils.BukkitSerialisation;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Profile {

    //data
    @Getter @Setter UUID id;
    @Getter @Setter String username;
    @Getter @Setter HashMap<String, Long> elo;
    @Getter @Setter int gamesPlayed;
    @Getter @Setter HashMap<Setting, Boolean> settings;
    @Getter @Setter int coins;


    //local data
    @Getter @Setter Match activeMatch;
    @Getter @Setter GameState gameState;
    @Getter @Setter Queue queue;
    @Getter @Setter Match spectatingMatch;
    @Getter HashMap<Player, Kit> duelRequests;
    @Getter HashMap<Player, Arena> duelMaps;
    @Getter HashMap<Player, Kit> wagerRequests;
    @Getter HashMap<Player, Integer> wagerAmount;
    @Getter @Setter Party party;
    @Getter @Setter Player previousDuel;
    @Getter @Setter long previousDuelSet;
    boolean loaded;
    @Getter @Setter boolean building;
    @Getter @Setter Kit editingKit;

    private static MongoCollection<Document> collection;
    public static ArrayList<Profile> profiles = new ArrayList<>();

    public Profile(UUID id) {
        this.id = id;
        gameState = GameState.LOBBY;
        duelRequests = new HashMap<>();
        wagerRequests = new HashMap<>();
        wagerAmount = new HashMap<>();
        settings = new HashMap<>();
        duelMaps = new HashMap<>();
        elo = new HashMap<>();
        activeMatch = null;
        party = null;
        settings.put(Setting.ALLOW_SPECTATORS, true);

        KitManager.get().getKits().keySet().forEach(o -> {
            elo.put(o, 2500L);
        });
        for(Setting setting : Setting.values()) {
            settings.put(setting, setting.defaultValue);
        }
    }

    public boolean checkFriendly(Player player) {
        return false;
    }

    public void load() {
        if(loaded) return;

        Document document = collection.find(new Document("uuid", id.toString())).first();
        if(document == null) { createAccount(); return; }


        gamesPlayed = (int) getOrDefault(document, "gamesPlayed", 0);
        username = (String) getOrDefault(document, "username", null);
        coins = (int) getOrDefault(document, "coins", 0);
        if(document.containsKey("elo")) {

            try {
                JSONObject jsonObject = (JSONObject) new JSONParser().parse(document.getString("elo"));
                for(Object s : jsonObject.keySet()) {
                    elo.put((String) s, (long) jsonObject.get(s));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        if(document.containsKey("settings")) {
            try {
                JSONObject jsonObject = (JSONObject) new JSONParser().parse(document.getString("settings"));
                for(Object s : jsonObject.keySet()) {
                    String key = (String) s;
                    boolean value = (Boolean) jsonObject.get(key);
                    settings.put(Setting.valueOf(key), value);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        profiles.add(this);
        loaded = true;

    }

    public static Profile getUser(UUID id) {
        for (Profile user : profiles) {
            if (user.getId().toString().equalsIgnoreCase(id.toString())) {
                return user;
            }
        }
        return null;
    }

    public void addCoins(int coins) {
        this.coins += coins;
    }

    public void removeCoins(int coins) {
        this.coins -= coins;
    }

    public static Profile getUser(String name) {
        for (Profile user : profiles) {
            if (user.getUsername() != null) {
                if (user.getUsername().equalsIgnoreCase(name)) {
                    return user;
                }
            }
        }
        Document document = collection.find(new Document("username", name)).first();
        if (document != null) {
            if (document.containsKey("uuid")) {
                return new Profile(UUID.fromString(document.getString("uuid")));
            }
        }

        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(reader.readLine());

            UUID uuid = UUID.fromString(String.valueOf(obj.get("id")).replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                    "$1-$2-$3-$4-$5"));
            return new Profile(uuid);

        } catch (Exception e) {
            return null;
        }
    }

    public Kit getKit(String name) {
        Kit reg = KitManager.get().getKitByName(name);
        if(Practice.get().getUserKit().isSet(id.toString() + "." + name)) {

            try {
                Kit newkit = new Kit(name, BukkitSerialisation.itemStackArrayFromBase64(Practice.get().getUserKit().getString(id.toString() + "." + name)), reg.getArmour(), reg.isBuilding(), reg.isCombo(), reg.isSumo(),reg.isQuake(), reg.getFill(), reg.getIcon(), null, null);
                return newkit;
            } catch (Exception e) {
                e.printStackTrace();
                return reg;
            }
        }
        return reg;
    }

    public void inviteToDuel(Player player, Kit kit) {
        duelRequests.put(player, kit);
        new BukkitRunnable() {

            @Override
            public void run() {
                if(duelRequests.containsKey(player)) {
                    duelRequests.remove(player);
                }
            }
        }.runTaskLater(Practice.get(), 20 * 60);
    }

    public void inviteToDuel(Player player, Kit kit, Arena arena) {
        duelRequests.put(player, kit);
        duelMaps.put(player, arena);
        new BukkitRunnable() {

            @Override
            public void run() {
                if(duelRequests.containsKey(player)) {
                    duelRequests.remove(player);
                }
            }
        }.runTaskLater(Practice.get(), 20 * 60);
    }

    public void inviteToWager(Player player, Kit kit, int amount) {
        wagerRequests.put(player, kit);
        wagerAmount.put(player, amount);
        new BukkitRunnable() {

            @Override
            public void run() {
                if(wagerRequests.containsKey(player)) {
                    wagerRequests.remove(player);
                }
            }
        }.runTaskLater(Practice.get(), 20 * 60);
    }

    public void save() {
        if(id == null) {
            return;
        }

        Document document = new Document("uuid", id.toString());
        document.put("username", username);
        document.put("gamesPlayed", gamesPlayed);
        document.put("coins", coins);

        JsonObject eloObject = new JsonObject();
        for(String e : elo.keySet()) {
            eloObject.addProperty(e, elo.get(e));
        }
        document.put("elo", eloObject.toString());

        JsonObject settingObject = new JsonObject();
        for(Setting s : settings.keySet()) {
            settingObject.addProperty(s.name(), settings.get(s));
        }
        document.put("settings", settingObject.toString());


        collection.replaceOne(new BasicDBObject("uuid", id.toString()), document);
    }

    public void createAccount() {
        Document document = new Document("uuid", id.toString());
        gamesPlayed = 0;
        coins = 0;
        document.put("gamesPlayed", 0);
        for(Kit kit : KitManager.get().getKits().values()) {
            elo.put(kit.getName(), 2500L);
        }

        collection.insertOne(document);
        profiles.add(this);

        if(settings.isEmpty()) {
            settings.put(Setting.SPECTATOR_NOTIFICATIONS, true);
            settings.put(Setting.SHOW_SCOREBOARD, true);
            settings.put(Setting.ALLOW_SPECTATORS, true);
        }

        loaded = true;
    }

    public static void setup(MongoDatabase db) {
        collection = db.getCollection("userdata");

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Practice.get(), new BukkitRunnable() {
            @Override
            public void run() {
                for(Profile profile : profiles) {
                    if(System.currentTimeMillis() - profile.getPreviousDuelSet() > 15 * 1000) {
                        profile.setPreviousDuelSet(-1L);
                        profile.setPreviousDuel(null);
                        Player target = Bukkit.getPlayer(profile.getId());
                        if(target == null) return;
                        target.getInventory().remove(Material.DIAMOND);
                    }
                }
            }
        },0, 20L);

    }

    public int getGlobalElo() {
        int total = 0;
        if(elo.size() == 0) {
            return 1000;
        }
        for(long l : elo.values()) {
            total += l;
        }
        return total / elo.size();
    }

    public boolean hasCoins(int x) {
        return coins -x >= 0;
    }

    public Object getOrDefault(Document document, String key, Object def) {
        if(document.containsKey(key)) {
            return document.get(key);
        }
        return def;
    }



}
