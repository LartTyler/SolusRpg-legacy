
package me.dbstudios.solusrpg.managers;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Pattern;
import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.entities.conf.RpgClass;
import me.dbstudios.solusrpg.exceptions.RpgClassConfigException;
import me.dbstudios.solusrpg.util.Directories;
import me.dbstudios.solusrpg.util.Util;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author Tyler Lartonoix
 */
public class ClassManager {
    private static final Map<String, RpgClass> classes;

    public static final RpgClass DEFAULT_CLASS;

    static {
        File f = new File(Directories.CONFIG + "config.yml");

        if (f.exists()) {
            FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
            List<String> classList = Util.toTypedList(conf.getList("config.classes", null), String.class);
            Map<String, RpgClass> cls = new HashMap<>();

            for (String cl : classList) {
                try {
                    RpgClass rpgClass = new RpgClass(cl);

                    cls.put(cl, rpgClass);
                } catch (RpgClassConfigException e) {
                    SolusRpg.log(Level.WARNING, "Could not load class '{0}'. Reason: {1}", cl, e.getMessage());
                }
            }

            classes = Collections.unmodifiableMap(cls);
	    DEFAULT_CLASS = classes.get(conf.getString("config.default-class"));
        } else {
            classes = Collections.unmodifiableMap(new HashMap<String, RpgClass>());
	    DEFAULT_CLASS = null;
        }
    }

    public static List<RpgClass> matchClass(String search) {
        List<RpgClass> matches = new ArrayList<>();

        for (RpgClass cl : classes.values())
            if (Pattern.compile("(?i)^" + search).matcher(cl.getName()).find() || Pattern.compile("(?i)^" + search).matcher(cl.getSystemName()).find())
                matches.add(cl);

        return matches;
    }

    public static boolean exists(String cl) {
        return classes.containsKey(cl);
    }

    public static RpgClass getClass(String cl) {
        return classes.get(cl);
    }

    public static Collection<RpgClass> getClasses() {
        return classes.values();
    }

    public static int size() {
	return classes.size();
    }
}
