
package me.dbstudios.solusrpg.entities;

import java.io.File;
import java.io.IOException;
import me.dbstudios.solusrpg.entities.conf.PermitNode;
import me.dbstudios.solusrpg.entities.conf.RpgClass;
import me.dbstudios.solusrpg.exceptions.RpgPlayerConfigException;
import me.dbstudios.solusrpg.managers.ClassManager;
import me.dbstudios.solusrpg.util.Directories;
import me.dbstudios.solusrpg.util.Util;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

/**
 * @author Tyler Lartonoix
 */
public class RpgPlayer {
    private final RpgClass rpgClass;
    private final SpoutPlayer basePlayer;
    
    public RpgPlayer(Player p) throws RpgPlayerConfigException {
        this.basePlayer = SpoutManager.getPlayer(p);
	
	File f = new File(Directories.DATA + p.getName().substring(0, 2).toLowerCase() + File.separator + p.getName().toLowerCase() + ".yml");
	
	if (!f.exists()) {
	    (new File(f.getPath().substring(0, f.getPath().lastIndexOf(File.separator)))).mkdirs();
	    
	    try {
		f.createNewFile();
	    } catch (IOException e) {
		throw new RpgPlayerConfigException("Could not create player data file.");
	    }
	}
	
	FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
	String cl = conf.getString("player.class", null);
	
	if (ClassManager.exists(cl))
	    this.rpgClass = ClassManager.getClass(cl);
	else
	    this.rpgClass = ClassManager.DEFAULT_CLASS;
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
    
    public RpgPlayer setPlayerListName(String name) {
	basePlayer.setPlayerListName(name);
	
	return this;
    }
    
    public RpgPlayer sendMessage(String msg) {
	basePlayer.sendMessage(msg);
	
	return this;
    }
    
    public RpgPlayer sendMessage(String msg, Object... args) {
	return this.sendMessage(Util.format(msg, args));
    }
    
    public SpoutPlayer getBasePlayer() {
	return this.basePlayer;
    }
    
    public RpgClass getRpgClass() {
	return this.rpgClass;
    }
    
    public boolean isAllowed(PermitNode node, String item) {
	return rpgClass.isAllowed(node, item);
    }
}
