package com.readutf.practice.arena.commands.subs;

import com.readutf.practice.arena.ArenaManager;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.command.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class ArenaDelete extends SubCommand {

    public ArenaDelete() {
        super("delete", Collections.emptyList(), "Delete an existing arena.", 2, "<arena>", false, "practice.command.arena.delete");
    }

    @Override
    public void execute(CommandSender sender, String s, String[] args) {
        String arenaName = args[1];
        if(!ArenaManager.get().arenaExists(arenaName)) {
            sender.sendMessage(SpigotUtils.color("&cAn Arena does not exist with this name."));
            return;
        }
        ArenaManager.get().removeArena(arenaName);
        ArenaManager.get().save();
        sender.sendMessage(SpigotUtils.color("&aThe arena has been removed."));
        return;
    }
}
