package com.thevoxelbox.voxelguest.modules.asshat.mute;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Monofraps
 */
@Entity
@Table(name = "mutes")
public class MutedPlayer
{
	@Id
	@GeneratedValue
	@Column
	private long id;

	@Column
	private String playerName;
	@Column
	private String muteReason;

	public MutedPlayer(final String playerName, final String muteReason)
	{

		this.playerName = playerName;
		this.muteReason = muteReason;
	}

	public String getPlayerName()
	{
		return playerName;
	}

	public void setPlayerName(final String playerName)
	{
		this.playerName = playerName;
	}

	public String getMuteReason()
	{
		return muteReason;
	}

	public void setMuteReason(final String muteReason)
	{
		this.muteReason = muteReason;
	}
}
