/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.util;

/**
 *
 * @author Tyler Lartonoix
 */
public interface Metadatable<K, V> {
    public void putMetadata(K key, V value);
    public V getMetadata(K key);
    public void removeMetadata(K key);
    public int getMetadataCount();
    public boolean hasMetadata(K key);
    public <T> T getMetadataAs(K key, Class<T> type);
}
