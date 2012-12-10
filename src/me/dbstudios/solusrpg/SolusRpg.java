
package me.dbstudios.solusrpg;

<<<<<<< HEAD
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.dbstudios.solusrpg.util.Directories;
import me.dbstudios.solusrpg.util.Util;
=======
import java.util.logging.Level;
import java.util.logging.Logger;
import me.dbstudios.solusrpg.managers.ClassManager;
import me.dbstudios.solusrpg.managers.PhraseManager;
import me.dbstudios.solusrpg.util.Util;
import org.bukkit.Bukkit;
>>>>>>> Began adding basic command structure
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Tyler Lartonoix
 */
public class SolusRpg extends JavaPlugin {
    private static SolusRpg instance = null;
    private static final Logger logger = Logger.getLogger("Minecraft");

    public void onEnable() {
<<<<<<< HEAD
        SolusRpg.instance = this;

        File f = new File(Directories.CONFIG + "config.yml");

        if (!f.exists())
            Util.extract("/resources/config.yml", f);
=======
	long start = System.currentTimeMillis();

        SolusRpg.instance = this;

	SolusRpg.log(Level.INFO, "Loaded {0} class{1}.", ClassManager.size(), ClassManager.size() != 1 ? "es" : "");
	SolusRpg.log(Level.INFO, "Loaded {0} phrase{1}.", PhraseManager.size(), PhraseManager.size() != 1 ? "s" : "");
	SolusRpg.log(Level.INFO, "SolusRpg enabled in {0} milliseconds.", System.currentTimeMillis() - start);
>>>>>>> Began adding basic command structure
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
