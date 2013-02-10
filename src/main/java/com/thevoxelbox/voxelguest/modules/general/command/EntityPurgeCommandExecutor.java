package com.thevoxelbox.voxelguest.modules.general.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thevoxelbox.voxelguest.modules.general.GeneralModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class EntityPurgeCommandExecutor implements TabExecutor
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

        if (args[0].equals("*"))
        {
            for (World world : Bukkit.getWorlds())
            {
                sender.sendMessage(ChatColor.GRAY + "Purging entities from: " + ChatColor.GREEN + world.getName());
                this.doEntityPurge(world);
            }
            return true;
        }
        for (String worldName : args)
        {
            final World world = Bukkit.getWorld(worldName);

            if (world != null)
            {
                sender.sendMessage(ChatColor.GRAY + "Purging entities from: " + ChatColor.GREEN + world.getName());
                this.doEntityPurge(world);
            }
            else
            {
                sender.sendMessage(ChatColor.RED + "Unknown world name " + worldName);
            }
        }

        return true;
    }

    public void doEntityPurge(World target)
    {
        for (Entity entity : target.getEntities())
        {
            if (entity.getType().equals(EntityType.ITEM_FRAME) ||
                    entity.getType().equals(EntityType.PAINTING) ||
                    entity.getType().equals(EntityType.PLAYER) ||
                    entity.getType().equals(EntityType.WOLF))
            {
                continue;
            }

            entity.remove();
        }
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args)
    {
        if (sender.hasPermission("voxelguest.general.ep"))
        {
            final List<String> worldNames = this.getWorldNames();
            if (args.length == 0)
            {
                return worldNames;
            }
            for (String worldName : worldNames)
            {
                if (worldName.toLowerCase().startsWith(args[args.length - 1]));
            }
        }
        return Collections.emptyList();
    }

    /**
     *
     * @return
     */
    public List<String> getWorldNames()
    {
        final List<String> worldNames = new ArrayList<>();
        for (World world : Bukkit.getWorlds())
        {
            worldNames.add(world.getName());
        }
        return worldNames;
    }
}
