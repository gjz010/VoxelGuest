package com.thevoxelbox.voxelguest;

import com.patrickanker.lib.commands.Command;
import com.patrickanker.lib.commands.CommandPermission;
import com.patrickanker.lib.permissions.PermissionsManager;
import com.thevoxelbox.voxelguest.modules.BukkitEventWrapper;
import com.thevoxelbox.voxelguest.modules.MetaData;
import com.thevoxelbox.voxelguest.modules.Module;
import com.thevoxelbox.voxelguest.modules.ModuleConfiguration;
import com.thevoxelbox.voxelguest.modules.ModuleEvent;
import com.thevoxelbox.voxelguest.modules.Setting;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.painting.PaintingBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * The World Protection Module was created to help maintain various server
 * aspects, such as grief prevention. The World Protection Module offers extreme
 * amounts of customization based on config variables.
 * <br/>
 * Handles: Block Drops Leaf Decay Ice Melting/Forming Snow Melting/Forming All
 * Fire/Explosion Events Enchanting(allow or disallow) Vehicle Damage Vehicle
 * Creation Weather Controls Portal Creation
 */
@MetaData(name = "World Protection", description = "Various world protection methods.")
public class WorldProtectionModule extends Module
{
	private HashSet<Integer> bannedblocks = new HashSet<Integer>();
	private HashSet<Integer> banneditems = new HashSet<Integer>();
	private List<EntityPurgeThread> purgeThreads = new ArrayList<EntityPurgeThread>();
	private List<String> protectedWorlds = new ArrayList<String>();

	/**
	 *
	 */
	public WorldProtectionModule()
	{
		super(WorldProtectionModule.class.getAnnotation(MetaData.class));
	}

	@Override
	public final void enable()
	{
		setConfiguration(new WorldProtectionConfiguration(this));
		bannedblocks.clear();
		banneditems.clear();

		try
		{
			final String[] st1 = getConfiguration().getString("unplacable-blocks").split(",");

			if (st1 != null)
			{
				for (String str : st1)
				{
					bannedblocks.add(Integer.parseInt(str));
				}
			}
		} catch (Exception ex)
		{
			VoxelGuest.log("Ignoring block blacklist");
		}

		try
		{
			final String[] st2 = getConfiguration().getString("unusable-items").split(",");

			if (st2 != null)
			{
				for (String str : st2)
				{
					banneditems.add(Integer.parseInt(str));
				}
			}
		} catch (Exception ex)
		{
			VoxelGuest.log("Ignoring item blacklist");
		}

		if (getConfiguration().getBoolean("enable-multi-worlds"))
		{
			Collections.addAll(protectedWorlds, getConfiguration().getString("protected-worlds").split(","));
		}
	}

	@Override
	public final String getLoadMessage()
	{
		return "World Protection has been loaded.";
	}

	@Override
	public final void disable()
	{
		if (!purgeThreads.isEmpty())
		{
			for (EntityPurgeThread thread : purgeThreads)
			{
				thread.interrupt();
			}
		}
	}

	/**
	 *
	 * @param cs The command sender.
	 * @param args The command arguments.
	 */
	@Command(aliases = {"entitypurge", "ep"},
			bounds = {1, 1},
			help = "Purge all non-players and non-paintings from worlds using\n"
					+ "§c/entitypurge [world]")
	@CommandPermission("voxelguest.protection.entitypurge")
	public final void entityPurge(final CommandSender cs, final String[] args)
	{
		final World world = Bukkit.getWorld(args[0]);

		if (world == null)
		{
			cs.sendMessage("§cNo world found by that name. Did you spell the name correctly?");
			return;
		}

		new EntityPurgeThread(world, cs).start();
	}

	private boolean isProtectedWorld(final World world)
	{
		return protectedWorlds.isEmpty() || protectedWorlds.contains(world.getName());
	}

	/**
	 * World Protection - BlockBreak Event Written by: Razorcane
	 * <br/>
	 * Handles Block Drops.
	 */
	@ModuleEvent(event = BlockBreakEvent.class)
	public final void onBlockBreak(final BukkitEventWrapper wrapper)
	{
		final BlockBreakEvent event = (BlockBreakEvent) wrapper.getEvent();
		final Player p = event.getPlayer();
		final Block b = event.getBlock();

		if (!isProtectedWorld(b.getWorld()))
		{
			return;
		}

		if (getConfiguration().getBoolean("disable-block-drops"))
		{
			b.setType(Material.AIR);
			event.setCancelled(true);
		}

		if (!bannedblocks.isEmpty() && bannedblocks.contains(b.getTypeId()) && !PermissionsManager.getHandler().hasPermission(p.getName(), "voxelguest.protection.bannedblocks"))
		{
			event.getPlayer().sendMessage(ChatColor.RED + "YOU CANNOT BREAK THIS.");
			event.setCancelled(true);
		}
	}

