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
    private final RpgPlayer owner;

    public SpecializationPopup(RpgPlayer owner) {
        this.owner = owner;

        Container c = new GenericContainer();
        int screenWidth = owner.getBasePlayer().getMainScreen().getWidth(), screenHeight = owner.getBasePlayer().getMainScreen().getHeight();

        c.setAnchor(WidgetAnchor.TOP_LEFT).setWidth(screenWidth - 20).setHeight(screenHeight - 20).setX(10).setY(10).setAutoDirty(true);

        SolusRpg.log(Level.INFO, "Spec. tree container: width = {0}, height = {1}, x = {2}, y = {3}", c.getWidth(), c.getHeight(), c.getX(), c.getY());

        for (Specialization s : SpecializationManager.getSpecializationCollection())
            c.addChild(buildTree(s));

        Widget[] children = c.getChildren();

        for (int i = 0; i < children.length; i++)
            if (i == 0)
                children[i].setX(0);
            else
                children[i].setX(children[i - 1].getWidth() + 5);

        this.setWidth(screenWidth).setHeight(screenHeight).setX(0).setY(0).setAnchor(WidgetAnchor.TOP_LEFT);
        this.attachWidget(SolusRpg.getInstance(), c);

        SolusRpg.log(Level.INFO, "SpecializationPopup: width = {0}, height = {1}, x = {2}, y = {3}", this.getWidth(), this.getHeight(), this.getX(), this.getY());
    }

    private Container buildTree(Specialization rootSpec) {
        SolusRpg.log(Level.INFO, "Building Spec. tree using root '{0}'.", rootSpec.getUniqueName());

        Container mc = new GenericContainer();

        mc.setAlign(WidgetAnchor.TOP_LEFT).setAutoDirty(true).setAnchor(WidgetAnchor.TOP_LEFT);

        List<Specialization> specList = new ArrayList<>();

        specList.add(rootSpec);

        // We iterate through all sub-specializations before we build the GUI in order to determine the dimensions of the container.
        // I hate to do it this way, but I can't think of any other way to solve this issue.
        int deep = 0, wide = 0;

        while (specList != null && !specList.isEmpty()) {
            List<Specialization> nextDepth = new ArrayList<>();

            for (Specialization spec : specList) {
                if (spec.hasSubSpecialization())
                    nextDepth.addAll(spec.getSubSpecialization());
            }

            if (specList.size() > wide)
                wide = specList.size();

            deep++;

            if (!nextDepth.isEmpty())
                specList = nextDepth;
            else
                specList = null;
        }

        SolusRpg.log(Level.INFO, "\t'{0}' tree found to be {1} deep and {2} wide.", rootSpec.getUniqueName(), deep, wide);

        mc.setWidth(32 * wide + 5 * (wide - 1)).setHeight(48 * deep + 6 * (deep - 1)).setAnchor(WidgetAnchor.TOP_LEFT);

        SolusRpg.log(Level.INFO, "\t'{0}' tree found to have a width of {1} and a height of {2}.", rootSpec.getUniqueName(), mc.getWidth(), mc.getHeight());

        int depth = 0;

        specList = new ArrayList<>();

        specList.add(rootSpec);

        while (specList != null && !specList.isEmpty()) {
            List<Specialization> nextDepth = new ArrayList<>();
            int sectionWidth = (int)Math.floor((double)mc.getWidth() / (double)specList.size());
            int pos = 1;

            SolusRpg.log(Level.INFO, "\tSection width found to be {0}.", sectionWidth);

            for (Specialization spec : specList) {
                SolusRpg.log(Level.INFO, "\t\tAt leaf '{0}', pos {1}, depth {2}.", spec.getUniqueName(), pos, depth);

                Container c = new GenericContainer();
//                Texture image = new GenericTexture(spec.getIconPath());
                Gradient g = new GenericGradient(new Color(0, 0, 0));
                Label levelLabel = new GenericLabel(spec.getLevel(owner) + "/" + spec.getMaxLevel());
                Button levelUpButton = new LevelUpButton("Level Up", spec, levelLabel);

                c.setAnchor(WidgetAnchor.CENTER_CENTER).setWidth(32).setHeight(48).setX(((sectionWidth * pos) - (int)Math.floor((double)sectionWidth / 2.0)) + (5 * (pos - 1))).setY(54 * depth);

                SolusRpg.log(Level.INFO, "\t\t\tPlacing Spec. container at x = {0} and y = {1}.", c.getX(), c.getY());

                c.addChildren(g, levelUpButton, levelLabel);
//                image.setAnchor(WidgetAnchor.TOP_LEFT).setHeight(32).setWidth(32).setX(0).setY(0);
                g.setAnchor(WidgetAnchor.TOP_LEFT).setHeight(32).setWidth(32).setX(0).setY(0);
                levelUpButton.setAnchor(WidgetAnchor.TOP_LEFT).setHeight(8).setWidth(32).setX(0).setY(0);
                levelLabel.setTextColor(new Color(255, 255, 255)).setShadow(true).setAlign(WidgetAnchor.TOP_RIGHT).setAnchor(WidgetAnchor.TOP_LEFT).setHeight(8).setWidth(30).setX(0)
                        .setY(20).setAutoDirty(true).setPriority(RenderPriority.Low);

//                c.addChildren(image, levelUpButton, levelLabel);
                mc.addChild(c);

                if (spec.hasSubSpecialization())
                    nextDepth.addAll(spec.getSubSpecialization());

                pos++;
            }

            depth++;

            if (!nextDepth.isEmpty())
                specList = nextDepth;
            else
                specList = null;
        }

        return mc;
    }
}
