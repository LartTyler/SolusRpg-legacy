/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgPlayerGainHealthEvent extends RpgPlayerEvent implements Cancellable {
    private final RegainReason reason;
    
    private boolean cancelled = false;
    private int amount;
    
    public RpgPlayerGainHealthEvent(Player player, int amount, RegainReason reason) {
	super(player);
	
	this.reason = reason;
	this.amount = amount;
    }
    
    public int getAmount() {
	return this.amount;
    }
    
    public RegainReason getReason() {
	return this.reason;
    }
    
    public boolean isCancelled() {
	return this.cancelled;
    }
    
    public void setCancelled(boolean cancelled) {
	this.cancelled = cancelled;
    }
}
