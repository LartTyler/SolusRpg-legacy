
package me.dbstudios.solusrpg.player.specialization;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.entities.conf.ItemGroups;
import me.dbstudios.solusrpg.entities.conf.PermitNode;
import me.dbstudios.solusrpg.entities.conf.Stat;
import me.dbstudios.solusrpg.entities.conf.StatType;
import me.dbstudios.solusrpg.exceptions.IncompatibleStatTypeException;
import me.dbstudios.solusrpg.util.Directories;
import me.dbstudios.solusrpg.util.RpgConstants;
import me.dbstudios.solusrpg.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Tyler Lartonoix
 */
public class RpgSpecialization implements Specialization {
    private final List<Map<String, Integer>> requires;
    private final List<Integer> levelReqs;
    private final List<Map<StatType, Stat>> statEffects;
    private final List<Map<PermitNode, List<String>>> permitEffects;
    private final List<Specialization> subSpecs;
    private final Specialization preSpec;
    private final String uniqueName;
    private final String name;
    private final String icoPath;
    private final String desc;

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
        List<Map<PermitNode, List<String>>> permitList = new ArrayList<>();

        if (list != null)
            for (Map<?, ?> map : list) {
                if (map.containsKey("stat") && map.get("stat") instanceof Map) {
                    Map<String, Integer> statMap = Util.toTypedMap((Map)map.get("stat"), String.class, Integer.class);
                    Map<StatType, Stat> stats = new EnumMap<>(StatType.class);

                    for (StatType t : StatType.values())
                        if (statMap.containsKey(t.toString()))
                            stats.put(t, new Stat(statMap.get(t.toString()), t));

                    statsList.add(stats);
                } else {
                    statsList.add(null);
                }

                if (map.containsKey("permit") && map.get("permit") instanceof Map) {
                    Map<?, ?> permitMap = (Map)map.get("permit");
                    Map<PermitNode, List<String>> nodes = new EnumMap<>(PermitNode.class);

                    for (PermitNode n : PermitNode.values())
                        if (permitMap.containsKey(n.getNode()) && permitMap.get(n.getNode()) instanceof List) {
                            List<String> patterns = new ArrayList<>();

                            for (Object pat : (List)permitMap.get(n.getNode()))
                                if (pat instanceof String)
                                    patterns.add((String)pat);

                            nodes.put(n, patterns);
                        }

                    permitList.add(nodes);
                } else {
                    permitList.add(null);
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
                    levels.add(0);
            }

        this.requires = Collections.unmodifiableList(reqList);
        this.levelReqs = Collections.unmodifiableList(levels);

        if (conf.isString("unique-name"))
            this.uniqueName = conf.getString("unique-name");
        else
            this.uniqueName = conf.getCurrentPath();

        this.icoPath = conf.getString("icon-path", null);
        this.desc = conf.getString("description", "No description available.");
        this.name = conf.getString("name", this.uniqueName);

        conf.set("effects", null);
        conf.set("unique-name", null);
        conf.set("icon-path", null);
        conf.set("requires", null);
        conf.set("description", null);
        conf.set("name", null);

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
        level -= 1;

        if (level >= 0 && level < this.getMaxLevel()) {
            Map<StatType, Stat> stats = statEffects.get(level);

            if (statEffects.get(level) != null)
                for (StatType key : stats.keySet())
                    try {
                        player.setStat(key, player.getPlayerStat(key).merge(stats.get(key)));
                    } catch (IncompatibleStatTypeException e) {}

            if (permitEffects.get(level) != null)
                for (PermitNode n : permitEffects.get(level).keySet()) {
                    List<Pattern> patterns = new ArrayList<>();

                    for (String s : permitEffects.get(level).get(n))
                        if (s.charAt(0) == RpgConstants.ITEM_GROUP_IDENTIFIER && ItemGroups.groupExists(s.substring(1).toLowerCase()))
                            patterns.addAll(ItemGroups.getGroup(s.substring(1).toLowerCase()));
                        else
                            patterns.add(Pattern.compile(s));

                    player.addAllowed(n, patterns);
                }

            player.putMetadata(uniqueName + ".level", level + 1);

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

            if (stats != null)
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
        level -= 1;

        if (level >= 0 && level < requires.size()) {
            Map<String, Integer> preSpecs = requires.get(level);
            boolean hasSpecs = true;

            if (preSpecs != null)
                for (String spec : preSpecs.keySet())
                    if (player.getMetadataAs(spec + ".level", Integer.class) != null && player.getMetadataAs(spec + ".level", Integer.class) < preSpecs.get(spec))
                        hasSpecs = false;

            return hasSpecs && levelReqs.get(level) <= player.getLevel();
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

        return level != null ? level : 0;
    }

    /**
     * Gets the max possible level of this specialization.
     *
     * @return
     */
    public int getMaxLevel() {
        return Math.max(statEffects.size(), permitEffects.size());
    }

    public String getDescription() {
        return this.desc;
    }

    public String getName() {
        return this.name;
    }

    public String getTooltip(int level) {
        String tooltip = "";

        level -= 1;

        try {
            Scanner s = new Scanner(new File(Directories.CONFIG + "spec_tooltip_format.dat"));
            Map<String, String> args = new HashMap<>();

            args.put("name", this.name);
            args.put("description", this.desc);

            String repl;

            if (level < 0) {
                repl = "None";
            } else {
                Map<StatType, Stat> stats = statEffects.get(level);

                repl = "Stat Changes:";

                if (stats != null)
                    for (StatType t : stats.keySet())
                        repl += "\n{pre}  " + t + ": " + (stats.get(t).getValue() >= 0 ? "+" : "") + stats.get(t).getValue() + "{post}";
                else
                    repl += "\n{pre}  None{post}";

                Map<PermitNode, List<String>> permits = permitEffects.get(level);

                repl += "\n{pre}Permit Changes:{post}";

                if (permits != null)
                    for (PermitNode n : permits.keySet()) {
                        String list = "";

                        for (String el : permits.get(n)) {
                            String value = "";

                            for (String seg : el.charAt(0) == '@' ? el.substring(1).split("_") : el.split("_"))
                                value += " " + seg.substring(0, 1).toUpperCase() + seg.substring(1).toLowerCase();

                            list += ", " + value.substring(1);
                        }

                        repl += "\n{pre}  " + n.getText() + ": " + (list.length() == 0 ? "None" : list.substring(2));
                    }
                else
                    repl += "\n{pre}  None{post}";
            }

            args.put("effects", repl);

            if (level + 1 >= this.getMaxLevel()) {
                repl = "Max level";
            } else {
                Map<StatType, Stat> stats = statEffects.get(level + 1);

                repl = "Stat Changes:";

                if (stats != null)
                    for (StatType t : stats.keySet())
                        repl += "\n{pre}  " + t + ": " + (stats.get(t).getValue() >= 0 ? "+" : "") + stats.get(t).getValue() + "{post}";
                else
                    repl += "\n{pre}  None{post}";

                Map<PermitNode, List<String>> permits = permitEffects.get(level + 1);

                repl += "\n{pre}Permit Changes:{post}";

                if (permits != null)
                    for (PermitNode n : permits.keySet()) {
                        String list = "";

                        for (String el : permits.get(n)) {
                            String value = "";

                            for (String seg : el.charAt(0) == '@' ? el.substring(1).split("_") : el.split("_"))
                                value += " " + seg.substring(0, 1).toUpperCase() + seg.substring(1).toLowerCase();

                            list += ", " + value.substring(1);
                        }

                        repl += "\n{pre}  " + n.getText() + ": " + list.substring(2);
                    }
                else
                    repl += "\n{pre}  None{post}";
            }

            args.put("next-effects", repl);

            while (s.hasNextLine()) {
                String line = s.nextLine();

                for (String key : args.keySet()) {
                    if (line.toLowerCase().contains("{effects}") || line.toLowerCase().contains("{next-effects}")) {
                        String[] broken = line.split("(?i)\\{(next-)?effects\\}");

                        args.put(key, args.get(key).replaceAll("(?i)\\{pre\\}", broken[0]).replaceAll("(?i)\\{post\\}", broken.length > 1 ? broken[1] : ""));
                    }

                    line = line.replaceAll("(?i)\\{" + key + "\\}", args.get(key));
                }

                for (ChatColor c : ChatColor.values())
                    line = line.replaceAll("(?i)\\{" + c.name() + "\\}", c.toString());

                tooltip += (tooltip.length() > 0 ? "\n" : "") + line;
            }
        } catch (IOException e) {}

        return tooltip;
    }
}
