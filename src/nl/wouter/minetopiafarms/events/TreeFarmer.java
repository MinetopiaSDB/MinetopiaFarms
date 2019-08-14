package nl.wouter.minetopiafarms.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import nl.minetopiasdb.api.SDBPlayer;
import nl.wouter.minetopiafarms.Main;
import nl.wouter.minetopiafarms.utils.CustomFlags;
import nl.wouter.minetopiafarms.utils.Utils;
import nl.wouter.minetopiafarms.utils.Utils.TreeObj;

public class TreeFarmer implements Listener {

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();

		if (e.getBlock().getType().toString().contains("LOG")) {
			if (!SDBPlayer.createSDBPlayer(e.getPlayer()).getPrefix().equalsIgnoreCase("Houthakker")) {
				e.getPlayer().sendMessage(Main.getMessage("BeroepNodig").replaceAll("<Beroep>", "houthakker"));
				e.setCancelled(true);
				return;
			}
			if (!e.getPlayer().getInventory().getItemInMainHand().getType().toString().contains("_AXE")) {
				e.getPlayer().sendMessage(Main.getMessage("ToolNodig").replaceAll("<Tool>", "bijl"));
				e.setCancelled(true);
				return;
			}

			if (!CustomFlags.isAllowed(p, e.getBlock().getLocation(), "houthakker")) {
				p.sendMessage(Main.getMessage("GeenRegion").replaceAll("<Tag>", "houthakker"));
				e.setCancelled(true);
				return;
			}

			Material blockType = e.getBlock().getType();
			@SuppressWarnings("deprecation")
			byte blockData = e.getBlock().getData();

			e.setCancelled(true);
			for (ItemStack drop : e.getBlock().getDrops()) {
				e.getPlayer().getInventory().addItem(drop);
			}

			e.getBlock().setType(Material.AIR);
			Utils.handleToolDurability(e.getPlayer());
			Utils.treePlaces.put(e.getBlock().getLocation(), new TreeObj(blockType, blockData));
			
			
			Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
				e.getBlock().setType(blockType);
				Utils.treePlaces.remove(e.getBlock().getLocation());
			}, 30 * 20);
		}
	}
}