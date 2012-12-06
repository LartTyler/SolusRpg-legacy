
package me.dbstudios.solusrpg.util;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import org.bukkit.Material;

/**
 * @author Tyler Lartonoix
 */
public class RpgConstants {
    public static final char ITEM_GROUP_IDENTIFIER = '@';
    public static final Map<Material, String[]> ITEM_NAME_MAP;
    
    static {
	Map<Material, String[]> nameMap = new EnumMap<>(Material.class);
	
	ITEM_NAME_MAP = Collections.unmodifiableMap(nameMap);
    }
}
