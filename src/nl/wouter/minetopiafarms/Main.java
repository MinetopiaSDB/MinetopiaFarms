package nl.wouter.minetopiafarms;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.material.Crops;
import org.bukkit.plugin.java.JavaPlugin;

import nl.wouter.minetopiafarms.commands.KiesCMD;
import nl.wouter.minetopiafarms.commands.MTFarmsCMD;
import nl.wouter.minetopiafarms.events.BlockBreaker;
import nl.wouter.minetopiafarms.events.FarmListener;
import nl.wouter.minetopiafarms.events.FishListener;
import nl.wouter.minetopiafarms.events.InventoryClickListener;
import nl.wouter.minetopiafarms.events.NPCClickListener;
import nl.wouter.minetopiafarms.events.TreeFarmer;
import nl.wouter.minetopiafarms.utils.CustomFlags;
import nl.wouter.minetopiafarms.utils.Updat3r;
import nl.wouter.minetopiafarms.utils.Utils;
import nl.wouter.minetopiafarms.utils.Utils.TreeObj;

public class Main extends JavaPlugin {

    private static Main pl;


    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new BlockBreaker(), this);
        Bukkit.getPluginManager().registerEvents(new FarmListener(), this);
        Bukkit.getPluginManager().registerEvents(new TreeFarmer(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new FishListener(), this);


        if (Bukkit.getPluginManager().getPlugin("Citizens") != null) {
            Bukkit.getPluginManager().registerEvents(new NPCClickListener(), this);
        }

        getCommand("kies").setExecutor(new KiesCMD());
        getCommand("minetopiafarms").setExecutor(new MTFarmsCMD());

        getConfig().addDefault("KostenVoorEenBaan", 2500);
        getConfig().addDefault("KrijgItemsBijBaanSelect", true);

        getConfig().addDefault("TerugverkoopPrijs.Mijnwerker.COAL_ORE", 10);
        getConfig().addDefault("TerugverkoopPrijs.Mijnwerker.IRON_ORE", 25);
        getConfig().addDefault("TerugverkoopPrijs.Mijnwerker.EMERALD_ORE", 30);
        getConfig().addDefault("TerugverkoopPrijs.Mijnwerker.GOLD_ORE", 25);
        getConfig().addDefault("TerugverkoopPrijs.Mijnwerker.LAPIS_ORE", 35);
        getConfig().addDefault("TerugverkoopPrijs.Mijnwerker.REDSTONE_ORE", 25);
        getConfig().addDefault("TerugverkoopPrijs.Mijnwerker.DIAMOND_ORE", 80);
        getConfig().addDefault("TerugverkoopPrijs.Boer.BEETROOTS", 35);
        getConfig().addDefault("TerugverkoopPrijs.Boer.WHEAT", 10);
        getConfig().addDefault("TerugverkoopPrijs.Boer.MELON", 30);
        getConfig().addDefault("TerugverkoopPrijs.Boer.PUMPKIN", 30);
        getConfig().addDefault("TerugverkoopPrijs.Boer.CARROTS", 20);
        getConfig().addDefault("TerugverkoopPrijs.Boer.POTATOES", 20);
        getConfig().addDefault("TerugverkoopPrijs.Houthakker", 25);
        getConfig().addDefault("TerugverkoopPrijs.Visser", 35);

        getConfig().addDefault("CommandsUitvoerenBijBaanWissel.Boer", Arrays.asList("Typ hier jouw commands"));
        getConfig().addDefault("CommandsUitvoerenBijBaanWissel.Houthakker", Arrays.asList("Typ hier jouw commands"));
        getConfig().addDefault("CommandsUitvoerenBijBaanWissel.Mijnwerker", Arrays.asList("Typ hier jouw commands"));
        getConfig().addDefault("CommandsUitvoerenBijBaanWissel.Visser", Arrays.asList("Typ hier jouw commands"));

        getConfig().addDefault("ItemsBijBaanSelect.Boer",Arrays.asList("DIAMOND_HOE"));
        getConfig().addDefault("ItemsBijBaanSelect.Mijnwerker",Arrays.asList("DIAMOND_PICKAXE"));
        getConfig().addDefault("ItemsBijBaanSelect.Houthakker",Arrays.asList("DIAMOND_AXE"));
        getConfig().addDefault("ItemsBijBaanSelect.Visser",Arrays.asList("FISHING_ROD"));

        getConfig().addDefault("MogelijkeItemsBijVangst", Arrays.asList("Typ hier welke materials de persoon krijgt."));
        getConfig().addDefault("VangstItemNaam", "&6Vangst");
        getConfig().addDefault("VangstItemLore", Arrays.asList("&3Jouw visvangst!"));
        getConfig().addDefault("Messages.VeranderenVanEenBaan",
                "&4Let op! &cHet veranderen van beroep kost &4€ <Bedrag>,-&c.");
        getConfig().addDefault("Messages.InventoryTitle", "&3Kies een &bberoep&3!");
        getConfig().addDefault("Messages.ItemName", "&3<Beroep>");
        getConfig().addDefault("Messages.ItemLore", "&3Kies het beroep &b<Beroep>");

        getConfig().addDefault("Messages.BeroepNodig", "&4ERROR: &cHiervoor heb je het beroep &4<Beroep> &cnodig!");
        getConfig().addDefault("Messages.ToolNodig", "&4ERROR: &cHiervoor heb je een &4<Tool> &cnodig!");
        getConfig().addDefault("Messages.TarweNietVolgroeid", "&4ERROR: &cDeze tarwe is niet volgroeid!");
        getConfig().addDefault("Messages.BietenNietVolgroeid", "&4ERROR: &cDeze bieten zijn niet volgroeid!");
        getConfig().addDefault("Messages.WortelNietVolgroeid", "&4ERROR: &cDeze wortel is niet volgroeid!");
        getConfig().addDefault("Messages.AardappelNietVolgroeid", "&4ERROR: &cDeze aardappel is niet volgroeid!");

        getConfig().addDefault("Messages.TeWeinigGeld", "&4ERROR: &cOm van baan te veranderen heb je &4€ <Bedrag> &cnodig!");

        getConfig().addDefault("Messages.GeldBetaald", "&3Gelukt! Wij hebben jou &b€ <Bedrag> &3betaald voor jouw opgehaalde spullen!");

        getConfig().addDefault("Messages.BaanVeranderd", "&3Jouw baan is succesvol veranderd naar &b<Baan>&3.");

        getConfig().addDefault("Messages.GeenRegion", "&4ERROR: &cDeze region moet de tag &4'<Tag>' &chebben.");
        getConfig().addDefault("Messages.Creative",
                "&3Omdat jij in &bCREATIVE &3zit heb jij een MinetopiaFarms bypass..");

        getConfig().addDefault("Messages.NPC.Name", "&6Verkooppunt");
        getConfig().addDefault("Messages.NPC.Skin.Name", "MrWouter");
        getConfig().addDefault("Messages.NPC.Skin.UUID", "836ce767-0e25-45a0-8012-fa1864d2b6aa");

        getConfig().addDefault("scheduler.cropgrow",120);
        getConfig().addDefault("scheduler.perstatetime",120);
        getConfig().addDefault("scheduler.miner.COAL_ORE",2400);
        getConfig().addDefault("scheduler.miner.DIAMOND_ORE",2400);
        getConfig().addDefault("scheduler.miner.EMERALD_ORE",2400);
        getConfig().addDefault("scheduler.miner.GOLD_ORE",2400);
        getConfig().addDefault("scheduler.miner.IRON_ORE",2400);
        getConfig().addDefault("scheduler.miner.LAPIS_ORE",2400);
        getConfig().addDefault("scheduler.miner.REDSTONE_ORE",2400);

        getConfig().options().copyDefaults(true);
        saveConfig();

        pl = this;

        CustomFlags.loadCustomFlag();

        final int cropgrowschedulertime = getConfig().getInt("scheduler.cropgrow");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            int growtime = getConfig().getInt("scheduler.perstatetime");
            for (Utils.GrowingCrop gcrop : new ArrayList<>(Utils.cropPlaces)) {
                BlockState state = gcrop.location.getBlock().getState();
                if (state.getData() instanceof Crops) {
                    gcrop.time += cropgrowschedulertime;
                    Crops crop = (Crops) state.getData();
                    if ((crop.getState().ordinal()+1)*growtime <= gcrop.time) {
                        crop.setState(CropState.values()[crop.getState().ordinal() + 1]);
                        if (crop.getState() == CropState.RIPE)
                            Utils.cropPlaces.remove(gcrop);
                        state.update();
                    }
                } else {
                    Utils.cropPlaces.remove(gcrop);
                }
            }
        }, cropgrowschedulertime, cropgrowschedulertime);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Location l : Utils.blockReplaces.keySet()) {
                l.getBlock().setType(Utils.blockReplaces.get(l));
            }
        }, 40 * 20l, 40 * 20l);

        Updat3r.getInstance().startTask();
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onJoin(PlayerJoinEvent e) {
                Updat3r.getInstance().sendUpdateMessageLater(e.getPlayer());
            }
        }, this);
    }

    public void onDisable() {
        for (Location l : Utils.ores.keySet()) {
            l.getBlock().setType(Utils.ores.get(l));
        }
        for (Utils.GrowingCrop gcrop : Utils.cropPlaces) {
            BlockState state = gcrop.location.getBlock().getState();
            if (state.getData() instanceof Crops) {
                Crops crop = (Crops) state.getData();
                crop.setState(CropState.RIPE);
                state.update();
            }
        }
        for (Location l : Utils.blockReplaces.keySet()) {
            l.getBlock().setType(Utils.blockReplaces.get(l));
        }
        for (Location l : Utils.treePlaces.keySet()) {
            TreeObj obj = Utils.treePlaces.get(l);
            l.getBlock().setType(obj.getMaterial());
            if (!Utils.is113orUp()) {
                try {
                    l.getBlock().getClass().getMethod("setData", byte.class).invoke(l.getBlock(), obj.getData());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static Main getPlugin() {
        return pl;
    }

    public static String getMessage(String path) {
        return Utils.color(pl.getConfig().getString("Messages." + path));
    }
}