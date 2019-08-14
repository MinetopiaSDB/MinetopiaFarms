package nl.wouter.minetopiafarms.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
import nl.wouter.minetopiafarms.utils.XMaterial;

public class BlockBreaker implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (CustomFlags.hasFlag(p, e.getBlock().getLocation())
                && (!p.hasPermission("minetopiafarms.bypassregions") && p.getGameMode() != GameMode.CREATIVE)) {
            e.setCancelled(true);
        }

        if (e.getBlock().getType().toString().contains("_ORE")) {
            if (p.getGameMode() == GameMode.CREATIVE) {
                p.sendMessage(Main.getMessage("Creative"));
                return;
            }
            if (!SDBPlayer.createSDBPlayer(e.getPlayer()).getPrefix().equalsIgnoreCase("Mijnwerker")) {
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

            final Material blockType = e.getBlock().getType() == Material.GLOWING_REDSTONE_ORE ? Material.REDSTONE_ORE : e.getBlock().getType();
            e.setCancelled(true);

            switch (blockType) {
                case COAL_ORE:
                    e.getPlayer().getInventory().addItem(new ItemStack(Material.COAL));
                    break;
                case DIAMOND_ORE:
                    e.getPlayer().getInventory().addItem(new ItemStack(Material.DIAMOND));
                    break;
                case EMERALD_ORE:
                    e.getPlayer().getInventory().addItem(new ItemStack(Material.EMERALD));
                    break;
                case GOLD_ORE:
                    e.getPlayer().getInventory().addItem(new ItemStack(Material.GOLD_INGOT));
                    break;
                case IRON_ORE:
                    e.getPlayer().getInventory().addItem(new ItemStack(Material.IRON_INGOT));
                    break;
                case LAPIS_ORE:
                    e.getPlayer().getInventory().addItem(new ItemStack(XMaterial.LAPIS_LAZULI.parseMaterial()));
                    break;
                case REDSTONE_ORE:
                    e.getPlayer().getInventory().addItem(new ItemStack(Material.REDSTONE));
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