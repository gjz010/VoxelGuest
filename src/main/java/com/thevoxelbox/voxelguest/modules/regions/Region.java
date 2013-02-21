package com.thevoxelbox.voxelguest.modules.regions;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Butters
 * @author Monofraps
 */
@DatabaseTable(tableName = "regions")
public class Region
{
    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField
    private String regionName;
    @DatabaseField
    private String worldName;
    @DatabaseField
    private boolean globalRegion; // indicates if this region is a global region
    @DatabaseField
    private int pointOneX = 0;
    @DatabaseField
    private int pointOneY = 0;
    @DatabaseField
    private int pointOneZ = 0;
    @DatabaseField
    private int pointTwoX = 0;
    @DatabaseField
    private int pointTwoY = 0;
    @DatabaseField
    private int pointTwoZ = 0;
    //World
    @DatabaseField
    private boolean buildingRestricted = true;
    @DatabaseField
    private boolean mobSpawnAllowed = false;
    @DatabaseField
    private boolean fireSpreadAllowed = false;
    @DatabaseField
    private boolean leafDecayAllowed = false;
    @DatabaseField
    private boolean blockGrowthAllowed = false;
    @DatabaseField
    private boolean blockSpreadAllowed = false;
    @DatabaseField
    private boolean blockDropAllowed = false;
    @DatabaseField
    private boolean creeperExplosionAllowed = false;
    @DatabaseField
    private boolean tntBreakingPaintingsAllowed = false;
    @DatabaseField
    private boolean lavaFlowAllowed = false;
    @DatabaseField
    private boolean waterFlowAllowed = false;
    @DatabaseField
    private boolean dragonEggMovementAllowed = false;
    @DatabaseField
    private boolean snowMeltingAllowed = false;
    @DatabaseField
    private boolean iceMeltingAllowed = false;
    @DatabaseField
    private boolean snowFormationAllowed = false;
    @DatabaseField
    private boolean iceFormationAllowed = false;
    @DatabaseField
    private boolean physicsAllowed = false;
    @DatabaseField
    private boolean enchantingAllowed = false;
    @DatabaseField
    private boolean creatureSpawnAllowed = false;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private ArrayList<Integer> bannedBlocks = new ArrayList<>();
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private ArrayList<Integer> bannedItems = new ArrayList<>();
    //Player
    @DatabaseField
    private boolean pvpDamageAllowed = false;
    @DatabaseField
    private boolean lavaDamageAllowed = false;
    @DatabaseField
    private boolean cactusDamageAllowed = false;
    @DatabaseField
    private boolean tntDamageAllowed = false;
    @DatabaseField
    private boolean drowningDamageAllowed = false;
    @DatabaseField
    private boolean explosiveDamageAllowed = false;
    @DatabaseField
    private boolean fallDamageAllowed = false;
    @DatabaseField
    private boolean fireDamageAllowed = false;
    @DatabaseField
    private boolean poisonDamageAllowed = false;
    @DatabaseField
    private boolean magicDamageAllowed = false;
    @DatabaseField
    private boolean projectileDamageAllowed = false;
    @DatabaseField
    private boolean hungerDamageAllowed = false;
    @DatabaseField
    private boolean voidDamageAllowed = false;
    @DatabaseField
    private boolean fireTickDamageAllowed = false;
    @DatabaseField
    private boolean lightningDamageAllowed = false;
    @DatabaseField
    private boolean suffocationDamageAllowed = false;
    @DatabaseField
    private boolean foodChangeAllowed = false;

    public Region()
    {
        
    }

    public Region(final String worldName, final Location pointOne, final Location pointTwo, final String regionName)
    {
        this.worldName = worldName;

        if(pointOne == null && pointTwo == null) {
            this.globalRegion = true;
        }

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
    }

