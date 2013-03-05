package com.thevoxelbox.voxelguest.modules.helper;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *
 * @author TheCryoknight
 */
public final class HelperListener implements Listener
{
    private final HelperModule module;

    public HelperListener(final HelperModule module)
    {
        this.module = module;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(final PlayerJoinEvent event)
    {
        Player newPlayer = event.getPlayer();
        if (newPlayer != null)
        {
            this.module.getManager().handleLogin(newPlayer);
        }
    }
}
