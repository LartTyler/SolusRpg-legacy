/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.commands;

import me.dbstudios.solusrpg.chat.ChatChannel;
import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.managers.ChannelManager;
import me.dbstudios.solusrpg.managers.PermissionManager;
import me.dbstudios.solusrpg.managers.PlayerManager;
import me.dbstudios.solusrpg.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgChatCommand {
    public static boolean onCommand(CommandSender sender, Command command, String... args) {
        switch (args.length) {
            case 2:
                if (args[1].equalsIgnoreCase("list") && PermissionManager.hasPermission(sender, "chat.list", true)) {
                    Util.sendMessage(sender, "{aqua}Available channels:");

                    for (ChatChannel c : ChannelManager.getChannels())
                        if ((c.isPrivateChannel() || c.isHiddenChannel()) && PermissionManager.hasAnyPermission(sender, "chat.moderate." + c.getSystemName(), "chat.list-hidden"))
                            Util.sendMessage(sender, "  {aqua}- {green}" + c.getName());
                        else if (!c.isPrivateChannel() && !c.isHiddenChannel())
                            Util.sendMessage(sender, "  {aqua}- {green}" + c.getName());
                } else if (args[1].equalsIgnoreCase("leave") && sender instanceof Player) {
                    RpgPlayer player = PlayerManager.get((Player)sender);

                    if (player.getActiveChannel() != null)
                        if (player.getActiveChannel().canLeave(player))
                            player.leaveChannel();
                        else
                            Util.sendMessage(sender, "{aqua}You do not have sufficient permissions to leave {green}" + player.getActiveChannel().getName() + "{aqua}.");
                    else
                        Util.sendMessage(sender, "{aqua}You cannot leave a channel if you aren't in any.");
                } else {
                    return false;
                }

                break;
            case 3:
                if (!(sender instanceof Player)) {
                    Util.sendMessage(sender, "This command may not be sent from the console.");

                    return true;
                }

                RpgPlayer player = PlayerManager.get((Player)sender);

                if (args[1].equalsIgnoreCase("join")) {
                    ChatChannel c = ChannelManager.matchChannel(args[2]);

                    if (c != null) {
                        if (c.canJoin(player))
                            player.joinChannel(c);
                        else
                            Util.sendMessage(sender, "{aqua}You do not have sufficient permissions to join {green}" + c.getName() + "{aqua}.");
                    } else {
                        Util.sendMessage(sender, "{aqua}No channel could be found matching '{green}" + args[2] + "{aqua}'.");
                    }
                } else if (args[1].equalsIgnoreCase("focus")) {
                    ChatChannel c = ChannelManager.matchChannel(args[2]);

                    if (c != null)
                        player.setActiveChannel(c.getName());
                    else
                        Util.sendMessage(sender, "{aqua}No channel could be found matching '{green}" + args[2] + "{aqua}'.");
                } else if (args[1].equalsIgnoreCase("leave")) {
                    ChatChannel c = ChannelManager.matchChannel(args[2]);

                    if (c != null)
                        if (c.canLeave(player))
                            player.leaveChannel(c);
                        else
                            Util.sendMessage(sender, "{aqua}No channel could be found matching '{green}" + args[2] + "{aqua}'.");
                } else if (args[1].equalsIgnoreCase("kick")) {
                    RpgPlayer target = PlayerManager.matchPlayer(args[2]);
                    ChatChannel active = player.getActiveChannel();

                    if (target != null && active.isInChannel(target) && active.canKick(sender, target))
                        active.kickMember(target);
                    else if (target == null)
                        Util.sendMessage(sender, "{aqua}Could not match player with the name '{green}" + args[2] + "{aqua}'.");
                    else
                        Util.sendMessage(sender, "{aqua}You do not have sufficient permissions to ban {green}" + args[2] + " {aqua}from {green}" + active.getName() + "{aqua}.");
                } else if (args[1].equalsIgnoreCase("ban")) {
                    RpgPlayer target = PlayerManager.matchPlayer(args[2]);
                    ChatChannel active = player.getActiveChannel();

                    if (target != null && active.canBan(sender, target))
                        active.banMember(target);
                    else if (target == null)
                        Util.sendMessage(sender, "{aqua}Could not match player with the name '{green}" + args[2] + "{aqua}'.");
                    else
                        Util.sendMessage(sender, "{aqua}You do not have sufficient permissions to ban {green}" + args[2] + " {aqua}from {green}" + active.getName() + "{aqua}.");
                } else if (args[1].equalsIgnoreCase("pardon")) {
                    RpgPlayer target = PlayerManager.matchPlayer(args[2]);
                    ChatChannel active = player.getActiveChannel();

                    if (target != null && active.canPardon(player))
                        active.pardonMember(target);
                    else if (target == null)
                        Util.sendMessage(sender, "{aqua}Could not match play with the name '{green}" + args[2] + "{aqua}.");
                    else
                        Util.sendMessage(sender, "{aqua}You do not have sufficient permissions to pardon {green}" + args[2] + " {aqua}from {green}" + active.getName() + "{aqua}.");
                } else {
                    return false;
                }

                break;
        }

        return true;
    }
}
