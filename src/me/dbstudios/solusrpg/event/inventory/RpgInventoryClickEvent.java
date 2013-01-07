/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.event.inventory;

import me.dbstudios.solusrpg.util.Util;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.inventory.SpoutItemStack;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgInventoryClickEvent extends RpgInventoryEvent implements Cancellable {
    private final SlotType slotType;
    private final int slot, rawSlot;
    private final boolean right, shift;

    private Result result = Result.DEFAULT;

    private boolean cancelled = false;

    public RpgInventoryClickEvent(InventoryView view, SlotType slotType, int rawSlot, boolean right, boolean shift) {
        super(view);

        this.slotType = slotType;
        this.slot = view.convertSlot(rawSlot);
        this.rawSlot = rawSlot;
        this.right = right;
        this.shift = shift;
    }

    public SpoutItemStack getItem() {
        if (slotType == SlotType.OUTSIDE)
            return this.getCursor();

        ItemStack item = super.getView().getItem(this.rawSlot);

        return item == null ? null : new SpoutItemStack(item);
    }

    public String getItemName() {
        return Util.getItemName(this.getItem().getType(), this.getItem().getData().getData());
    }

    public SpoutItemStack getCursor() {
        ItemStack item = super.getView().getCursor();

        return item == null ? null : new SpoutItemStack(item);
    }

    public int getRawSlot() {
        return this.rawSlot;
    }

    public Result getResult() {
        return this.result;
    }

    public int getSlot() {
        return this.slot;
    }

    public SlotType getSlotType() {
        return this.slotType;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
        this.result = cancelled ? Result.DENY : Result.DEFAULT;
    }

    public boolean isLeftClick() {
        return !this.right;
    }

    public boolean isRightClick() {
        return this.right;
    }

    public boolean isShiftClick() {
        return this.shift;
    }

    public void setCurrentItem(ItemStack item) {
        super.getView().setItem(rawSlot, item);
    }

    public void setCursor(ItemStack item) {
        super.getView().setCursor(item);
    }
}
