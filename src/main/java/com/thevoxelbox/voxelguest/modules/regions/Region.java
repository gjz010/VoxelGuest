/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thevoxelbox.voxelguest.modules.regions;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Joe
 * @author Monofraps
 */
@Entity
@Table(name = "regions")
public class Region implements Serializable
{
	@Id
	@Column
	private long id;

	@Column
	private final String regionName;
	@Column
	private Location pointOne;
	@Column
	private Location pointTwo;

	//World
	@Column
	private boolean allowMobSpawn = false;
	@Column
	private boolean allowFireSpread = false;
	@Column
	private boolean allowLeafDecay = false;
	@Column
	private boolean allowBlockGrowth = false;
	@Column
	private boolean allowBlockSpread = false;
	@Column
	private boolean allowCreeperExplosions = false;
	@Column
	private boolean allowBreakingPaintings = false;
	@Column
	private boolean allowLavaFlow = false;
	@Column
	private boolean allowWaterFlow = false;
	@Column
	private boolean allowDragonEggMovement = false;
	@Column
	private boolean allowSnowMelting = false;
	@Column
	private boolean allowIceMelting = false;
	@Column
	private boolean allowSnowFormation = false;
	@Column
	private boolean allowIceFormation = false;
	@Column
	private boolean allowEnchanting = false;
	@Column
	private List<Material> bannedBlocks = new ArrayList<>();
	@Column
	private List<Material> bannedItems = new ArrayList<>();
	@Column
	private String buildPermission;

	//Player
	@Column
	private boolean allowPlayerDamage;
	@Column
	private boolean allowHunger;

	public Region(final String worldName, final Location pointOne, final Location pointTwo, final String regionName, final String buildPermission)
	{
		this.pointOne = pointOne;
		this.pointTwo = pointTwo;
		this.regionName = regionName;
		this.buildPermission = buildPermission;
	}

	public final boolean isLocationInRegion(final Location locationToCheck)
	{
		if (!locationToCheck.getWorld().equals(this.pointOne.getWorld()))
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

	public Location getPointOne()
	{
		return pointOne;
	}

	public void setPointOne(final Location pointOne)
	{
		this.pointOne = pointOne;
	}

	public Location getPointTwo()
	{
		return pointTwo;
	}

	public void setPointTwo(final Location pointTwo)
	{
		this.pointTwo = pointTwo;
	}

	public boolean isMobSpawnAllowed()
	{
		return allowMobSpawn;
	}

	public void setAllowMobSpawn(final boolean allowMobSpawn)
	{
		this.allowMobSpawn = allowMobSpawn;
	}

	public boolean isFireSpreadAllowed()
	{
		return allowFireSpread;
	}

	public void setAllowFireSpread(final boolean allowFireSpread)
	{
		this.allowFireSpread = allowFireSpread;
	}

	public boolean isLeafDecayAllowed()
	{
		return allowLeafDecay;
	}

	public void setAllowLeafDecay(final boolean allowLeafDecay)
	{
		this.allowLeafDecay = allowLeafDecay;
	}

	public boolean isBlowGrowthAllowed()
	{
		return allowBlockGrowth;
	}

	public void setAllowBlockGrowth(final boolean allowBlockGrowth)
	{
		this.allowBlockGrowth = allowBlockGrowth;
	}

	public boolean isBlockSpreadAllowed()
	{
		return allowBlockSpread;
	}

	public void setAllowBlockSpread(final boolean allowBlockSpread)
	{
		this.allowBlockSpread = allowBlockSpread;
	}

	public boolean isCreeperExplosionsAllowed()
	{
		return allowCreeperExplosions;
	}

	public void setAllowCreeperExplosions(final boolean allowCreeperExplosions)
	{
		this.allowCreeperExplosions = allowCreeperExplosions;
	}

	public boolean isBreakingPaintingsAllowed()
	{
		return allowBreakingPaintings;
	}

	public void setAllowBreakingPaintings(boolean allowBreakingPaintings)
	{
		this.allowBreakingPaintings = allowBreakingPaintings;
	}

	public List<Material> getBannedBlocks()
	{
		return bannedBlocks;
	}

	public void setBannedBlocks(List<Material> bannedBlocks)
	{
		this.bannedBlocks = bannedBlocks;
	}

	public List<Material> getBannedItems()
	{
		return bannedItems;
	}

	public void setBannedItems(final List<Material> bannedItems)
	{
		this.bannedItems = bannedItems;
	}

	public boolean isPlayerDamageAllowed()
	{
		return allowPlayerDamage;
	}

	public void setAllowPlayerDamage(final boolean allowPlayerDamage)
	{
		this.allowPlayerDamage = allowPlayerDamage;
	}

	public boolean isPlayerHungerAllowed()
	{
		return allowHunger;
	}

	public void setAllowHunger(final boolean allowHunger)
	{
		this.allowHunger = allowHunger;
	}

	public String getBuildPermission()
	{
		return buildPermission;
	}

	public boolean isLavaFlowAllowed()
	{
		return allowLavaFlow;
	}

	public void setAllowLavaFlow(final boolean allowLavaFlow)
	{
		this.allowLavaFlow = allowLavaFlow;
	}

	public boolean isWaterFlowAllowed()
	{
		return allowWaterFlow;
	}

	public void setAllowWaterFlow(final boolean allowWaterFlow)
	{
		this.allowWaterFlow = allowWaterFlow;
	}

	public boolean isDragonEggMovementAllowed()
	{
		return allowDragonEggMovement;
	}

	public void setAllowDragonEggMovement(final boolean allowDragonEggMovement)
	{
		this.allowDragonEggMovement = allowDragonEggMovement;
	}

	public boolean isSnowMeltingAllowed()
	{
		return allowSnowMelting;
	}

	public void setAllowSnowMelting(final boolean allowSnowMelting)
	{
		this.allowSnowMelting = allowSnowMelting;
	}

	public boolean isIceMeltingAllowed()
	{
		return allowIceMelting;
	}

	public void setAllowIceMelting(final boolean allowIceMelting)
	{
		this.allowIceMelting = allowIceMelting;
	}

	public boolean isSnowFormationAllowed()
	{
		return allowSnowFormation;
	}

	public boolean isIceFormationAllowed()
	{
		return allowIceFormation;
	}

	public boolean isEnchantingAllowed()
	{
		return allowEnchanting;
	}

	public void setAllowEnchanting(final boolean allowEnchanting)
	{
		this.allowEnchanting = allowEnchanting;
	}

	public void setAllowSnowFormation(final boolean allowSnowFormation)
	{
		this.allowSnowFormation = allowSnowFormation;
	}

	public void setAllowIceFormation(final boolean allowIceFormation)
	{
		this.allowIceFormation = allowIceFormation;
	}

}
