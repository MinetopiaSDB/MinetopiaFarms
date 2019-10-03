package nl.mrwouter.minetopiafarms.utils;

import java.util.Comparator;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class RegionCompare implements Comparator<ProtectedRegion> {
	@Override
	public int compare(ProtectedRegion o1, ProtectedRegion o2) {
		if (o1.getPriority() == o2.getPriority()) {
			return 0;
		}
		if (o1.getPriority() < o2.getPriority()) {
			return 1;
		}
		if (o1.getPriority() > o2.getPriority()) {
			return -1;
		}
		return 12345;
	}
}