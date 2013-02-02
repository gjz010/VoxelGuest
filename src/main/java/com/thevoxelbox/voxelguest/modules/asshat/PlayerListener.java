package com.thevoxelbox.voxelguest.modules.asshat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * @author Monofraps
 */
public class PlayerListener implements Listener
{
	private AsshatModule module;

	/**
	 *
	 * @param module The parent module.
	 */
	public PlayerListener(final AsshatModule module)
	{
		this.module = module;
	}

	/**
	 * Handles muted players and global silence.
	 * @param event
	 */
	@EventHandler
	public final void onChatEvent(final AsyncPlayerChatEvent event)
	{
		final Player player = event.getPlayer();

		if (module.getMutelist().isPlayerMuted(player.getName()))
		{
			event.setCancelled(true);

			player.sendMessage("You are muted for: ");
			player.sendMessage(module.getMutelist().whyIsPlayerMuted(player.getName()));
		}

		if (module.isSilenceEnabled())
		{
			if (!player.hasPermission(AsshatModule.SILENCE_BYPASS_PERM))
			{
				event.setCancelled(true);
			}
		}
	}

	/**
	 * Handles banned players.
	 * @param event
	 */
	@EventHandler
	public final void onPlayerLogin(final PlayerLoginEvent event)
	{
		final Player player = event.getPlayer();

		if (module.getBanlist().isPlayerBanned(player.getName()))
		{
			event.disallow(PlayerLoginEvent.Result.KICK_BANNED, module.getBanlist().whyIsPlayerBanned(player.getName()));
		}
	}

	/**
	 * Handles global freeze.
	 * @param event
	 */
	@EventHandler
	public final void onPlayerMove(final PlayerMoveEvent event)
	{
		final Player player = event.getPlayer();

		if (module.isFreezeEnabled())
		{
			if (!player.hasPermission(AsshatModule.FREEZE_BYPASS_PERM))
			{
				event.setCancelled(true);
			}
		}
	}
}
