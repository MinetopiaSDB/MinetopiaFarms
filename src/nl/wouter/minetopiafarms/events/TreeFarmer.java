package nl.wouter.minetopiafarms.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import nl.wouter.minetopiafarms.Main;
import nl.wouter.minetopiafarms.utils.CustomFlags;
import nl.wouter.minetopiafarms.utils.Utils;
import wouter.is.cool.SDBPlayer;

public class TreeFarmer implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();

		if (e.getBlock().getType().toString().contains("_LOG")) {
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

			if (CustomFlags.isAllowed(p, "houthakker")) {
				p.sendMessage(Main.getMessage("GeenRegion").replaceAll("<Tag>", "houthakker"));
				return;
			}

			Material blockType = e.getBlock().getType();
			byte blockData = e.getBlock().getData();

			e.setCancelled(true);
			for (ItemStack drop : e.getBlock().getDrops()) {
				e.getPlayer().getInventory().addItem(drop);
			}

			e.getBlock().setType(Material.AIR);
			Utils.handleToolDurability(e.getPlayer());
			
			Bukkit.getScheduler().runTaskLater(Main.pl, new Runnable() {
				@Override
				public void run() {
					e.getBlock().setType(blockType);
					if (Utils.is113orUp()) {
						try {
							e.getBlock().getClass().getMethod("setData", byte.class).invoke(e.getBlock(), blockData);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}, 30 * 20);
		}
	}
}
