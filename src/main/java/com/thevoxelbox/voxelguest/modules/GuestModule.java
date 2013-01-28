package com.thevoxelbox.voxelguest.modules;

import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;

/**
 * @author MikeMatrix
 */
public abstract class GuestModule implements Module
{
	private String name = "Default Module Name (Yell at the developer if you see this!)";
	protected boolean enabled = false;
	protected Set<Listener> eventListeners = new HashSet<>();

	@Override
	public void onEnable() {
		enabled = true;
	}

	@Override
	public void onDisable() {
		enabled = false;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	@Override
	public Set<Listener> getListeners() {
		return eventListeners;
	}

	@Override
	public String toString() {
		return getName();
	}
}
