package com.readutf.practice.arena.commands.subs;

import com.readutf.practice.arena.Arena;
import com.readutf.practice.arena.ArenaManager;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ArenaSetSpawn extends SubCommand {

    public ArenaSetSpawn() {
        super("setspawn", Collections.singletonList("setspawns"), "Set the spawn points for an arena.", 3, "<arena> <team>", true, "practice.command.arena.setspawn");
    }

    @Override
    public void execute(CommandSender sender, String s, String[] args) {
        String arenaName = args[1];

        Player player = (Player) sender;

        if(!ArenaManager.get().arenaExists(arenaName)) {
            sender.sendMessage(SpigotUtils.color("&cA arena with the name '" + arenaName + "' does not exist."));
            return;
        }

        Integer team = SpigotUtils.parseInt(args[2]);
        if(team == null) {
            sender.sendMessage(SpigotUtils.color("&cInvalid Value."));
            return;
        }
        if(team > 2) {
            sender.sendMessage(SpigotUtils.color("&cAn arena can only have 2 team spawn points."));
            return;
        }
        if(team < 0) {
            sender.sendMessage(SpigotUtils.color("&cValue must be positive."));
            return;
        }

        Arena arena = ArenaManager.get().getArenaByName(arenaName);
        if(team == 1) {
            arena.setSpawn1(player.getLocation());
            ArenaManager.get().save();
            sender.sendMessage(SpigotUtils.color("&aSpawn point 1 has been updated."));
            ArenaManager.get().save();
            return;
        }
        if(team == 2) {
            arena.setSpawn2(player.getLocation());
            ArenaManager.get().save();
            sender.sendMessage(SpigotUtils.color("&aSpawn point 2 has been updated."));
            ArenaManager.get().save();
            return;
        }


    }
}
