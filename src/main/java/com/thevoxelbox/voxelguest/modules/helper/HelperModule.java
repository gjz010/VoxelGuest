package com.thevoxelbox.voxelguest.modules.helper;

import com.thevoxelbox.voxelguest.modules.GuestModule;
import com.thevoxelbox.voxelguest.modules.helper.command.HelperCommand;
import com.thevoxelbox.voxelguest.modules.helper.command.HelperReviewCommand;
import com.thevoxelbox.voxelguest.modules.helper.command.WLReviewCommand;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author TheCryoknight
 */
public class HelperModule extends GuestModule
{
    private final WLReviewCommand wLReviewCommand;
    private final HelperCommand helperCommand;
    private final HelperReviewCommand helperReviewCommand;

    private final HelperManager manager;

    public HelperModule()
    {
        this.setName("Helper Module");
        this.wLReviewCommand = new WLReviewCommand(this);
        this.helperCommand = new HelperCommand(this);
        this.helperReviewCommand = new HelperReviewCommand(this);
        this.manager = new HelperManager();
    }

    @Override
    public void onEnable()
    {
        this.getManager().initHelperList();
        super.onEnable();
    }

    @Override
    public Set<Listener> getListeners()
    {
        final Set<Listener> listeners = new HashSet<>();
        return listeners;
    }

    @Override
    public Object getConfiguration()
    {
        return null;
    }

    @Override
    public Map<String, CommandExecutor> getCommandMappings()
    {
        final Map<String, CommandExecutor> command = new HashMap<>();
        command.put("wlreview", this.wLReviewCommand);
        command.put("helper", this.helperCommand);
        command.put("helperreview", this.helperReviewCommand);
        return command;
    }

    public HelperManager getManager()
    {
        return manager;
    }

}
