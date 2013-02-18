package com.thevoxelbox.voxelguest.modules.general;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.server.v1_4_R1.Packet43SetExperience;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_4_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class LagMeterHelper extends Thread
{
    private Set<Player> activePlayers = new HashSet<>();
    private volatile boolean isStoped = false;

    public LagMeterHelper()
    {
        
    }

    public void togglePlayer(Player player)
    {
        if (!this.activePlayers.add(player))
        {
            this.activePlayers.remove(player);
            player.sendMessage(ChatColor.GRAY + "Your experence bar will nolonger reperesnts the servers TPS.");
        }
        else
        {
            player.sendMessage(ChatColor.GRAY + "Your experence bar will now reperesnt the servers TPS.");
        }
    }
    
    public boolean isPlayerOnTpsWatch(Player player)
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
        while (!this.isStoped)
        {
            for (Player player : this.activePlayers)
            {
                final CraftPlayer cPlayer = (CraftPlayer) player;
                tps = (float) TPSTicker.calculateTPS();
                if (tps > 20)
                {
                    tps = 20;
                }
                final Packet43SetExperience packet = new Packet43SetExperience(tps / 20.0F, 0, (int) tps);
                cPlayer.getHandle().playerConnection.sendPacket(packet);
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
