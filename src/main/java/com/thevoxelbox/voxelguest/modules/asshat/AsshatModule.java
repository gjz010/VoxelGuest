package com.thevoxelbox.voxelguest.modules.asshat;

import com.google.common.base.Preconditions;
import com.thevoxelbox.voxelguest.modules.GuestModule;
import com.thevoxelbox.voxelguest.modules.asshat.ban.Banlist;
import com.thevoxelbox.voxelguest.modules.asshat.ban.BannedPlayer;
import com.thevoxelbox.voxelguest.modules.asshat.command.BanCommandExecutor;
import com.thevoxelbox.voxelguest.modules.asshat.command.BanreasonCommandExecutor;
import com.thevoxelbox.voxelguest.modules.asshat.command.FreezeCommandExecutor;
import com.thevoxelbox.voxelguest.modules.asshat.command.KickCommandExecutor;
import com.thevoxelbox.voxelguest.modules.asshat.command.MuteCommandExecutor;
import com.thevoxelbox.voxelguest.modules.asshat.command.SoapboxCommandExecutor;
import com.thevoxelbox.voxelguest.modules.asshat.command.UnbanCommandExecutor;
import com.thevoxelbox.voxelguest.modules.asshat.command.UnmuteCommandExecutor;
import com.thevoxelbox.voxelguest.modules.asshat.mute.MutedPlayer;
import com.thevoxelbox.voxelguest.modules.asshat.mute.Mutelist;
import com.thevoxelbox.voxelguest.persistence.Persistence;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Monofraps
 */
public class AsshatModule extends GuestModule
{
	public static final String SILENCE_BYPASS_PERM = "voxelguest.asshat.bypass.silence";
	public static final String FREEZE_BYPASS_PERM = "voxelguest.asshat.bypass.freeze";

	private PlayerListener playerListener;

	private BanCommandExecutor banCommandExecutor;
	private UnbanCommandExecutor unbanCommandExecutor;
	private BanreasonCommandExecutor banreasonCommandExecutor;
	private MuteCommandExecutor muteCommandExecutor;
	private UnmuteCommandExecutor unmuteCommandExecutor;
	private KickCommandExecutor kickCommandExecutor;
	private SoapboxCommandExecutor soapboxCommandExecutor;
	private FreezeCommandExecutor freezeCommandExecutor;

	private Mutelist mutelist = new Mutelist();
	private Banlist banlist = new Banlist();
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

		Persistence.getInstance().registerPersistentClass(BannedPlayer.class);
		Persistence.getInstance().registerPersistentClass(MutedPlayer.class);
	}

	@Override
	public void onEnable()
	{
		banlist.load();
		mutelist.load();

		super.onEnable();
	}

	@Override
	public void onDisable()
	{
		banlist.save();
		mutelist.save();

		super.onDisable();
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

	public Banlist getBanlist()
	{
		return banlist;
	}

	public Mutelist getMutelist()
	{
		return mutelist;
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
