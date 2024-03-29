
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
    private final List<Integer> levelReqs;
    private final List<Map<StatType, Stat>> statEffects;
    private final List<Map<PermitNode, List<Pattern>>> permitEffects;
    private final List<Specialization> subSpecs;
    private final Specialization preSpec;
    private final String uniqueName;
    private final String icoPath;

    /**
     * Used to construct a new Specialization tree. The specialization present at the given <code>section</code> will be used as the root node for this tree.
     *
     * This constructor is the same as calling <code>new RpgSpecialization(section, null)</code>
     *
     * @param section   - The <code>ConfigurationSection</code> that contains the root Specialization for the new Spec. tree
     */
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
        List<Integer> levels = new ArrayList<>();
        list = conf.getMapList("requires");

        if (list != null)
            for (Map<?, ?> map : list) {
                if (map.containsKey("specialization") && map.get("specialization") instanceof Map)
                    reqList.add(Util.toTypedMap((Map)map.get("specialization"), String.class, Integer.class));
                else
                    reqList.add(null);

                if (map.containsKey("level") && map.get("level") instanceof Integer)
                    levels.add((Integer)map.get("level"));
                else
                    levels.add(null);
            }

        this.requires = Collections.unmodifiableList(reqList);
        this.levelReqs = Collections.unmodifiableList(levels);

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

    /**
     * Applies all specialization effects of the given level to the player.
     *
     * This method will ALWAYS apply it's effects, even if the player does not meet that specialization levels prerequisites.
     *
     * @param player        - The player to apply the specialization to
     * @param level         - The specialization level to be applied
     * @return              - True if the specialization was applied successfully
     */
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

    /**
     * Removes all specialization effects of the given level from the player.
     *
     * This method ALWAYS removes it's effects, even if the player does not have the given specialization level.
     *
     * In general, it is good practice to call this method before attempting to apply next level specialization effects.
     *
     * @param player        - The player to remove effects from
     * @param level         - The level of the effects to remove
     * @return              - True if the operation completed successfully
     */
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

    /**
     * Gets the string URI of the image that should be used to represent this specialization on the spec tree.
     *
     * @return      The String URI of this specializations image
     */
    public String getIconPath() {
        return this.icoPath;
    }

    /**
     * Determines if this specialization has any sub-specializations
     *
     * @return
     */
    public boolean hasSubSpecialization() {
        return !subSpecs.isEmpty();
    }

    /**
     * Determines if this specialization is the root specialization of it's tree.
     *
     * @return
     */
    public boolean isRootSpecialization() {
        return this.preSpec == null;
    }

    /**
     * Gets a list of all sub-specializations of this specialization.
     *
     * @return      A <code>List</code> of all sub-specializations, or an empty list if none are found
     */
    public List<Specialization> getSubSpecialization() {
        return Collections.unmodifiableList(this.subSpecs);
    }

    /**
     * Gets the specialization that is the parent of this specialization.
     *
     * @return      The parent specialization, or null if this is the root specialization of it's tree
     */
    public Specialization getPreSpecialization() {
        return this.preSpec;
    }

    /**
     * Gets the unique name of this specialization.
     *
     * The unique name is generally used for specialization metadata. It is constructed from the path used to reach it's configuration section.
     *
     * @return
     */
    public String getUniqueName() {
        return this.uniqueName;
    }

    /**
     * Checks to see if the given player has the prerequisites necessary to obtain this specialization at <code>level</code>.
     *
     * @param player        - The player to check
     * @param level         - The level to check
     * @return              - True if the player has all prerequisites
     */
    public boolean hasRequiredSpecializations(RpgPlayer player, int level) {
        if (level >= 0 && level < requires.size()) {
            Map<String, Integer> preSpecs = requires.get(level);

            if (preSpecs == null)
                return true;

            boolean hasSpecs = true;

            for (String spec : preSpecs.keySet())
                if (player.getMetadataAs(spec, Integer.class) < preSpecs.get(spec))
                    hasSpecs = false;

            Integer levelReq = null;

            if (level >= 0 && level < levelReqs.size())
                levelReq = levelReqs.get(level);

            return hasSpecs && (levelReq == null || levelReq <= player.getLevel());
        }

        return false;
    }

    /**
     * Gets a <code>Map</code> containing specialization names and levels that are required to obtain this specialization at the given level.
     *
     * @param level     - The desired specialization level
     * @return          - A <code>Map</code> containing all required pre-specializations
     */
    public Map<String, Integer> getRequiredSpecializations(int level) {
        if (level >= 0 && level < requires.size())
            return requires.get(level);

        return null;
    }

    /**
     * Gets the level required to obtain this specialization at <code>level</code>.
     *
     * @param level     - The desired level
     * @return          - The player level required, or null if one is not required
     */
    public Integer getRequiredLevel(int level) {
        return level >= 0 && level < levelReqs.size() ? levelReqs.get(level) : null;
    }

    /**
     * Gets the player's current specialization level.
     *
     * @param player    - The player to query
     * @return          - 0 if they player has not learned this specialization, or the player's current spec. level
     */
    public int getLevel(RpgPlayer player) {
        Integer level = player.getMetadataAs(uniqueName + ".level", Integer.class);

        return level != null ? level + 1 : 0;
    }

    /**
     * Gets the max possible level of this specialization.
     *
     * @return
     */
    public int getMaxLevel() {
        return Math.max(statEffects.size(), permitEffects.size());
    }
}
