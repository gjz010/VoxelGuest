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

	public final void mute(final String playerName, final String banReason)
	{
		Preconditions.checkState(!isPlayerMuted(playerName), "Player %s already muted.", playerName);
		mutedPlayers.add(new MutedPlayer(playerName, banReason));
	}

	public final void unmute(final String playerName)
	{
		Preconditions.checkState(isPlayerMuted(playerName), "Player %s is not muted.", playerName);
		mutedPlayers.remove(playerName);
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

	public final boolean isPlayerMuted(final String playerName)
	{
		return getMutedPlayer(playerName) != null;

	}

	public final String whyIsPlayerMuted(final String playerName)
	{
		Preconditions.checkState(isPlayerMuted(playerName), "Player %s must be muted in order to get the mute reason.", playerName);

		return getMutedPlayer(playerName).getMuteReason();
	}

	public final List<MutedPlayer> getMutedPlayers()
	{
		return mutedPlayers;
	}

	public final void setMutedPlayers(final List<MutedPlayer> mutedPlayers)
	{
		this.mutedPlayers = mutedPlayers;
	}

	public final void load()
	{
		mutedPlayers.clear();

		List<Object> protoList = Persistence.getInstance().loadAll(MutedPlayer.class);
		for (Object protoPlayer : protoList)
		{
			mutedPlayers.add((MutedPlayer) protoPlayer);
		}
	}

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
