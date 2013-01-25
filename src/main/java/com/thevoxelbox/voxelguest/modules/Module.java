package com.thevoxelbox.voxelguest.modules;

import java.util.Set;

import org.bukkit.event.Listener;

/**
 * @author MikeMatrix
 */
public interface Module
{
    void onEnable();

    void onDisable();

    Set<Listener> getListeners();
}
