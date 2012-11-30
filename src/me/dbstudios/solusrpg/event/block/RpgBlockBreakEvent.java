/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.event.block;

import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.managers.PlayerManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgBlockBreakEvent extends RpgBlockEvent implements Cancellable {
    private final RpgPlayer player;
    
    private boolean cancelled = false;
    private int exp = 0;
    
    public RpgBlockBreakEvent(Block block, Player player, int exp) {
	super(block);
	
	this.player = PlayerManager.get(player);
	this.exp = exp;
    }
    
    public RpgPlayer getPlayer() {
	return this.player;
    }
    
    public int getExp() {
	return this.exp;
    }
    
    public void setExp(int exp) {
	this.exp = exp;
    }
    
    public boolean isCancelled() {
	return this.cancelled;
    }
    
    public void setCancelled(boolean cancelled) {
	this.cancelled = cancelled;
    }
}
