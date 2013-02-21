package com.thevoxelbox.voxelguest.modules.greylist;

import com.thevoxelbox.voxelguest.configuration.annotations.ConfigurationGetter;
import com.thevoxelbox.voxelguest.configuration.annotations.ConfigurationSetter;
import com.thevoxelbox.voxelguest.modules.GuestModule;
import com.thevoxelbox.voxelguest.modules.greylist.command.GreylistCommandExecutor;
import com.thevoxelbox.voxelguest.modules.greylist.command.UngreylistCommandExecutor;
import com.thevoxelbox.voxelguest.modules.greylist.listener.GreylistListener;
import com.thevoxelbox.voxelguest.modules.greylist.model.Greylistee;
import com.thevoxelbox.voxelguest.persistence.Persistence;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * @author MikeMatrix
 */
public class GreylistModule extends GuestModule
{
    private GreylistListener greylistListener;
    private GreylistCommandExecutor greylistCommandExecutor;
    private UngreylistCommandExecutor ungreylistCommandExecutor;
    private WhitelistCommandExecutor whitelistCommandExecutor;
    private SocketListener socketListener;
    private BukkitTask socketListenerTask;
    private boolean explorationMode = false;
    private String notGreylistedKickMessage = "You are not greylisted.";
    private String authToken = "changeme";

    private GraylistConfiguration config;
    private StreamThread streamTask;

    /**
     *
     */
    public GreylistModule()
    {
        this.setName("Greylist Module");
        config = new GraylistConfiguration();
        greylistListener = new GreylistListener(this);
        greylistCommandExecutor = new GreylistCommandExecutor(this);
        ungreylistCommandExecutor = new UngreylistCommandExecutor(this);
        whitelistCommandExecutor = new WhitelistCommandExecutor(this);
    }

    @Override
    public final void onEnable()
    {
        socketListener = new SocketListener(11368, this);
        socketListenerTask = Bukkit.getScheduler().runTaskAsynchronously(VoxelGuest.getPluginInstance(), socketListener);
        if (config.isStreamGraylisting())
        {
            this.streamTask = new StreamThread(this);
            this.streamTask.start();
        }
        super.onEnable();
    }
    @Override
    public final void onDisable()
    {
        if (this.streamTask != null) {
            this.streamTask.killProcesses();
        }
        super.onDisable();
    }

    @Override
    public void onDisable()
    {
        super.onDisable();

        socketListener.setRun(false);
        socketListenerTask.cancel();
        socketListener = null;
        socketListenerTask = null;
    }

    @Override
    public final Object getConfiguration()
    {
        return this.config;
    }

    @Override
    public final HashSet<Listener> getListeners()
    {
        final HashSet<Listener> listeners = new HashSet<>();
        listeners.add(greylistListener);
        return listeners;
    }

    @Override
    public final HashMap<String, CommandExecutor> getCommandMappings()
    {
        HashMap<String, CommandExecutor> commandMapping = new HashMap<>();
        commandMapping.put("greylist", greylistCommandExecutor);
        commandMapping.put("ungreylist", ungreylistCommandExecutor);
        commandMapping.put("whitelist", whitelistCommandExecutor);

        return commandMapping;
    }

    @ConfigurationGetter("exploration-mode")
    public final boolean isExplorationMode()
    {
        return explorationMode;
    }

    @ConfigurationSetter("exploration-mode")
    public final void setExplorationMode(final boolean explorationMode)
    {
        this.explorationMode = explorationMode;
    }

    @ConfigurationGetter("not-greylisted-kick-message")
    public final String getNotGreylistedKickMessage()
    {
        return notGreylistedKickMessage;
    }

    @ConfigurationSetter("not-greylisted-kick-message")
    public final void setNotGreylistedKickMessage(final String notGreylistedKickMessage)
    {
        this.notGreylistedKickMessage = notGreylistedKickMessage;
    }

    @ConfigurationGetter("injection-auth-token")
    public String getAuthToken()
    {
        return authToken;
    }

    @ConfigurationSetter("injection-auth-token")
    public void setAuthToken(final String authToken)
    {
        this.authToken = authToken;
    }

    public final boolean isOnPersistentGreylist(final String name)
    {
        final List<Greylistee> greylistees;

        try
        {
            final HashMap<String, Object> selectRestrictions = new HashMap<>();
            selectRestrictions.put("name", name.toLowerCase());

            greylistees = Persistence.getInstance().loadAll(Greylistee.class, selectRestrictions);
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }

        for (Greylistee greylistee : greylistees)
        {
            if (greylistee.getName().equalsIgnoreCase(name))
            {
                return true;
            }
        }
        return false;
    }

    public void greylist(final String name)
    {
        final HashMap<String, Object> selectRestrictions = new HashMap<>();
        selectRestrictions.put("name", name.toLowerCase());
        final List<Greylistee> greylistees = Persistence.getInstance().loadAll(Greylistee.class, selectRestrictions);

        for (Greylistee greylistee : greylistees)
        {
            if (greylistee.getName().equalsIgnoreCase(name))
            {
                return;
            }
        }
        Persistence.getInstance().save(new Greylistee(name.toLowerCase()));

        if (this.config.isSetGroupOnGraylist())
        {
            if (VoxelGuest.getPerms().playerAddGroup(Bukkit.getWorlds().get(0), name, this.config.getGraylistGroupName()))
            {
                VoxelGuest.getPluginInstance().getLogger().warning("Error: Could not set new graylisted player to group.");
            }
        }
    }

    public void ungreylist(final String name)
    {
        HashMap<String, Object> selectRestrictions = new HashMap<>();
        selectRestrictions.put("name", name.toLowerCase());
        final List<Greylistee> greylistees = Persistence.getInstance().loadAll(Greylistee.class, selectRestrictions);

        for (Greylistee greylistee : greylistees)
        {
            if (greylistee.getName().equalsIgnoreCase(name))
            {
                Persistence.getInstance().delete(greylistee);
            }
        }
    }
    public GraylistConfiguration getConfig()
    {
        return this.config;
    }
}
