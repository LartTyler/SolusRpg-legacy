/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dbstudios.solusrpg.entities.conf;

/**
 *
 * @author
 * tyler
 */
public interface Meter<O, T extends Number> {
    public T getValue();
    public T getMaxValue();
    public void setValue(T value);
    public String getMeterName();
    public O getOwner();
    public void add(T amount);
    public void remove(T amount);
}
