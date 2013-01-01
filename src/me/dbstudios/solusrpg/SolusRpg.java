
package me.dbstudios.solusrpg;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.dbstudios.solusrpg.commands.RpgCommandExecutor;
import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.entities.conf.ItemGroups;
import me.dbstudios.solusrpg.event.listeners.EventDistributor;
import me.dbstudios.solusrpg.event.listeners.RpgStockListener;
import me.dbstudios.solusrpg.managers.ClassManager;
import me.dbstudios.solusrpg.managers.PhraseManager;
import me.dbstudios.solusrpg.managers.PlayerManager;
import me.dbstudios.solusrpg.managers.SpecializationManager;
import me.dbstudios.solusrpg.util.Directories;
import me.dbstudios.solusrpg.util.OutputFormatter;
import me.dbstudios.solusrpg.util.SimpleFormatter;
import me.dbstudios.solusrpg.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Tyler Lartonoix
 */
public class SolusRpg extends JavaPlugin {
    private static SolusRpg instance = null;
    private static final Logger logger = Logger.getLogger("Minecraft");

    public void onEnable() {
	long start = System.currentTimeMillis();

        SolusRpg.instance = this;

        File f = new File(Directories.CONFIG + "config.yml");

        if (!f.exists())
            Util.extract("/resources/config.yml", f);

        f = new File(Directories.CONFIG + "item_groups.yml");

        if (!f.exists())
            Util.extract("/resources/item_groups.yml", f);

        f = new File(Directories.CONFIG + "player_info_format.dat");

        if (!f.exists())
            Util.extract("/resources/player_info_format.dat", f);

        f = new File(Directories.CONFIG + "class_info_format.dat");

        if (!f.exists())
            Util.extract("/resources/class_info_format.dat", f);

        f = new File(Directories.CONFIG + "class_list_format.dat");

        if (!f.exists())
            Util.extract("/resources/class_list_format.dat", f);

        f = new File(Directories.CONFIG + "specializations.yml");

        if (!f.exists())
            Util.extract("/resources/specializations.yml", f);

        OutputFormatter.registerFormatter(new SimpleFormatter());

        Bukkit.getPluginManager().registerEvents(new EventDistributor(), this);
        Bukkit.getPluginManager().registerEvents(new RpgStockListener(), this);

        Bukkit.getPluginCommand("rpg").setExecutor(new RpgCommandExecutor());

	SolusRpg.log(Level.INFO, "Loaded {0} class{1}.", ClassManager.size(), ClassManager.size() != 1 ? "es" : "");
	SolusRpg.log(Level.INFO, "Loaded {0} phrase{1}.", PhraseManager.size(), PhraseManager.size() != 1 ? "s" : "");
        SolusRpg.log(Level.INFO, "Loaded {0} item group{1}.", ItemGroups.size(), ItemGroups.size() != 1 ? "s" : "");
        SolusRpg.log(Level.INFO, "Loaded {0} specialization tree{1}.", SpecializationManager.size(), SpecializationManager.size() != 1 ? "s" : "");
	SolusRpg.log(Level.INFO, "SolusRpg enabled in {0} milliseconds.", System.currentTimeMillis() - start);
    }

    public void onDisable() {
        if (PlayerManager.size() > 0) {
            SolusRpg.log(Level.WARNING, "{0} player{1} {2} still connected when the shutdown command was given, and will be saved before shut down can continue.", PlayerManager.size(), PlayerManager.size() != 1 ? "s" : "",
                    PlayerManager.size() != 1 ? "were" : "was");

            for (RpgPlayer p : PlayerManager.getOnlinePlayers())
                PlayerManager.remove(p.getBasePlayer());
        }

        SolusRpg.log(Level.INFO, "SolusRpg disabled.");
    }

    public static SolusRpg getInstance() {
        return SolusRpg.instance;
    }

    public static void log(Level level, String msg) {
        logger.log(level, "[SolusRpg] {0}", msg);
    }

    public static void log(Level level, String msg, Object... args) {
        SolusRpg.log(level, Util.format(msg, args));
    }
}
