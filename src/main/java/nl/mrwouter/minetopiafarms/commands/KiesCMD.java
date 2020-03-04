package nl.mrwouter.minetopiafarms.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import nl.mrwouter.minetopiafarms.Main;
import nl.mrwouter.minetopiafarms.utils.ItemBuilder;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class KiesCMD implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return true;
		}
		Player p = (Player) sender;

		p.sendMessage(Main.getMessage("VeranderenVanEenBaan").replaceAll("<Bedrag>",
				"" + Main.getPlugin().getConfig().getInt("KostenVoorEenBaan")));

		Inventory inv = Bukkit.createInventory(null, 9 * 3, Main.getMessage("InventoryTitle"));

		HashMap<String, Material> beroepen = new HashMap<>();
		beroepen.put("Boer", Material.DIAMOND_HOE);
		beroepen.put("Houthakker", Material.DIAMOND_AXE);
		beroepen.put("Mijnwerker", Material.DIAMOND_PICKAXE);
		beroepen.put("Visser", Material.FISHING_ROD);

//		Verwijder de beroepen die uitgeschakeld zijn
		beroepen.keySet().stream().filter(beroep -> !Main.getPlugin().getConfig().getBoolean("GebruikBaan." + beroep))
				.collect(Collectors.toList()).forEach(beroep -> beroepen.remove(beroep));

		if (beroepen.size() == 0) {
			inv.setItem(13, new ItemBuilder(Material.BARRIER)
				.setName("§4Er zijn geen beroepen beschikbaar!")
				.addLoreLine("§3Activeer beroepen in de config.").toItemStack());
		} else {
			int startingIndex = 14 - beroepen.size(), index = 0;
			for (String beroep : beroepen.keySet()) {
				inv.setItem(startingIndex + index * 2, new ItemBuilder(beroepen.get(beroep))
					.setName(Main.getMessage("ItemName").replace("<Beroep>", beroep))
					.addLoreLine(Main.getMessage("ItemLore").replace("<Beroep>", beroep.toLowerCase())).toItemStack());
				index++;
			}
		}
		
		p.openInventory(inv);

		return true;
	}
}
