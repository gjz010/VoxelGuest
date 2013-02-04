package com.thevoxelbox.voxelguest.modules.regions;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Butters
 * @author Monofraps
 */
@DatabaseTable(tableName = "regions")
public class Region implements Serializable
{

    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField
    private String regionName;
    @DatabaseField
    private String worldName;
    @DatabaseField
    private int pointOneX;
    @DatabaseField
    private int pointOneY;
    @DatabaseField
    private int pointOneZ;
    @DatabaseField
    private int pointTwoX;
    @DatabaseField
    private int pointTwoY;
    @DatabaseField
    private int pointTwoZ;
    //World
    @DatabaseField
    private boolean allowMobSpawn = false;
    @DatabaseField
    private boolean allowFireSpread = false;
    @DatabaseField
    private boolean allowLeafDecay = false;
    @DatabaseField
    private boolean allowBlockGrowth = false;
    @DatabaseField
    private boolean allowBlockSpread = false;
    @DatabaseField
    private boolean allowCreeperExplosions = false;
    @DatabaseField
    private boolean allowTntBreakingPaintings = false;
    @DatabaseField
    private boolean allowLavaFlow = false;
    @DatabaseField
    private boolean allowWaterFlow = false;
    @DatabaseField
    private boolean allowDragonEggMovement = false;
    @DatabaseField
    private boolean allowSnowMelting = false;
    @DatabaseField
    private boolean allowIceMelting = false;
    @DatabaseField
    private boolean allowSnowFormation = false;
    @DatabaseField
    private boolean allowIceFormation = false;
    @DatabaseField
    private boolean allowEnchanting = false;
    @DatabaseField
    private List<Integer> bannedBlocks = new ArrayList<>();
    @DatabaseField
    private List<Integer> bannedItems = new ArrayList<>();
    @DatabaseField
    private String buildPermission;
    //Player
    @DatabaseField
    private boolean allowPvPDamage = false;
    @DatabaseField
    private boolean allowLavaDamage = false;
    @DatabaseField
    private boolean allowCactusDamage = false;
    @DatabaseField
    private boolean allowTnTDamage = false;
    @DatabaseField
    private boolean allowDrowningDamage = false;
    @DatabaseField
    private boolean allowExplosiveDamage = false;
    @DatabaseField
    private boolean allowFallDamage = false;
    @DatabaseField
    private boolean allowFireDamage = false;
    @DatabaseField
    private boolean allowPoisonDamage = false;
    @DatabaseField
    private boolean allowMagicDamage = false;
    @DatabaseField
    private boolean allowProjectileDamage = false;
    @DatabaseField
    private boolean allowHungerDamage = false;
    @DatabaseField
    private boolean allowVoidDamage = false;
    @DatabaseField
    private boolean allowFireTickDamage = false;
    @DatabaseField
    private boolean allowLightningDamage = false;
    @DatabaseField
    private boolean allowSuffocationDamage = false;
    @DatabaseField
    private boolean allowFoodChange = false;

    public Region()
    {
    }

    public Region(final String worldName, final Location pointOne, final Location pointTwo, final String regionName, final String buildPermission)
    {
        this.worldName = pointOne.getWorld().getName();
        if (pointOne != null)
        {
            this.pointOneX = pointOne.getBlockX();
            this.pointOneY = pointOne.getBlockY();
            this.pointOneZ = pointOne.getBlockZ();
        }
        if (pointTwo != null)
        {
            this.pointTwoX = pointTwo.getBlockX();
            this.pointTwoY = pointTwo.getBlockY();
            this.pointTwoZ = pointTwo.getBlockZ();
        }
        this.regionName = regionName;
        this.buildPermission = buildPermission;
    }

    public final boolean isLocationInRegion(final Location locationToCheck)
    {
        if (!locationToCheck.getWorld().equals(getPointOne().getWorld()))
        {
            return false;
        }

        //For open worlds that do not have specified points
        if (getPointOne() == null && getPointTwo() == null)
        {
            return true;
        }

        return locationToCheck.toVector().isInAABB(Vector.getMinimum(getPointOne().toVector(), getPointTwo().toVector()), Vector.getMaximum(getPointOne().toVector(), getPointTwo().toVector()));

    }

    public Location getPointOne()
    {
        return new Location(Bukkit.getWorld(worldName), pointOneX, pointOneY, pointOneZ);
    }

    public void setPointOne(final Location pointOne)
    {
        this.worldName = pointOne.getWorld().getName();
        this.pointOneX = pointOne.getBlockX();
        this.pointOneY = pointOne.getBlockY();
        this.pointOneZ = pointOne.getBlockZ();
    }

    public Location getPointTwo()
    {
        return new Location(Bukkit.getWorld(worldName), pointTwoX, pointTwoY, pointTwoZ);
    }

