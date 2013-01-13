
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
import me.dbstudios.solusrpg.entities.conf.PermitNode;
import me.dbstudios.solusrpg.entities.conf.Stat;
import me.dbstudios.solusrpg.entities.conf.StatType;
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
 * @author Tyler Lartonoix
 */
public class RpgRespecCommand {
    public static boolean onCommand(CommandSender sender, Command command, String... args) {
        List<Player> potentialPlayers = Bukkit.matchPlayer(args[1]);

        if (!PermissionManager.hasPermission(sender, "player.respec")) {
            Util.sendMessage(sender, "{red}Error: {aqua}You do not have sufficient permissions to perform this command.");
        } else {
            if (!potentialPlayers.isEmpty()) {
                RpgPlayer target = PlayerManager.get(potentialPlayers.get(0));
                int skillPoints = (target.getLevel() - 1) * LevelManager.getSkillPointsPerLevel() + LevelManager.getStartingSkillPoints();
                double healthPercentage = Math.max((int)Math.ceil((double)target.getHealth() / (double)target.getMaxHealth()), 1.0);
                
                target.setSkillPoints(skillPoints);

                for (StatType t : StatType.values())
                    target.setStat(t, new Stat(0, t));

                for (PermitNode n : PermitNode.values())
                    target.resetAllowed(n);

                target.setHealth((int)Math.ceil((double)target.getMaxHealth() * healthPercentage));
                target.clearMetadata();

                try {
                    Scanner s = new Scanner(new File(Directories.CONFIG + "respec_result_format.dat"));

                    Map<String, String> arguments = new HashMap<>();

                    arguments.put("target", target.getName());

                    while (s.hasNextLine()) {
                        String[] lineSplit = s.nextLine().split("[^\\\\]#");

                        if (lineSplit[0].length() > 0 && !lineSplit[0].startsWith("#"))
                            Util.sendMessage(sender, lineSplit[0].replace("\\#", "#"), arguments);
                    }
                } catch (IOException e) {
                    SolusRpg.log(Level.WARNING, "Could not locate file: " + Directories.CONFIG + "modify_result_format.dat");
                    Util.sendMessage(sender, "{red}An error occurred while attempting to execute your command.", null);
                }
            } else {
                Util.sendMessage(sender, "{red}Error: {aqua}Could not match player '{green}" + args[1] + "{aqua}'.");
            }
        }

        return true;
    }
}
