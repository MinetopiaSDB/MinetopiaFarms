package nl.wouter.minetopiafarms.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Utils {
	public static HashMap<Location, Material> ores = new HashMap<>();
	public static ArrayList<Location> cropPlaces = new ArrayList<>();
	public static HashMap<Location, Material> blockReplaces = new HashMap<>();
	
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

	@SuppressWarnings("deprecation")
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
				&& !nmsver.startsWith("v1_10_") && !nmsver.startsWith("v1_11_") && !nmsver.startsWith("v1_12_");
	}

	public static Material getCropsMaterial() {
		if (is113orUp()) {
			return Material.valueOf("WHEAT");
		}
		return Material.valueOf("CROPS");
	}

	public static Material getBeetrootMaterial() {
		if (is113orUp()) {
			return Material.valueOf("BEETROOTS");
		}
		return Material.valueOf("BEETROOT_BLOCK");
	}
	

	public static Material getMelonMaterial() {
		if (is113orUp()) {
			return Material.MELON;
		}
		return Material.valueOf("MELON_BLOCK");
	}
	
	public static Material getCarrotItem() {
		if (is113orUp()) {
			return Material.CARROTS;
		}
		return Material.valueOf("CARROT_ITEM");
	}

	public static Material getPotatoItem() {
		if (is113orUp()) {
			return Material.POTATOES;
		}
		return Material.valueOf("POTATO_ITEM");
	}
	
	public static Material getCarrotBlock() {
		if (is113orUp()) {
			return Material.CARROTS;
		}
		return Material.valueOf("CARROT");
	}

	public static Material getPotatoBlock() {
		if (is113orUp()) {
			return Material.POTATOES;
		}
		return Material.valueOf("POTATO");
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
