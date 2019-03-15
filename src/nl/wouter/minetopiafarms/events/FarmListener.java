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

public class FarmListener implements Listener {
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (e.getBlock().getType() == Utils.getCropsMaterial()) {
			if (p.getGameMode() == GameMode.CREATIVE) {
				p.sendMessage(Main.getMessage("Creative"));
				return;
			}
			if (!SDBPlayer.createSDBPlayer(e.getPlayer()).getPrefix().equalsIgnoreCase("Boer")) {
				e.getPlayer().sendMessage(Main.getMessage("BeroepNodig").replaceAll("<Beroep>", "boer"));
				e.setCancelled(true);
				return;
			}
			if (!p.getItemInHand().getType().toString().contains("HOE")) {
				e.getPlayer().sendMessage(Main.getMessage("ToolNodig").replaceAll("<Tool>", "hoe"));
				e.setCancelled(true);
				return;
			}

			if (!CustomFlags.isAllowed(p, e.getBlock().getLocation(), "farm")) {
				p.sendMessage(Main.getMessage("GeenRegion").replaceAll("<Tag>", "farm"));
				return;
			}

			Crops crops = (Crops) e.getBlock().getState().getData();
			if (Utils.getCropsMaterial() == Material.WHEAT_SEEDS) {
				if (crops.getState() != CropState.RIPE) {
					e.getPlayer().sendMessage(Main.getMessage("TarweNietVolgroeid"));
					e.setCancelled(true);
					return;
				}

				e.setCancelled(true);
				p.getInventory().addItem(new ItemStack(Material.WHEAT, 1));
				Utils.wheatPlaces.add(e.getBlock().getLocation());
				e.getBlock().setType(Utils.getCropsMaterial());
			} else if (Utils.getCropsMaterial() == Material.PUMPKIN_SEEDS) {
				if (crops.getState() != CropState.RIPE) {
					e.getPlayer().sendMessage(Main.getMessage("PompoenNietVolgroeid")); 
					e.setCancelled(true);
					return;
				}

				e.setCancelled(true);
				p.getInventory().addItem(new ItemStack(Material.PUMPKIN, 1));
				Utils.pumpkinPlaces.add(e.getBlock().getLocation());
				e.getBlock().setType(Utils.getCropsMaterial());
			} else if (Utils.getCropsMaterial() == Material.BEETROOT_SEEDS) {
				if (crops.getState() != CropState.RIPE) {
					e.getPlayer().sendMessage(Main.getMessage("BietenNietVolgroeid")); 
					e.setCancelled(true);
					return;
				}

				e.setCancelled(true);
				p.getInventory().addItem(new ItemStack(Material.BEETROOT, 1));
				Utils.beetrootPlaces.add(e.getBlock().getLocation());
				e.getBlock().setType(Utils.getCropsMaterial());
			} else if (Utils.getCropsMaterial() == Material.MELON_SEEDS) {
				if (crops.getState() != CropState.RIPE) {
					e.getPlayer().sendMessage(Main.getMessage("MeloenNietVolgroeid")); 
					e.setCancelled(true);
					return;
				}

				e.setCancelled(true);
				p.getInventory().addItem(new ItemStack(Material.MELON, 1));
				Utils.melonPlaces.add(e.getBlock().getLocation());
				e.getBlock().setType(Utils.getCropsMaterial());
			} else if (Utils.getCropsMaterial() == Material.CARROT) {
				if (crops.getState() != CropState.RIPE) {
					e.getPlayer().sendMessage(Main.getMessage("WortelNietVolgroeid")); 
					e.setCancelled(true);
					return;
				}

				e.setCancelled(true);
				p.getInventory().addItem(new ItemStack(Material.CARROT, 1));
				Utils.carrotPlaces.add(e.getBlock().getLocation());
				e.getBlock().setType(Utils.getCropsMaterial());
			} else if (Utils.getCropsMaterial() == Material.POTATO) {
				if (crops.getState() != CropState.RIPE) {
					e.getPlayer().sendMessage(Main.getMessage("AardappelNietVolgroeid")); 
					e.setCancelled(true);
					return;
				}

				e.setCancelled(true);
				p.getInventory().addItem(new ItemStack(Material.POTATO, 1));
				Utils.potatoPlaces.add(e.getBlock().getLocation());
				e.getBlock().setType(Utils.getCropsMaterial());
			}
		}
	}
}
