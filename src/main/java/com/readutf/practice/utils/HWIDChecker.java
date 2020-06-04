package com.readutf.practice.utils;

import com.readutf.practice.Practice;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;

public class HWIDChecker {

    public static boolean check(String s) {
        try {
            URL url = new URL(decryptBase64("aHR0cHM6Ly9wYXN0ZWJpbi5jb20vcmF3L0hieWVqNUFl"));
            url.openConnection();
            ArrayList<String> keys = new ArrayList<String>();
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                keys.add(line);
            }
            if(keys.contains(encryptSha256(s))) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                System.out.println("checking hwid");
                try {
                    URL url = new URL(decryptBase64("aHR0cHM6Ly9wYXN0ZWJpbi5jb20vcmF3L0hieWVqNUFl"));
                    url.openConnection();
                    ArrayList<String> keys = new ArrayList<String>();
                    URLConnection connection = url.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        keys.add(line);
                    }
                    if(!keys.contains(encryptSha256(s))) {
                        Bukkit.getPluginManager().disablePlugin(Practice.get());
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskTimer(Practice.get(), 0L, 20 * 60);

        return false;
    }

    public static String decryptBase64(String strEncrypted) {
        String strData = "";

        try {
            byte[] decoded = Base64.getDecoder().decode(strEncrypted);
            strData = (new String(decoded, "UTF-8"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return strData;
    }

    public static String encryptSha256(String strEncrypt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(strEncrypt.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

}
