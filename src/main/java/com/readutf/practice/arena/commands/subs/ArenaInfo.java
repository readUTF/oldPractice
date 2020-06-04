package com.readutf.practice.arena.commands.subs;

import com.readutf.practice.arena.Arena;
import com.readutf.practice.arena.ArenaManager;
import com.readutf.practice.kits.Kit;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.command.SubCommand;
import org.apache.commons.lang.WordUtils;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class ArenaInfo extends SubCommand {

    public ArenaInfo() {
        super("info", Arrays.asList("i"), "View information about a selected arena.", 2, "<arena>", false, "practice.command.arena.info");
    }

    @Override
    public void execute(CommandSender sender, String c, String[] args) {
        Arena arena = ArenaManager.get().getArenaByName(args[1]);
        if(arena == null) {
            sender.sendMessage(SpigotUtils.color("&cCould not find arena called '" + args[1] + "'"));
            return;
        }

        sender.sendMessage(SpigotUtils.color(""));
        sender.sendMessage(SpigotUtils.color(""));
        sender.sendMessage(SpigotUtils.color("&9" + WordUtils.capitalize(arena.getName()) + "&7(&bSetup: " + formatBoolean(arena.isSetup()) + ", &bBuilding: " + formatBoolean(arena.isBuilding()) + "&7)"));
        sender.sendMessage(SpigotUtils.color(" &bSpawn 1: &7(" + SpigotUtils.formatLocation(arena.getSpawn1(), "&cNot Defined.") + "&7)"));
        sender.sendMessage(SpigotUtils.color(" &bSpawn 2: &7(" + SpigotUtils.formatLocation(arena.getSpawn2(), "&cNot Defined.") + "&7)"));
        sender.sendMessage(SpigotUtils.color(" &bRegion: &7(" + SpigotUtils.formatLocation(arena.getArenaRegion().getMaximumPoint(), "&cNot Defined.") + ") (" + SpigotUtils.formatLocation(arena.getArenaRegion().getMinimumPoint(), "&cNot Defined.") + "&7)"));
        sender.sendMessage(SpigotUtils.color(" &bAllowed Kits: "));
        sender.sendMessage(SpigotUtils.color(""));
        for(String allowed : arena.getAllowedKits()) {
            sender.sendMessage(SpigotUtils.color("  &7- &b" + allowed));
        }
        sender.sendMessage(SpigotUtils.color(""));
        sender.sendMessage(SpigotUtils.color(""));
    }

    public String formatBoolean(boolean b) {
        if((Boolean) b == null) return "&cError";
        return b ? "&aTrue" : "&cFalse";
    }

}
