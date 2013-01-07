/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.entities.conf.PermitNode;
import me.dbstudios.solusrpg.entities.conf.RpgClass;
import me.dbstudios.solusrpg.entities.conf.StatType;
import me.dbstudios.solusrpg.managers.ClassManager;
import me.dbstudios.solusrpg.managers.LevelManager;
import me.dbstudios.solusrpg.managers.PermissionManager;
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
public class RpgInfoCommand {
    public static boolean onCommand(CommandSender sender, Command command, String... args) {
        List<Player> potentialMatches = Bukkit.matchPlayer(args[1]);

        if (!potentialMatches.isEmpty()) {
            if (!PermissionManager.hasPermission(sender, "dbstudios.solusrpg.info.player", false)) {
                Util.sendMessage(sender, "{dark_red}Error: {aqua}You do not have sufficient permissions to perform this command.");
            } else {
                RpgPlayer target = PlayerManager.get(potentialMatches.get(0));

                try {
                    Scanner s = new Scanner(new File(Directories.CONFIG + "player_info_format.dat"));

                    Map<String, String> arguments = new HashMap<>();

                    arguments.put("display-name", target.getDisplayName());
                    arguments.put("name", target.getName());
                    arguments.put("class", target.getRpgClass().getName());
                    arguments.put("health-name", target.getHealthMeter().getMeterName());
                    arguments.put("health", target.getHealth() + "");
                    arguments.put("max-health", target.getMaxHealth() + "");
                    arguments.put("level", target.getLevel() + "");
                    arguments.put("experience", target.getExp() + "");
                    arguments.put("experience-to-level", LevelManager.getExpToLevel(target.getLevel() + 1) + "");

                    while (s.hasNextLine()) {
                        String[] lineSplit = s.nextLine().split("[^\\\\]#");

                        if (lineSplit[0].length() > 0 && !lineSplit[0].startsWith("#"))
                            Util.sendMessage(sender, lineSplit[0].replace("\\#", "#"), arguments);
                    }
                } catch (FileNotFoundException e) {
                    SolusRpg.log(Level.WARNING, "Could not locate file: " + Directories.CONFIG + "player_info_format.dat");
                    Util.sendMessage(sender, "{red}An error occurred while attempting to execute your command.", null);
                }
            }
        } else {
            if (!PermissionManager.hasPermission(sender, "dbstudios.solusrpg.info.class", true)) {
                Util.sendMessage(sender, "{dark_red}Error: {aqua}You do not have sufficient permissions to perform this command.");
            } else {
                List<RpgClass> potentialClasses = ClassManager.matchClass(args[1]);

                if (!potentialClasses.isEmpty()) {
                    RpgClass target = potentialClasses.get(0);

                    try {
                        Scanner s = new Scanner(new File(Directories.CONFIG + "class_info_format.dat"));

                        Map<String, String> arguments = new HashMap<>();

                        arguments.put("name", target.getName());
                        arguments.put("bio", target.getBio());
                        arguments.put("health-name", target.getConfiguration().getString("class.stats.health.name", "Health"));

                        for (PermitNode n : PermitNode.values()) {
                            String list = "";

                            for (String el : Util.toTypedList(target.getConfiguration().getList("class." + n, null), String.class)) {
                                String value = "";

                                for (String seg : el.charAt(0) == '@' ? el.substring(1).split("_") : el.split("_"))
                                    value += " " + seg.substring(0, 1).toUpperCase() + seg.substring(1).toLowerCase();

                                list += ", " + value.substring(1);
                            }

                            arguments.put(n.getNode(), list.length() > 0 ? list.substring(2) : "Nothing");
                        }

                        for (StatType t : StatType.values())
                            arguments.put(t.name(), target.getStat(t).getValue() + "");

                        while (s.hasNextLine()) {
                            String[] lineSplit = s.nextLine().split("[^\\\\]#");

                            if (lineSplit[0].length() > 0 && !lineSplit[0].startsWith("#"))
                                Util.sendMessage(sender, lineSplit[0].replace("\\#", "#"), arguments);
                        }
                    } catch (FileNotFoundException e) {
                        SolusRpg.log(Level.WARNING, "Could not locate file: " + Directories.CONFIG + "class_info_format.dat");
                    }
                } else {
                    Util.sendMessage(sender, "{aqua}Could not find any class or player matching '{green}" + args[1] + "'{aqua}.");
                }
            }
        }

	return true;
    }
}
