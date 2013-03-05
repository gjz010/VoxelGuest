package com.thevoxelbox.voxelguest.modules.general;

import net.minecraft.server.v1_4_R1.Packet43SetExperience;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_4_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author TheCryoknight
 */
public class LagMeterHelper extends Thread
{
    private final Set<Player> activePlayers = Collections.synchronizedSet(new HashSet<Player>());
    private volatile boolean isStoped = false;

    /**
     * Sets a players watch state.
     * @param player The name of the player.
     * @param state A boolean to indicate whether or not zou want to set or unset the watch state.
     */
    public final void setPlayerWatchState(final Player player, final boolean state)
    {
        if (state)
        {
            this.activePlayers.add(player);
        }
        else
        {
            this.activePlayers.remove(player);
        }
    }

    /**
     * Toggles whether or not the players is watching TPS
     *
     * @param player player to toggle
     */
    public final void togglePlayer(final Player player)
    {
        if (this.activePlayers.contains(player))
        {
            this.setPlayerWatchState(player, false);
            player.sendMessage(ChatColor.GRAY + "Your experence bar will nolonger reperesnts the servers TPS.");
        }
        else
        {
            this.setPlayerWatchState(player, true);
            player.sendMessage(ChatColor.GRAY + "Your experence bar will now reperesnt the servers TPS.");
        }
    }

    /**
     * Checks if a player is on the tps watch list.
     * @param player The player name.
     * @return Returns a boolean indicating if the given player name is on the tps watch list.
     */
    public final boolean isPlayerOnTpsWatch(final Player player)
    {
        return this.activePlayers.contains(player);
    }

    public final void setStopped(final boolean stop)
    {
        this.isStoped = stop;
    }

    @Override
    public final void run()
    {
        float tps;
        try
        {
            while (!this.isStoped)
            {
                for (final Player player : this.activePlayers)
                {
                    final CraftPlayer cPlayer = (CraftPlayer) player;
                    tps = (float) TPSTicker.calculateTPS();
                    if (tps > 20)
                    {
                        tps = 20;
                    }
                    if (cPlayer.isOnline())
                    {
                        final Packet43SetExperience packet = new Packet43SetExperience(tps / 20.0F, 0, (int) tps);
                        cPlayer.getHandle().playerConnection.sendPacket(packet);
                    }
                }
                try
                {
                    Thread.sleep(0xbb8);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
