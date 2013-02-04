package com.thevoxelbox.voxelguest.modules.general.command;

import com.thevoxelbox.voxelguest.modules.general.GeneralModule;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class VanishCommandExecutor implements CommandExecutor
{
    private GeneralModule module;

    public VanishCommandExecutor(final GeneralModule generalModule)
    {
        this.module = generalModule;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (!sender.hasPermission(GeneralModule.VANISH_PERM))
        {
            sender.sendMessage(ChatColor.RED + "You do not have permission to do that.");
            return true;
        }

        final List<String> oVanished = module.getoVanished();
        final List<String> vanished = module.getoVanished();

        if (oVanished.contains(sender.getName()))
        {
            oVanished.remove(sender.getName());
        }
        if (vanished.contains(sender.getName()))
        {
            sender.sendMessage(ChatColor.AQUA + "You have reappeared!");
            vanished.remove(sender.getName());
        }
        else
        {
            sender.sendMessage(ChatColor.AQUA + "You have vanished!");
            vanished.add(sender.getName());
            module.hidePlayerForAll((Player) sender);
        }
        
        module.setVanished(vanished);
        module.setoVanished(oVanished);

        return true;
    }
}
