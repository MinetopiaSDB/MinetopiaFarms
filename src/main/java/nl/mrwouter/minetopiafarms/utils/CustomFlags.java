package nl.mrwouter.minetopiafarms.utils;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import nl.mrwouter.minetopiafarms.Main;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

import java.util.List;
import java.util.Optional;

public class CustomFlags {

	public static StringFlag farmFlag = new StringFlag("minetopiafarms");

	public static void loadCustomFlag(FlagRegistry flagRegistry) {
		try {
			flagRegistry.register(farmFlag);
		} catch (FlagConflictException e) {
			farmFlag = (StringFlag) flagRegistry.get("minetopiafarms");
		}
	}

	public static boolean isAllowed(Player player, Location location, String name) {
		List<ProtectedRegion> regions = Main.getPlugin().getWorldGuard().getRegions(location);

		return regions.stream()
				.map(protectedRegion -> Optional.ofNullable(protectedRegion.getFlag(farmFlag))
						.orElse("")
						.equalsIgnoreCase(name))
				.findAny().isPresent();
	}

	public static boolean hasFlag(Location location) {
		List<ProtectedRegion> regions = Main.getPlugin().getWorldGuard().getRegions(location);

		return regions.stream()
				.map(protectedRegion -> protectedRegion.getFlag(farmFlag) != null) // not null, so present
				.findAny().isPresent();
	}

}