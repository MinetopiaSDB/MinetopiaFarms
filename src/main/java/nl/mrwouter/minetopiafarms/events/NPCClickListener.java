package nl.mrwouter.minetopiafarms.events;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

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
					XMaterial xMaterialItem = XMaterial.valueOf(item.getType().name());
					String itemName = xMaterialItem.name();

					Double configPrice = Main.getPlugin().getConfig().getDouble("TerugverkoopPrijs.Boer." + itemName);
					if(configPrice == 0 || xMaterialItem.parseItem() == null) return;

					if(clicker.getInventory().removeItem(item).size() == 0){
						paymentAmount += (configPrice * item.getAmount());
					}
				}
			}

			for (ItemStack item : clicker.getInventory().getContents()) {
				if (item != null && item.getType() != null) {
					XMaterial xMaterialItem = XMaterial.valueOf(item.getType().name());
					String itemName = xMaterialItem.name();

					Double configPrice = Main.getPlugin().getConfig().getDouble("TerugverkoopPrijs.Mijnwerker." + itemName);
					if(configPrice == 0 || xMaterialItem.parseItem() == null) return;

					if(clicker.getInventory().removeItem(item).size() == 0){
						paymentAmount += (configPrice * item.getAmount());
					}
				}
			}

			for (ItemStack item : clicker.getInventory().getContents()) {
				if (item != null && item.getType() != null) {
					XMaterial xMaterialItem = XMaterial.valueOf(item.getType().name());
					String itemName = xMaterialItem.name();

					Double configPrice = Main.getPlugin().getConfig().getDouble("TerugverkoopPrijs.Houthakker." + itemName);
					if(configPrice == 0 || xMaterialItem.parseItem() == null) return;

					if(clicker.getInventory().removeItem(item).size() == 0){
						paymentAmount += (configPrice * item.getAmount());
					}
				}
			}

			API.getEcon().depositPlayer(clicker, paymentAmount);
			clicker.sendMessage(
					Main.getMessage("GeldBetaald").replaceAll("<Bedrag>", Utils.formatMoney(paymentAmount)));
			API.updateScoreboard(clicker);

		}
	}
}