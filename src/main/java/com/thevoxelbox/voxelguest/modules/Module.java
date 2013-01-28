package com.thevoxelbox.voxelguest.modules;

import org.bukkit.event.Listener;

import java.util.Set;

/**
 * @author MikeMatrix
 */
public interface Module
{
	void onEnable();
	void onDisable();

	boolean isEnabled();
	Set<Listener> getListeners();
	String getName();
}
