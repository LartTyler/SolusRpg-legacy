
package me.dbstudios.solusrpg.player.specialization;

import java.util.*;
import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.entities.conf.Stat;
import me.dbstudios.solusrpg.entities.conf.StatType;
import me.dbstudios.solusrpg.exceptions.IncompatibleStatTypeException;
import me.dbstudios.solusrpg.util.Util;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Tyler Lartonoix
 */
public class RpgSpecialization implements Specialization {
    private final List<Map<StatType, Stat>> statEffects;
    private final List<Specialization> subSpecs;
    private final Specialization preSpec;
    private final String uniqueName;
    private final String icoPath;

    public RpgSpecialization(ConfigurationSection section) {
        this(section, null);
    }

    public RpgSpecialization(ConfigurationSection conf, Specialization preSpec) {
        this.preSpec = preSpec;

        List<ConfigurationSection> effectsList = Util.toTypedList(conf.getList("effects"), ConfigurationSection.class);
        List<Map<StatType, Stat>> effects = new ArrayList<>();

        if (!effectsList.isEmpty())
            for (ConfigurationSection s : effectsList) {
                Map<StatType, Stat> statMap = new EnumMap<>(StatType.class);

                for (StatType t : StatType.values())
                    if (s.getInt("stat." + t, 0) != 0)
                        statMap.put(t, new Stat(s.getInt("stat." + t), t));

                effects.add(Collections.unmodifiableMap(statMap));
            }

        this.statEffects = Collections.unmodifiableList(effects);

        if (conf.isString("unique-name"))
            this.uniqueName = conf.getString("unique-name");
        else
            this.uniqueName = conf.getCurrentPath();

        this.icoPath = conf.getString("icon-path", null);

        conf.set("effects", null);
        conf.set("unique-name", null);
        conf.set("icon-path", null);

        List<Specialization> subs = new ArrayList<>();

        if (conf.getKeys(false).size() > 0)
            for (String key : conf.getKeys(false))
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
}
