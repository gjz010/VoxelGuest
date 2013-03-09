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
 * Handles /vmodules commands.
 *
 * @author Monofraps
 */
public final class ModulesCommandExecutor implements TabExecutor
{
    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final Command command, final String s, final String[] strings)
    {
        return Lists.newArrayList("enable", "disable", "list");
    }

    @Override
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] args)
    {
        if (args.length < 1)
        {
            commandSender.sendMessage("Not enough arguments.");
            return false;
        }

        switch (args[0].toLowerCase())
        {
            case "list":
                listModules(commandSender);
                break;

            case "enable":
                if (args.length < 2)
                {
                    commandSender.sendMessage("Not enough arguments.");
                    return false;
                }
                if (enableModule(commandSender, args[1]))
                {
                    return true;
                }
                break;

            case "disable":
                if (args.length < 2)
                {
                    commandSender.sendMessage("Not enough arguments.");
                    return false;
                }
                if (disableModule(commandSender, args[1]))
                {
                    return true;
                }
                break;

            default:
                commandSender.sendMessage("Unknown sub command. Available: list, enable and disable");
        }


        return false;
    }

    private void listModules(final CommandSender commandSender)
    {
        final HashMap<Module, HashSet<Listener>> registeredModules = VoxelGuest.getModuleManagerInstance().getRegisteredModules();
        for (Module module : registeredModules.keySet())
        {
            commandSender.sendMessage(module.getClass().getName());
        }
    }

    private boolean enableModule(final CommandSender commandSender, final String moduleClassName)
    {
        final HashMap<Module, HashSet<Listener>> registeredModules = VoxelGuest.getModuleManagerInstance().getRegisteredModules();

        for (Module module : registeredModules.keySet())
        {
            if (module.getClass().getName().toLowerCase().endsWith(moduleClassName.toLowerCase()))
            {
                if (module.isEnabled())
                {
                    commandSender.sendMessage("Module already enabled.");
                    return true;
                }
                VoxelGuest.getModuleManagerInstance().enableModuleByType(module.getClass());
                return true;
            }
        }

        commandSender.sendMessage("No such module.");
        return false;
    }

    private boolean disableModule(final CommandSender commandSender, final String moduleClassName)
    {
        final HashMap<Module, HashSet<Listener>> registeredModules = VoxelGuest.getModuleManagerInstance().getRegisteredModules();
        for (Module module : registeredModules.keySet())
        {
            if (module.getClass().getName().toLowerCase().endsWith(moduleClassName.toLowerCase()))
            {
                if (!module.isEnabled())
                {
                    commandSender.sendMessage("Module is not enabled.");
                    return true;
                }
                VoxelGuest.getModuleManagerInstance().disableModuleByType(module.getClass());
                return true;
            }
        }

        commandSender.sendMessage("No such module.");
        return false;
    }

}
