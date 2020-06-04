package com.readutf.practice.arena.commands.subs;

import com.readutf.practice.arena.Arena;
import com.readutf.practice.arena.ArenaManager;
import com.readutf.practice.kits.Kit;
import com.readutf.practice.kits.KitManager;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.command.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class ArenaAddKit extends SubCommand {

    public ArenaAddKit() {
        super("addkit", Arrays.asList(""), "Add allowed kits to an arena.", 3, "<arena> <kit>", false, "practice.command.arena.addkit");
    }

    @Override
    public void execute(CommandSender sender, String s, String[] args) {
        Arena arena = ArenaManager.get().getArenaByName(args[1]);
        if(arena == null) {
            sender.sendMessage(SpigotUtils.color("&cCould not find arena."));
            return;
        }

        Kit kit = KitManager.get().getKitByName(args[2]);
        if(kit == null) {
            sender.sendMessage(SpigotUtils.color("&cCould not find kit."));
            return;
        }
        if(arena.getAllowedKits().contains(kit.getName())) {
            sender.sendMessage(SpigotUtils.color("&cKit is already added."));
            return;
        }

        arena.getAllowedKits().add(kit.getName());
        ArenaManager.get().save();
        sender.sendMessage(SpigotUtils.color("&aKit has been added."));
    }
}
