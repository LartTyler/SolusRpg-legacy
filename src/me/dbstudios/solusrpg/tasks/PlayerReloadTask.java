/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.tasks;

import me.dbstudios.solusrpg.managers.PlayerManager;
import org.bukkit.entity.Player;

/**
 *
 * @author Tyler Lartonoix
 */
public class PlayerReloadTask implements Runnable {
    private final Player player;

    public PlayerReloadTask(Player player) {
        this.player = player;
    }

    public void run() {
        PlayerManager.remove(player);
        PlayerManager.add(player);
    }
}
