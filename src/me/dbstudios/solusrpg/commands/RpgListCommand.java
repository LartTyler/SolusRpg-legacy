/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.regex.Pattern;
import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.entities.conf.RpgClass;
import me.dbstudios.solusrpg.managers.ClassManager;
import me.dbstudios.solusrpg.managers.PermissionManager;
import me.dbstudios.solusrpg.util.Directories;
import me.dbstudios.solusrpg.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgListCommand {
    public static boolean onCommand(CommandSender sender, Command command, String[] args) {
        if (Pattern.matches("(?i)^class(es)?$", args[1])) {
            if (PermissionManager.hasPermission(sender, "dbstudios.solusrpg.list.class", true)) {
                String classList = "";

                for (RpgClass cl : ClassManager.getClasses())
                    if (!cl.isPrivate() || PermissionManager.hasPermission(sender, "dbstudios.solusrpg.list.view-private", false))
                        classList += (classList.length() == 0 ? "" : "\n") + "{aqua}- {green}" + cl.getName();

                if (classList.length() == 0)
                    classList = "{aqua}- No classes available";

                Map<String, String> arguments = new HashMap<>();

                arguments.put("class-list", classList);

                try {
                    Scanner s = new Scanner(new File(Directories.CONFIG + "class_list_format.dat"));

                    while (s.hasNextLine()) {
                        String[] lineSplit = s.nextLine().split("[^\\\\]#");

                        if (lineSplit[0].length() > 0 && !lineSplit[0].startsWith("#"))
                            Util.sendMessage(sender, lineSplit[0].replace("\\#", "#"), arguments);
                    }
                } catch(FileNotFoundException e) {
                    SolusRpg.log(Level.WARNING, "Could not locate file: " + Directories.CONFIG + "class_list_command.dat");
                    Util.sendMessage(sender, "{red}An error occurred while attempting to execute your command.", null);
                }
            } else {
                Util.sendMessage(sender, "{red}Error: {aqua}You do not have sufficient permissions to perform this command.");
            }
        } else if (Pattern.matches("(?i)^skill(s)?$", args[1])) {
            Util.sendMessage(sender, "{red}Error: {aqua}This command has not been implemeted yet.");
        }

        return true;
    }
}
