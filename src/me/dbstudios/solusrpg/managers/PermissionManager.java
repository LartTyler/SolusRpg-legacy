
package me.dbstudios.solusrpg.managers;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

/**
 * @author Tyler Lartonoix
 */
public class PermissionManager {
    private static final Permission permission;

    static {
        permission = Bukkit.getServicesManager().getRegistration(Permission.class).getProvider();
    }

    public static boolean hasPermission(CommandSender sender, String perm) {
        return PermissionManager.hasPermission(sender, perm, false);
    }

    public static boolean hasPermission(CommandSender sender, String perm, boolean def) {
        return sender.isOp() || permission.has(sender, "dbstudios.solusrpg." + perm) || def;
    }

    public static boolean hasAnyPermission(CommandSender sender, String... perms) {
        return PermissionManager.hasAnyPermission(sender, false, perms);
    }

    public static boolean hasAnyPermission(CommandSender sender, boolean def, String... perms) {
        for (String s : perms)
            if (PermissionManager.hasPermission(sender, s, def))
                return true;

        return false;
    }
}
