/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.social.party;

import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.social.OwnedGroup;

/**
 *
 * @author Tyler Lartonoix
 */
public interface Party extends OwnedGroup<RpgPlayer> {
    public double getExpModifier();
}
