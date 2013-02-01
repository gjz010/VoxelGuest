/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thevoxelbox.voxelguest.modules.regions;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Joe
 */
public class Region implements Serializable
{

	public final String regionName;
	public final String worldName;
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

	public Region(final String worldName, final Location pointOne, final Location pointTwo, final String regionName, final String buildPermission)
	{
		this.worldName = worldName;
		this.pointOne = pointOne;
		this.pointTwo = pointTwo;
		this.regionName = regionName;
		this.buildPermission = buildPermission;
	}

	public final boolean isLocationInRegion(final Location locationToCheck)
	{
		if (!locationToCheck.getWorld().getName().equalsIgnoreCase(worldName))
		{
			return false;
		}

		//For open worlds that do not have specified points
		if (pointOne == null && pointTwo == null)
		{
			return true;
		}

		return locationToCheck.toVector().isInAABB(Vector.getMinimum(pointOne.toVector(), pointTwo.toVector()), Vector.getMaximum(pointOne.toVector(), pointTwo.toVector()));

	}

	public final Location getPointOne()
	{
		return pointOne;
	}

	public final void setPointOne(final Location pointOne)
	{
		this.pointOne = pointOne;
	}

	public final Location getPointTwo()
	{
		return pointTwo;
	}

	public final void setPointTwo(final Location pointTwo)
	{
		this.pointTwo = pointTwo;
	}

	public final boolean isMobSpawnAllowed()
	{
		return allowMobSpawn;
	}

	public final void setAllowMobSpawn(final boolean allowMobSpawn)
	{
		this.allowMobSpawn = allowMobSpawn;
	}

	public final boolean isFireSpreadAllowed()
	{
		return allowFireSpread;
	}

	public final void setAllowFireSpread(final boolean allowFireSpread)
	{
		this.allowFireSpread = allowFireSpread;
	}

	public final boolean isLeafDecayAllowed()
	{
		return allowLeafDecay;
	}

	public final void setAllowLeafDecay(final boolean allowLeafDecay)
	{
		this.allowLeafDecay = allowLeafDecay;
	}

	public final boolean isBlowGrowthAllowed()
	{
		return allowBlockGrowth;
	}

	public final void setAllowBlockGrowth(final boolean allowBlockGrowth)
	{
		this.allowBlockGrowth = allowBlockGrowth;
	}

	public final boolean isBlockSpreadAllowed()
	{
		return allowBlockSpread;
	}

	public final void setAllowBlockSpread(final boolean allowBlockSpread)
	{
		this.allowBlockSpread = allowBlockSpread;
	}

	public final boolean isCreeperExplosionsAllowed()
	{
		return allowCreeperExplosions;
	}

	public final void setAllowCreeperExplosions(final boolean allowCreeperExplosions)
	{
		this.allowCreeperExplosions = allowCreeperExplosions;
	}

	public final boolean isBreakingPaintingsAllowed()
	{
		return allowBreakingPaintings;
	}

	public final void setAllowBreakingPaintings(boolean allowBreakingPaintings)
	{
		this.allowBreakingPaintings = allowBreakingPaintings;
	}

	public final List<Block> getBannedBlocks()
	{
		return bannedBlocks;
	}

	public final void setBannedBlocks(List<Block> bannedBlocks)
	{
		this.bannedBlocks = bannedBlocks;
	}

	public final List<ItemStack> getBannedItems()
	{
		return bannedItems;
	}

	public final void setBannedItems(final List<ItemStack> bannedItems)
	{
		this.bannedItems = bannedItems;
	}

	public final boolean isPlayerDamageAllowed()
	{
		return allowPlayerDamage;
	}

	public final void setAllowPlayerDamage(final boolean allowPlayerDamage)
	{
		this.allowPlayerDamage = allowPlayerDamage;
	}

	public final boolean isPlayerHungerAllowed()
	{
		return allowHunger;
	}

	public final void setAllowHunger(final boolean allowHunger)
	{
		this.allowHunger = allowHunger;
	}

	public final String getBuildPermission()
	{
		return buildPermission;
	}

	public final boolean isLavaFlowAllowed()
	{
		return allowLavaFlow;
	}

	public final void setAllowLavaFlow(final boolean allowLavaFlow)
	{
		this.allowLavaFlow = allowLavaFlow;
	}

	public final boolean isWaterFlowAllowed()
	{
		return allowWaterFlow;
	}

	public final void setAllowWaterFlow(final boolean allowWaterFlow)
	{
		this.allowWaterFlow = allowWaterFlow;
	}

	public final boolean isDragonEggMovementAllowed()
	{
		return allowDragonEggMovement;
	}

	public final void setAllowDragonEggMovement(final boolean allowDragonEggMovement)
	{
		this.allowDragonEggMovement = allowDragonEggMovement;
	}

	public final boolean isSnowMeltingAllowed()
	{
		return allowSnowMelting;
	}

	public final void setAllowSnowMelting(final boolean allowSnowMelting)
	{
		this.allowSnowMelting = allowSnowMelting;
	}

	public final boolean isIceMeltingAllowed()
	{
		return allowIceMelting;
	}

	public final void setAllowIceMelting(final boolean allowIceMelting)
	{
		this.allowIceMelting = allowIceMelting;
	}

	public final boolean isSnowFormationAllowed()
	{
		return allowSnowFormation;
	}

	public final boolean isIceFormationAllowed()
	{
		return allowIceFormation;
	}

	public final boolean isEnchantingAllowed()
	{
		return allowEnchanting;
	}

	public final void setAllowEnchanting(final boolean allowEnchanting)
	{
		this.allowEnchanting = allowEnchanting;
	}

	public final void setAllowSnowFormation(final boolean allowSnowFormation)
	{
		this.allowSnowFormation = allowSnowFormation;
	}

	public final void setAllowIceFormation(final boolean allowIceFormation)
	{
		this.allowIceFormation = allowIceFormation;
	}

}
