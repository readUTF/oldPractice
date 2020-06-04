package com.readutf.practice.spectator.listeners;

import com.readutf.practice.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class SpectatorListeners implements Listener {

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        if(Utils.isSpectator(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player hit = (Player) e.getEntity();
            Player hitter = (Player) e.getDamager();
            if(Utils.isSpectator(hitter)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void itemDrop(PlayerDropItemEvent e) {
        if(Utils.isSpectator(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(Utils.isSpectator((Player) e.getWhoClicked())) {
            e.setCancelled(true);
        }
    }

}