	/**
	 * World Protection - BlockPlace Event Written by: Razorcane
	 * <br/>
	 * Handles prevention of certain blocks from being placed.
	 */
	@ModuleEvent(event = BlockPlaceEvent.class)
	public final void onBlockPlace(final BukkitEventWrapper wrapper)
	{
		final BlockPlaceEvent event = (BlockPlaceEvent) wrapper.getEvent();
		final Player player = event.getPlayer();
		final Block b = event.getBlock();

		if (!isProtectedWorld(b.getWorld()))
		{
			return;
		}

		if (!bannedblocks.isEmpty() && bannedblocks.contains(b.getTypeId()) && !PermissionsManager.getHandler().hasPermission(player.getName(), "voxelguest.protection.bannedblocks"))
		{
			event.getPlayer().sendMessage(ChatColor.RED + "YOU CANNOT PLACE THIS.");
			event.setCancelled(true);
		}
	}

	/**
	 * World Protection - PlayerInteract Event Written by: Razorcane
	 * <br/>
	 * Handles the prevention of using restricted items.
	 */
	@ModuleEvent(event = PlayerInteractEvent.class)
	public final void onPlayerInteract(final BukkitEventWrapper wrapper)
	{
		final PlayerInteractEvent event = (PlayerInteractEvent) wrapper.getEvent();
		final Player player = event.getPlayer();
		final ItemStack is = event.getItem();

		if (!isProtectedWorld(player.getWorld()))
		{
			return;
		}

		if (is != null && !banneditems.isEmpty() && banneditems.contains(is.getTypeId()) && !PermissionsManager.getHandler().hasPermission(player.getName(), "voxelguest.protection.banneditems"))
		{
			event.getPlayer().sendMessage(ChatColor.RED + "YOU CANNOT USE THIS.");
			event.setCancelled(true);
		}
	}

	/**
	 * World Protection - LeavesDecay Event Written by: Razorcane
	 * <br/>
	 * Handles leaf decay, obviously.
	 */
	@ModuleEvent(event = LeavesDecayEvent.class, ignoreCancelledEvents = true)
	public final void onLeavesDecay(final BukkitEventWrapper wrapper)
	{
		final LeavesDecayEvent event = (LeavesDecayEvent) wrapper.getEvent();

		if (!isProtectedWorld(event.getBlock().getWorld()))
		{
			return;
		}

		if (getConfiguration().getBoolean("disable-leaf-decay"))
		{
			event.setCancelled(true);
		}
	}

	/**
	 * World Protection - BlockGrow Event Written by: Billyyjoee
	 * <br/>
	 * Handles Block Growth such as;
	 * Wheat,
	 * Pumpkins,
	 * Sugar Cane,
	 * Watermelons &
	 * Cactus
	 */
	@ModuleEvent(event = BlockGrowEvent.class, ignoreCancelledEvents = true)
	public final void onBlockGrow(final BukkitEventWrapper wrapper)
	{
		final BlockGrowEvent event = (BlockGrowEvent) wrapper.getEvent();

		if (!isProtectedWorld(event.getBlock().getWorld()))
		{
			return;
		}

		if (getConfiguration().getBoolean("disable-block-growth"))
		{
			event.setCancelled(true);
		}
	}

	/**
	 * World Protection - BlockFromTo Event Written by: Billyyjoee
	 * <br/>
	 * Handles Water/Lava/DragonEgg Movement
	 */
	@ModuleEvent(event = BlockFromToEvent.class, ignoreCancelledEvents = true)
	public final void onBlockFromTo(final BukkitEventWrapper wrapper)
	{
		final BlockFromToEvent event = (BlockFromToEvent) wrapper.getEvent();
		final Block b = event.getBlock();

		if (!isProtectedWorld(b.getWorld()))
		{
			return;
		}

		if (b.getType().equals(Material.DRAGON_EGG) && getConfiguration().getBoolean("disable-dragonegg-movement"))
		{
			event.setCancelled(true);
			return;
		}

		if (b.getType().equals(Material.STATIONARY_LAVA) && getConfiguration().getBoolean("disable-lava-flow"))
		{
			event.setCancelled(true);
			return;
		}

		if (b.getType().equals(Material.LAVA) && getConfiguration().getBoolean("disable-lava-flow"))
		{
			event.setCancelled(true);
			return;
		}
		if (b.getType().equals(Material.STATIONARY_WATER) && getConfiguration().getBoolean("disable-water-flow"))
		{
			event.setCancelled(true);
			return;
		}
		if (b.getType().equals(Material.WATER) && getConfiguration().getBoolean("disable-water-flow"))
		{
			event.setCancelled(true);
		}
	}

