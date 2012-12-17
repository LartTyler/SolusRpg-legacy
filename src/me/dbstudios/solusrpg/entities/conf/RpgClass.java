
package me.dbstudios.solusrpg.entities.conf;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;
import me.dbstudios.solusrpg.exceptions.RpgClassConfigException;
import me.dbstudios.solusrpg.util.Directories;
import me.dbstudios.solusrpg.util.RpgConstants;
import me.dbstudios.solusrpg.util.Util;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author Tyler Lartonoix
 */
public class RpgClass {
    private final Map<PermitNode, List<Pattern>> permitNodes;
    private final Map<StatType, Stat> stats;
    private final String systemName;
    private final String name;
    private final String bio;

    public RpgClass(String systemName) throws RpgClassConfigException {
        File f = new File(Directories.CLASSES + systemName + ".yml");

        if (!f.exists())
            throw new RpgClassConfigException("Could not find class configuration file.");

        this.systemName = systemName;

        FileConfiguration conf = YamlConfiguration.loadConfiguration(f);

        this.name = conf.getString("class.name", systemName);
        this.bio = conf.getString("class.description", "No description available.");

        Map<PermitNode, List<Pattern>> nodes = new EnumMap<>(PermitNode.class);

        for (PermitNode node : PermitNode.values()) {
            List<Pattern> patterns = new ArrayList<>();

            for (String item : Util.toTypedList(conf.getList("class." + node, null), String.class))
                if (item.charAt(0) == RpgConstants.ITEM_GROUP_IDENTIFIER && ItemGroups.groupExists(item.substring(1)))
                    patterns.addAll(ItemGroups.getGroup(item.substring(1)));
                else
                    patterns.add(Pattern.compile(item));

            nodes.put(node, patterns);
        }

        this.permitNodes = Collections.unmodifiableMap(nodes);

	Map<StatType, Stat> statMap = new EnumMap<>(StatType.class);

	for (StatType t : StatType.values())
	    statMap.put(t, new Stat(conf, t, "class.stats"));

	this.stats = Collections.unmodifiableMap(statMap);
    }

    public String getSystemName() {
        return this.systemName;
    }

    public String getName() {
        return this.name;
    }

    public String getBio() {
        return this.bio;
    }

    public boolean isAllowed(PermitNode node, String itemName) {
        for (Pattern p : this.permitNodes.get(node))
            if (p.matcher(itemName).find())
                return true;

        return false;
    }

    public Stat getStat(StatType type) {
	return this.stats.get(type);
    }
}
