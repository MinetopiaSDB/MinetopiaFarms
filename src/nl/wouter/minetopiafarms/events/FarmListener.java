package nl.wouter.minetopiafarms.events;

import org.bukkit.CropState;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Crops;

import nl.minetopiasdb.api.SDBPlayer;
import nl.wouter.minetopiafarms.Main;
import nl.wouter.minetopiafarms.utils.CustomFlags;
import nl.wouter.minetopiafarms.utils.Utils;
import nl.wouter.minetopiafarms.utils.XMaterial;

public class FarmListener implements Listener {
	
	/*
	 * Waarom the actual fuck doe ik nog aan opensourcing. Dit ziet er niet uit.
	 */
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (e.getBlock().getType() == XMaterial.BEETROOTS.parseMaterial() || e.getBlock().getType() == XMaterial.WHEAT.parseMaterial()
				|| e.getBlock().getType() == XMaterial.MELON.parseMaterial() || e.getBlock().getType() == XMaterial.PUMPKIN.parseMaterial()
				|| e.getBlock().getType() == XMaterial.CARROTS.parseMaterial() || e.getBlock().getType() == XMaterial.POTATOES.parseMaterial()) {
			if (p.getGameMode() == GameMode.CREATIVE) {
				p.sendMessage(Main.getMessage("Creative"));
				return;
			}
			if (!SDBPlayer.createSDBPlayer(e.getPlayer()).getPrefix().equalsIgnoreCase("Boer")) {
				e.getPlayer().sendMessage(Main.getMessage("BeroepNodig").replaceAll("<Beroep>", "boer"));
				e.setCancelled(true);
				return;
			}
			if (!p.getInventory().getItemInMainHand().getType().toString().contains("HOE")) {
				e.getPlayer().sendMessage(Main.getMessage("ToolNodig").replaceAll("<Tool>", "hoe"));
				e.setCancelled(true);
				return;
			}

			if (!CustomFlags.isAllowed(p, e.getBlock().getLocation(), "farm")) {
				p.sendMessage(Main.getMessage("GeenRegion").replaceAll("<Tag>", "farm"));
				return;
			}

			if (!(e.getBlock().getState().getData() instanceof Crops)) {
				if (e.getBlock().getType() == XMaterial.PUMPKIN.parseMaterial()) {
					p.getInventory().addItem(new ItemStack(XMaterial.PUMPKIN.parseMaterial(), 1));
				} else if (e.getBlock().getType() == XMaterial.MELON.parseMaterial()) {
					p.getInventory().addItem(new ItemStack(e.getBlock().getType(), 1));
				}
				e.setCancelled(true);
				Utils.blockReplaces.put(e.getBlock().getLocation(), e.getBlock().getType());
				e.getBlock().setType(Material.AIR);
				return;
			}
			Crops crops = (Crops) e.getBlock().getState().getData();
			if (e.getBlock().getType() == XMaterial.WHEAT.parseMaterial()) {
				if (crops.getState() != CropState.RIPE) {
					e.getPlayer().sendMessage(Main.getMessage("TarweNietVolgroeid"));
					e.setCancelled(true);
					return;
				}

				e.setCancelled(true);
				p.getInventory().addItem(new ItemStack(Material.WHEAT, 1));
				Utils.cropPlaces.add(e.getBlock().getLocation());
				e.getBlock().setType(e.getBlock().getType());
			} else if (e.getBlock().getType() == XMaterial.BEETROOTS.parseMaterial()) {
				if (crops.getState() != CropState.RIPE) {
					e.getPlayer().sendMessage(Main.getMessage("BietenNietVolgroeid"));
					e.setCancelled(true);
					return;
				}

				e.setCancelled(true);
				p.getInventory().addItem(new ItemStack(XMaterial.BEETROOTS.parseMaterial(), 1));
				Utils.cropPlaces.add(e.getBlock().getLocation());
				e.getBlock().setType(e.getBlock().getType());
			} else if (e.getBlock().getType() == XMaterial.CARROTS.parseMaterial()) {
				if (crops.getState() != CropState.RIPE) {
					e.getPlayer().sendMessage(Main.getMessage("WortelNietVolgroeid"));
					e.setCancelled(true);
					return;
				}

				e.setCancelled(true);
				p.getInventory().addItem(new ItemStack(XMaterial.CARROTS.parseMaterial(), 1));
				Utils.cropPlaces.add(e.getBlock().getLocation());
				e.getBlock().setType(e.getBlock().getType());
			} else if (e.getBlock().getType() == XMaterial.POTATOES.parseMaterial()) {
				if (crops.getState() != CropState.RIPE) {
					e.getPlayer().sendMessage(Main.getMessage("AardappelNietVolgroeid"));
					e.setCancelled(true);
					return;
				}

				e.setCancelled(true);
				p.getInventory().addItem(new ItemStack(XMaterial.POTATOES.parseMaterial(), 1));
				Utils.cropPlaces.add(e.getBlock().getLocation());
				e.getBlock().setType(e.getBlock().getType());
			}
		}
	}
}