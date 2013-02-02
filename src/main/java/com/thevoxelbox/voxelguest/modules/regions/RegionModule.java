package com.thevoxelbox.voxelguest.modules.regions;

import com.thevoxelbox.voxelguest.modules.GuestModule;
import com.thevoxelbox.voxelguest.persistence.Persistence;
import org.bukkit.Location;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author Joe
 */
public class RegionModule extends GuestModule
{
    private List<Region> regions = new ArrayList<>();
    private BlockEventListener blockEventListener;
    private PlayerEventListener playerEventListener;

    public RegionModule()
    {
        setName("Region Module");

        blockEventListener = new BlockEventListener(this);
        playerEventListener = new PlayerEventListener(this);
    }

    @Override
    public final void onEnable()
    {
        super.onEnable();

	    Persistence.getInstance().registerPersistentClass(Region.class);

	    regions.clear();
	    List<Object> protoList = Persistence.getInstance().loadAll(Region.class);
	    for (Object protoRegion : protoList)
	    {
		    regions.add((Region) protoRegion);
	    }
    }

    @Override
    public final void onDisable()
    {
	    regions.clear();
	    List<Object> protoList = new ArrayList<>();
	    for (Region region : regions)
	    {
		    protoList.add(region);
	    }

        super.onDisable();
    }

    @Override
    public String getConfigFileName()
    {
        return "region";
    }

    @Override
    public Object getConfiguration()
    {
        return null;
    }

    @Override
    public final HashSet<Listener> getListeners()
    {
        final HashSet<Listener> listeners = new HashSet<>();
        listeners.add(blockEventListener);
        listeners.add(playerEventListener);

        return listeners;
    }

    public final Region getRegionAtLocation(final Location regionLocation)
    {
        for (Region region : regions)
        {
            if (region.isLocationInRegion(regionLocation))
            {
                return region;
            }
        }
        return null;
    }

}
