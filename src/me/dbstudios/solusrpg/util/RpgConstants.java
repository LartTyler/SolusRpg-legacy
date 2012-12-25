
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

        nameMap.put(Material.STEP, new String[] {
            "STONE_STEP",
            "SANDSTONE_STEP",
            "WOOD_STEP",
            "COBBLESTONE_STEP",
            "BRICK_STEP",
            "STONE_BRICK_STEP",
            "STONE_STEP"
        });

        nameMap.put(Material.SMOOTH_BRICK, new String[] {
           "SMOOTH_BRICK",
           "MOSSY_SMOOTH_BRICK",
           "CRACKED_SMOOTH_BRICK"
        });

        nameMap.put(Material.INK_SACK, new String[] {
           "BLACK_DYE",
           "ROSE_RED_DYE",
           "GREEN_DYE",
           "BROWN_DYE",
           "LAPIS_LAZULI_DYE",
           "PURPLE_DYE",
           "CYAN_DYE",
           "LIGHT_GRAY_DYE",
           "GRAY_DYE",
           "PINK_DYE",
           "LIME_DYE",
           "YELLOW_DYE",
           "LIGHT_BLUE_DYE",
           "MAGENTA_DYE",
           "ORANGE_DYE",
           "WHITE_DYE"
        });

        nameMap.put(Material.WOOL, new String[] {
            "WHITE_WOOL",
            "ORANGE_WOOL",
            "MAGENTA_WOOL",
            "LIGHT_BLUE_WOOL",
            "YELLOW_WOOL",
            "LIME_GREEN_WOOL",
            "PINK_WOOL",
            "GRAY_WOOL",
            "LIGHT_GRAY_WOOL",
            "CYAN_WOOL",
            "PURPLE_WOOL",
            "BLUE_WOOL",
            "BROWN_WOOL",
            "GREEN_WOOL",
            "RED_WOOL",
            "BLACK_WOOL"
        });

        nameMap.put(Material.GRASS, new String[] {
            "DEAD_SHRUB",
            "TALL_GRASS",
            "FERN"
        });

        nameMap.put(Material.LEAVES, new String[] {
            "OAK_LEAVES",
            "PINE_LEAVES",
            "BIRCH_LEAVES",
            "JUNGLE_LEAVES"
        });

        nameMap.put(Material.SAPLING, new String[] {
            "OAK_SAPLING",
            "PINE_SAPLINE",
            "BIRCH_SAPLING",
            "JUNGLE_SAPLING"
        });

        nameMap.put(Material.DOUBLE_STEP, new String[] {
            "STONE_DOUBLE_STEP",
            "SANDSTONE_DOUBLE_STEP",
            "WOOD_DOUBLE_STEP",
            "COBBLESTONE_DOUBLE_STEP",
            "BRICK_DOUBLE_STEP",
            "STONE_BRICK_DOUBLE_STEP"
        });

        nameMap.put(Material.AIR, new String[] {
            "FISTS"
        });

	ITEM_NAME_MAP = Collections.unmodifiableMap(nameMap);
    }
}
