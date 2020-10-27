package nl.mrwouter.minetopiafarms.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.cryptomorin.xseries.XMaterial;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import nl.minetopiasdb.api.API;
import nl.minetopiasdb.api.playerdata.PlayerManager;
import nl.minetopiasdb.api.playerdata.objects.OnlineSDBPlayer;
import nl.mrwouter.minetopiafarms.Main;

public class InventoryClickListener implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getView().getTitle().equalsIgnoreCase(Main.getMessage("InventoryTitle"))) {
			e.setCancelled(true);
			if (e.getCurrentItem() == null) {
				return;
			}

			if (NBTEditor.contains(e.getCurrentItem(), "mtfarms_beroep")) {
				Player player = (Player) e.getWhoClicked();
				String beroep = NBTEditor.getString(e.getCurrentItem(), "mtfarms_beroep");

				if (API.getEcon().getBalance(player) < Main.getPlugin().getConfig().getInt("KostenVoorEenBaan")) {
					player.sendMessage(Main.getMessage("TeWeinigGeld").replaceAll("<Bedrag>",
							"" + Main.getPlugin().getConfig().getInt("KostenVoorEenBaan")));
					return;
				}
				player.closeInventory();
				player.sendMessage(Main.getMessage("BaanVeranderd").replaceAll("<Baan>", beroep));

				API.getEcon().withdrawPlayer(player, Main.getPlugin().getConfig().getInt("KostenVoorEenBaan"));

				OnlineSDBPlayer sdbPlayer = PlayerManager.getOnlinePlayer(player.getUniqueId());

				if (Main.getPlugin().getConfig().getBoolean("PrefixEnabled")) {
					sdbPlayer.setPrefix(beroep);
				}

				API.updateScoreboard(player);

				if (Main.getPlugin().getConfig().getBoolean("KrijgItemsBijBaanSelect")) {
					for (String material : Main.getPlugin().getConfig().getStringList("ItemsBijBaanSelect." + beroep))
						e.getWhoClicked().getInventory().addItem(XMaterial.valueOf(material).parseItem());
				}
				for (String cmd : Main.getPlugin().getConfig()
						.getStringList("CommandsUitvoerenBijBaanWissel." + beroep)) {
					if (!cmd.equalsIgnoreCase("Typ hier jouw commands")) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
								cmd.replaceAll("<Player>", e.getWhoClicked().getName()));
					}
				}
			}
		}
	}
}