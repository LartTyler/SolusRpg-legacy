/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgCommandExecutor implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String... args) {
	if (args.length > 1 && args[0].equalsIgnoreCase("modify"))
	    return RpgModifyCommand.onCommand(sender, command, args);
        else if (args.length > 1 && args[0].equalsIgnoreCase("info"))
	    return RpgInfoCommand.onCommand(sender, command, args);
        else if (args.length > 1 && args[0].equalsIgnoreCase("list"))
            return RpgListCommand.onCommand(sender, command, args);
        else if (args.length == 1 && args[0].equalsIgnoreCase("spec"))
            return RpgSpecCommand.onCommand(sender, command, args);
        else if (args.length == 2 && args[0].equalsIgnoreCase("set") && args[1].equalsIgnoreCase("spawn"))
            return RpgSetSpawnCommand.onCommand(sender, command, args);

	return false;
    }
}
