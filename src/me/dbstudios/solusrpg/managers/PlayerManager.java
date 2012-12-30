/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dbstudios.solusrpg.managers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.exceptions.RpgPlayerConfigException;
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
}
