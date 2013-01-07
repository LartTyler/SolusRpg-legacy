/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.event.inventory;

import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.managers.PlayerManager;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.InventoryView;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgInventoryOpenEvent extends RpgInventoryEvent implements Cancellable {
    private final RpgPlayer player;

    private boolean cancelled = false;

    public RpgInventoryOpenEvent(InventoryView view) {
        super(view);

        this.player = PlayerManager.get(view.getPlayer().getUniqueId());
    }

    public RpgPlayer getPlayer() {
        return this.player;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
