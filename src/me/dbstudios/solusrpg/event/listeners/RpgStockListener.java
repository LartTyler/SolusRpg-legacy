/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.event.listeners;

import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.entities.conf.PermitNode;
import me.dbstudios.solusrpg.event.block.RpgBlockBreakEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgStockListener implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void onRpgBlockBreak(RpgBlockBreakEvent ev) {
	RpgPlayer player = ev.getPlayer();
	
	if (!player.isAllowed(PermitNode.BREAK, ev.getBlockName())) {
	    
	}
    }
}
