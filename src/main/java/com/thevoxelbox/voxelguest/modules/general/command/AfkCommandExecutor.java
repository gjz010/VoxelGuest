package com.thevoxelbox.voxelguest.modules.general.command;

import com.thevoxelbox.voxelguest.modules.general.GeneralModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TheCryoknight
 */
public class AfkCommandExecutor implements CommandExecutor
{
    private final GeneralModule module;

    /**
     * Creates a new /afk command executor
     * @param generalModule The owning parent module.
     */
    public AfkCommandExecutor(final GeneralModule generalModule)
    {
        this.module = generalModule;
    }

    @Override
    public final boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args)
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
                    for (final String arg : args)
                    {
                        afkMsg += " ";
                        afkMsg += arg;
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
