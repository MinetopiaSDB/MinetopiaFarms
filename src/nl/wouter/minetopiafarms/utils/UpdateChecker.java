package nl.wouter.minetopiafarms.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import nl.wouter.minetopiafarms.Main;

public class UpdateChecker {
    
	private static final String USER_AGENT = "MINETOPIAFARMS";
    static String resourceID = "57385";

	private boolean taskRunning = false;
	public String version = null;
	
	private static UpdateChecker checker;
	
	public static UpdateChecker getInstance() {
		if (checker == null) {
			checker = new UpdateChecker();
		}
		return checker;
	}
	
	@SuppressWarnings("deprecation")
	public void startTask() {
		if (taskRunning) {
			return;
		}
		taskRunning = true;
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(Main.pl, new Runnable() {
			public void run() {
				version = getLatestVersion();
			}
		}, 30 * 20l, 30 * 60 * 20l);
	}
	
	public String getVersion() {
		if (version == null) {
			version = getLatestVersion();
		}
		return version;
	}

	public String getLatestVersion() {
		try {
			String version = "?";
			URL website = new URL("https://updates.minetopiasdb.nl/updatechecker.php?resourceid=" + resourceID);
			HttpURLConnection connection = (HttpURLConnection) website.openConnection();
			connection.addRequestProperty("User-Agent", USER_AGENT);
			InputStream inputStream = connection.getInputStream();
			Scanner s = new Scanner(inputStream);
			if (s.hasNext()) {
				version = s.next();
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
    private boolean isUpdateAvailible() {
        try {
            String version = getVersion();
            String[] ver = version.split("\\.");
            String[] sdbver = Main.pl.getDescription().getVersion().split("\\.");

            if (ver.length == 1 || ver[0].equals("?")) {
                return false;
            } else {
                if (Integer.valueOf(ver[0]) > Integer.valueOf(sdbver[0]) || Integer.valueOf(ver[1]) > Integer.valueOf(sdbver[1])) {
                    return true;
                }
                if (ver.length >= 3 && Integer.valueOf(ver[1]) >= Integer.valueOf(sdbver[1])) {
                    if (sdbver.length <= 2 || Integer.valueOf(ver[2]) > Integer.valueOf(sdbver[2])) {
                        return true;
                    }
                }
            }
        } catch (Exception ex) {
        }
        return false;
    }

    public void sendUpdateMessageLater(Player p) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.pl, new Runnable() {
            public void run() {
                if (p.isOp()) {
                    if (isUpdateAvailible()) {
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
