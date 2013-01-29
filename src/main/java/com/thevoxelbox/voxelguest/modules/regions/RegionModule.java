/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thevoxelbox.voxelguest.modules.regions;

import com.thevoxelbox.voxelguest.modules.GuestModule;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joe
 */
public class RegionModule extends GuestModule
{

    private List<Region> regions = new ArrayList<>();
    
    public RegionModule() {
        setName("Region Module");
    }

    @Override
    public void onEnable(){
        this.eventListeners.add(new BlockEventListener(this));
        super.onEnable();
    }

    @Override
    public void onDisable(){
        super.onDisable();
    }    
    
}
