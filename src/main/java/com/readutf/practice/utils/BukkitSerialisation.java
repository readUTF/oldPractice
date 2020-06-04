package com.readutf.practice.utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BukkitSerialisation {


    public static String itemStackArrayToBase64(ItemStack[] items) {

        StringBuilder str = new StringBuilder();


        for(ItemStack item : items) {
            str.append(ItemStackSerializer.serialise(item)).append("##");
        }

        return str.toString();
    }

    public static ItemStack[] itemStackArrayFromBase64(String data) {
        String[] parts = data.split("##");

        List<ItemStack> items = new ArrayList<>();
        for(String part: parts) {
            items.add(ItemStackSerializer.deseralise(part));
        }
        return items.toArray(new ItemStack[items.size()]);
    }

}
