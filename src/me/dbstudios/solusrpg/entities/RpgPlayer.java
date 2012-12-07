
package me.dbstudios.solusrpg.entities;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import me.dbstudios.solusrpg.entities.conf.PermitNode;
import me.dbstudios.solusrpg.entities.conf.RpgClass;
import me.dbstudios.solusrpg.exceptions.RpgPlayerConfigException;
import me.dbstudios.solusrpg.managers.ClassManager;
import me.dbstudios.solusrpg.util.Directories;
import me.dbstudios.solusrpg.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

/**
 * @author Tyler Lartonoix
 */
public class RpgPlayer {
    private final SpoutPlayer basePlayer;

    private RpgClass rpgClass;

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
}
