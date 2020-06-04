package com.readutf.practice.match.listeners;

import com.readutf.practice.Practice;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.util.Vector;
import org.inventivetalent.particle.ParticleEffect;

import java.util.*;

public class TrajectoryHandler implements Listener {

    HashMap<UUID, Long> drawing = new HashMap<>();

    public TrajectoryHandler(Practice practice) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(practice, () -> {
            for (UUID uuid : drawing.keySet()) {

                Player player = Bukkit.getPlayer(uuid);
                if (player == null || !player.isOnline()) return;
                Location location = player.getEyeLocation();
                List<Vector> vlist = new ArrayList<Vector>();
                float powerMod = getBowPowerModifier(uuid) * 1.5f;
                float yaw = location.getYaw();
                float pitch = location.getPitch();
                location.setX(location.getX() - Math.cos(yaw / 180.0f * 3.141593f) * 0.1599999964237213);
                location.setY(location.getY() - 0.1000000014901161);
                location.setZ(location.getZ() - Math.sin(yaw / 180.0f * 3.141593f) * 0.1599999964237213);
                double motX = -Math.sin(yaw / 180.0f * 3.141593f) * Math.cos(pitch / 180.0f * 3.141593f);
                double motZ = Math.cos(yaw / 180.0f * 3.141593f) * Math.cos(pitch / 180.0f * 3.141593f);
                double motY = -Math.sin(pitch / 180.0f * 3.141593f);
                double f2 = Math.sqrt(motX * motX + motY * motY + motZ * motZ);
                motX /= f2;
                motY /= f2;
                motZ /= f2;
                motX *= powerMod;
                motY *= powerMod;
                motZ *= powerMod;
                for (double y = location.getY() + motY, totalDistance = 0.0; y > 0.0 && totalDistance < 30; y += motY, totalDistance += Math.sqrt(Math.pow(motX, 2.0) + Math.pow(motY, 2.0) + Math.pow(motZ, 2.0))) {

                    motY *= 0.9900000095367432;
                    motX *= 0.9900000095367432;
                    motZ *= 0.9900000095367432;
                    motY -= 0.05000000074505806;
                    vlist.add(new Vector(motX, motY, motZ));
                }


                Location newLoc = location.clone();
                for (Vector vector : vlist) {
                    newLoc.add(vector);
                    if(newLoc.getBlock().getType().isSolid()) {
                        break;
                    }
                    ParticleEffect.FLAME.send(Arrays.asList(player), newLoc, 0,0,0,0,1);
                }
            }

        }, 0L, 5L);


        for (UUID uuid : drawing.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isOnline()) return;
            Bukkit.getScheduler().scheduleSyncRepeatingTask(practice, () -> {
            }, 0L, 5L);
        }
    }

    @EventHandler
    public void onDraw(PlayerInteractEvent e) {
        if (e.getItem() != null && e.getItem().getType() == Material.BOW) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                drawing.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
            }
        }
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            Player shooter = (Player) e.getEntity();
            drawing.remove(shooter.getUniqueId());
        }
    }

    @EventHandler
    public void onChangeSlot(PlayerItemHeldEvent e) {
        drawing.remove(e.getPlayer().getUniqueId());
    }

    private long getHoldingTimeInTicks(UUID uuid) {
        long l = 72000L - (System.currentTimeMillis() - drawing.get(uuid)) / 50L;
        return Math.max(l, 0L);
    }

    public float getBowPowerModifier(UUID uuid) {
        long i = getHoldingTimeInTicks(uuid);
        int j = (int) (72000L - i);
        float f = j / 20.0f;
        f = (f * f + f * 2.0f) / 3.0f;
        if (f > 1.0f) {
            f = 1.0f;
        }
        return f * 2.0f;
    }

}
