package com.thevoxelbox.voxelguest.modules.general;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
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
     * @param generalModule The parent module.
     */
    public ConnectionEventListener(final GeneralModule generalModule)
    {
        this.module = generalModule;

        Preconditions.checkState(generalModule.getConfiguration() instanceof GeneralModuleConfiguration);
        this.configuration = (GeneralModuleConfiguration) generalModule.getConfiguration();
    }

    @EventHandler
    public final void onPlayerJoin(final PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        event.setJoinMessage(this.formatJoinLeaveMessage(configuration.getJoinFormat(), player.getName()));

        if (module.getoVanished().contains(player.getName()))
        {
            module.getVanished().add(player.getName());
            module.getoVanished().remove(player.getName());
            module.hidePlayerForAll(player);
        }

        module.hideAllForPlayer(player);

        if (module.getoFakequit().contains(player.getName()))
        {
            module.getFakequit().add(player.getName());
            module.getoFakequit().remove(player.getName());
            event.setJoinMessage("");
        }
    }

    @EventHandler
    public final void onPlayerQuit(final PlayerQuitEvent event)
    {
        Player player = event.getPlayer();

        event.setQuitMessage(this.formatJoinLeaveMessage(configuration.getLeaveFormat(), player.getName()));

        if (module.getVanished().contains(player.getName()))
        {
            module.getoVanished().add(player.getName());
            module.getVanished().remove(player.getName());
        }

        if (module.getFakequit().contains(player.getName()))
        {
            module.getoFakequit().add(player.getName());
            module.getFakequit().remove(player.getName());
            event.setQuitMessage("");
        }
    }

    @EventHandler
    public final void onPlayerKick(final PlayerKickEvent event)
    {
        Player player = event.getPlayer();

        event.setLeaveMessage(this.formatJoinLeaveMessage(configuration.getKickFormat(), player.getName()));

        if (module.getVanished().contains(player.getName()))
        {
            module.getoVanished().add(player.getName());
            module.getVanished().remove(player.getName());
        }

        if (module.getFakequit().contains(player.getName()))
        {
            module.getoFakequit().add(player.getName());
            module.getFakequit().remove(player.getName());
            event.setLeaveMessage("");
        }
    }

    private String formatJoinLeaveMessage(final String msg, final String playerName)
    {
        return msg.replace("$no", Integer.toString(Bukkit.getOnlinePlayers().length)).replace("$n", playerName);
    }
}