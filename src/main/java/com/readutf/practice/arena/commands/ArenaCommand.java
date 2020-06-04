package com.readutf.practice.arena.commands;

import com.readutf.practice.arena.Arena;
import com.readutf.practice.arena.commands.subs.*;
import com.readutf.uLib.libraries.command.CommandHelper;
import com.readutf.uLib.libraries.command.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArenaCommand implements CommandExecutor, TabCompleter {

    List<SubCommand> arenas = Arrays.asList(new ArenaCreate(), new ArenaDelete(), new ArenaSetRegion(), new ArenaSetSpawn(), new ArenaKits(), new ArenaAddKit(), new ArenaList(), new ArenaInfo(), new ArenaRemoveKit());

    public boolean onCommand(CommandSender sender, Command cmd, String c, String[] args) {

        CommandHelper helper = new CommandHelper(c, "Manage arenas.", ChatColor.BLUE, ChatColor.AQUA, ChatColor.GRAY, Arrays.asList(new ArenaCreate(), new ArenaDelete(), new ArenaSetRegion(), new ArenaSetSpawn(), new ArenaKits(), new ArenaAddKit(), new ArenaList(), new ArenaInfo(), new ArenaRemoveKit()));

        if(args.length < 1) {
            helper.sendTo(sender);
            return true;
        }

        helper.handleSubs(sender, args);

        return true;
    }

    public String formatLocation(Location l) {
        return l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        if(args.length == 1) {
            List<String> subs = new ArrayList<>();
            for(SubCommand subCommand : arenas) {
                subs.add(subCommand.getName());
            }
            return subs;
        }
        return null;
    }
}
