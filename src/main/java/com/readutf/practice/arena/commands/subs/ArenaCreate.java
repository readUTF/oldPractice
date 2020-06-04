package com.readutf.practice.arena.commands.subs;

import com.readutf.practice.arena.Arena;
import com.readutf.practice.arena.ArenaManager;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.command.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class ArenaCreate extends SubCommand {

    public ArenaCreate() {
        super("create", Collections.emptyList(), "Create a new arena.", 2, "<arena>", false, "practice.command.arena.create");
    }

    @Override
    public void execute(CommandSender sender, String s, String[] args) {
        String arenaName = args[1];
        if(ArenaManager.get().arenaExists(arenaName)) {
            sender.sendMessage(SpigotUtils.color("&cAn Arena already exists with this name."));
            return;
        }
        ArenaManager.get().createArena(arenaName);
        ArenaManager.get().save();
        sender.sendMessage(SpigotUtils.color("&aThe arena '" + arenaName + "' has been created!"));
        return;
    }
}
