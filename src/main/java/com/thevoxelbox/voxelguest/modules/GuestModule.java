package com.thevoxelbox.voxelguest.modules;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.event.Listener;

/**
 * @author MikeMatrix
 */
public abstract class GuestModule implements Module
{
    protected boolean enabled = false;
    protected Set<Listener> eventListeners = new HashSet<>();
    private String name = "Default Module Name (Yell at the developer if you see this!)";

    @Override
    public void onEnable()
    {
        enabled = true;
    }

    @Override
    public void onDisable()
    {
        enabled = false;
    }

    @Override
    public boolean isEnabled()
    {
        return enabled;
    }

    @Override
    public Set<Listener> getListeners()
    {
        return eventListeners;
    }

    @Override
    public String getName()
    {
        return name;
    }

    protected void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return getName();
    }
}
