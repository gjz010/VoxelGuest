package com.thevoxelbox.voxelguest.modules.asshat;

import com.thevoxelbox.voxelguest.configuration.annotations.ConfigurationGetter;
import com.thevoxelbox.voxelguest.configuration.annotations.ConfigurationSetter;

/**
 * Represents the asshat module configuration.
 */
public class AsshatModuleConfiguration
{
    private String defaultAsshatReason = "asshat";
    private String banBroadcastMsg = "§8Player §c%playername%§8 has been banned by §c%admin%§8 for: §9%reason%";
    private String unbanBroadcastMsg = "§8Player §c%playername%§8 has been unbanned by §c%admin%";
    private String kickBroadcastMsg = "§8Player §c%playername%§8 has been kicked by §c%admin%§8 for: §9%reason%";
    private String gagBroadcastMsg = "§8Player §c%playername%§8 has been gagged by §c%admin%§8 for: §9%reason%";
    private String ungagBroadcastMsg = "§8Player §c%playername%§8 has been ungagged by §c%admin%";

    @ConfigurationGetter("asshat-reason")
    public final String getDefaultAsshatReason()
    {
        return defaultAsshatReason;
    }

    @ConfigurationSetter("asshat-reason")
    public final void setDefaultAsshatReason(final String defaultAsshatReason)
    {
        this.defaultAsshatReason = defaultAsshatReason;
    }

    @ConfigurationGetter("ban-message")
    public final String getBanBroadcastMsg()
    {
        return banBroadcastMsg;
    }

    @ConfigurationSetter("ban-message")
    public final void setBanBroadcastMsg(final String banBroadcastMsg)
    {
        this.banBroadcastMsg = banBroadcastMsg;
    }

    @ConfigurationGetter("unban-message")
    public final String getUnbanBroadcastMsg()
    {
        return unbanBroadcastMsg;
    }

    @ConfigurationSetter("unban-message")
    public final void setUnbanBroadcastMsg(final String unbanBroadcastMsg)
    {
        this.unbanBroadcastMsg = unbanBroadcastMsg;
    }

    @ConfigurationGetter("kick-message")
    public final String getKickBroadcastMsg()
    {
        return kickBroadcastMsg;
    }

    @ConfigurationSetter("kick-message")
    public final void setKickBroadcastMsg(final String kickBroadcastMsg)
    {
        this.kickBroadcastMsg = kickBroadcastMsg;
    }

    @ConfigurationGetter("gag-message")
    public final String getGagBroadcastMsg()
    {
        return gagBroadcastMsg;
    }

    @ConfigurationSetter("gag-message")
    public final void setGagBroadcastMsg(final String gagBroadcastMsg)
    {
        this.gagBroadcastMsg = gagBroadcastMsg;
    }

    @ConfigurationGetter("ungag-message")
    public final String getUngagBroadcastMsg()
    {
        return ungagBroadcastMsg;
    }

    @ConfigurationSetter("ungag-message")
    public final void setUngagBroadcastMsg(final String ungagBroadcastMsg)
    {
        this.ungagBroadcastMsg = ungagBroadcastMsg;
    }
}
