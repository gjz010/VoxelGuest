package com.thevoxelbox.voxelguest.modules;

import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;

/**
 * @author MikeMatrix
 */
public interface Module
{
	void onEnable();
	void onDisable();

	boolean isEnabled();
	HashSet<Listener> getListeners();
	String getName();
}
