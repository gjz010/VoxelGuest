/*
 * Copyright (C) 2011 - 2012, psanker and contributors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are 
 * permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice, this list of 
 *   conditions and the following 
 * * Redistributions in binary form must reproduce the above copyright notice, this list of 
 *   conditions and the following disclaimer in the documentation and/or other materials 
 *   provided with the distribution.
 * * Neither the name of Overcaffeinated Development nor the names of its contributors may be 
 *   used to endorse or promote products derived from this software without specific prior 
 *   written value.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS 
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE 
 * COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR 
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.thevoxelbox.voxelguest.commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.craftbukkit.v1_5_R2.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.thevoxelbox.voxelguest.VoxelGuest;
import com.thevoxelbox.voxelguest.modules.Module;
import com.thevoxelbox.voxelguest.permissions.InsufficientPermissionsException;
import com.thevoxelbox.voxelguest.permissions.PermissionsManager;

public class CommandManager {
    // =============================
    // - COMMAND ENGINE
    // - 
    // - Built by: psanker
    // =============================

    private String[] helpArgs = {"help", "h", "?"};
    protected Map<String, Method> aliases = new HashMap<String, Method>();
    protected Map<Method, Object> instances = new HashMap<Method, Object>();
    
    private final Plugin plugin;
    
    public CommandManager(Plugin pl)
    {
        plugin = pl;
    }

    public void registerCommands(Class<?> cls) 
    {
        CraftServer craftServer = (CraftServer) plugin.getServer();
        ArrayList<org.bukkit.command.Command> registeredCommands = new ArrayList<org.bukkit.command.Command>();
        
        Object obj = null;

        try {
            obj = cls.newInstance();
        } catch (InstantiationException ex) {
        	Bukkit.getLogger().warning("[VoxelGuest] Could not register commands from " + cls.getCanonicalName());
        } catch (IllegalAccessException ex) {
        	Bukkit.getLogger().warning("[VoxelGuest] Could not register commands from " + cls.getCanonicalName());
        }

        for (Method method : cls.getMethods()) {
            if (!method.isAnnotationPresent(Command.class)) {
                continue; // Improper command registration, helper method, or other method type
            }

            boolean isStatic = Modifier.isStatic(method.getModifiers());
            Command command = method.getAnnotation(Command.class);
            CommandPermission commandPermission = method.getAnnotation(CommandPermission.class);
            
            // Check for other already registered commands with this name
            if ((craftServer.getCommandMap().getCommand(command.aliases()[0]) != null) && (!VoxelGuest.getConfigData().getBoolean("override-other-commands"))) {
                org.bukkit.command.Command _cmd = craftServer.getCommandMap().getCommand(command.aliases()[0]);
                
                if (_cmd instanceof PluginIdentifiableCommand) {
                    PluginIdentifiableCommand _pic = (PluginIdentifiableCommand) _cmd;
                    Bukkit.getLogger().warning("Did not register command \"" + command.aliases()[0] + "\" because of already existing command from \"" + _pic.getPlugin().getName()  + "\"");
                    continue;
                }
                
                Bukkit.getLogger().warning("Did not register command \"" + command.aliases()[0] + "\" because of already existing command from unknown source");
                continue;
            }
            
            // Create Bukkit command hook
            GuestCommand pluginCommand = new GuestCommand(command.aliases()[0], plugin);
            
            if (commandPermission != null) {
                pluginCommand.setPermission(commandPermission.value());
            } else if (commandPermission == null) {
                pluginCommand.setPermission("");
            }
            
            if (pluginCommand.getUsage() != null) {
                pluginCommand.setUsage("");
            }
            
            if (pluginCommand.getDescription() != null) {
                pluginCommand.setDescription("");
            }

            // If not static, grab the instance to reference from.
            // If the instance doesn't exist, move on. Command is not registered.
            if (!isStatic) {
                if (obj == null) {
                    continue;
                } else {
                    instances.put(method, obj);
                }
            }

            List<String> aliasList = new ArrayList<String>();
            for (String alias : command.aliases()) {
                String al = alias.toLowerCase();

                aliases.put(al, method);
                
                if (!al.equalsIgnoreCase(pluginCommand.getName())) {
                    aliasList.add(al);
                }
            }
            
            pluginCommand.setAliases(aliasList);
            registeredCommands.add(pluginCommand);
        }
        
        craftServer.getCommandMap().registerAll(plugin.getName(), registeredCommands);
        
        
    }
    
    public void registerCommands(Object obj) 
    {
        Class<?> cls = obj.getClass();
        CraftServer craftServer = (CraftServer) plugin.getServer();
        ArrayList<org.bukkit.command.Command> registeredCommands = new ArrayList<org.bukkit.command.Command>();
        
        for (Method method : cls.getMethods()) {
            if (!method.isAnnotationPresent(Command.class)) {
                continue; // Improper command registration, helper method, or other method type
            }

            boolean isStatic = Modifier.isStatic(method.getModifiers());
            Command command = method.getAnnotation(Command.class);
            CommandPermission commandPermission = method.getAnnotation(CommandPermission.class);
            
            // Check for other already registered commands with this name
            if ((craftServer.getCommandMap().getCommand(command.aliases()[0]) != null) && (!VoxelGuest.getConfigData().getBoolean("override-other-commands"))) {
                org.bukkit.command.Command _cmd = craftServer.getCommandMap().getCommand(command.aliases()[0]);
                
                if (_cmd instanceof PluginIdentifiableCommand) {
                    PluginIdentifiableCommand _pic = (PluginIdentifiableCommand) _cmd;
                    Bukkit.getLogger().warning("Did not register command \"" + command.aliases()[0] + "\" because of already existing command from \"" + _pic.getPlugin().getName()  + "\"");
                    continue;
                }
                
                Bukkit.getLogger().warning("Did not register command \"" + command.aliases()[0] + "\" because of already existing command from unknown source");
                continue;
            }
            
            // Create Bukkit command hook
            GuestCommand pluginCommand = new GuestCommand(command.aliases()[0], plugin);
            
            if (commandPermission != null && pluginCommand.getPermission() != null) {
                pluginCommand.setPermission(commandPermission.value());
            } else if (commandPermission == null & pluginCommand.getPermission() != null) {
                pluginCommand.setPermission("");
            }
            
            if (pluginCommand.getUsage() != null) {
                pluginCommand.setUsage("");
            }
            
            if (pluginCommand.getDescription() != null) {
                pluginCommand.setDescription("");
            }

            // If not static, grab the instance to reference from.
            // If the instance doesn't exist, move on. Command is not registered.
            if (!isStatic) {
                if (obj == null) {
                    continue;
                } else {
                    instances.put(method, obj);
                }
            }

            List<String> aliasList = new ArrayList<String>();
            for (String alias : command.aliases()) {
                String al = alias.toLowerCase();

                aliases.put(al, method);
                
                if (!al.equalsIgnoreCase(pluginCommand.getName())) {
                    aliasList.add(al);
                }
            }
            
            pluginCommand.setAliases(aliasList);
            registeredCommands.add(pluginCommand);
        }
        
        craftServer.getCommandMap().registerAll(plugin.getName(), registeredCommands);
        
        
        
    }

    private boolean isRegistered(String command) 
    {
        return aliases.containsKey(command.toLowerCase());
    }
    
    
    public boolean executeCommandProcessErrors(org.bukkit.command.Command command, CommandSender cs, String[] args, Plugin owner)
    {
        try {
            executeCommand(command, cs, args);
        } catch (CommandException ex) {
            String report = new StringBuilder().append("§c").append(ex.getMessage()).toString();
            cs.sendMessage(report);

            if (((ex instanceof CommandMethodInvocationException)) || ((ex instanceof MalformattedCommandException))) {
                Bukkit.getLogger().severe(ex.getMessage());
                ex.printStackTrace();
                return true;
            }
            if ((ex instanceof ArgumentOutOfBoundsException)) {
                try {
                    sendHelp(cs, command);
                } catch (MalformattedCommandException ex1) {
                    String _report = new StringBuilder().append("§c").append(ex1.getMessage()).toString();
                    cs.sendMessage(_report);

                    Bukkit.getLogger().severe(ex.getMessage());
                    ex.printStackTrace();
                    return true;
                }
            }
        } catch (InsufficientPermissionsException ex) {
            String report = new StringBuilder().append("§c").append(ex.getMessage()).toString();
            cs.sendMessage(report);
        }

        return true;
    }

    public void executeCommand(org.bukkit.command.Command command, CommandSender cs, String[] args) 
            throws CommandException, InsufficientPermissionsException 
    {
        // Search if command is registered
        if (!this.isRegistered(command.getName())) {
            throw new UnhandledCommandException("Unhandled command: " + command.getName());
        }

        // Get method and check to see if it matches the Command method interface
        Method method = aliases.get(command.getName());

        if (!method.isAnnotationPresent(Command.class)) {
            throw new MalformattedCommandException("Malformatted command: " + command.getName());
        }

        Command cmd = method.getAnnotation(Command.class);

        // Check out of bounds for arguments and other things like such
        boolean playerOnly = cmd.playerOnly();

        if (playerOnly && !(cs instanceof Player)) {
            throw new CommandException("Player-only command: " + command.getName());
        }

        int[] bounds = cmd.bounds();
        
        if (args.length < bounds[0] || (args.length > bounds[1] && bounds[1] >= 0)) {
            throw new ArgumentOutOfBoundsException("Argument out of bounds: " + command.getName());
        }

        if (args.length == 1 && Arrays.asList(helpArgs).contains(args[0])) {
            sendHelp(cs, command);
            return;
        }

        if (method.isAnnotationPresent(CommandPermission.class)) {
            CommandPermission perm = method.getAnnotation(CommandPermission.class);

            // -- Check if cs is player or not
            if (cs instanceof Player) {
                Player p = (Player) cs;
                
                if (!PermissionsManager.getHandler().hasPermission(p.getName(), "system.admin")) {
                    if (!PermissionsManager.getHandler().hasPermission(p.getName(), perm.value())) {
                        throw new InsufficientPermissionsException();
                    } 
                }     
            }
        }
        
        if (method.isAnnotationPresent(Subcommands.class)) {
            Subcommands subs = method.getAnnotation(Subcommands.class);
            
            if (cs instanceof Player) {
                Player p = (Player) cs;
                
                
                if (!PermissionsManager.getHandler().hasPermission(p.getName(), "system.admin")) {
                    try {
                        if (Arrays.asList(subs.arguments()).contains(args[0])) {
                            for (int i = 0; i < subs.arguments().length; i++) {
                                if (subs.arguments()[i].equalsIgnoreCase(args[0]) && !PermissionsManager.getHandler().hasPermission(p.getName(), subs.permission()[i])) {
                                    throw new InsufficientPermissionsException();
                                }
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        // Continue
                    }
                }
            }
        }

        // Checks clear... Run command
        Object instance = instances.get(method);
        invokeMethod(method, cs, args, instance);
    }

    private void invokeMethod(Method method, CommandSender cs, String[] args, Object instance) 
            throws CommandMethodInvocationException 
    {
        Object[] commandMethodArgs = {cs, args};

        try {
            method.invoke(instance, commandMethodArgs);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            throw new CommandMethodInvocationException("Internal error. Could not execute command.");
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
           throw new CommandMethodInvocationException("Internal error. Could not execute command.");
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
            throw new CommandMethodInvocationException("Internal error. Could not execute command.");
        }
    }
    
    public void sendHelp(CommandSender cs, org.bukkit.command.Command command) 
            throws MalformattedCommandException 
    {
        Method method = aliases.get(command.getName());
        
        if (!method.isAnnotationPresent(Command.class)) {
            throw new MalformattedCommandException("Malformatted command: " + command.getName());
        }

        Command cmd = method.getAnnotation(Command.class);
        
        String help = "§6===Help: " + command.getName() + "===\n" + cmd.help() + "\n" + "§6=========================";
        
        for (String str : getMessageLines(help)) {
            cs.sendMessage(str);
        }
    }
    
    private static String[] getMessageLines(String message) {
        String[] split = message.split("\n");
        return split;
    }
}