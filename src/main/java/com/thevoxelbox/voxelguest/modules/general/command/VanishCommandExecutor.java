package com.thevoxelbox.voxelguest.modules.general.command;

import com.thevoxelbox.voxelguest.modules.general.GeneralModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        if (sender instanceof Player)
        {
            this.module.getVanishFakequitHandler().toggleVanish((Player) sender);
            return true;
        }
        return false;
    }
}
