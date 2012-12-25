/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.event.player;

import me.dbstudios.solusrpg.util.DamageType;
import me.dbstudios.solusrpg.util.Util;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.getspout.spoutapi.block.SpoutBlock;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgPlayerDamageByBlockEvent extends RpgPlayerDamageEvent {
    private final SpoutBlock damager;

    public RpgPlayerDamageByBlockEvent(Player player, Block damager, DamageCause cause, int damage, DamageType type) {
	super(player, cause, damage, type);

	this.damager = Util.toSpoutBlock(damager);
    }

    public SpoutBlock getDamager() {
	return this.damager;
    }
}
