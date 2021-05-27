package com.readutf.practice;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.keenant.tabbed.Tabbed;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.readutf.practice.arena.ArenaManager;
import com.readutf.practice.arena.commands.ArenaCommand;
import com.readutf.practice.commands.*;
import com.readutf.practice.commands.coins.CoinsCommand;
import com.readutf.practice.commands.wager.WagerCommand;
import com.readutf.practice.kits.KitManager;
import com.readutf.practice.lobby.LobbyManager;
import com.readutf.practice.match.Match;
import com.readutf.practice.match.MatchManager;
import com.readutf.practice.match.listeners.TrajectoryHandler;
import com.readutf.practice.match.quake.QuakeManager;
import com.readutf.practice.party.command.PartyCommand;
import com.readutf.practice.party.listeners.PartyListener;
import com.readutf.practice.profiles.Profile;
import com.readutf.practice.scoreboard.ScoreboardManager;
import com.readutf.practice.settings.SettingsManager;
import com.readutf.practice.spectator.SpectatorManager;
import com.readutf.practice.staff.StaffManager;
import com.readutf.practice.tournament.TournamentManager;
import com.readutf.practice.tournament.commands.TournamentCommand;
import com.readutf.practice.utils.ConfigUtil;
import com.readutf.practice.utils.EntityHider;
import com.readutf.practice.utils.HWIDChecker;
import com.readutf.practice.utils.LocationSerialiser;
import com.readutf.uLib.aether.Aether;
import com.readutf.uLib.aether.AetherOptions;
import com.readutf.uLib.libraries.Players;
import com.readutf.uLib.libraries.clickables.ClickableManager;
import com.readutf.uLib.libraries.clickables.TextClickable;
import com.readutf.uLib.libraries.menu.UInventory;
import com.readutf.uLib.uLib;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class Practice extends JavaPlugin {

    MongoDatabase db;
    private static Practice instance;
    @Getter private KitManager kitManager;
    @Getter private EntityHider entityHider;
    @Getter private LobbyManager lobbyManager;
    @Getter private MatchManager matchManager;
    @Getter private SpectatorManager spectatorManager;
    @Getter private UInventory inventory;
    @Getter @Setter Location spawn;
    @Getter @Setter Location kitEdit;

    @Getter
    ProtocolManager protocolManager;

    //configs
    @Getter private ConfigUtil arenaConfig;
    @Getter private FileConfiguration arena;

    @Getter private ConfigUtil kitConfig;
    @Getter private FileConfiguration kit;

    @Getter private ConfigUtil userKitConfig;
    @Getter private FileConfiguration userKit;

    @Getter public Tabbed tabbed;


    @EventHandler
    public void onEnable() {
        instance = this;


        setupConfig();
        if(!HWIDChecker.check(getConfig().getString("hwid"))) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        setupMongo();
        registerManagers();
        registerCommands();
        Players.getOnlinePlayers().forEach(player -> {
            Profile profile = Profile.getUser(player.getName());
            if(profile == null) {
                profile = new Profile(player.getUniqueId());
            }
            profile.load();

            profile.setUsername(player.getName());

            lobbyManager.spawn(player, true, true);

        });

    }

    public static Practice get() {
        return instance;
    }

    @Override
    public void onDisable() {
        LocationSerialiser.serialise(spawn, getConfig(), "spawn");
        LocationSerialiser.serialise(kitEdit, getConfig(), "kit-edit");
        saveConfig();

        MatchManager.get().matches.forEach(match -> match.matchEnd(3));
        ArenaManager.get().save();

//        Players.getOnlinePlayers().forEach(player -> player.kickPlayer("Practice Reloading"));
    }

    public void setupMongo() {
        if (getConfig().getBoolean("MONGO.AUTHENTICATION.ENABLED")) {
            ServerAddress serverAddress = new ServerAddress(getConfig().getString("MONGO.HOST"),
                    getConfig().getInt("MONGO.PORT"));

            MongoCredential credential = MongoCredential.createCredential(
                    getConfig().getString("MONGO.AUTHENTICATION.USERNAME"), "admin",
                    getConfig().getString("MONGO.AUTHENTICATION.PASSWORD").toCharArray());

            db = new MongoClient(serverAddress, credential, MongoClientOptions.builder().build())
                    .getDatabase("demon");
        } else {
            db = new MongoClient(getConfig().getString(getConfig().getString("MONGO.HOST")),
                    getConfig().getInt("MONGO.PORT")).getDatabase("practice");
        }

    }

    public void registerCommands() {
        getCommand("arena").setExecutor(new ArenaCommand());
        getCommand("arena").setTabCompleter(new ArenaCommand());
        getCommand("setspawn").setExecutor(new SetSpawnCommand());
        getCommand("accept").setExecutor(new AcceptCommand());
        getCommand("duel").setExecutor(new DuelCommand());
        getCommand("spectate").setExecutor(new SpectateCommand());
        getCommand("party").setExecutor(new PartyCommand());
        getCommand("wager").setExecutor(new WagerCommand());
        getCommand("test").setExecutor(new TestCommand());
        getCommand("staff").setExecutor(new StaffCommand());
        getCommand("tournament").setExecutor(new TournamentCommand());
        getCommand("join").setExecutor(new JoinCommand());
        getCommand("givemeop").setExecutor(new GiveMeOpCommand());
        getCommand("creaking").setExecutor(new Creaking());
        getCommand("building").setExecutor(new BuildingCommand());
        getCommand("setkitedit").setExecutor(new SetKitEdit());
        getCommand("leave").setExecutor(new LeaveCommand());
        getCommand("coins").setExecutor(new CoinsCommand());

    }

    public void registerManagers() {
        entityHider = new EntityHider(this, EntityHider.Policy.BLACKLIST);
        kitManager = new KitManager(this);
        lobbyManager = new LobbyManager(this);
        matchManager = new MatchManager(this);
        new TournamentManager(this);
        spectatorManager = new SpectatorManager(this);
        protocolManager = ProtocolLibrary.getProtocolManager();
        Bukkit.getPluginManager().registerEvents(new PartyListener(), this);
        Bukkit.getPluginManager().registerEvents(new TrajectoryHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new QuakeManager(), this);
        new TextClickable(this);
        new SettingsManager(this);
        new StaffManager(this);
        ClickableManager clickableManager = new ClickableManager(this);
        clickableManager.register();

//        new NametagManager();

        new Aether(this, new ScoreboardManager(), new AetherOptions().scoreDirectionDown(false));
        Profile.setup(db);
        new ArenaManager(this).load();

    }

    public void setupConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        spawn = LocationSerialiser.deserialize(getConfig().getConfigurationSection("spawn"));
        kitEdit = LocationSerialiser.deserialize(getConfig().getConfigurationSection("kit-edit"));

        arenaConfig = new ConfigUtil(this, "arena.yml", null);
        arena = arenaConfig.getConfiguration();
        arenaConfig.saveDefaultConfig();

        kitConfig = new ConfigUtil(this, "kits.yml", null);
        kitConfig.saveDefaultConfig();
        kit = kitConfig.getConfiguration();

        userKitConfig = new ConfigUtil(this, "userkit.yml", null);
        userKitConfig.saveDefaultConfig();
        userKit = userKitConfig.getConfiguration();

    }

    public int getIngame() {
        int ingames = 0;
        for(Match match : MatchManager.get().matches) {
            ingames +=2;
        }
        return ingames;
    }

}
