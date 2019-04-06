package nl.wouter.minetopiafarms.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import nl.wouter.minetopiafarms.Main;
import nl.wouter.minetopiafarms.utils.Updat3r;
import nl.wouter.minetopiafarms.utils.Utils;

public class MTFarmsCMD implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("minetopiafarms.hulp")) {
			sender.sendMessage(Utils.color("&4ERROR: &cJe mist de permissie minetopiafarms.hulp"));
			return true;
		}
		sender.sendMessage(Utils.color("&bUitleg: &3Voer de command uit bij een region die (bijv.) een farm moet worden."));
		
		sender.sendMessage(Utils.color("&3Houthakkers: \n&3&3/rg flag &b<Region> &3minetopiafarms houthakker"));
		sender.sendMessage(Utils.color("&3Mijnwerker: \n&3&3/rg flag &b<Region> &3minetopiafarms mijn"));
		sender.sendMessage(Utils.color("&3Boer: \n&3&3/rg flag &b<Region> &3minetopiafarms farm"));
		
		sender.sendMessage("\n ");
		sender.sendMessage(Utils.color("&3Sloop op regions met MinetopiaFarms flag: &bminetopiafarms.bypassregions"));
		
		//More or less just for debug reasons.
		if (args.length == 1 && args[0].equalsIgnoreCase("updateinfo")) {
			sender.sendMessage(Utils.color("&3Cached 'latest': &b" + Updat3r.getInstance().getLatestCached().getVersion()));
			sender.sendMessage(Utils.color("&3Latest: &b" + Updat3r.getInstance().getLatestUpdate(Updat3r.PROJECT_NAME, Updat3r.API_KEY).getVersion()));
			sender.sendMessage(Utils.color("&3Actual version: &b" + Main.pl.getDescription().getVersion()));
		}
		if (args.length == 1 && args[0].equalsIgnoreCase("update")) {
			if (!Updat3r.getInstance().getLatestCached().isNewer()) {
				sender.sendMessage(Utils.color("&cEr is geen update beschikbaar!"));
				return true;
			}
			sender.sendMessage(Utils.color("&3We gaan de update nu installeren!"));
			Updat3r.getInstance().downloadLatest(Updat3r.getInstance().getLatestCached().getDownloadLink(), "MinetopiaFarms", Main.pl);
			Bukkit.reload();
		}
		return true;
	}

}
