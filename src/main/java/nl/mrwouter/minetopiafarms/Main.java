package nl.mrwouter.minetopiafarms;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.material.Crops;
import org.bukkit.plugin.java.JavaPlugin;

import nl.mrwouter.minetopiafarms.commands.KiesCMD;
import nl.mrwouter.minetopiafarms.commands.MTFarmsCMD;
import nl.mrwouter.minetopiafarms.events.BlockBreaker;
import nl.mrwouter.minetopiafarms.events.FarmListener;
import nl.mrwouter.minetopiafarms.events.InventoryClickListener;
import nl.mrwouter.minetopiafarms.events.NPCClickListener;
import nl.mrwouter.minetopiafarms.events.TreeFarmer;
import nl.mrwouter.minetopiafarms.utils.CustomFlags;
import nl.mrwouter.minetopiafarms.utils.Updat3r;
import nl.mrwouter.minetopiafarms.utils.Utils;
import nl.mrwouter.minetopiafarms.utils.Utils.GrowingCrop;
import nl.mrwouter.minetopiafarms.utils.Utils.TreeObj;

@SuppressWarnings("deprecation")
public class Main extends JavaPlugin {

	private static Main pl;

    public void onLoad() {
        CustomFlags.loadCustomFlag();
    }

	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new BlockBreaker(), this);
		Bukkit.getPluginManager().registerEvents(new FarmListener(), this);
		Bukkit.getPluginManager().registerEvents(new TreeFarmer(), this);
		Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);

		if (Bukkit.getPluginManager().getPlugin("Citizens") != null) {
			Bukkit.getPluginManager().registerEvents(new NPCClickListener(), this);
		}

		getCommand("kies").setExecutor(new KiesCMD());
		getCommand("minetopiafarms").setExecutor(new MTFarmsCMD());

		Utils.buildConfig(getConfig());
		getConfig().set("ItemsBijBaanSelect.Visser", null);
		getConfig().set("MogelijkeItemsBijVangst", null);
		getConfig().set("VangstItemNaam", null);
		getConfig().set("VangstItemLore", null);
		getConfig().set("Banen.Visser.Enabled", null);
		getConfig().set("Banen.Visser.Item", null);
		getConfig().set("TerugverkoopPrijs.Visser", null);
		getConfig().set("CommandsUitvoerenBijBaanWissel.Visser", null);

		getConfig().options().copyDefaults(true);
		saveConfig();

		pl = this;

		final int cropgrowschedulertime = getConfig().getInt("scheduler.cropgrow");
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
			int growtime = getConfig().getInt("scheduler.perstatetime");
			for (GrowingCrop gcrop : new ArrayList<>(Utils.cropPlaces)) {
				BlockState state = gcrop.location.getBlock().getState();
				if (state.getData() instanceof Crops) {
					gcrop.time += cropgrowschedulertime;
					Crops crop = (Crops) state.getData();
					if ((crop.getState().ordinal() + 1) * growtime <= gcrop.time) {
						if (crop.getState() == CropState.RIPE) {
							Utils.cropPlaces.remove(gcrop);
							continue;
						}

						crop.setState(CropState.values()[crop.getState().ordinal() + 1]);
						state.update();
					}
				} else {
					Utils.cropPlaces.remove(gcrop);
				}
			}
		}, cropgrowschedulertime, cropgrowschedulertime);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
			for (Location l : Utils.blockReplaces.keySet()) {
				l.getBlock().setType(Utils.blockReplaces.get(l));
			}
		}, 40 * 20L, 40 * 20L);

		Updat3r.getInstance().startTask();
		Bukkit.getPluginManager().registerEvents(new Listener() {
			@EventHandler
			public void onJoin(PlayerJoinEvent e) {
				Updat3r.getInstance().sendUpdateMessageLater(e.getPlayer());
			}
		}, this);
	}

	public void onDisable() {
		for (Location l : Utils.ores.keySet()) {
			l.getBlock().setType(Utils.ores.get(l));
		}
		for (GrowingCrop gcrop : Utils.cropPlaces) {
			BlockState state = gcrop.location.getBlock().getState();
			if (state.getData() instanceof Crops) {
				Crops crop = (Crops) state.getData();
				crop.setState(CropState.RIPE);
				state.update();
			}
		}
		for (Location l : Utils.blockReplaces.keySet()) {
			l.getBlock().setType(Utils.blockReplaces.get(l));
		}
		for (Location l : Utils.treePlaces.keySet()) {
			TreeObj obj = Utils.treePlaces.get(l);
			l.getBlock().setType(obj.getMaterial());
			if (!Utils.is113orUp()) {
				try {
					l.getBlock().getClass().getMethod("setData", byte.class).invoke(l.getBlock(), obj.getData());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	public static Main getPlugin() {
		return pl;
	}

	public static String getMessage(String path) {
		return Utils.color(pl.getConfig().getString("Messages." + path));
	}
}
