/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dbstudios.solusrpg.entities.conf;

/**
 *
 * @author
 * Tyler
 * Lartonoix
 */
public enum PermitNode {
    BREAK("can-break", "Can break"),
    CRAFT("can-craft", "Can craft"),
    PLACE("can-place", "Can place"),
    SMELT("can-smelt", "Can smelt"),
    WEAR("can-wear", "Can wear"),
    USE("can-use", "Can use");

    private final String path;
    private final String node;
    private final String text;

    private PermitNode(String path, String text) {
        this.node = path;
        this.path = "permit-nodes." + path;
        this.text = text;
    }

    public String toString() {
        return this.path;
    }

    public String getText() {
        return this.text;
    }

    public String getNode() {
        return this.node;
    }
}
