
package me.dbstudios.solusrpg.entities;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.entities.conf.*;
import me.dbstudios.solusrpg.exceptions.IncompatibleStatTypeException;
import me.dbstudios.solusrpg.exceptions.RpgPlayerConfigException;
import me.dbstudios.solusrpg.managers.ClassManager;
import me.dbstudios.solusrpg.managers.LevelManager;
import me.dbstudios.solusrpg.util.Directories;
import me.dbstudios.solusrpg.util.Metadatable;
import me.dbstudios.solusrpg.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

/**
 * @author Tyler Lartonoix
 */
public class RpgPlayer implements Metadatable<String, Object> {
    private final SpoutPlayer basePlayer;
    private final Map<StatType, Stat> stats;
    private final RpgHealthMeter health;
    private final Map<String, Object> metadata = new HashMap<>();

    private int level = 0;
    private int exp = 0;
    private int skillPoints = -1;

    private RpgClass rpgClass;

    public RpgPlayer(Player p) throws RpgPlayerConfigException {
        this.basePlayer = SpoutManager.getPlayer(p);

	File f = new File(Directories.DATA + p.getName().substring(0, 2).toLowerCase() + File.separator + p.getName().toLowerCase() + ".yml");

	if (!f.exists()) {
	    (new File(Directories.DATA + p.getName().substring(0, 2).toLowerCase() + File.separator)).mkdirs();

	    try {
		f.createNewFile();
	    } catch (IOException e) {
		throw new RpgPlayerConfigException("Could not create player data file.");
	    }

            this.skillPoints = LevelManager.getSkillPointsPerLevel();
	}

	FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
	String cl = conf.getString("player.class", null);

	if (ClassManager.exists(cl))
	    this.rpgClass = ClassManager.getClass(cl);
	else
	    this.rpgClass = ClassManager.DEFAULT_CLASS;

	Map<StatType, Stat> statMap = new EnumMap<>(StatType.class);

	for (StatType t : StatType.values())
	    statMap.put(t, new Stat(conf, t, "player.stats"));

	this.stats = statMap;
        this.health = new RpgHealthMeter(this, rpgClass.getConfiguration());
        this.health.setValue(conf.getInt("player.health", health.getMaxValue()));

        ConfigurationSection s = conf.getConfigurationSection("player.metadata");

        if (s != null)
            for (String k : s.getKeys(false))
                metadata.put(k.replace('_', '.'), s.getString(k));

        this.level = conf.getInt("player.level", 0);
        this.exp = conf.getInt("player.experience", 0);

        if (this.skillPoints == -1)
            this.skillPoints = conf.getInt("player.skill-points", 0);
    }

    public String getName() {
	return basePlayer.getName();
    }

    public String getDisplayName() {
	return basePlayer.getDisplayName();
    }

    public RpgPlayer setDisplayName(String name) {
	basePlayer.setDisplayName(name);

	return this;
    }

    public RpgPlayer setTitle(String name) {
	basePlayer.setTitle(name);

	return this;
    }
    // Alyssa was here
    public RpgPlayer setPlayerListName(String name) {
	basePlayer.setPlayerListName(name);

	return this;
    }

    public RpgPlayer sendMessage(String msg) {
	for (ChatColor c : ChatColor.values())
	    msg = msg.replaceAll("(?i)\\{" + c.name() + "\\}", c.toString());

	msg = msg.replaceAll("(?i)\\{player-name\\}", this.getName());
	msg = msg.replaceAll("(?i)\\{display-name\\}", this.getDisplayName());
	msg = msg.replaceAll("(?i)\\{class\\}", rpgClass.getName());

	basePlayer.sendMessage(msg);

	return this;
    }

    public RpgPlayer sendMessage(String msg, Object... args) {
	return this.sendMessage(Util.format(msg, args));
    }

    public RpgPlayer sendEventMessage(String msg, Map<String, String> args) {
	for (String key : args.keySet())
	    msg = msg.replaceAll("(?i)\\{" + key + "\\}", args.get(key));

	return this.sendMessage(msg);
    }

    public SpoutPlayer getBasePlayer() {
	return this.basePlayer;
    }

    public RpgClass getRpgClass() {
	return this.rpgClass;
    }

    public RpgPlayer setRpgClass(RpgClass cl) {
	this.rpgClass = cl;

	return this;
    }

    public boolean isAllowed(PermitNode node, String item) {
	return rpgClass.isAllowed(node, item);
    }

