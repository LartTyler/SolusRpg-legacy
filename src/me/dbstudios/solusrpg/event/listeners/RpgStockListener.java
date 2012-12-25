/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.event.listeners;

import java.util.HashMap;
import java.util.Map;
import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.entities.conf.PermitNode;
import me.dbstudios.solusrpg.event.block.RpgBlockBreakEvent;
import me.dbstudios.solusrpg.event.block.RpgBlockPlaceEvent;
import me.dbstudios.solusrpg.event.player.RpgPlayerDamageEntityEvent;
import me.dbstudios.solusrpg.event.player.RpgPlayerInteractEvent;
import me.dbstudios.solusrpg.managers.PhraseManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgStockListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onRpgBlockBreak(RpgBlockBreakEvent ev) {
	RpgPlayer player = ev.getPlayer();

	if (!player.isAllowed(PermitNode.BREAK, ev.getBlockName())) {
	    ev.setCancelled(true);

	    Map<String, String> args = new HashMap<>();

	    args.put("item", ev.getBlockName().replace('_', ' ').toLowerCase());

	    if (PhraseManager.phraseExists("player.break-deny"))
		player.sendEventMessage(PhraseManager.getPhrase("player.break-deny"), args);
	}
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRpgBlockPlace(RpgBlockPlaceEvent ev) {
	RpgPlayer player = ev.getPlayer();

	if (!player.isAllowed(PermitNode.PLACE, ev.getBlockName())) {
	    ev.setCancelled(true);

	    Map<String, String> args = new HashMap<>();

	    args.put("item", ev.getBlockName().replace('_', ' ').toLowerCase());

	    if (PhraseManager.phraseExists("player.place-deny"))
		player.sendEventMessage(PhraseManager.getPhrase("player.place-deny"), args);
	}
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRpgPlayerInteract(RpgPlayerInteractEvent ev) {
	RpgPlayer player = ev.getPlayer();

	if (!player.isAllowed(PermitNode.USE, ev.getItemName())) {
	    ev.setCancelled(true);

	    Map<String, String> args = new HashMap<>();

	    args.put("item", ev.getItemName().replace('_', ' ').toLowerCase());

	    if (PhraseManager.phraseExists("player.use-deny"))
		player.sendEventMessage(PhraseManager.getPhrase("player.use-deny"), args);
	}
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRpgPlayerDamageEntity(RpgPlayerDamageEntityEvent ev) {
	RpgPlayer player = ev.getPlayer();

	if (!player.isAllowed(PermitNode.USE, ev.getWeapon())) {
	    ev.setCancelled(true);

	    if (PhraseManager.phraseExists("player.use-deny")) {
		Map<String, String> args = new HashMap<>();

		args.put("item", ev.getWeapon().replace('_', ' ').toLowerCase());

		player.sendEventMessage(PhraseManager.getPhrase("player.use-deny"), args);
	    }
	}

	if (!ev.isCancelled()) {
	    // TODO: Damage modifications based on player's stats
            
	}
    }
}
