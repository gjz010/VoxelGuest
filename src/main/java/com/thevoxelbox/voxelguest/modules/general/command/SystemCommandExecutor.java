package com.thevoxelbox.voxelguest.modules.general.command;

import com.google.common.base.Preconditions;
import com.sun.management.OperatingSystemMXBean;
import com.thevoxelbox.voxelguest.modules.general.TPSTicker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
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
            if (args[0].equalsIgnoreCase("mem"))
            {
                final double memUsed = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576;
                final double memMax = Runtime.getRuntime().maxMemory() / 1048576;

                sender.sendMessage(ChatColor.DARK_GRAY + "==============================");
                sender.sendMessage(ChatColor.GRAY + "JVM Memory" + ChatColor.WHITE + ": " + renderBar(memUsed, memMax));
                sender.sendMessage(ChatColor.GRAY + "JVM Heap Memory (MB)" + ChatColor.WHITE + ": " + ChatColor.GREEN + ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / 1048576);
                sender.sendMessage(ChatColor.GRAY + "JVM Free Memory (MB)" + ChatColor.WHITE + ": " + ChatColor.GREEN + Runtime.getRuntime().freeMemory() / 1048576);
                sender.sendMessage(ChatColor.GRAY + "JVM Maximum Memory (MB)" + ChatColor.WHITE + ": " + ChatColor.GREEN + ((Runtime.getRuntime().maxMemory() == Long.MAX_VALUE) ? "No defined limit" : Runtime.getRuntime().maxMemory() / 1048576));
                sender.sendMessage(ChatColor.GRAY + "JVM Used Memory (MB)" + ChatColor.WHITE + ": " + ChatColor.GREEN + Runtime.getRuntime().totalMemory() / 1048576);

                for (MemoryPoolMXBean memData: ManagementFactory.getMemoryPoolMXBeans())
                {
                    sender.sendMessage(ChatColor.DARK_GRAY + "==============================");
                    sender.sendMessage(ChatColor.GRAY + "Name" + ChatColor.WHITE + ": " + ChatColor.GREEN + memData.getName());
                    sender.sendMessage(ChatColor.GRAY + "Type" + ChatColor.WHITE + ": " + ChatColor.GREEN + memData.getType());
                    sender.sendMessage(ChatColor.GRAY + "Usage" + ChatColor.WHITE + ": " + ChatColor.GREEN + memData.getUsage().getUsed());
                    sender.sendMessage(ChatColor.GRAY + "Max usage" + ChatColor.WHITE + ": " + ChatColor.GREEN + memData.getUsage().getMax());
                    if (memData.isUsageThresholdSupported())
                    {
                        sender.sendMessage(ChatColor.GRAY + "Threshold " + ChatColor.WHITE + ": " + ChatColor.GREEN + memData.getUsageThreshold());
                    }
                    sender.sendMessage(ChatColor.DARK_GRAY + "==============================");
                    return true;
                }
            }
        }
        return false;
    }

    private void printSpecs(CommandSender sender)
    {
        sender.sendMessage(ChatColor.DARK_GRAY + "==============================");
        sender.sendMessage(ChatColor.AQUA + "Server Specs");

        OperatingSystemMXBean operatingSystemBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        sender.sendMessage(ChatColor.GRAY + "Operating System" + ChatColor.WHITE + ": " + ChatColor.GREEN + operatingSystemBean.getName() + " version " + operatingSystemBean.getVersion());
        sender.sendMessage(ChatColor.GRAY + "Architecture" + ChatColor.WHITE + ": " + ChatColor.GREEN + operatingSystemBean.getArch());

        sender.sendMessage(ChatColor.DARK_GRAY + "==============================");
        sender.sendMessage(ChatColor.AQUA + "CPU Specs");
        sender.sendMessage(ChatColor.GRAY + "CPU Usage" + ChatColor.WHITE + ": " + renderBar(operatingSystemBean.getSystemCpuLoad(), 1));
        sender.sendMessage(ChatColor.GRAY + "Available cores" + ChatColor.WHITE + ": " + ChatColor.GREEN + Runtime.getRuntime().availableProcessors());

        sender.sendMessage(ChatColor.DARK_GRAY + "==============================");
        sender.sendMessage(ChatColor.AQUA + "Memory Specs");
        final double memUsed = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576;
        final double memMax = Runtime.getRuntime().maxMemory() / 1048576;

        sender.sendMessage(ChatColor.GRAY + "JVM Memory" + ChatColor.WHITE + ": " + renderBar(memUsed, memMax));
        sender.sendMessage(ChatColor.GRAY + "JVM Heap Memory (MB)" + ChatColor.WHITE + ": " + ChatColor.GREEN + ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / 1048576);
        sender.sendMessage(ChatColor.GRAY + "JVM Free Memory (MB)" + ChatColor.WHITE + ": " + ChatColor.GREEN + Runtime.getRuntime().freeMemory() / 1048576);
        sender.sendMessage(ChatColor.GRAY + "JVM Maximum Memory (MB)" + ChatColor.WHITE + ": " + ChatColor.GREEN + ((Runtime.getRuntime().maxMemory() == Long.MAX_VALUE) ? "No defined limit" : Runtime.getRuntime().maxMemory() / 1048576));
        sender.sendMessage(ChatColor.GRAY + "JVM Used Memory (MB)" + ChatColor.WHITE + ": " + ChatColor.GREEN + Runtime.getRuntime().totalMemory() / 1048576);

        sender.sendMessage(ChatColor.DARK_GRAY + "==============================");
        sender.sendMessage(ChatColor.AQUA + "Bukkit Specs");

        final List<World> loadedWorlds = Bukkit.getWorlds();
        sender.sendMessage(ChatColor.GRAY + "Loaded Worlds" + ChatColor.WHITE + ":");
        for (World world : loadedWorlds)
        {
            sender.sendMessage(ChatColor.GREEN + "- " + world.getName()
                    + ChatColor.GRAY + " [" + ChatColor.WHITE + "Chunks: " + ChatColor.GOLD
                    + world.getLoadedChunks().length + ChatColor.WHITE + ", Entities: "
                    + ChatColor.GOLD + world.getEntities().size() + ChatColor.GRAY + "]");
        }

        String ticks = ChatColor.GRAY + "TPS" + ChatColor.WHITE + ": ";
        if (TPSTicker.hasTicked())
        {
            sender.sendMessage(ticks + renderTPSBar(TPSTicker.calculateTPS(), TPS_PER_SECOND_THRESHOLD));
        }
        else
        {
            sender.sendMessage(ticks + ChatColor.RED + "No TPS poll yet");
        }
    }

    private String renderBar(final double value, final double max)
    {
        final double usedLevel = BAR_SEGMENTS * (value / max);
        final int usedRounded = (int) Math.round(usedLevel);
        String bar = ChatColor.DARK_GRAY + "[";

        for (int i = 0; i < BAR_SEGMENTS; i++)
        {
            bar += ((i + 1) <= usedRounded) ? ChatColor.AQUA + "#" : ChatColor.GRAY + "_";
        }

        final double percent = (usedLevel / BAR_SEGMENTS);
        final NumberFormat format = NumberFormat.getPercentInstance();

        return (bar + ChatColor.DARK_GRAY + "] (" + ChatColor.WHITE + format.format(percent) + ChatColor.DARK_GRAY + ")");
    }

    //TODO: Should be merged with renderBar
    private String renderTPSBar(final double value, final double max)
    {
        double usedLevel = TPS_PER_SECOND_THRESHOLD * (value / max);
        int usedRounded = (int) Math.round(usedLevel);
        String bar = ChatColor.DARK_GRAY + "[";

        for (int i = 0; i < TPS_PER_SECOND_THRESHOLD; i++)
        {
            if ((i + 1) <= usedRounded)
            {
                bar += ChatColor.AQUA + "#";
            }
            else
            {
                bar += ChatColor.GRAY + "_";
            }
        }

        double percent = (usedLevel / TPS_PER_SECOND_THRESHOLD);

        if (percent > 1)
        {
            percent = 1;
        }

        return (bar + ChatColor.DARK_GRAY + "] (" + ChatColor.WHITE
                + percent * TPS_PER_SECOND_THRESHOLD + " TPS"
                + ChatColor.DARK_GRAY + ")");
    }
}
