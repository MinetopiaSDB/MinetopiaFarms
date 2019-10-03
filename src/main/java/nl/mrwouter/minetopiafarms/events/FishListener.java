package nl.mrwouter.minetopiafarms.events;

import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import nl.minetopiasdb.api.SDBPlayer;
import nl.mrwouter.minetopiafarms.Main;
import nl.mrwouter.minetopiafarms.utils.CustomFlags;
import nl.mrwouter.minetopiafarms.utils.ItemBuilder;

public class FishListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onFishStart(PlayerFishEvent e) {
		Player p = (Player) e.getPlayer();
		if (CustomFlags.hasFlag(p, e.getHook().getLocation())) {
			//Creative bypass isn't really relevant here.
			/*if (p.getGameMode() == GameMode.CREATIVE) {
				p.sendMessage(Main.getMessage("Creative"));
				return;
			}*/
			if (!SDBPlayer.createSDBPlayer(e.getPlayer()).getPrefix().equalsIgnoreCase("Visser")) {
				e.getPlayer().sendMessage(Main.getMessage("BeroepNodig").replaceAll("<Beroep>", "visser"));
				e.setCancelled(true);
				return;
			}
			if (!p.getInventory().getItemInMainHand().getType().equals(Material.FISHING_ROD)) {
				e.getPlayer().sendMessage(Main.getMessage("ToolNodig").replaceAll("<Tool>", "fishing_rod"));
				e.setCancelled(true);
				return;
			}

			if (!CustomFlags.isAllowed(p, e.getPlayer().getLocation(), "fishing")) {
				p.sendMessage(Main.getMessage("GeenRegion").replaceAll("<Tag>", "fishing"));
				return;
			}

			if (e.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
				List<String> list = Main.getPlugin().getConfig().getStringList("MogelijkeItemsBijVangst");
				int index = new Random().nextInt(list.size());
				String str = list.get(index);
				if (Material.getMaterial(str) != null) {
					ItemStack stack = new ItemBuilder(Material.getMaterial(str), 1)
							.setName(Main.getPlugin().getConfig().getString("VangstItemNaam"))
							.setLore(Main.getPlugin().getConfig().getStringList("VangstItemLore")).toItemStack();
					p.getInventory().addItem(stack);
				} else {
					Main.getPlugin().getLogger().severe("Het item " + str + " kan niet worden gegeven, omdat het niet bestaat!");
					return;
				}
			}
		}
	}

}
