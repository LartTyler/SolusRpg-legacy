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
public class RpgHealthMeter extends RpgMeter {
    public RpgHealthMeter(RpgPlayer owner, FileConfiguration conf) {
        super(owner, conf);
    }

    public void damage(int amount) {
        super.remove(amount);
    }

    public void heal(int amount) {
        super.add(amount);
    }
}
