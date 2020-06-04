package com.readutf.practice.utils;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.schematic.SchematicFormat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.io.File;

public final class WorldEditUtil {

    public static void pasteSchematic(File f, Location result) {
        System.out.println(f.getAbsolutePath());
        System.out.println(f.exists());

        Vector vec = new Vector(result.getBlockX(), result.getBlockY(), result.getBlockZ());

        EditSession es = WorldEdit.getInstance().getEditSessionFactory().getEditSession(
                new BukkitWorld(result.getWorld()), WorldEdit.getInstance().getConfiguration().maxChangeLimit);

        SchematicFormat format = SchematicFormat.getFormat(f);
        try {
            CuboidClipboard cc = format.load(f);
            cc.paste(es, vec, false);
            System.out.println("Pasted at " + vec.getBlockX() + " " + vec.getBlockY() + " " + vec.getBlockZ());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }


    }


}