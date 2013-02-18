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
public interface Group<T> {
    public int getPopulation();
    public int getMaxPopulation();
    public Set<T> getMembers();
    public void addMember(T member);
    public void removeMember(T member);
    public void kickMember(T member);
    public void banMember(T member);
    public void pardonMember(T member);
}
