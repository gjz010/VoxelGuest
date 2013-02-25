package com.thevoxelbox.voxelguest.modules.regions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.thevoxelbox.voxelguest.persistence.Persistence;
/**
 * Handles the access, use, persistence, and management of all of the currently active regions.
 *
 * @author TheCryoknight
 */
public class RegionManager
{
    private static final String REGION_MODIFY_PERMISSION_PREFIX = "voxelguest.regions.modify.";

    private final Set<Region> activeRegions = new HashSet<>();

    public RegionManager()
    {
        this.initRegions();
    }

    /**
     * Adds a new region to the currently active list. Also, saves region to persistence.
     *
     * @param newRegion Region adding
     * @return True if successfully added to the active list.
     */
    public boolean addRegion(final Region newRegion)
    {
        Persistence.getInstance().save(newRegion);
        return this.activeRegions.add(newRegion);
    }

    /**
     * removes a old region from the currently active list. Also, removes region from persistence.
     *
     * @param oldRegion Region removing
     * @return True if successfully removed from the active list.
     */
    public boolean removeRegion(final Region oldRegion)
    {
        Persistence.getInstance().delete(oldRegion);
        return this.activeRegions.remove(oldRegion);
    }

    /**
     * Return region witch matches name provided, or null if no region match
     *
     * @param regionName
     * @return
     */
    public Region getRegion(final String regionName)
    {
        for (Region region : this.activeRegions)
        {
            if (region.getRegionName().equalsIgnoreCase(regionName))
            {
                return region;
            }
        }
        return null;
    }

    /**
     * Returns the first region that the location is residing in.
     * This should not be used because it does not support nested regions.
     *
     * @deprecated Use getRegionsAtLoc(final Location loc)
     * @param loc Location to find zone
     * @return The region that location is at
     */
    @Deprecated
    public Region getRegionAtLoc(final Location loc)
    {
        for (Region region : this.activeRegions)
        {
            if (region.inBounds(loc))
            {
                return region;
            }
        }
        return null;
    }

    /**
     * Returns a list of all of the regions at the specified location.
     *
     * @param loc Location to find regions from
     * @return A list of all of the regions at the specified location
     */
    public List<Region> getRegionsAtLoc(final Location loc)
    {
        final List<Region> regionsInBounds = new ArrayList<>();
        for (Region region : this.activeRegions)
        {
            if (region.inBounds(loc))
            {
                regionsInBounds.add(region);
            }
        }
        return regionsInBounds;
    }

    /**
     * Checks if a player can modify the region.
     * It is important to note that this is safe for nested and partially nested zones.
     * This is the least selective way of determining access if there are nested zones.
     * Such as a player only needs to be able to build in one of the regions that that are in to return true.
     *
     * @param player Player to check for building access
     * @param loc Location of the edit event
     * @return true if the player can edit the zone they are in
     */
    public boolean canPlayerModify(final Player player, final Location loc)
    {
        final List<Region> regionsInbounds = this.getRegionsAtLoc(loc);
        boolean canModify = false;
        for (Region region : regionsInbounds)
        {
            if (player.hasPermission(RegionManager.REGION_MODIFY_PERMISSION_PREFIX + region.getRegionName().toLowerCase()))
            {
                canModify = true;
            }
        }
        return canModify;
    }

    /**
     * Creates a list of all the names of the currently active regions.
     * 
     * @return list of region names
     */
    public List<String> getRegionNames()
    {
        final List<String> nameList = new ArrayList<>();
        for (Region region : this.activeRegions)
        {
            nameList.add(region.getRegionName());
        }
        return nameList;
    }

    public void initRegions()
    {
        final List<Region> regionObjects = Persistence.getInstance().loadAll(Region.class);
        for (Region region : regionObjects)
        {
            this.activeRegions.add(region);
        }
    }

    /**
     * Creates an array that contains all of the currently active regions.
     * Any modifications to this array will NOT effect the list of active regions.
     *
     * @return Array of active regions
     */
    public Region[] getActiveRegions()
    {
        return this.activeRegions.toArray(new Region[this.activeRegions.size()]);
    }
}
