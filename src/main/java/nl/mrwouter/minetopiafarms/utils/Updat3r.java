package nl.mrwouter.minetopiafarms.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import nl.mrwouter.minetopiafarms.Main;

public class Updat3r {

	private boolean taskRunning = false;
	private Update update = null;

	private static Updat3r instance = null;
	public static String PROJECT_NAME = "MinetopiaFarms";
	public static String API_KEY = "vwZBhEnQH9kwmXf7bhx3ej3Mp6SwqM87";

	public static Updat3r getInstance() {
		if (instance == null) {
			instance = new Updat3r();
		}
		return instance;
	}

	public void startTask() {
		if (taskRunning) {
			return;
		}
		taskRunning = true;
		Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(), () -> update = getLatestUpdate(PROJECT_NAME, API_KEY), 30 * 20L, 30 * 60 * 20L);
	}

	public Update getLatestUpdate(String project, String apiKey) {
		try {
			URL url = new URL("https://updates.mrwouter.nl/api/v1/updates/?project=" + project + "&key=" + apiKey
					+ "&show=latest");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("User-Agent", project + " UpdateChecker");
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			conn.connect();

			JsonParser jp = new JsonParser();
			JsonElement root = jp.parse(new InputStreamReader((InputStream) conn.getContent()));
			JsonObject rootobj = root.getAsJsonObject();

			int errorcode = rootobj.get("status").getAsInt();
			if (errorcode != 200) {
				Main.getPlugin().getLogger().warning(
						"[Updat3r] An error has occured: " + rootobj.get("message").getAsString());
				return null;
			}
			JsonArray updates = rootobj.get("updates").getAsJsonArray();
			for (JsonElement update : updates) {
				JsonObject updateObj = update.getAsJsonObject();
				String version = updateObj.get("version").getAsString();
				String download = updateObj.get("download").getAsString();
				String releaseDate = updateObj.get("releaseDate").getAsString();
				boolean critical = updateObj.get("critical").getAsBoolean();
				return new Update(version, download, releaseDate, critical);
			}
			return null;
		} catch (Exception ex) {
			Main.getPlugin().getLogger().warning("[Updat3r] " +
					"An error has occured. Please report the stacktrace below to the developer of " + project);
			ex.printStackTrace();
			return null;
		}
	}

	public Update getLatestCached() {
		if (update == null) {
			update = getLatestUpdate(PROJECT_NAME, API_KEY);
		}
		return update;
	}

	public void downloadLatest(String fileURL, String project, Plugin pl) {
		try {
			URL url = new URL(fileURL);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestProperty("User-Agent", project + " UpdateChecker");
			int responseCode = httpConn.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream inputStream = httpConn.getInputStream();
				String saveFilePath = new File(
						pl.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();

				FileOutputStream outputStream = new FileOutputStream(saveFilePath);

				int bytesRead = -1;
				byte[] buffer = new byte[4096];
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}

				outputStream.close();
				inputStream.close();
			} else {
				Main.getPlugin().getLogger().warning("[Updat3r] " +
						"An error has occured whilst downloading this resource. Please report the stacktrace below to the developer of "
						+ project + " (resp. code: " + responseCode + ")");
			}
			httpConn.disconnect();
		} catch (Exception ex) {
			Main.getPlugin().getLogger().warning("[Updat3r] " +
					"An error has occured whilst downloading this resource. Please report the stacktrace below to the developer of "
					+ project);
			ex.printStackTrace();
		}
	}

	public void sendUpdateMessage(Player p) {
		p.sendMessage("   §3-=-=-=[§bMinetopiaFarms§3]=-=-=-   ");
		p.sendMessage("§3Er is een update beschikbaar voor §bMinetopiaFarms§3!");
		p.sendMessage("§3Je maakt nu gebruik van versie §b" + Main.getPlugin().getDescription().getVersion() + "§3.");
		p.sendMessage("§3De nieuwste versie is §b" + update.getVersion());
		if (update.isCritical()) {
			p.sendMessage("§3§lLet op, deze build is gemarkeert als belangrijk. Installeer hem zo snel mogelijk!");
		}
		p.sendMessage("§3Om deze update te installeren, type §b/mtfarms update.");
		p.sendMessage("   §3-=-=-=[§bMinetopiaFarms§3]=-=-=-   ");
	}

	public void sendUpdateMessageLater(Player p) {
		Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getPlugin(), () -> {
			if (p.isOp()) {
				if (update != null && update.isNewer()) {
					sendUpdateMessage(p);
				}
			}
		}, 40L);
	}

	public static class Update {

		private String version, downloadlink, releaseDate;
		private boolean critical;

		public Update(String version, String downloadlink, String releaseDate, boolean critical) {
			this.version = version;
			this.downloadlink = downloadlink;
			this.releaseDate = releaseDate;
			this.critical = critical;
		}

		public String getVersion() {
			return version;
		}

		public String getDownloadLink() {
			return downloadlink;
		}

		public String getReleaseDate() {
			return releaseDate;
		}

		public boolean isCritical() {
			return critical;
		}

		public boolean isNewer() {
			String[] ver = version.split("\\.");
			String[] sdbver = Main.getPlugin().getDescription().getVersion().split("\\.");

			if (ver.length == 1 || ver[0].equals("?")) {
				return false;
			} else {
				if (Integer.parseInt(ver[0]) > Integer.parseInt(sdbver[0])) {
					return true;
				}
				if (Integer.parseInt(ver[1]) > Integer.parseInt(sdbver[1])) {
					return true;
				}
				if (ver.length >= 3) {
					if (Integer.parseInt(ver[1]) >= Integer.parseInt(sdbver[1])) {
						if (sdbver.length <= 2) {
							return true;
						} else return Integer.parseInt(ver[2]) > Integer.parseInt(sdbver[2]);
					}
				}
			}
			return false;
		}
	}
}
