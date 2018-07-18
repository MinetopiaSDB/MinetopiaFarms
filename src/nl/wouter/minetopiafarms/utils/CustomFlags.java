package nl.wouter.minetopiafarms.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

import nl.wouter.minetopiafarms.Main;

public class CustomFlags {

	public static final StringFlag farmFlag = new StringFlag("minetopiafarms");

	public static void loadCustomFlag() {
		// ... do your own plugin things, get the WorldGuard object, etc

		FlagRegistry registry = getWorldGuard().getFlagRegistry();

		try {
			
			registry.register(farmFlag);
			
		} catch (Exception e) {
			Main.pl.getLogger().severe("Something went wrong whilst loading flag 'MinetopiaFarms'");
			e.printStackTrace();
		}

	}

	public static WorldGuardPlugin getWorldGuard() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		if ((plugin == null) || (!(plugin instanceof WorldGuardPlugin))) {
			return null;
		}
		return (WorldGuardPlugin) plugin;
	}
	
	public static boolean isAllowed(Player p, String name) {
		ApplicableRegionSet set = CustomFlags.getWorldGuard().getRegionManager(p.getWorld()).getApplicableRegions(p.getLocation());
		LocalPlayer localPlayer = CustomFlags.getWorldGuard().wrapPlayer(p);
		String type = set.queryValue(localPlayer, CustomFlags.farmFlag);
		
		return type != null || !name.equalsIgnoreCase(type);
	}

	public static boolean hasFlag(Player p) {
		ApplicableRegionSet set = CustomFlags.getWorldGuard().getRegionManager(p.getWorld()).getApplicableRegions(p.getLocation());
		LocalPlayer localPlayer = CustomFlags.getWorldGuard().wrapPlayer(p);
		String type = set.queryValue(localPlayer, CustomFlags.farmFlag);
		
		return type != null;
	}
	
}
