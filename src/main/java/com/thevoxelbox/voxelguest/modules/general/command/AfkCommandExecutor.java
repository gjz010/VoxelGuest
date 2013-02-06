package com.thevoxelbox.voxelguest.modules.general.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.thevoxelbox.voxelguest.modules.general.GeneralModule;

public class AfkCommandExecutor implements CommandExecutor {
    private GeneralModule module;

    public AfkCommandExecutor(final GeneralModule generalModule)
    {
        this.module = generalModule;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            this.module.getAfkManager().toggleAfk(player);
            if (this.module.getAfkManager().isPlayerAfk(player))
            {
                if (args.length != 0)
                {
                    String afkMsg = "";
                    for (int i = 0; i < args.length; i++)
                    {
                        afkMsg += " ";
                        afkMsg += args[i];
                    }
                    Bukkit.broadcastMessage(ChatColor.DARK_AQUA + player.getName() + ChatColor.DARK_GRAY + afkMsg);
                    return true;
                }
                Bukkit.broadcastMessage(ChatColor.DARK_AQUA + player.getName() + ChatColor.DARK_GRAY + " has gone AFK");
            }
            return true;
        }
        return false;
    }

}
