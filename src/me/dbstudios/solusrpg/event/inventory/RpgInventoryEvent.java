/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.event.inventory;

import java.util.ArrayList;
import java.util.List;
import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.managers.PlayerManager;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgInventoryEvent extends Event {
    public static HandlerList handlers = new HandlerList();

    private final InventoryView view;
    private final RpgPlayer clicker;

    public RpgInventoryEvent(InventoryView view) {
        this.view = view;
        this.clicker = PlayerManager.get(view.getPlayer().getUniqueId());
    }

    public InventoryView getView() {
        return this.view;
    }

    public Inventory getInventory() {
        return view.getTopInventory();
    }

    public List<HumanEntity> getViewers() {
        return view.getTopInventory().getViewers();
    }

    public List<RpgPlayer> getRpgViewers() {
        List<RpgPlayer> players = new ArrayList<>();

        for (HumanEntity e : this.getViewers())
            if (PlayerManager.exists(e.getUniqueId()))
                players.add(PlayerManager.get(e.getUniqueId()));

        return players;
    }

    public RpgPlayer getClicker() {
        return this.clicker;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
