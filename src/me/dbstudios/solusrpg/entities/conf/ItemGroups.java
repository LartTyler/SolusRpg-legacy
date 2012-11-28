
package me.dbstudios.solusrpg.entities.conf;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;
import me.dbstudios.solusrpg.util.Directories;
import me.dbstudios.solusrpg.util.Util;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author Tyler Lartonoix
 */
public class ItemGroups {
    private static final Map<String, List<Pattern>> groups;

    static {
        File f = new File(Directories.CONFIG + "item_groups.yml");

        if (f.exists()) {
            FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
            Map<String, List<Pattern>> groupList = new HashMap<>();

            for (String key : conf.getConfigurationSection("groups").getKeys(false)) {
                List<String> items = Util.toTypedList(conf.getList("groups." + key, null), String.class);
                List<Pattern> patterns = new ArrayList<>();

                for (String item : items)
                    patterns.add(Pattern.compile(item));

                groupList.put(key, Collections.unmodifiableList(patterns));
            }

            groups = Collections.unmodifiableMap(groupList);
        } else {
            groups = Collections.unmodifiableMap(new HashMap<String, List<Pattern>>());
        }
    }

    public static boolean groupExists(String group) {
        return groups.containsKey(group);
    }

    public static List<Pattern> getGroup(String group) {
        return groups.get(group);
    }
}
