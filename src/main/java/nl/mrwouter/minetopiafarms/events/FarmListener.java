package nl.mrwouter.minetopiafarms.events;

import org.bukkit.CropState;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Crops;

import com.cryptomorin.xseries.XMaterial;

import nl.minetopiasdb.api.playerdata.PlayerManager;
import nl.mrwouter.minetopiafarms.Main;
import nl.mrwouter.minetopiafarms.utils.CustomFlags;
import nl.mrwouter.minetopiafarms.utils.Utils;

@SuppressWarnings("deprecation")
public class FarmListener implements Listener {

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if ((e.getBlock().getType() == Utils.getBeetrootMaterial()
				|| e.getBlock().getType() == Utils.getCropsMaterial()
				|| e.getBlock().getType() == Utils.getMelonMaterial()
				|| e.getBlock().getType() == XMaterial.PUMPKIN.parseMaterial()
				|| e.getBlock().getType() == XMaterial.CARROTS.parseMaterial()
				|| e.getBlock().getType() == XMaterial.POTATOES.parseMaterial())
				&& CustomFlags.hasFlag(e.getBlock().getLocation())) {
			if (p.getGameMode() == GameMode.CREATIVE) {
				p.sendMessage(Main.getMessage("Creative"));
				return;
			}
			if (Main.getPlugin().getConfig().getBoolean("PrefixEnabled") && !PlayerManager.getOnlinePlayer(e.getPlayer().getUniqueId()).getPrefix().equalsIgnoreCase("Boer")) {
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
					p.getInventory().addItem(XMaterial.PUMPKIN.parseItem());
				} else if (e.getBlock().getType() == Utils.getMelonMaterial()) {
					p.getInventory().addItem(new ItemStack(e.getBlock().getType(), 1));
				}
				e.setCancelled(true);
				Utils.blockReplaces.put(e.getBlock().getLocation(), e.getBlock().getType());
				e.getBlock().setType(Material.AIR);
				return;
			}
			Crops crops = (Crops) e.getBlock().getState().getData();
			if (e.getBlock().getType() == Utils.getCropsMaterial()
					|| e.getBlock().getType() == Utils.getBeetrootMaterial()
					|| e.getBlock().getType() == XMaterial.CARROTS.parseMaterial()
					|| e.getBlock().getType() == XMaterial.POTATOES.parseMaterial()) {
				if (crops.getState() != CropState.RIPE) {
					if (e.getBlock().getType() == Utils.getCropsMaterial()) {
						e.getPlayer().sendMessage(Main.getMessage("TarweNietVolgroeid"));
					} else if (e.getBlock().getType() == XMaterial.CARROTS.parseMaterial()) {
						e.getPlayer().sendMessage(Main.getMessage("WortelNietVolgroeid"));
					} else if (e.getBlock().getType() == XMaterial.POTATOES.parseMaterial()) {
						e.getPlayer().sendMessage(Main.getMessage("AardappelNietVolgroeid"));
					} else if (e.getBlock().getType() == Utils.getBeetrootMaterial()) {
						e.getPlayer().sendMessage(Main.getMessage("BietenNietVolgroeid"));
					}
					e.setCancelled(true);
					return;
				}
				if (e.getBlock().getType() == Utils.getCropsMaterial()) {
					e.getPlayer().getInventory().addItem(XMaterial.WHEAT.parseItem());
				} else if (e.getBlock().getType() == XMaterial.CARROTS.parseMaterial()) {
					e.getPlayer().getInventory().addItem(new ItemStack(Utils.getCarrotItem(), 1));
				} else if (e.getBlock().getType() == XMaterial.POTATOES.parseMaterial()) {
					e.getPlayer().getInventory().addItem(new ItemStack(Utils.getPotatoItem(), 1));
				} else if (e.getBlock().getType() == Utils.getBeetrootMaterial()) {
					e.getPlayer().getInventory().addItem(new ItemStack(Material.BEETROOT));
				}
				e.setCancelled(true);
				crops.setState(CropState.SEEDED);
				Utils.handleToolDurability(p);
				Utils.cropPlaces.add(new Utils.GrowingCrop(e.getBlock().getLocation()));
				e.getBlock().setType(e.getBlock().getType());
			}
		}
	}
}