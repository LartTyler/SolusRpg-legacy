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

                while (s.hasNextLine())
                    Util.sendMessage(sender, s.nextLine(), arguments);
            } catch (FileNotFoundException e) {
                SolusRpg.log(Level.WARNING, "Could not locate file: " + Directories.CONFIG + "player_info_format.dat");
            }
        } else {
            List<RpgClass> potentialClasses = ClassManager.matchClass(args[1]);

            if (!potentialClasses.isEmpty()) {
                RpgClass target = potentialClasses.get(0);

                try {
                    Scanner s = new Scanner(new File(Directories.CONFIG + "player_info_format.dat"));

                    Map<String, String> arguments = new HashMap<>();

                    arguments.put("name", target.getName());
                    arguments.put("bio", target.getBio());

                    for (PermitNode n : PermitNode.values()) {
                        String list = "";

                        for (String el : Util.toTypedList(target.getConfiguration().getList("class." + n, null), String.class))
                            list += ", " + (el.charAt(0) == '@' ? el.substring(1) : el);

                        arguments.put(n.getNode(), list.substring(2));
                    }

                    for (StatType t : StatType.values())
                        arguments.put(t.name(), target.getStat(t).getValue() + "");

                    while (s.hasNextLine())
                        Util.sendMessage(sender, s.nextLine(), arguments);
                } catch (FileNotFoundException e) {
                    SolusRpg.log(Level.WARNING, "Could not locate file: " + Directories.CONFIG + "class_info_format.dat");
                }
            }
        }

	return false;
    }
}
