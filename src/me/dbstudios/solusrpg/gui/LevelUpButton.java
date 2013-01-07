/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.gui;

import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.managers.PlayerManager;
import me.dbstudios.solusrpg.player.specialization.Specialization;
import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.Label;

/**
 *
 * @author Tyler Lartonoix
 */
public class LevelUpButton extends GenericButton {
    private final Specialization spec;
    private final Label levelLabel;

    public LevelUpButton(String text, Specialization spec, Label levelLabel) {
        super(text);

        this.spec = spec;
        this.levelLabel = levelLabel;
    }

    public void onButtonClick(ButtonClickEvent ev) {
        RpgPlayer clicker = PlayerManager.get(ev.getPlayer());
        int specLevel = spec.getLevel(clicker);

        if (clicker.getSkillPoints() > 0 && specLevel + 1 <= spec.getMaxLevel()) {
            spec.removeEffect(clicker, specLevel);
            spec.applyEffect(clicker, specLevel + 1);

            clicker.removeSkillPoints(1);

            if (levelLabel != null)
                levelLabel.setText((specLevel + 1) + "/" + spec.getMaxLevel());
        } else {
            ev.setCancelled(true);
        }
    }
}
