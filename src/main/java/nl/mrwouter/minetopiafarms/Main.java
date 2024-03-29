package nl.mrwouter.minetopiafarms;

import java.util.ArrayList;

import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import nl.mrwouter.worldguard_6.WorldGuard6;
import nl.mrwouter.worldguard_7.WorldGuard7;
import nl.mrwouter.worldguard_core.WorldGuard;
import org.bukkit.Bukkit;
import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
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
import sun.security.pkcs11.wrapper.CK_SSL3_KEY_MAT_OUT;

@SuppressWarnings("deprecation")
public class Main extends JavaPlugin {

	private static Main plugin;
	private WorldGuard worldGuardPlugin;

	public WorldGuard getWorldGuard() {
		return worldGuardPlugin;
	}

	public void onLoad() {
		if (Bukkit.getPluginManager().getPlugin("WorldGuard").getDescription().getVersion().startsWith("6.")) {
			worldGuardPlugin = new WorldGuard6();
			getLogger().info("Using WorldGuard v6 (" + worldGuardPlugin.getClass().getName() + ")");
		} else {
			worldGuardPlugin = new WorldGuard7();
			getLogger().info("Using WorldGuard v7 (" + worldGuardPlugin.getClass().getName() + ")");
		}

		FlagRegistry registry = getWorldGuard().getFlagRegistry();
		CustomFlags.loadCustomFlag(registry);
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
		getConfig().set("scheduler.perstatetime", null);
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

		plugin = this;

		final int cropScheduleTime = getConfig().getInt("scheduler.cropgrow");
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
			for (GrowingCrop gcrop : new ArrayList<>(Utils.cropPlaces)) {
				Block block = gcrop.location.getBlock();
				BlockState state = block.getState();
				if (!(state.getData() instanceof Crops)) {
					Utils.cropPlaces.remove(gcrop);

				}

				Crops crop = (Crops) state.getData();
				if (crop.getState() == CropState.RIPE) {
					Utils.cropPlaces.remove(gcrop);
					continue;
				}

				crop.setState(CropState.values()[crop.getState().ordinal() + 1]);
				state.setData(crop);
				state.update(true);
			}
		}, cropScheduleTime, cropScheduleTime);

		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
			for (Location l : Utils.blockReplaces.keySet()) {
				l.getBlock().setType(Utils.blockReplaces.get(l));
			}
		}, cropScheduleTime, cropScheduleTime);

		Bukkit.getPluginManager().registerEvents(new Listener() {
			@EventHandler
			public void onJoin(PlayerJoinEvent event) {
				Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
					Player player = event.getPlayer();
					if (!player.isOnline()) {
						return;
					}

					if (player.hasPermission("minetopiasdb.sdb")) {
						Updat3r.getInstance().getLatestCached()
								.thenAccept(update -> {
									if (update != null && update.isNewer()) {
										player.sendMessage("   §3-=-=-=[§bMinetopiaFarms§3]=-=-=-   ");
										player.sendMessage("§3Er is een update beschikbaar voor §bMinetopiaFarms§3!");
										player.sendMessage("§3Je maakt nu gebruik van versie §b" + Main.getPlugin().getDescription().getVersion() + "§3.");
										player.sendMessage("§3De nieuwste versie is §b" + update.getVersion());
										player.sendMessage("§3Om deze update te installeren, type §b/mtfarms update.");
										player.sendMessage("   §3-=-=-=[§bMinetopiaFarms§3]=-=-=-   ");
									}
								});
					}
				}, 20 * 3L);
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
				state.setData(crop);
				state.update(true);
			}
		}
		for (Location l : Utils.blockReplaces.keySet()) {
			l.getBlock().setType(Utils.blockReplaces.get(l));
		}
		for (Location l : Utils.treePlaces.keySet()) {
			TreeObj obj = Utils.treePlaces.get(l);
			l.getBlock().setType(obj.getMaterial());
		}
	}

	public static Main getPlugin() {
		return plugin;
	}

	public static String getMessage(String path) {
		return Utils.color(getPlugin().getConfig().getString("Messages." + path));
	}
}
