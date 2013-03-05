package com.thevoxelbox.voxelguest.modules.greylist.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author Monofraps
 */
public class PlayerUngreylistedEvent extends Event
{
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private String playerName;

    /**
     * Creates a new player ungreylisy event instance.
     * @param playerName The player who has been ungreylisted.
     */
    public PlayerUngreylistedEvent(final String playerName)
    {

        this.playerName = playerName;
    }

    @Override
    public final HandlerList getHandlers()
    {
        return HANDLER_LIST;
    }

    public final String getPlayerName()
    {
        return playerName;
    }

    public final void setPlayerName(final String playerName)
    {
        this.playerName = playerName;
    }
}
