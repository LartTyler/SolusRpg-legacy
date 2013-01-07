/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.event.player;

import org.bukkit.entity.Player;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgPlayerQuitEvent extends RpgPlayerEvent {
    private String message;
    
    public RpgPlayerQuitEvent(Player player, String message) {
	super(player);
	
	this.message = message;
    }
    
    public String getQuitMessage() {
	return this.message;
    }
    
    public void setQuitMessage(String message) {
	this.message = message;
    }
}
