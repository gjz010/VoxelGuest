package com.thevoxelbox.voxelguest.modules.greylist;

import com.thevoxelbox.voxelguest.VoxelGuest;
import com.thevoxelbox.voxelguest.modules.GuestModule;
import com.thevoxelbox.voxelguest.modules.greylist.command.GreylistCommandExecutor;
import com.thevoxelbox.voxelguest.modules.greylist.command.UngreylistCommandExecutor;
import com.thevoxelbox.voxelguest.modules.greylist.command.WhitelistCommandExecutor;
import com.thevoxelbox.voxelguest.modules.greylist.listener.GreylistListener;
import com.thevoxelbox.voxelguest.modules.greylist.model.Greylistee;
import com.thevoxelbox.voxelguest.persistence.Persistence;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;

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

    private GreylistConfiguration config;
    private StreamThread streamTask;

    /**
     *
     */
    public GreylistModule()
    {
        this.setName("Greylist Module");
        config = new GreylistConfiguration();
        greylistListener = new GreylistListener(this);
        greylistCommandExecutor = new GreylistCommandExecutor(this);
        ungreylistCommandExecutor = new UngreylistCommandExecutor(this);
        whitelistCommandExecutor = new WhitelistCommandExecutor(this);
    }

    @Override
    public final void onEnable()
    {
        if (config.isStreamGreylisting())
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

        if (this.config.isSetGroupOnGreylist())
        {
            if (VoxelGuest.getPerms().playerAddGroup(Bukkit.getWorlds().get(0), name, this.config.getGreylistGroupName()))
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
    public GreylistConfiguration getConfig()
    {
        return this.config;
    }
}
