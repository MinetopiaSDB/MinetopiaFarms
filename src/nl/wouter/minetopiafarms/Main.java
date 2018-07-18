package nl.wouter.minetopiafarms;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.material.Crops;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import nl.wouter.minetopiafarms.commands.KiesCMD;
import nl.wouter.minetopiafarms.commands.MTFarmsCMD;
import nl.wouter.minetopiafarms.events.BlockBreaker;
import nl.wouter.minetopiafarms.events.FarmListener;
import nl.wouter.minetopiafarms.events.InventoryClickListener;
import nl.wouter.minetopiafarms.events.TreeFarmer;
import nl.wouter.minetopiafarms.utils.CustomFlags;
import nl.wouter.minetopiafarms.utils.UpdateChecker;
import nl.wouter.minetopiafarms.utils.Utils;

public class Main extends JavaPlugin {
	public static Plugin pl;

	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new BlockBreaker(), this);
		Bukkit.getPluginManager().registerEvents(new FarmListener(), this);
		Bukkit.getPluginManager().registerEvents(new TreeFarmer(), this);
		Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);

		getCommand("kies").setExecutor(new KiesCMD());
		getCommand("minetopiafarms").setExecutor(new MTFarmsCMD());
		
		getConfig().addDefault("KostenVoorEenBaan", 2500);
		getConfig().addDefault("KrijgItemsBijBaanSelect", true);
		
		getConfig().addDefault("CommandsUitvoerenBijBaanWissel.Boer", Arrays.asList("Typ hier jouw commands"));
		getConfig().addDefault("CommandsUitvoerenBijBaanWissel.Houthakker", Arrays.asList("Typ hier jouw commands"));
		getConfig().addDefault("CommandsUitvoerenBijBaanWissel.Mijnwerker", Arrays.asList("Typ hier jouw commands"));
		getConfig().addDefault("Messages.VeranderenVanEenBaan", "&4Let op! &cHet veranderen van beroep kost &4€ <Bedrag> ,-&c.");
		getConfig().addDefault("Messages.InventoryTitle", "&3Kies een &bberoep&3!");
		getConfig().addDefault("Messages.ItemName", "&3<Beroep>");
		getConfig().addDefault("Messages.ItemLore", "&3Kies het beroep &b<Beroep>");
		
		getConfig().addDefault("Messages.BeroepNodig", "&4ERROR: &cHiervoor heb je het beroep &4<Beroep> &cnodig!");
		getConfig().addDefault("Messages.ToolNodig", "&4ERROR: &cHiervoor heb je een &4<Tool> &cnodig!");
		getConfig().addDefault("Messages.TarweNietVolgroeid", "&4ERROR: &cDeze tarwe is niet volgroeid!");
	
		getConfig().addDefault("Messages.TeWeinigGeld", "&4ERROR: &cOm van baan te veranderen heb je &4€ <Bedrag>,- &cnodig!");
		
		getConfig().addDefault("Messages.BaanVeranderd", "&3Jouw baan is succesvol veranderd naar &b<Baan>&3.");
		
		getConfig().addDefault("Messages.GeenRegion", "&4ERROR: &cDeze region moet de tag &4'<Tag>' &chebben.");
		
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		pl = this;
		
		CustomFlags.loadCustomFlag();
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@SuppressWarnings("deprecation")
			public void run() {

				for (Location l : new ArrayList<Location>(Utils.wheatPlaces)) {
					if (l.getBlock().getType() == Material.CROPS) {
						Crops crop = (Crops) l.getBlock().getState().getData();
						if (crop.getState() == CropState.SEEDED) {
							l.getBlock().setData((byte) 1);
						} else if (crop.getState() == CropState.GERMINATED) {
							l.getBlock().setData((byte) 2);
						} else if (crop.getState() == CropState.VERY_SMALL) {
							l.getBlock().setData((byte) 3);
						} else if (crop.getState() == CropState.SMALL) {
							l.getBlock().setData((byte) 4);
						} else if (crop.getState() == CropState.MEDIUM) {
							l.getBlock().setData((byte) 5);
						} else if (crop.getState() == CropState.TALL) {
							l.getBlock().setData((byte) 6);
						} else if (crop.getState() == CropState.VERY_TALL) {
							l.getBlock().setData((byte) 7);
							Utils.wheatPlaces.remove(l);
						}
					} else {
						Utils.wheatPlaces.remove(l);
					}
				}
			}
		}, 6 * 20l, 6 * 20l);
		
		Bukkit.getPluginManager().registerEvents(new Listener() {
			@EventHandler
			public void onJoin(PlayerJoinEvent e) {
				UpdateChecker.sendUpdateMessageLater(e.getPlayer());
			}
		}, this);
	}

	public void onDisable() {
		for (Location l : Utils.ironOres) {
			l.getBlock().setType(Material.IRON_ORE);
		}
		for (Location l : Utils.ironOres) {
			l.getBlock().setType(Material.WHEAT);
		}
	}
	
	public static String getMessage(String path) {
		return Utils.color(pl.getConfig().getString("Messages." + path));
	}
}
