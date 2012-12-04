/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgPlayerDamageEntityEvent extends RpgPlayerEvent implements Cancellable {
    private final Entity target;
    private final DamageCause cause;
    
    private boolean cancelled = false;
    private int damage;
    
    public RpgPlayerDamageEntityEvent(Player player, Entity target, DamageCause cause, int damage) {
	super(player);
	
	this.target = target;
	this.cause = cause;
	this.damage = damage;
    }
    
    public Entity getTarget() {
	return this.target;
    }
    
    public DamageCause getCause() {
	return this.cause;
    }
    
    public int getDamage() {
	return this.damage;
    }
    
    public void setDamage(int damage) {
	this.damage = damage;
    }
    
    public boolean isCancelled() {
	return this.cancelled;
    }
    
    public void setCancelled(boolean cancelled) {
	this.cancelled = cancelled;
    }
}