    public Stat getStat(StatType type) {
	try {
	    return this.stats.get(type).merge(this.rpgClass.getStat(type));
	} catch (IncompatibleStatTypeException e) {
	    e.printStackTrace();
	}

	return null;
    }

    public RpgPlayer setStat(StatType type, Stat stat) {
        this.stats.put(type, stat);

        return this;
    }

    public RpgPlayer damage(int amount) {
        health.damage(amount);

        return this;
    }

    public RpgPlayer heal(int amount) {
        health.heal(amount);

        return this;
    }

    public RpgPlayer setHealth(int value) {
        health.setValue(value);

        return this;
    }

    public int getHealth() {
        return health.getValue();
    }

    public int getMaxHealth() {
        return health.getMaxValue();
    }

    public RpgHealthMeter getHealthMeter() {
        return this.health;
    }

    public void save() {
        File f = new File(Directories.DATA + basePlayer.getName().substring(0, 2).toLowerCase() + File.separator + basePlayer.getName().toLowerCase() + ".yml");

	if (!f.exists()) {
	    (new File(Directories.DATA + basePlayer.getName().substring(0, 2).toLowerCase() + File.separator)).mkdirs();

	    try {
		f.createNewFile();
	    } catch (IOException e) {
		SolusRpg.log(Level.SEVERE, "Could not save player data for {0}.", this.getName());
	    }
	}

	FileConfiguration conf = YamlConfiguration.loadConfiguration(f);

        conf.set("player.class", rpgClass.getSystemName());
        conf.set("player.health", health.getValue());

        for (StatType t : StatType.values())
            conf.set("player.stats." + t, stats.get(t).getValue());

        for (String key : metadata.keySet())
            conf.set("player.metadata." + key.replace('.', '_'), metadata.get(key));

        conf.set("player.level", this.level);
        conf.set("player.experience", this.exp);
        conf.set("player.skill-points", this.skillPoints);

        try {
            conf.save(f);
        } catch (IOException e) {
            SolusRpg.log(Level.SEVERE, "Could not save player data for {0}.", this.getName());
        }
    }

    public void putMetadata(String key, Object value) {
        metadata.put(key, value);
    }

    public Object getMetadata(String key) {
        return metadata.get(key);
    }

    public <T> T getMetadataAs(String key, Class<T> type) {
        Object o = metadata.get(key);

        if (o != null && type.isInstance(o))
            return type.cast(o);
        else
            return null;
    }

    public void removeMetadata(String key) {
        metadata.remove(key);
    }

    public int getMetadataCount() {
        return metadata.size();
    }

    public boolean hasMetadata(String key) {
        return metadata.containsKey(key);
    }

    public RpgPlayer setExp(int exp) {
        this.exp = exp;

        return this;
    }

    public int getExp() {
        return this.exp;
    }

    public RpgPlayer addExp(int amount) {
        this.exp += amount;

        int toLevel = LevelManager.getExpToLevel(this.level + 1);

        if (toLevel <= this.exp) {
            while (toLevel <= this.exp) {
                this.exp -= toLevel;
                this.level++;
                this.skillPoints += LevelManager.getSkillPointsPerLevel();

                toLevel = LevelManager.getExpToLevel(this.level + 1);
            }
        }

        this.basePlayer.setLevel(this.level);
        this.basePlayer.setExp((float)((double)this.exp / (double)toLevel));

        return this;
    }

    public RpgPlayer removeExp(int amount) {
        this.exp = Math.max(this.exp - amount, 0);

        return this;
    }

    public int getLevel() {
        return this.level;
    }

    public RpgPlayer setLevel(int level) {
        this.level = level;

        return this;
    }

    public RpgPlayer addLevels(int amount) {
        this.level = LevelManager.getLevelCap() != -1 ? Math.min(this.level + amount, LevelManager.getLevelCap()) : this.level + amount;

        return this;
    }

    public RpgPlayer removeLevels(int amount) {
        this.level = Math.max(this.level - amount, 0);

        return this;
    }

    public int getSkillPoints() {
        return this.skillPoints;
    }

    public RpgPlayer setSkillPoints(int skillPoints) {
        this.skillPoints = skillPoints;

        return this;
    }

    public RpgPlayer addSkillPoints(int amount) {
        this.skillPoints += amount;

        return this;
    }

    public RpgPlayer removeSkillPoints(int amount) {
        this.skillPoints = Math.max(this.skillPoints - amount, 0);

        return this;
    }
}