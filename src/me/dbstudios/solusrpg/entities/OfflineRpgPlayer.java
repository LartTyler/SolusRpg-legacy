/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.entities;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.entities.conf.RpgClass;
import me.dbstudios.solusrpg.entities.conf.Stat;
import me.dbstudios.solusrpg.entities.conf.StatType;
import me.dbstudios.solusrpg.exceptions.RpgPlayerConfigException;
import me.dbstudios.solusrpg.managers.ClassManager;
import me.dbstudios.solusrpg.util.Metadatable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author Tyler Lartonoix
 */
public class OfflineRpgPlayer implements Metadatable<String, Object> {
    private final File file;
    private final Map<String, Object> metadata = new HashMap<>();
    private final Map<StatType, Stat> stats = new EnumMap<>(StatType.class);

    private String displayName;
    private RpgClass rpgClass;
    private int health;
    private int level;
    private int exp;
    private int skillPoints;

    public OfflineRpgPlayer(File file) throws RpgPlayerConfigException {
        this.file = file;

        FileConfiguration conf = YamlConfiguration.loadConfiguration(file);

        this.displayName = conf.getString("player.name");
        this.rpgClass = ClassManager.getClass("player.class");
        this.health = conf.getInt("player.health");

        for (StatType t : StatType.values())
            stats.put(t, new Stat(conf, t, "player.stats"));

        this.level = conf.getInt("player.level");
        this.exp = conf.getInt("player.experience");
        this.skillPoints = conf.getInt("player.skill-points");

        ConfigurationSection s = conf.getConfigurationSection("player.metadata");

        if (s != null)
            for (String k : s.getKeys(false))
                metadata.put(k.replace('_', '.'), s.get(k));
    }

    public File getSaveFile() {
        return this.file;
    }

    public FileConfiguration getConfiguration() {
        return YamlConfiguration.loadConfiguration(this.file);
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public void clearMetadata() {
        metadata.clear();
    }

    public boolean save() {
        FileConfiguration conf = this.getConfiguration();

        conf.set("player.class", rpgClass.getSystemName());
        conf.set("player.health", health);

        for (StatType t : StatType.values())
            conf.set("player.stats." + t, stats.get(t).getValue());

        for (String key : metadata.keySet())
            conf.set("player.metadata." + key.replace('.', '_'), metadata.get(key));

        conf.set("player.level", this.level);
        conf.set("player.experience", this.exp);
        conf.set("player.skill-points", this.skillPoints);
        conf.set("player.name", this.displayName);

        try {
            conf.save(this.file);

            return true;
        } catch (IOException e) {
            SolusRpg.log(Level.SEVERE, "Could not save player data for {0}.", file.getName());
        }

        return false;
    }
}
