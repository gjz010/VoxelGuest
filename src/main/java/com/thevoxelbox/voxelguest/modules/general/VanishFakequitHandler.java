package com.thevoxelbox.voxelguest.modules.general;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.google.common.base.Preconditions;

/**
 *
 * @author TheCryoknight
 */
public class VanishFakequitHandler
{
    private final GeneralModule module;
    private static final String VANISH_PERM = "voxelguest.general.vanish";

    private final Set<Player> vanished = new HashSet<Player>();
    private final Set<Player> fakeQuit = new HashSet<Player>();

    private final Set<String> offlineVanished = new HashSet<String>();
    private final Set<String> offlineFakeQuit = new HashSet<String>();

    public VanishFakequitHandler(GeneralModule module)
    {
        this.module = module;
    }

    /**
     * Switches the vanished state of the specified player.
     *
     * @param player Player toggling their state
     * @return true if player is now vanished
     */
    public boolean toggleVanish(final Player player)
    {
        Preconditions.checkNotNull(player);

        if (this.fakeQuit.contains(player))
        {
            player.sendMessage(ChatColor.AQUA + "You are already fakequit");
            return false;
        }
        if (this.vanished.contains(player))
        {
            this.revealPlayer(player);
            player.sendMessage(ChatColor.AQUA + "You have reappeared!");
            this.vanished.remove(player);
            return false;
        }
        else
        {
            this.hidePlayer(player);
            this.vanished.add(player);
            player.sendMessage(ChatColor.AQUA + "You have vanished!");
            return true;
        }
    }

    /**
     * Switches the fakequit state of the specified player.
     *
     * @param player Player toggling their state
     * @return true if player is now vanished
     */
    public boolean toggleFakeQuit(final Player player)
    {
        Preconditions.checkNotNull(player);

        if (this.vanished.contains(player))
        {
            this.toggleVanish(player);
        }
        if (this.fakeQuit.contains(player))
        {
            this.revealPlayer(player);
            this.fakeQuit.remove(player);
            Preconditions.checkState(module.getConfiguration() instanceof GeneralModuleConfiguration);

            Bukkit.broadcastMessage(this.module.formatJoinLeaveMessage(((GeneralModuleConfiguration) this.module.getConfiguration()).getJoinFormat(), player.getName()));
            player.sendMessage(ChatColor.AQUA + "You have un-fakequit!");
            return false;
        }
        else
        {
            this.hidePlayer(player);
            this.fakeQuit.add(player);
            Preconditions.checkState(module.getConfiguration() instanceof GeneralModuleConfiguration);

            Bukkit.broadcastMessage(this.module.formatJoinLeaveMessage(((GeneralModuleConfiguration) this.module.getConfiguration()).getLeaveFormat(), player.getName()));
            player.sendMessage(ChatColor.AQUA + "You have fakequit!");
            return true;
        }
    }

    public boolean handleDisconnect(final Player player)
    {
        Preconditions.checkNotNull(player);

        if (this.vanished.contains(player))
        {
            this.offlineVanished.add(player.getName());
            this.vanished.remove(player);
        }
        if (this.fakeQuit.contains(player.getName()))
        {
            this.offlineFakeQuit.add(player.getName());
            this.fakeQuit.remove(player);
            return true;
        }
        return false;
    }

    public void handleConnect(final Player player)
    {
        Preconditions.checkNotNull(player);

        if (this.offlineVanished.contains(player.getName()))
        {
            this.toggleVanish(player);
        }
        if (this.offlineFakeQuit.contains(player.getName()))
        {
            this.toggleFakeQuit(player);
        }

        if (player.hasPermission(VANISH_PERM))
        {
            return;
        }

        for (Player onlinePlayer : this.vanished)
        {
            if (onlinePlayer != null)
            {
                player.hidePlayer(onlinePlayer);
            }
        }
    }

    /**
     * Checks to see if the specified player is vanished.
     *
     * @param player
     * @return
     */
    public boolean isPlayerVanished(final Player player)
    {
        Preconditions.checkNotNull(player);

        return this.vanished.contains(player);
    }

    /**
     * Checks to see if the specified player has fakequit.
     *
     * @param player Player whose fakequit state to query
     * @return true if specified player is fakequit
     */
    public boolean isPlayerFakequit(final Player player)
    {
        Preconditions.checkNotNull(player);

        return this.fakeQuit.contains(player);
    }

    public int getFakequitSize()
    {
        return this.fakeQuit.size();
    }

    /**
     * Hides the specified player.
     *
     * @param player player hiding
     */
    private void hidePlayer(final Player player)
    {
        Preconditions.checkNotNull(player);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
        {
            if (!onlinePlayer.hasPermission(VANISH_PERM))
            {
                onlinePlayer.hidePlayer(player);
            }
        }
    }

    /**
     * Shows the specified player to others.
     * 
     * @param player
     */
    private void revealPlayer(final Player player)
    {
        Preconditions.checkNotNull(player);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
        {
            if (!onlinePlayer.hasPermission(VANISH_PERM))
            {
                onlinePlayer.showPlayer(player);
            }
        }
    }
}
