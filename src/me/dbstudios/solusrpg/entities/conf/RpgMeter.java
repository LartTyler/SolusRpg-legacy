/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dbstudios.solusrpg.entities.conf;

import me.dbstudios.solusrpg.entities.RpgPlayer;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author
 * tyler
 */
public abstract class RpgMeter implements Meter<RpgPlayer, Integer> {
    private final RpgPlayer owner;
    private final int max;
    private final String name;

    private int value;

    public RpgMeter(RpgPlayer owner, FileConfiguration conf) {
       this.owner = owner;
       this.name = conf.getString("class.stats.health.name", "Unnamed Meter");
       this.max = conf.getInt("class.stats.vitality", 20);
    }

    public Integer getValue() {
        return this.value;
    }

    public Integer getMaxValue() {
        return this.max;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getMeterName() {
        return this.name;
    }

    public RpgPlayer getOwner() {
        return this.owner;
    }

    public void add(Integer amount) {
        this.value = Math.min(this.value + amount, max);
    }

    public void remove(Integer amount) {
        this.value = Math.max(this.value - amount, 0);
    }
}
