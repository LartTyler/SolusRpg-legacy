/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.commands;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.entities.conf.RpgClass;
import me.dbstudios.solusrpg.managers.ClassManager;
import me.dbstudios.solusrpg.managers.LevelManager;
import me.dbstudios.solusrpg.managers.PlayerManager;
import me.dbstudios.solusrpg.util.Directories;
import me.dbstudios.solusrpg.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgModifyCommand {
    public static boolean onCommand(CommandSender sender, Command command, String... args) {
	switch (args.length) {
	    case 1:
		// This will open the GUI. Not high priority ATM, so we'll put it off.

		break;
	    case 2:
		// This would also launch the GUI, with it pre-loaded to the second argument. Again, low priority, so this is not used.

                break;
            default:
                List<Player> potentialPlayers = Bukkit.matchPlayer(args[1]);

                if (!potentialPlayers.isEmpty()) {
                    RpgPlayer target = PlayerManager.get(potentialPlayers.get(0));
                    Boolean success = null;
                    String val = "";

                    for (int i = 4; i < args.length; i++)
                        val += " " + args[i];

                    if (args[2].equalsIgnoreCase("set") && args.length >= 5)
                        success = target.modify(args[3], val.substring(1));
                    else if (args[2].equalsIgnoreCase("remove") && args.length == 4)
                        success = target.modify(args[3], null);

                    if (success == null)
                        return false;

                    try {
                        Scanner s = new Scanner(new File(Directories.CONFIG + "player_info_format.dat"));

                        Map<String, String> arguments = new HashMap<>();

                        arguments.put("target", target.getName());
                        arguments.put("operation", args[2].equalsIgnoreCase("set") ? Util.format("Set {0} = {1}", args[3], val) : Util.format("Removed {0}", args[3]));

                        while (s.hasNextLine()) {
                            String[] lineSplit = s.nextLine().split("[^\\\\]#");

                            if (lineSplit[0].length() > 0 && !lineSplit[0].startsWith("#"))
                                Util.sendMessage(sender, lineSplit[0].replace("\\#", "#"), arguments);
                        }
                    } catch (IOException e) {
                        SolusRpg.log(Level.WARNING, "Could not locate file: " + Directories.CONFIG + "player_info_format.dat");
                        Util.sendMessage(sender, "{red}An error occurred while attempting to execute your command.", null);
                    }
                } else {
                    List<RpgClass> potentialClasses = ClassManager.matchClass(args[1]);

                    if (!potentialClasses.isEmpty()) {
                        RpgClass target = potentialClasses.get(0);
                    }
                }
	}

	return true;
    }
}
