package com.thevoxelbox.voxelguest.modules.worldprotection;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

//TODO: find a proper name

/**
 * @author Monofraps
 */
public class BlockEventListener implements Listener
{
    private WorldProtectionModule protectionModule;

    public BlockEventListener(WorldProtectionModule protectionModule)
    {

        this.protectionModule = protectionModule;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        if (!protectionModule.isProtectedWorld(event.getBlock().getWorld()))
        {
            return;
        }


    }
}
