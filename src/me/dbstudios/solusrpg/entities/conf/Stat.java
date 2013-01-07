/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.entities.conf;

import me.dbstudios.solusrpg.exceptions.IncompatibleStatTypeException;
import me.dbstudios.solusrpg.util.Util;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Tyler Lartonoix
 */
public class Stat {
    private final StatType type;

    private int val;

    public Stat(int val, StatType type) {
	this.val = val;
	this.type = type;
    }

    public Stat(FileConfiguration conf, StatType type, String pathPrefix) {
	this.type = type;
	this.val = conf.getInt(pathPrefix + "." + type.name().toLowerCase());
    }

    public StatType getStatType() {
	return this.type;
    }

    public int getValue() {
	return this.val;
    }

    public Stat merge(Stat stat) throws IncompatibleStatTypeException {
	if (stat.getStatType() == this.type)
	    return new Stat(stat.getValue() + this.val, this.type);
	else
	    throw new IncompatibleStatTypeException(Util.format("The stat type '{0}' is not compatible with the type '{1}'.", stat.getStatType(), this.type));
    }
}
