/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.event.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.entities.conf.PermitNode;
import me.dbstudios.solusrpg.entities.conf.StatType;
import me.dbstudios.solusrpg.event.block.RpgBlockBreakEvent;
import me.dbstudios.solusrpg.event.block.RpgBlockPlaceEvent;
import me.dbstudios.solusrpg.event.player.*;
import me.dbstudios.solusrpg.managers.PhraseManager;
import me.dbstudios.solusrpg.managers.PlayerManager;
import me.dbstudios.solusrpg.util.Util;
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
            String item = ev.getBlockName().toLowerCase();

	    args.put("item", (Util.isUncountable(item) ? "" : "a" + (Util.isVowel(item.charAt(0)) ? "n " : " ")) + item.replace('_', ' '));

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
            String item = ev.getBlockName().toLowerCase();

	    args.put("item", (Util.isUncountable(item) ? "" : "a" + (Util.isVowel(item.charAt(0)) ? "n " : " ")) + item.replace('_', ' '));

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
            String item = ev.getItemName().toLowerCase();

	    args.put("item", (Util.isUncountable(item) ? "" : "a" + (Util.isVowel(item.charAt(0)) ? "n " : " ")) + item.replace('_', ' '));

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
                String item = ev.getWeapon().toLowerCase();

                args.put("item", (Util.isUncountable(item) ? "" : "a" + (Util.isVowel(item.charAt(0)) ? "n " : " ")) + item.replace('_', ' '));

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

            if (damage > 0) {
                ev.setDamage(damage);
            } else {
                ev.setCancelled(true);

                if (PhraseManager.phraseExists("player.no-damage")) {
                    Map<String, String> args = new HashMap<>();

                    args.put("target", Util.getEntityName(ev.getTarget()));
                    args.put("weapon", ev.getWeapon());

                    player.sendEventMessage(PhraseManager.getPhrase("player.no-damage"), args);
                }
            }
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
                String item = ev.getWeapon().toLowerCase();

                args.put("item", (Util.isUncountable(item) ? "" : "a" + (Util.isVowel(item.charAt(0)) ? "n" : "")) + ' ' + item.replace('_', ' '));

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

            if (damage > 0) {
                ev.setDamage(Math.max(0, damage));
            } else {
                ev.setCancelled(true);

                if (PhraseManager.phraseExists("player.no-damage")) {
                    Map<String, String> args = new HashMap<>();

                    args.put("target", ev.getPlayer().getDisplayName());
                    args.put("weapon", ev.getWeapon());

                    damager.sendEventMessage(PhraseManager.getPhrase("player.no-damage"), args);
                }
            }
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

        if (damage > 0)
            ev.setDamage(Math.max(0, damage));
        else
            ev.setCancelled(true);
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

        if (damage > 0)
            ev.setDamage(Math.max(0, damage));
        else
            ev.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRpgPlayerDamage(RpgPlayerDamageEvent ev) {
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

        if (damage > 0)
            ev.setDamage(Math.max(0, damage));
        else
            ev.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRpgPlayerSpawn(RpgPlayerSpawnEvent ev) {
        ev.getPlayer().setHealth(ev.getPlayer().getMaxHealth());
    }
}
