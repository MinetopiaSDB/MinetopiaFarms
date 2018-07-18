package nl.wouter.minetopiafarms.utils;

import java.net.URL;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import nl.wouter.minetopiafarms.Main;

public class UpdateChecker {
	static String resourceID = "57385";
	private static final String USER_AGENT = "MINETOPIAFARMS";

	public static String getVersion() {
		try {
			String version = "?";
			System.setProperty("User-Agent", USER_AGENT);
			URL website = new URL("https://api.spiget.org/v2/resources/" + resourceID + "/versions/");
			Scanner s = new Scanner(website.openStream());
			while (s.hasNext()) {
				boolean b = false;
				String sn = s.next();
				if (sn.contains("\"name\"")) {
					b = true;
				}
				if (b) {
					version = s.next().replaceAll("\"", "").replaceAll(",", "");
					break;
				}
			}
			s.close();
			if (version.equals("?")) {
				URL website2 = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceID);
				Scanner s2 = new Scanner(website2.openStream());
				if (s2.hasNext()) {
					version = s2.next();
				}
				s2.close();
			}
			return version;
		} catch (Exception ex) {
			ex.printStackTrace();
			Main.pl.getLogger().info("Failed to check for a update on spiget API (V2).");
		}
		return "?";
	}

	
	//Yes, this is indeed shit, but it works for now.
	public static boolean isUpdateAvailible() {
		try {
			String version = getVersion();
			String[] ver = version.split("\\.");
			String[] sdbver = Main.pl.getDescription().getVersion().split("\\.");

			if (ver.length == 1 || ver[0].equals("?")) {
				return false;
			} else {
				if (ver.length <= 1) {
					return false;
				} else {
					if (Integer.valueOf(ver[0]) > Integer.valueOf(sdbver[0])) {
						return true;
					}
					if (Integer.valueOf(ver[1]) > Integer.valueOf(sdbver[1])) {
						return true;
					}
					if (ver.length >= 3) {
						if (Integer.valueOf(ver[1]) >= Integer.valueOf(sdbver[1])) {
							if (sdbver.length <= 2) {
								return true;
							} else if (Integer.valueOf(ver[2]) > Integer.valueOf(sdbver[2])) {
								return true;
							}
						}
					}
				}
			}
		} catch (Exception ex) {
		}
		return false;
	}

	public static void sendUpdateMessageLater(Player p) {
		Bukkit.getScheduler().runTaskLaterAsynchronously(Main.pl, new Runnable() {
			public void run() {
				if (p.isOp()) {
					if (UpdateChecker.isUpdateAvailible()) {
						String ver = getVersion();
						p.sendMessage("   §3-=-=-=[§bMinetopiaFarms§3]=-=-=-   ");
						p.sendMessage("§3Er is een update beschikbaar voor §bMinetopiaFarms§3!");
						p.sendMessage(
								"§3Je maakt nu gebruik van versie §b" + Main.pl.getDescription().getVersion() + "§3.");
						p.sendMessage("§3De nieuwste versie is §b" + ver);
						p.sendMessage("§3Om deze update te installeren, ga naar https://spigotmc.org/resources/"
								+ resourceID);
						p.sendMessage("   §3-=-=-=[§bMinetopiaFarms§3]=-=-=-   ");
					}
				}
			}
		}, 35l);
	}
}
