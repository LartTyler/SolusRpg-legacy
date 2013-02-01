
package me.dbstudios.solusrpg.managers;

import java.io.File;
import java.util.*;
import me.dbstudios.solusrpg.entities.RpgPlayer;
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

    public static void applyOwnedSpecializations(RpgPlayer player) {
        for (Specialization root : specTrees.values()) {
            List<Specialization> subs = new ArrayList<>();

            subs.add(root);

            while (subs != null && !subs.isEmpty()) {
                List<Specialization> newSubs = new ArrayList<>();

                for (Specialization s : subs) {
                    if (player.hasMetadata(s.getUniqueName() + ".level")) {
                        Integer level = player.getMetadataAs(s.getUniqueName(), Integer.class);

                        if (level != null)
                            for (int i = 1; i <= level; i++)
                                s.applyEffect(player, i);
                    }

                    if (s.hasSubSpecialization())
                        newSubs.addAll(s.getSubSpecialization());
                }

                if (newSubs.isEmpty())
                    subs.clear();
                else
                    subs = newSubs;
            }
        }
    }
}
