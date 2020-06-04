package com.readutf.practice.commands;

import com.readutf.practice.Practice;
import com.readutf.practice.arena.Arena;
import com.readutf.practice.arena.ArenaManager;
import com.readutf.practice.kits.Kit;
import com.readutf.practice.kits.KitManager;
import com.readutf.practice.tournament.Tournament;
import com.readutf.practice.tournament.TournamentManager;
import com.readutf.practice.utils.Cooldown;
import com.readutf.practice.utils.CountDown;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String c, String[] args) {

        Arena arena = ArenaManager.get().getArenaByName("1");

        arena.newArena((Player) sender);




        return true;
    }
}
