package com.thevoxelbox.voxelguest.modules.greylist.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This event gets called right after a player was successfully added to the greylist.
 * @author Monofraps
 */
public class PlayerGreylistedEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();
    private String playerName;
    public PlayerGreylistedEvent(String playerName) {

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
