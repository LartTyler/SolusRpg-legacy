/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.event.listeners;

import java.util.HashMap;
import java.util.Map;
import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.entities.conf.PermitNode;
import me.dbstudios.solusrpg.entities.conf.StatType;
import me.dbstudios.solusrpg.event.block.RpgBlockBreakEvent;
import me.dbstudios.solusrpg.event.block.RpgBlockPlaceEvent;
import me.dbstudios.solusrpg.event.player.*;
import me.dbstudios.solusrpg.managers.PhraseManager;
import me.dbstudios.solusrpg.managers.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgStockListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onRpgPlayerQuit(RpgPlayerQuitEvent ev) {
        PlayerManager.remove(ev.getPlayer().getBasePlayer());
    }

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
            int damage = 0;

            switch (ev.getDamageType()) {
                case PHYSICAL:
                    damage = player.getStat(StatType.STRENGTH).getValue();

                    break;
                case MAGICAL:
                    damage = player.getStat(StatType.MAGIC).getValue();

                    break;
            }

            ev.setDamage(damage);
	}
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRpgPlayerDamageByPlayer(RpgPlayerDamageByPlayerEvent ev) {
        RpgPlayer player = ev.getPlayer();
        RpgPlayer damager = ev.getDamager();

        if (!damager.isAllowed(PermitNode.USE, ev.getWeapon())) {
            ev.setCancelled(true);

            if (PhraseManager.phraseExists("player.use-deny")) {
                Map<String, String> args = new HashMap<>();

                args.put("item", ev.getWeapon().replace('_', ' ').toLowerCase());

                damager.sendMessage(PhraseManager.getPhrase("player.use-deny"), args);
            }
        }

        if (!ev.isCancelled()) {
            int damage = 0;

            switch (ev.getDamageType()) {
                case PHYSICAL:
                    damage = damager.getStat(StatType.STRENGTH).getValue() - player.getStat(StatType.ARMOR).getValue();

                    break;
                case MAGICAL:
                    damage = damager.getStat(StatType.MAGIC).getValue() - player.getStat(StatType.AURA).getValue();

                    break;
            }

            ev.setDamage(Math.max(0, damage));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRpgPlayerDamageByBlock(RpgPlayerDamageByBlockEvent ev) {
        RpgPlayer player = ev.getPlayer();
        int damage = ev.getDamage();

        switch (ev.getDamageType()) {
            case PHYSICAL:
                damage -= player.getStat(StatType.ARMOR).getValue();

                break;
            case MAGICAL:
                damage -= player.getStat(StatType.AURA).getValue();

                break;
        }

        ev.setDamage(Math.max(0, damage));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRpgPlayerDamageByEntity(RpgPlayerDamageByEntityEvent ev) {
        RpgPlayer player = ev.getPlayer();
        int damage = ev.getDamage();

        switch (ev.getDamageType()) {
            case PHYSICAL:
                damage -= player.getStat(StatType.ARMOR).getValue();

                break;
            case MAGICAL:
                damage -= player.getStat(StatType.MAGIC).getValue();

                break;
        }

        ev.setDamage(Math.max(0, damage));
    }
}
