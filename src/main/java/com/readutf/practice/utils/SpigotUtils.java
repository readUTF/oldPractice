package com.readutf.practice.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class SpigotUtils {

	static String UUIDAPI = "https://api.mojang.com/users/profiles/minecraft/";

	/*
	 * Used to get the uuid of a player from the mojang servers
	 * 
	 * @param The player you want to get the uuid from
	 * 
	 * @return the players uuid
	 */
	public static UUID getUUID(Player p) {
		try {
			URL url = new URL(UUIDAPI + p.getName());
			URLConnection connection = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = in.readLine();
			line = line.replace("}", "").replace("{", "").substring(6, 38);

			line = line.substring(0, 8) + "-" + line.substring(8, 12) + "-" + line.substring(12, 16) + "-"
					+ line.substring(16, 20) + '-' + line.substring(20);

			return UUID.fromString(line);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean isItem(ItemStack item, ItemStack item1) {
		if(item != null && item1 != null) {
			if(item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
				if(item1.hasItemMeta() && item1.getItemMeta().hasDisplayName()) {
					if(item.getItemMeta().getDisplayName().equalsIgnoreCase(item1.getItemMeta().getDisplayName())) {
						return true;
					}
				}
				
			}
		}
		return false;
	}

	/*
	 * Used to get the uuid of a player from the mojang servers
	 * 
	 * @param The players name you want to get the uuid from
	 * 
	 * @return the players uuid
	 */
	public static UUID getUUID(String p) {
		try {
			URL url = new URL(UUIDAPI + p);
			URLConnection connection = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = in.readLine();
			line = line.replace("}", "").replace("{", "").substring(6, 38);
			line = line.substring(0, 8) + "-" + line.substring(8, 12) + "-" + line.substring(12, 16) + "-"
					+ line.substring(16, 20) + '-' + line.substring(20);

			return UUID.fromString(line);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ItemStack[] reverse(ItemStack[] a) 
    { 
		ItemStack[] b = a.clone();
        Collections.reverse(Arrays.asList(b));
        return b;
        
    } 
	
	public static Integer parseInt(String s) {
		try {
			int value = Integer.parseInt(s);
			return value;
		} catch (Exception e) {
			return null;
		}
		
		
	}

	public static String formatLocation(Location l) {
		if (l == null)
			return "None";

		return l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ();
	}

	public static String formatLocation(Location l, String errorReturn) {
		if (l == null)
			return errorReturn;

		return l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ();
	}
	
	public static String color(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	public static String formatLocationWithWorld(Location l) {
		if (l == null)
			return "None";

		return l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ() + " (" + l.getWorld().getName() + ")";
	}
	
	public static String formatTime(long millis) {
	    String hms = String.format("%02dh %02dm %02ds", TimeUnit.MILLISECONDS.toHours(millis),
	            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
	            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
	    return hms;
	}
	
	public static int roundToNine(int x) {
		if(x < 9) {
			return 9;
		}
		if(x < 18) {
			return 18;
		}
		if(x < 27) {
			return 27;
		}
		if(x < 36) {
			return 36;
		}
		if(x < 45) {
			return 45;
		}
		return 54;
	}

	/*
	 * Used to ChatColor each line in an ArrayList
	 * 
	 * @param The ArrayList to convert
	 * 
	 * @return The coloured ArrayList
	 */
	public static ArrayList<String> colorArray(List<String> array) {
		ArrayList<String> newarray = new ArrayList<>();
		for (String s : array) {
			newarray.add(SpigotUtils.color(s));
		}
		return newarray;
	}

	/*
	 * Used to ChatColor each line in an ArrayList
	 * 
	 * @param The String list to convert
	 * 
	 * @return The coloured String list
	 */
	public static String[] colorArray(String[] array) {
		ArrayList<String> newarray = new ArrayList<>();
		for (String s : array) {
			newarray.add(SpigotUtils.color(s));
		}
		return newarray.toArray(new String[0]);
	}

	/*
	 * Used to replace Strings in an ArrayList
	 */

	public static ArrayList<String> replaceArray(List<String> array, String oldChar, String newChar) {
		ArrayList<String> newarray = new ArrayList<>();
		for (String s : array) {
			newarray.add(s.replaceAll(oldChar, newChar));
		}

		return newarray;
	}

	/*
	 * Calculate chance out of 100
	 * 
	 * @param The chance
	 * 
	 * @return whether the outcome happened
	 */

	public static Boolean chance(int chance) {
		Random rnd = new Random();
		int i = rnd.nextInt(100) + 1;

		if (i <= chance) {
			return true;
		} else {
			return false;
		}
	}

	public static String getCountry(String ip) {
		try {
			URL url = new URL("http://ip-api.com/json/" + ip);
			URLConnection connection = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = in.readLine();
			line = line.replace("}", "");
			line = line.replace("{", "");
			line = line.replace(line.substring(0, 1), "");
			HashMap<String, String> info = new HashMap<>();
			String[] lines = line.split(",");
			for (String s : lines) {
				String[] split = s.split(":");
				info.put(split[0], split[1]);
			}
			return info.get("country");
		} catch (Exception e) {
			return null;
		}

	}

	public static void clearInventory(Player p) {
		PlayerInventory inv = p.getInventory();
		inv.clear();
		inv.setHelmet(null);
		inv.setChestplate(null);
		inv.setLeggings(null);
		inv.setBoots(null);
	}

	private static boolean debugEnabled = true;

	public static void debug(String string) {
		if(debugEnabled) {
			System.out.println("[Debug] " +string);
		}
	}

	public static String serializeList(List<String> list) {
		if(list == null) {
			return "[]";
		}
		return new Gson().toJson(list);
	}

	public static List<String> deserializeList(String s) {
		JsonArray array = new JsonParser().parse(s).getAsJsonArray();
		Type type = new TypeToken<List<String>>(){}.getType();
		return new Gson().fromJson(array, type);
	}

}