    public void setPointTwo(final Location pointTwo)
    {
        this.worldName = pointTwo.getWorld().getName();
        this.pointTwoX = pointTwo.getBlockX();
        this.pointTwoY = pointTwo.getBlockY();
        this.pointTwoZ = pointTwo.getBlockZ();
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

    public boolean isTntBreakingPaintingsAllowed()
    {
        return allowTntBreakingPaintings;
    }

    public void setAllowBreakingPaintings(boolean allowBreakingPaintings)
    {
        this.allowTntBreakingPaintings = allowBreakingPaintings;
    }

    public List<Integer> getBannedBlocks()
    {
        return bannedBlocks;
    }

    public void setBannedBlocks(List<Integer> bannedBlocks)
    {
        this.bannedBlocks = bannedBlocks;
    }

    public List<Integer> getBannedItems()
    {
        return bannedItems;
    }

    public void setBannedItems(final List<Integer> bannedItems)
    {
        this.bannedItems = bannedItems;
    }

    public boolean isPlayerDamageAllowed()
    {
        return allowPvPDamage;
    }

    public void setAllowPlayerDamage(final boolean allowPlayerDamage)
    {
        this.allowPvPDamage = allowPlayerDamage;
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

    public boolean isAllowPvPDamage()
    {
        return allowPvPDamage;
    }

    public void setAllowPvPDamage(boolean allowPvPDamage)
    {
        this.allowPvPDamage = allowPvPDamage;
    }

    public boolean isAllowLavaDamage()
    {
        return allowLavaDamage;
    }

    public void setAllowLavaDamage(boolean allowLavaDamage)
    {
        this.allowLavaDamage = allowLavaDamage;
    }

    public boolean isAllowCactusDamage()
    {
        return allowCactusDamage;
    }

    public void setAllowCactusDamage(boolean allowCactusDamage)
    {
        this.allowCactusDamage = allowCactusDamage;
    }

    public boolean isAllowTnTDamage()
    {
        return allowTnTDamage;
    }

    public void setAllowTnTDamage(boolean allowTnTDamage)
    {
        this.allowTnTDamage = allowTnTDamage;
    }

    public boolean isAllowDrowningDamage()
    {
        return allowDrowningDamage;
    }

    public void setAllowDrowningDamage(boolean allowDrowningDamage)
    {
        this.allowDrowningDamage = allowDrowningDamage;
    }

    public boolean isAllowExplosiveDamage()
    {
        return allowExplosiveDamage;
    }

    public void setAllowExplosiveDamage(boolean allowExplosiveDamage)
    {
        this.allowExplosiveDamage = allowExplosiveDamage;
    }

    public boolean isAllowFallDamage()
    {
        return allowFallDamage;
    }

    public void setAllowFallDamage(boolean allowFallDamage)
    {
        this.allowFallDamage = allowFallDamage;
    }

    public boolean isAllowFireDamage()
    {
        return allowFireDamage;
    }

    public void setAllowFireDamage(boolean allowFireDamage)
    {
        this.allowFireDamage = allowFireDamage;
    }

    public boolean isAllowPoisonDamage()
    {
        return allowPoisonDamage;
    }

    public void setAllowPoisonDamage(boolean allowPoisonDamage)
    {
        this.allowPoisonDamage = allowPoisonDamage;
    }

    public boolean isAllowMagicDamage()
    {
        return allowMagicDamage;
    }

    public void setAllowMagicDamage(boolean allowMagicDamage)
    {
        this.allowMagicDamage = allowMagicDamage;
    }

    public boolean isAllowProjectileDamage()
    {
        return allowProjectileDamage;
    }

    public void setAllowProjectileDamage(boolean allowProjectileDamage)
    {
        this.allowProjectileDamage = allowProjectileDamage;
    }

    public boolean isAllowHungerDamage()
    {
        return allowHungerDamage;
    }

    public void setAllowHungerDamage(boolean allowHungerDamage)
    {
        this.allowHungerDamage = allowHungerDamage;
    }

    public boolean isAllowVoidDamage()
    {
        return allowVoidDamage;
    }

    public void setAllowVoidDamage(boolean allowVoidDamage)
    {
        this.allowVoidDamage = allowVoidDamage;
    }

    public boolean isAllowFoodChange()
    {
        return allowFoodChange;
    }

    public void setAllowFoodChange(boolean allowFoodChange)
    {
        this.allowFoodChange = allowFoodChange;
    }

    public boolean isAllowTntBreakingPaintings()
    {
        return allowTntBreakingPaintings;
    }

    public void setAllowTntBreakingPaintings(boolean allowTntBreakingPaintings)
    {
        this.allowTntBreakingPaintings = allowTntBreakingPaintings;
    }

    public boolean isAllowFireTickDamage()
    {
        return allowFireTickDamage;
    }

    public void setAllowFireTickDamage(boolean allowFireTickDamage)
    {
        this.allowFireTickDamage = allowFireTickDamage;
    }

    public boolean isAllowLightningDamage()
    {
        return allowLightningDamage;
    }

    public void setAllowLightningDamage(boolean allowLightningDamage)
    {
        this.allowLightningDamage = allowLightningDamage;
    }

    public boolean isAllowSuffocationDamage()
    {
        return allowSuffocationDamage;
    }

    public void setAllowSuffocationDamage(boolean allowSuffocationDamage)
    {
        this.allowSuffocationDamage = allowSuffocationDamage;
    }

    public String getRegionName()
    {
        return regionName;
    }


}
