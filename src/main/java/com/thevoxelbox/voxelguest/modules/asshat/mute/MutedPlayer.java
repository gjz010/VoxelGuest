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

	public MutedPlayer()
	{
		this.playerName = "";
		this.muteReason = "";
	}

	/**
	 * @param playerName The name of the muted player.
	 * @param muteReason The reason the player is muted for.
	 */
	public MutedPlayer(final String playerName, final String muteReason)
	{
		this.playerName = playerName;
		this.muteReason = muteReason;
	}

	/**
	 * @return Returns the name of the muted player.
	 */
	public final String getPlayerName()
	{
		return playerName;
	}

	/**
	 * @return Returns the reason the player is muted for.
	 */
	public final String getMuteReason()
	{
		return muteReason;
	}
}
