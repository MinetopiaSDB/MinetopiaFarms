package nl.wouter.minetopiafarms.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

import nl.wouter.minetopiafarms.Main;

public class CustomFlags {

	public static final StringFlag farmFlag = new StringFlag("minetopiafarms");

	public static void loadCustomFlag() {
		FlagRegistry registry = WorldGuardLegacyManager.getInstance().getFlagRegistry();

		try {
			
			registry.register(farmFlag);
			
		} catch (Exception e) {
			Main.getPlugin().getLogger().severe("Something went wrong whilst loading flag 'MinetopiaFarms'");
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