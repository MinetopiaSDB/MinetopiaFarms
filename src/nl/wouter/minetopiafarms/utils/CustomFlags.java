package nl.wouter.minetopiafarms.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;

import nl.wouter.minetopiafarms.Main;

public class CustomFlags {

	public static final StringFlag farmFlag = new StringFlag("minetopiafarms");

	public static void loadCustomFlag() {

		FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();

		try {
			
			registry.register(farmFlag);
			
		} catch (Exception e) {
			Main.getPlugin().getLogger().severe("Something went wrong whilst loading flag 'MinetopiaFarms'");
			e.printStackTrace();
		}

	}
	
	public static boolean isAllowed(Player p, Location loc, String name) {
		RegionManager rm = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(loc.getWorld()));
		ApplicableRegionSet set = rm.getApplicableRegions(WG.convertToSk89qBV(loc));
		LocalPlayer localPlayer = WG.wgp.wrapPlayer(p);
		String type = set.queryValue(localPlayer, CustomFlags.farmFlag);
		
		return type != null && name.equalsIgnoreCase(type);
	}

	public static boolean hasFlag(Player p, Location loc) {
		RegionManager rm = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(loc.getWorld()));
		ApplicableRegionSet set = rm.getApplicableRegions(WG.convertToSk89qBV(loc));
		LocalPlayer localPlayer = WG.wgp.wrapPlayer(p);
		String type = set.queryValue(localPlayer, CustomFlags.farmFlag);
		
		return type != null;
	}
	
}
