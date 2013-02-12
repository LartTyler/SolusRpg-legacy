/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.chat;

import java.io.File;
import java.util.Set;
import me.dbstudios.solusrpg.entities.RpgPlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Tyler Lartonoix
 */
public interface ChatChannel {
    /**
     * Gets a <code>Set</code> of all members currently in this channel.
     *
     * @return All of the current members of this channel
     */
    public Set<RpgPlayer> getMembers();

    /**
     * Gets a <code>Set</code> of all members currently <i>active</i> in this channel.
     *
     * @return All members of this channel currently focused on the channel
     */
    public Set<RpgPlayer> getActiveMembers();

    /**
     * Gets a <code>Set</code> of all worlds this channel can send chats to.
     *
     * An empty set should denote all worlds. A result of <code>null</code> denotes no worlds.
     *
     * @return A <code>Set</code> containing what worlds this channel may communicate in
     */
    public Set<World> getChattableWorlds();

    /**
     * Gets the display name of this channel, for displaying to users.
     *
     * This is usually the "pretty" name of the channel, and can contain spaces, dashes, underscores, etc.
     *
     * @return The "pretty" name of the channel, for display purposes
     */
    public String getName();

    /**
     * Gets the internal name of this channel.
     *
     * This should be used to identify the channel on the file system and within configurations, where certain characters (such as spaces) may not be permitted.
     *
     * @return The internally used name of this channel
     */
    public String getSystemName();

    /**
     * Gets the (usually) single character representation of this channel.
     *
     * While the symbol can be anything, it should usually be the short name of the channel.
     *
     * @return The symbol of this channel
     */
    public String getSymbol();

    /**
     * Gets the algorithm used to determine the range factor to apply to chats over a distance.
     *
     * @return The algorithm used to determine chat mangling over distance
     */
    public String getRangeFactorAlgorithm();

    /**
     * Gets the format string used for messages.
     *
     * @return The string that defines chat message formats
     */
    public String getFormat();

    /**
     * Gets the max range that this channel can send messages to.
     *
     * @return This channels max range
     */
    public int getRange();

    /**
     * Sends a message to the entire channel from <code>sender</code>.
     *
     * @param sender The player from whom to send the message
     * @param msg The message to send
     * @return A count of the number of players to receive the message
     */
    public int sendMessage(RpgPlayer sender, String msg);

    /**
     * Sends a message to the entire channel from <code>sender</code>.
     *
     * This method should be functionally the same as sendMessage(RpgPlayer, String). Internally, it will most likely make a call to the previously defined
     * sendMessage. The only difference should be that the arguments present in <code>args</code> is first parsed into the message.
     *
     * @param sender The player from whom to send the message
     * @param msg The message to send
     * @param args An array of arguments to parse into the string before sending
     * @return A count of the number of players to receive the message
     */
    public int sendMessage(RpgPlayer sender, String msg, Object... args);

    /**
     * Sends a broadcast to the entire channel from <code>sender</code>.
     *
     * This method should function the same as it's sendMessage counterpart. The only difference should be that range and material
     * factors will be ignored.
     *
     * @param sender The player from whom to send the message
     * @param msg The message to send
     */
    public void sendBroadcast(RpgPlayer sender, String msg);

    /**
     * Sends a broadcast to the entire channel from <code>sender</code>.
     *
     * This method should function the same as it's sendMessage counterpart. The only difference should be that range and material
     * factors will be ignored.
     *
     * @param sender The player from whom to send the message
     * @param msg The message to send
     * @param args An array of arguments to parse into the string before sending
     */
    public void sendBroadcast(RpgPlayer sender, String msg, Object... args);

    /**
     * Sends a general broadcast to the entire channel.
     *
     * @param msg The message to send
     */
    public void sendBroadcast(String msg);

    /**
     * Sends a general broadcast to the entire channel.
     *
     * @param msg The message to send
     * @param args An array of aguments to parse into the string before sending
     */
    public void sendBroadcast(String msg, Object... args);

    /**
     * Gets the maximum number of allowed members.
     *
     * Anything less than 1 should denote an unlimited amount.
     *
     * @return The max members that may be in this channel at one time
     */
    public int getMaxPopulation();

    /**
     * Gets the current population of the channel.
     *
     * @return The number of players in this channel
     */
    public int getPopulation();

