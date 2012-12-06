/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.event.player;

import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.managers.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author Tyler Lartonoix
 */
public abstract class RpgPlayerEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    
    private final RpgPlayer player;
    
    public RpgPlayerEvent(Player player) {
	this.player = PlayerManager.get(player);
    }
    
    public RpgPlayer getPlayer() {
	return this.player;
    }

    public HandlerList getHandlers() {
	return RpgPlayerEvent.handlers;
    }

    public static HandlerList getHandlerList() {
	return RpgPlayerEvent.handlers;
    }
}
