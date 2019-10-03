package nl.mrwouter.minetopiafarms.events;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import nl.minetopiasdb.api.API;
import nl.minetopiasdb.api.SDBPlayer;
import nl.mrwouter.minetopiafarms.Main;
import nl.mrwouter.minetopiafarms.utils.XMaterial;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase(Main.getMessage("InventoryTitle"))) {
            e.setCancelled(true);
            String beroep = "none";
            if (e.getSlot() == 10) {
                beroep = "Boer";
            } else if (e.getSlot() == 12) {
                beroep = "Houthakker";
            } else if (e.getSlot() == 14) {
                beroep = "Mijnwerker";
            } else if (e.getSlot() == 16) {
                beroep = "Visser";
            }
            if (!beroep.equalsIgnoreCase("none")) {
                if (API.getEcon().getBalance(((Player) e.getWhoClicked())) < Main.getPlugin().getConfig()
                        .getInt("KostenVoorEenBaan")) {
                    e.getWhoClicked().sendMessage(Main.getMessage("TeWeinigGeld").replaceAll("<Bedrag>",
                            "" + Main.getPlugin().getConfig().getInt("KostenVoorEenBaan")));
                    return;
                }
                e.getWhoClicked().closeInventory();
                e.getWhoClicked().sendMessage(Main.getMessage("BaanVeranderd").replaceAll("<Baan>", beroep));

                API.getEcon().withdrawPlayer(((OfflinePlayer) e.getWhoClicked()),
                        Main.getPlugin().getConfig().getInt("KostenVoorEenBaan"));
                SDBPlayer pl = SDBPlayer.createSDBPlayer(((Player) e.getWhoClicked()));
                pl.setPrefix(beroep);
                API.updateScoreboard(((Player) e.getWhoClicked()));

                if (Main.getPlugin().getConfig().getBoolean("KrijgItemsBijBaanSelect")) {
                    for (String material : Main.getPlugin().getConfig().getStringList("ItemsBijBaanSelect."+beroep))
                        e.getWhoClicked().getInventory().addItem(XMaterial.valueOf(material).parseItem());
                }
                for (String cmd : Main.getPlugin().getConfig().getStringList("CommandsUitvoerenBijBaanWissel." + beroep)) {
                    if (!cmd.equalsIgnoreCase("Typ hier jouw commands")) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("<Player>", e.getWhoClicked().getName()));
                    }
                }
            }
        }
    }
}