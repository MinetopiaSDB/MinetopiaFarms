package nl.wouter.minetopiafarms.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import nl.wouter.minetopiafarms.Main;
import nl.wouter.minetopiafarms.utils.ItemBuilder;

public class KiesCMD implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return true;
		}
		Player p = (Player) sender;

		p.sendMessage(Main.getMessage("VeranderenVanEenBaan").replaceAll("<Bedrag>",
				"" + Main.getPlugin().getConfig().getInt("KostenVoorEenBaan")));

		Inventory inv = Bukkit.createInventory(null, 9 * 3, Main.getMessage("InventoryTitle"));

		inv.setItem(10, new ItemBuilder(Material.DIAMOND_HOE)
				.setName(Main.getMessage("ItemName").replaceAll("<Beroep>", "Boer"))
				.addLoreLine(Main.getMessage("ItemLore").replaceAll("<Beroep>", "boer")).toItemStack());

		inv.setItem(12, new ItemBuilder(Material.DIAMOND_AXE)
				.setName(Main.getMessage("ItemName").replaceAll("<Beroep>", "Houthakker"))
				.addLoreLine(Main.getMessage("ItemLore").replaceAll("<Beroep>", "houthakker")).toItemStack());

		inv.setItem(14, new ItemBuilder(Material.DIAMOND_PICKAXE)
				.setName(Main.getMessage("ItemName").replaceAll("<Beroep>", "Mijnwerker"))
				.addLoreLine(Main.getMessage("ItemLore").replaceAll("<Beroep>", "mijnwerker")).toItemStack());
		
		inv.setItem(16, new ItemBuilder(Material.FISHING_ROD)
				.setName(Main.getMessage("ItemName").replaceAll("<Beroep>", "Visser"))
				.addLoreLine(Main.getMessage("ItemLore").replaceAll("<Beroep>", "visser")).toItemStack());
		
		p.openInventory(inv);

		return true;
	}
}
