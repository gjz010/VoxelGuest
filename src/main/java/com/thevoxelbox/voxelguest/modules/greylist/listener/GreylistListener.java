package com.thevoxelbox.voxelguest.modules.greylist.listener;

import com.google.common.base.Preconditions;
import com.thevoxelbox.voxelguest.VoxelGuest;
import com.thevoxelbox.voxelguest.modules.greylist.GreylistConfiguration;
import com.thevoxelbox.voxelguest.modules.greylist.GreylistModule;
import com.thevoxelbox.voxelguest.modules.greylist.event.PlayerGreylistedEvent;
import com.thevoxelbox.voxelguest.modules.greylist.event.PlayerUngreylistedEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 * @author MikeMatrix
 */
public class GreylistListener implements Listener
{
    GreylistModule greylistModule;
    GreylistConfiguration moduleConfiguration;

    public GreylistListener(final GreylistModule greylistModule)
    {
        this.greylistModule = greylistModule;
        this.moduleConfiguration = (GreylistConfiguration)greylistModule.getConfiguration();
    }

    @EventHandler
    public void onPlayerLogin(final PlayerLoginEvent event)
    {
        Preconditions.checkNotNull(event);

        if (greylistModule.getConfig().isExplorationMode())
        {
            return;
        }

        final Player player = event.getPlayer();
        if (!player.hasPermission("voxelguest.greylist.override") && !greylistModule.isOnPersistentGreylist(player.getName()))
        {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, greylistModule.getConfig().getNotGreylistedKickMessage());
        }
    }

    @EventHandler
    public void onPlayerGreylisted(PlayerGreylistedEvent event) {
        if (moduleConfiguration.isSetGroupOnGraylist())
        {
            if (VoxelGuest.getPerms().playerAddGroup(Bukkit.getWorlds().get(0), event.getPlayerName(), moduleConfiguration.getGraylistGroupName()))
            {
                VoxelGuest.getPluginInstance().getLogger().warning("Error: Could not set new greylisted player to group.");
            }
        }

        if(moduleConfiguration.isBroadcastGreylists()) {
            Bukkit.broadcastMessage(ChatColor.GRAY + event.getPlayerName() + ChatColor.DARK_GRAY + " was added to the greylist.");
        }
    }
}
