package com.thevoxelbox.voxelguest.modules.asshat.command;

import com.thevoxelbox.voxelguest.modules.asshat.AsshatModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Executes /banreason commands.
 *
 * @author Monofraps
 */
public class BanreasonCommandExecutor implements CommandExecutor
{
    private final AsshatModule module;

    /**
     * Creates a new banreason command executor.
     *
     * @param module The owning module.
     */
    public BanreasonCommandExecutor(final AsshatModule module)
    {
        this.module = module;
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] args)
    {
        if (!commandSender.hasPermission("voxelguest.asshat.banreason"))
        {
            commandSender.sendMessage("You don't have permissions.");
            return true;
        }

        if (args.length < 1)
        {
            commandSender.sendMessage("Invalid number of parameters.");
            return false;
        }

        final String playerName = args[0];

        if (!module.getBanlist().isPlayerBanned(playerName))
        {
            commandSender.sendMessage(String.format("Player %s is not even banned.", playerName));
            return true;
        }

        commandSender.sendMessage(String.format("%s is banned for %s", playerName, module.getBanlist().whyIsPlayerBanned(playerName)));
        return true;
    }
}
