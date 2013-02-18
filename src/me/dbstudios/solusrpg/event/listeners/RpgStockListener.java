/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.event.listeners;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.entities.conf.PermitNode;
import me.dbstudios.solusrpg.entities.conf.StatType;
import me.dbstudios.solusrpg.event.block.RpgBlockBreakEvent;
import me.dbstudios.solusrpg.event.block.RpgBlockPlaceEvent;
import me.dbstudios.solusrpg.event.inventory.RpgInventoryCloseEvent;
import me.dbstudios.solusrpg.event.inventory.RpgInventoryOpenEvent;
import me.dbstudios.solusrpg.event.player.*;
import me.dbstudios.solusrpg.managers.ChannelManager;
import me.dbstudios.solusrpg.managers.LevelManager;
import me.dbstudios.solusrpg.managers.PhraseManager;
import me.dbstudios.solusrpg.managers.PlayerManager;
import me.dbstudios.solusrpg.managers.SpecializationManager;
import me.dbstudios.solusrpg.util.Directories;
import me.dbstudios.solusrpg.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgStockListener implements Listener {
    private final Map<Projectile, Integer> arrowDamage = new HashMap<>();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRpgPlayerQuit(RpgPlayerQuitEvent ev) {
        PlayerManager.remove(ev.getPlayer().getBasePlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRpgPlayerJoin(RpgPlayerJoinEvent ev) {
        SpecializationManager.applyOwnedSpecializations(ev.getPlayer());
        ChannelManager.handleAutojoins(ev.getPlayer());

        ev.getPlayer().setActiveChannel();

        FileConfiguration conf = YamlConfiguration.loadConfiguration(new File(Directories.DATA + "config.yml"));

        if (conf.isConfigurationSection("config.spawn") && !ev.getPlayer().hasPlayedBefore())
            ev.getPlayer().getBasePlayer().teleport(new Location(Bukkit.getWorld(conf.getString("config.spawn.world")), conf.getDouble("config.spawn.x"), conf.getDouble("config.spawn.y"), conf.getDouble("config.spawn.z"), (float)conf.getDouble("config.spawn.yaw"), (float)conf.getDouble("config.spawn.pitch")));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRpgBlockBreak(RpgBlockBreakEvent ev) {
	RpgPlayer player = ev.getPlayer();

        if (player == null) {
            player.sendMessage("{red}Error: {aqua}Your player data has not been initialized yet. Please wait a few more moments.");

            return;
        }

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

        if (player == null) {
            player.sendMessage("{red}Error: {aqua}Your player data has not been initialized yet. Please wait a few more moments.");

            return;
        }

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

        if (player == null) {
            player.sendMessage("{red}Error: {aqua}Your player data has not been initialized yet. Please wait a few more moments.");

            return;
        }

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

        if (player == null) {
            player.sendMessage("{red}Error: {aqua}Your player data has not been initialized yet. Please wait a few more moments.");

            return;
        }

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
            int damage = ev.getDamage();

            switch (ev.getDamageType()) {
                case PHYSICAL:
                    damage += player.getStat(StatType.STRENGTH).getValue();

                    break;
                case MAGICAL:
                    damage += player.getStat(StatType.MAGIC).getValue();

                    break;
            }

            ev.setDamage(Math.max(1, damage));
	}
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRpgPlayerDamageByPlayer(RpgPlayerDamageByPlayerEvent ev) {
        RpgPlayer player = ev.getPlayer();
        RpgPlayer damager = ev.getDamager();

        if (player == null) {
            player.sendMessage("{red}Error: {aqua}Your player data has not been initialized yet. Please wait a few more moments.");

            return;
        }

        if (damager == null) {
            damager.sendMessage("{red}Error: {aqua}Your player data has not been initialized yet. Please wait a few more moments.");

            return;
        }

        if (!damager.isAllowed(PermitNode.USE, ev.getWeapon())) {
            ev.setCancelled(true);

            if (PhraseManager.phraseExists("player.use-deny")) {
                Map<String, String> args = new HashMap<>();
                String item = ev.getWeapon().toLowerCase();

                args.put("item", (Util.isUncountable(item) ? "" : "a" + (Util.isVowel(item.charAt(0)) ? "n" : "")) + ' ' + item.replace('_', ' '));

                damager.sendEventMessage(PhraseManager.getPhrase("player.use-deny"), args);
            }
        }

        if (!ev.isCancelled()) {
            int damage = ev.getDamage();

            switch (ev.getDamageType()) {
                case PHYSICAL:
                    damage = damage + (damager.getStat(StatType.STRENGTH).getValue() - player.getStat(StatType.ARMOR).getValue());

                    break;
                case MAGICAL:
                    damage = damage + (damager.getStat(StatType.MAGIC).getValue() - player.getStat(StatType.AURA).getValue());

                    break;
            }

            ev.setDamage(Math.max(1, damage));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRpgPlayerDamageByBlock(RpgPlayerDamageByBlockEvent ev) {
        RpgPlayer player = ev.getPlayer();
        int damage = ev.getDamage();

        if (player == null) {
            player.sendMessage("{red}Error: {aqua}Your player data has not been initialized yet. Please wait a few more moments.");

            return;
        }

        switch (ev.getDamageType()) {
            case PHYSICAL:
                damage -= player.getStat(StatType.ARMOR).getValue();

                break;
            case MAGICAL:
                damage -= player.getStat(StatType.AURA).getValue();

                break;
        }

        ev.setDamage(Math.max(1, damage));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRpgPlayerDamageByEntity(RpgPlayerDamageByEntityEvent ev) {
        RpgPlayer player = ev.getPlayer();
        int damage = ev.getDamage();

        if (ev.getDamager() instanceof Projectile && arrowDamage.containsKey((Projectile)ev.getDamager()))
            damage += arrowDamage.get((Projectile)ev.getDamager());

        switch (ev.getDamageType()) {
            case PHYSICAL:
                damage -= player.getStat(StatType.ARMOR).getValue();

                break;
            case MAGICAL:
                damage -= player.getStat(StatType.MAGIC).getValue();

                break;
        }

        ev.setDamage(Math.max(1, damage));
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

        ev.setDamage(Math.max(1, damage));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRpgPlayerSpawn(RpgPlayerSpawnEvent ev) {
        ev.getPlayer().setHealth(ev.getPlayer().getMaxHealth());

        final RpgPlayer player = ev.getPlayer();

        if (player == null) {
            player.sendMessage("{red}Error: {aqua}Your player data has not been initialized yet. Please wait a few more moments.");

            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(SolusRpg.getInstance(), new Runnable() {
            public void run() {
                player.addExp(0);
            }
        }, 5);

        FileConfiguration conf = YamlConfiguration.loadConfiguration(new File(Directories.DATA + "config.yml"));

        if (conf.isConfigurationSection("config.spawn"))
            ev.setSpawnLocation(new Location(Bukkit.getWorld(conf.getString("config.spawn.world")), conf.getDouble("config.spawn.x"), conf.getDouble("config.spawn.y"), conf.getDouble("config.spawn.z"), (float)conf.getDouble("config.spawn.yaw"), (float)conf.getDouble("config.spawn.pitch")));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRpgPlayerExpChange(RpgPlayerExpChangeEvent ev) {
        if (LevelManager.getLevelCap() != -1 && ev.getPlayer().getLevel() >= LevelManager.getLevelCap())
            ev.setAmount(0);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRpgInventoryOpen(RpgInventoryOpenEvent ev) {
        ev.getPlayer().setActiveInventoryType(ev.getInventory().getType());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRpgInventoryClose(RpgInventoryCloseEvent ev) {
        RpgPlayer player = ev.getPlayer();

        if (player == null) {
            player.sendMessage("{red}Error: {aqua}Your player data has not been initialized yet. Please wait a few more moments.");

            return;
        }

        ItemStack[] armor = player.getInventory().getArmorContents();

        for (int i = 0; i < armor.length; i++) {
            if (armor[i] == null || armor[i].getType() == Material.AIR)
                continue;

            String item = Util.getItemName(armor[i].getType(), armor[i].getData().getData());

            if (!player.isAllowed(PermitNode.WEAR, item)) {
                player.getWorld().dropItem(player.getLocation(), armor[i]);

                armor[i] = null;

                if (PhraseManager.phraseExists("player.wear-deny")) {
                    Map<String, String> args = new HashMap<>();

                    args.put("item", (Util.isUncountable(item) ? "" : "a" + (Util.isVowel(item.charAt(0)) ? "n " : " ")) + item.replace('_', ' ').toLowerCase());

                    player.sendEventMessage(PhraseManager.getPhrase("player.wear-deny"), args);
                }
            }
        }

        player.getInventory().setArmorContents(armor);
        player.setActiveInventoryType(null);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRpgPlayerShootBow(RpgPlayerShootBowEvent ev) {
        arrowDamage.put(ev.getProjectile(), ev.getPlayer().getStat(StatType.STRENGTH).getValue());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRpgPlayerChat(RpgPlayerChatEvent ev) {
        ev.setCancelled(!ev.getChannel().canChat(ev.getPlayer()));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void finalOnRpgPlayerChat(RpgPlayerChatEvent ev) {
        if (!ev.isCancelled())
            ev.getChannel().sendMessage(ev.getPlayer(), ev.getMessage());
    }
}