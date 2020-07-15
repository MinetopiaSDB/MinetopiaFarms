package nl.mrwouter.minetopiafarms.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.cryptomorin.xseries.XMaterial;

import nl.minetopiasdb.api.API;
import nl.minetopiasdb.api.playerdata.PlayerManager;
import nl.minetopiasdb.api.playerdata.objects.OnlineSDBPlayer;
import nl.mrwouter.minetopiafarms.Main;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase(Main.getMessage("InventoryTitle"))) {
            e.setCancelled(true);
            String beroep = "none";
            if (e.getCurrentItem().getType().equals(Material.DIAMOND_HOE)) {
                beroep = "Boer";
            } else if (e.getCurrentItem().getType().equals(Material.DIAMOND_AXE)) {
                beroep = "Houthakker";
            } else if (e.getCurrentItem().getType().equals(Material.DIAMOND_PICKAXE)) {
                beroep = "Mijnwerker";
            } else if (e.getCurrentItem().getType().equals(Material.FISHING_ROD)) {
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

                OnlineSDBPlayer sdbPlayer = PlayerManager.getOnlinePlayer(e.getWhoClicked().getUniqueId());
                sdbPlayer.setPrefix(beroep);

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