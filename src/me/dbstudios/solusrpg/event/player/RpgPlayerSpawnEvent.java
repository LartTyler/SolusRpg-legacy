/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.event.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgPlayerSpawnEvent extends RpgPlayerEvent {
    private Location location;
    private boolean isBedSpawn;
    
    public RpgPlayerSpawnEvent(Player player, Location location, boolean isBedSpawn) {
	super(player);
	
	this.location = location;
	this.isBedSpawn = isBedSpawn;
    }
    
    public Location getSpawnLocation() {
	return this.location;
    }
    
    public void setSpawnLocation(Location location) {
	this.location = location;
    }
    
    public boolean isBedSpawn() {
	return this.isBedSpawn;
    }
}
