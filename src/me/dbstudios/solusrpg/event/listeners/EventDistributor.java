/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.event.listeners;

import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.event.block.RpgBlockBreakEvent;
import me.dbstudios.solusrpg.event.block.RpgBlockPlaceEvent;
import me.dbstudios.solusrpg.event.player.*;
import me.dbstudios.solusrpg.managers.PlayerManager;
import me.dbstudios.solusrpg.util.DamageType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;

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
	    RpgPlayerDamageByPlayerEvent event = new RpgPlayerDamageByPlayerEvent((Player)ev.getEntity(), (Player)ev.getDamager(), ev.getCause(), ev.getDamage(), DamageType.PHYSICAL);

	    Bukkit.getPluginManager().callEvent(event);

	    ev.setCancelled(event.isCancelled());
	    ev.setDamage(event.getDamage());
	} else if (ev.getEntity() instanceof Player) {
	    RpgPlayerDamageByEntityEvent event = new RpgPlayerDamageByEntityEvent((Player)ev.getEntity(), ev.getDamager(), ev.getCause(), ev.getDamage(), DamageType.PHYSICAL);

	    Bukkit.getPluginManager().callEvent(event);

	    ev.setCancelled(event.isCancelled());
	    ev.setDamage(event.getDamage());
	} else if (ev.getDamager() instanceof Player) {
	    RpgPlayerDamageEntityEvent event = new RpgPlayerDamageEntityEvent((Player)ev.getDamager(), ev.getEntity(), ev.getCause(), ev.getDamage(), DamageType.PHYSICAL);

	    Bukkit.getPluginManager().callEvent(event);

	    ev.setCancelled(event.isCancelled());
	    ev.setDamage(event.getDamage());
	}
    }

    // This is the more desireable way to modify the damage a player will take. Since HIGHEST priority occurs last, this would allow any damage the player takes via the event
    // system to be scaled appropriately before being applied.
    @EventHandler(priority = EventPriority.HIGHEST)
    public void postEntityDamageByEntity(EntityDamageByEntityEvent ev) {
        if (!ev.isCancelled() && ev.getDamage() > 0 && ev.getEntity() instanceof Player) {
            RpgPlayer player = PlayerManager.get((Player)ev.getEntity());

            player.setHealth(player.getHealth() - ev.getDamage());

            int trueHealth = (int)Math.ceil(20.0 * ((double)player.getHealth() / (double)player.getMaxHealth()));

            ev.setDamage(player.getBasePlayer().getHealth() - trueHealth);

            // If we reach this point, damage should be dealt to the RpgPlayer. However, this does not always mean that the player's true health
            // will change. If it does not (i.e. the end damage is set to zero), then the damage animation will not player. The block below will increase the
            // players health by 1, then set the final event damage to 1, forcing the damage animation to play, but not causing any change in the player's true health.
            if (ev.getDamage() == 0 && trueHealth != 20) {
                player.getBasePlayer().setHealth(player.getBasePlayer().getHealth() + 1);
                ev.setDamage(1);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageByBlock(EntityDamageByBlockEvent ev) {
	if (ev.getEntity() instanceof Player) {
	    RpgPlayerDamageByBlockEvent event = new RpgPlayerDamageByBlockEvent((Player)ev.getEntity(), ev.getDamager(), ev.getCause(), ev.getDamage(), DamageType.PHYSICAL);

	    Bukkit.getPluginManager().callEvent(event);

	    ev.setCancelled(event.isCancelled());
	    ev.setDamage(ev.getDamage());
	}
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void postEntityDamageByBlock(EntityDamageByBlockEvent ev) {
        if (!ev.isCancelled() && ev.getDamage() > 0 && ev.getEntity() instanceof Player) {
            RpgPlayer player = PlayerManager.get((Player)ev.getEntity());

            player.setHealth(player.getHealth() - ev.getDamage());

            int trueHealth = (int)Math.ceil(20.0 * ((double)player.getHealth() / (double)player.getMaxHealth()));

            ev.setDamage(player.getBasePlayer().getHealth() - trueHealth);

            if (ev.getDamage() == 0 && trueHealth != 20) {
                player.getBasePlayer().setHealth(player.getBasePlayer().getHealth() + 1);
                ev.setDamage(1);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageEvent(EntityDamageEvent ev) {
        if (ev.getEntity() instanceof Player) {
            RpgPlayerDamageEvent event = new RpgPlayerDamageEvent((Player)ev.getEntity(), ev.getCause(), ev.getDamage(), DamageType.TRUE);

            Bukkit.getPluginManager().callEvent(event);

            ev.setCancelled(event.isCancelled());
            ev.setDamage(event.getDamage());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void postEntityDamageEvent(EntityDamageEvent ev) {
        if (!ev.isCancelled() && ev.getDamage() > 0 && ev.getEntity() instanceof Player) {
            RpgPlayer player = PlayerManager.get((Player)ev.getEntity());

            player.setHealth(player.getHealth() - ev.getDamage());

            int trueHealth = (int)Math.ceil(20.0 * ((double)player.getHealth() / (double)player.getMaxHealth()));

            ev.setDamage(player.getBasePlayer().getHealth() - trueHealth);

            if (ev.getDamage() == 0 && trueHealth != 20) {
                player.getBasePlayer().setHealth(player.getBasePlayer().getHealth() + 1);
                ev.setDamage(1);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityRegainHealth(EntityRegainHealthEvent ev) {
	if (ev.getEntity() instanceof Player) {
	    RpgPlayerGainHealthEvent event = new RpgPlayerGainHealthEvent((Player)ev.getEntity(), ev.getAmount(), ev.getRegainReason());

	    Bukkit.getPluginManager().callEvent(event);

	    ev.setAmount(event.getAmount());
	    ev.setCancelled(ev.isCancelled());
	}
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void postEntityRegainHealth(EntityRegainHealthEvent ev) {
        if (!ev.isCancelled() && ev.getAmount() > 0 && ev.getEntity() instanceof Player) {
            RpgPlayer player = PlayerManager.get((Player)ev.getEntity());

            player.setHealth(player.getHealth() + ev.getAmount());

            int trueHealth = (int)Math.floor(20.0 * ((double)player.getHealth() / (double)player.getMaxHealth()));

            ev.setAmount(Math.max(trueHealth - player.getBasePlayer().getHealth(), 0));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent ev) {
	RpgPlayerInteractEntityEvent event = new RpgPlayerInteractEntityEvent(ev.getPlayer(), ev.getRightClicked());

	Bukkit.getPluginManager().callEvent(event);

	ev.setCancelled(event.isCancelled());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteractEvent(PlayerInteractEvent ev) {
        if (ev.getAction() != Action.LEFT_CLICK_BLOCK)
            return;

	RpgPlayerInteractEvent event = new RpgPlayerInteractEvent(ev.getPlayer(), ev.getAction(), ev.getItem(), ev.getClickedBlock(), ev.getBlockFace());

	Bukkit.getPluginManager().callEvent(event);

	ev.setCancelled(event.isCancelled());
	ev.setUseInteractedBlock(event.useBlock());
	ev.setUseItemInHand(event.useItem());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityShootBow(EntityShootBowEvent ev) {
	if (ev.getEntity() instanceof Player && ev.getProjectile() instanceof Projectile) {
	    RpgPlayerShootBowEvent event = new RpgPlayerShootBowEvent((Player)ev.getEntity(), ev.getBow(), (Projectile)ev.getProjectile(), ev.getForce());

	    Bukkit.getPluginManager().callEvent(event);

	    ev.setCancelled(event.isCancelled());
	    ev.setProjectile(event.getProjectile());
	}
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerExpChange(PlayerExpChangeEvent ev) {
        RpgPlayerExpChangeEvent event = new RpgPlayerExpChangeEvent(ev.getPlayer(), ev.getAmount());

        Bukkit.getPluginManager().callEvent(event);

        ev.setAmount(event.getAmount());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void postPlayerExpChange(PlayerExpChangeEvent ev) {
        if (ev.getAmount() > 0) {
            RpgPlayer player = PlayerManager.get(ev.getPlayer());

            player.addExp(ev.getAmount());
            ev.setAmount(0);
        }
    }
}
