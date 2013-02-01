package com.thevoxelbox.voxelguest.modules.asshat.command;

import com.thevoxelbox.voxelguest.modules.asshat.AsshatModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @author Monofraps
 */
public class BanreasonCommandExecutor implements CommandExecutor
{
	AsshatModule module;

	public BanreasonCommandExecutor(AsshatModule module) {
		this.module = module;
	}

	@Override
	public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] args)
	{
		if(args.length < 1) {
			commandSender.sendMessage("Invalid number of parameters.");
			return false;
		}

		final String playerName = args[0];

		if(!module.isPlayerBanned(playerName)) {
			commandSender.sendMessage(String.format("Player % is not even banned.", playerName));
			return true;
		}

		commandSender.sendMessage(String.format("%s is banned for %s", playerName, module.whyIsPlayerBanned(playerName)));
		return true;
	}
}
