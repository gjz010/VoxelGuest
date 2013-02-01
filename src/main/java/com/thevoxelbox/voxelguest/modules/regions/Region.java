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
	public final String regionName;
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

	public final List<Material> getBannedBlocks()
	{
		return bannedBlocks;
	}

	public final void setBannedBlocks(List<Material> bannedBlocks)
	{
		this.bannedBlocks = bannedBlocks;
	}

	public final List<Material> getBannedItems()
	{
		return bannedItems;
	}

	public final void setBannedItems(final List<Material> bannedItems)
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
