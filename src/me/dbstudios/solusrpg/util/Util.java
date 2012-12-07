
package me.dbstudios.solusrpg.util;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.getspout.spoutapi.SpoutWorld;
import org.getspout.spoutapi.block.SpoutBlock;

/**
 * @author Tyler Lartonoix
 */
public class Util {
    public static String format(String str, Object... args) {
        for (int i = 0; i < args.length; i++)
            str = str.replace("{" + i + "}", args[i].toString());

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

    public static SpoutBlock toSpoutBlock(Block block) {
	return (new SpoutWorld(block.getWorld())).getBlockAt(block.getLocation());
    }

    public static String getItemName(Material type, byte data) {
	String[] items = RpgConstants.ITEM_NAME_MAP.get(type);

	if (data < items.length)
	    return items[data];
	else
	    return type.name();
    }
}
