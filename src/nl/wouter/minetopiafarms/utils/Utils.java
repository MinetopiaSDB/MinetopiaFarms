package nl.wouter.minetopiafarms.utils;

import org.bukkit.*;
import java.util.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.entity.*;

public class Utils {
	
	public static ArrayList<Location> ironOres = new ArrayList<>();
	public static ArrayList<Location> wheatPlaces = new ArrayList<>();
	
	public static String color(String input) {
		return ChatColor.translateAlternateColorCodes('&', input);
	}

	public static ItemStack createItemStack(Material mat, String name, List<String> lore) {
		ItemStack is = new ItemStack(mat);
		ItemMeta ism = is.getItemMeta();
		if (name != null) {
			ism.setDisplayName(color(name));
		}
		ism.setLore(lore);
		is.setItemMeta(ism);
		return is;
	}

	public static void handleToolDurability(Player p) {
		if ((short) (p.getInventory().getItemInMainHand().getDurability() + 2) >= p.getInventory().getItemInMainHand()
				.getType().getMaxDurability()) {
			p.getInventory().remove(p.getInventory().getItemInMainHand());
		} else {
			p.getInventory().getItemInMainHand()
					.setDurability((short) (p.getInventory().getItemInMainHand().getDurability() + 2));
		}
		p.updateInventory();
	}
	
	public static boolean is113orUp() {
		String nmsver = Bukkit.getServer().getClass().getPackage().getName();
		nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
		return !nmsver.startsWith("v1_7_") && !nmsver.startsWith("v1_8_") && !nmsver.startsWith("v1_9_")
				&& !nmsver.startsWith("v1_10_") && !nmsver.startsWith("v1_11_");
	}
	
	public static Material getCropsMaterial() {
		if (is113orUp()) {
			return Material.valueOf("WHEAT");
		}
		return Material.valueOf("CROPS");
	}
}
