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
public class RpgPlayerJoinEvent extends RpgPlayerEvent {
    private String joinMsg;
    
    public RpgPlayerJoinEvent(Player player, String joinMsg) {
	super(player);
	
	this.joinMsg = joinMsg;
    }
    
    public String getJoinMessage() {
	return this.joinMsg;
    }
    
    public void setJoinMessage(String joinMsg) {
	this.joinMsg = joinMsg;
    }
}
