
package me.dbstudios.solusrpg.util;

import java.util.ArrayList;
import java.util.List;

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
}
