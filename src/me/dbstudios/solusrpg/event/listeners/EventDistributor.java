/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.event.listeners;

import me.dbstudios.solusrpg.event.player.RpgPlayerJoinEvent;
import me.dbstudios.solusrpg.event.player.RpgPlayerQuitEvent;
import me.dbstudios.solusrpg.managers.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author Tyler Lartonoix
 */
public class EventDistributor implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent ev) {
	if (PlayerManager.add(ev.getPlayer())) {
	    RpgPlayerJoinEvent event = new RpgPlayerJoinEvent(ev.getPlayer(), ev.getJoinMessage());
	    
	    Bukkit.getPluginManager().callEvent(event);
	    
	    ev.setJoinMessage(event.getJoinMessage());
	}
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent ev) {
	RpgPlayerQuitEvent event = new RpgPlayerQuitEvent(ev.getPlayer(), ev.getQuitMessage());
	
	Bukkit.getPluginManager().callEvent(event);
	
	ev.setQuitMessage(event.getQuitMessage());
    }
}
