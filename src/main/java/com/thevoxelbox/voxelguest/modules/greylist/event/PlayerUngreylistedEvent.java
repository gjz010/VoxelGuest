package com.thevoxelbox.voxelguest.modules.greylist.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author Monofraps
 */
public class PlayerUngreylistedEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();
    private String playerName;

    public PlayerUngreylistedEvent(String playerName) {

        this.playerName = playerName;
    }

    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }

    public String getPlayerName()
    {
        return playerName;
    }

    public void setPlayerName(final String playerName)
    {
        this.playerName = playerName;
    }
}
