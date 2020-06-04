package com.readutf.practice.arena.commands.subs;

import com.readutf.practice.arena.Arena;
import com.readutf.practice.arena.ArenaManager;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.command.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class ArenaKits extends SubCommand {

    public ArenaKits() {
        super("kits", Collections.singletonList("listkits"), "View the allowed kits in this arena.", 2, "<arena>", false, "practice.command.arena.kits");
    }

    @Override
    public void execute(CommandSender sender, String s, String[] args) {
        Arena arena = ArenaManager.get().getArenaByName(args[1]);
        if(arena == null) {
            sender.sendMessage(SpigotUtils.color("&cCould not find arena."));
            return;
        }
        sender.sendMessage(SpigotUtils.color("&9&lKits"));
        for(String kit : arena.getAllowedKits()) {
            sender.sendMessage(SpigotUtils.color(" &7* " + kit));
        }
    }
}
