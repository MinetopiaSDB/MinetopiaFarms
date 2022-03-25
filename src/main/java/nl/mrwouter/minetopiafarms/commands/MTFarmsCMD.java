package nl.mrwouter.minetopiafarms.commands;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.util.Colorizer;
import net.citizensnpcs.npc.skin.SkinnableEntity;
import nl.mrwouter.minetopiafarms.Main;
import nl.mrwouter.minetopiafarms.utils.Updat3r;
import nl.mrwouter.minetopiafarms.utils.Utils;

public class MTFarmsCMD implements CommandExecutor, TabCompleter {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("minetopiafarms.hulp")) {
			sender.sendMessage(Utils.color("&4ERROR: &cJe mist de permissie minetopiafarms.hulp"));
			return true;
		}
		// More or less just for debug reasons.
		if (args.length == 1 && args[0].equalsIgnoreCase("updateinfo")) {
			sender.sendMessage(
					Utils.color("&3Cached 'latest': &b" + Updat3r.getInstance().getLatestCached().getVersion()));
			sender.sendMessage(Utils.color("&3Latest: &b"
					+ Updat3r.getInstance().getLatestUpdate(Updat3r.PROJECT_NAME, Updat3r.API_KEY).getVersion()));
			sender.sendMessage(Utils.color("&3Actual version: &b" + Main.getPlugin().getDescription().getVersion()));
		} else if (args.length == 1 && args[0].equalsIgnoreCase("update")) {
			if (!Updat3r.getInstance().getLatestCached().isNewer()) {
				sender.sendMessage(Utils.color("&cEr is geen update beschikbaar!"));
				return true;
			}
			sender.sendMessage(Utils.color("&3We gaan de update nu installeren!"));
			Updat3r.getInstance().downloadLatest(Updat3r.getInstance().getLatestCached().getDownloadLink(),
					"MinetopiaFarms", Main.getPlugin());
			Bukkit.reload();
		} else if (args.length == 1 && args[0].equalsIgnoreCase("spawnnpc")) {

			if (Bukkit.getPluginManager().getPlugin("Citizens") == null) {
				sender.sendMessage(Utils.color("&4ERROR: &cCitizens is hiervoor benodigd!"));
				return true;
			}

			if (!(sender instanceof Player)) {
				sender.sendMessage(Utils.color("&4ERROR: &cJe moet een speler zijn om dit te doen!"));
				return true;
			}
			Player player = (Player) sender;

			NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, Main.getMessage("NPC.Name"));

			npc.data().setPersistent("player-skin-name", Main.getMessage("NPC.Skin.Name"));
			npc.data().setPersistent("player-skin-use-latest", false);

			npc.spawn(player.getLocation());

			sender.sendMessage(Utils.color("&3NPC gespanwed op jouw huidige locatie!"));
		} else {
			sender.sendMessage(Utils.color("&bUitleg: &3Voer de command uit bij een region die (bijv.) een farm moet worden."));
			sender.sendMessage(Utils.color("&3Houthakkers: \n&3&3/rg flag &b<Region> &3minetopiafarms houthakker"));
			sender.sendMessage(Utils.color("&3Mijnwerker: \n&3&3/rg flag &b<Region> &3minetopiafarms mijn"));
			sender.sendMessage(Utils.color("&3Boer: \n&3&3/rg flag &b<Region> &3minetopiafarms farm"));
			sender.sendMessage(" ");
			sender.sendMessage(Utils.color("&3Spawn een verkoop NPC: \n&b/mtfarms spawnnpc"));
			sender.sendMessage(" ");
			sender.sendMessage(Utils.color("&3Sloop op regions met MinetopiaFarms flag: &bminetopiafarms.bypassregions"));
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
		return Arrays.asList("update", "updateinfo", "spawnnpc");
	}
}
