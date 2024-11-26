package nl.mrwouter.minetopiafarms.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.npc.skin.Skin;
import net.citizensnpcs.npc.skin.SkinnableEntity;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import nl.mrwouter.minetopiafarms.Main;
import nl.mrwouter.minetopiafarms.utils.Updat3r;
import nl.mrwouter.minetopiafarms.utils.Utils;
import org.bukkit.util.StringUtil;

public class MTFarmsCMD implements TabExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("minetopiafarms.hulp")) {
			sender.sendMessage(Utils.color("&4ERROR: &cJe mist de permissie minetopiafarms.hulp"));
			return true;
		}

		if (args.length == 1 && args[0].equalsIgnoreCase("update")) {
			Updat3r.Update update;
			try {
				update = Updat3r.getInstance().getLatestCached().get();
			} catch (Exception ex) {
				ex.printStackTrace();
				sender.sendMessage(Utils.color("&cEr is geen update beschikbaar!"));
				return true;
			}

			if (update == null || !update.isNewer()) {
				sender.sendMessage(Utils.color("&cEr is geen update beschikbaar!"));
				return true;
			}

			sender.sendMessage(Utils.color("&3We gaan de update nu installeren!"));

			Updat3r.getInstance().downloadLatest(update.getDownloadLink(),
					Main.getPlugin());

			for (Player player : Bukkit.getOnlinePlayers()) {
				player.kickPlayer("Installing update..");
			}

			Bukkit.getScheduler().runTask(Main.getPlugin(), Bukkit::reload);
			return true;
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


			npc.spawn(player.getLocation());

			SkinTrait trait = npc.getOrAddTrait(SkinTrait.class);
			trait.setSkinName(Main.getMessage("NPC.Skin.Name"), true);
			npc.despawn(DespawnReason.PENDING_RESPAWN);
			npc.spawn(player.getLocation());

			sender.sendMessage(Utils.color("&3NPC gespawned op jouw huidige locatie!"));
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
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
		if(args.length == 1){
			return getApplicableTabCompleters(args[0], Arrays.asList("spawnnpc", "update"));
		}

		return null;
	}

	public ArrayList<String> getApplicableTabCompleters(String arg, List<String> completions) {
		return StringUtil.copyPartialMatches(arg, completions, new ArrayList<String>(completions.size()));
	}
}
