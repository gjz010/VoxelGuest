/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thevoxelbox.voxelguest.modules.regions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Joe
 */
public class Region implements Serializable
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
    private boolean allowCreeperExplosions = false;
    private boolean allowBreakingPaintings = false;
    private boolean allowLavaFlow = false;
    private boolean allowWaterFlow = false;
    private boolean allowDragonEggMovement = false;
    private boolean allowSnowMelting = false;
    private boolean allowIceMelting = false;
    private boolean allowSnowFormation = false;
    private boolean allowIceFormation = false;
    private boolean allowEnchanting = false;
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
    
    //To-Do
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

    public boolean allowMobSpawn() {
        return allowMobSpawn;
    }

    public void setAllowMobSpawn(boolean allowMobSpawn) {
        this.allowMobSpawn = allowMobSpawn;
    }

    public boolean allowFireSpread() {
        return allowFireSpread;
    }

    public void setAllowFireSpread(boolean allowFireSpread) {
        this.allowFireSpread = allowFireSpread;
    }

    public boolean allowLeafyDecay() {
        return allowLeafDecay;
    }

    public void setAllowLeafDecay(boolean allowLeafDecay) {
        this.allowLeafDecay = allowLeafDecay;
    }

    public boolean allowBlockGrowth() {
        return allowBlockGrowth;
    }

    public void setAllowBlockGrowth(boolean allowBlockGrowth) {
        this.allowBlockGrowth = allowBlockGrowth;
    }

    public boolean allowBlockSpread() {
        return allowBlockSpread;
    }

    public void setAllowBlockSpread(boolean allowBlockSpread) {
        this.allowBlockSpread = allowBlockSpread;
    }

    public boolean allowExplosions() {
        return allowCreeperExplosions;
    }

    public void setAllowCreeperExplosions(boolean allowCreeperExplosions) {
        this.allowCreeperExplosions = allowCreeperExplosions;
    }

    public boolean allowBreakingPaintings() {
        return allowBreakingPaintings;
    }

    public void setAllowBreakingPaintings(boolean allowBreakingPaintings) {
        this.allowBreakingPaintings = allowBreakingPaintings;
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

    public boolean allowPlayerDamage() {
        return allowPlayerDamage;
    }

    public void setAllowPlayerDamage(boolean allowPlayerDamage) {
        this.allowPlayerDamage = allowPlayerDamage;
    }

    public boolean allowHunger() {
        return allowHunger;
    }

    public void setAllowHunger(boolean allowHunger) {
        this.allowHunger = allowHunger;
    }

    public String getBuildPermission() {
        return buildPermission;
    }

    public boolean allowLavaFlow() {
        return allowLavaFlow;
    }

    public void setAllowLavaFlow(boolean allowLavaFlow) {
        this.allowLavaFlow = allowLavaFlow;
    }

    public boolean allowWaterFlow() {
        return allowWaterFlow;
    }

    public void setAllowWaterFlow(boolean allowWaterFlow) {
        this.allowWaterFlow = allowWaterFlow;
    }

    public boolean allowDragonEggMovement() {
        return allowDragonEggMovement;
    }

    public void setAllowDragonEggMovement(boolean allowDragonEggMovement) {
        this.allowDragonEggMovement = allowDragonEggMovement;
    }

    public boolean allowSnowMelting() {
        return allowSnowMelting;
    }

    public void setAllowSnowMelting(boolean allowSnowMelting) {
        this.allowSnowMelting = allowSnowMelting;
    }

    public boolean allowIceMelting() {
        return allowIceMelting;
    }

    public void setAllowIceMelting(boolean allowIceMelting) {
        this.allowIceMelting = allowIceMelting;
    }

    public boolean allowSnowFormation() {
        return allowSnowFormation;
    }

    public boolean allowIceFormation() {
        return allowIceFormation;
    }

    public boolean allowEnchanting() {
        return allowEnchanting;
    }

    public void setAllowEnchanting(boolean allowEnchanting) {
        this.allowEnchanting = allowEnchanting;
    }

    public void setAllowSnowFormation(boolean allowSnowFormation) {
        this.allowSnowFormation = allowSnowFormation;
    }

    public void setAllowIceFormation(boolean allowIceFormation) {
        this.allowIceFormation = allowIceFormation;
    }
    
    
    
}
