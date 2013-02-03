package com.thevoxelbox.voxelguest.modules.general.command;

import java.lang.management.ManagementFactory;
import java.text.NumberFormat;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import com.google.common.base.Preconditions;
import com.thevoxelbox.voxelguest.modules.general.GeneralModule;
import com.thevoxelbox.voxelguest.modules.general.TPSTicker;

public class SystemCommandExecutor implements CommandExecutor 
{
    @SuppressWarnings("unused")
    private GeneralModule module;

    public SystemCommandExecutor(final GeneralModule generalModule) {
        this.module = generalModule;
    }

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
        else if (args.length == 1)
        {
            if (args[0].equalsIgnoreCase("gc")) {
                System.gc();
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
        double rawCPUUsage = ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
        sender.sendMessage("§7CPU Usage§f: " + renderBar(rawCPUUsage, (Runtime.getRuntime().availableProcessors())));
        sender.sendMessage("§7Available cores§f: §a" + Runtime.getRuntime().availableProcessors());

        sender.sendMessage("§8==============================");
        sender.sendMessage("§bMemory Specs");
        double memUsed = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576;
        double memMax = Runtime.getRuntime().maxMemory() / 1048576;

        sender.sendMessage("§7JVM Memory§f: " + renderBar(memUsed, memMax));
        sender.sendMessage("§7JVM Heap Memory (MB)§f: §a" + ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / 1048576);
        sender.sendMessage("§7JVM Free Memory (MB)§f: §a" + Runtime.getRuntime().freeMemory() / 1048576);
        sender.sendMessage("§7JVM Maximum Memory (MB)§f: §a" + ((Runtime.getRuntime().maxMemory() == Long.MAX_VALUE) ? "No defined limit" : Runtime.getRuntime().maxMemory() / 1048576));
        sender.sendMessage("§7JVM Used Memory (MB)§f: §a" + Runtime.getRuntime().totalMemory() / 1048576);

        sender.sendMessage("§8==============================");
        sender.sendMessage("§bBukkit Specs");

        World[] loadedWorlds = new World[Bukkit.getWorlds().size()];
        loadedWorlds = Bukkit.getWorlds().toArray(loadedWorlds);

        sender.sendMessage("§7Loaded Worlds§f:");

        for (World world : loadedWorlds) {
            Chunk[] chunks = world.getLoadedChunks();

            Entity[] entities = new Entity[world.getEntities().size()];
            entities = world.getEntities().toArray(entities);

            sender.sendMessage("§a- " + world.getName() + " §7[§fChunks: §6" + chunks.length + "§f, Entities: §6" + entities.length + "§7]");
        }

        String ticks = "§7TPS§f: ";
        if (TPSTicker.hasTicked())
        {
            sender.sendMessage(ticks + renderTPSBar(TPSTicker.calculateTPS(), 20));
        }
        else
        {
            sender.sendMessage(ticks + "§cNo TPS poll yet");
        }
    }

    private String renderBar(double value, double max)
    {
        double usedLevel = 20 * (value / max);
        int usedRounded = (int) Math.round(usedLevel);
        String bar = "§8[";

        for (int i = 0; i < 20; i++)
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

        double percent = (usedLevel / 20);
        NumberFormat format = NumberFormat.getPercentInstance();

        return (bar + "§8] (§f" + format.format(percent) + "§8)");
    }

    private String renderTPSBar(double value, double max)
    {
        double usedLevel = 20 * (value / max);
        int usedRounded = (int) Math.round(usedLevel);
        String bar = "§8[";

        for (int i = 0; i < 20; i++)
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

        double percent = (usedLevel / 20);

        if (percent > 1)
        {
            percent = 1;
        }

        return (bar + "§8] (§f" + percent * 20 + " TPS§8)");
    }
}
