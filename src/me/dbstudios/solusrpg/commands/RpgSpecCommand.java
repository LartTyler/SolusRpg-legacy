
package me.dbstudios.solusrpg.commands;

import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.gui.SpecializationPopup;
import me.dbstudios.solusrpg.managers.PlayerManager;
import me.dbstudios.solusrpg.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Tyler Lartonoix
 */
public class RpgSpecCommand {
    public static boolean onCommand(CommandSender sender, Command command, String... args) {
        if (sender instanceof Player) {
            RpgPlayer player = PlayerManager.get((Player)sender);

            player.getBasePlayer().getMainScreen().attachPopupScreen(new SpecializationPopup(player));
        } else {
            Util.sendMessage(sender, "Only players can execute this command.");
        }

        return true;
    }
}
