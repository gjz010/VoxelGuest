package com.thevoxelbox.voxelguest.modules.asshat.ban;

import javax.persistence.Column;
import javax.persistence.Entity;
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
	@Column
	private long id;

	@Column
	private String playerName;
	@Column
	private String banReason;

	public BannedPlayer(final String playerName, final String banReason)
	{

		this.playerName = playerName;
		this.banReason = banReason;
	}

	public String getPlayerName()
	{
		return playerName;
	}

	public void setPlayerName(final String playerName)
	{
		this.playerName = playerName;
	}

	public String getBanReason()
	{
		return banReason;
	}

	public void setBanReason(final String banReason)
	{
		this.banReason = banReason;
	}
}
