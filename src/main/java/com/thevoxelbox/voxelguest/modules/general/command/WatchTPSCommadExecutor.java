package com.thevoxelbox.voxelguest.modules.general.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.thevoxelbox.voxelguest.modules.general.GeneralModule;

public class WatchTPSCommadExecutor implements CommandExecutor
{
    private final GeneralModule module;

    public WatchTPSCommadExecutor(final GeneralModule module)
    {
        this.module = module;
    }

    @Override

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            this.module.getLagmeter().togglePlayer((Player) sender);
            return true;
        }
        return false;
    }

}
