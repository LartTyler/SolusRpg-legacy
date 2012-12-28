/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.commands;

import java.util.regex.Pattern;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgListCommand {
    public static boolean onCommand(CommandSender sender, Command command, String[] args) {
        if (Pattern.matches("(?i)^class(es)?$", args[1])) {

        } else if (Pattern.matches("(?i)^skill(s)?$", args[1])) {

        }

        return false;
    }
}
