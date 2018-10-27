package nl.wouter.minetopiafarms.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WorldGuardLegacyManager {

	private static WorldGuardLegacyManager legacyInstance = new WorldGuardLegacyManager();

	public static WorldGuardLegacyManager getInstance() {
		return legacyInstance;
	}

	private static String wgVerStr = null;

	public static WorldGuardPlugin getWorldGuard() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		if ((plugin == null) || (!(plugin instanceof WorldGuardPlugin))) {
			return null;
		}
		return (WorldGuardPlugin) plugin;
	}

	public FlagRegistry getFlagRegistry() {
		if (getWgVer().contains("7.")) {
			try {
				Class<?> wgClass = Reflection.getClass("com.sk89q.worldguard.WorldGuard");

				Object instance = wgClass.getDeclaredMethod("getInstance").invoke(null);
				Class<?> wgInstanceClass = instance.getClass();
				Method declaredMethod = wgInstanceClass.getDeclaredMethod("getFlagRegistry");
				return (FlagRegistry) declaredMethod.invoke(instance);
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		} else {
			return getWorldGuard().getFlagRegistry();
		}
	}

	public RegionManager getRegionManager(World w) {
		if (getWgVer().contains("7.")) {
			// return
			// com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer().get(new
			// BukkitWorld(w));
			try {
				Class<?> wgClass = Reflection.getClass("com.sk89q.worldguard.WorldGuard");

				Object instance = wgClass.getDeclaredMethod("getInstance").invoke(null);
				Class<?> wgInstanceClass = instance.getClass();

				Object platform = wgInstanceClass.getDeclaredMethod("getPlatform").invoke(instance);
				Class<?> wgPlatformClass = platform.getClass();

				Object regionContainer = wgPlatformClass.getDeclaredMethod("getRegionContainer").invoke(platform);
				Class<?> wgRegionContainer = regionContainer.getClass();

				return (RegionManager) wgRegionContainer.getSuperclass()
						.getMethod("get", com.sk89q.worldedit.world.World.class)
						.invoke(regionContainer, new BukkitWorld(w));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			return getWorldGuard().getRegionManager(w);
		}
		return null;
	}

	public List<ProtectedRegion> getRegions(Location loc) {
		return new ArrayList<ProtectedRegion>(getRegionManager(loc.getWorld())
				.getApplicableRegions(new Vector(loc.getX(), loc.getY(), loc.getZ())).getRegions());
	}

	public ApplicableRegionSet getApplicableRegionSet(Location loc) {
		return getRegionManager(loc.getWorld()).getApplicableRegions(new Vector(loc.getX(), loc.getY(), loc.getZ()));
	}

	public String getWgVer() {
		if (wgVerStr == null) {
			wgVerStr = Bukkit.getPluginManager().getPlugin("WorldGuard").getDescription().getVersion();
		}
		return wgVerStr;
	}

}
