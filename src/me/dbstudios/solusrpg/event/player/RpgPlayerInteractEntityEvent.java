/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgPlayerInteractEntityEvent extends RpgPlayerEvent implements Cancellable {
    private final Entity entity;
    
    private boolean cancelled = false;
    
    public RpgPlayerInteractEntityEvent(Player player, Entity entity) {
	super(player);
	
	this.entity = entity;
    }
    
    public Entity getEntity() {
	return this.entity;
    }
    
    public boolean isCancelled() {
	return this.cancelled;
    }
    
    public void setCancelled(boolean cancelled) {
	this.cancelled = cancelled;
    }
}
