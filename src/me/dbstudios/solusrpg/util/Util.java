
package me.dbstudios.solusrpg.util;

import java.util.ArrayList;
import java.util.List;
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
    
    public static String getItemName(Block block) {
	String[] items = RpgConstants.ITEM_NAME_MAP.get(block.getType());
	
	if (block.getData() < items.length)
	    return items[block.getData()];
	else
	    return block.getType().name();
    }
}
