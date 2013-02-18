/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dbstudios.solusrpg.managers;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.entities.OfflineRpgPlayer;
import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.exceptions.RpgPlayerConfigException;
import me.dbstudios.solusrpg.tasks.PlayerReloadTask;
import me.dbstudios.solusrpg.util.Directories;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 *
 * @author Tyler Lartonoix
 */
public class PlayerManager {
    private static final Map<UUID, RpgPlayer> players = new HashMap<>();

    public static boolean add(Player player) {
	try {
	    players.put(player.getUniqueId(), new RpgPlayer(player));

	    return true;
	} catch (RpgPlayerConfigException e) {
	    SolusRpg.log(Level.SEVERE, "Could not instance player data for '{0}'. Reason: {1}.", player.getName(), e.getMessage());
	    player.kickPlayer("An error occurred while reading your player data, or creating you a new profile. Please contact an administrator.");

	    return false;
	}
    }

    public static RpgPlayer get(UUID uuid) {
	return players.get(uuid);
    }

    public static RpgPlayer get(Player player) {
	return PlayerManager.get(player.getUniqueId());
    }

    public static OfflineRpgPlayer getOfflinePlayer(String name) {
        if (name.length() > 2)
            try {
                File dir = new File(Directories.DATA + name.substring(0, 2).toLowerCase());

                if (dir.exists()) {
                    File[] files = dir.listFiles(new FilenameFilter() {
                        public boolean accept(File dir, String name) {
                            if (name.substring(name.lastIndexOf(".") + 1).equalsIgnoreCase("yml"))
                                return true;

                            return false;
                        }
                    });

                    for (File f : files)
                        if (f.getName().toLowerCase().startsWith(name.toLowerCase()))
                            return new OfflineRpgPlayer(f);
                }
            } catch (RpgPlayerConfigException e) {}

        return null;
    }

    public static boolean exists(UUID uuid) {
	return players.containsKey(uuid);
    }

    public static boolean exists(Player player) {
	return PlayerManager.exists(player.getUniqueId());
    }

    public static void remove(UUID uuid) {
	players.remove(uuid);
    }

    public static void remove(Player player) {
        PlayerManager.get(player).save();
	PlayerManager.remove(player.getUniqueId());
    }

    public static int size() {
	return players.size();
    }

    public static Collection<RpgPlayer> getOnlinePlayers() {
        return players.values();
    }

    public static void reloadPlayer(Player player) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(SolusRpg.getInstance(), new PlayerReloadTask(player), 5);
    }

    public static RpgPlayer matchPlayer(String name) {
        for (RpgPlayer p : players.values())
            if (p.getName().toLowerCase().startsWith(name.toLowerCase()))
                return p;

        return null;
    }
}
