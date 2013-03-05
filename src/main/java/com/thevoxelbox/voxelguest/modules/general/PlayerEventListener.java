package com.thevoxelbox.voxelguest.modules.general;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * @author TheCryoknight
 */
public class PlayerEventListener implements Listener
{
    private final GeneralModule module;

    /**
     * Creates a new player event listener instance.
     * @param generalModule The owning module.
     */
    public PlayerEventListener(final GeneralModule generalModule)
    {
        this.module = generalModule;
    }

    @EventHandler
    public final void onPlayerMove(final PlayerMoveEvent event)
    {
        if (event.getPlayer() != null)
        {
            if (this.module.getAfkManager().isPlayerAfk(event.getPlayer()))
            {
                this.module.getAfkManager().toggleAfk(event.getPlayer());
            }
        }
    }

    @EventHandler
    public final void onPlayerChat(final AsyncPlayerChatEvent event)
    {
        if (event.getPlayer() != null)
        {
            if (this.module.getAfkManager().isPlayerAfk(event.getPlayer()))
            {
                this.module.getAfkManager().toggleAfk(event.getPlayer());
            }
        }
    }

    @EventHandler
    public final void onPlayerTeleport(final PlayerTeleportEvent event)
    {
        if (event.getPlayer() != null)
        {
            if (this.module.getAfkManager().isPlayerAfk(event.getPlayer()))
            {
                this.module.getAfkManager().toggleAfk(event.getPlayer());
            }
        }
    }

    @EventHandler
    public final void onPlayerCommand(final PlayerCommandPreprocessEvent event)
    {
        if (event.getPlayer() != null)
        {
            if (this.module.getAfkManager().isPlayerAfk(event.getPlayer()))
            {
                this.module.getAfkManager().toggleAfk(event.getPlayer());
            }
        }
    }

    @EventHandler
    public final void onPlayerInteract(final PlayerInteractEvent event)
    {
        if (event.getPlayer() != null)
        {
            if (this.module.getAfkManager().isPlayerAfk(event.getPlayer()))
            {
                this.module.getAfkManager().toggleAfk(event.getPlayer());
            }
        }
    }

    @EventHandler
    public final void onPlayerQuit(final PlayerQuitEvent event)
    {
        if (event.getPlayer() != null)
        {
            if (this.module.getAfkManager().isPlayerAfk(event.getPlayer()))
            {
                this.module.getAfkManager().toggleAfk(event.getPlayer());
            }
        }
    }

    @EventHandler
    public final void onPlayerKick(final PlayerKickEvent event)
    {
        if (event.getPlayer() != null)
        {
            if (this.module.getAfkManager().isPlayerAfk(event.getPlayer()))
            {
                this.module.getAfkManager().toggleAfk(event.getPlayer());
            }
        }
    }
}
