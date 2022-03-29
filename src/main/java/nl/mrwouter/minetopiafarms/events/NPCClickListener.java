package nl.mrwouter.minetopiafarms.events;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import nl.minetopiasdb.api.API;
import nl.mrwouter.minetopiafarms.Main;
import nl.mrwouter.minetopiafarms.utils.Utils;

public class NPCClickListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onNPCRightClick(NPCRightClickEvent e) {
		Player clicker = e.getClicker();
		NPC clicked = e.getNPC();
		if (Main.getMessage("NPC.Name").equals(clicked.getName())) {
			double paymentAmount = 0;
			for (ItemStack item : clicker.getInventory().getContents()) {
				if (item != null && item.getType() != null) {
					paymentAmount += calculateItemPrice(item, clicker);
				}
			}

			if(paymentAmount != 0){
				API.getEcon().depositPlayer(clicker, paymentAmount);
				clicker.sendMessage(
						Main.getMessage("GeldBetaald").replaceAll("<Bedrag>", Utils.formatMoney(paymentAmount)));
				API.updateScoreboard(clicker);
			}
		}
	}

	private double calculateItemPrice(ItemStack item, Player clicker){;
		String itemName = XMaterial.matchXMaterial(item).name();

		Double configPrice = getJobItemPrice("Boer", itemName);

		if (configPrice.doubleValue() == 0) {
			configPrice = getJobItemPrice("Mijnwerker", itemName);

			if (configPrice.doubleValue() == 0 &&
					item.getType().toString().contains("LOG") &&
					clicker.getInventory().removeItem(item).size() == 0)
				return (item.getAmount() * Main.getPlugin().getConfig().getDouble("TerugverkoopPrijs.Houthakker"));
		}

		if (configPrice.doubleValue() == 0 || XMaterial.matchXMaterial(item).parseItem() == null)
			return 0;

		if (clicker.getInventory().removeItem(item).size() == 0)
			return configPrice.doubleValue() * item.getAmount();

		return 0;
	}

	private double getJobItemPrice(String job, String itemName){
		return Main.getPlugin().getConfig().getDouble("TerugverkoopPrijs." + job + "." + itemName);
	}
}