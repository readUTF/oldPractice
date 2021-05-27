package com.readutf.practice.commands.wager.subs;

import com.readutf.practice.Practice;
import com.readutf.practice.kits.Kit;
import com.readutf.practice.kits.KitManager;
import com.readutf.practice.profiles.Profile;
import com.readutf.practice.utils.SpigotUtils;
import com.readutf.uLib.libraries.ColorUtil;
import com.readutf.uLib.libraries.command.SubCommand;
import com.readutf.uLib.libraries.menu.ItemClick;
import com.readutf.uLib.libraries.menu.Menu;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Duel extends SubCommand {


    public Duel() {
        super("duel", Collections.emptyList(), "Fight against a player, winner takes all the coins.", 3, "<player>", true, "practice.command.wager.duel");
    }

    @Override
    public void execute(CommandSender sender, String c, String[] args) {

        Player player = (Player) sender;

        if (args.length == 3) {
            Player target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                sender.sendMessage(SpigotUtils.color("&cCould not find player '" + args[1] + "'"));
                return;
            }
            if (target == player) {
                sender.sendMessage(SpigotUtils.color("&cYou can't duel yourself."));
                return;
            }
            Integer amount = SpigotUtils.parseInt(args[2]);
            if(amount == null) {
                player.sendMessage(SpigotUtils.color("&cInvalid Number."));
                return;
            }
            if(amount < 100) {
                player.sendMessage(SpigotUtils.color("&cYou must wager more than 100 coins."));
                return;
            }

            Profile profile = Profile.getUser(target.getUniqueId());
            Profile targetProfile = Profile.getUser(target.getUniqueId());
            if(!profile.hasCoins(amount)) {
                player.sendMessage(ColorUtil.color("&cYou do not have enough coins to start this wager."));
                return;
            }

            if(!targetProfile.hasCoins(amount)) {
                player.sendMessage(ColorUtil.color("&c" + target.getName() + " does not have enough coins to start this wager."));
                return;
            }




            Menu menu = new Menu(SpigotUtils.roundToNine(KitManager.get().getKits().size()) / 9, ColorUtil.color("&eSelect A kit"), true);
            int x = 0;
            for (Kit kit : KitManager.get().getKits().values()) {

                menu.setItem(x, kit.getIcon(),
                        new ItemClick() {
                            @Override
                            public void itemClick(Player player) {


                                BaseComponent baseComponent = new TextComponent(SpigotUtils.color("&7(Click To Accept)"));
                                baseComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wager accept " + player.getName()));
                                targetProfile.inviteToWager(player, kit, amount);
                                target.sendMessage(SpigotUtils.color("&6" + player.getName() + " &ehas requested to wager you with &6" + kit.getName() + " &efor &6" + amount + "."));
                                target.spigot().sendMessage(baseComponent);
                                player.closeInventory();
                                player.sendMessage(SpigotUtils.color("&aDuel requests sent."));
                            }
                        });
                x++;
            }
            Practice.get().getInventory().createMenu(UUID.randomUUID().toString(), menu).openMenu(player);
        }


    }
}
