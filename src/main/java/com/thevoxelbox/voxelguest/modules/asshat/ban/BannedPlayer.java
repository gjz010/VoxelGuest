package com.thevoxelbox.voxelguest.modules.asshat.ban;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Monofraps
 */
@Entity
@Table(name = "bans")
public class BannedPlayer
{
	@Id
	@GeneratedValue
	@Column
	private long id;
	@Column
	private String playerName;
	@Column
	private String banReason;

	public BannedPlayer()
	{
		this.playerName = "";
		this.banReason = "";
	}

	/**
	 * @param playerName The name of the banned player.
	 * @param banReason  The reason the player is banned for.
	 */
	public BannedPlayer(final String playerName, final String banReason)
	{

		this.playerName = playerName;
		this.banReason = banReason;
	}

	/**
	 * @return Returns the name of the banned player.
	 */
	public final String getPlayerName()
	{
		return playerName;
	}

	/**
	 * @return Returns the reason the player is banned for.
	 */
	public final String getBanReason()
	{
		return banReason;
	}
}
