package com.thevoxelbox.voxelguest.modules.general;

import com.thevoxelbox.voxelguest.VoxelGuest;
import com.thevoxelbox.voxelguest.modules.GuestModule;
import com.thevoxelbox.voxelguest.modules.general.command.EntityPurgeCommandExecutor;
import com.thevoxelbox.voxelguest.modules.general.command.FakequitCommandExecutor;
import com.thevoxelbox.voxelguest.modules.general.command.SystemCommandExecutor;
import com.thevoxelbox.voxelguest.modules.general.command.VanishCommandExecutor;
import com.thevoxelbox.voxelguest.modules.general.command.VpgCommandExecutor;
import com.thevoxelbox.voxelguest.modules.general.command.VtpCommandExecutor;
import com.thevoxelbox.voxelguest.modules.general.command.WhoCommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class GeneralModule extends GuestModule
{

	public static final String ENTITY_PURGE_PERM = "voxelguest.general.ep";
	public static final String VANISH_PERM = "voxelguest.general.vanish";
	public static final String FAKEQUIT_PERM = "voxelguest.general.fakequit";

	private GeneralModuleConfiguration configuration;

	//CommandExecuters
	private final EntityPurgeCommandExecutor entityPurgeCommandExecutor;
	private final VanishCommandExecutor vanishCommandExecutor;
	private final FakequitCommandExecutor fakequitCommandExecutor;
	private final WhoCommandExecutor whoCommandExecutor;
	
	/*
	 * these will be used to persist vanished and fakequit players through reloads and restarts
	private String[] reloadVanishedList;
	private String[] reloadFakequitList;
	private String[] reloadOfflineFQList;
	*/
	private final SystemCommandExecutor systemCommandExecutor;
	private final VpgCommandExecutor vpgCommandExecutor;
    private final VtpCommandExecutor vtpCommandExecutor;
	private List<String> vanished = new ArrayList<>();
	private List<String> oVanished = new ArrayList<>();
	private List<String> fakequit = new ArrayList<>();
	private List<String> oFakequit = new ArrayList<>();
	//Listener
	private ConnectionEventListener connectionEventListener;
	//TPS ticker
	private TPSTicker ticker = new TPSTicker();

	public GeneralModule()
	{
		setName("General Module");

		configuration = new GeneralModuleConfiguration();

		entityPurgeCommandExecutor = new EntityPurgeCommandExecutor();
		vanishCommandExecutor = new VanishCommandExecutor(this);
		connectionEventListener = new ConnectionEventListener(this);
		fakequitCommandExecutor = new FakequitCommandExecutor(this);
		whoCommandExecutor = new WhoCommandExecutor(this);
		systemCommandExecutor = new SystemCommandExecutor();
		vpgCommandExecutor = new VpgCommandExecutor();
	    vtpCommandExecutor = new VtpCommandExecutor();
	}

	@Override
	public final void onEnable()
	{
		Bukkit.getScheduler().scheduleSyncRepeatingTask(VoxelGuest.getPluginInstance(), ticker, 0L, TPSTicker.getPollInterval());
		//load persisted vanished players

		super.onEnable();


	}

	@Override
	public final void onDisable()
	{
		//save vanished players

		super.onDisable();
	}

	@Override
	public final HashSet<Listener> getListeners()
	{
		final HashSet<Listener> listeners = new HashSet<>();
		listeners.add(connectionEventListener);

		return listeners;
	}

	@Override
	public HashMap<String, CommandExecutor> getCommandMappings()
	{
		HashMap<String, CommandExecutor> commandMappings = new HashMap<>();
		commandMappings.put("ep", entityPurgeCommandExecutor);
		commandMappings.put("vanish", vanishCommandExecutor);
		commandMappings.put("fakequit", fakequitCommandExecutor);
		commandMappings.put("who", whoCommandExecutor);
		commandMappings.put("sys", systemCommandExecutor);
		commandMappings.put("vpg", vpgCommandExecutor);
	    commandMappings.put("vtp", vtpCommandExecutor);

		return commandMappings;
	}

	/*
	 * Hides the specified player for all online players
	 */
	public void hidePlayerForAll(Player hidden)
	{
		if (hidden == null)
		{
			return;
		}

		for (Player p : Bukkit.getOnlinePlayers())
		{
			if (!p.hasPermission(VANISH_PERM))
			{
				p.hidePlayer(hidden);
			}
		}
	}

	/*
	 * Hides all online vanished players for the specified player
	 */
	public void hideAllForPlayer(Player player)
	{
		if (player == null)
		{
			return;
		}

		if (player.hasPermission(VANISH_PERM))
		{
			return;
		}

		for (String s : vanished)
		{
			Player hidden = Bukkit.getPlayer(s);
			if (hidden != null) player.hidePlayer(hidden);
		}
	}

	public List<String> getFakequit()
	{
		return fakequit;
	}
	
	public void setFakequit(List<String> f)
	{
		fakequit = f;
	}

	public List<String> getoFakequit()
	{
		return oFakequit;
	}
	public void setoFakequitd(List<String> f)
	{
		oFakequit = f;
	}

	public List<String> getVanished()
	{
		return vanished;
	}
	
	public void setVanished(List<String> v)
	{
		vanished = v;
	}

	public List<String> getoVanished()
	{
		return oVanished;
	}
	
	public void setoVanished(List<String> v)
	{
		oVanished = v;
	}

	@Override
	public Object getConfiguration()
	{
		return configuration;
	}

	@Override
	public String getConfigFileName()
	{
		return "general";
	}
}
