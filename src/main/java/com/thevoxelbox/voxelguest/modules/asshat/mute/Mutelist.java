package com.thevoxelbox.voxelguest.modules.asshat.mute;

import com.google.common.base.Preconditions;
import com.thevoxelbox.voxelguest.modules.asshat.ban.BannedPlayer;
import com.thevoxelbox.voxelguest.persistence.Persistence;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Monofraps
 */
public class Mutelist
{
	/**
	 * Mutes a player and stores the reason he was muted for.
	 * @param playerName The name of the player to mute.
	 * @param banReason The reason the player is muted for.
	 */
	public final void mute(final String playerName, final String banReason)
	{
		Preconditions.checkState(!isPlayerMuted(playerName), "Player %s already muted.", playerName);
		Persistence.getInstance().save(new MutedPlayer(playerName, banReason));
	}

	/**
	 * Unmutes a player.
	 * @param playerName The name of the player to unmute.
	 */
	public final void unmute(final String playerName)
	{
		Preconditions.checkState(isPlayerMuted(playerName), "Player %s is not muted.", playerName);
		Persistence.getInstance().delete(getMutedPlayer(playerName));
	}

	private MutedPlayer getMutedPlayer(final String playerName)
	{
		final List<Object> mutedPlayers = Persistence.getInstance().loadAll(MutedPlayer.class, Restrictions.like("playerName", playerName.toLowerCase()));
		for (Object mutedPlayerObject : mutedPlayers)
		{
			Preconditions.checkState(mutedPlayerObject instanceof MutedPlayer);

			MutedPlayer mutedPlayer = (MutedPlayer) mutedPlayerObject;
			if (mutedPlayer.getPlayerName().equalsIgnoreCase(playerName))
			{
				return mutedPlayer;
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
}
