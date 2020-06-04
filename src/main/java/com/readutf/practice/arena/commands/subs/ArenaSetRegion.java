package com.readutf.practice.arena.commands.subs;

import com.readutf.practice.arena.Arena;
import com.readutf.practice.arena.ArenaManager;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.ColorUtil;
import com.readutf.uLib.libraries.command.SubCommand;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class ArenaSetRegion extends SubCommand {

    public ArenaSetRegion() {
        super("setregion", Arrays.asList("sr", "area", "setarea"), "Define the area of the arena.", 2, "setregion <arena>", true, "practice.command.arena.setregion");
    }

    @Override
    public void execute(CommandSender sender, String s, String[] args) {

        String arenaName = args[1];

        if(!ArenaManager.get().arenaExists(arenaName)) {
            sender.sendMessage(SpigotUtils.color("&cA arena with the name '" + arenaName + "' does not exist."));
            return;
        }

        Arena arena = ArenaManager.get().getArenaByName(args[1]);
        WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        Selection selection = worldEdit.getSelection((Player) sender);
        if (selection == null) {
            sender.sendMessage(ColorUtil.color("&cYou must have a worldedit selection to set the arena region."));
            return;
        }
        CuboidSelection cuboidSelection = new CuboidSelection(selection.getWorld(), selection.getMaximumPoint(), selection.getMinimumPoint());
        arena.setArenaRegion(cuboidSelection);
        ArenaManager.get().save();
        sender.sendMessage(SpigotUtils.color("&aThe region has been updated for '" + arenaName + ".'"));
        return;
    }
}