	/**
	 * World Protection - BlockFade Event Written by: Razorcane
	 * <br/>
	 * Handles Snow/Ice Melting
	 */
	@ModuleEvent(event = BlockFadeEvent.class, ignoreCancelledEvents = true)
	public final void onBlockFade(final BukkitEventWrapper wrapper)
	{
		final BlockFadeEvent event = (BlockFadeEvent) wrapper.getEvent();
		final Block b = event.getNewState().getBlock();

		if (!isProtectedWorld(b.getWorld()))
		{
			return;
		}

		if (b.getType().equals(Material.ICE) && getConfiguration().getBoolean("disable-ice-melting"))
		{
			event.setCancelled(true);
			return;
		}

		if (b.getType().equals(Material.SNOW) && getConfiguration().getBoolean("disable-snow-melting"))
		{
			event.setCancelled(true);
		}
	}

	/**
	 * World Protection - BlockForm Event Written by: Razorcane
	 * <br/>
	 * Handles Ice/Snow Forming
	 */
	@ModuleEvent(event = BlockFormEvent.class, ignoreCancelledEvents = true)
	public void onBlockForm(final BukkitEventWrapper wrapper)
	{
		final BlockFormEvent event = (BlockFormEvent) wrapper.getEvent();
		final Block b = event.getBlock();

		if (!isProtectedWorld(b.getWorld()))
		{
			return;
		}

		if (b.getType().equals(Material.ICE) && getConfiguration().getBoolean("disable-ice-formation"))
		{
			event.setCancelled(true);
			return;
		}

		if (b.getType().equals(Material.SNOW) && getConfiguration().getBoolean("disable-snow-formation"))
		{
			event.setCancelled(true);
		}
	}

	/**
	 * World Protection - BlockBurn Event Written by: Razorcane
	 * <br/>
	 * Handles Fire burning blocks
	 */
	@ModuleEvent(event = BlockBurnEvent.class)
	public final void onBlockBurn(final BukkitEventWrapper wrapper)
	{
		final BlockBurnEvent event = (BlockBurnEvent) wrapper.getEvent();

		if (!isProtectedWorld(event.getBlock().getWorld()))
		{
			return;
		}

		if (getConfiguration().getBoolean("disable-block-burning"))
		{
			event.setCancelled(true);
		}
	}

	/**
	 * World Protection BlockIgnite Event Written by: Razorcane
	 * <p/>
	 * Handles the ignition(fire) of blocks, whether it be from lava, lightning,
	 * or player means.
	 */
	@ModuleEvent(event = BlockIgniteEvent.class)
	public final void onBlockIgnite(final BukkitEventWrapper wrapper)
	{
		final BlockIgniteEvent event = (BlockIgniteEvent) wrapper.getEvent();
		final IgniteCause cause = event.getCause();

		if (!isProtectedWorld(event.getBlock().getWorld()))
		{
			return;
		}

		boolean fireSpread = (cause == IgniteCause.SPREAD || cause == IgniteCause.LAVA || cause == IgniteCause.LIGHTNING);

		if (fireSpread && getConfiguration().getBoolean("disable-block-ignite"))
		{
			event.setCancelled(true);
		}
	}

	/**
	 * World Protection - BlockSpread Event Written by: Razorcane
	 * <p/>
	 * Handles Fire Spread.
	 */
	@ModuleEvent(event = BlockSpreadEvent.class)
	public final void onBlockSpread(final BukkitEventWrapper wrapper)
	{
		final BlockSpreadEvent event = (BlockSpreadEvent) wrapper.getEvent();
		final boolean fireSpread = (event.getNewState().getType() == Material.FIRE);

		if (!isProtectedWorld(event.getBlock().getWorld()))
		{
			return;
		}

		if (fireSpread && getConfiguration().getBoolean("disable-fire-spread"))
		{
			event.setCancelled(true);
		}
	}

