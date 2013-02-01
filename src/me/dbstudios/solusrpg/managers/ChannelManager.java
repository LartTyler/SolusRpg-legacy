/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.managers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import me.dbstudios.solusrpg.chat.ChatChannel;
import me.dbstudios.solusrpg.util.Directories;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author Tyler Lartonoix
 */
public class ChannelManager {
    private static Map<String, ChatChannel> channels = new HashMap<>();
    private static String rfAlgo;
    private static String format;
    private static double rfTolerance;
    private static double mfTolerance;
    private static double obfusTolerance;

    static {
        FileConfiguration conf = YamlConfiguration.loadConfiguration(new File(Directories.CONFIG + "config.yml"));

        rfAlgo = conf.getString("config.chat.range-factor-algorithm", "({distance} / {range}) / 4");
        format = conf.getString("config.chat.format", "{green}[{symbol}] {sender}: {aqua}{message}");
        rfTolerance = conf.getDouble("config.chat.range-factor-tolerance", 20.0);
        mfTolerance = conf.getDouble("config.chat.material-factor-tolerance", 10.0);
        obfusTolerance = conf.getDouble("config.chat.obfus-tolerance", 15.0);
    }

    public static ChatChannel getChannel(String channel) {
        return channels.get(channel);
    }

    public static boolean channelExists(String channel) {
        return channels.containsKey(channel);
    }

    public static int size() {
        return channels.size();
    }

    public static String getRangeFactorAlgorithm() {
        return rfAlgo;
    }

    public static String getFormat() {
        return format;
    }

    public static double getRangeFactorTolerance() {
        return rfTolerance;
    }

    public static double getMaterialFactorTolerance() {
        return mfTolerance;
    }

    public static double getObfusTolerance() {
        return obfusTolerance;
    }
}
