package com.readutf.practice.lobby.listeners;

import com.readutf.practice.profiles.GameState;
import com.readutf.practice.profiles.Profile;
import com.readutf.practice.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ProtectionListeners implements Listener {

    @EventHandler
    public void playerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        Profile profile = Profile.getUser(e.getPlayer().getUniqueId());
        if(profile.isBuilding()) {
            return;
        }


        if(profile.getGameState() == GameState.EDITING_KIT) {
            if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if(e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.CHEST) {
                    ArrayList<Material> materials = new ArrayList<>();
                    Inventory inventory = Bukkit.createInventory(null, 9 * 4, "Click an item");
                    int count = 0;
                    for(ItemStack item : profile.getEditingKit().getItems()) {
                        if(item != null && !materials.contains(item.getType())) {
                            inventory.setItem(count, item);
                            materials.add(item.getType());
                            count++;
                        }
                    }

                    player.openInventory(inventory);
                }
            }
        }


        if(!Utils.inMatch(e.getPlayer())) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void foodLoss(FoodLevelChangeEvent e) {
        if(!Utils.inMatch((Player) e.getEntity())) {
            e.setFoodLevel(20);
        }
    }

    @EventHandler
    public void itemDrop(PlayerDropItemEvent e) {
        Profile profile = Profile.getUser(e.getPlayer().getUniqueId());
        if(profile.isBuilding()) {
            return;
        }
        if(!Utils.inMatch(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrag(InventoryDragEvent e) {
        Profile profile = Profile.getUser(e.getWhoClicked().getUniqueId());
        if(profile.isBuilding()) {
            return;
        }
        if(profile.getGameState() == GameState.EDITING_KIT) {
            return;
        }


        if(!Utils.inMatch((Player) e.getWhoClicked())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent e) {
        if(e.getEntityType() == EntityType.DROPPED_ITEM) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void weatherChange(WeatherChangeEvent e) {
        if(e.toWeatherState()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Profile profile = Profile.getUser(e.getWhoClicked().getUniqueId());
        if(profile.isBuilding()) {
            return;
        }

        if(profile.getGameState() == GameState.EDITING_KIT) {
            return;
        }

        if(!Utils.inMatch((Player) e.getWhoClicked())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void entityDamage(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();

            if(!Utils.inMatch((Player) e.getEntity())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player) {
            Player damager = (Player) e.getDamager();
            Profile profile = Profile.getUser(damager.getUniqueId());
            if(!Utils.inMatch(damager)) {
                e.setCancelled(true);
            }
        }
    }

}
