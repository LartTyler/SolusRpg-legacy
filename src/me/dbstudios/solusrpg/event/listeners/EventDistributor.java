/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.event.listeners;

import me.dbstudios.solusrpg.event.block.RpgBlockBreakEvent;
import me.dbstudios.solusrpg.event.block.RpgBlockPlaceEvent;
import me.dbstudios.solusrpg.event.player.RpgPlayerDamageByEntityEvent;
import me.dbstudios.solusrpg.event.player.RpgPlayerDamageByPlayerEvent;
import me.dbstudios.solusrpg.event.player.RpgPlayerDamageEntityEvent;
import me.dbstudios.solusrpg.event.player.RpgPlayerDamageEvent;
import me.dbstudios.solusrpg.event.player.RpgPlayerEvent;
import me.dbstudios.solusrpg.event.player.RpgPlayerJoinEvent;
import me.dbstudios.solusrpg.event.player.RpgPlayerQuitEvent;
import me.dbstudios.solusrpg.event.player.RpgPlayerSpawnEvent;
import me.dbstudios.solusrpg.managers.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

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
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent ev) {
	RpgBlockBreakEvent event = new RpgBlockBreakEvent(ev.getBlock(), ev.getPlayer(), ev.getExpToDrop());
	
	Bukkit.getPluginManager().callEvent(event);
	
	ev.setExpToDrop(event.getExp());
	ev.setCancelled(event.isCancelled());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent ev) {
	RpgBlockPlaceEvent event = new RpgBlockPlaceEvent(ev.getBlock(), ev.getBlockReplacedState(), ev.getBlockAgainst(), ev.getItemInHand(), ev.getPlayer(), ev.canBuild());
	
	Bukkit.getPluginManager().callEvent(event);
	
	ev.setBuild(event.canBuild());
	ev.setCancelled(event.isCancelled());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerRespawn(PlayerRespawnEvent ev) {
	RpgPlayerSpawnEvent event = new RpgPlayerSpawnEvent(ev.getPlayer(), ev.getRespawnLocation(), ev.isBedSpawn());
	
	Bukkit.getPluginManager().callEvent(event);
	
	ev.setRespawnLocation(event.getSpawnLocation());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent ev) {
	if (ev.getEntity() instanceof Player && ev.getDamager() instanceof Player) {
	    RpgPlayerDamageByPlayerEvent event = new RpgPlayerDamageByPlayerEvent((Player)ev.getEntity(), (Player)ev.getDamager(), ev.getCause(), ev.getDamage());
	    
	    Bukkit.getPluginManager().callEvent(event);
	    
	    ev.setCancelled(event.isCancelled());
	    ev.setDamage(event.getDamage());
	} else if (ev.getEntity() instanceof Player) {
	    RpgPlayerDamageByEntityEvent event = new RpgPlayerDamageByEntityEvent((Player)ev.getEntity(), ev.getDamager(), ev.getCause(), ev.getDamage());
	    
	    Bukkit.getPluginManager().callEvent(event);
	    
	    ev.setCancelled(event.isCancelled());
	    ev.setDamage(event.getDamage());
	} else if (ev.getDamager() instanceof Player) {
	    RpgPlayerDamageEntityEvent event = new RpgPlayerDamageEntityEvent((Player)ev.getDamager(), ev.getEntity(), ev.getCause(), ev.getDamage());
	    
	    Bukkit.getPluginManager().callEvent(event);
	    
	    ev.setCancelled(event.isCancelled());
	    ev.setDamage(event.getDamage());
	}
    }
}