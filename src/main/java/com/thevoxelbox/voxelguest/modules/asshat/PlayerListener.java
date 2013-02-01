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
	private static final String SILENCE_BYPASS_PERM = "voxelguest.asshat.bypass.silence";
	private static final String FREEZE_BYPASS_PERM = "voxelguest.asshat.bypass.freeze";

	private AsshatModule module;

	public PlayerListener(AsshatModule module)
	{
		this.module = module;
	}

	@EventHandler
	public void onChatEvent(AsyncPlayerChatEvent event)
	{
		final Player player = event.getPlayer();

		if (module.isPlayerMuted(player.getName()))
		{
			event.setCancelled(true);

			player.sendMessage("You are muted for: ");
			player.sendMessage(module.whyIsPlayerMuted(player.getName()));
		}

		if (module.isSilenceEnabled())
		{
			if (!player.hasPermission(SILENCE_BYPASS_PERM))
			{
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event)
	{
		final Player player = event.getPlayer();

		if (module.isPlayerBanned(player.getName()))
		{
			event.disallow(PlayerLoginEvent.Result.KICK_BANNED, module.whyIsPlayerBanned(player.getName()));
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		final Player player = event.getPlayer();

		if (module.isFreezeEnabled())
		{
			if (!player.hasPermission(FREEZE_BYPASS_PERM))
			{
				event.setCancelled(true);
			}
		}
	}
}
