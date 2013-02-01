package com.thevoxelbox.voxelguest.modules.asshat.command;

import com.thevoxelbox.voxelguest.modules.asshat.AsshatModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @author Monofraps
 */
public class SoapboxCommandExecutor implements CommandExecutor
{
	private AsshatModule module;

	public SoapboxCommandExecutor(final AsshatModule module)
	{
		this.module = module;
	}

	@Override
	public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] strings)
	{
		module.setSilenceEnabled(!module.isSilenceEnabled());

		return true;
	}
}
