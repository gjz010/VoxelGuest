package com.thevoxelbox.voxelguest.modules.worldprotection;

import com.thevoxelbox.voxelguest.modules.GuestModule;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.omg.CORBA.SetOverrideType;

import java.util.HashSet;
import java.util.Set;

import com.thevoxelbox.voxelguest.modules.GuestModule;

import org.bukkit.World;

/**
 * @author Monofraps
 */
public class WorldProtectionModule extends GuestModule
{
	private Set<String> protectedWorlds = new HashSet<>();

	BlockEventListener blockListener;

	public WorldProtectionModule() {
		setName("World Protection");

		blockListener = new BlockEventListener(this);
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public HashSet<Listener> getListeners()
	{
		final HashSet<Listener> listeners = new HashSet<>();
		listeners.add(blockListener);

		return listeners;
	}

	public boolean isProtectedWorld(World world) {
		return protectedWorlds.contains(world.getName());
	}

	public boolean isProtectedWorld(String worldName) {
		return protectedWorlds.contains(worldName);
	}
}
