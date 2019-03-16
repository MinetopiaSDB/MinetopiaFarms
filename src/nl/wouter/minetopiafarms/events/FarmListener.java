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
		if (e.getBlock().getType() == Utils.getBeetrootMaterial() || e.getBlock().getType() == Utils.getCropsMaterial()
				|| e.getBlock().getType() == Utils.getMelonMaterial() || e.getBlock().getType() == Material.PUMPKIN
				|| e.getBlock().getType() == Utils.getCarrotBlock()|| e.getBlock().getType() == Utils.getPotatoBlock()) {
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

			if (!(e.getBlock().getState().getData() instanceof Crops)) {
				if (e.getBlock().getType() == Material.PUMPKIN) {
					p.getInventory().addItem(new ItemStack(Material.PUMPKIN, 1));
				} else if (e.getBlock().getType() == Utils.getMelonMaterial()) {
					p.getInventory().addItem(new ItemStack(e.getBlock().getType(), 1));
				}
				e.setCancelled(true);
				Utils.blockReplaces.put(e.getBlock().getLocation(), e.getBlock().getType());
				e.getBlock().setType(Material.AIR);
				return;
			}
			Crops crops = (Crops) e.getBlock().getState().getData();
			if (e.getBlock().getType() == Utils.getCropsMaterial()) {
				if (crops.getState() != CropState.RIPE) {
					e.getPlayer().sendMessage(Main.getMessage("TarweNietVolgroeid"));
					e.setCancelled(true);
					return;
				}

				e.setCancelled(true);
				p.getInventory().addItem(new ItemStack(Material.WHEAT, 1));
				Utils.cropPlaces.add(e.getBlock().getLocation());
				e.getBlock().setType(e.getBlock().getType());
			} else if (e.getBlock().getType() == Utils.getBeetrootMaterial()) {
				if (crops.getState() != CropState.RIPE) {
					e.getPlayer().sendMessage(Main.getMessage("BietenNietVolgroeid"));
					e.setCancelled(true);
					return;
				}

				e.setCancelled(true);
				p.getInventory().addItem(new ItemStack(Material.BEETROOT, 1));
				Utils.cropPlaces.add(e.getBlock().getLocation());
				e.getBlock().setType(e.getBlock().getType());
			} else if (e.getBlock().getType() == Utils.getCarrotBlock()) {
				if (crops.getState() != CropState.RIPE) {
					e.getPlayer().sendMessage(Main.getMessage("WortelNietVolgroeid"));
					e.setCancelled(true);
					return;
				}

				e.setCancelled(true);
				p.getInventory().addItem(new ItemStack(Utils.getCarrotItem(), 1));
				Utils.cropPlaces.add(e.getBlock().getLocation());
				e.getBlock().setType(e.getBlock().getType());
			} else if (e.getBlock().getType() == Utils.getPotatoBlock()) {
				if (crops.getState() != CropState.RIPE) {
					e.getPlayer().sendMessage(Main.getMessage("AardappelNietVolgroeid"));
					e.setCancelled(true);
					return;
				}

				e.setCancelled(true);
				p.getInventory().addItem(new ItemStack(Utils.getPotatoItem(), 1));
				Utils.cropPlaces.add(e.getBlock().getLocation());
				e.getBlock().setType(e.getBlock().getType());
			}
		}
	}
}
