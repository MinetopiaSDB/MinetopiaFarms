package nl.wouter.minetopiafarms.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import nl.minetopiasdb.api.API;
import nl.minetopiasdb.api.SDBPlayer;
import nl.wouter.minetopiafarms.Main;

public class InventoryClickListener implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		InventoryView view = e.getView();
		if (view.getTitle().equalsIgnoreCase(Main.getMessage("InventoryTitle"))) {
			e.setCancelled(true);
			String beroep = "none";
			if (e.getSlot() == 10) {
				beroep = "Boer";
			} else if (e.getSlot() == 13) {
				beroep = "Houthakker";
			} else if (e.getSlot() == 16) {
				beroep = "Mijnwerker";
			}
			if (!beroep.equalsIgnoreCase("none")) {
				if (API.getEcon().getBalance(((Player) e.getWhoClicked())) < Main.getPlugin().getConfig()
						.getInt("KostenVoorEenBaan")) {
					e.getWhoClicked().sendMessage(Main.getMessage("TeWeinigGeld").replaceAll("<Bedrag>",
							"" + Main.getPlugin().getConfig().getInt("KostenVoorEenBaan")));
					return;
				}
				e.getWhoClicked().closeInventory();
				e.getWhoClicked().sendMessage(Main.getMessage("BaanVeranderd").replaceAll("<Baan>", beroep));

				API.getEcon().withdrawPlayer(((Player) e.getWhoClicked()),
						Main.getPlugin().getConfig().getInt("KostenVoorEenBaan"));
				SDBPlayer pl = SDBPlayer.createSDBPlayer(((Player) e.getWhoClicked()));
				pl.setPrefix(beroep);
				
				API.updateScoreboard(((Player) e.getWhoClicked()));

				if (beroep.equalsIgnoreCase("mijnwerker")) {
					if (Main.getPlugin().getConfig().getBoolean("KrijgItemsBijBaanSelect")) {
						e.getWhoClicked().getInventory().addItem(new ItemStack(Material.DIAMOND_PICKAXE));
					}
					for (String cmd: Main.getPlugin().getConfig().getStringList("CommandsUitvoerenBijBaanWissel.Mijnwerker")) {
						if (!cmd.equalsIgnoreCase("Typ hier jouw commands")) {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("<Player>", e.getWhoClicked().getName()));
						}
					}
				} else if (beroep.equalsIgnoreCase("boer")) {
					if (Main.getPlugin().getConfig().getBoolean("KrijgItemsBijBaanSelect")) {
						e.getWhoClicked().getInventory().addItem(new ItemStack(Material.DIAMOND_HOE));
					}
					for (String cmd: Main.getPlugin().getConfig().getStringList("CommandsUitvoerenBijBaanWissel.Boer")) {
						if (!cmd.equalsIgnoreCase("Typ hier jouw commands")) {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("<Player>", e.getWhoClicked().getName()));
						}
					}
				} else if (beroep.equalsIgnoreCase("houthakker")) {
					if (Main.getPlugin().getConfig().getBoolean("KrijgItemsBijBaanSelect")) {
						e.getWhoClicked().getInventory().addItem(new ItemStack(Material.DIAMOND_AXE));
					}
					for (String cmd: Main.getPlugin().getConfig().getStringList("CommandsUitvoerenBijBaanWissel.Houthakker")) {
						if (!cmd.equalsIgnoreCase("Typ hier jouw commands")) {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("<Player>", e.getWhoClicked().getName()));
						}
					}
				}
			}
		}
	}
}
