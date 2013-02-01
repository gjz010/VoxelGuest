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

	public void ban(String playerName, String banReason) {
		Preconditions.checkState(!isPlayerBanned(playerName), "Player %s already banned.", playerName);
		bannedPlayers.add(new BannedPlayer(playerName, banReason));
	}

	public void unban(String playerName) {
		Preconditions.checkState(isPlayerBanned(playerName), "Player %s is not banned.", playerName);
		bannedPlayers.remove(playerName);
	}

	private BannedPlayer getBannedPlayer(String playerName) {
		for(BannedPlayer player : bannedPlayers) {
			if(player.getPlayerName().equalsIgnoreCase(playerName)) {
				return player;
			}
		}

		return null;
	}

	public boolean isPlayerBanned(String playerName)
	{
		return getBannedPlayer(playerName) != null;

	}

	public String whyIsPlayerBanned(String playerName)
	{
		Preconditions.checkState(isPlayerBanned(playerName), "Player %s must be banned in order to get the ban reason.", playerName);

		return getBannedPlayer(playerName).getBanReason();
	}

	public List<BannedPlayer> getBannedPlayers()
	{
		return bannedPlayers;
	}

	public void setBannedPlayers(final List<BannedPlayer> bannedPlayers)
	{
		this.bannedPlayers = bannedPlayers;
	}

	public void load()
	{
		bannedPlayers.clear();

		List<Object> protoList =  Persistence.getInstance().loadAll(BannedPlayer.class);
		for(Object protoPlayer : protoList) {
			bannedPlayers.add((BannedPlayer) protoPlayer);
		}
	}

	public void save() {
		List<Object> protoList = new ArrayList<>();
		for(BannedPlayer protoPlayer : bannedPlayers) {
			protoList.add(protoPlayer);
		}

		Persistence.getInstance().saveAll(protoList);
	}
}
