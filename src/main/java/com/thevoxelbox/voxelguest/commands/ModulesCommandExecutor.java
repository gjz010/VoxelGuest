package com.thevoxelbox.voxelguest.commands;

import com.thevoxelbox.voxelguest.VoxelGuest;
import com.thevoxelbox.voxelguest.modules.Module;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    private static final String[] subcommands = {"enable", "disable", "list"};

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args)
    {
        if (sender.hasPermission("voxelguest.manage.modules"))
        {
            final List<String> matches = new ArrayList<>();
            if (args.length >= 1)
            {
                if (args.length == 1)
                {
                    for (String subcommand : subcommands)
                    {
                        if (subcommand.startsWith(args[0].toLowerCase()))
                        {
                            matches.add(subcommand);
                        }
                    }
                }
                else if (args.length == 2)
                {
                    final HashMap<Module, HashSet<Listener>> registeredModules = VoxelGuest.getModuleManagerInstance().getRegisteredModules();
                    for (Module module : registeredModules.keySet())
                    {
                        final String className = module.getClass().getName();
                        if (className.toLowerCase().startsWith(args[1].toLowerCase()))
                        {
                            matches.add(className);
                        }
                    }
                }
            }
            else
            {
                matches.addAll(Arrays.asList(subcommands));
            }
            Collections.sort(matches);
            return matches;
        }
        return null;
    }

    @Override
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] args)
    {
        if (args.length < 1)
        {
            commandSender.sendMessage(ChatColor.RED + "Not enough arguments.");
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
                    commandSender.sendMessage(ChatColor.RED + "Not enough arguments.");
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
                    commandSender.sendMessage(ChatColor.RED + "Not enough arguments.");
                    return false;
                }
                if (disableModule(commandSender, args[1]))
                {
                    return true;
                }
                break;

            default:
                commandSender.sendMessage("Unknown Subcommand. Available: list, enable, and disable");
        }


        return false;
    }

    private void listModules(final CommandSender sender)
    {
        final HashMap<Module, HashSet<Listener>> registeredModules = VoxelGuest.getModuleManagerInstance().getRegisteredModules();

        sender.sendMessage(ChatColor.GREEN + "Registered Modules");
        sender.sendMessage(ChatColor.GRAY + "-------------------");

        for (Module module : registeredModules.keySet())
        {
            sender.sendMessage((module.isEnabled() ? ChatColor.GREEN : ChatColor.RED) + module.getClass().getName());
        }
    }

    private boolean enableModule(final CommandSender sender, final String moduleClassName)
    {
        final HashMap<Module, HashSet<Listener>> registeredModules = VoxelGuest.getModuleManagerInstance().getRegisteredModules();

        for (Module module : registeredModules.keySet())
        {
            if (module.getClass().getName().toLowerCase().endsWith(moduleClassName.toLowerCase()))
            {
                if (module.isEnabled())
                {
                    sender.sendMessage(ChatColor.RED + module.getName() + " is already enabled.");
                    return true;
                }
                VoxelGuest.getModuleManagerInstance().enableModuleByType(module.getClass());
                sender.sendMessage(ChatColor.GRAY + module.getName() + " has been enabled!");
                return true;
            }
        }

        sender.sendMessage(ChatColor.RED + "No such module registered.");
        return false;
    }

    private boolean disableModule(final CommandSender sender, final String moduleClassName)
    {
        final HashMap<Module, HashSet<Listener>> registeredModules = VoxelGuest.getModuleManagerInstance().getRegisteredModules();
        for (Module module : registeredModules.keySet())
        {
            if (module.getClass().getName().toLowerCase().endsWith(moduleClassName.toLowerCase()))
            {
                if (!module.isEnabled())
                {
                    sender.sendMessage(ChatColor.RED + module.getName() + " is not enabled.");
                    return true;
                }
                VoxelGuest.getModuleManagerInstance().disableModuleByType(module.getClass());
                sender.sendMessage(ChatColor.GRAY + module.getName() + " has been disabled!");
                return true;
            }
        }

        sender.sendMessage(ChatColor.RED + "No such module.");
        return false;
    }

}
