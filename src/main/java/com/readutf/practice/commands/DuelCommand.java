package com.readutf.practice.commands;

import com.readutf.practice.Practice;
import com.readutf.practice.arena.Arena;
import com.readutf.practice.arena.ArenaManager;
import com.readutf.practice.arena.menu.OpenMenu;
import com.readutf.practice.kits.Kit;
import com.readutf.practice.kits.KitManager;
import com.readutf.practice.match.MatchManager;
import com.readutf.practice.match.queue.QueueType;
import com.readutf.practice.match.rewards.WagerReward;
import com.readutf.practice.profiles.Profile;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.practice.utils.Utils;
import com.readutf.uLib.libraries.ColorUtil;
import com.readutf.uLib.libraries.clickables.TextClickable;
import com.readutf.uLib.libraries.menu.ItemClick;
import com.readutf.uLib.libraries.menu.Menu;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

public class DuelCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String c, String[] args) {




        if (!(sender instanceof Player)) {
            sender.sendMessage(SpigotUtils.color("&cYou must be a player to use this command."));
        }
        Player player = (Player) sender;

        if(!Utils.inLobby(player)) {
            player.sendMessage(SpigotUtils.color("&cYou cannot duel right now."));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(SpigotUtils.color("&cArgs: /duel <player>"));
            return true;
        }


        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == player) {
                sender.sendMessage(SpigotUtils.color("&cYou can't duel yourself."));
                return true;
            }
            if (target == null) {
                sender.sendMessage(SpigotUtils.color("&cCould not find player '" + args[0] + "'"));
                return true;
            }
            Profile targetProfile = Profile.getUser(target.getUniqueId());

            if(!Utils.inLobby(target)) {
                sender.sendMessage(SpigotUtils.color("&cYou cannot duel this player right now."));
                return true;
            }

            if(targetProfile.getDuelRequests().containsKey(player)) {
                sender.sendMessage(SpigotUtils.color("&cThis player already has a duel request."));
                return true;
            }


            Menu menu = new Menu(SpigotUtils.roundToNine(KitManager.get().getKits().size()) / 9, ColorUtil.color("&eSelect A kit"), true);
            int x = 0;
            for (Kit kit : KitManager.get().getKits().values()) {

                menu.setItem(x, kit.getIcon(),
                        new ItemClick() {
                            @Override
                            public void itemClick(Player player) {

                                player.sendMessage(SpigotUtils.color("&ePlease select an arena."));
                                OpenMenu.openMenu(player, kit, target);
                            }
                        });
                x++;
            }
            Practice.get().getInventory().createMenu(UUID.randomUUID().toString(), menu).openMenu(player);
        }

        if(args.length == 2) {
            if(!args[0].equalsIgnoreCase("accept")) {
                sender.sendMessage(SpigotUtils.color("&c/duel accept <player>"));
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null || !target.isOnline()) {
                player.sendMessage(SpigotUtils.color("&cCould find player"));
                return true;
            }

            if(!Utils.inLobby(target)) {
                sender.sendMessage(SpigotUtils.color("&cYou must be in the lobby to accept."));
                return true;
            }


            Profile profile = Profile.getUser(player.getUniqueId());
            if (profile.getDuelRequests().containsKey(target)) {
                Kit kit = profile.getDuelRequests().get(target);
                Arena arena = ArenaManager.get().getNextArena(kit);
                if(arena == null) {
                    sender.sendMessage(SpigotUtils.color("&cCould not find arena."));
                    return true;
                }
                MatchManager.get().startMatch(Arrays.asList(player), Arrays.asList(target
                ), arena, kit, QueueType.UNRANKED, null);


                profile.getDuelRequests().remove(player);
                return true;
            } else {
                player.sendMessage(SpigotUtils.color("&cYou do not have a duel request from this player."));
            }
        }


        return true;
    }
}
