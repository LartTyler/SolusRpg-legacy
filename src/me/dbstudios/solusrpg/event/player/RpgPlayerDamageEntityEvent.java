/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.event.player;

import me.dbstudios.solusrpg.util.DamageType;
import me.dbstudios.solusrpg.util.Util;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgPlayerDamageEntityEvent extends RpgPlayerEvent implements Cancellable {
    private final Entity target;
    private final DamageCause cause;
    private final String weaponName;
    private final DamageType type;

    private boolean cancelled = false;
    private int damage;

    public RpgPlayerDamageEntityEvent(Player player, Entity target, DamageCause cause, int damage, DamageType type) {
	super(player);

	this.weaponName = Util.getItemName(player.getItemInHand().getType(), player.getItemInHand().getData().getData());
	this.target = target;
	this.cause = cause;
	this.damage = damage;
        this.type = type;
    }

    public Entity getTarget() {
	return this.target;
    }

    public DamageCause getCause() {
	return this.cause;
    }

    public String getWeapon() {
	return this.weaponName;
    }

    public int getDamage() {
	return this.damage;
    }

    public void setDamage(int damage) {
	this.damage = damage;
    }

    public DamageType getDamageType() {
        return this.type;
    }

    public boolean isCancelled() {
	return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
	this.cancelled = cancelled;
    }
}
