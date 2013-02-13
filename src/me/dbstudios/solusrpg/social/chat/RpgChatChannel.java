/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.dbstudios.solusrpg.social.chat;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import me.dbstudios.solusrpg.entities.RpgPlayer;
import me.dbstudios.solusrpg.exceptions.ChannelConfigurationException;
import me.dbstudios.solusrpg.managers.ChannelManager;
import me.dbstudios.solusrpg.managers.DensityManager;
import me.dbstudios.solusrpg.managers.PermissionManager;
import me.dbstudios.solusrpg.managers.PhraseManager;
import me.dbstudios.solusrpg.util.Directories;
import me.dbstudios.solusrpg.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.BlockIterator;

/**
 *
 * @author Tyler Lartonoix
 */
public class RpgChatChannel implements ChatChannel {
    private final String systemName;
    private final int maxPopulation;

    private Set<RpgPlayer> members = new HashSet<>();
    private Set<UUID> validWorlds = new HashSet<>();
    private String name;
    private String symbol;
    private String rfAlgo;
    private String format;
    private int range;
    private double rfTolerance;
    private double mfTolerance;
    private double obfusTolerance;
    private boolean isPrivate;
    private boolean isHidden;
    private boolean isCrossWorld;

    public RpgChatChannel(String systemName) throws ChannelConfigurationException {
        File f = new File(Directories.CHAT_CHANNELS + systemName + ".yml");

        if (!f.exists())
            throw new ChannelConfigurationException("Could not find configuration for " + systemName + ".");

        FileConfiguration conf = YamlConfiguration.loadConfiguration(f);

        for (String world : Util.toTypedList(conf.getList("channel.worlds", null), String.class))
            if (Bukkit.getWorld(world) != null)
                validWorlds.add(Bukkit.getWorld(world).getUID());

        this.systemName = systemName;
        this.name = conf.getString("channel.name", "Chat Channel");
        this.symbol = conf.getString("channel.symbol", name.substring(0, 2).toLowerCase());
        this.rfAlgo = conf.getString("channel.range-factor-algorithm", ChannelManager.getRangeFactorAlgorithm());
        this.format = conf.getString("channel.format", ChannelManager.getFormat());
        this.range = conf.getInt("channel.range", -1);
        this.maxPopulation = conf.getInt("channel.max-population", -1);
        this.rfTolerance = conf.getDouble("channel.range-factor-tolerance", ChannelManager.getRangeFactorTolerance());
        this.mfTolerance = conf.getDouble("channel.material-factor-tolerance", ChannelManager.getMaterialFactorTolerance());
        this.obfusTolerance = conf.getDouble("channel.obfus-tolerance", ChannelManager.getObfusTolerance());
        this.isPrivate = conf.getBoolean("channel.private", false);
        this.isHidden = conf.getBoolean("channel.hidden", false);
        this.isCrossWorld = conf.getBoolean("channel.cross-world", true);
    }

    public Set<RpgPlayer> getMembers() {
        return Collections.unmodifiableSet(members);
    }

    public Set<RpgPlayer> getActiveMembers() {
        Set<RpgPlayer> actives = new HashSet<>();

        for (RpgPlayer member : members)
            if (member.getActiveChannel() == this)
                actives.add(member);

        return actives;
    }

    public Set<World> getChattableWorlds() {
        Set<World> worlds = new HashSet<>();

        for (UUID uid : validWorlds)
            worlds.add(Bukkit.getWorld(uid));

        return worlds;
    }

    public String getName() {
        return this.name;
    }

