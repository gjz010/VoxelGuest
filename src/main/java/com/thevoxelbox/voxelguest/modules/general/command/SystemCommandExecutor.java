package com.thevoxelbox.voxelguest.modules.general.command;

import com.google.common.base.Preconditions;
import com.thevoxelbox.voxelguest.modules.general.TPSTicker;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import java.lang.management.ManagementFactory;
import java.text.NumberFormat;
import java.util.List;

public class SystemCommandExecutor implements CommandExecutor
{
    private static final int TPS_PER_SECOND_THRESHOLD = 20;
    private static final int BAR_SEGMENTS = 20;

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args)
    {
        Preconditions.checkNotNull(sender);
        Preconditions.checkNotNull(command);
        Preconditions.checkNotNull(args);

        if (args.length == 0)
        {
            this.printSpecs(sender);
            return true;
        }

        if (args.length > 0)
        {
            if (args[0].equalsIgnoreCase("gc"))
            {
                System.gc();
                return true;
            }
        }

        return false;
    }

    private void printSpecs(CommandSender sender)
    {
        sender.sendMessage("§8==============================");
        sender.sendMessage("§bServer Specs");

        sender.sendMessage("§7Operating System§f: §a" + ManagementFactory.getOperatingSystemMXBean().getName() + " version " + ManagementFactory.getOperatingSystemMXBean().getVersion());
        sender.sendMessage("§7Architecture§f: §a" + ManagementFactory.getOperatingSystemMXBean().getArch());

        sender.sendMessage("§8==============================");
        sender.sendMessage("§bCPU Specs");
        final double rawCPUUsage = ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
        sender.sendMessage("§7CPU Usage§f: " + renderBar(rawCPUUsage, (Runtime.getRuntime().availableProcessors())));
        sender.sendMessage("§7Available cores§f: §a" + Runtime.getRuntime().availableProcessors());

        sender.sendMessage("§8==============================");
        sender.sendMessage("§bMemory Specs");
        final double memUsed = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576;
        final double memMax = Runtime.getRuntime().maxMemory() / 1048576;

        sender.sendMessage("§7JVM Memory§f: " + renderBar(memUsed, memMax));
        sender.sendMessage("§7JVM Heap Memory (MB)§f: §a" + ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / 1048576);
        sender.sendMessage("§7JVM Free Memory (MB)§f: §a" + Runtime.getRuntime().freeMemory() / 1048576);
        sender.sendMessage("§7JVM Maximum Memory (MB)§f: §a" + ((Runtime.getRuntime().maxMemory() == Long.MAX_VALUE) ? "No defined limit" : Runtime.getRuntime().maxMemory() / 1048576));
        sender.sendMessage("§7JVM Used Memory (MB)§f: §a" + Runtime.getRuntime().totalMemory() / 1048576);

        sender.sendMessage("§8==============================");
        sender.sendMessage("§bBukkit Specs");

        final List<World> loadedWorlds = Bukkit.getWorlds();
        sender.sendMessage("§7Loaded Worlds§f:");
        for (World world : loadedWorlds)
        {
            sender.sendMessage("§a- " + world.getName() + " §7[§fChunks: §6" + world.getLoadedChunks().length + "§f, Entities: §6" + world.getEntities().size() + "§7]");
        }

        String ticks = "§7TPS§f: ";
        if (TPSTicker.hasTicked())
        {
            sender.sendMessage(ticks + renderTPSBar(TPSTicker.calculateTPS(), TPS_PER_SECOND_THRESHOLD));
        }
        else
        {
            sender.sendMessage(ticks + "§cNo TPS poll yet");
        }
    }

    private String renderBar(final double value, final double max)
    {
        final double usedLevel = BAR_SEGMENTS * (value / max);
        final int usedRounded = (int) Math.round(usedLevel);
        String bar = "§8[";

        for (int i = 0; i < BAR_SEGMENTS; i++)
        {
            bar += ((i + 1) <= usedRounded) ? "§b#" : "§7_";
        }

        final double percent = (usedLevel / BAR_SEGMENTS);
        final NumberFormat format = NumberFormat.getPercentInstance();

        return (bar + "§8] (§f" + format.format(percent) + "§8)");
    }

    //TODO: Should be merged with renderBar
    private String renderTPSBar(final double value, final double max)
    {
        double usedLevel = TPS_PER_SECOND_THRESHOLD * (value / max);
        int usedRounded = (int) Math.round(usedLevel);
        String bar = "§8[";

        for (int i = 0; i < TPS_PER_SECOND_THRESHOLD; i++)
        {
            if ((i + 1) <= usedRounded)
            {
                bar += "§b#";
            }
            else
            {
                bar += "§7_";
            }
        }

        double percent = (usedLevel / TPS_PER_SECOND_THRESHOLD);

        if (percent > 1)
        {
            percent = 1;
        }

        return (bar + "§8] (§f" + percent * TPS_PER_SECOND_THRESHOLD + " TPS§8)");
    }
}
