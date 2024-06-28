package nl.mrwouter.minetopiafarms.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import nl.minetopiasdb.api.playerdata.PlayerManager;
import nl.mrwouter.minetopiafarms.Main;
import nl.mrwouter.minetopiafarms.utils.CustomFlags;
import nl.mrwouter.minetopiafarms.utils.Utils;

public class BlockBreaker implements Listener {

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (CustomFlags.hasFlag(e.getBlock().getLocation())
				&& (!p.hasPermission("minetopiafarms.bypassregions") && p.getGameMode() != GameMode.CREATIVE)) {
			e.setCancelled(true);
		}

		if (e.getBlock().getType().name().contains("_ORE") && CustomFlags.hasFlag(e.getBlock().getLocation())) {
			if (p.getGameMode() == GameMode.CREATIVE) {
				p.sendMessage(Main.getMessage("Creative"));
				return;
			}

			if (Main.getPlugin().getConfig().getBoolean("PrefixEnabled") && !PlayerManager.getOnlinePlayer(e.getPlayer().getUniqueId()).getPrefix().equalsIgnoreCase("Mijnwerker")) {
				e.getPlayer().sendMessage(Main.getMessage("BeroepNodig").replaceAll("<Beroep>", "mijnwerker"));
				e.setCancelled(true);
				return;
			}
			if (!e.getPlayer().getInventory().getItemInMainHand().getType().name().contains("_PICKAXE")) {
				e.getPlayer().sendMessage(Main.getMessage("ToolNodig").replaceAll("<Tool>", "houweel"));
				e.setCancelled(true);
				return;
			}
			if (!CustomFlags.isAllowed(e.getBlock().getLocation(), "mijn")) {
				p.sendMessage(Main.getMessage("GeenRegion").replaceAll("<Tag>", "mijn"));
				e.setCancelled(true);
				return;
			}

			Material blockType = e.getBlock().getType().name().contains("REDSTONE_ORE")
					? Material.REDSTONE_ORE
					: e.getBlock().getType();
			e.setCancelled(true);

			if (Main.getPlugin().getConfig().get("scheduler.miner." + blockType.name()) == null) {
				p.sendMessage(Main.getMessage("MateriaalOnbekend"));
				e.setCancelled(true);
				return;
			}

			e.getPlayer().getInventory().addItem(new ItemStack(blockType, 1));

			e.getBlock().getDrops().clear();
			Utils.ores.put(e.getBlock().getLocation(), e.getBlock().getType());
			e.getBlock().getLocation().getBlock().setType(Material.COBBLESTONE);
			Utils.handleToolDurability(e.getPlayer());
			Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
				e.getBlock().setType(blockType);
				Utils.ores.remove(e.getBlock().getLocation());
			}, Main.getPlugin().getConfig().getInt("scheduler.miner." + blockType.name()));
		}
	}
}