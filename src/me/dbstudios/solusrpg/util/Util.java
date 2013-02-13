
package me.dbstudios.solusrpg.util;

import bsh.EvalError;
import bsh.Interpreter;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Pattern;
import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.social.chat.ChatChannel;
import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.entities.conf.StatType;
import me.dbstudios.solusrpg.managers.ChannelManager;
import me.dbstudios.solusrpg.managers.LevelManager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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

    public static String getItemName(ItemStack item) {
        return item != null ? Util.getItemName(item.getType(), item.getData().getData()) : null;
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

    public static String prettifyString(String str, boolean ucWords) {
        String pretty = "";

        for (String s : str.split("_"))
            if (ucWords)
                pretty += " " + s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
            else
                pretty += " " + s.toLowerCase();

        return pretty.substring(1);
    }

    public static double getDistance(RpgPlayer a, RpgPlayer b) {
        return Util.getDistance(a.getLocation(), b.getLocation());
    }

    public static double getDistance(Location a, Location b) {
        return Math.sqrt(Math.pow(b.getX() - a.getX(), 2) + Math.pow(b.getY() - a.getY(), 2) + Math.pow(b.getZ() - a.getZ(), 2));
    }

    public static double getRangeFactor(ChatChannel channel, double distance) {
        return Util.getRangeFactor(channel.getRangeFactorAlgorithm(), channel.getRange(), distance);
    }

    public static double getRangeFactor(String algo, int range, double distance) {
        Interpreter i = new Interpreter();
        Object raw = null;

        try {
            raw = i.eval(algo.toLowerCase().replace("{range}", range + "").replace("{distance}", distance + ""));
        } catch (EvalError e) {
            SolusRpg.log(Level.WARNING, "Could not evaluate range factor algorithm '{0}'.", algo);
        }

        if (raw == null)
            try {
                raw = i.eval(ChannelManager.getRangeFactorAlgorithm().replace("{range}", range + "").replace("{distance}", distance + ""));
            } catch (EvalError e) {}

        return raw != null ? (double)raw : -1.0;
    }

    public static String buildPhrase(String phrase, RpgPlayer player) {
        return Util.buildPhrase(phrase, player, Util.buildPhraseArgs(player));
    }

    public static String buildPhrase(String phrase, RpgPlayer player, Map<String, String> args) {
        for (String key : args.keySet())
            phrase = phrase.replaceAll("(?i)\\{" + key + "\\}", args.get(key));

        return phrase;
    }

    public static Map<String, String> buildPhraseArgs(RpgPlayer player) {
        Map<String, String> args = new HashMap<>();

        args.put("x-coord", player.getLocation().getX() + "");
        args.put("y-coord", player.getLocation().getY() + "");
        args.put("z-coord", player.getLocation().getZ() + "");
        args.put("x-block-coord", player.getLocation().getBlockX() + "");
        args.put("y-block-coord", player.getLocation().getBlockY() + "");
        args.put("z-block-coord", player.getLocation().getBlockZ() + "");
        args.put("world", player.getWorld().getName());
        args.put("player", player.getDisplayName());
        args.put("player-name", player.getName());

        switch (player.getWorld().getEnvironment()) {
            case THE_END:
                args.put("dimension-symbol", "E");
                args.put("dimension-name", "The End");

                break;
            case NETHER:
                args.put("dimension-symbol", "N");
                args.put("dimension-name", "Nether");

                break;
            case NORMAL:
                args.put("dimension-symbol", "O");
                args.put("dimension-name", "Overworld");

                break;
        }

        switch (player.getGameMode()) {
            case ADVENTURE:
                args.put("gamemode-symbol", "A");
                args.put("gamemode-name", "Adventure");

                break;
            case CREATIVE:
                args.put("gamemode-symbol", "C");
                args.put("gamemode-name", "Creative");

                break;
            case SURVIVAL:
                args.put("gamemode-symbol", "S");
                args.put("gamemode-name", "Survival");

                break;
        }

        args.put("health", player.getHealth() + "");
        args.put("max-health", player.getMaxHealth() + "");

        for (StatType t : StatType.values())
            args.put(t.name().toLowerCase(), player.getStat(t).getValue() + "");

        args.put("channel-name", player.getActiveChannel().getName());
        args.put("channel-symbol", player.getActiveChannel().getSymbol());
        args.put("channel-sysname", player.getActiveChannel().getSystemName());
        args.put("item-in-hand", Util.prettifyString(Util.getItemName(player.getItemInHand()), true));
        args.put("headgear", Util.prettifyString(Util.getItemName(player.getInventory().getHelmet()), true));
        args.put("chestpiece", Util.prettifyString(Util.getItemName(player.getInventory().getChestplate()), true));
        args.put("leggings", Util.prettifyString(Util.getItemName(player.getInventory().getLeggings()), true));
        args.put("boots", Util.prettifyString(Util.getItemName(player.getInventory().getBoots()), true));
        args.put("level", player.getLevel() + "");
        args.put("exp", player.getExp() + "");
        args.put("exp-to-level", LevelManager.getExpToLevel(player.getLevel() + 1) + "");
        args.put("class-name", player.getRpgClass().getName());
        args.put("class-sysname", player.getRpgClass().getSystemName());

        return args;
    }
}
