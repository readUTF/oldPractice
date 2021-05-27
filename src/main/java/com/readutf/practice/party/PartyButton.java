package com.readutf.practice.party;

import com.readutf.practice.arena.Arena;
import com.readutf.practice.arena.ArenaManager;
import com.readutf.practice.match.MatchManager;
import com.readutf.practice.match.queue.QueueType;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.clickables.TextClickable;
import com.readutf.uLib.menu.Button;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PartyButton extends Button {

    public PartyButton(Party party) {
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public List<String> getLore() {
        return null;
    }

    @Override
    public Material getMaterial() {
        return null;
    }

    @Override
    public void onClick(Player player) {

    }
}
