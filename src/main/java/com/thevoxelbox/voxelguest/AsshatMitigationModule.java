/*
 * Copyright (C) 2011 - 2012, psanker and contributors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are 
 * permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice, this list of 
 *   conditions and the following 
 * * Redistributions in binary form must reproduce the above copyright notice, this list of 
 *   conditions and the following disclaimer in the documentation and/or other materials 
 *   provided with the distribution.
 * * Neither the name of The VoxelPlugineering Team nor the names of its contributors may be 
 *   used to endorse or promote products derived from this software without specific prior 
 *   written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS 
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE 
 * COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR 
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.thevoxelbox.voxelguest;

import com.patrickanker.lib.commands.Command;
import com.patrickanker.lib.commands.CommandPermission;
import com.patrickanker.lib.config.PropertyConfiguration;
import com.patrickanker.lib.permissions.PermissionsManager;
import com.patrickanker.lib.util.Formatter;
import com.thevoxelbox.voxelguest.modules.BukkitEventWrapper;
import com.thevoxelbox.voxelguest.modules.MetaData;
import com.thevoxelbox.voxelguest.modules.Module;
import com.thevoxelbox.voxelguest.modules.ModuleConfiguration;
import com.thevoxelbox.voxelguest.modules.ModuleEvent;
import com.thevoxelbox.voxelguest.modules.Setting;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author Razorcane
 * @author Monofraps (cleanup and fixes)
 */
@MetaData(name = "Asshat Mitigator", description = "Major asshat handling.")
public class AsshatMitigationModule extends Module
{

	private final PropertyConfiguration bannedList = new PropertyConfiguration("banned", "/VoxelGuest/asshatmitigation");
	private final List<String> gagged = new ArrayList<String>();
	private final List<String> frozen = new ArrayList<String>();
	private boolean allFreeze = false;
	private boolean silenceMode = false;

	/**
	 *
	 */
	public AsshatMitigationModule()
	{
		super(AsshatMitigationModule.class.getAnnotation(MetaData.class));
	}

	@SuppressWarnings("unchecked")
	@Override
	public final void enable()
	{
		setConfiguration(new AsshatMitigationConfiguration(this));

		bannedList.load();
		// update ban list
		final HashMap<String, Object> updatedMap = new HashMap<String, Object>();
		for (final String name : bannedList.getAllEntries().keySet())
		{
			if (updatedMap.containsKey(name.toLowerCase()))
			{
				continue;
			}

			updatedMap.put(name.toLowerCase(), bannedList.getEntry(name));
		}
		bannedList.clear();
		for (final String name : updatedMap.keySet())
		{
			bannedList.setString(name, (String) updatedMap.get(name));
		}
		bannedList.save();

		gagged.clear();
	}

	@Override
	public final String getLoadMessage()
	{
		return "Asshat Mitigator has been loaded.";
	}

	@Override
	public final void disable()
	{
		bannedList.save();
	}

