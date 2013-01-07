/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.managers;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import me.dbstudios.solusrpg.util.Directories;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author Tyler Lartonoix
 */
public class PhraseManager {
    private static final Map<String, String> phrases;

    static {
	FileConfiguration conf = YamlConfiguration.loadConfiguration(new File(Directories.CONFIG + "config.yml"));
	ConfigurationSection phraseSection = conf.getConfigurationSection("config.phrases");

	phrases = Collections.unmodifiableMap(PhraseManager.loadPhrases(phraseSection, null));
    }

    public static String getPhrase(String key) {
	return phrases.get(key);
    }

    public static boolean phraseExists(String key) {
	return phrases.containsKey(key);
    }

    private static Map<String, String> loadPhrases(ConfigurationSection phraseSection, String path) {
	Map<String, String> p = new HashMap<>();

	for (String key : phraseSection.getKeys(false))
	    if (phraseSection.isConfigurationSection(key))
		p.putAll(PhraseManager.loadPhrases(phraseSection.getConfigurationSection(key), (path != null ? path + "." : "") + key));
	    else
		p.put((path != null ? path + "." : "") + key, phraseSection.getString(key));

	return p;
    }

    public static int size() {
	return phrases.size();
    }
}
