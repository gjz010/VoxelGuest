package com.thevoxelbox.voxelguest.commands;

import com.google.common.collect.Lists;
import com.thevoxelbox.voxelguest.VoxelGuest;
import com.thevoxelbox.voxelguest.modules.Module;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * @author Monofraps
 */
public class ModulesCommandExecutor implements TabExecutor
{
    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final Command command, final String s, final String[] strings)
    {
        return Lists.newArrayList("enable", "disable", "list");
    }

    @Override
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] args)
    {
        if (args.length < 2)
        {
            commandSender.sendMessage("Not enough arguments.");
            return false;
        }

        switch (args[0].toLowerCase())
        {
            case "list":
            {
                final HashMap<Module, HashSet<Listener>> registeredModules = VoxelGuest.getModuleManagerInstance().getRegisteredModules();
                for (Module module : registeredModules.keySet())
                {
                    commandSender.sendMessage(module.getClass().getName());
                }
            }
            break;

            case "enable":
            {
                String moduleClassName = args[1];
                final HashMap<Module, HashSet<Listener>> registeredModules = VoxelGuest.getModuleManagerInstance().getRegisteredModules();
                for (Module module : registeredModules.keySet())
                {
                    if (module.getClass().getName().equalsIgnoreCase(moduleClassName))
                    {
                        if (module.isEnabled())
                        {
                            commandSender.sendMessage("Module already enabled.");
                            return true;
                        }
                        VoxelGuest.getModuleManagerInstance().enableModuleByType(module.getClass());
                    }
                }
            }
            break;

            case "disable":
            {
                String moduleClassName = args[1];
                final HashMap<Module, HashSet<Listener>> registeredModules = VoxelGuest.getModuleManagerInstance().getRegisteredModules();
                for (Module module : registeredModules.keySet())
                {
                    if (module.getClass().getName().equalsIgnoreCase(moduleClassName))
                    {
                        if (!module.isEnabled())
                        {
                            commandSender.sendMessage("Module is not enabled.");
                            return true;
                        }
                        VoxelGuest.getModuleManagerInstance().enableModuleByType(module.getClass());
                    }
                }
            }
            break;
        }


        return true;
    }
}
