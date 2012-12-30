/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.util;

import me.dbstudios.solusrpg.entities.conf.RpgClass;
import me.dbstudios.solusrpg.managers.ClassManager;

/**
 *
 * @author Tyler Lartonoix
 */
public class SimpleFormatter implements Formatter {
    public static String classListNL(String prefix) {
        String val = "";

        for (RpgClass cl : ClassManager.matchClass(".*"))
            val += prefix + cl.getName();

        return val;
    }
}