    public final boolean inBounds(final Location locationToCheck)
    {
        if (!locationToCheck.getWorld().equals(getPointOne().getWorld()))
        {
            return false;
        }

        //For open worlds that do not have specified points
        if (globalRegion)
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

    public long getId()
    {
        return id;
    }

    public void setId(final long id)
    {
        this.id = id;
    }

    public String getRegionName()
    {
        return regionName;
    }

    public void setRegionName(final String regionName)
    {
        this.regionName = regionName;
    }

    public String getWorldName()
    {
        return worldName;
    }

    public void setWorldName(final String worldName)
    {
        this.worldName = worldName;
    }

    public int getPointOneX()
    {
        return pointOneX;
    }

    public void setPointOneX(final int pointOneX)
    {
        this.pointOneX = pointOneX;
    }

    public int getPointOneY()
    {
        return pointOneY;
    }

    public void setPointOneY(final int pointOneY)
    {
        this.pointOneY = pointOneY;
    }

    public int getPointOneZ()
    {
        return pointOneZ;
    }

    public void setPointOneZ(final int pointOneZ)
    {
        this.pointOneZ = pointOneZ;
    }

    public int getPointTwoX()
    {
        return pointTwoX;
    }

    public void setPointTwoX(final int pointTwoX)
    {
        this.pointTwoX = pointTwoX;
    }

    public int getPointTwoY()
    {
        return pointTwoY;
    }

    public void setPointTwoY(final int pointTwoY)
    {
        this.pointTwoY = pointTwoY;
    }

    public int getPointTwoZ()
    {
        return pointTwoZ;
    }

    public void setPointTwoZ(final int pointTwoZ)
    {
        this.pointTwoZ = pointTwoZ;
    }

    public boolean isMobSpawnAllowed()
    {
        return mobSpawnAllowed;
    }

    public void setMobSpawnAllowed(final boolean mobSpawnAllowed)
    {
        this.mobSpawnAllowed = mobSpawnAllowed;
    }

    public boolean isFireSpreadAllowed()
    {
        return fireSpreadAllowed;
    }

    public void setFireSpreadAllowed(final boolean fireSpreadAllowed)
    {
        this.fireSpreadAllowed = fireSpreadAllowed;
    }

    public boolean isLeafDecayAllowed()
    {
        return leafDecayAllowed;
    }

    public void setLeafDecayAllowed(final boolean leafDecayAllowed)
    {
        this.leafDecayAllowed = leafDecayAllowed;
    }

    public boolean isBlockGrowthAllowed()
    {
        return blockGrowthAllowed;
    }

    public void setBlockGrowthAllowed(final boolean blockGrowthAllowed)
    {
        this.blockGrowthAllowed = blockGrowthAllowed;
    }

    public boolean isBlockSpreadAllowed()
    {
        return blockSpreadAllowed;
    }

    public void setBlockSpreadAllowed(final boolean blockSpreadAllowed)
    {
        this.blockSpreadAllowed = blockSpreadAllowed;
    }

    public boolean isBlockDropAllowed()
    {
        return blockDropAllowed;
    }

    public void setBlockDropAllowed(final boolean blockDropAllowed)
    {
        this.blockDropAllowed = blockDropAllowed;
    }

    public boolean isCreeperExplosionAllowed()
    {
        return creeperExplosionAllowed;
    }

    public void setCreeperExplosionAllowed(final boolean creeperExplosionAllowed)
    {
        this.creeperExplosionAllowed = creeperExplosionAllowed;
    }

    public boolean isTntBreakingPaintingsAllowed()
    {
        return tntBreakingPaintingsAllowed;
    }

    public void setTntBreakingPaintingsAllowed(final boolean tntBreakingPaintingsAllowed)
    {
        this.tntBreakingPaintingsAllowed = tntBreakingPaintingsAllowed;
    }

    public boolean isLavaFlowAllowed()
    {
        return lavaFlowAllowed;
    }

    public void setLavaFlowAllowed(final boolean lavaFlowAllowed)
    {
        this.lavaFlowAllowed = lavaFlowAllowed;
    }

    public boolean isWaterFlowAllowed()
    {
        return waterFlowAllowed;
    }

    public void setWaterFlowAllowed(final boolean waterFlowAllowed)
    {
        this.waterFlowAllowed = waterFlowAllowed;
    }

    public boolean isDragonEggMovementAllowed()
    {
        return dragonEggMovementAllowed;
    }

    public void setDragonEggMovementAllowed(final boolean dragonEggMovementAllowed)
    {
        this.dragonEggMovementAllowed = dragonEggMovementAllowed;
    }

    public boolean isSnowMeltingAllowed()
    {
        return snowMeltingAllowed;
    }

    public void setSnowMeltingAllowed(final boolean snowMeltingAllowed)
    {
        this.snowMeltingAllowed = snowMeltingAllowed;
    }

    public boolean isIceMeltingAllowed()
    {
        return iceMeltingAllowed;
    }

    public void setIceMeltingAllowed(final boolean iceMeltingAllowed)
    {
        this.iceMeltingAllowed = iceMeltingAllowed;
    }

    public boolean isSnowFormationAllowed()
    {
        return snowFormationAllowed;
    }

    public void setSnowFormationAllowed(final boolean snowFormationAllowed)
    {
        this.snowFormationAllowed = snowFormationAllowed;
    }

    public boolean isIceFormationAllowed()
    {
        return iceFormationAllowed;
    }

    public void setIceFormationAllowed(final boolean iceFormationAllowed)
    {
        this.iceFormationAllowed = iceFormationAllowed;
    }

    public boolean isEnchantingAllowed()
    {
        return enchantingAllowed;
    }

    public void setEnchantingAllowed(final boolean enchantingAllowed)
    {
        this.enchantingAllowed = enchantingAllowed;
    }

    public boolean isPhysicsAllowed() {
        return physicsAllowed;
    }

    public void setPhysicsAllowed(boolean physicsAllowed) {
        this.physicsAllowed = physicsAllowed;
    }

    /**
     * @return the creatureSpawnAllowed
     */
    public boolean isCreatureSpawnAllowed() {
        return creatureSpawnAllowed;
    }

    /**
     * @param creatureSpawnAllowed the creatureSpawnAllowed to set
     */
    public void setCreatureSpawnAllowed(boolean creatureSpawnAllowed) {
        this.creatureSpawnAllowed = creatureSpawnAllowed;
    }

    public List<Integer> getBannedBlocks()
    {
        return bannedBlocks;
    }

    public void setBannedBlocks(final List<Integer> bannedBlocks)
    {
        ArrayList<Integer> newList = new ArrayList<Integer>();
        newList.addAll(bannedBlocks);
        this.bannedBlocks = newList;
    }

    public List<Integer> getBannedItems()
    {
        return bannedItems;
    }

    public void setBannedItems(final List<Integer> bannedItems)
    {
        ArrayList<Integer> newList = new ArrayList<Integer>();
        newList.addAll(bannedItems);
        this.bannedItems = newList;
    }

    public boolean isPvpDamageAllowed()
    {
        return pvpDamageAllowed;
    }

    public void setPvpDamageAllowed(final boolean pvpDamageAllowed)
    {
        this.pvpDamageAllowed = pvpDamageAllowed;
    }

    public boolean isLavaDamageAllowed()
    {
        return lavaDamageAllowed;
    }

    public void setLavaDamageAllowed(final boolean lavaDamageAllowed)
    {
        this.lavaDamageAllowed = lavaDamageAllowed;
    }

    public boolean isCactusDamageAllowed()
    {
        return cactusDamageAllowed;
    }

    public void setCactusDamageAllowed(final boolean cactusDamageAllowed)
    {
        this.cactusDamageAllowed = cactusDamageAllowed;
    }

    public boolean isTntDamageAllowed()
    {
        return tntDamageAllowed;
    }

    public void setTntDamageAllowed(final boolean tntDamageAllowed)
    {
        this.tntDamageAllowed = tntDamageAllowed;
    }

    public boolean isDrowningDamageAllowed()
    {
        return drowningDamageAllowed;
    }

    public void setDrowningDamageAllowed(final boolean drowningDamageAllowed)
    {
        this.drowningDamageAllowed = drowningDamageAllowed;
    }

    public boolean isExplosiveDamageAllowed()
    {
        return explosiveDamageAllowed;
    }

    public void setExplosiveDamageAllowed(final boolean explosiveDamageAllowed)
    {
        this.explosiveDamageAllowed = explosiveDamageAllowed;
    }

    public boolean isFallDamageAllowed()
    {
        return fallDamageAllowed;
    }

    public void setFallDamageAllowed(final boolean fallDamageAllowed)
    {
        this.fallDamageAllowed = fallDamageAllowed;
    }

    public boolean isFireDamageAllowed()
    {
        return fireDamageAllowed;
    }

    public void setFireDamageAllowed(final boolean fireDamageAllowed)
    {
        this.fireDamageAllowed = fireDamageAllowed;
    }

    public boolean isPoisonDamageAllowed()
    {
        return poisonDamageAllowed;
    }

    public void setPoisonDamageAllowed(final boolean poisonDamageAllowed)
    {
        this.poisonDamageAllowed = poisonDamageAllowed;
    }

    public boolean isMagicDamageAllowed()
    {
        return magicDamageAllowed;
    }

    public void setMagicDamageAllowed(final boolean magicDamageAllowed)
    {
        this.magicDamageAllowed = magicDamageAllowed;
    }

    public boolean isProjectileDamageAllowed()
    {
        return projectileDamageAllowed;
    }

    public void setProjectileDamageAllowed(final boolean projectileDamageAllowed)
    {
        this.projectileDamageAllowed = projectileDamageAllowed;
    }

    public boolean isHungerDamageAllowed()
    {
        return hungerDamageAllowed;
    }

    public void setHungerDamageAllowed(final boolean hungerDamageAllowed)
    {
        this.hungerDamageAllowed = hungerDamageAllowed;
    }

    public boolean isVoidDamageAllowed()
    {
        return voidDamageAllowed;
    }

    public void setVoidDamageAllowed(final boolean voidDamageAllowed)
    {
        this.voidDamageAllowed = voidDamageAllowed;
    }

    public boolean isFireTickDamageAllowed()
    {
        return fireTickDamageAllowed;
    }

    public void setFireTickDamageAllowed(final boolean fireTickDamageAllowed)
    {
        this.fireTickDamageAllowed = fireTickDamageAllowed;
    }

    public boolean isLightningDamageAllowed()
    {
        return lightningDamageAllowed;
    }

    public void setLightningDamageAllowed(final boolean lightningDamageAllowed)
    {
        this.lightningDamageAllowed = lightningDamageAllowed;
    }

    public boolean isSuffocationDamageAllowed()
    {
        return suffocationDamageAllowed;
    }

    public void setSuffocationDamageAllowed(final boolean suffocationDamageAllowed)
    {
        this.suffocationDamageAllowed = suffocationDamageAllowed;
    }

    public boolean isFoodChangeAllowed()
    {
        return foodChangeAllowed;
    }

    public void setFoodChangeAllowed(final boolean foodChangeAllowed)
    {
        this.foodChangeAllowed = foodChangeAllowed;
    }

    public boolean isGlobal()
    {
        return globalRegion;
    }

    public void setGlobal(final boolean globalRegion)
    {
        this.globalRegion = globalRegion;
    }

    public boolean isBuildingRestricted()
    {
        return buildingRestricted;
    }

    public void setBuildingRestricted(final boolean buildingRestricted)
    {
        this.buildingRestricted = buildingRestricted;
    }
    
    public String toColoredString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.GRAY + "Region info for: " + ChatColor.GREEN + this.getRegionName() + ChatColor.GRAY + ":\n");
        builder.append(ChatColor.GRAY + "World: " + ChatColor.GREEN + this.getPointOne().getWorld().getName() + "\n");
        builder.append(ChatColor.GRAY + "Type: " + ChatColor.GREEN + this.isGlobal() + "\n");
        if (!this.isGlobal())
        {
            builder.append(ChatColor.GRAY + "Point one: " + ChatColor.DARK_GRAY + "(" + ChatColor.GREEN + this.getPointOne().getX() + ChatColor.DARK_GRAY + ", " + ChatColor.GREEN + this.getPointOne().getZ() + ChatColor.DARK_GRAY + ")\n");
            builder.append(ChatColor.GRAY + "Point two: " + ChatColor.DARK_GRAY + "(" + ChatColor.GREEN + this.getPointTwo().getX() + ChatColor.DARK_GRAY + ", " + ChatColor.GREEN + this.getPointTwo().getZ() + ChatColor.DARK_GRAY + ")\n");
        }
        return builder.toString();
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Region info for: " + this.getRegionName() + ":\n");
        builder.append("World: " + this.getPointOne().getWorld().getName() + "\n");
        builder.append("Type: " + this.isGlobal() + "\n");
        if (!this.isGlobal())
        {
            builder.append("Point one: (" + this.getPointOne().getX() + ", " + this.getPointOne().getZ() + ")\n");
            builder.append("Point two: (" + this.getPointTwo().getX() + ", " + this.getPointTwo().getZ() + ")\n");
        }
        return builder.toString();
    }
}
