package com.thevoxelbox.voxelguest.modules.helper;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ReviewRequest
{
    private final Location loc;
    private final Player guest;

    public ReviewRequest(final Player guest, final Location loc)
    {
        this.loc = loc;
        this.guest = guest;
    }

    /**
     * @return The location of whitelist review
     */
    public Location getLoc()
    {
        return loc;
    }

    /**
     * @return The guest that submitted the whitelist review
     */
    public Player getGuest()
    {
        return guest;
    }

}
