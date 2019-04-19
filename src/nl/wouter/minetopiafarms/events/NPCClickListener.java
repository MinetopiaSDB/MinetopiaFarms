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
import nl.wouter.minetopiafarms.utils.XMaterial;

public class NPCClickListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onNPCRightClick(NPCRightClickEvent e) {
		Player clicker = e.getClicker();
		NPC clicked = e.getNPC();
		if (clicked.getName() != "Farm Verkooppunt") {
			e.setCancelled(true);
			return;
		}
		
		if (SDBPlayer.createSDBPlayer(clicker).getPrefix().equalsIgnoreCase("Boer")) {
			int beetcount = 0;
			int wheatcount = 0;
			int meloncount = 0;
			int pumpkincount = 0;
			int carrotcount = 0;
			int potatocount = 0;
			
			if (e.getClicker().getInventory().contains(XMaterial.BEETROOTS.parseMaterial())) {
				for (ItemStack item : clicker.getInventory().getContents()) {
					Material itemmat = item.getType();
					if (itemmat == XMaterial.BEETROOTS.parseMaterial()) {
						clicker.getInventory().removeItem(item);
						beetcount++;
					}
				}
			}
			
			if (e.getClicker().getInventory().contains(XMaterial.WHEAT.parseMaterial())) {
				for (ItemStack item : clicker.getInventory().getContents()) {
					Material itemmat = item.getType();
					if (itemmat == XMaterial.WHEAT.parseMaterial()) {
						clicker.getInventory().removeItem(item);
						wheatcount++;
					}
				}
			}
			
			if (e.getClicker().getInventory().contains(XMaterial.MELON.parseMaterial())) {
				for (ItemStack item : clicker.getInventory().getContents()) {
					Material itemmat = item.getType();
					if (itemmat == XMaterial.MELON.parseMaterial()) {
						clicker.getInventory().removeItem(item);
						meloncount++;
					}
				}
			}
			
			if (e.getClicker().getInventory().contains(XMaterial.PUMPKIN.parseMaterial())) {
				for (ItemStack item : clicker.getInventory().getContents()) {
					Material itemmat = item.getType();
					if (itemmat == XMaterial.PUMPKIN.parseMaterial()) {
						clicker.getInventory().removeItem(item);
						pumpkincount++;
					}
				}
			}
			
			if (e.getClicker().getInventory().contains(XMaterial.CARROTS.parseMaterial())) {
				for (ItemStack item : clicker.getInventory().getContents()) {
					Material itemmat = item.getType();
					if (itemmat == XMaterial.CARROTS.parseMaterial()) {
						clicker.getInventory().removeItem(item);
						carrotcount++;
					}
				}
			}
			
			if (e.getClicker().getInventory().contains(XMaterial.POTATOES.parseMaterial())) {
				for (ItemStack item : clicker.getInventory().getContents()) {
					Material itemmat = item.getType();
					if (itemmat == XMaterial.POTATOES.parseMaterial()) {
						clicker.getInventory().removeItem(item);
						potatocount++;
					}
				}
			}
			
			int totaalprijs = (beetcount * Main.getPlugin().getConfig().getInt("TerugverkoopPrijs.Boer.BEETROOTS"))
					+ (wheatcount * Main.getPlugin().getConfig().getInt("TerugverkoopPrijs.Boer.WHEAT"))
					+ (meloncount * Main.getPlugin().getConfig().getInt("TerugverkoopPrijs.Boer.MELON"))
					+ (pumpkincount * Main.getPlugin().getConfig().getInt("TerugverkoopPrijs.Boer.PUMPKIN"))
					+ (carrotcount * Main.getPlugin().getConfig().getInt("TerugverkoopPrijs.Boer.CARROTS"))
					+ (potatocount * Main.getPlugin().getConfig().getInt("TerugverkoopPrijs.Boer.POTATOES"));
		
			API.getEcon().depositPlayer(clicker, totaalprijs);
			clicker.sendMessage(Main.getMessage("GeldBetaald").replaceAll("<Bedrag>", String.valueOf(totaalprijs)));
			e.setCancelled(true);

		} else if (SDBPlayer.createSDBPlayer(clicker).getPrefix().equalsIgnoreCase("Mijnwerker")) {
			
			int coalcount = 0;
			int ironcount = 0;
			int emeraldcount = 0;
			int goldcount = 0;
			int lapiscount = 0;
			int redstonecount = 0;
			int diamondcount = 0;
			
			if (e.getClicker().getInventory().contains(XMaterial.COAL.parseMaterial())) {
				for (ItemStack item : clicker.getInventory().getContents()) {
					Material itemmat = item.getType();
					if (itemmat == XMaterial.COAL.parseMaterial()) {
						clicker.getInventory().removeItem(item);
						coalcount++;
					}
				}
			}
			
			if (e.getClicker().getInventory().contains(XMaterial.DIAMOND.parseMaterial())) {
				for (ItemStack item : clicker.getInventory().getContents()) {
					Material itemmat = item.getType();
					if (itemmat == XMaterial.DIAMOND.parseMaterial()) {
						clicker.getInventory().removeItem(item);
						diamondcount++;
					}
				}
			}
			
			if (e.getClicker().getInventory().contains(XMaterial.EMERALD.parseMaterial())) {
				for (ItemStack item : clicker.getInventory().getContents()) {
					Material itemmat = item.getType();
					if (itemmat == XMaterial.EMERALD.parseMaterial()) {
						clicker.getInventory().removeItem(item);
						emeraldcount++;
					}
				}
			}
			
			if (e.getClicker().getInventory().contains(XMaterial.GOLD_INGOT.parseMaterial())) {
				for (ItemStack item : clicker.getInventory().getContents()) {
					Material itemmat = item.getType();
					if (itemmat == XMaterial.GOLD_INGOT.parseMaterial()) {
						clicker.getInventory().removeItem(item);
						goldcount++;
					}
				}
			}
			
			if (e.getClicker().getInventory().contains(XMaterial.IRON_INGOT.parseMaterial())) {
				for (ItemStack item : clicker.getInventory().getContents()) {
					Material itemmat = item.getType();
					if (itemmat == XMaterial.IRON_INGOT.parseMaterial()) {
						clicker.getInventory().removeItem(item);
						ironcount++;
					}
				}
			}
			
			if (e.getClicker().getInventory().contains(XMaterial.LAPIS_LAZULI.parseMaterial())) {
				for (ItemStack item : clicker.getInventory().getContents()) {
					Material itemmat = item.getType();
					if (itemmat == XMaterial.LAPIS_LAZULI.parseMaterial()) {
						clicker.getInventory().removeItem(item);
						lapiscount++;
					}
				}
			}
			
			if (e.getClicker().getInventory().contains(XMaterial.REDSTONE.parseMaterial())) {
				for (ItemStack item : clicker.getInventory().getContents()) {
					Material itemmat = item.getType();
					if (itemmat == XMaterial.REDSTONE.parseMaterial()) {
						clicker.getInventory().removeItem(item);
						redstonecount++;
					}
				}
			}
			
			int totaalprijs = (coalcount * Main.getPlugin().getConfig().getInt("TerugverkoopPrijs.Boer.COAL_ORE"))
					+ (diamondcount * Main.getPlugin().getConfig().getInt("TerugverkoopPrijs.Boer.DIAMOND_ORE"))
					+ (emeraldcount * Main.getPlugin().getConfig().getInt("TerugverkoopPrijs.Boer.EMERALD_ORE"))
					+ (goldcount * Main.getPlugin().getConfig().getInt("TerugverkoopPrijs.Boer.GOLD_ORE"))
					+ (ironcount * Main.getPlugin().getConfig().getInt("TerugverkoopPrijs.Boer.IRON_ORE"))
					+ (lapiscount * Main.getPlugin().getConfig().getInt("TerugverkoopPrijs.Boer.LAPIS_ORE"))
					+ (redstonecount * Main.getPlugin().getConfig().getInt("TerugverkoopPrijs.Boer.REDSTONE_ORE"));
		
			API.getEcon().depositPlayer(clicker, totaalprijs);
			clicker.sendMessage(Main.getMessage("GeldBetaald").replaceAll("<Bedrag>", String.valueOf(totaalprijs)));
			e.setCancelled(true);
			
		} else if (SDBPlayer.createSDBPlayer(clicker).getPrefix().equalsIgnoreCase("Visser")) {
			
			int counter = 0;
			if (e.getClicker().getInventory().contains(XMaterial.PUFFERFISH.parseMaterial()) || e.getClicker().getInventory().contains(XMaterial.TROPICAL_FISH.parseMaterial())) {
				for (ItemStack item : clicker.getInventory().getContents()) {
					Material itemmat = item.getType();
					if (itemmat == XMaterial.PUFFERFISH.parseMaterial()) {
						clicker.getInventory().removeItem(item);
						counter++;
					} else if (itemmat == XMaterial.TROPICAL_FISH.parseMaterial()) {
						clicker.getInventory().removeItem(item);
						counter++;
					}
				}
			}
			
			int totaalprijs = counter * Main.getPlugin().getConfig().getInt("TerugverkoopPrijs.Visser");
			API.getEcon().depositPlayer(clicker, totaalprijs);
			clicker.sendMessage(Main.getMessage("GeldBetaald").replaceAll("<Bedrag>", String.valueOf(totaalprijs)));
			e.setCancelled(true);
			
		} else if (SDBPlayer.createSDBPlayer(clicker).getPrefix().equalsIgnoreCase("Houthakker")) {
			
			int counter = 0;
			
			for (ItemStack item : clicker.getInventory().getContents()) {
				Material itemmat = item.getType();
				if (itemmat.toString().contains("_LOG")) {
					clicker.getInventory().removeItem(item);
					counter++;
				}
			}
			
			int totaalprijs = counter * Main.getPlugin().getConfig().getInt("TerugverkoopPrijs.Houthakker");
			API.getEcon().depositPlayer(clicker, totaalprijs);
			clicker.sendMessage(Main.getMessage("GeldBetaald").replaceAll("<Bedrag>", String.valueOf(totaalprijs)));
			e.setCancelled(true);
			
		} else {
			clicker.sendMessage(Main.getMessage("BeroepNodig").replaceAll("<Beroep>", "boer, mijnwerker, visser of houthakker"));
			e.setCancelled(true);
			return;
		}
	}

}