    public String getSystemName() {
        return this.systemName;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public String getRangeFactorAlgorithm() {
        return this.rfAlgo;
    }

    public String getFormat() {
        return this.format;
    }

    public int getRange() {
        return this.range;
    }

    public int sendMessage(RpgPlayer sender, String msg) {
        int received = 0;

        String formatted = format;

        formatted = formatted.replaceAll("(?i)\\{channel-symbol\\}", this.symbol);
        formatted = formatted.replaceAll("(?i)\\{channel-name\\}", this.name);
        formatted = Util.buildPhrase(formatted, sender);

        for (ChatColor c : ChatColor.values())
            formatted = formatted.replaceAll("(?i)\\{" + c.name() + "\\}", c.toString());

        for (RpgPlayer member : members) {
            double rf = Util.getRangeFactor(this, Util.getDistance(sender, member));
            double mf = 0.0;

            BlockIterator it = new BlockIterator(sender.getWorld(), sender.getLocation().toVector(), member.getLocation().toVector(), 2.0, (int)Math.ceil(Util.getDistance(sender, member)));

            while (it.hasNext()) {
                Block b = it.next();

                if (DensityManager.hasCustomDensity(b) || (!DensityManager.hasCustomDensity(b) && b.getType().isSolid()))
                    mf += DensityManager.getBlockDensity(b);
            }

            double obfusFactor = ((rf >= this.rfTolerance ? rf : 0.0) + (mf >= this.mfTolerance ? mf : 0.0)) / 100.0;
            String msgToSend = "";

            for (char c : msg.toCharArray())
                if (c != ' ' && Math.random() <= obfusFactor)
                    msgToSend += '.';
                else
                    msgToSend += c;

            if ((double)msgToSend.replace(".", "").replace(" ", "").length() / (double)msg.length() >= obfusTolerance / 100.0) {
                member.sendMessage(formatted.replaceAll("(?i)\\{message\\}", msgToSend));

                received++;
            }
        }

        return received;
    }

    public int sendMessage(RpgPlayer sender, String msg, Object... args) {
        if (args != null)
            for (int i = 0; i < args.length; i++)
                msg = msg.replaceAll("(?i)\\{" + i + "\\}", args[i].toString());

        return this.sendMessage(sender, msg);
    }

    public void sendBroadcast(RpgPlayer sender, String msg) {
        String formatted = format;

        formatted = formatted.replaceAll("(?i)\\{channel-symbol\\}", this.symbol);
        formatted = formatted.replaceAll("(?i)\\{channel-name\\}", this.name);
        formatted = formatted.replaceAll("(?i)\\{message\\}", msg);
        formatted = Util.buildPhrase(formatted, sender);

        for (ChatColor c : ChatColor.values())
            formatted = formatted.replaceAll("(?i)\\{" + c.name() + "\\}", c.toString());

        for (RpgPlayer member : members)
            if (member != sender)
                member.sendMessage(formatted);
    }

    public void sendBroadcast(RpgPlayer sender, String msg, Object... args) {
        if (args != null)
            for (int i = 0; i < args.length; i++)
                msg = msg.replaceAll("(?i)\\{" + i + "\\}", args[i].toString());

        this.sendBroadcast(sender, msg);
    }

    public void sendBroadcast(String msg) {
        for (RpgPlayer member : members)
            member.sendMessage(msg);
    }

    public void sendBroadcast(String msg, Object... args) {
        if (args != null)
            for (int i = 0; i < args.length; i++)
                msg = msg.replaceAll("(?i)\\{" + i + "\\}", args[i].toString());

        this.sendBroadcast(msg);
    }

    public int getMaxPopulation() {
        return this.maxPopulation;
    }

    public int getPopulation() {
        return members.size();
    }

    public double getRangeFactorTolerance() {
        return this.rfTolerance;
    }

    public double getMaterialFactorTolerance() {
        return this.mfTolerance;
    }

    public boolean isBanned(RpgPlayer player) {
        return PermissionManager.hasPermission(player.getBasePlayer(), "chat.banned." + this.systemName);
    }

    public boolean isModerator(RpgPlayer player) {
        return PermissionManager.hasPermission(player.getBasePlayer(), "chat.moderator." + this.systemName);
    }

    public boolean isChattableWorld(World world) {
        return validWorlds.isEmpty() || validWorlds.contains(world.getUID());
    }

    public boolean isInChannel(RpgPlayer player) {
        return members.contains(player);
    }

    public boolean isPrivateChannel() {
        return this.isPrivate;
    }

    public boolean isHiddenChannel() {
        return this.isHidden;
    }

    public boolean isCrossWorld() {
        return this.isCrossWorld;
    }

    public boolean isFull() {
        return members.size() == this.maxPopulation;
    }

    public boolean canJoin(RpgPlayer player) {
        return PermissionManager.hasAnyPermission(player.getBasePlayer(), "chat.join." + this.systemName, "chat.general." + this.systemName, "chat.moderate." + this.systemName) && !PermissionManager.hasPermission(player.getBasePlayer(), "chat.banned." + this.systemName);
    }

    public boolean canChat(RpgPlayer player) {
        return PermissionManager.hasAnyPermission(player.getBasePlayer(), "chat.chat." + this.systemName, "chat.general." + this.systemName, "chat.moderate." + this.systemName);
    }

    public boolean canLeave(RpgPlayer player) {
        return PermissionManager.hasAnyPermission(player.getBasePlayer(), "chat.leave." + this.systemName, "chat.general." + this.systemName, "chat.moderate." + this.systemName);
    }

    public boolean canKick(CommandSender sender, RpgPlayer target) {
        return PermissionManager.hasAnyPermission(sender, "chat.kick." + this.systemName, "chat.moderate." + this.systemName) && !PermissionManager.hasPermission(target.getBasePlayer(), "chat.moderate." + this.systemName);
    }

    public boolean canBan(CommandSender sender, RpgPlayer target) {
        return PermissionManager.hasAnyPermission(sender, "chat.ban." + this.systemName, "chat.moderate." + this.systemName) && !PermissionManager.hasPermission(target.getBasePlayer(), "chat.moderate." + this.systemName);
    }

    public boolean canPardon(RpgPlayer player) {
        return PermissionManager.hasAnyPermission(player.getBasePlayer(), "chat.pardon." + this.systemName, "chat.moderate." + this.systemName);
    }

    public File getFile() {
        return new File(Directories.CHAT_CHANNELS + systemName + ".yml");
    }

    public FileConfiguration getConfiguration() {
        return YamlConfiguration.loadConfiguration(this.getFile());
    }

    public void addMember(RpgPlayer player) {
        if (!members.contains(player)) {
            members.add(player);

            if (PhraseManager.phraseExists("chat.join-channel-announcement")) {
                Map<String, String> args = Util.buildPhraseArgs(player);

                args.put("channel-name", this.name);
                args.put("channel-symbol", this.symbol);
                args.put("channel-sysname", this.systemName);

                this.sendBroadcast(Util.buildPhrase(PhraseManager.getPhrase("chat.join-channel-announcement"), player, args));
            }
        }
    }

    public void removeMember(RpgPlayer player) {
        if (members.contains(player)) {
            members.remove(player);

            if (PhraseManager.phraseExists("chat.leave-channel-announcement")) {
                Map<String, String> args = Util.buildPhraseArgs(player);

                args.put("channel-name", this.name);
                args.put("channel-symbol", this.symbol);
                args.put("channel-sysname", this.systemName);

                this.sendBroadcast(Util.buildPhrase(PhraseManager.getPhrase("chat.leave-channel-announcement"), player, args));
            }

            if (player.getActiveChannel() == this) {
                List<ChatChannel> channels = ChannelManager.getJoinedChannels(player);

                if (channels.size() > 0)
                    player.setActiveChannel(channels.get(0).getSystemName());
                else
                    player.setActiveChannel(null);
            }
        }
    }

    public void banMember(RpgPlayer player) {
        if (PhraseManager.phraseExists("chat.player-banned-announcement")) {
            Map<String, String> args = Util.buildPhraseArgs(player);

            args.put("channel-name", this.name);
            args.put("channel-symbol", this.symbol);
            args.put("channel-sysname", this.systemName);

            this.sendBroadcast(Util.buildPhrase(PhraseManager.getPhrase("chat.player-banned-announcement"), player, args));
        }

        PermissionManager.addPermission(player.getBasePlayer(), "chat.banned." + this.systemName);

        this.removeMember(player);
    }

    public void pardonMember(RpgPlayer player) {
        PermissionManager.removePermission(player.getBasePlayer(), "chat.banned." + this.systemName);

        if (PhraseManager.phraseExists("chat.player-pardon-announcement")) {
            Map<String, String> args = Util.buildPhraseArgs(player);

            args.put("channel-name", this.name);
            args.put("channel-symbol", this.symbol);
            args.put("channel-sysname", this.systemName);

            this.sendBroadcast(Util.buildPhrase(PhraseManager.getPhrase("chat.player-pardon-announcement"), player, args));
        }
    }

    public void kickMember(RpgPlayer player) {
        if (PhraseManager.phraseExists("chat.player-kicked-announcement")) {
            Map<String, String> args = Util.buildPhraseArgs(player);

            args.put("channel-name", this.name);
            args.put("channel-symbol", this.symbol);
            args.put("channel-sysname", this.systemName);

            this.sendBroadcast(Util.buildPhrase(PhraseManager.getPhrase("chat.player-kicked-announcement"), player, args));
        }

        this.removeMember(player);
    }
}
