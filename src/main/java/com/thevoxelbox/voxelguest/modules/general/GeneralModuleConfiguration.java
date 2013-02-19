package com.thevoxelbox.voxelguest.modules.general;

import com.thevoxelbox.voxelguest.configuration.annotations.ConfigurationGetter;
import com.thevoxelbox.voxelguest.configuration.annotations.ConfigurationSetter;
import org.bukkit.ChatColor;

/**
 * @author Monofraps
 */
public class GeneralModuleConfiguration
{
    private String adminColor = ChatColor.GOLD.toString();
    private String curatorColor = ChatColor.DARK_PURPLE.toString();
    private String sniperColor = ChatColor.DARK_GREEN.toString();
    private String liteSniperColor = ChatColor.GREEN.toString();
    private String memberColor = ChatColor.WHITE.toString();
    private String guestColor = ChatColor.GRAY.toString();
    private String visitorColor = ChatColor.DARK_GRAY.toString();
    private String vipColor = ChatColor.DARK_AQUA.toString();
    private boolean randomAfkMsgs = true;
    private boolean defaultWatchTPSState = true;

    private String kickFormat = ChatColor.DARK_GRAY
            + "(" + ChatColor.GOLD + "$no" + ChatColor.DARK_GRAY + ") "
            + ChatColor.DARK_AQUA + "$n" + ChatColor.DARK_RED + " was kicked out";

    private String leaveFormat = ChatColor.DARK_GRAY
            + "(" + ChatColor.GOLD + "$no" + ChatColor.DARK_GRAY + ") "
            + ChatColor.DARK_AQUA + "$n" + ChatColor.GRAY + " left";

    private String joinFormat = ChatColor.DARK_GRAY
            + "(" + ChatColor.GOLD + "$no" + ChatColor.DARK_GRAY + ") "
            + ChatColor.DARK_AQUA + "$n" + ChatColor.GRAY + " joined";

    private String fakequitPrefix = ChatColor.DARK_GRAY
            + "[" + ChatColor.RED + "FQ" + ChatColor.DARK_GRAY + "]";

    private int permGenShutdownThreshold = 80;
    private int permGenWarningThreshold = 65;

    @ConfigurationGetter("admin-color")
    public final String getAdminColor()
    {
        return adminColor;
    }

    @ConfigurationSetter("admin-color")
    public final void setAdminColor(final String adminColor)
    {
        this.adminColor = adminColor;
    }

    @ConfigurationGetter("curator-color")
    public final String getCuratorColor()
    {
        return curatorColor;
    }

    @ConfigurationSetter("curator-color")
    public final void setCuratorColor(final String curatorColor)
    {
        this.curatorColor = curatorColor;
    }

    @ConfigurationGetter("sniper-color")
    public final String getSniperColor()
    {
        return sniperColor;
    }

    @ConfigurationSetter("sniper-color")
    public final void setSniperColor(final String sniperColor)
    {
        this.sniperColor = sniperColor;
    }

    @ConfigurationGetter("litesniper-color")
    public final String getLiteSniperColor()
    {
        return liteSniperColor;
    }

    @ConfigurationSetter("litesniper-color")
    public final void setLiteSniperColor(final String liteSniperColor)
    {
        this.liteSniperColor = liteSniperColor;
    }

    @ConfigurationGetter("member-color")
    public final String getMemberColor()
    {
        return memberColor;
    }

    @ConfigurationSetter("admin-color")
    public final void setMemberColor(final String memberColor)
    {
        this.memberColor = memberColor;
    }

    @ConfigurationGetter("guest-color")
    public final String getGuestColor()
    {
        return guestColor;
    }

    @ConfigurationSetter("guest-color")
    public final void setGuestColor(final String guestColor)
    {
        this.guestColor = guestColor;
    }

    @ConfigurationGetter("visitor-color")
    public final String getVisitorColor()
    {
        return visitorColor;
    }

    @ConfigurationSetter("visitor-color")
    public final void setVisitorColor(final String visitorColor)
    {
        this.visitorColor = visitorColor;
    }

    @ConfigurationGetter("vip-color")
    public final String getVipColor()
    {
        return vipColor;
    }

    @ConfigurationSetter("vip-color")
    public final void setVipColor(final String vipColor)
    {
        this.vipColor = vipColor;
    }

    @ConfigurationGetter("kick-format")
    public final String getKickFormat()
    {
        return kickFormat;
    }

    @ConfigurationSetter("kick-format")
    public final void setKickFormat(final String kickFormat)
    {
        this.kickFormat = kickFormat;
    }

    @ConfigurationGetter("leave-format")
    public final String getLeaveFormat()
    {
        return leaveFormat;
    }

    @ConfigurationSetter("leave-format")
    public final void setLeaveFormat(final String leaveFormat)
    {
        this.leaveFormat = leaveFormat;
    }

    @ConfigurationGetter("join-format")
    public final String getJoinFormat()
    {
        return joinFormat;
    }

    @ConfigurationSetter("join-format")
    public final void setJoinFormat(final String joinFormat)
    {
        this.joinFormat = joinFormat;
    }

    @ConfigurationGetter("fakequit-prefix")
    public final String getFakequitPrefix()
    {
        return fakequitPrefix;
    }

    @ConfigurationSetter("fakequit-prefix")
    public final void setFakequitPrefix(final String fakequitPrefix)
    {
        this.fakequitPrefix = fakequitPrefix;
    }

    @ConfigurationGetter("permgen-shutdown-threshold")
    public int getPermGenShutdownThreshold()
    {
        return permGenShutdownThreshold;
    }

    @ConfigurationSetter("permgen-shutdown-threshold")
    public void setPermGenShutdownThreshold(final int permGenShutdownThreshold)
    {
        this.permGenShutdownThreshold = permGenShutdownThreshold;
    }

    @ConfigurationGetter("permgen-warning-threshold")
    public int getPermGenWarningThreshold()
    {
        return permGenWarningThreshold;
    }

    @ConfigurationSetter("permgen-warning-threshold")
    public void setPermGenWarningThreshold(final int permGenWarningThreshold)
    {
        this.permGenWarningThreshold = permGenWarningThreshold;
    }

    @ConfigurationGetter("random-afk-messages")
    public boolean isRandomAfkMsgs()
    {
        return randomAfkMsgs;
    }

    @ConfigurationSetter("random-afk-messages")
    public void setRandomAfkMsgs(boolean randomAfkMsgs)
    {
        this.randomAfkMsgs = randomAfkMsgs;
    }

    @ConfigurationGetter("force-watch-tps")
    public boolean getDefaultWatchTPSState()
    {
        return defaultWatchTPSState;
    }

    @ConfigurationSetter("force-watch-tps")
    public void setDefaultWatchTPSState(boolean defaultWatchTPSState)
    {
        this.defaultWatchTPSState = defaultWatchTPSState;
    }
}
