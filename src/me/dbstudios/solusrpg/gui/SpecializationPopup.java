/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.managers.SpecializationManager;
import me.dbstudios.solusrpg.player.specialization.Specialization;
import org.getspout.spoutapi.gui.*;

/**
 *
 * @author Tyler Lartonoix
 */
public class SpecializationPopup extends GenericPopup {
    private final RpgPlayer player;

    public SpecializationPopup(RpgPlayer player) {
        super();

        Label skillPointsLabel = new GenericLabel("Available skill points: " + player.getSkillPoints());

        skillPointsLabel.setScale(0.75f).setFixed(true).setX(10).setY(2).setHeight(8);

        super.attachWidget(SolusRpg.getInstance(), skillPointsLabel);

        this.player = player;
        int leftOffset = 10;

        for (Specialization spec : SpecializationManager.getSpecializationCollection()) {
            Container mc = new GenericContainer();
            List<Specialization> specList = new ArrayList<>();
            int deep = 0, wide = 0;

            specList.add(spec);

            while (specList != null && !specList.isEmpty()) {
                List<Specialization> subSpecs = new ArrayList<>();

                wide = Math.max(wide, specList.size());
                deep++;

                for (Specialization s : specList)
                    if (s.hasSubSpecialization())
                        subSpecs.addAll(s.getSubSpecialization());

                if (!subSpecs.isEmpty())
                    specList = subSpecs;
                else
                    specList = null;
            }

            mc.setAuto(false).setLayout(ContainerType.OVERLAY).setFixed(true).setWidth(wide * 36).setHeight(deep * 50).setX(leftOffset).setY(10);
            leftOffset += mc.getWidth() + 10;
            specList = new ArrayList<>();

            specList.add(spec);

            int depth = 0;

            while (specList != null && !specList.isEmpty()) {
                List<Specialization> subSpecs = new ArrayList<>();
                int sectWidth = (int)Math.floor((double)mc.getWidth() / (double)specList.size()), pos = 1;

                SolusRpg.log(Level.INFO, "Section width: {0}", sectWidth);

                for (Specialization s : specList) {
                    Container c = new GenericContainer();

                    c.setAuto(false).setLayout(ContainerType.OVERLAY).setWidth(32).setHeight(42).setMarginLeft((int)Math.floor((pos * sectWidth) - (int)Math.floor((double)sectWidth / 2.0))).setMarginTop(50 * depth);

                    Gradient placeholder = new GenericGradient(new Color(80, 80, 80));

                    placeholder.setFixed(true).setWidth(32).setHeight(32).setMargin(0).setTooltip(s.getTooltip(s.getLevel(player)));

                    Label level = new GenericLabel(s.getLevel(this.player) + "/" + s.getMaxLevel());

                    level.setScale(0.5f).setAuto(true).setFixed(true).setWidth(GenericLabel.getStringWidth(level.getText(), 0.5f)).setHeight(6).setMarginTop(26).setMarginLeft(30 - level.getWidth()).setPriority(RenderPriority.Low);

                    Button levelUp = new LevelUpButton("Level Up", s, level, skillPointsLabel, placeholder);

                    levelUp.setAuto(true).setFixed(true).setWidth(33).setHeight(10).setMarginTop(34);

                    c.addChildren(placeholder, level, levelUp);
                    mc.addChild(c);

                    if (s.hasSubSpecialization())
                        subSpecs.addAll(s.getSubSpecialization());

                    pos++;
                }

                if (!subSpecs.isEmpty())
                    specList = subSpecs;
                else
                    specList = null;

                depth++;
            }

            super.attachWidget(SolusRpg.getInstance(), mc);
        }
    }
}