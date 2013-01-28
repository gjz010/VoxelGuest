package com.thevoxelbox.voxelguest;

import com.thevoxelbox.voxelguest.modules.Module;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Monofraps
 */
public class ModuleManager      // implements ModuleManager -- TODO: export API stuff
{
	// maps module <-> registered event listeners
	private HashMap<Module, Set<Listener>> registeredModules = new HashMap<>();

	/**
	 * Registers a guest module and enables it immediately if parameter enable is true.
	 *
	 * @param module The instance of the module class to register.
	 * @param enable If set to true, the method will enable the module immediately after registration by calling enableModule
	 */
	public final void registerGuestModule(final Module module, final boolean enable)
	{
		assert module != null;

		if (this.registeredModules.containsKey(module))
		{
			Bukkit.getLogger().severe(String.format("Module already registered. (Module: %s)", module.toString()));
			return;
		}

		this.registeredModules.put(module, new HashSet<Listener>());

		if (enable)
		{
			try
			{
				enableModuleByInstance(module);
			} catch (Exception ex)
			{
				Bukkit.getLogger().severe(String.format("Failed to enable module %s", module.toString()));
				ex.printStackTrace();
			}
		}
	}

	//TODO: Could be made private ...?
	/**
	 * Enables a give module.
	 *
	 * @param module The instance of the module to enable.
	 */
	public final void enableModuleByInstance(final Module module)
	{
		assert module != null;

		final boolean isRegisteredModule = this.registeredModules.containsKey(module);
		if (!isRegisteredModule)
		{
			Bukkit.getLogger().warning("Guest module manager was asked to activate a non-registered module. I will activate the module but cannot keep track of its state and listeners.");
		}

		if (module.isEnabled())
		{
			Bukkit.getLogger().warning(String.format("Module already enabled. (Module: %s)", module.toString()));
			return;
		}

		module.onEnable();

		// try block prevents loose event listeners from floating around, when the registration fails at some point
		// it also prevents incomplete modules from staying enabled
		try
		{
			final Set<Listener> moduleListeners = module.getListeners();

			if (!moduleListeners.isEmpty())
			{
				Set<Listener> internalListenerTracker = null;
				if (isRegisteredModule)
				{
					internalListenerTracker = this.registeredModules.get(module);
				}

				int numRegisteredListeners = 0;
				for (Listener listener : moduleListeners)
				{
					Bukkit.getPluginManager().registerEvents(listener, VoxelGuest.getPluginInstance());
					if (isRegisteredModule)
					{
						internalListenerTracker.add(listener);
					}
					numRegisteredListeners++;
				}

				Bukkit.getLogger().info(String.format("Registered %d event listeners for module %s", numRegisteredListeners, module.toString()));
			}
		} catch (Exception ex)
		{
			Bukkit.getLogger().severe(String.format("Exception while enabling module: %s", ex.getMessage()));
			ex.printStackTrace();
			disableModuleByInstance(module);
		}
	}

	/**
	 * Enables all registered modules of type [module]
	 *
	 * @param module The type of the modules to enable.
	 */
	public final void enableModulesByType(final Class<? extends Module> module)
	{
		assert module != null;

		for (Module registeredModule : this.registeredModules.keySet())
		{
			if (module.isAssignableFrom(registeredModule.getClass()))
			{
				try
				{
					enableModuleByInstance(registeredModule);
				} catch (Exception ex)
				{
					Bukkit.getLogger().severe(String.format("Exception while enabling module: %s", ex.getMessage()));
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * Disables a give module.
	 *
	 * @param module The instance of the module to disable.
	 */
	public final void disableModuleByInstance(final Module module)
	{
		assert module != null;

		final boolean isRegisteredModule = this.registeredModules.containsKey(module);
		if (!isRegisteredModule)
		{
			Bukkit.getLogger().warning("Guest module manager was asked to disable a non-registered module. I will disable the module but cannot keep track of its state and listeners.");
		}

		if (!module.isEnabled())
		{
			Bukkit.getLogger().warning(String.format("Module already disabled. (Module: %s)", module.toString()));
			return;
		}

		try
		{
			// unregister the module listeners
			try
			{
				final Set<Listener> moduleListeners = module.getListeners();
				if (isRegisteredModule)
				{
					moduleListeners.addAll(this.registeredModules.get(module));
					this.registeredModules.get(module).clear();
				}

				if (!moduleListeners.isEmpty())
				{
					for (Listener listener : moduleListeners)
					{
						Bukkit.getPluginManager().registerEvents(listener, VoxelGuest.getPluginInstance());
						HandlerList.unregisterAll(listener);
					}
				}
			} catch (Exception ex)
			{
				Bukkit.getLogger().severe("Failed to unregister module listeners.");
				ex.printStackTrace();
			} finally
			{
				if (isRegisteredModule)
				{
					registeredModules.get(module).clear();
				}
			}

			module.onDisable();
		} catch (Exception ex)
		{
			Bukkit.getLogger().severe(String.format("Exception while disabling module: %s", ex.getMessage()));
			ex.printStackTrace();
		}
	}

	/**
	 * Disables all modules of type [module].
	 *
	 * @param module The type of the modules to disable.
	 */
	public final void disableModulesByType(final Class<? extends Module> module)
	{
		assert module != null;

		for (Module registeredModule : this.registeredModules.keySet())
		{
			if (module.isAssignableFrom(registeredModule.getClass()))
			{
				try
				{
					disableModuleByInstance(registeredModule);
				} catch (Exception ex)
				{
					Bukkit.getLogger().severe(String.format("Exception while disabling module: %s", ex.getMessage()));
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * Restarts or just enables a give module. It calls disableModuleByInstance and enableModuleByInstance internally.
	 * @param module The instance of the module to restart.
	 */
	public void restartModule(final Module module)
	{
		assert module != null;

		if (!module.isEnabled())
		{
			enableModuleByInstance(module);
			return;
		}

		try
		{
			disableModuleByInstance(module);
			enableModuleByInstance(module);
		} catch (Exception ex)
		{
			Bukkit.getLogger().severe(String.format("Failed to restart module %s because: %s", module.toString(), ex.getMessage()));
			ex.printStackTrace();
		}
	}

	public final void shutdown()
	{
		for (Module module : registeredModules.keySet())
		{
			if (module.isEnabled())
			{
				try
				{
					disableModuleByInstance(module);
				} catch (Exception ex)
				{
					Bukkit.getLogger().severe(String.format("Failed to disable module %s because: %s", module.toString(), ex.getMessage()));
					ex.printStackTrace();
				}
			}
		}

		registeredModules.clear();
	}
}
