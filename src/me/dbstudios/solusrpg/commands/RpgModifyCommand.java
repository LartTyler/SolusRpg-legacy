/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.commands;

import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.managers.PlayerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgModifyCommand {
    public static boolean onCommand(CommandSender sender, Command command, String... args) {
	if (sender instanceof Player)
	    return RpgModifyCommand.modifyPlayer(PlayerManager.get((Player)sender), command, args);

	return false;
    }

    private static boolean modifyPlayer(RpgPlayer sender, Command command, String... args) {


	return false;
    }
}
