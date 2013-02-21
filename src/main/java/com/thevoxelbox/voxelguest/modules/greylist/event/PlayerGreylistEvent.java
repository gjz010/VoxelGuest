package com.thevoxelbox.voxelguest.modules.greylist.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This event is called before a player gets added to the greylist.
 * @author Monofraps
 */
public class PlayerGreylistEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();
    private String playerName;
    private boolean cancelled = false;

    public PlayerGreylistEvent(String playerName) {

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

    public boolean isCancelled()
    {
        return cancelled;
    }

    public void setCancelled(final boolean cancelled)
    {
        this.cancelled = cancelled;
    }
}
