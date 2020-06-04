package com.readutf.practice.arena;

import com.readutf.practice.Practice;
import com.readutf.practice.kits.Kit;
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.adapter.BukkitImplAdapter;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.registry.WorldData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Arena {



    @Getter @Setter String name;
    @Getter @Setter Location spawn1;
    @Getter @Setter Location spawn2;
    @Getter @Setter CuboidSelection arenaRegion;
    @Getter @Setter boolean building;
    @Getter int maxMatches;
    @Getter @Setter int activeMatches;
    @Setter @Getter List<String> allowedKits;
    @Getter @Setter int offset;

    public Arena(String name) {
        maxMatches = 20;
        this.name = name;
        allowedKits = new ArrayList<>();
    }

    public boolean isSetup() {
        if(spawn1 != null && spawn2 != null && arenaRegion != null) {
            return true;
        }
        return false;
    }

    public void newArena(Player player)  {

        try {
            World world = new BukkitWorld(arenaRegion.getWorld());

            Region region = arenaRegion.getRegionSelector().getRegion();
            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1);
            BlockArrayClipboard clipboard = new BlockArrayClipboard(region);

            offset = 1000;

            Vector target = region.getMinimumPoint().add(offset, 0, offset);

            System.out.print(region.getMinimumPoint());
            System.out.print(region.getMaximumPoint());


            ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(editSession, region, editSession, region.getMinimumPoint().add(offset, 0, offset));
            System.out.print(forwardExtentCopy.getAffected());
            Operations.complete(forwardExtentCopy);

            System.out.println(forwardExtentCopy.getAffected());

            player.teleport(new Location(arenaRegion.getWorld(), target.getBlockX(), target.getBlockY(), target.getBlockZ()));

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
