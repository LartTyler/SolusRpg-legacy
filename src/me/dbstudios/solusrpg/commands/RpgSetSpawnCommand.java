
package me.dbstudios.solusrpg.commands;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.managers.PermissionManager;
import me.dbstudios.solusrpg.util.Directories;
import me.dbstudios.solusrpg.util.Util;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 * @author Tyler Lartonoix
 */
public class RpgSetSpawnCommand {
    public static boolean onCommand(CommandSender sender, Command command, String... args) {
        if (!PermissionManager.hasPermission(sender, "set.spawn", false)) {
            Util.sendMessage(sender, "{red}Error: {aqua}You do not have sufficient permissions to perform this command.");
        } else if (!(sender instanceof Player)) {
            Util.sendMessage(sender, "Error: Only a player can execute this command.");
        } else {
            FileConfiguration conf = YamlConfiguration.loadConfiguration(new File(Directories.DATA + "config.yml"));
            Location l = ((Player)sender).getLocation();

            conf.set("config.spawn.world", l.getWorld().getName());
            conf.set("config.spawn.x", l.getX());
            conf.set("config.spawn.y", l.getY());
            conf.set("config.spawn.z", l.getZ());
            conf.set("config.spawn.pitch", l.getPitch());
            conf.set("config.spawn.yaw", l.getYaw());

            try {
                conf.save(new File(Directories.DATA + "config.yml"));
            } catch (IOException e) {
                SolusRpg.log(Level.WARNING, "Could not save spawn point.");
            }

            Util.sendMessage(sender, "{aqua}Spawn point updated to your current location.");
        }

        return true;
    }
}
