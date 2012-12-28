/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.dbstudios.solusrpg.SolusRpg;

/**
 *
 * @author Tyler Lartonoix
 */
public class OutputFormatter {
    private static Map<String, Method> formatMethods = new HashMap<>();
    private static Map<String, Formatter> formatters = new HashMap<>();

    public static void registerFormatter(Formatter f) {
        for (Method m : f.getClass().getMethods()) {
            formatMethods.put(m.getName(), m);
            formatters.put(m.getName(), f);
        }
    }

    public static String format(String value, Map<String, String> args) {
        // Replace key placeholders first, since keys should always take priority
        if (args != null)
            for (String key : args.keySet())
                value = value.replace("\\$\\{" + key + "\\}", value);

        Matcher m = Pattern.compile("(?i)\\$\\{[a-z][a-z0-9_-]*\\}(?=\\(((?=(\\d+(?=\\.\\d+)?|'[^']*'|true|false|null)(?=, ?)?)*)\\))?").matcher(value);

        while (m.find()) {
            String method = m.group(0);
            String[] split = m.group(1).split(", ?(?=[\\dtfn'])(?=([^']*'[^']*')*[^']*$)");
            Object[] arguments = new Object[split.length];

            for (int i = 0; i < split.length; i++)
                if (split[i].charAt(0) == '\'')
                    arguments[i] = split[i].substring(1, split[i].length() - 2);
                else if (split[i].equalsIgnoreCase("true"))
                    arguments[i] = true;
                else if (split[i].equalsIgnoreCase("false"))
                    arguments[i] = false;
                else if (split[i].equalsIgnoreCase("null"))
                    arguments[i] = null;
                else
                    arguments[i] = Integer.parseInt(split[i]);

            try {
                Object val = formatMethods.get(method).invoke(formatters.get(method), arguments);

                if (val instanceof String)
                    value = value.replace(m.group(), (String)val);
            } catch (IllegalAccessException | InvocationTargetException e) {
                SolusRpg.log(Level.WARNING, "An error occurred while attempting to run the formatting commad '{0}'.", method);
            }
        }

        return value;
    }
}
