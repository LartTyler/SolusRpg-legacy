/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.events;

import me.dbstudios.solusrpg.util.Util;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.getspout.spoutapi.block.SpoutBlock;

/**
 *
 * @author tlartonoix
 */
public class RpgBlockEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    
    private final SpoutBlock block;
    
    public RpgBlockEvent(Block block) {
	this.block = Util.toSpoutBlock(block);
    }
    
    public SpoutBlock getBlock() {
	return this.block;
    }

    public HandlerList getHandlers() {
	return RpgBlockEvent.handlers;
    }

    public static HandlerList getHandlerList() {
	return RpgBlockEvent.handlers;
    }
}
