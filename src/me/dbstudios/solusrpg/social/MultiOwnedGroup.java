/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.social;

import java.util.Set;

/**
 *
 * @author Tyler Lartonoix
 */
public interface MultiOwnedGroup<T> extends Group<T> {
    public T getFounder();
    public Set<T> getLeaders();
}
