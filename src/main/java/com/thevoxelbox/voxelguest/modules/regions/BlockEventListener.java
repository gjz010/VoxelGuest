/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thevoxelbox.voxelguest.modules.regions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 *
 * @author Joe
 */
public class BlockEventListener implements Listener
{

    private RegionModule regionModule;
    private final String CANT_BUILD_HERE = "&4You cannot build here";

    public BlockEventListener(RegionModule regionModule) {
        this.regionModule = regionModule;
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Region region = regionModule.getRegionAtLocation(event.getBlock().getLocation());
        if(region == null){
            return;
        }
        
        if(!region.getBuildPermission().equalsIgnoreCase("")){
            if(!event.getPlayer().hasPermission(region.getBuildPermission())){
                event.getPlayer().sendMessage(CANT_BUILD_HERE);
                event.setCancelled(true);
            }
        }
        
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Region region = regionModule.getRegionAtLocation(event.getBlock().getLocation());
        if(region == null){
            return;
        }
        
        if(!region.getBuildPermission().equalsIgnoreCase("")){
            if(!event.getPlayer().hasPermission(region.getBuildPermission())){
                event.getPlayer().sendMessage(CANT_BUILD_HERE);
                event.setCancelled(true);
            }
        }
        
        if(region.getBannedBlocks().contains(event.getBlockPlaced())){
            event.getPlayer().sendMessage(CANT_BUILD_HERE);
            event.setCancelled(true);
        }
    }
    
    
    
}
