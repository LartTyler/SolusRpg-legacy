/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.event.player;

import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.managers.PlayerManager;
import me.dbstudios.solusrpg.util.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgPlayerDamageByPlayerEvent extends RpgPlayerDamageEvent {
    private final RpgPlayer damager;

    public RpgPlayerDamageByPlayerEvent(Player player, Player damager, DamageCause cause, int damage, DamageType type) {
	super(player, cause, damage, type);

	this.damager = PlayerManager.get(damager);
    }

    public RpgPlayer getDamager() {
	return this.damager;
    }
}
