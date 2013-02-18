/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.event.player;

import me.dbstudios.solusrpg.social.chat.ChatChannel;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgPlayerChatEvent extends RpgPlayerEvent implements Cancellable {
    private String msg;
    private boolean cancelled = false;

    public RpgPlayerChatEvent(Player player, String msg) {
        super(player);

        this.msg = msg;
    }

    public ChatChannel getChannel() {
        return super.getPlayer().getActiveChannel();
    }

    public String getMessage() {
        return this.msg;
    }

    public void setMessage(String msg) {
        this.msg = msg;
    }

    public void setChannel(ChatChannel channel) {
        this.setChannel(channel.getSystemName());
    }

    public void setChannel(String channel) {
        super.getPlayer().setActiveChannel(channel);
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
