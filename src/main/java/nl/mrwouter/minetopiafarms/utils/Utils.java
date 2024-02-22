package nl.mrwouter.minetopiafarms.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Utils {
	public static HashMap<Location, Material> ores = new HashMap<>();
	public static ArrayList<GrowingCrop> cropPlaces = new ArrayList<>();
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

	public static String formatMoney(double money) {
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
		return decimalFormat.format(money).replace(",", "tmp").replace(".", ",").replace("tmp", ".");
	}

	/*
	 * XMaterial isn't perfect, especially when dealing with crops/carrots/etc. Just having these methods here to guarantee the good item is given.
	 */
	public static Material getCropsMaterial() {
		if (is113orUp()) {
			return Material.valueOf("WHEAT");
		}
		return Material.valueOf("CROPS");
	}

	/*
	 * XMaterial isn't perfect, especially when dealing with crops/carrots/etc. Just having these methods here to guarantee the good item is given.
	 */
	public static Material getBeetrootMaterial() {
		if (is113orUp()) {
			return Material.valueOf("BEETROOTS");
		}
		return Material.valueOf("BEETROOT_BLOCK");
	}

	/*
	 * XMaterial isn't perfect, especially when dealing with crops/carrots/etc. Just having these methods here to guarantee the good item is given.
	 */
	public static Material getMelonMaterial() {
		if (is113orUp()) {
			return Material.valueOf("MELON");
		}
		return Material.valueOf("MELON_BLOCK");
	}

	/*
	 * XMaterial isn't perfect, especially when dealing with crops/carrots/etc. Just having these methods here to guarantee the good item is given.
	 */
	public static Material getCarrotItem() {
		if (is113orUp()) {
			return Material.CARROT;
		}
		return Material.valueOf("CARROT_ITEM");
	}

	/*
	 * XMaterial isn't perfect, especially when dealing with crops/carrots/etc. Just having these methods here to guarantee the good item is given.
	 */
	public static Material getPotatoItem() {
		if (is113orUp()) {
			return Material.POTATO;
		}
		return Material.valueOf("POTATO_ITEM");
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

	public static class GrowingCrop {

		public Location location;
		public int time;// ticks

		public GrowingCrop(Location location) {
			this.location = location;
			this.time = 0;
		}

	}

	public static void buildConfig(Configuration config){
		config.addDefault("KostenVoorEenBaan", 2500);
		config.addDefault("KrijgItemsBijBaanSelect", true);
		config.addDefault("PrefixEnabled", true);

		config.addDefault("Banen.Mijnwerker.Enabled", true);
		config.addDefault("Banen.Mijnwerker.Item", XMaterial.DIAMOND_PICKAXE.name());
		config.addDefault("Banen.Boer.Enabled", true);
		config.addDefault("Banen.Boer.Item", XMaterial.DIAMOND_HOE.name());
		config.addDefault("Banen.Houthakker.Enabled", true);
		config.addDefault("Banen.Houthakker.Item", XMaterial.DIAMOND_AXE.name());

		config.addDefault("TerugverkoopPrijs.Mijnwerker.COAL_ORE", 10);
		config.addDefault("TerugverkoopPrijs.Mijnwerker.IRON_ORE", 25);
		config.addDefault("TerugverkoopPrijs.Mijnwerker.EMERALD_ORE", 30);
		config.addDefault("TerugverkoopPrijs.Mijnwerker.GOLD_ORE", 25);
		config.addDefault("TerugverkoopPrijs.Mijnwerker.LAPIS_ORE", 35);
		config.addDefault("TerugverkoopPrijs.Mijnwerker.REDSTONE_ORE", 25);
		config.addDefault("TerugverkoopPrijs.Mijnwerker.DIAMOND_ORE", 80);
		config.addDefault("TerugverkoopPrijs.Boer.BEETROOTS", 35);
		config.addDefault("TerugverkoopPrijs.Boer.WHEAT", 10);
		config.addDefault("TerugverkoopPrijs.Boer.MELON", 30);
		config.addDefault("TerugverkoopPrijs.Boer.PUMPKIN", 30);
		config.addDefault("TerugverkoopPrijs.Boer.CARROTS", 20);
		config.addDefault("TerugverkoopPrijs.Boer.POTATOES", 20);
		config.addDefault("TerugverkoopPrijs.Houthakker", 25);

		config.addDefault("CommandsUitvoerenBijBaanWissel.Boer",
				Collections.singletonList("Typ hier jouw commands"));
		config.addDefault("CommandsUitvoerenBijBaanWissel.Houthakker",
				Collections.singletonList("Typ hier jouw commands"));
		config.addDefault("CommandsUitvoerenBijBaanWissel.Mijnwerker",
				Collections.singletonList("Typ hier jouw commands"));

		config.addDefault("ItemsBijBaanSelect.Boer", Collections.singletonList("DIAMOND_HOE"));
		config.addDefault("ItemsBijBaanSelect.Mijnwerker", Collections.singletonList("DIAMOND_PICKAXE"));
		config.addDefault("ItemsBijBaanSelect.Houthakker", Collections.singletonList("DIAMOND_AXE"));

		config.addDefault("Messages.VeranderenVanEenBaan",
				"&4Let op! &cHet veranderen van beroep kost &4€ <Bedrag>,-&c.");
		config.addDefault("Messages.InventoryTitle", "&3Kies een &bberoep&3!");
		config.addDefault("Messages.ItemName", "&3<Beroep>");
		config.addDefault("Messages.ItemLore", "&3Kies het beroep &b<Beroep>");

		config.addDefault("Messages.BeroepNodig", "&4ERROR: &cHiervoor heb je het beroep &4<Beroep> &cnodig!");
		config.addDefault("Messages.ToolNodig", "&4ERROR: &cHiervoor heb je een &4<Tool> &cnodig!");
		config.addDefault("Messages.TarweNietVolgroeid", "&4ERROR: &cDeze tarwe is niet volgroeid!");
		config.addDefault("Messages.BietenNietVolgroeid", "&4ERROR: &cDeze bieten zijn niet volgroeid!");
		config.addDefault("Messages.WortelNietVolgroeid", "&4ERROR: &cDeze wortel is niet volgroeid!");
		config.addDefault("Messages.AardappelNietVolgroeid", "&4ERROR: &cDeze aardappel is niet volgroeid!");

		config.addDefault("Messages.TeWeinigGeld",
				"&4ERROR: &cOm van baan te veranderen heb je &4\u20ac <Bedrag> &cnodig!");

		config.addDefault("Messages.GeldBetaald",
				"&3Gelukt! Wij hebben jou &b\u20ac <Bedrag> &3betaald voor jouw opgehaalde spullen!");

		config.addDefault("Messages.BaanVeranderd", "&3Jouw baan is succesvol veranderd naar &b<Baan>&3.");

		config.addDefault("Messages.GeenRegion", "&4ERROR: &cDeze region moet de tag &4'<Tag>' &chebben.");
		config.addDefault("Messages.Creative",
				"&3Omdat jij in &bCREATIVE &3zit heb jij een MinetopiaFarms bypass.");
		config.addDefault("Messages.MateriaalOnbekend", "&4ERROR: &cJe mag dit blok niet slopen.");

		config.addDefault("Messages.NPC.Name", "&6Verkooppunt");
		config.addDefault("Messages.NPC.Skin.Name", "MrWouter");
		config.addDefault("Messages.NPC.Skin.UUID", "836ce767-0e25-45a0-8012-fa1864d2b6aa");

		config.addDefault("scheduler.cropgrow", 120);
		config.addDefault("scheduler.miner.COAL_ORE", 2400);
		config.addDefault("scheduler.miner.DIAMOND_ORE", 2400);
		config.addDefault("scheduler.miner.EMERALD_ORE", 2400);
		config.addDefault("scheduler.miner.GOLD_ORE", 2400);
		config.addDefault("scheduler.miner.IRON_ORE", 2400);
		config.addDefault("scheduler.miner.LAPIS_ORE", 2400);
		config.addDefault("scheduler.miner.REDSTONE_ORE", 2400);

		config.set("ItemsBijBaanSelect.Visser", null);
		config.set("MogelijkeItemsBijVangst", null);
		config.set("VangstItemNaam", null);
		config.set("VangstItemLore", null);
		config.set("Banen.Visser.Enabled", null);
		config.set("Banen.Visser.Item", null);
		config.set("TerugverkoopPrijs.Visser", null);
		config.set("CommandsUitvoerenBijBaanWissel.Visser", null);
	}
}