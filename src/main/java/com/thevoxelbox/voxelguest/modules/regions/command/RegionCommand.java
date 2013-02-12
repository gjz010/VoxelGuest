package com.thevoxelbox.voxelguest.modules.regions.command;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import com.thevoxelbox.voxelguest.modules.regions.Region;
import com.thevoxelbox.voxelguest.modules.regions.RegionModule;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Butters
 */
public class RegionCommand implements CommandExecutor
{

    private RegionModule regionModule;

    public RegionCommand(RegionModule regionModule)
    {
        this.regionModule = regionModule;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmnd, final String string, final String[] args)
    {
        if (!(sender.hasPermission("voxelguest.regions.modifyregion")))
        {
            sender.sendMessage(ChatColor.RED + "Invalid permissions");
            return false;
        }

        if (args.length == 0)
        {
            sender.sendMessage("/vgregion <option>");
            sender.sendMessage("Ex: /vgregion help");
            return false;
        }

        if (args[0].equalsIgnoreCase("create"))
        {
            createRegion(sender, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("help"))
        {
            printHelp(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("edit"))
        {
            //TODO: Write edit code
            return true;
        }

        if (args[0].equalsIgnoreCase("remove"))
        {
            //TODO: Write region remove code
            return true;
        }

        return false;
    }

    private void printHelp(CommandSender sender)
    {
        sender.sendMessage("To create a new region syntax is: /vgregion create [name] [x1] [z1] [x2] [z2] <-Flags>");
    }

    private void createRegion(final CommandSender sender, final String[] args)
    {
        if (args.length >= 6)
        {
            final String regionName = args[1];
            int x1 = 0, z1 = 0, x2 = 0, z2 = 0;
            try
            {
                x1 = Integer.parseInt(args[2]);
                z1 = Integer.parseInt(args[3]);
                x2 = Integer.parseInt(args[4]);
                z2 = Integer.parseInt(args[5]);
            }
            catch(NumberFormatException e)
            {
                sender.sendMessage("Error in  parsing arguments: invalid syntax");
                sender.sendMessage(e.getMessage());
            }
            final World regionWorld = ((Player) sender).getWorld();
            final Map<CommandFlags, String> flags = CommandFlags.parseFlags(args); //Flag and state in a map
            final Location pointOne = new Location(regionWorld, x1, 0, z1);
            final Location pointTwo = new Location(regionWorld, x2, regionWorld.getMaxHeight(), z2);
            final Region newRegion = new Region(regionWorld.getName(), pointOne, pointTwo, regionName);
            this.processFlags(flags, newRegion);
            this.regionModule.getRegionManager().addRegion(newRegion);
            this.printRegionInfo(sender, newRegion);
        }
    }

    private void printRegionInfo(CommandSender cs, Region region)
    {
        cs.sendMessage(ChatColor.GRAY + "Region info for: " + ChatColor.GREEN + region.getRegionName() + ChatColor.GRAY + ":");
        cs.sendMessage(ChatColor.GRAY + "World: " + ChatColor.GREEN + region.getPointOne().getWorld().getName());
        cs.sendMessage(ChatColor.GRAY + "Point one: " + ChatColor.DARK_GRAY + "(" + ChatColor.GREEN + region.getPointOne().getX() + ChatColor.DARK_GRAY + ", " + ChatColor.GREEN + region.getPointOne().getZ() + ChatColor.DARK_GRAY + ")");
        cs.sendMessage(ChatColor.GRAY + "Point two: " + ChatColor.DARK_GRAY + "(" + ChatColor.GREEN + region.getPointTwo().getX() + ChatColor.DARK_GRAY + ", " + ChatColor.GREEN + region.getPointTwo().getZ() + ChatColor.DARK_GRAY + ")");
    }

    public void processFlags(Map<CommandFlags, String> flags, Region region)
    {
        if (flags.containsKey(CommandFlags.BANNED_BLOCKS))
        {
            String state = flags.get(CommandFlags.BANNED_BLOCKS);
            flags.remove(CommandFlags.BANNED_BLOCKS);
            this.processBlockList(state);
            region.setBannedBlocks(Arrays.asList(this.processBlockList(state)));
        }
        if (flags.containsKey(CommandFlags.BANNED_ITEMS))
        {
            String state = flags.get(CommandFlags.BANNED_ITEMS);
            flags.remove(CommandFlags.BANNED_ITEMS);
            this.processBlockList(state);
            region.setBannedBlocks(Arrays.asList(this.processBlockList(state)));
        }
        for (Entry<CommandFlags, String> flag : flags.entrySet())
        {
            switch (flag.getKey())
            {
            case BLOCK_DROP_ALLOWED:
                region.setBlockDropAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case BLOCK_GROWTH_ALLOWED:
                region.setBlockGrowthAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case BLOCK_SPREAD_ALLOWED:
                region.setBlockSpreadAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case CACTUS_DAMMAGE_ALLOWED:
                region.setCactusDamageAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case CREEPER_EXPLOSION_ALLOWED:
                region.setCreeperExplosionAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case DRAGON_EGG_MOVEMENT_ALLOWED:
                region.setDragonEggMovementAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case DROWNING_DAMMAGE_ALLOWED:
                region.setDrowningDamageAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case ENCHANTING_ALLOWED:
                region.setEnchantingAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case EXPLOSIVE_DAMMAGE_ALLOWED:
                region.setExplosiveDamageAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case FALL_DAMMAGE_ALLOWED:
                region.setFallDamageAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case FIRETICK_DAMMAGE_ALLOWED:
                region.setFireTickDamageAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case FIRE_DAMMAGE_ALLOWED:
                region.setFireDamageAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case FIRE_SPREAD_ALLOWED:
                region.setFireSpreadAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case FOOD_CHANGE_ALLOWED:
                region.setFoodChangeAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case HUNGER_DAMMAGE_ALLOWED:
                region.setHungerDamageAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case ICE_FORMATION_ALLOWED:
                region.setIceFormationAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case ICE_MELTING_ALLOWED:
                region.setIceMeltingAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case LAVA_DAMMAGE_ALLOWED:
                region.setLavaDamageAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case LAVA_FLOW_ALLOWED:
                region.setLavaFlowAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case LEAF_DECAY_ALLOWED:
                region.setLeafDecayAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case LIGHTNING_DAMMAGE_ALLOWED:
                region.setLightningDamageAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case MAGIC_DAMMAGE_ALLOWED:
                region.setMagicDamageAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case POISON_DAMMAGE_ALLOWED:
                region.setPoisonDamageAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case PROJECTILE_DAMMAGE_ALLOWED:
                region.setProjectileDamageAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case PVP_DAMMAGE_ALLOWED:
                region.setPvpDamageAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case SNOW_FORMATION_ALLOWED:
                region.setDrowningDamageAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case SNOW_MELTING_ALLOWED:
                region.setSnowMeltingAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case SUFFOCATION_DAMMAGE_ALLOWED:
                region.setSuffocationDamageAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case TNT_BREAKING_PAINTINGS_ALLOWED:
                region.setTntBreakingPaintingsAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case TNT_DAMMAGE_ALLOWED:
                region.setTntDamageAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case VOID_DAMMAGE_ALLOWED:
                region.setVoidDamageAllowed(this.parseCommandBool(flag.getValue()));
                break;
            case WATER_FLOW_ALLOWED:
                region.setWaterFlowAllowed(this.parseCommandBool(flag.getValue()));
                break;
            default:
                break;
            }
        }
    }
    private Integer[] processBlockList(final String list)
    {
        final String[] cleanList = list.replaceAll("[", "").replaceAll("]", "").split(",");
        final Integer[] bannedIds = new Integer[cleanList.length];
        try
        {
            for (int i = 0; i < cleanList.length; i++)
            {
                bannedIds[i] = Integer.parseInt(cleanList[i]);
            }
        }
        catch (NumberFormatException e)
        {
            
        }
        return bannedIds;
    }

    private boolean parseCommandBool(String str)
    {
        if (str.startsWith("t"))
        {
            return true;
        }
        if (str.startsWith("y"))
        {
            return true;
        }
        return false;
    }
}
