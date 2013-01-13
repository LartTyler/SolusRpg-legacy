
package me.dbstudios.solusrpg.managers;

import bsh.EvalError;
import bsh.Interpreter;
import java.io.File;
import java.util.logging.Level;
import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.util.Directories;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author Tyler Lartonoix
 */
public class LevelManager {
    private static final int levelCap;
    private static final String expAlgorithm;
    private static final int skillPointsPerLevel;
    private static final int startingLevel;
    private static final int startingSkillPoints;

    static {
        File f = new File(Directories.CONFIG + "config.yml");

        if (f.exists()) {
            FileConfiguration conf = YamlConfiguration.loadConfiguration(f);

            levelCap = conf.getInt("config.leveling.level-cap", 20);
            expAlgorithm = conf.getString("config.leveling.experience-algorithm", "50 * ({level} - 1)");
            skillPointsPerLevel = conf.getInt("config.leveling.skill-points", 1);
            startingLevel = conf.getInt("config.leveling.starting-level", 1);
            startingSkillPoints = conf.getInt("config.leveling.starting-skill-points", 2);
        } else {
            levelCap = 20;
            expAlgorithm = "50 * ({level} - 1)";
            skillPointsPerLevel = 1;
            startingLevel = 1;
            startingSkillPoints = 2;
        }
    }

    public static int getLevelCap() {
        return levelCap;
    }

    public static String getExpAlgorithm() {
        return expAlgorithm;
    }

    public static int getSkillPointsPerLevel() {
        return skillPointsPerLevel;
    }

    public static int getExpToLevel(int level) {
        Interpreter i = new Interpreter();
        Object rawToLevel = null;

        try {
            rawToLevel = i.eval(expAlgorithm.replaceAll("(?i)\\{level\\}", level + ""));
        } catch (EvalError e) {
            SolusRpg.log(Level.INFO, "Could not evaluate experience algorithm. Please check that your syntax is correct.");
        }

        if (rawToLevel == null)
            try {
                rawToLevel = i.eval("50 * (" + level + " - 1)");
            } catch (EvalError e) {}

        return (Integer)rawToLevel;
    }

    public static int getStartingLevel() {
        return startingLevel;
    }

    public static int getStartingSkillPoints() {
        return startingSkillPoints;
    }
}
