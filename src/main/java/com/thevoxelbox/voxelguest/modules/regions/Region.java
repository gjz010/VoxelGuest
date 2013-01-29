/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thevoxelbox.voxelguest.modules.regions;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Joe
 */
public class Region 
{
    
    public final String regionName;
    private Location pointOne;
    private Location pointTwo;
    
    //World
    private boolean allowMobSpawn = false;
    private boolean allowFireSpread = false;
    private boolean allowLeafDecay = false;
    private boolean allowBlockGrowth = false;
    private boolean allowBlockSpread = false;
    private boolean allowExplosions = false;
    private boolean allowBreakingPaintings = false;
    private boolean allowBreakingItemframes = false;
    private List<Block> bannedBlocks = new ArrayList<>();
    private List<ItemStack> bannedItems = new ArrayList<>();
    private String buildPermission;
    
    //Player
    private boolean allowPlayerDamage;
    private boolean allowHunger;

    public Region(Location pointOne, Location pointTwo, String regionName, String buildPermission) {
        this.pointOne = pointOne;
        this.pointTwo = pointTwo;
        this.regionName = regionName;
        this.buildPermission = buildPermission;
    }
    
    public boolean isLocationInRegion(Location locationToCheck){
        return true;
    }

    public Location getPointOne() {
        return pointOne;
    }

    public void setPointOne(Location pointOne) {
        this.pointOne = pointOne;
    }

    public Location getPointTwo() {
        return pointTwo;
    }

    public void setPointTwo(Location pointTwo) {
        this.pointTwo = pointTwo;
    }

    public boolean isAllowMobSpawn() {
        return allowMobSpawn;
    }

    public void setAllowMobSpawn(boolean allowMobSpawn) {
        this.allowMobSpawn = allowMobSpawn;
    }

    public boolean isAllowFireSpread() {
        return allowFireSpread;
    }

    public void setAllowFireSpread(boolean allowFireSpread) {
        this.allowFireSpread = allowFireSpread;
    }

    public boolean isAllowLeafDecay() {
        return allowLeafDecay;
    }

    public void setAllowLeafDecay(boolean allowLeafDecay) {
        this.allowLeafDecay = allowLeafDecay;
    }

    public boolean isAllowBlockGrowth() {
        return allowBlockGrowth;
    }

    public void setAllowBlockGrowth(boolean allowBlockGrowth) {
        this.allowBlockGrowth = allowBlockGrowth;
    }

    public boolean isAllowBlockSpread() {
        return allowBlockSpread;
    }

    public void setAllowBlockSpread(boolean allowBlockSpread) {
        this.allowBlockSpread = allowBlockSpread;
    }

    public boolean isAllowExplosions() {
        return allowExplosions;
    }

    public void setAllowExplosions(boolean allowExplosions) {
        this.allowExplosions = allowExplosions;
    }

    public boolean isAllowBreakingPaintings() {
        return allowBreakingPaintings;
    }

    public void setAllowBreakingPaintings(boolean allowBreakingPaintings) {
        this.allowBreakingPaintings = allowBreakingPaintings;
    }

    public boolean isAllowBreakingItemframes() {
        return allowBreakingItemframes;
    }

    public void setAllowBreakingItemframes(boolean allowBreakingItemframes) {
        this.allowBreakingItemframes = allowBreakingItemframes;
    }

    public List<Block> getBannedBlocks() {
        return bannedBlocks;
    }

    public void setBannedBlocks(List<Block> bannedBlocks) {
        this.bannedBlocks = bannedBlocks;
    }

    public List<ItemStack> getBannedItems() {
        return bannedItems;
    }

    public void setBannedItems(List<ItemStack> bannedItems) {
        this.bannedItems = bannedItems;
    }

    public boolean isAllowPlayerDamage() {
        return allowPlayerDamage;
    }

    public void setAllowPlayerDamage(boolean allowPlayerDamage) {
        this.allowPlayerDamage = allowPlayerDamage;
    }

    public boolean isAllowHunger() {
        return allowHunger;
    }

    public void setAllowHunger(boolean allowHunger) {
        this.allowHunger = allowHunger;
    }

    public String getBuildPermission() {
        return buildPermission;
    }

    
    
}
