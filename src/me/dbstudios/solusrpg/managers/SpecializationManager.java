
package me.dbstudios.solusrpg.managers;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.player.specialization.RpgSpecialization;
import me.dbstudios.solusrpg.player.specialization.Specialization;
import me.dbstudios.solusrpg.util.Directories;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author Tyler Lartonoix
 */
public class SpecializationManager {
    private static final Map<String, Specialization> specTrees;

    static {
        Map<String, Specialization> trees = new HashMap<>();
        File f = new File(Directories.CONFIG + "specializations.yml");

        if (f.exists()) {
            FileConfiguration conf = YamlConfiguration.loadConfiguration(f);

            for (String key : conf.getKeys(false))
                trees.put(key, new RpgSpecialization(conf.getConfigurationSection(key)));
        }

        for (String key : trees.keySet()) {
            SolusRpg.log(Level.INFO, key);

            printSpecTree(trees.get(key).getSubSpecialization(), 1);
        }

        specTrees = Collections.unmodifiableMap(trees);
    }

    public static Specialization getRootSpecialization(String key) {
        return specTrees.get(key);
    }

    public static boolean hasRootSpecialization(String key) {
        return specTrees.containsKey(key);
    }

    public static int size() {
        return specTrees.size();
    }

    public static Set<String> getSpecializations() {
        return specTrees.keySet();
    }

    public static Collection<Specialization> getSpecializationCollection() {
        return specTrees.values();
    }

    private static void printSpecTree(List<Specialization> specs, int depth) {
        for (Specialization s : specs) {
            String out = "";

            for (int i = 0; i < depth; i++)
                out += "\t";

            SolusRpg.log(Level.INFO, out + s.getUniqueName());

            if (s.hasSubSpecialization())
                printSpecTree(s.getSubSpecialization(), depth + 1);
        }
    }
}
