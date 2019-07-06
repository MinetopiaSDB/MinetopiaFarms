package nl.wouter.minetopiafarms.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import nl.minetopiasdb.api.API;
import nl.minetopiasdb.api.SDBPlayer;
import nl.wouter.minetopiafarms.Main;
import nl.wouter.minetopiafarms.utils.Utils;
import nl.wouter.minetopiafarms.utils.XMaterial;

public class NPCClickListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onNPCRightClick(NPCRightClickEvent e) {
		Player clicker = e.getClicker();
		NPC clicked = e.getNPC();
		if (Main.getMessage("NPC.Name").equals(clicked.getName())) {

			if (SDBPlayer.createSDBPlayer(clicker).getPrefix().equalsIgnoreCase("Boer")) {
				double paymentAmount = 0;
				for (ItemStack item : clicker.getInventory().getContents()) {
					if (item != null && item.getType() != null) {
						if (item.getType() == XMaterial.BEETROOTS.parseMaterial()) {
							clicker.getInventory().removeItem(item);
							paymentAmount = paymentAmount
									+ (Main.getPlugin().getConfig().getDouble("TerugverkoopPrijs.Boer.BEETROOTS")
											* item.getAmount());
						} else if (item.getType() == XMaterial.WHEAT.parseMaterial()) {
							clicker.getInventory().removeItem(item);
							paymentAmount = paymentAmount
									+ (Main.getPlugin().getConfig().getDouble("TerugverkoopPrijs.Boer.WHEAT")
											* item.getAmount());
						} else if (item.getType() == XMaterial.MELON.parseMaterial()) {
							clicker.getInventory().removeItem(item);
							paymentAmount = paymentAmount
									+ (Main.getPlugin().getConfig().getDouble("TerugverkoopPrijs.Boer.MELON")
											* item.getAmount());
						} else if (item.getType() == XMaterial.PUMPKIN.parseMaterial()) {
							clicker.getInventory().removeItem(item);
							paymentAmount = paymentAmount
									+ (Main.getPlugin().getConfig().getDouble("TerugverkoopPrijs.Boer.PUMPKIN")
											* item.getAmount());
						} else if (item.getType() == XMaterial.CARROTS.parseMaterial()) {
							clicker.getInventory().removeItem(item);
							paymentAmount = paymentAmount
									+ (Main.getPlugin().getConfig().getDouble("TerugverkoopPrijs.Boer.CARROTS")
											* item.getAmount());
						} else if (item.getType() == XMaterial.POTATOES.parseMaterial()) {
							clicker.getInventory().removeItem(item);
							paymentAmount = paymentAmount
									+ (Main.getPlugin().getConfig().getDouble("TerugverkoopPrijs.Boer.POTATOES")
											* item.getAmount());
						}
					}
				}

				API.getEcon().depositPlayer(clicker, paymentAmount);
				clicker.sendMessage(
						Main.getMessage("GeldBetaald").replaceAll("<Bedrag>", Utils.formatMoney(paymentAmount)));
				API.updateScoreboard(clicker);
			} else if (SDBPlayer.createSDBPlayer(clicker).getPrefix().equalsIgnoreCase("Mijnwerker")) {
				double paymentAmount = 0;
				for (ItemStack item : clicker.getInventory().getContents()) {
					if (item != null && item.getType() != null) {
						if (item.getType() == XMaterial.COAL_ORE.parseMaterial()) {
							clicker.getInventory().removeItem(item);
							paymentAmount = paymentAmount
									+ (Main.getPlugin().getConfig().getDouble("TerugverkoopPrijs.Mijnwerker.COAL_ORE")
											* item.getAmount());
						} else if (item.getType() == XMaterial.IRON_ORE.parseMaterial()) {
							clicker.getInventory().removeItem(item);
							paymentAmount = paymentAmount
									+ (Main.getPlugin().getConfig().getDouble("TerugverkoopPrijs.Mijnwerker.IRON_ORE")
											* item.getAmount());
						} else if (item.getType() == XMaterial.EMERALD_ORE.parseMaterial()) {
							clicker.getInventory().removeItem(item);
							paymentAmount = paymentAmount
									+ (Main.getPlugin().getConfig().getDouble("TerugverkoopPrijs.Mijnwerker.EMERALD_ORE")
											* item.getAmount());
						} else if (item.getType() == XMaterial.GOLD_ORE.parseMaterial()) {
							clicker.getInventory().removeItem(item);
							paymentAmount = paymentAmount
									+ (Main.getPlugin().getConfig().getDouble("TerugverkoopPrijs.Mijnwerker.GOLD_ORE")
											* item.getAmount());
						} else if (item.getType() == XMaterial.LAPIS_ORE.parseMaterial()) {
							clicker.getInventory().removeItem(item);
							paymentAmount = paymentAmount
									+ (Main.getPlugin().getConfig().getDouble("TerugverkoopPrijs.Mijnwerker.LAPIS_ORE")
											* item.getAmount());
						} else if (item.getType() == XMaterial.REDSTONE_ORE.parseMaterial()) {
							clicker.getInventory().removeItem(item);
							paymentAmount = paymentAmount
									+ (Main.getPlugin().getConfig().getDouble("TerugverkoopPrijs.Mijnwerker.REDSTONE_ORE")
											* item.getAmount());
						} else if (item.getType() == XMaterial.DIAMOND_ORE.parseMaterial()) {
							clicker.getInventory().removeItem(item);
							paymentAmount = paymentAmount
									+ (Main.getPlugin().getConfig().getDouble("TerugverkoopPrijs.Mijnwerker.DIAMOND_ORE")
											* item.getAmount());
						}
					}
				}

				API.getEcon().depositPlayer(clicker, paymentAmount);
				clicker.sendMessage(
						Main.getMessage("GeldBetaald").replaceAll("<Bedrag>", Utils.formatMoney(paymentAmount)));
				API.updateScoreboard(clicker);
			} else if (SDBPlayer.createSDBPlayer(clicker).getPrefix().equalsIgnoreCase("Visser")) {

				double paymentAmount = 0;
				if (e.getClicker().getInventory().contains(XMaterial.PUFFERFISH.parseMaterial())
						|| e.getClicker().getInventory().contains(XMaterial.TROPICAL_FISH.parseMaterial())) {
					for (ItemStack item : clicker.getInventory().getContents()) {
						if (item != null && item.getType() != null) {
							Material itemmat = item.getType();
							if (itemmat == XMaterial.PUFFERFISH.parseMaterial()
									|| itemmat == XMaterial.TROPICAL_FISH.parseMaterial()) {
								clicker.getInventory().removeItem(item);
								paymentAmount = paymentAmount + (item.getAmount()
										* Main.getPlugin().getConfig().getInt("TerugverkoopPrijs.Visser"));
							}
						}
					}
				}

				API.getEcon().depositPlayer(clicker, paymentAmount);
				clicker.sendMessage(
						Main.getMessage("GeldBetaald").replaceAll("<Bedrag>", Utils.formatMoney(paymentAmount)));
				API.updateScoreboard(clicker);
			} else if (SDBPlayer.createSDBPlayer(clicker).getPrefix().equalsIgnoreCase("Houthakker")) {
				double paymentAmount = 0;
				for (ItemStack item : clicker.getInventory().getContents()) {
					if (item != null && item.getType() != null) {
						if (item.getType().toString().contains("LOG")) {
							clicker.getInventory().removeItem(item);
							paymentAmount = paymentAmount + (item.getAmount()
									* Main.getPlugin().getConfig().getInt("TerugverkoopPrijs.Houthakker"));
						}
					}
				}

				API.getEcon().depositPlayer(clicker, paymentAmount);
				clicker.sendMessage(
						Main.getMessage("GeldBetaald").replaceAll("<Bedrag>", Utils.formatMoney(paymentAmount)));
				API.updateScoreboard(clicker);
			} else {
				clicker.sendMessage(Main.getMessage("BeroepNodig").replaceAll("<Beroep>",
						"boer, mijnwerker, visser of houthakker"));
				e.setCancelled(true);
				return;
			}
		}
	}
}
