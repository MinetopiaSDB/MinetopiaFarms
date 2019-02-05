package nl.wouter.minetopiafarms.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WG {
	public static WorldGuardPlugin wgp;
	public static WorldEditPlugin wep;

	public static boolean hasWorldGuard() {
		return wgp != null;
	}

	public static boolean hasWorldEdit() {
		return wep != null;
	}

	public static boolean setWorldGuard(Plugin plugin) {
		wgp = (WorldGuardPlugin) plugin;
		return true;
	}

	public static boolean setWorldEdit(Plugin plugin) {
		wep = (WorldEditPlugin) plugin;
		return true;
	}
	
	public static BlockVector convertToSk89qBV(Location location) {
        return (new BlockVector(location.getX(), location.getY(), location.getZ()));
    }
	
	public static ProtectedRegion createRegion(Player p, String id) throws StorageException {
		LocalSession l = WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(p));
		Region s;
		try {
			s = l.getSelection(l.getSelectionWorld());
		} catch (IncompleteRegionException e) {
			return null;
		}
		RegionManager rm = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(p.getWorld()));
		rm.removeRegion(id);
		ProtectedRegion region;
	      // Detect the type of region from WorldEdit
        if (s instanceof Polygonal2DRegion) {
            Polygonal2DRegion polySel = (Polygonal2DRegion) s;
            int minY = polySel.getMinimumY();
            int maxY = polySel.getMaximumY();
            region = new ProtectedPolygonalRegion(id, polySel.getPoints(), minY, maxY);
        } else { /// default everything to cuboid
            region = new ProtectedCuboidRegion(id,
            		s.getMinimumPoint().toBlockVector(),
            		s.getMaximumPoint().toBlockVector());
        }
		region.setPriority(11); /// some relatively high priority
		region.setFlag(Flags.INTERACT, State.DENY);
		region.setFlag(Flags.INTERACT.getRegionGroupFlag(), RegionGroup.NON_MEMBERS);
		rm.addRegion(region);
		rm.save();
		return region;
	}
	
	public static ArrayList<ProtectedRegion> getRegionsIn(Location loc) {
		ArrayList<ProtectedRegion> inRegions = new ArrayList<ProtectedRegion>();

		RegionManager rm = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(loc.getWorld()));
		
		for (ProtectedRegion protectedRegion : rm.getApplicableRegions(convertToSk89qBV(loc))) inRegions.add(protectedRegion);

		return inRegions;
	}
	
	public static ProtectedRegion getRegionfromStringList(List<String> str, Location loc) {
		RegionManager rm = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(loc.getWorld()));
		ApplicableRegionSet mogreg = rm.getApplicableRegions(convertToSk89qBV(loc));
		for (ProtectedRegion mogregion : mogreg) {
			for (String strgo : str) {
				if(strgo.equalsIgnoreCase(mogregion.getId())) {
					return mogregion;
				}
			}
		}
		
		//Niks gevonden!
		return null;
	}
	
	public static ProtectedRegion getRegionfromString(String str, Location loc) {
		RegionManager rm = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(loc.getWorld()));
		ApplicableRegionSet mogreg = rm.getApplicableRegions(convertToSk89qBV(loc));
		for (ProtectedRegion mogregion : mogreg) {
			if(str.equalsIgnoreCase(mogregion.getId())) {
				return mogregion;
			}
		}
		
		//Niks gevonden!
		return null;
	}
	
	public static ProtectedRegion getRegionfromStringListInWorld(List<String> str, Location loc) {
		RegionManager rm = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(loc.getWorld()));
		Map<String, ProtectedRegion> mp = rm.getRegions();
		Iterator<Entry<String, ProtectedRegion>> it = mp.entrySet().iterator();
	    while (it.hasNext()) {
			Map.Entry<String, ProtectedRegion> pair = (Entry<String, ProtectedRegion>) it.next();
			for (String string : str) {
		        if(pair.getValue().getId().equals(string)) {
		        	return pair.getValue();
		        }
			}
	    }
		
		//Niks gevonden!
		return null;
	}
	
	public static ProtectedRegion getRegionfromStringInWorld(String str, Location loc) {
		String strgood = str.toLowerCase();
		RegionManager rm = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(loc.getWorld()));
		Map<String, ProtectedRegion> mp = rm.getRegions();
		ProtectedRegion reg = mp.get(strgood);
		if (reg != null) {
			return reg;
		} else {
			return null;
		}
	}
	
	public static boolean isMember(Player p, ProtectedRegion region, OfflinePlayer target) {
		World w = p.getWorld();
		RegionManager rm = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(w));
		
		ProtectedRegion currentRegion = rm.getRegion(region.getId());
		DefaultDomain currentMembers = currentRegion.getMembers();
		return currentMembers.contains(target.getUniqueId());
	}
	
	public static boolean isOwner(Player p, ProtectedRegion region, OfflinePlayer target) {
		World w = p.getWorld();
		RegionManager rm = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(w));
		
		ProtectedRegion currentRegion = rm.getRegion(region.getId());
		DefaultDomain currentOwners = currentRegion.getOwners();
		return currentOwners.contains(target.getUniqueId());
	}
	
	public static boolean addMember(Player p, ProtectedRegion region, OfflinePlayer newmember) {
		try {
			World w = p.getWorld();
			RegionManager rm = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(w));
			
			ProtectedRegion currentRegion = rm.getRegion(region.getId());
			DefaultDomain currentMembers = currentRegion.getMembers();
			currentMembers.addPlayer(UUID.fromString(newmember.getUniqueId().toString()));
	
			rm.save();
		
			return true;
		}
		catch (StorageException e) {
			return false;
		}
	}

	public static boolean addOwner(Player p, ProtectedRegion region, OfflinePlayer newowner) {
		try {
			World w = p.getWorld();
			RegionManager rm = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(w));
			
			ProtectedRegion currentRegion = rm.getRegion(region.getId());
			DefaultDomain currentOwners = currentRegion.getOwners();
			currentOwners.addPlayer(UUID.fromString(newowner.getUniqueId().toString()));
	
			rm.save();
		
			return true;
		}
		catch (StorageException e) {
			return false;
		}
	}

	public static boolean removeMember(Player p, ProtectedRegion region, OfflinePlayer newmember) {
		try {
			World w = p.getWorld();
			RegionManager rm = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(w));
			
			ProtectedRegion currentRegion = rm.getRegion(region.getId());
			DefaultDomain currentMembers = currentRegion.getMembers();
			currentMembers.removePlayer(UUID.fromString(newmember.getUniqueId().toString()));
	
			rm.save();
		
			return true;
		}
		catch (StorageException e) {
			return false;
		}
	}

	public static boolean removeOwner(Player p, ProtectedRegion region, UUID newowner) {
		try {
			World w = p.getWorld();
			RegionManager rm = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(w));
			
			ProtectedRegion currentRegion = rm.getRegion(region.getId());
			DefaultDomain currentOwners = currentRegion.getOwners();
			currentOwners.removePlayer(newowner);
	
			rm.save();
		
			return true;
		}
		catch (StorageException e) {
			return false;
		}
	}
	
	public static ArrayList<String> getMembers(Player p, ProtectedRegion region) {
		World w = p.getWorld();
		RegionManager rm = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(w));
		
		ProtectedRegion currentRegion = rm.getRegion(region.getId());
		DefaultDomain currentMembers = currentRegion.getMembers();
		
		ArrayList<String> members = new ArrayList<String>();
		for (UUID uuid : currentMembers.getUniqueIds()) {
			OfflinePlayer member = Bukkit.getServer().getOfflinePlayer(uuid);
			members.add(member.getName());
		}
		return members;
	}

	public static ArrayList<String> getOwners(Player p, ProtectedRegion region) {
		World w = p.getWorld();
		RegionManager rm = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(w));
		
		ProtectedRegion currentRegion = rm.getRegion(region.getId());
		DefaultDomain currentOwners = currentRegion.getOwners();
		
		ArrayList<String> owners = new ArrayList<String>();
		for (UUID uuid : currentOwners.getUniqueIds()) {
			OfflinePlayer owner = Bukkit.getServer().getOfflinePlayer(uuid);
			owners.add(owner.getName());
		}
		return owners;
	}
	
	public static boolean removeRegion(Player p, String name) {
		try {
			World w = p.getWorld();
			RegionManager rm = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(w));
			
			ProtectedRegion currentRegion = rm.getRegion(name);
			if (currentRegion == null) {
				return false;
			}
			
			rm.removeRegion(name);
			rm.save();
			
			return true;
		}
		catch (StorageException e) {
			return false;
		}
	}
}