/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgPlayerDamageEvent extends RpgPlayerEvent implements Cancellable {
    private final DamageCause cause;
    
    private boolean cancelled = false;
    private int damage;
    
    public RpgPlayerDamageEvent(Player player, DamageCause cause, int damage) {
	super(player);
	
	this.cause = cause;
	this.damage = damage;
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
