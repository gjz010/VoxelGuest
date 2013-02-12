package com.thevoxelbox.voxelguest.modules.regions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.thevoxelbox.voxelguest.persistence.Persistence;

public class RegionManager
{
    private static final String REGION_MODIFY_PERMISSION_PREFIX = "voxelguest.regions.modify.";

    private final Set<Region> activeRegions = new HashSet<>();

    public RegionManager()
    {
        this.initRegions();
    }

    public boolean addRegion(Region newRegion)
    {
        Persistence.getInstance().save(newRegion);
        return this.activeRegions.add(newRegion);
    }

    public void removeRegion(Region oldRegion)
    {
        Persistence.getInstance().delete(oldRegion);
        this.activeRegions.remove(oldRegion);
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

    public Region getRegionAtLoc(final Location loc)
    {
        //TODO: create smarter algorithms to deal with nested and partially nested zones
        for (Region region : this.activeRegions)
        {
            if (region.inBounds(loc))
            {
                return region;
            }
        }
        return null;
    }

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
    
    public void initRegions()
    {
        final List<Region> regionObjects = Persistence.getInstance().loadAll(Region.class);
        for (Region region : regionObjects)
        {
            this.activeRegions.add(region);
        }
    }
    public boolean canPlayerModify(Player player, Location loc)
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
}
