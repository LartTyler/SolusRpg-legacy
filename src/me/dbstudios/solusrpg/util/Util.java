
package me.dbstudios.solusrpg.util;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Pattern;
import me.dbstudios.solusrpg.SolusRpg;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutWorld;
import org.getspout.spoutapi.block.SpoutBlock;

/**
 * @author Tyler Lartonoix
 */
public class Util {
    public static String format(String str, Object... args) {
        for (int i = 0; i < args.length; i++)
            str = str.replace("{" + i + "}", args[i] != null ? args[i].toString() : "null");

        return str;
    }

    public static <T> List<T> toTypedList(List<?> orig, Class<T> type) {
        List<T> list = new ArrayList<>();

        if (orig != null && !orig.isEmpty())
            for (Object o : orig)
                if (type.isInstance(o))
                    list.add(type.cast(o));

        return list;
    }

    public static <T, U> Map<T, U> toTypedMap(Map<?, ?> orig, Class<T> keyType, Class<U> valType) {
        Map<T, U> map = new HashMap<>();

        if (orig != null && !orig.isEmpty())
            for (Object key : orig.keySet())
                if (keyType.isInstance(key) && valType.isInstance(orig.get(key)))
                    map.put(keyType.cast(key), valType.cast(orig.get(key)));

        return map;
    }

    public static SpoutBlock toSpoutBlock(Block block) {
	return (new SpoutWorld(block.getWorld())).getBlockAt(block.getLocation());
    }

    public static String getItemName(Material type, byte data) {
	String[] items = RpgConstants.ITEM_NAME_MAP.get(type);

        switch (type) {
            case LEAVES:
                if (data >= 12)
                    data -= 12;

            case LOG:
                if (data >= 8)
                    data -= 8;

                if (data >= 4)
                    data -= 4;

            break;
        }

        if (items != null)
            if (data < items.length && data >= 0)
                return items[data];

        return type.name();
    }

    public static void extract(String resource, File dest) {
        (new File(dest.getPath().substring(0, dest.getPath().lastIndexOf(File.separator)))).mkdirs();

        try (
            InputStream in = Util.class.getResourceAsStream(resource);
            OutputStream out = new FileOutputStream(dest)
        ) {
            dest.createNewFile();

            int b;
            while ((b = in.read()) != -1)
                out.write(b);
        } catch (IOException e) {
            SolusRpg.log(Level.SEVERE, "Could not extract resource: {0}", resource);
        }
    }

    public static String getEntityName(Entity entity) {
        String name;

        if (entity instanceof Player) {
            name = ((Player)entity).getDisplayName();
        } else {
            name = entity.getType().getName();
        }

        return name;
    }

    public static void sendMessage(CommandSender sender, String msg) {
        Util.sendMessage(sender, msg, null);
    }

    public static void sendMessage(CommandSender sender, String msg, Map<String, String> args) {
        if (args != null)
            for (String key : args.keySet())
                msg = msg.replaceAll("(?i)\\{" + key + "\\}", args.get(key));

        for (ChatColor c : ChatColor.values())
            msg = msg.replaceAll("(?i)\\{" + c.name() + "\\}", c.toString());

//        msg = OutputFormatter.format(msg, args);

        sender.sendMessage(sender instanceof Player ? msg : ChatColor.stripColor(msg));
    }

    public static boolean isUncountable(String str) {
        if (str.endsWith("s"))
            return true;

        for (String s : RpgConstants.UNCOUNTABLE_MATERIALS)
            if (Pattern.matches("(?i)^" + s + "$", str))
                return true;

        return false;
    }

    public static boolean isVowel(char c) {
        c = Character.toLowerCase(c);

        if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u')
            return true;

        return false;
    }
}
