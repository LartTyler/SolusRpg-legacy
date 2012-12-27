/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.commands;

import java.util.List;
import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.entities.conf.RpgClass;
import me.dbstudios.solusrpg.managers.ClassManager;
import me.dbstudios.solusrpg.managers.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgInfoCommand {
    public static boolean onCommand(CommandSender sender, Command command, String... args) {
        List<Player> potentialMatches = Bukkit.matchPlayer(args[1]);

        if (!potentialMatches.isEmpty()) {
            RpgPlayer target = PlayerManager.get(potentialMatches.get(0));

            // TODO: Display player stats
        } else {
            List<RpgClass> potentialClasses = ClassManager.matchClass(args[1]);

            if (!potentialClasses.isEmpty()) {
                RpgClass target = potentialClasses.get(0);

                // TODO: Display class stats
            }
        }

	return false;
    }
}
