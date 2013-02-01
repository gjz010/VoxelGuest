package com.thevoxelbox.voxelguest;

import java.io.File;

import com.thevoxelbox.voxelguest.modules.regions.RegionModule;
import com.thevoxelbox.voxelguest.persistence.Persistence;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author MikeMatrix
 * @author Monofraps
 */
public class VoxelGuest extends JavaPlugin
{
	private static VoxelGuest pluginInstance = null;
	private static ModuleManager moduleManagerInstance = null;

	public static VoxelGuest getPluginInstance()
	{
		return VoxelGuest.pluginInstance;
	}

	private static void setPluginInstance(VoxelGuest pluginInstance)
	{
		if (VoxelGuest.pluginInstance != null)
		{
			throw new RuntimeException("Guest Plugin Instance already set.");
		}

		VoxelGuest.pluginInstance = pluginInstance;
	}

	public static ModuleManager getModuleManagerInstance()
	{
		return moduleManagerInstance;
	}

	public static void setModuleManagerInstance(final ModuleManager moduleManagerInstance)
	{
		if (VoxelGuest.moduleManagerInstance != null)
		{
			throw new RuntimeException("Guest Module Manger Instance already set.");
		}

		VoxelGuest.moduleManagerInstance = moduleManagerInstance;
	}

	@Override
	public void onDisable()
	{
		VoxelGuest.getModuleManagerInstance().shutdown();
	}

	@Override
	public void onEnable()
	{
		VoxelGuest.setPluginInstance(this);
		VoxelGuest.setModuleManagerInstance(new ModuleManager());

        VoxelGuest.getModuleManagerInstance().registerGuestModule(new RegionModule(), false);
    }

    @Override
    public void onLoad()
    {
        Persistence.getInstance().initialize(new File(getDataFolder(), "persistence.db"));
    }
}
