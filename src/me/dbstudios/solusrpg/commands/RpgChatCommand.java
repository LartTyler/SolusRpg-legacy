/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.commands;

import me.dbstudios.solusrpg.managers.PermissionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgChatCommand {
    public static boolean onCommand(CommandSender sender, Command command, String... args) {
        switch (args.length) {
            case 2:
                if (args[1].equalsIgnoreCase("list") && PermissionManager.hasPermission(sender, "chat.list", true)) {
                    
                }
        }

        return true;
    }
}
