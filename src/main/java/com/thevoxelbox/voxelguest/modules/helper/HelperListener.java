package com.thevoxelbox.voxelguest.modules.helper;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class HelperListener implements Listener
{
    private final HelperModule module;

    public HelperListener(final HelperModule module)
    {
        this.module = module;
    }

    @EventHandler()
    public final void onPlayerJoin(final PlayerJoinEvent event)
    {
        Player newPlayer = event.getPlayer();
        if (newPlayer != null)
        {
            if (this.module.getManager().isHelper(newPlayer))
            {
                final String msg = this.module.getManager().getActiveRequests();
                if (msg != null)
                {
                    newPlayer.sendMessage(msg);
                }
            }
        }
    }
}
