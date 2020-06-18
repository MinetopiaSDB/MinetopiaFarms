package nl.mrwouter.minetopiafarms.events;

import nl.minetopiasdb.api.playerdata.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import nl.mrwouter.minetopiafarms.Main;
import nl.mrwouter.minetopiafarms.utils.CustomFlags;
import nl.mrwouter.minetopiafarms.utils.Utils;
import nl.mrwouter.minetopiafarms.utils.XMaterial;

public class BlockBreaker implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (CustomFlags.hasFlag(p, e.getBlock().getLocation())
                && (!p.hasPermission("minetopiafarms.bypassregions") && p.getGameMode() != GameMode.CREATIVE)) {
            e.setCancelled(true);
        }

        if (e.getBlock().getType().toString().contains("_ORE") && CustomFlags.hasFlag(p, e.getBlock().getLocation())) {
        	if (p.getGameMode() == GameMode.CREATIVE) {
                p.sendMessage(Main.getMessage("Creative"));
                return;
            }

            if (!PlayerManager.getOnlinePlayer(e.getPlayer().getUniqueId()).getPrefix().equalsIgnoreCase("Mijnwerker")) {
                e.getPlayer().sendMessage(Main.getMessage("BeroepNodig").replaceAll("<Beroep>", "mijnwerker"));
                e.setCancelled(true);
                return;
            }
            if (!e.getPlayer().getInventory().getItemInMainHand().getType().toString().contains("PICKAXE")) {
                e.getPlayer().sendMessage(Main.getMessage("ToolNodig").replaceAll("<Tool>", "houweel"));
                e.setCancelled(true);
                return;
            }
            if (!CustomFlags.isAllowed(p, e.getBlock().getLocation(), "mijn")) {
                p.sendMessage(Main.getMessage("GeenRegion").replaceAll("<Tag>", "mijn"));
                e.setCancelled(true);
                return;
            }
            

            Material blockType = e.getBlock().getType().toString().contains("REDSTONE_ORE") ? Material.REDSTONE_ORE : e.getBlock().getType();
            e.setCancelled(true);

            switch (blockType) {
                case COAL_ORE:
                    e.getPlayer().getInventory().addItem(XMaterial.COAL.parseItem());
                    break;
                case DIAMOND_ORE:
                    e.getPlayer().getInventory().addItem(XMaterial.DIAMOND.parseItem());
                    break;
                case EMERALD_ORE:
                    e.getPlayer().getInventory().addItem(XMaterial.EMERALD.parseItem());
                    break;
                case GOLD_ORE:
                    e.getPlayer().getInventory().addItem(XMaterial.GOLD_INGOT.parseItem());
                    break;
                case IRON_ORE:
                    e.getPlayer().getInventory().addItem(XMaterial.IRON_INGOT.parseItem());
                    break;
                case LAPIS_ORE:
                    e.getPlayer().getInventory().addItem(XMaterial.LAPIS_LAZULI.parseItem());
                    break;
                case REDSTONE_ORE:
                    e.getPlayer().getInventory().addItem(XMaterial.REDSTONE.parseItem());
                    break;
                default:
                    //Not important & should be unreachable
                    return;
            }

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