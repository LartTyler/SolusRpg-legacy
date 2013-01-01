
package me.dbstudios.solusrpg.player.specialization;

import java.util.List;
import me.dbstudios.solusrpg.entities.RpgPlayer;

/**
 * @author Tyler Lartonoix
 */
public interface Specialization {
    public boolean applyEffect(RpgPlayer player, int level);
    public boolean removeEffect(RpgPlayer player, int level);
    public String getIconPath();
    public boolean hasSubSpecialization();
    public boolean isRootSpecialization();
    public List<Specialization> getSubSpecialization();
    public Specialization getPreSpecialization();
}