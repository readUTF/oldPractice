package com.readutf.practice.arena.commands.subs;

import com.readutf.practice.arena.Arena;
import com.readutf.practice.arena.ArenaManager;
import com.readutf.practice.kits.Kit;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.command.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class ArenaRemoveKit extends SubCommand {

    public ArenaRemoveKit() {
        super("removekit",
                Arrays.asList("rkit"),
                "Remove kits that can be used on this map.",
                3,
                "<arena> <kit>",
                false,
                "practice.command.arena.removekit");
    }

    @Override
    public void execute(CommandSender sender, String c, String[] args) {

        Arena arena = ArenaManager.get().getArenaByName(args[1]);
        if(arena == null) {
            sender.sendMessage(SpigotUtils.color("&cCould not find arena by the name '" + args[1] + "'"));
            return;
        }
        if(arena.getAllowedKits().contains(args[2])) {
            arena.getAllowedKits().remove(args[2]);
            ArenaManager.get().save();
            sender.sendMessage(SpigotUtils.color("&aYou have removed the kit &b" + args[2] + "&7 from &9" + arena.getName()));
        } else {
            sender.sendMessage(SpigotUtils.color("&9" + arena.getName() + " &cdoes not have &b" + args[2] + " &cenabled."));
        }





    }
}
