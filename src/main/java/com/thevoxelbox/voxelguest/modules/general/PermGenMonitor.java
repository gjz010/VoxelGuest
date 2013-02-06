package com.thevoxelbox.voxelguest.modules.general;

import org.bukkit.Bukkit;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;
import java.util.Iterator;

/**
 * @author Monofraps
 */
public class PermGenMonitor implements Runnable
{
    private boolean sentConsoleWarning = false;
    private boolean sentBroadcastWarning = false;

    @Override
    public void run()
    {
        Iterator<MemoryPoolMXBean> iter = ManagementFactory.getMemoryPoolMXBeans().iterator();
        while (iter.hasNext())
        {
            MemoryPoolMXBean item = iter.next();
            String name = item.getName();
            MemoryUsage usage = item.getUsage();

            if (name != null && name.contains("Perm Gen") && !name.contains("ro") && !name.contains("rw"))
            {
                double permGenUsage = (double)usage.getUsed() / (double)usage.getMax();
                if(permGenUsage > 0.55f && !sentConsoleWarning) {
                    Bukkit.getLogger().warning("MEDIUM WARNING: Perm Gen space exceeded 55% usage! A server restart is recommended.");
                    sentConsoleWarning = true;
                    break;
                }

                if(permGenUsage > 0.65f && !sentBroadcastWarning) {
                    Bukkit.getLogger().warning("SEVERE WARNING: Perm Gen space exceeded 65% usage! A server restart is recommended.");
                    Bukkit.broadcastMessage("SEVERE WARNING: Perm Gen space exceeded 65% usage! A server restart is recommended.");
                    sentBroadcastWarning = true;
                    break;
                }

                if (permGenUsage > 0.8f)
                {
                    Bukkit.broadcastMessage("Perm Gen space exceeded 80% usage! Forcing server shutdown to prevent data loss.");
                    Bukkit.getLogger().severe("Perm Gen threshold exceeded. Forcing shutdown to prevent data loss.");
                    Bukkit.shutdown();
                }
            }
        }
    }
}
