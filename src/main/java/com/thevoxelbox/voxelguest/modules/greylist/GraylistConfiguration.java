package com.thevoxelbox.voxelguest.modules.greylist;

import com.thevoxelbox.voxelguest.configuration.annotations.ConfigurationGetter;
import com.thevoxelbox.voxelguest.configuration.annotations.ConfigurationSetter;

public class GraylistConfiguration
{
    private String streamPasswordHash = "changeme";
    private int streamPort = 8080;
    private String notGreylistedKickMessage = "You are not greylisted.";
    private String whitelistGroupName = "Member";
    private boolean explorationMode = false;
    private boolean streamGraylisting = false;

    @ConfigurationGetter("exploration-mode")
    public final boolean isExplorationMode()
    {
        return explorationMode;
    }

    @ConfigurationSetter("exploration-mode")
    public final void setExplorationMode(final boolean explorationMode)
    {
        this.explorationMode = explorationMode;
    }

    @ConfigurationGetter("not-greylisted-kick-message")
    public final String getNotGreylistedKickMessage()
    {
        return notGreylistedKickMessage;
    }

    @ConfigurationSetter("not-greylisted-kick-message")
    public final void setNotGreylistedKickMessage(final String notGreylistedKickMessage)
    {
        this.notGreylistedKickMessage = notGreylistedKickMessage;
    }
    
    @ConfigurationGetter("stream-port")
    public int getStreamPort()
    {
        return streamPort;
    }

    @ConfigurationSetter("stream-port")
    public void setStreamPort(final int streamPort)
    {
        this.streamPort = streamPort;
    }

    @ConfigurationGetter("stream-password")
    public String getStreamPasswordHash()
    {
        return streamPasswordHash;
    }

    @ConfigurationSetter("stream-password")
    public void setStreamPasswordHash(final String streamPasswordHash)
    {
        this.streamPasswordHash = streamPasswordHash;
    }

    @ConfigurationGetter("stream-enable")
    public boolean isStreamGraylisting()
    {
        return streamGraylisting;
    }

    @ConfigurationSetter("stream-enable")
    public void setStreamGraylisting(final boolean streamGraylisting)
    {
        this.streamGraylisting = streamGraylisting;
    }

    @ConfigurationGetter("wl-group-name")
    public String getWhitelistGroupName()
    {
        return whitelistGroupName;
    }

    @ConfigurationSetter("wl-group-name")
    public void setWhitelistGroupName(final String whitelistGroupName)
    {
        this.whitelistGroupName = whitelistGroupName;
    }
}