	/**
	 * Asshat Mitigation - Ban Written by: Razorcane
	 * <p/>
	 * Handles the banning of both online and offline players. However, exact
	 * player names must be given when banning offline players.
	 */
	@Command(aliases = {"ban", "vban", "vbano", "bano"}, bounds = {1, -1}, help = "To ban someone, simply type\n" + "§c/ban [player] (reason)")
	@CommandPermission("voxelguest.asshat.ban")
	public final void ban(final CommandSender cs, final String[] args)
	{
		String playerName = args[0];
		String reason = "";
		boolean silent = false;
		boolean forceExact = false;

		if (args.length > 1)
		{
			for (short i = 1; i < args.length; ++i)
			{
				String arg = args[i];

				if (arg.equalsIgnoreCase("-silent") || arg.equalsIgnoreCase("-si"))
				{
					silent = true;
				}
				else if(arg.equals("-exact") || arg.equalsIgnoreCase("-e")) {
					forceExact = true;
				}
				else
				{
					reason += args[i] + " ";
				}
			}
		}

		final List<String> players = getOnlineOfflinePlayersName(playerName, true, forceExact);

		if (players.size() > 1)
		{
			cs.sendMessage(ChatColor.RED + "Partial match:");
			String nameList = "";
			for (final String name : players)
			{
				nameList += name + " | ";
			}
			cs.sendMessage(nameList);
			return;
		}

		playerName = players.get(0);

		if (reason.isEmpty())
		{
			reason = getConfiguration().getString("default-asshat-reason");
		}

		banPlayer(playerName, reason);

		if (silent)
		{
			Bukkit.getLogger().info(String.format("Player %s has been banned by %s for: %s", playerName, cs.getName(), reason));
		}
		else
		{
			Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "Player " + ChatColor.RED + playerName + ChatColor.DARK_GRAY + " has been banned by "
					+ ChatColor.RED + cs.getName() + ChatColor.DARK_GRAY + " for:");
			Bukkit.broadcastMessage(ChatColor.BLUE + reason);
		}
	}

	/**
	 * Asshat Mitigation - Unban Written by: Razorcane
	 * <p/>
	 * Controls the unbanning of banned players. Name must be exact, and player
	 * must be banned, in order to be unbanned.
	 */
	@Command(aliases = {"unban", "vunban"}, bounds = {1, -1}, help = "To unban someone, simply type\n" + "§c/unban [player]")
	@CommandPermission("voxelguest.asshat.unban")
	public final void unban(final CommandSender cs, final String[] args)
	{
		boolean silent = false;

		for (final String arg : args)
		{
			if (arg.equalsIgnoreCase("-silent") || arg.equalsIgnoreCase("-si"))
			{
				silent = true;
			}
		}

		final String playerName = args[0];
		if (isPlayerBanned(playerName))
		{
			unbanPlayer(playerName);

			if (silent)
			{
				Bukkit.getLogger().info(String.format("Player %s has been unbanned by %s", playerName, cs.getName()));
			}
			else
			{
				Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "Player " + ChatColor.RED + playerName + ChatColor.DARK_GRAY + " has been unbanned by "
						+ ChatColor.RED + cs.getName());
			}
		}
		else
		{
			cs.sendMessage(ChatColor.RED + "Player isn't banned.");
		}
	}

	/**
	 * Asshat Mitigation - Gag Written by: Razorcane
	 * <p/>
	 * Gags a player, or prevents them from talking until they are ungagged,
	 * there is a server restart, or they type the designated phrase.
	 */

	@Command(aliases = {"gag", "vgag"}, bounds = {1, -1}, help = "To gag someone, simply type\n" + "§c/gag [player] (reason)", playerOnly = false)
	@CommandPermission("voxelguest.asshat.gag")
	public final void gag(final CommandSender cs, final String[] args)
	{
		String playerName = args[0];
		final List<String> players = getOnlineOfflinePlayersName(playerName, false);


		String reason = "";
		boolean silent = false;

		if (players.isEmpty())
		{
			cs.sendMessage(ChatColor.RED + "No player with the name " + playerName + " found.");
			return;
		}

		if (players.size() > 1)
		{
			cs.sendMessage(ChatColor.RED + "Partial match:");
			String nameList = "";
			for (final String name : players)
			{
				nameList += name + " | ";
			}
			cs.sendMessage(nameList);
			return;
		}

		if (args.length > 1)
		{
			for (int i = 1; i < args.length; i++)
			{
				final String arg = args[i];

				if (arg.equalsIgnoreCase("-silent") || arg.equalsIgnoreCase("-si"))
				{
					silent = true;
				}
				else
				{
					reason += args[i] + " ";
				}
			}
		}

		if (reason.isEmpty())
		{
			reason = getConfiguration().getString("default-asshat-reason");
		}

		playerName = players.get(0);

		if (gagged.contains(playerName))
		{
			gagged.remove(playerName);
			cs.sendMessage(ChatColor.RED + playerName + ChatColor.WHITE + " has been ungagged.");
		}
		else
		{
			gagged.add(playerName);
			if (silent)
			{
				Bukkit.getLogger().info(String.format("Player %s has been gagged by %s for: %s", playerName, cs.getName(), reason));
			}
			else
			{
				Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "Player " + ChatColor.RED + playerName + ChatColor.DARK_GRAY + " has been gagged by "
						+ ChatColor.RED + cs.getName() + ChatColor.DARK_GRAY + " for:");
				Bukkit.broadcastMessage(ChatColor.BLUE + reason);
			}
		}

	}

	/**
	 * Freezes a/all player/players.
	 *
	 * @param cs   The command sender
	 * @param args The command arguments
	 */
	@Command(aliases = {"freeze", "fr"}, bounds = {1, 1}, help = "Freezes the defined player in\n"
			+ "§c/freeze [player]§f or freeze all players (except those with \"voxelguest.asshat.freeze.bypass\") with\n" + "§c/freeze --all§f or §c/freeze -a")
	@CommandPermission("voxelguest.asshat.freeze.freeze")
	public final void freeze(final CommandSender cs, final String[] args)
	{
		if (args[0].equalsIgnoreCase("--all") || args[0].equalsIgnoreCase("-a"))
		{
			allFreeze = !allFreeze;

			if (allFreeze)
			{
				for (final Player p : Bukkit.getOnlinePlayers())
				{
					if (!PermissionsManager.getHandler().hasPermission(p.getName(), "voxelguest.asshat.freeze.bypass"))
					{
						frozen.add(p.getName());
					}
				}

				cs.sendMessage("§aEveryone §7has been §bfrozen.");
			}
			else
			{
				frozen.clear();
				cs.sendMessage("§aEveryone §7has been §cthawed.");
			}

			return;
		}

		final String playerName = args[0];
		final List<Player> players = Bukkit.matchPlayer(playerName);

		if (players.isEmpty())
		{
			cs.sendMessage(ChatColor.RED + "No player found with that name.");
		}
		else if (players.size() > 1)
		{
			cs.sendMessage(ChatColor.RED + "Partial match:");
			String nameList = "";
			for (final Player p : players)
			{
				nameList += p.getName() + " | ";
			}
			cs.sendMessage(nameList);
		}
		else
		{
			if (frozen.contains(playerName))
			{
				frozen.remove(playerName);
				cs.sendMessage("§a" + playerName + " §7has been §cthawed.");
			}
			else
			{
				frozen.add(playerName);
				cs.sendMessage("§a" + playerName + " §7has been §bfrozen.");
			}
		}
	}

	/**
	 * Asshat Mitigation - Kick Written by: Razorcane
	 * <p/>
	 * Kicks a player from the server. Entering no reason defaults to the
	 * default asshat reason, which is "Asshat".
	 */

	@Command(aliases = {"kick", "vkick"}, bounds = {1, -1}, help = "To kick someone, simply type\n" + "§c/kick [player] (reason)", playerOnly = false)
	@CommandPermission("voxelguest.asshat.kick")
	public final void kick(final CommandSender cs, final String[] args)
	{
		String playerName = args[0];
		final List<String> players = getOnlineOfflinePlayersName(playerName, false);

		String reason = "";
		boolean silent = false;

		if (players.isEmpty())
		{
			cs.sendMessage(ChatColor.RED + "No player with the name " + playerName + " found.");
			return;
		}

		if (players.size() > 1)
		{
			cs.sendMessage(ChatColor.RED + "Partial match:");
			String nameList = "";
			for (final String name : players)
			{
				nameList += name + " | ";
			}
			cs.sendMessage(nameList);
			return;
		}

		if (args.length > 1)
		{
			for (int i = 1; i < args.length; i++)
			{
				String arg = args[i];

				if (arg.equalsIgnoreCase("-silent") || arg.equalsIgnoreCase("-si"))
				{
					silent = true;
				}
				else
				{
					reason += args[i] + " ";
				}
			}
		}

		if (reason.isEmpty())
		{
			reason = getConfiguration().getString("default-asshat-reason");
		}

		playerName = players.get(0);

		Bukkit.getPlayerExact(playerName).kickPlayer(reason);
		if (silent)
		{
			Bukkit.getLogger().info(String.format("Player %s has been kicked by %s for: %s", playerName, cs.getName(), reason));
		}
		else
		{
			Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "Player " + ChatColor.RED + playerName + ChatColor.DARK_GRAY
					+ " has been kicked by " + ChatColor.RED + cs.getName() + ChatColor.DARK_GRAY + " for:");
			Bukkit.broadcastMessage(ChatColor.BLUE + reason);
		}		

	}

	/**
	 * Mutes all players without bypass permission.
	 *
	 * @param cs   The command sender
	 * @param args The command arguments
	 */
	@Command(aliases = {"soapbox", "silence"}, bounds = {0, 0}, help = "Toggle the silence")
	@CommandPermission("voxelguest.admin.silence")
	public final void silence(final CommandSender cs, final String[] args)
	{
		silenceMode = !silenceMode;
		getConfiguration().setBoolean("silence-mode", silenceMode);
		cs.sendMessage(ChatColor.GOLD + "Silent mode has been " + ((silenceMode) ? "enabled" : "disabled"));
	}

	/**
	 * @param wrapper -
	 *
	 * @deprecated
	 */
	@ModuleEvent(event = PlayerPreLoginEvent.class, ignoreCancelledEvents = false)
	public final void onPlayerPreLogin(final BukkitEventWrapper wrapper)
	{
		final PlayerPreLoginEvent event = (PlayerPreLoginEvent) wrapper.getEvent();
		final String playerName = event.getName();

		if (isPlayerBanned(playerName))
		{
			event.setResult(PlayerPreLoginEvent.Result.KICK_FULL);
			event.disallow(PlayerPreLoginEvent.Result.KICK_FULL, "You are banned for: " + getBanReason(playerName));
		}
	}

	/**
	 * @param wrapper -
	 */
	@ModuleEvent(event = AsyncPlayerChatEvent.class, ignoreCancelledEvents = false)
	public final void onPlayerChat(final BukkitEventWrapper wrapper)
	{
		final AsyncPlayerChatEvent event = (AsyncPlayerChatEvent) wrapper.getEvent();
		final Player player = event.getPlayer();

		if (silenceMode)
		{
			if (!PermissionsManager.getHandler().hasPermission(event.getPlayer().getName(), "voxelguest.bypass.silence"))
			{
				event.setCancelled(true);
			}
		}

		if (gagged.contains(player.getName()))
		{
			if (event.getMessage().equals(getConfiguration().getString("unrestrict-chat-message")))
			{
				gagged.remove(player.getName());

				for (String str : Formatter.selectFormatter(SimpleFormatter.class).formatMessages(getConfiguration().getString("ungag-message-format"),
						VoxelGuest.getGuestPlayer(player)))
				{
					player.sendMessage(str);
				}

				event.setCancelled(true);
			}
			else
			{
				for (String str : Formatter.selectFormatter(SimpleFormatter.class).formatMessages(getConfiguration().getString("gag-message-format"),
						VoxelGuest.getGuestPlayer(player)))
				{
					player.sendMessage(str);
				}

				event.setCancelled(true);
			}
		}
	}

	/**
	 * @param wrapper -
	 */
	@ModuleEvent(event = PlayerMoveEvent.class)
	public final void onPlayerMove(final BukkitEventWrapper wrapper)
	{
		final PlayerMoveEvent event = (PlayerMoveEvent) wrapper.getEvent();

		if (frozen.contains(event.getPlayer().getName()))
		{
			final Location orig = event.getFrom();
			final Location dest = event.getTo();

			// only cancel if the player actually walks; do not cancel rotation
			if ((orig.getBlockX() != dest.getBlockX()) || (orig.getBlockY() != dest.getBlockY()) || (orig.getBlockZ() != dest.getBlockZ()))
			{
				event.setTo(orig);
				event.setCancelled(true);
			}
		}
	}

	/**
	 * @param wrapper -
	 */
	@ModuleEvent(event = PlayerTeleportEvent.class)
	public final void onPlayerTeleport(final BukkitEventWrapper wrapper)
	{
		final PlayerTeleportEvent event = (PlayerTeleportEvent) wrapper.getEvent();

		if (frozen.contains(event.getPlayer().getName()))
		{
			event.setTo(event.getFrom());
			event.setCancelled(true);
		}
	}

	private void banPlayer(final String playerName, final String reason)
	{
		Player player = Bukkit.getPlayerExact(playerName);
		if(player != null) {
			player.kickPlayer(reason);
		}

		bannedList.setString(playerName.toLowerCase(), reason);

		if (getConfiguration().getBoolean("save-banlist-on ban"))
		{
			bannedList.save();
		}
	}

	private void unbanPlayer(final String playerName)
	{
		if (isPlayerBanned(playerName.toLowerCase()))
		{
			bannedList.removeEntry(playerName.toLowerCase());
		}

		bannedList.save();
	}

	private boolean isPlayerBanned(final String playerName)
	{
		return bannedList.hasEntry(playerName.toLowerCase());
	}

	private String getBanReason(final String playerName)
	{
		return bannedList.getString(playerName.toLowerCase());
	}

	/**
	 * Tries to find either all online players matched by [exp] or an offline player with the exact name [exp].
	 *
	 * @param exp Either the partial or exact name of a player.
	 *
	 * @return A list of possible players names. (Only 1 entry = exact match)
	 */
	private List<String> getOnlineOfflinePlayersName(final String exp, final boolean includeOffline, final boolean forceExact)
	{
		final List<Player> possibilities = Bukkit.matchPlayer(exp);
		final List<String> possibleNames = new ArrayList<String>();
		final Player exactPlayer = Bukkit.getPlayerExact(exp);

		if (exactPlayer != null)
		{
			possibleNames.add(exactPlayer.getName());
			if(!includeOffline) {
				return possibleNames;
			}
		}

		for (Player player : possibilities)
		{
			possibleNames.add(player.getName());
		}

		if ((possibleNames.isEmpty() && includeOffline) || forceExact)
		{
			if(forceExact) {
				possibleNames.clear();
			}

			possibleNames.add(exp);
		}

		return possibleNames;
	}

	private List<String> getOnlineOfflinePlayersName(final String exp, final boolean includeOffline)
	{
		return getOnlineOfflinePlayersName(exp, includeOffline, false);
	}

	class AsshatMitigationConfiguration extends ModuleConfiguration
	{

		@Setting("default-asshat-reason")
		public String defaultAsshatReason = "&cAsshat";
		@Setting("save-banlist-on-ban")
		public boolean saveBanlistOnBan = false;
		@Setting("unrestrict-chat-message")
		public String unrestrictChatMessage = "I agree. Allow me to chat.";
		@Setting("gag-message-format")
		public String gagMessageFormat = "&cYou have been gagged. You cannot chat until you say\n" + "&6the ungag key phrase.";
		@Setting("ungag-message-format")
		public String ungagMessageFormat = "&aYou have been ungagged.";

		public AsshatMitigationConfiguration(AsshatMitigationModule parent)
		{
			super(parent);
		}
	}
}
