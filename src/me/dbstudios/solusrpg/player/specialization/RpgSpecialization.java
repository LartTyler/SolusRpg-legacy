
package me.dbstudios.solusrpg.player.specialization;

import java.util.*;
import java.util.regex.Pattern;
import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.entities.conf.PermitNode;
import me.dbstudios.solusrpg.entities.conf.Stat;
import me.dbstudios.solusrpg.entities.conf.StatType;
import me.dbstudios.solusrpg.exceptions.IncompatibleStatTypeException;
import me.dbstudios.solusrpg.util.Util;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Tyler Lartonoix
 */
public class RpgSpecialization implements Specialization {
    private final List<Map<String, Integer>> requires;
    private final List<Map<StatType, Stat>> statEffects;
    private final List<Map<PermitNode, List<Pattern>>> permitEffects;
    private final List<Specialization> subSpecs;
    private final Specialization preSpec;
    private final String uniqueName;
    private final String icoPath;

    public RpgSpecialization(ConfigurationSection section) {
        this(section, null);
    }

    public RpgSpecialization(ConfigurationSection conf, Specialization preSpec) {
        this.preSpec = preSpec;

        List<Map<?, ?>> list = conf.getMapList("effects");
        List<Map<StatType, Stat>> statsList = new ArrayList<>();
        List<Map<PermitNode, List<Pattern>>> permitList = new ArrayList<>();

        if (list != null)
            for (Map<?, ?> map : list) {
                if (map.containsKey("stat") && map.get("stat") instanceof Map) {
                    Map<String, Integer> statMap = Util.toTypedMap((Map)map.get("stat"), String.class, Integer.class);
                    Map<StatType, Stat> stats = new EnumMap<>(StatType.class);

                    for (StatType t : StatType.values())
                        if (statMap.containsKey(t.toString()))
                            stats.put(t, new Stat(statMap.get(t.toString()), t));

                    statsList.add(stats);
                }

                if (map.containsKey("permit") && map.get("permit") instanceof Map) {
                    Map<?, ?> permitMap = (Map)map.get("permit");
                    Map<PermitNode, List<Pattern>> nodes = new EnumMap<>(PermitNode.class);

                    for (PermitNode n : PermitNode.values())
                        if (permitMap.containsKey(n.toString()) && permitMap.get(n.toString()) instanceof List) {
                            List<Pattern> patterns = new ArrayList<>();

                            patterns.addAll((List)permitMap.get(n.toString()));
                            nodes.put(n, patterns);
                        }

                    permitList.add(nodes);
                }
            }

        this.statEffects = Collections.unmodifiableList(statsList);
        this.permitEffects = Collections.unmodifiableList(permitList);

        List<Map<String, Integer>> reqList = new ArrayList<>();
        list = conf.getMapList("requires");

        if (list != null)
            for (Map<?, ?> map : list)
                if (map.containsKey("specialization") && map.get("specialization") instanceof Map)
                    reqList.add(Util.toTypedMap((Map)map.get("specialization"), String.class, Integer.class));

        this.requires = Collections.unmodifiableList(reqList);

        if (conf.isString("unique-name"))
            this.uniqueName = conf.getString("unique-name");
        else
            this.uniqueName = conf.getCurrentPath();

        this.icoPath = conf.getString("icon-path", null);

        conf.set("effects", null);
        conf.set("unique-name", null);
        conf.set("icon-path", null);
        conf.set("requires", null);

        List<Specialization> subs = new ArrayList<>();

        if (conf.getKeys(false).size() > 0)
            for (String key : conf.getKeys(false))
                if (conf.isConfigurationSection(key))
                    subs.add(new RpgSpecialization(conf.getConfigurationSection(key), this));

        this.subSpecs = Collections.unmodifiableList(subs);
    }

    public boolean applyEffect(RpgPlayer player, int level) {
        if (level >= 0 && level < statEffects.size()) {
            Map<StatType, Stat> stats = statEffects.get(level);

            for (StatType key : stats.keySet())
                try {
                    player.setStat(key, player.getStat(key).merge(stats.get(key)));
                } catch (IncompatibleStatTypeException e) {}

            for (PermitNode n : permitEffects.get(level).keySet())
                player.addAllowed(n, permitEffects.get(level).get(n));

            player.putMetadata(uniqueName + ".level", level);

            return true;
        }

        return false;
    }

    public boolean removeEffect(RpgPlayer player, int level) {
        if (player.getMetadataAs(uniqueName + ".level", Integer.class) == level) {
            Map<StatType, Stat> stats = statEffects.get(level);

            for (StatType key : stats.keySet())
                try {
                    player.setStat(key, player.getStat(key).merge(new Stat(-stats.get(key).getValue(), key)));
                } catch (IncompatibleStatTypeException e) {}

            // Permit effects are only applied and never removed

            player.removeMetadata(uniqueName + ".level");

            return true;
        }

        return false;
    }

    public String getIconPath() {
        return this.icoPath;
    }

    public boolean hasSubSpecialization() {
        return !subSpecs.isEmpty();
    }

    public boolean isRootSpecialization() {
        return this.preSpec == null;
    }

    public List<Specialization> getSubSpecialization() {
        return Collections.unmodifiableList(this.subSpecs);
    }

    public Specialization getPreSpecialization() {
        return this.preSpec;
    }

    public String getUniqueName() {
        return this.uniqueName;
    }

    public boolean hasRequiredSpecializations(RpgPlayer player, int level) {
        if (level >= 0 && level < requires.size()) {
            Map<String, Integer> preSpecs = requires.get(level);

            if (preSpecs == null)
                return true;

            boolean hasSpecs = true;

            for (String spec : preSpecs.keySet())
                if (player.getMetadataAs(spec, Integer.class) < preSpecs.get(spec))
                    hasSpecs = false;

            return hasSpecs;
        }

        return false;
    }

    public Map<String, Integer> getRequiredSpecializations(int level) {
        if (level >= 0 && level < requires.size())
            return requires.get(level);

        return null;
    }

    public int getLevel(RpgPlayer player) {
        Integer level = player.getMetadataAs(uniqueName + ".level", Integer.class);

        return level != null ? level + 1 : 0;
    }

    public int getMaxLevel() {
        return statEffects.size();
    }
}
