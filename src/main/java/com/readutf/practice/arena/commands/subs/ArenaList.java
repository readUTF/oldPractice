package com.readutf.practice.arena.commands.subs;

import com.readutf.practice.arena.Arena;
import com.readutf.practice.arena.ArenaManager;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.command.SubCommand;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.WordUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class ArenaList extends SubCommand {

    public ArenaList() {
        super("list", Arrays.asList("listarenas", "arenas"), "list all created arenas.", 1, "", false, "practice.command.arena.list");
    }

    @Override
    public void execute(CommandSender sender, String c, String[] args) {

        sender.sendMessage(SpigotUtils.color(""));
        sender.sendMessage(SpigotUtils.color(""));
        for(Arena arena : ArenaManager.get().getArenas()) {
            BaseComponent component = new TextComponent(SpigotUtils.color("&9" + WordUtils.capitalize(arena.getName()) + " &7(" + SpigotUtils.formatLocation(arena.getSpawn1()) + " : " + SpigotUtils.formatLocation(arena.getSpawn2()) + "&7)"));
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/arena info " + arena.getName()));
            if(sender instanceof Player) {
                Player player = (Player) sender;
                player.spigot().sendMessage(component);
            } else {
                sender.sendMessage(SpigotUtils.color("&9" + WordUtils.capitalize(arena.getName()) + " &7(" + SpigotUtils.formatLocation(arena.getSpawn1()) + " : " + SpigotUtils.formatLocation(arena.getSpawn2()) + "&7)"));
            }
        }
        sender.sendMessage(SpigotUtils.color(""));
        sender.sendMessage(SpigotUtils.color(""));
    }
}
