package com.thevoxelbox.voxelguest.modules.asshat.command;

import com.thevoxelbox.voxelguest.modules.asshat.AsshatModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @author Monofraps
 */
public class FreezeCommandExecutor implements CommandExecutor
{
	private AsshatModule module;

	public FreezeCommandExecutor(final AsshatModule module)
	{
		this.module = module;
	}

	@Override
	public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] strings)
	{
		module.setFreezeEnabled(!module.isFreezeEnabled());

		return true;
	}
}
