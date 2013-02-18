/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.social;

/**
 *
 * @author Tyler Lartonoix
 */
public interface OwnedGroup<T> extends Group<T> {
    public T getLeader();
}
