package nl.wouter.minetopiafarms.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.flags.registry.SimpleFlagRegistry;

import nl.wouter.minetopiafarms.Main;

public class CustomFlags {

	public static final StringFlag farmFlag = new StringFlag("minetopiafarms");

	public static void loadCustomFlag() {
		SimpleFlagRegistry registry = (SimpleFlagRegistry) WorldGuardLegacyManager.getInstance().getFlagRegistry();
		try {	
			registry.setInitialized(false);
			registry.register(farmFlag);
			registry.setInitialized(true);
		} catch (Exception e) {	
			Main.getPlugin().getLogger().warning("An error occured whilst loading MinetopiaFarms flag!");
			e.printStackTrace();
		}


	}
	
	public static boolean isAllowed(Player p, Location loc, String name) {
		ApplicableRegionSet set = WorldGuardLegacyManager.getInstance().getApplicableRegionSet(loc);
		LocalPlayer localPlayer = WorldGuardLegacyManager.getInstance().getWorldGuard().wrapPlayer(p);
		String type = set.queryValue(localPlayer, CustomFlags.farmFlag);
		
		return type != null && name.equalsIgnoreCase(type);
	}

	public static boolean hasFlag(Player p, Location loc) {
		ApplicableRegionSet set = WorldGuardLegacyManager.getInstance().getApplicableRegionSet(loc);
		LocalPlayer localPlayer = WorldGuardLegacyManager.getInstance().getWorldGuard().wrapPlayer(p);
		String type = set.queryValue(localPlayer, CustomFlags.farmFlag);
		
		return type != null;
	}
	
}