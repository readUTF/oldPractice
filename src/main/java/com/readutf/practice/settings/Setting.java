package com.readutf.practice.settings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public enum Setting {


    SHOW_SCOREBOARD("&6In-Match Sidebar",
            Collections.singletonList("&eToggle visibility of the scoreboard inmatches"),
            Material.ITEM_FRAME,
            "&aYou have enabled the sidebar in fights.",
            "&cYou have disabled the sidebar in fights.",
            true),
    ALLOW_SPECTATORS("&6Allow Spectators",
            Collections.singletonList("&eToggles the ability for spectators to watch your match."),
            Material.DIODE,
            "&aYou have allowed spectators viewing your match.",
            "&cYou have prevented spectators viewing your match.",
            true),
    SPECTATOR_NOTIFICATIONS("&6Spectator Notifications",
            Collections.singletonList("&eToggles the messages sent when spectators join your match."),
            Material.REDSTONE_TORCH_ON,
            "&aYou have enabled spectator notifications",
            "&cYou have disabled spectator notifications",
            true),
    RECEIVE_DUELS("&6Duel Requests",
            Collections.singletonList("&eToggles people being able to /duel & /wager you"),
            Material.BOOK,
            "&aYou have enabled duel requests.",
            "&cYou have disabled duel requests.",
            true);



    //The name of the setting
    @Getter private String name;

    //A description of the setting and what it does
    //Set as the lare of the item
    public List<String> description;

    public Material icon;

    public String enabledMessage;
    public String disabledMessage;
    public boolean defaultValue;




}
