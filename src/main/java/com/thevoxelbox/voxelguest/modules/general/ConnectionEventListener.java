package com.thevoxelbox.voxelguest.modules.general;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionEventListener implements Listener
{
    private GeneralModule module;
    private GeneralModuleConfiguration configuration;

    /**
     *
     * @param generalModule The parent module.
     */
    public ConnectionEventListener(final GeneralModule generalModule)
    {
        this.module = generalModule;
        this.configuration = (GeneralModuleConfiguration)generalModule.getConfiguration();
    }

    @EventHandler
    public final void onPlayerJoin(final PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        event.setJoinMessage(this.module.formatJoinLeaveMessage(configuration.getJoinFormat(), player.getName()));
        this.module.getVanishFakequitHandler().handleConnect(player);
        if (this.module.getVanishFakequitHandler().isPlayerFakequit(player))
        {
            event.setJoinMessage("");
        }
    }

    @EventHandler
    public final void onPlayerQuit(final PlayerQuitEvent event)
    {
        Player player = event.getPlayer();

        if (this.module.getLagmeter().isPlayerOnTpsWatch(player))
        {
            this.module.getLagmeter().togglePlayer(player);
        }

        event.setQuitMessage(this.module.formatJoinLeaveMessage(configuration.getLeaveFormat(), player.getName()));
        
        if (this.module.getVanishFakequitHandler().handleDisconnect(player))
        {
            event.setQuitMessage("");
        }
    }

    @EventHandler
    public final void onPlayerKick(final PlayerKickEvent event)
    {
        Player player = event.getPlayer();

        if (this.module.getLagmeter().isPlayerOnTpsWatch(player))
        {
            this.module.getLagmeter().togglePlayer(player);
        }

        event.setLeaveMessage(this.module.formatJoinLeaveMessage(configuration.getKickFormat(), player.getName()));

        if (this.module.getVanishFakequitHandler().handleDisconnect(player))
        {
            event.setLeaveMessage("");
        }
    }
}