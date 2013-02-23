package com.thevoxelbox.voxelguest.modules.helper.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.thevoxelbox.voxelguest.modules.helper.HelperModule;

public class WLReviewCommand implements CommandExecutor
{
    private final HelperModule module;

    public WLReviewCommand(final HelperModule module)
    {
        this.module = module;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args)
    {
        if (sender instanceof Player)
        {
            if(!sender.hasPermission("voxelguest.helper.wloveride"))
            {
                this.module.getManager().newReview((Player) sender);
            }
            else
            {
                sender.sendMessage(ChatColor.RED + "You can no longer submit a whitelist review!");
            }
            return true;
        }
        return false;
    }
}
