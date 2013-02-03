package com.thevoxelbox.voxelguest.modules.general.command;

import com.thevoxelbox.voxelguest.modules.general.GeneralModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class EntityPurgeCommandExecutor implements CommandExecutor
{
	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args)
	{
		if (!sender.hasPermission(GeneralModule.ENTITY_PURGE_PERM))
		{
			sender.sendMessage("You don't have permissions to do this.");
			return true;
		}

		if (args.length == 0)
		{
			sender.sendMessage(ChatColor.RED + "Please enter a world name");
			return false;
		}

		for (String worldName : args)
		{
			final World world = Bukkit.getWorld(worldName);

			if (world != null)
			{
				sender.sendMessage(ChatColor.RED + "Purging entities from " + world.getName());
				for (Entity e : world.getEntities())
				{
					if (e.getType().equals(EntityType.ITEM_FRAME) ||
							e.getType().equals(EntityType.PAINTING) ||
							e.getType().equals(EntityType.PLAYER) ||
							e.getType().equals(EntityType.WOLF))
					{
						continue;
					}

					e.remove();
				}
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "Unknown world name " + worldName);
			}
		}

		return true;
	}
}
