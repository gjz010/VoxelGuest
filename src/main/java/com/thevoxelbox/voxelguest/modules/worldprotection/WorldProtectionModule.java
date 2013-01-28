package com.thevoxelbox.voxelguest.modules.worldprotection;

import com.thevoxelbox.voxelguest.modules.GuestModule;
import org.bukkit.World;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Monofraps
 */
public class WorldProtectionModule extends GuestModule
{
	private Set<String> protectedWorlds = new HashSet<>();

	public WorldProtectionModule() {
		setName("World Protection");
	}

	@Override
	public void onEnable() {
		this.eventListeners.add(new BlockEventListener(this));

		super.onEnable();
	}

	@Override
	public void onDisable() {
		this.eventListeners.clear();

		super.onDisable();
	}

	public boolean isProtectedWorld(World world) {
		return protectedWorlds.contains(world.getName());
	}

	public boolean isProtectedWorld(String worldName) {
		return protectedWorlds.contains(worldName);
	}
}
