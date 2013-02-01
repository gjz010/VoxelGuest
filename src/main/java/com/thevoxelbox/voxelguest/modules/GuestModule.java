package com.thevoxelbox.voxelguest.modules;

/**
 * Implements all primitive basic functions to simplify module creation.
 *
 * @author MikeMatrix
 * @author Monofraps
 */
public abstract class GuestModule implements Module
{
	private String name = "Default Module Name (Yell at the developer if you see this!)";
	private boolean enabled = false;

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
