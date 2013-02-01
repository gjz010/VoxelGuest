package com.thevoxelbox.voxelguest.modules.asshat.ban;

import com.google.common.base.Preconditions;
import com.thevoxelbox.voxelguest.persistence.Persistence;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Monofraps
 */
public class Banlist
{
	private List<BannedPlayer> bannedPlayers = new ArrayList<>();

	/**
	 * Bans the player named playerName and stores the ban reason.
	 * @param playerName The name of the player to ban.
	 * @param banReason The reason the player is banned for.
	 */
	public final void ban(final String playerName, final String banReason)
	{
		Preconditions.checkState(!isPlayerBanned(playerName), "Player %s already banned.", playerName);
		bannedPlayers.add(new BannedPlayer(playerName, banReason));
	}

	/**
	 * Unbans the player named playerName.
	 * @param playerName The name of the player to unban.
	 */
	public final void unban(final String playerName)
	{
		Preconditions.checkState(isPlayerBanned(playerName), "Player %s is not banned.", playerName);
		bannedPlayers.remove(getBannedPlayer(playerName));
	}

	private BannedPlayer getBannedPlayer(final String playerName)
	{
		for (BannedPlayer player : bannedPlayers)
		{
			if (player.getPlayerName().equalsIgnoreCase(playerName))
			{
				return player;
			}
		}

		return null;
	}

	/**
	 * Checks if a player is banned.
	 * @param playerName The name of the player to check.
	 * @return Returns true if the player is banned, otherwise false.
	 */
	public final boolean isPlayerBanned(final String playerName)
	{
		return getBannedPlayer(playerName) != null;

	}

	/**
	 * Gets the reason why a player is banned.
	 * @param playerName The name of the player to check.
	 * @return Returns the reason the player is banned for.
	 */
	public final String whyIsPlayerBanned(final String playerName)
	{
		Preconditions.checkState(isPlayerBanned(playerName), "Player %s must be banned in order to get the ban reason.", playerName);

		return getBannedPlayer(playerName).getBanReason();
	}

	/**
	 * Loads banned players list from persistence system.
	 */
	public final void load()
	{
		bannedPlayers.clear();

		List<Object> protoList = Persistence.getInstance().loadAll(BannedPlayer.class);
		for (Object protoPlayer : protoList)
		{
			bannedPlayers.add((BannedPlayer) protoPlayer);
		}
	}

	/**
	 * Saves banned players list to persistence system.
	 */
	public final void save()
	{
		List<Object> protoList = new ArrayList<>();
		for (BannedPlayer protoPlayer : bannedPlayers)
		{
			protoList.add(protoPlayer);
		}

		Persistence.getInstance().saveAll(protoList);
	}
}
