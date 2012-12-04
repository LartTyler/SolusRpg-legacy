/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.event.player;

import me.dbstudios.solusrpg.util.Util;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.block.SpoutBlock;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.material.Material;
import org.getspout.spoutapi.material.MaterialData;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgPlayerInteractEvent extends RpgPlayerEvent implements Cancellable {
    private final Action action;
    private final SpoutItemStack item;
    private final SpoutBlock block;
    private final BlockFace face;
    
    private boolean cancelled = false;
    private Result useBlock = Result.DEFAULT;
    private Result useItem = Result.DEFAULT;
    
    public RpgPlayerInteractEvent(Player player, Action action, ItemStack item, Block block, BlockFace face) {
	super(player);
	
	this.action = action;
	this.item = new SpoutItemStack(item);
	this.block = Util.toSpoutBlock(block);
	this.face = face;
    }
    
    public Action getAction() {
	return this.action;
    }
    
    public BlockFace getBlockFace() {
	return this.face;
    }
    
    public SpoutBlock getClickedBlock() {
	return this.block;
    }
    
    public SpoutItemStack getItem() {
	return this.item;
    }
    
    public Material getMaterial() {
	return this.item.getMaterial();
    }
    
    public boolean involvesBlock() {
	return this.block != null;
    }
    
    public boolean involvesItem() {
	return this.item != null;
    }
    
    public boolean isBlockInHand() {
	return MaterialData.getBlock(this.item.getTypeId(), this.item.getData().getData()) != null;
    }
    
    public boolean isCancelled() {
	return this.cancelled;
    }
    
    public void setCancelled(boolean cancelled) {
	this.cancelled = cancelled;
    }
    
    public void setUseBlock(Result useBlock) {
	this.useBlock = useBlock;
    }
    
    public void setUseItem(Result useItem) {
	this.useItem = useItem;
    }
    
    public Result useBlock() {
	return this.useBlock;
    }
    
    public Result useItem() {
	return this.useItem;
    }
}