    /**
     * Gets the tolerance (the minimum number) that the range factor must exceed for mangling to occur.
     *
     * @return The minimum range factor needed to mangle messages
     */
    public double getRangeFactorTolerance();

    /**
     * Gets the tolerance (the minimum number) that the material factor must exceed for mangling to occur.
     *
     * @return The minimum material factor needed to mangle messages
     */
    public double getMaterialFactorTolerance();

    /**
     * Checks if the given player is banned from this channel or not.
     *
     * @param player The player to check ban status on
     * @return True if the player is banned, false otherwise
     */
    public boolean isBanned(RpgPlayer player);

    /**
     * Checks if the given player is a moderator in this channel.
     *
     * @param player The player to check moderator status on
     * @return True if the player is a moderator, false otherwise
     */
    public boolean isModerator(RpgPlayer player);

    /**
     * Checks if the given <code>world</code> can send/receive messages from this channel.
     *
     * @param world The world to check
     * @return True if the world can chat on this channel, false otherwise
     */
    public boolean isChattableWorld(World world);

    /**
     * Checks if the given player is in the channel.
     *
     * @param player The player to check
     * @return True if the player exists in this channel, false otherwise
     */
    public boolean isInChannel(RpgPlayer player);

    /**
     * Checks if this channel is a private channel (requires a password).
     *
     * @return True if this channel requires a password to join
     */
    public boolean isPrivateChannel();

    /**
     * Checks if this channel is a hidden channel (does not show up on /rpg chat list for those without chat.list-hidden).
     *
     * @return True if this channel will show up in the channel list command
     */
    public boolean isHiddenChannel();

    /**
     * Checks if this channel can send messages to worlds that are not the originating world.
     *
     * @return True if this channel can send messages to other worlds, false otherwise
     */
    public boolean isCrossWorld();

    /**
     * Checks if this channel has reached it's maximum population.
     *
     * @return True if this channel should not accept any new members, false otherwise
     */
    public boolean isFull();

    /**
     * Checks if the given player can join this channel.
     *
     * @param player The player to check
     * @return True if the player can join (i.e. has all the necessary permissions and is not banned), false otherwise
     */
    public boolean canJoin(RpgPlayer player);

    /**
     * Checks if the given player can send messages on this channel.
     *
     * @param player The player to check
     * @return True if the player can send messages (i.e. has all the necessary permissions), false otherwise
     */
    public boolean canChat(RpgPlayer player);

    /**
     * Checks if the given player can leave this channel.
     *
     * @param player The player to check
     * @return True if the player can leave the channel, false otherwise
     */
    public boolean canLeave(RpgPlayer player);

    /**
     * Checks if the given command sender can kick the target player.
     *
     * @param sender The user attempting to kick the target
     * @param target The player that will be kicked
     * @return True if the sender can kick the target, false otherwise
     */
    public boolean canKick(CommandSender sender, RpgPlayer target);

    /**
     * Checks if the given command sender can ban the target player.
     *
     * @param sender The user attempting to kick the target
     * @param target The player that will be banned
     * @return True if the sender can kick the target, false otherwise
     */
    public boolean canBan(CommandSender sender, RpgPlayer target);

    /**
     * Checks if the given player can pardon a banned player.
     *
     * @param player The player to check
     * @return True if the player can pardon a ban, false otherwise
     */
    public boolean canPardon(RpgPlayer player);

    /**
     * Gets the file that stores this channels configuration.
     *
     * @return A <code>File</code> object that has this channels configuration
     */
    public File getFile();

    /**
     * Gets this channels <code>FileConfiguration</code>.
     *
     * @return This channels configuration
     */
    public FileConfiguration getConfiguration();

    /**
     * Adds a member to the channel.
     *
     * @param player The player to add
     */
    public void addMember(RpgPlayer player);

    /**
     * Removes a player from this channel.
     *
     * @param player The player to remove
     */
    public void removeMember(RpgPlayer player);

    /**
     * Bans a player from this channel.
     *
     * @param player The player to ban
     */
    public void banMember(RpgPlayer player);

    /**
     * Pardons a player from their ban for this channel.
     *
     * @param player The player to pardon
     */
    public void pardonMember(RpgPlayer player);

    /**
     * Kicks a player from this channel.
     *
     * @param player The player to kick
     */
    public void kickMember(RpgPlayer player);
}
