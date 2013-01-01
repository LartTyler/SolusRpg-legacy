
package me.dbstudios.solusrpg.event.player;

import org.bukkit.entity.Player;

/**
 * @author Tyler Lartonoix
 */
public class RpgPlayerExpChangeEvent extends RpgPlayerEvent {
    private int amount;

    public RpgPlayerExpChangeEvent(Player player, int amount) {
        super(player);

        this.amount = amount;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
