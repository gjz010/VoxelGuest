package com.thevoxelbox.voxelguest.modules.helper.command;

import com.thevoxelbox.voxelguest.modules.helper.HelperModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WLReviewCommand implements CommandExecutor
{
    private final HelperModule module;

    public WLReviewCommand(final HelperModule module)
    {
        this.module = module;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            if (!sender.hasPermission("voxelguest.helper.wloveride"))
            {
                this.module.getManager().newReview((Player) sender);
            }
            return true;
        }
        return false;
    }
}
