package nl.wouter.minetopiafarms.utils;

import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class Utils {
	public static HashMap<Location, Material> ores = new HashMap<>();
	public static ArrayList<Location> cropPlaces = new ArrayList<>();
	public static HashMap<Location, Material> blockReplaces = new HashMap<>();
	
	public static HashMap<Location, TreeObj> treePlaces = new HashMap<>();

	public static String color(String input) {
		return ChatColor.translateAlternateColorCodes('&', input);
	}

	public static void handleToolDurability(Player p) {
		ItemStack item = p.getInventory().getItemInMainHand();
		ItemMeta meta = item.getItemMeta();
		if (meta instanceof Damageable) {
			Damageable dam = (Damageable) meta;
			if (dam.getDamage() + 2 >= item.getType().getMaxDurability()) {
				p.getInventory().remove(p.getInventory().getItemInMainHand());
			} else {
				dam.setDamage(dam.getDamage() + 2);
			}
			item.setItemMeta(meta);
			p.updateInventory();
		}
	}

	public static class TreeObj {

		Material mat;
		byte data;

		public TreeObj(Material mat, byte data) {
			this.mat = mat;
			this.data = data;
		}

		public byte getData() {
			return data;
		}

		public Material getMaterial() {
			return mat;
		}
	}
}