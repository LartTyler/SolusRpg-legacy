/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.event.block;

import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.managers.PlayerManager;
import me.dbstudios.solusrpg.util.Util;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.block.SpoutBlock;
import org.getspout.spoutapi.inventory.SpoutItemStack;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgBlockPlaceEvent extends RpgBlockEvent implements Cancellable {
    private final BlockState oldState;
    private final SpoutBlock placedAgainst;
    private final SpoutItemStack inHand;
    private final RpgPlayer player;
    
    private boolean cancelled = false;
    private boolean canBuild;
    
    public RpgBlockPlaceEvent(Block block, BlockState oldState, Block placedAgainst, ItemStack inHand, Player player, boolean canBuild) {
	super(block);
	
	this.oldState = oldState;
	this.placedAgainst = Util.toSpoutBlock(placedAgainst);
	this.inHand = new SpoutItemStack(inHand);
	this.player = PlayerManager.get(player);
	this.canBuild = canBuild;
    }
    
    public boolean canBuild() {
	return this.canBuild;
    }
    
    public SpoutBlock getBlockAgainst() {
	return this.placedAgainst;
    }
    
    public BlockState getOldState() {
	return this.oldState;
    }
    
    public ItemStack getItemInHand() {
	return this.inHand;
    }
    
    public RpgPlayer getPlayer() {
	return this.player;
    }
    
    public boolean isCancelled() {
	return this.cancelled;
    }
    
    public void setBuild(boolean canBuild) {
	this.canBuild = canBuild;
    }
    
    public void setCancelled(boolean cancelled) {
	this.cancelled = cancelled;
    }
}
