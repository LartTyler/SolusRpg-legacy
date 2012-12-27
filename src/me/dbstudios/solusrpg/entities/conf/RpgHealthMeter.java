/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dbstudios.solusrpg.entities.conf;

import me.dbstudios.solusrpg.entities.RpgPlayer;
import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author
 * tyler
 */
public class RpgHealthMeter extends RpgMeter {
    public RpgHealthMeter(RpgPlayer owner, ConfigurationSection meterNode) {
        super(owner, meterNode);
    }

    public void damage(int amount) {
        super.remove(amount);
        
    }
}
