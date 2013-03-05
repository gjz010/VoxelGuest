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
import com.thevoxelbox.voxelguest.modules.general.command.WatchTPSCommadExecutor;
import com.thevoxelbox.voxelguest.modules.general.command.WhoCommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author TheCryoknight
 * @author Deamon5550
 */
public class GeneralModule extends GuestModule
{
    public static final String FAKEQUIT_PERM = "voxelguest.general.fakequit";

    private GeneralModuleConfiguration configuration;

    //CommandExecuters
    private final EntityPurgeCommandExecutor entityPurgeCommandExecutor;
    private final VanishCommandExecutor vanishCommandExecutor;
    private final FakequitCommandExecutor fakequitCommandExecutor;
    private final WhoCommandExecutor whoCommandExecutor;
    private final AfkCommandExecutor afkCommandExecutor;
    private final WatchTPSCommadExecutor watchTPSCommadExecutor;

    private final SystemCommandExecutor systemCommandExecutor;
    private final VpgCommandExecutor vpgCommandExecutor;
    private final VtpCommandExecutor vtpCommandExecutor;

    //Listener
    private final ConnectionEventListener connectionEventListener;
    private final PlayerEventListener playerEventListener;

    //TPS ticker
    private final TPSTicker ticker = new TPSTicker();
    private int tpsTickerTaskId = -1;

    //Lag Meter thread and helper
    private final LagMeterHelper lagmeter = new LagMeterHelper();

    //Handlers
    private final AfkManager afkManager;
    private final VanishFakequitHandler vanishFakequitHandler;

    private int permGenMonitorTaskId = -1;

    /**
     * Creates a new general module instance.
     */
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
        this.watchTPSCommadExecutor = new WatchTPSCommadExecutor(this);
        this.systemCommandExecutor = new SystemCommandExecutor();
        this.vpgCommandExecutor = new VpgCommandExecutor();
        this.vtpCommandExecutor = new VtpCommandExecutor();
        this.afkManager = new AfkManager();
        this.vanishFakequitHandler = new VanishFakequitHandler(this);
    }

    @Override
    public final void onEnable()
    {
        final PermGenMonitor permGenMonitor = new PermGenMonitor(configuration);

        tpsTickerTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(VoxelGuest.getPluginInstance(), ticker, 0, TPSTicker.getPOLL_INTERVAL());
        permGenMonitorTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(VoxelGuest.getPluginInstance(), permGenMonitor, 20, 20 * 5);
        this.lagmeter.setDaemon(true);
        this.lagmeter.start();

        super.onEnable();
    }

    @Override
    public final void onDisable()
    {
        this.lagmeter.setStopped(true);
        Bukkit.getScheduler().cancelTask(tpsTickerTaskId);
        Bukkit.getScheduler().cancelTask(permGenMonitorTaskId);

        tpsTickerTaskId = -1;
        permGenMonitorTaskId = -1;
        if (lagmeter.isAlive())
        {
            try
            {
                lagmeter.join();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

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
    public final HashMap<String, CommandExecutor> getCommandMappings()
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
        commandMappings.put("watchtps", this.watchTPSCommadExecutor);

        return commandMappings;
    }

    @Override
    public final Object getConfiguration()
    {
        return configuration;
    }

    @Override
    public final String getConfigFileName()
    {
        return "general";
    }

    /**
     *
     * @return Returns the AFK manager.
     */
    public final AfkManager getAfkManager()
    {
        return afkManager;
    }

    /**
     *
     * @return Returns the fakequit handler.
     */
    public final VanishFakequitHandler getVanishFakequitHandler()
    {
        return vanishFakequitHandler;
    }

    /**
     * Replaces all occurrences of $no to the number of online players and replaces all $n with a given plazer name.
     * @param msg The format string.
     * @param playerName The player name to replace all occurrences of $n
     * @return Returns the formatted and replaced string.
     */
    public final String formatJoinLeaveMessage(final String msg, final String playerName)
    {
        int onlinePlayers = Bukkit.getOnlinePlayers().length;
        onlinePlayers -= this.getVanishFakequitHandler().getFakequitSize();
        return msg.replace("$no", Integer.toString(onlinePlayers)).replace("$n", playerName);
    }

    /**
     *
     * @return Returns the lagmeter instance.
     */
    public final LagMeterHelper getLagmeter()
    {
        return lagmeter;
    }
}
