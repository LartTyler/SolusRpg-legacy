/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.chat;

import java.io.File;
import java.util.Set;
import me.dbstudios.solusrpg.entities.RpgPlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Tyler Lartonoix
 */
public interface ChatChannel {
    public Set<RpgPlayer> getMembers();
    public Set<RpgPlayer> getActiveMembers();
    public Set<World> getChattableWorlds();
    public String getName();
    public String getSystemName();
    public String getSymbol();
    public String getRangeFactorAlgorithm();
    public String getFormat();
    public int getRange();
    public int sendMessage(RpgPlayer sender, String msg);
    public int sendMessage(RpgPlayer sender, String msg, Object... args);
    public void sendBroadcast(RpgPlayer sender, String msg);
    public void sendBroadcast(RpgPlayer sender, String msg, Object... args);
    public void sendBroadcast(String msg);
    public void sendBroadcast(String msg, Object... args);
    public int getMaxPopulation();
    public int getPopulation();
    public double getRangeFactorTolerance();
    public double getMaterialFactorTolerance();
    public boolean isBanned(RpgPlayer player);
    public boolean isModerator(RpgPlayer player);
    public boolean isChattableWorld(World world);
    public boolean isInChannel(RpgPlayer player);
    public boolean isPrivateChannel();
    public boolean isHiddenChannel();
    public boolean isCrossWorld();
    public boolean isFull();
    public boolean canJoin(RpgPlayer player);
    public boolean canChat(RpgPlayer player);
    public boolean canLeave(RpgPlayer player);
    public boolean canKick(RpgPlayer player, RpgPlayer target);
    public boolean canBan(RpgPlayer player, RpgPlayer target);
    public boolean canPardon(RpgPlayer player);
    public boolean hasMember(RpgPlayer player);
    public File getFile();
    public FileConfiguration getConfiguration();
    public void addMember(RpgPlayer player);
    public void removeMember(RpgPlayer player);
}
