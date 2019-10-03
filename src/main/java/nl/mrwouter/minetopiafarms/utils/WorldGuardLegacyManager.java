package nl.mrwouter.minetopiafarms.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WorldGuardLegacyManager {

	private static String wgVerStr = null;
	private static WorldGuardLegacyManager instance = null;

	public static WorldGuardLegacyManager getInstance() {
		if (instance == null) {
			instance = new WorldGuardLegacyManager();
		}
		return instance;
	}

	public WorldGuardPlugin getWorldGuard() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		if ((plugin == null) || (!(plugin instanceof WorldGuardPlugin))) {
			return null;
		}
		return (WorldGuardPlugin) plugin;
	}

	public WorldEditPlugin getWorldEdit() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		if ((plugin == null) || (!(plugin instanceof WorldEditPlugin))) {
			return null;
		}
		return (WorldEditPlugin) plugin;
	}

	public ProtectedRegion getLowerCasePlot(World w, String regionname) {
		for (ProtectedRegion pr : getRegionManager(w).getRegions().values()) {
			if (pr.getId().toLowerCase().equalsIgnoreCase(regionname)) {
				return pr;
			}
		}
		return null;
	}

	public RegionManager getRegionManager(World w) {
		if (getWgVer().contains("7.")) {
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

	@SuppressWarnings("unchecked")
	public List<ProtectedRegion> getRegions(Location loc) {
		ArrayList<ProtectedRegion> regions = new ArrayList<ProtectedRegion>();
		if (getWgVer().contains("7.")) {
			try {
				// List<ProtectedRegion> regions = new ArrayList<ProtectedRegion>();
				RegionManager mngr = getRegionManager(loc.getWorld());

				Class<?> blockVector3 = Reflection.getClass("com.sk89q.worldedit.math.BlockVector3");

				Method applicableRegions = mngr.getClass().getDeclaredMethod("getApplicableRegions", blockVector3);

				Method blockVectorAt = blockVector3.getDeclaredMethod("at", double.class, double.class, double.class);
				Object blockVector = blockVectorAt.invoke(null, loc.getX(), loc.getY(), loc.getZ());

				Object regionSet = applicableRegions.invoke(mngr, blockVector);

				Method getregions = regionSet.getClass().getMethod("getRegions");

				regions = new ArrayList<ProtectedRegion>(((HashSet<ProtectedRegion>) getregions.invoke(regionSet)));

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			// return new
			// ArrayList<ProtectedRegion>(getRegionManager(loc.getWorld()).getApplicableRegions(BlockVector3.at(loc.getX(),
			// loc.getY(), loc.getZ())).getRegions());
		} else {
			regions = new ArrayList<ProtectedRegion>(getRegionManager(loc.getWorld())
					.getApplicableRegions(new Vector(loc.getX(), loc.getY(), loc.getZ())).getRegions());
		}
		Collections.sort(regions, new RegionCompare());
		return regions;
	}

	public ProtectedCuboidRegion getProtectedCubiodRegion(String regionname, Location loc1, Location loc2) {
		if (getWgVer().contains("7.")) {
			try {

				Object bvloc1 = getBlockVectorV3(loc1);
				Object bvloc2 = getBlockVectorV3(loc2);

				Class<?> prCbRg = Reflection.getClass("com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion");

				return (ProtectedCuboidRegion) prCbRg.getConstructor(String.class, bvloc1.getClass(), bvloc2.getClass())
						.newInstance(regionname, bvloc1, bvloc2);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			return new ProtectedCuboidRegion(regionname,
					new com.sk89q.worldedit.BlockVector(loc1.getX(), loc1.getY(), loc1.getZ()),
					new com.sk89q.worldedit.BlockVector(loc2.getX(), loc2.getY(), loc2.getZ()));
		}
		return null;
	}

	public Object getBlockVectorV3(Location loc) throws Exception {
		Class<?> blockVector3 = Reflection.getClass("com.sk89q.worldedit.math.BlockVector3");

		Method blockVectorAt = blockVector3.getDeclaredMethod("at", double.class, double.class, double.class);
		return blockVectorAt.invoke(null, loc.getX(), loc.getY(), loc.getZ());
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
	
	public ApplicableRegionSet getApplicableRegionSet(Location loc) {
		if (getWgVer().contains("7.")) {
			try {
				RegionManager mngr = getRegionManager(loc.getWorld());

				Class<?> blockVector3 = Reflection.getClass("com.sk89q.worldedit.math.BlockVector3");

				Method applicableRegions = mngr.getClass().getDeclaredMethod("getApplicableRegions", blockVector3);

				Method blockVectorAt = blockVector3.getDeclaredMethod("at", double.class, double.class, double.class);
				Object blockVector = blockVectorAt.invoke(null, loc.getX(), loc.getY(), loc.getZ());

				Object regionSet = applicableRegions.invoke(mngr, blockVector);
				
				return (ApplicableRegionSet) regionSet;
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		} else {
			return getRegionManager(loc.getWorld()).getApplicableRegions(new Vector(loc.getX(), loc.getY(), loc.getZ()));
		}
	}


	public String getWgVer() {
		if (wgVerStr == null) {
			wgVerStr = Bukkit.getPluginManager().getPlugin("WorldGuard").getDescription().getVersion();
		}
		return wgVerStr;
	}
}