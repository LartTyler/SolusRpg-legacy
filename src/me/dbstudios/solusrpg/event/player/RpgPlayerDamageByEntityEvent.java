/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.event.player;

import me.dbstudios.solusrpg.util.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgPlayerDamageByEntityEvent extends RpgPlayerDamageEvent {
    private final Entity damager;

    public RpgPlayerDamageByEntityEvent(Player player, Entity damager, DamageCause cause, int damage, DamageType type) {
	super(player, cause, damage, type);

	this.damager = damager;
    }

    public Entity getDamager() {
	return this.damager;
    }
}
