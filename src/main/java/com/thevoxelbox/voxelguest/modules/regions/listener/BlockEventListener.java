package com.thevoxelbox.voxelguest.modules.regions.listener;

import java.util.List;

import com.google.common.base.Preconditions;
import com.thevoxelbox.voxelguest.modules.regions.Region;
import com.thevoxelbox.voxelguest.modules.regions.RegionModule;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * @author Butters
 */
public class BlockEventListener implements Listener
{

    private final String CANT_BUILD_HERE = "§cYou cannot build here";
    private RegionModule regionModule;

    public BlockEventListener(final RegionModule regionModule)
    {
        this.regionModule = regionModule;
    }

    @EventHandler(ignoreCancelled = true)
    public final void onBlockBreak(final BlockBreakEvent event)
    {
        Preconditions.checkNotNull(event.getPlayer());
        Preconditions.checkNotNull(event.getBlock());
        if (!this.regionModule.getRegionManager().canPlayerModify(event.getPlayer(), event.getBlock().getLocation()))
        {
            event.setCancelled(true);
            event.getPlayer().sendMessage(CANT_BUILD_HERE);
        }
    }

    /**
     * Prevents block drops.
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public final void onBlockDrop(final BlockBreakEvent event)
    {
        Preconditions.checkNotNull(event);
        //TODO: Define as region based
        Block selectedBlock = event.getBlock();
        if (selectedBlock != null)
        {
            event.getBlock().setType(Material.AIR);
        }
        event.setCancelled(true);
    }

    @EventHandler
    public final void onBlockPlace(final BlockPlaceEvent event)
    {
        Preconditions.checkNotNull(event.getPlayer());
        Preconditions.checkNotNull(event.getBlock());
        if (!this.regionModule.getRegionManager().canPlayerModify(event.getPlayer(), event.getBlock().getLocation()))
        {
            event.setCancelled(true);
            event.getPlayer().sendMessage(CANT_BUILD_HERE);
        }
    }

    @EventHandler
    public final void onPlayerInteract(final PlayerInteractEvent event)
    {
        if (event.getClickedBlock() == null)
        {
            return;
        }
        if (!this.regionModule.getRegionManager().canPlayerModify(event.getPlayer(), event.getClickedBlock().getLocation()))
        {
            event.setCancelled(true);
            event.getPlayer().sendMessage(CANT_BUILD_HERE);
        }
    }

    @EventHandler
    public final void onLeafDecay(final LeavesDecayEvent event)
    {
        Preconditions.checkNotNull(event.getBlock());
        final Location eventLoc = event.getBlock().getLocation();
        final Region region = this.regionModule.getRegionManager().getRegionAtLoc(eventLoc);

        if (region != null)
        {
            if (!region.isLeafDecayAllowed())
            {
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(true);
        return;
    }

    @EventHandler
    public final void onBlockGrow(final BlockGrowEvent event)
    {
        Preconditions.checkNotNull(event.getBlock());
        final Location eventLoc = event.getBlock().getLocation();
        final Region region = this.regionModule.getRegionManager().getRegionAtLoc(eventLoc);

        if (region != null)
        {
            if (!region.isBlockGrowthAllowed())
            {
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(true);
        return;
    }

    @EventHandler
    public final void onFromTo(final BlockFromToEvent event)
    {
        Preconditions.checkNotNull(event.getBlock());
        final Location eventLoc = event.getBlock().getLocation();
        final Region region = this.regionModule.getRegionManager().getRegionAtLoc(eventLoc);

        if (region != null)
        {
            if (!region.isBlockSpreadAllowed())
            {
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(true);
        return;
    }

    @EventHandler
    public final void onBlockFade(final BlockFadeEvent event)
    {
        Preconditions.checkNotNull(event.getBlock());
        final Location eventLoc = event.getBlock().getLocation();
        final Region region = this.regionModule.getRegionManager().getRegionAtLoc(eventLoc);

        if (region != null)
        {
            if (!region.isBlockSpreadAllowed())
            {
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(true);
        return;
    }

    @EventHandler
    public final void onBlockForm(final BlockFormEvent event)
    {
        Preconditions.checkNotNull(event.getBlock());
        final Location eventLoc = event.getBlock().getLocation();
        final Region region = this.regionModule.getRegionManager().getRegionAtLoc(eventLoc);

        if (region != null)
        {
            if (!region.isBlockSpreadAllowed())
            {
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(true);
        return;
    }

    @EventHandler
    public final void onBlockIgnite(final BlockIgniteEvent event)
    {
        Preconditions.checkNotNull(event.getBlock());
        final Location eventLoc = event.getBlock().getLocation();
        final Region region = this.regionModule.getRegionManager().getRegionAtLoc(eventLoc);

        if (region != null)
        {
            if (!region.isFireSpreadAllowed())
            {
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(true);
        return;
    }

    @EventHandler
    public final void onBlockSpread(final BlockSpreadEvent event)
    {
        final Location eventLoc = event.getBlock().getLocation();
        final Region region = this.regionModule.getRegionManager().getRegionAtLoc(eventLoc);

        if (region != null)
        {
            if (!region.isBlockSpreadAllowed())
            {
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(true);
        return;
    }
    
    @EventHandler
    public final void onBlockPhysics(final BlockPhysicsEvent event)
    {
        final Location eventLoc = event.getBlock().getLocation();
        final List<Region> regions = this.regionModule.getRegionManager().getRegionsAtLoc(eventLoc);
        if (regions.isEmpty())
        {
            event.setCancelled(true);
        }
        boolean isNotAllowed = false;
        for (Region region : regions)
        {
            if (!region.isPhysicsAllowed())
            {
                isNotAllowed = true;
                break;
            }
        }
        if (isNotAllowed)
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public final void onEnchant(final EnchantItemEvent event)
    {
        final Region region = this.regionModule.getRegionManager().getRegionAtLoc(event.getEnchantBlock().getLocation());

        if (region != null)
        {
            if (!region.isEnchantingAllowed())
            {
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(true);
        return;
    }

    @EventHandler
    public final void onEntityExplode(final EntityExplodeEvent event)
    {
        final Location eventLoc = event.getLocation();
        final Region region = this.regionModule.getRegionManager().getRegionAtLoc(eventLoc);

        if (region != null)
        {
            if (!region.isCreeperExplosionAllowed())
            {
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(true);
        return;
    }

    @EventHandler
    public final void onPaintingBreak(final HangingBreakByEntityEvent event)
    {
        final Location eventLoc = event.getEntity().getLocation();
        final Region region = this.regionModule.getRegionManager().getRegionAtLoc(eventLoc);

        if (region != null)
        {
            if (!region.isBuildingRestricted())
            {
                if (event.getRemover() instanceof Player)
                {
                    if (!this.regionModule.getRegionManager().canPlayerModify((Player) event.getRemover(), event.getEntity().getLocation()))
                    {
                        event.setCancelled(true);
                    }
                }
            }
            return;
        }
        event.setCancelled(true);
        return;
    }

}
