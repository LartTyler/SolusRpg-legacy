
package me.dbstudios.solusrpg;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.dbstudios.solusrpg.util.Util;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Tyler Lartonoix
 */
public class SolusRpg extends JavaPlugin {
    private static SolusRpg instance = null;
    private static final Logger logger = Logger.getLogger("Minecraft");

    public void onEnable() {
        SolusRpg.instance = this;
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
