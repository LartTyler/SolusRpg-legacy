/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.gui;

import java.util.ArrayList;
import java.util.List;
import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.managers.SpecializationManager;
import me.dbstudios.solusrpg.player.specialization.Specialization;
import org.getspout.spoutapi.gui.Button;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.Container;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericContainer;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.Label;
import org.getspout.spoutapi.gui.Texture;
import org.getspout.spoutapi.gui.Widget;
import org.getspout.spoutapi.gui.WidgetAnchor;

/**
 *
 * @author Tyler Lartonoix
 */
public class SpecializationPopup extends GenericPopup {
    private final Container treeContainer;
    private final RpgPlayer owner;

    public SpecializationPopup(RpgPlayer owner) {
        this.owner = owner;

        Container c = new GenericContainer();

        c.setAuto(false).setAutoDirty(true);

        for (Specialization s : SpecializationManager.getSpecializationCollection())
            c.addChild(buildTree(s));

        Widget[] children = c.getChildren();

        for (int i = 0; i < children.length; i++)
            if (i == 0)
                children[i].setX(0);
            else
                children[i].setX(children[i - 1].getWidth() + 8);

        this.treeContainer = c;
    }

    private Container buildTree(Specialization rootSpec) {
        Container mc = new GenericContainer();
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

        mc.setWidth(32 * wide - 4 * (wide - 1)).setHeight(48 * deep - 6 * (deep - 1));

        int depth = 0;

        while (specList != null && !specList.isEmpty()) {
            List<Specialization> nextDepth = new ArrayList<>();
            int offset = (int)Math.floor(((double)mc.getWidth() / (double)specList.size()) / 2);
            int pos = 1;

            for (Specialization spec : specList) {
                Container c = new GenericContainer();
                Texture image = new GenericTexture(spec.getIconPath());
                Button decrease = new GenericButton("-"), increase = new GenericButton("+");
                Label levelLabel = new GenericLabel(spec.getLevel(owner) + "/" + spec.getMaxLevel());

                c.setAnchor(WidgetAnchor.CENTER_CENTER).setWidth(32).setHeight(48).setX(-(pos * offset)).setY(54 * depth);

                image.setAnchor(WidgetAnchor.TOP_LEFT).setHeight(32).setWidth(32).setX(0).setY(0);
                decrease.setEnabled(false).setAnchor(WidgetAnchor.TOP_LEFT).setHeight(14).setWidth(14).setX(0).setY(34);
                increase.setAnchor(WidgetAnchor.TOP_LEFT).setHeight(14).setWidth(14).setX(16).setY(34);
                levelLabel.setTextColor(new Color(0, 255, 0)).setAlign(WidgetAnchor.TOP_RIGHT).setAnchor(WidgetAnchor.TOP_LEFT).setHeight(10).setWidth(30).setX(0).setY(20);

                c.addChildren(image, decrease, increase);
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
