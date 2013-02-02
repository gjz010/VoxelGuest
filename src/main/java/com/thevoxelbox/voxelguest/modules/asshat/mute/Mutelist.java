package com.thevoxelbox.voxelguest.modules.asshat.mute;

import com.google.common.base.Preconditions;
import com.thevoxelbox.voxelguest.persistence.Persistence;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Monofraps
 */
public class Mutelist
{
	private List<MutedPlayer> mutedPlayers = new ArrayList<>();

	/**
	 * Mutes a player and stores the reason he was muted for.
	 * @param playerName The name of the player to mute.
	 * @param banReason The reason the player is muted for.
	 */
	public final void mute(final String playerName, final String banReason)
	{
		Preconditions.checkState(!isPlayerMuted(playerName), "Player %s already muted.", playerName);
		mutedPlayers.add(new MutedPlayer(playerName, banReason));
	}

	/**
	 * Unmutes a player.
	 * @param playerName The name of the player to unmute.
	 */
	public final void unmute(final String playerName)
	{
		Preconditions.checkState(isPlayerMuted(playerName), "Player %s is not muted.", playerName);
		mutedPlayers.remove(getMutedPlayer(playerName));
	}

	private MutedPlayer getMutedPlayer(final String playerName)
	{
		for (MutedPlayer player : mutedPlayers)
		{
			if (player.getPlayerName().equalsIgnoreCase(playerName))
			{
				return player;
			}
		}

		return null;
	}

	/**
	 * Checks if a player is muted.
	 * @param playerName The name of the player to check.
	 * @return Returns true if the player is muted, otherwise false.
	 */
	public final boolean isPlayerMuted(final String playerName)
	{
		return getMutedPlayer(playerName) != null;
	}

	/**
	 * Gets the reason a player is muted for.
	 * @param playerName The name of the player.
	 * @return Returns the reason a player is banned for.
	 */
	public final String whyIsPlayerMuted(final String playerName)
	{
		Preconditions.checkState(isPlayerMuted(playerName), "Player %s must be muted in order to get the mute reason.", playerName);

		return getMutedPlayer(playerName).getMuteReason();
	}

	/**
	 * Loads the muted players from the persistence system.
	 */
	public final void load()
	{
		mutedPlayers.clear();

		List<Object> protoList = Persistence.getInstance().loadAll(MutedPlayer.class);
		for (Object protoPlayer : protoList)
		{
			mutedPlayers.add((MutedPlayer) protoPlayer);
		}
	}

	/**
	 * Saves the muted player to the persistence system.
	 */
	public final void save()
	{
		List<Object> protoList = new ArrayList<>();
		for (MutedPlayer protoPlayer : mutedPlayers)
		{
			protoList.add(protoPlayer);
		}

		Persistence.getInstance().saveAll(protoList);
	}
}
