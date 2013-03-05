package com.thevoxelbox.voxelguest.modules.general;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * @author TheCryoknight
 */
public class AfkManager
{

    private final Set<String> playersAfk = new HashSet<>();

    /**
     * Sets the players afk state
     *
     * @param player Player who gets their AFK state set.
     * @param isAfk True to set the player AFK, false to set him as non-AFK
     */
    public final synchronized void setPlayerAfk(final Player player, final boolean isAfk)
    {
        if (isAfk)
        {
            this.playersAfk.add(player.getName());
            return;
        }
        this.playersAfk.remove(player.getName());
    }

    /**
     * Toggles the specified players afk state
     *
     * @param player The player who wants his AFK status to be toggled.
     */
    public final void toggleAfk(final Player player)
    {
        if (this.playersAfk.contains(player.getName()))
        {
            this.setPlayerAfk(player, false);
            Bukkit.broadcastMessage(ChatColor.DARK_AQUA + player.getName() + ChatColor.DARK_GRAY + " has returned");
            return;
        }
        this.setPlayerAfk(player, true);
    }

    /**
     * Checks to see if Player is afk
     *
     * @param player Player to check afk state
     *
     * @return true if player is afk
     */
    public final boolean isPlayerAfk(final Player player)
    {
        return this.playersAfk.contains(player.getName());
    }
}
