package com.thevoxelbox.voxelguest.modules.asshat;

import com.google.common.base.Preconditions;
import com.thevoxelbox.voxelguest.modules.GuestModule;
import com.thevoxelbox.voxelguest.modules.asshat.command.BanCommandExecutor;
import com.thevoxelbox.voxelguest.modules.asshat.command.BanreasonCommandExecutor;
import com.thevoxelbox.voxelguest.modules.asshat.command.FreezeCommandExecutor;
import com.thevoxelbox.voxelguest.modules.asshat.command.KickCommandExecutor;
import com.thevoxelbox.voxelguest.modules.asshat.command.MuteCommandExecutor;
import com.thevoxelbox.voxelguest.modules.asshat.command.SoapboxCommandExecutor;
import com.thevoxelbox.voxelguest.modules.asshat.command.UnbanCommandExecutor;
import com.thevoxelbox.voxelguest.modules.asshat.command.UnmuteCommandExecutor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Monofraps
 */
public class AsshatModule extends GuestModule
{
	private PlayerListener playerListener;

	private BanCommandExecutor banCommandExecutor;
	private UnbanCommandExecutor unbanCommandExecutor;
	private BanreasonCommandExecutor banreasonCommandExecutor;
	private MuteCommandExecutor muteCommandExecutor;
	private UnmuteCommandExecutor unmuteCommandExecutor;
	private KickCommandExecutor kickCommandExecutor;
	private SoapboxCommandExecutor soapboxCommandExecutor;
	private FreezeCommandExecutor freezeCommandExecutor;

	private HashMap<String, String> mutedPlayers = new HashMap<>();
	private HashMap<String, String> bannedPlayers = new HashMap<>();
	private boolean silenceEnabled = false;
	private boolean freezeEnabled;

	public AsshatModule()
	{
		playerListener = new PlayerListener(this);

		banCommandExecutor = new BanCommandExecutor(this);
		unbanCommandExecutor = new UnbanCommandExecutor(this);
		banreasonCommandExecutor = new BanreasonCommandExecutor(this);
		muteCommandExecutor = new MuteCommandExecutor(this);
		unmuteCommandExecutor = new UnmuteCommandExecutor(this);
		kickCommandExecutor = new KickCommandExecutor();
		soapboxCommandExecutor = new SoapboxCommandExecutor(this);
		freezeCommandExecutor = new FreezeCommandExecutor(this);
	}

	@Override
	public HashSet<Listener> getListeners()
	{
		final HashSet<Listener> listeners = new HashSet<>();
		listeners.add(playerListener);

		return listeners;
	}

	@Override
	public Object getConfiguration()
	{
		return null;
	}

	@Override
	public String getConfigFileName()
	{
		return "asshat";
	}

	@Override
	public HashMap<String, CommandExecutor> getCommandMappings()
	{
		HashMap<String, CommandExecutor> commandMappings = new HashMap<>();
		commandMappings.put("ban", banCommandExecutor);
		commandMappings.put("unban", unbanCommandExecutor);
		commandMappings.put("banreason", banreasonCommandExecutor);
		commandMappings.put("mute", muteCommandExecutor);
		commandMappings.put("unmute", unmuteCommandExecutor);
		commandMappings.put("kick", kickCommandExecutor);
		commandMappings.put("soapbox", soapboxCommandExecutor);
		commandMappings.put("freeze", freezeCommandExecutor);

		return commandMappings;
	}

	public boolean isPlayerMuted(String playerName)
	{
		return mutedPlayers.containsKey(playerName);
	}

	public String whyIsPlayerMuted(String playerName)
	{
		Preconditions.checkState(isPlayerMuted(playerName), "Player %s must be muted in order to get the mute reason.", playerName);

		return mutedPlayers.get(playerName);
	}

	public boolean isPlayerBanned(String playerName)
	{
		return bannedPlayers.containsKey(playerName);
	}

	public String whyIsPlayerBanned(String playerName)
	{
		Preconditions.checkState(isPlayerBanned(playerName), "Player %s must be banned in order to get the ban reason.", playerName);

		return bannedPlayers.get(playerName);
	}

	public void ban(String playerName, String banReason) {
		Preconditions.checkState(!isPlayerBanned(playerName), "Player %s already banned.", playerName);
		bannedPlayers.put(playerName, banReason);
	}

	public void unban(String playerName) {
		Preconditions.checkState(isPlayerBanned(playerName), "Player %s is not banned.", playerName);
		bannedPlayers.remove(playerName);
	}

	public void mute(final String playerName, final String muteReason)
	{
		Preconditions.checkState(!isPlayerMuted(playerName), "Player %s already muted.", playerName);
		mutedPlayers.put(playerName, muteReason);
	}

	public void unmute(final String playerName)
	{
		Preconditions.checkState(!isPlayerMuted(playerName), "Player %s is not muted.", playerName);
		mutedPlayers.remove(playerName);
	}

	public boolean isSilenceEnabled()
	{
		return silenceEnabled;
	}

	public void setSilenceEnabled(final boolean silenceEnabled)
	{
		this.silenceEnabled = silenceEnabled;
	}

	public boolean isFreezeEnabled()
	{
		return freezeEnabled;
	}

	public void setFreezeEnabled(final boolean freezeEnabled)
	{
		this.freezeEnabled = freezeEnabled;
	}
}
