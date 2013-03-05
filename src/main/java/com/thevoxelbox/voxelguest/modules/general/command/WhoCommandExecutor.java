package com.thevoxelbox.voxelguest.modules.general.command;

import com.google.common.base.Preconditions;
import com.thevoxelbox.voxelguest.VoxelGuest;
import com.thevoxelbox.voxelguest.modules.general.GeneralModule;
import com.thevoxelbox.voxelguest.modules.general.GeneralModuleConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Handles the who command.
 */
public class WhoCommandExecutor implements CommandExecutor
{
    private final GeneralModule module;
    private final GeneralModuleConfiguration configuration;

    /**
     * Creates a new instance of the who command executor.
     * @param generalModule The owning module.
     */
    public WhoCommandExecutor(final GeneralModule generalModule)
    {
        this.module = generalModule;
        this.configuration = (GeneralModuleConfiguration) generalModule.getConfiguration();
    }

    @Override
    public final boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        final boolean admin = sender.hasPermission(GeneralModule.FAKEQUIT_PERM);

        final HashMap<String, List<String>> groups = new HashMap<String, List<String>>();
        for (Player player : Bukkit.getOnlinePlayers())
        {
            final boolean fq = this.module.getVanishFakequitHandler().isPlayerFakequit(player);
            if (fq && !admin)
            {
                continue;
            }

            final String group = VoxelGuest.getPerms().getPrimaryGroup(player);
            final List<String> names = new ArrayList<>();

            if (groups.containsKey(group))
            {
                names.addAll(groups.get(group));
            }

            names.add(fq ? configuration.getFakequitPrefix() + player.getDisplayName() : player.getDisplayName());
            groups.put(group, names);

        }

        sender.sendMessage(ChatColor.DARK_GRAY + "------------------------------");
        String header = "";

        for (String groupName : groups.keySet())
        {
            header += ChatColor.DARK_GRAY + "[" + getColour(groupName) + groupName.substring(0, 1).toUpperCase() + ":" + groups.get(groupName).size() + ChatColor.DARK_GRAY + "] ";
        }

        final int numOnlinePlayers = Bukkit.getOnlinePlayers().length - this.module.getVanishFakequitHandler().getFakequitSize();
        header += ChatColor.DARK_GRAY + "(" + ChatColor.WHITE + "O:" + String.valueOf(numOnlinePlayers) + ChatColor.DARK_GRAY + ")";
        sender.sendMessage(header);

        for (String groupName : groups.keySet())
        {
            final List<String> names = groups.get(groupName);
            String groupOut = ChatColor.DARK_GRAY + "[" + getColour(groupName) + groupName.substring(0, 1).toUpperCase() + ChatColor.DARK_GRAY + "] ";
            for (int i = 0; i < names.size(); i++)
            {
                groupOut += ChatColor.WHITE + names.get(i);
                if (i < names.size() - 1)
                {
                    groupOut += ChatColor.GOLD + ", ";
                }
            }
            sender.sendMessage(groupOut);
        }

        sender.sendMessage(ChatColor.DARK_GRAY + "------------------------------");

        return true;
    }

    private String getColour(final String groupStr)
    {
        if (groupStr.equalsIgnoreCase("admin"))
        {
            return configuration.getAdminColor();
        }

        if (groupStr.equalsIgnoreCase("curator"))
        {
            return configuration.getCuratorColor();
        }

        if (groupStr.equalsIgnoreCase("sniper"))
        {
            return configuration.getSniperColor();
        }

        if (groupStr.equalsIgnoreCase("litesniper"))
        {
            return configuration.getLiteSniperColor();
        }

        if (groupStr.equalsIgnoreCase("member"))
        {
            return configuration.getMemberColor();
        }

        if (groupStr.equalsIgnoreCase("guest"))
        {
            return configuration.getGuestColor();
        }

        if (groupStr.equalsIgnoreCase("visitor"))
        {
            return configuration.getVisitorColor();
        }

        if (groupStr.equalsIgnoreCase("vip"))
        {
            return configuration.getVipColor();
        }

        return ChatColor.WHITE.toString();
    }
}
