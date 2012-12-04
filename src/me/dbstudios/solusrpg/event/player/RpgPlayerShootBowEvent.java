/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.event.player;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgPlayerShootBowEvent extends RpgPlayerEvent implements Cancellable {
    private final ItemStack bow;
    
    private boolean cancelled = false;
    private Projectile projectile;
    private float force;
    
    public RpgPlayerShootBowEvent(Player player, ItemStack bow, Projectile projectile, float force) {
	super(player);
	
	this.bow = bow;
	this.projectile = projectile;
	this.force = force;
    }
    
    public ItemStack getBow() {
	return this.bow;
    }
    
    public float getForce() {
	return this.force;
    }
    
    public Projectile getProjectile() {
	return this.projectile;
    }
    
    public void setProjectile(Projectile projectile) {
	this.projectile = projectile;
    }
    
    public boolean isCancelled() {
	return this.cancelled;
    }
    
    public void setCancelled(boolean cancelled) {
	this.cancelled = cancelled;
    }
}
