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

import nl.wouter.minetopiafarms.Main;
import nl.wouter.minetopiafarms.utils.CustomFlags;
import nl.wouter.minetopiafarms.utils.Utils;
import wouter.is.cool.SDBPlayer;

public class FarmListener implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		// GMC -> no effects
		if (p.getGameMode() == GameMode.CREATIVE) {
			return;
		}
		

		if (e.getBlock().getType() == Material.CROPS) {
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
			
			if (!CustomFlags.isAllowed(p, "farm")) {
				p.sendMessage(Main.getMessage("GeenRegion").replaceAll("<Tag>", "farm"));
				return;
			}
			
			Crops crops = (Crops) e.getBlock().getState().getData();
			if (crops.getState() != CropState.RIPE) {
				e.getPlayer().sendMessage(Main.getMessage("TarweNietVolgroeid"));
				e.setCancelled(true);
				return;
			}

			e.setCancelled(true);
			p.getInventory().addItem(new ItemStack(Material.WHEAT, 1));
			Utils.wheatPlaces.add(e.getBlock().getLocation());
			e.getBlock().setType(Material.CROPS);

			e.getBlock().setData((byte) 0);
		}
	}
}
