package nl.wouter.minetopiafarms.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class Utils {
	
	public static ArrayList<Location> ironOres = new ArrayList<>();
	public static ArrayList<Location> wheatPlaces = new ArrayList<>();
	public static HashMap<Location, TreeObj> treePlaces = new HashMap<>();
	
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
		ItemStack item = p.getInventory().getItemInMainHand();
		if (item.getItemMeta() instanceof Damageable) {
			Damageable dam = (Damageable) item.getItemMeta();
			if (dam.getDamage() + 2 >= item.getType().getMaxDurability()) {
				p.getInventory().remove(p.getInventory().getItemInMainHand());
			} else {
				dam.setDamage(dam.getDamage() + 2);
			}
			p.updateInventory();
		}
	}
	
	public static class TreeObj {
		
		Material mat;
		BlockData data;
		
		public TreeObj(Material mat, BlockData data) {
			this.mat = mat;
			this.data = data;
		}
		
		public BlockData getData() {
			return data;
		}
		
		public Material getMaterial() {
			return mat;
		}
	}
}
