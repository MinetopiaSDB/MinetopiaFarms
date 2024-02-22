package nl.mrwouter.minetopiafarms.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

import org.bukkit.plugin.Plugin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import nl.mrwouter.minetopiafarms.Main;

public class Updat3r {

	private Update update = null;
	public static String PROJECT_NAME = "MinetopiaFarms";
	public static String API_KEY = "6898c758-8cd8-4a53-ac03-b2e4fd48b6cd";
	private long lastCheckedForUpdate;

	private static Updat3r instance = null;

	public static Updat3r getInstance() {
		if (instance == null) {
			instance = new Updat3r();
		}
		return instance;
	}

	private CompletableFuture<Update> getLatestUpdate0(String project, String apiKey) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				URL requestUrl = new URL("https://updates.mrwouter.nl/api/v2/updates/" + project + "/latest");
				HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();

				connection.setRequestProperty("Authorization", "Bearer " + apiKey);
				connection.setRequestProperty("User-Agent", project + " UpdateChecker");
				connection.setConnectTimeout(5000);
				connection.setReadTimeout(5000);

				try (InputStreamReader reader = new InputStreamReader((InputStream) connection.getContent())) {
					JsonElement root = JsonParser.parseReader(reader);
					JsonObject rootObject = root.getAsJsonObject();

					int errorCode = rootObject.get("status").getAsInt();
					if (errorCode != 200) {
						Main.getPlugin().getLogger().warning(
								"[Updat3r] [" + project + "] An error has occured: " + rootObject.get("message").getAsString());
						return null;
					}

					JsonArray updates = rootObject.get("updates").getAsJsonArray();
					if (updates.isEmpty()) {
						return null; // no updates
					}

					JsonObject update = updates.get(0).getAsJsonObject();
					return new Update(update.get("version").getAsString(), update.get("download").getAsString());
				}
			} catch (IOException ex) {
				Main.getPlugin().getLogger().warning("[Updat3r] An error has occured. If this happens multiple " +
						"times in a row, report the stacktrace below to the developer of " + project);
				ex.printStackTrace();
			} finally {
				lastCheckedForUpdate = System.currentTimeMillis();
			}
			return null;
		});
	}

	public CompletableFuture<Update> getLatestCached() {
		if (lastCheckedForUpdate + 45 * 60 * 1000 < System.currentTimeMillis()) {
			return getLatestUpdate0(PROJECT_NAME, API_KEY)
					.thenApply(update -> this.update = update);
		}

		return CompletableFuture.supplyAsync(() -> update);
	}

	public void downloadLatest(String fileUrl, Plugin plugin) {
		try {
			URL url = new URL(fileUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("User-Agent", PROJECT_NAME + " UpdateChecker");
			conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
			int responseCode = conn.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream inputStream = conn.getInputStream();
				String saveFilePath = new File(
						plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();

				FileOutputStream outputStream = new FileOutputStream(saveFilePath);

				int bytesRead;
				byte[] buffer = new byte[4096];
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}

				outputStream.close();
				inputStream.close();
			} else {
				Main.getPlugin().getLogger().warning("[Updat3r] An error has occured whilst downloading this resource. Please report the stacktrace below to the developer of "
						+ PROJECT_NAME + " (resp. code: " + responseCode + ")");
			}
			conn.disconnect();
		} catch (Exception ex) {
			Main.getPlugin().getLogger().warning("[Updat3r] An error has occured whilst downloading this resource. Please report the stacktrace below to the developer of "
					+ PROJECT_NAME);
			ex.printStackTrace();
		}
	}

	public boolean isNewer(String newerVersion, String currentVersion) {
		String[] ver = newerVersion.split("\\.");
		String[] sdbver = currentVersion.split("\\.");
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
					if (sdbver.length == 2) {
						return true;
					} else if (Integer.parseInt(ver[2]) > Integer.parseInt(sdbver[2])) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static class Update {

		private final String version, downloadlink;

		public Update(String version, String downloadlink) {
			this.version = version;
			this.downloadlink = downloadlink;
		}

		public String getVersion() {
			return version;
		}

		public String getDownloadLink() {
			return downloadlink;
		}

		public boolean isNewer() {
			return Updat3r.getInstance().isNewer(version, Main.getPlugin().getDescription().getVersion());
		}
	}
}