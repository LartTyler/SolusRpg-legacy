/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dbstudios.solusrpg.entities.conf;

import bsh.Interpreter;
import java.io.File;
import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.util.Directories;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author
 * tlartonoix
 */
public class Level {
    private Interpreter inter = new Interpreter();
    private String levelFunction;
    private int level;
    private int exp;

    public Level(RpgPlayer player) {
	File f = new File(Directories.CONFIG + "config.yml");
	FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
    }
}
