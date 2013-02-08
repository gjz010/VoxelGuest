package com.thevoxelbox.voxelguest.modules.general.command;

import com.thevoxelbox.voxelguest.modules.general.GeneralModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FakequitCommandExecutor implements CommandExecutor
{
    private GeneralModule module;

    public FakequitCommandExecutor(final GeneralModule generalModule)
    {
        this.module = generalModule;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (sender instanceof Player)
        {
            this.module.getVanishFakequitHandler().toggleFakeQuit((Player) sender);
            return true;
        }
        return false;
    }
}
