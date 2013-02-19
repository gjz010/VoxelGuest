package com.thevoxelbox.voxelguest.modules.general;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.server.v1_4_R1.Packet43SetExperience;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_4_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 *
 * @author TheCryoknight
 */
public class LagMeterHelper extends Thread
{
    private final Set<Player> activePlayers = new HashSet<>();
    private volatile boolean isStoped = false;


    public LagMeterHelper()
    {
        
    }

    public void setPlayerWatchState(final Player player, final boolean state)
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
    public void togglePlayer(final Player player)
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
    
    public boolean isPlayerOnTpsWatch(final Player player)
    {
        return this.activePlayers.contains(player);
    }

    public void setStopped(final boolean stop)
    {
        this.isStoped = stop;
    }

    @Override
    public void run()
    {
        float tps = 0;
        try {
            while (!this.isStoped)
            {
                for (Player player : this.activePlayers.toArray(new Player[0]))
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
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