	/**
	 * World Protection - EnchantItem Event Written by: Razorcane
	 * <p/>
	 * Handles Item Enchanting, obviously.
	 */
	@ModuleEvent(event = EnchantItemEvent.class)
	public final void onEnchantItem(final BukkitEventWrapper wrapper)
	{
		final EnchantItemEvent event = (EnchantItemEvent) wrapper.getEvent();

		if (!isProtectedWorld(event.getEnchanter().getWorld()))
		{
			return;
		}

		if (getConfiguration().getBoolean("disable-enchanting"))
		{
			event.setCancelled(true);
		}
	}

	/**
	 * World Protection - EntityExplode Event Written by: Razorcane
	 * <br/>
	 * Handles mob explosions, such as creepers.
	 */
	@ModuleEvent(event = EntityExplodeEvent.class)
	public final void onEntityExplode(final BukkitEventWrapper wrapper)
	{
		final EntityExplodeEvent event = (EntityExplodeEvent) wrapper.getEvent();

		if (!isProtectedWorld(event.getEntity().getWorld()))
		{
			return;
		}

		if (getConfiguration().getBoolean("disable-creeper-explosion"))
		{
			event.setCancelled(true);
		}
	}

	/**
	 * World Protection - PaintingBreak Event Written by: Billyyjoee
	 * <p/>
	 * Handlesh how a painting is broken
	 *
	 * @deprecated
	 */
	@ModuleEvent(event = PaintingBreakEvent.class)
	public final void onPaintingBreak(final BukkitEventWrapper wrapper)
	{
		final PaintingBreakEvent event = (PaintingBreakEvent) wrapper.getEvent();

		if (getConfiguration().getBoolean("disable-painting-pop"))
		{
			event.setCancelled(true);
		}
	}

	/**
	 * @param wrapper
	 *
	 * @deprecated
	 */
	@ModuleEvent(event = PaintingBreakByEntityEvent.class)
	public final void onPaintingBreakByEntity(final BukkitEventWrapper wrapper)
	{
		final PaintingBreakByEntityEvent event = (PaintingBreakByEntityEvent) wrapper.getEvent();

		if (getConfiguration().getBoolean("disable-painting-pop"))
		{
			event.setCancelled(true);
		}
	}

	private final class WorldProtectionConfiguration extends ModuleConfiguration
	{

		@Setting("enable-multi-worlds")
		public boolean multiworld = false;
		@Setting("protected-worlds")
		public String protectedWorlds = "";
		@Setting("disable-block-drops")
		public boolean blockdrops = false;
		@Setting("disable-leaf-decay")
		public boolean leafdecay = false;
		@Setting("disable-ice-melting")
		public boolean icemelt = false;
		@Setting("disable-snow-melting")
		public boolean snowmelt = false;
		@Setting("disable-ice-formation")
		public boolean iceform = false;
		@Setting("disable-snow-formation")
		public boolean snowform = false;
		@Setting("disable-block-burning")
		public boolean blockburn = false;
		@Setting("disable-block-ignite")
		public boolean blockignite = true;
		@Setting("disable-block-growth")
		public boolean blockgrow = true;
		@Setting("disable-fire-spread")
		public boolean firespred = false;
		@Setting("disable-lava-flow")
		public boolean lavaflow = false;
		@Setting("disable-water-flow")
		public boolean waterflow = false;
		@Setting("disable-enchanting")
		public boolean enchanting = false;
		@Setting("disable-painting-pop")
		public boolean paintingpop = false;
		@Setting("disable-creeper-explosion")
		public boolean creeperexplode = false;
		@Setting("disable-dragonegg-movement")
		public boolean dragoneggmovement = false;
		@Setting("unplacable-blocks")
		public String unplacable = "8,9,10,11,46";
		@Setting("unusable-items")
		public String unusableitems = "325,326,327";

		public WorldProtectionConfiguration(WorldProtectionModule parent)
		{
			super(parent);
		}
	}

	private final class EntityPurgeThread extends Thread
	{

		private final World world;
		private final CommandSender sender;

		public EntityPurgeThread(World w, CommandSender cs)
		{
			world = w;
			sender = cs;
			registerPurgeThread();
		}

		@Override
		public void run()
		{
			final List<Entity> entities = world.getEntities();

			for (Entity e : entities)
			{
				if (!((e instanceof Player) || (e instanceof Painting) || (e instanceof ItemFrame)))
				{
					e.remove();
				}
			}

			sender.sendMessage("§aEntity purge complete");

			if (purgeThreads.contains(this))
			{
				purgeThreads.remove(this);
			}
		}

		private void registerPurgeThread()
		{
			purgeThreads.add(this);
		}
	}
}
