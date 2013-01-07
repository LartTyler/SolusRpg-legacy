/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.event.inventory;

import me.dbstudios.solusrpg.util.Util;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.Recipe;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgCraftItemEvent extends RpgInventoryClickEvent {
    private final Recipe recipe;
    private final String recipeResult;

    public RpgCraftItemEvent(InventoryView view, SlotType slotType, int rawSlot, boolean right, boolean shift, Recipe recipe) {
        super(view, slotType, rawSlot, right, shift);

        this.recipe = recipe;
        this.recipeResult = Util.getItemName(recipe.getResult().getType(), recipe.getResult().getData().getData());
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public String getRecipeResult() {
        return this.recipeResult;
    }

    public CraftingInventory getInventory() {
        return (CraftingInventory)super.getInventory();
    }
}
