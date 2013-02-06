/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.managers;

import java.io.File;
import java.util.EnumMap;
import java.util.Map;
import me.dbstudios.solusrpg.util.Directories;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.SpoutWorld;

/**
 *
 * @author Tyler Lartonoix
 */
public class DensityManager {
    private final static Map<Material, Double> densities = new EnumMap<>(Material.class);

    private static double hardnessMult;

    static {
        FileConfiguration conf = YamlConfiguration.loadConfiguration(new File(Directories.CONFIG + "block_densities.yml"));
        ConfigurationSection sect = conf.getConfigurationSection("densities");

        if (sect != null)
            for (String key : sect.getKeys(false))
                if (Material.matchMaterial(key) != null && Material.matchMaterial(key).isBlock())
                    densities.put(Material.matchMaterial(key), sect.getDouble(key));

        conf = YamlConfiguration.loadConfiguration(new File(Directories.CONFIG + "config.yml"));

        hardnessMult = conf.getDouble("config.block-densities.base-hardness-multiplier", 15.0);
    }

    public static int size() {
        return densities.size();
    }

    public static boolean hasCustomDensity(Block b) {
        return DensityManager.hasCustomDensity(b.getType());
    }

    public static boolean hasCustomDensity(Material m) {
        return densities.containsKey(m);
    }

    public static double getBlockDensity(Block b) {
        if (densities.containsKey(b.getType()))
            return densities.get(b.getType());

        return SpoutManager.getMaterialManager().getHardness(new SpoutWorld(b.getWorld()).getBlockAt(b.getLocation()).getBlockType()) * hardnessMult;
    }
}
