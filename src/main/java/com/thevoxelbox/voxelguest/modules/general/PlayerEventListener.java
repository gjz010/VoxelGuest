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
 * 
 * @author TheCryoknight
 */
public class PlayerEventListener implements Listener
{
    private final GeneralModule module;
    public PlayerEventListener(final GeneralModule generalModule)
    {
        this.module = generalModule;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
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
    public void onPlayerChat(AsyncPlayerChatEvent event)
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
    public void onPlayerTeleport(PlayerTeleportEvent event)
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
    public void onPlayerCommand(PlayerCommandPreprocessEvent event)
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
    public void onPlayerInteract(PlayerInteractEvent event)
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
    public void onPlayerQuit(PlayerQuitEvent event)
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
    public void onPlayerKick(PlayerKickEvent event)
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
