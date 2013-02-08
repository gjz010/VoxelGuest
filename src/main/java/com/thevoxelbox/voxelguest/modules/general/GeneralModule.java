package com.thevoxelbox.voxelguest.modules.general;

import com.thevoxelbox.voxelguest.VoxelGuest;
import com.thevoxelbox.voxelguest.modules.GuestModule;
import com.thevoxelbox.voxelguest.modules.general.command.AfkCommandExecutor;
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
    private final AfkCommandExecutor afkCommandExecutor;

    private final SystemCommandExecutor systemCommandExecutor;
    private final VpgCommandExecutor vpgCommandExecutor;
    private final VtpCommandExecutor vtpCommandExecutor;
    private List<String> vanished = new ArrayList<>();
    private List<String> oVanished = new ArrayList<>();
    private List<String> fakequit = new ArrayList<>();
    private List<String> oFakequit = new ArrayList<>();

    //Listener
    private final ConnectionEventListener connectionEventListener;
    private final PlayerEventListener playerEventListener;

    //TPS ticker
    private final TPSTicker ticker = new TPSTicker();
    private int tpsTickerTaskId = -1;
    //Afk handler
    private final AfkManager afkManager;

    private PermGenMonitor permGenMonitor;
    private int permGenMonitorTaskId = -1;

    public GeneralModule()
    {
        this.setName("General Module");

        this.configuration = new GeneralModuleConfiguration();

        this.entityPurgeCommandExecutor = new EntityPurgeCommandExecutor();
        this.vanishCommandExecutor = new VanishCommandExecutor(this);
        this.connectionEventListener = new ConnectionEventListener(this);
        this.playerEventListener = new PlayerEventListener(this);
        this.fakequitCommandExecutor = new FakequitCommandExecutor(this);
        this.whoCommandExecutor = new WhoCommandExecutor(this);
        this.afkCommandExecutor = new AfkCommandExecutor(this);
        this.systemCommandExecutor = new SystemCommandExecutor();
        this.vpgCommandExecutor = new VpgCommandExecutor();
        this.vtpCommandExecutor = new VtpCommandExecutor();
        this.afkManager = new AfkManager();
    }

    @Override
    public final void onEnable()
    {
        permGenMonitor = new PermGenMonitor(configuration);

        tpsTickerTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(VoxelGuest.getPluginInstance(), ticker, 0, TPSTicker.getPollInterval());
        permGenMonitorTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(VoxelGuest.getPluginInstance(), permGenMonitor, 20, 20 * 5);

        super.onEnable();
    }

    @Override
    public final void onDisable()
    {
        Bukkit.getScheduler().cancelTask(tpsTickerTaskId);
        Bukkit.getScheduler().cancelTask(permGenMonitorTaskId);

        tpsTickerTaskId = -1;
        permGenMonitorTaskId = -1;

        super.onDisable();
    }

    @Override
    public final HashSet<Listener> getListeners()
    {
        final HashSet<Listener> listeners = new HashSet<>();
        listeners.add(this.connectionEventListener);
        listeners.add(this.playerEventListener);

        return listeners;
    }

    @Override
    public HashMap<String, CommandExecutor> getCommandMappings()
    {
        HashMap<String, CommandExecutor> commandMappings = new HashMap<>();
        commandMappings.put("ep", this.entityPurgeCommandExecutor);
        commandMappings.put("vanish", this.vanishCommandExecutor);
        commandMappings.put("fakequit", this.fakequitCommandExecutor);
        commandMappings.put("who", this.whoCommandExecutor);
        commandMappings.put("afk", this.afkCommandExecutor);
        commandMappings.put("sys", this.systemCommandExecutor);
        commandMappings.put("vpg", this.vpgCommandExecutor);
        commandMappings.put("vtp", this.vtpCommandExecutor);

        return commandMappings;
    }

    /**
     * Hides the specified player for all online players
     *
     * @param hidden player to hide
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

    /**
     * Hides all online vanished players for the specified player
     *
     * @param player Player to hide vanished from
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
            if (hidden != null)
            {
                player.hidePlayer(hidden);
            }
        }
    }

    /**
     * Gets the current list of people fake quit.
     *
     * @return List of the names of players fake quit
     */
    public List<String> getFakequit()
    {
        return fakequit;
    }

    /**
     * Sets the list of people fake quit.
     *
     * @param newFQList List of names to set as fake quit
     */
    public void setFakequit(List<String> newFQList)
    {
        fakequit = newFQList;
    }

    /**
     * Gets the list of names of players on the offline fake quit.
     * 
     * @return
     */
    public List<String> getoFakequit()
    {
        return oFakequit;
    }

    /**
     * Sets the list of names of players on the offline fake quit.
     * 
     * @param newOfflineFQList
     */
    public void setoFakequitd(List<String> newOfflineFQList)
    {
        oFakequit = newOfflineFQList;
    }

    /**
     * Get the list of people vanished.
     *
     * @return List of people vanished
     */
    public List<String> getVanished()
    {
        return vanished;
    }

    /**
     * Get the list of people vanished.
     *
     * @param v
     */
    public void setVanished(List<String> v)
    {
        vanished = v;
    }

    /**
     * Gets the list of names of players on the offline vanished.
     *
     * @return
     */
    public List<String> getoVanished()
    {
        return oVanished;
    }

    /**
     * Sets the list of names of players on the offline vanished.
     *
     * @param v
     */
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

    public AfkManager getAfkManager() {
        return afkManager;
    }
}
