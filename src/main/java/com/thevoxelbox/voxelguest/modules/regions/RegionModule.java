/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thevoxelbox.voxelguest.modules.regions;

import com.thevoxelbox.voxelguest.modules.GuestModule;
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

	public RegionModule()
	{
		setName("Region Module");

		blockEventListener = new BlockEventListener(this);
	}

	@Override
	public final void onEnable()
	{
		super.onEnable();
	}

	@Override
	public final void onDisable()
	{
		super.onDisable();
	}

	@Override
	public final HashSet<Listener> getListeners()
	{
		final HashSet<Listener> listeners = new HashSet<>();
		listeners.add(blockEventListener);

		return listeners;
	}

	@Override
	public Object getConfiguration()
	{
		return null;
	}

	@Override
	public String getConfigFileName()
	{
		return "region";
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
