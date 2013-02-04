package com.thevoxelbox.voxelguest.modules.general.command;

import com.google.common.base.Preconditions;
import com.thevoxelbox.voxelguest.modules.general.GeneralModule;
import com.thevoxelbox.voxelguest.modules.general.GeneralModuleConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class FakequitCommandExecutor implements CommandExecutor
{
    private GeneralModule module;
    private GeneralModuleConfiguration configuration;

    public FakequitCommandExecutor(final GeneralModule generalModule)
    {
        this.module = generalModule;

        Preconditions.checkState(generalModule.getConfiguration() instanceof GeneralModuleConfiguration);
        this.configuration = (GeneralModuleConfiguration) generalModule.getConfiguration();
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!sender.hasPermission(GeneralModule.FAKEQUIT_PERM))
        {
            sender.sendMessage(ChatColor.RED + "You do not have permission to do that.");
            return true;
        }

        final List<String> fakequit = module.getFakequit();
        final List<String> ofakequit = module.getoFakequit();

        if (ofakequit.contains(sender.getName())) ofakequit.remove(sender.getName());
        if (fakequit.contains(sender.getName()))
        {
            sender.sendMessage(ChatColor.AQUA + "You have un-fakequit!");
            fakequit.remove(sender.getName());

            String online = Bukkit.getOnlinePlayers().length - fakequit.size() + "";
            String fQMsg = configuration.getJoinFormat().replace("$no", online).replace("$n", sender.getName());
            Bukkit.broadcastMessage(fQMsg);
        }
        else
        {
            sender.sendMessage(ChatColor.AQUA + "You have fakequit!");

            fakequit.add(sender.getName());

            final List<String> vanished = module.getVanished();

            if (!vanished.contains(sender.getName()))
            {
                vanished.add(sender.getName());
                module.hidePlayerForAll((Player) sender);
            }

            String online = Bukkit.getOnlinePlayers().length - fakequit.size() + "";
            String fQMsg = configuration.getLeaveFormat().replace("$no", online).replace("$n", sender.getName());
            Bukkit.broadcastMessage(fQMsg);
        }

        return true;
    }
}
