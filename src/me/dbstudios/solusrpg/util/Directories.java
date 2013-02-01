/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dbstudios.solusrpg.util;

import java.io.File;

/**
 *
 * @author
 * Tyler
 * Lartonoix
 */
public enum Directories {
    BASE("plugins::dbstudios::SolusRpg::"),
    CONFIG(BASE + "config::"),
    CLASSES(BASE + "classes::"),
    CHAT(BASE + "chat::"),
    CHAT_CHANNELS(CHAT + "channels::"),
    DATA(BASE + "data::");

    private final String path;

    private Directories(String path) {
        this.path = path.replace("::", File.separator);
    }

    public String toString() {
        return this.path;
    }
}
