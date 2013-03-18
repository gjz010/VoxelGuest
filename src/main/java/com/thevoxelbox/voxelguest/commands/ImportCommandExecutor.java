package com.thevoxelbox.voxelguest.commands;

import com.thevoxelbox.voxelguest.modules.asshat.ban.Banlist;
import com.thevoxelbox.voxelguest.modules.general.AfkMessage;
import com.thevoxelbox.voxelguest.modules.greylist.GreylistHelper;
import com.thevoxelbox.voxelguest.persistence.Persistence;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Scanner;

/**
 * Handles /vgimport commands.
 * @author Monofraps
 * @author TheCryoknight
 */
public final class ImportCommandExecutor implements TabExecutor
{
    private static final String[] options = {"bans", "greylist", "afkmessages"};

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String s, final String[] args)
    {
        if (args.length != 1)
        {
            sender.sendMessage("Wrong number of parameters.");
            return false;
        }

        switch (args[0].toLowerCase())
        {
            case "bans":
            {
                // Import bans from VG3 and VG4
                final Banlist banlist = new Banlist();
                this.importVG3Bans(banlist, sender);
                this.importVG4Bans(banlist, sender);
                sender.sendMessage(ChatColor.GRAY + "Banlist import compleated!");
                return true;
            }

            case "afkmessages":
            {
                // Import Afk messages from VG3
                this.importAfkMessages(sender);
                sender.sendMessage(ChatColor.GRAY + "Afk message import compleated!");
                return true;
            }

            case "greylist":
            {
                this.importGreylist(sender);
                sender.sendMessage(ChatColor.GRAY + "greylist import compleated!");
            }

            default:
            {
                sender.sendMessage("Unknown import statement.");
                return false;
            }
        }
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args)
    {
        List<String> matches = new ArrayList<>();
        if (args.length >= 1)
        {
            for (String opt : options)
            {
                if (opt.startsWith(args[0].toLowerCase()))
                {
                    matches.add(opt);
                }
            }
        }
        else
        {
            matches.addAll(Arrays.asList(options));
        }
        return matches;
    }

    /**
     * Imports all bans from VoxelGuest version 4.
     * If a player is already banned it will do nothing.
     * These are the bans stored at "plugins/VoxelGuest/asshatmitigation/banned.properties".
     *
     * @param banlist Banlist helper from the asshat module
     * @param sender User running the command
     */
    public void importVG4Bans(final Banlist banlist, final CommandSender sender)
    {
        final Properties properties = new Properties();
        final File banfileVG4 = new File("plugins" + File.separator + "VoxelGuest" + File.separator + "asshatmitigation" + File.separator + "banned.properties");
        try
        {
            properties.load(new FileInputStream(banfileVG4));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        for (Entry<Object, Object> ban : properties.entrySet())
        {
            if ((ban.getKey() instanceof String) && (ban.getValue() instanceof String))
            {
                final String bannedName = (String) ban.getKey();
                final String banReason = (String) ban.getValue();
                if (!banlist.isPlayerBanned(bannedName))
                {
                    banlist.ban(bannedName, banReason);
                }
            }
        }
    }

    /**
     * Imports all bans from VoxelGuest version 3.
     * If a player is already banned it will do nothing.
     * These are the bans stored at "plugins\VoxelGuest\banned.txt".
     *
     * @param banlist Banlist helper from the asshat module
     * @param sender User running the command
     */
    public void importVG3Bans(final Banlist banlist, final CommandSender sender)
    {
        final File banfileVG3 = new File("plugins" + File.separator + "VoxelGuest" + File.separator + "banned.txt");
        final Scanner scanner;
        try
        {
            scanner = new Scanner(new FileInputStream(banfileVG3));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            sender.sendMessage("Could not find banned.txt file.");
            return;
        }

        try
        {
            while (scanner.hasNextLine())
            {
                final String line = scanner.nextLine();
                final String[] data = line.split(">");
                if (!banlist.isPlayerBanned(data[0]))
                {
                    banlist.ban(data[0], data[1]);
                }
                sender.sendMessage(ChatColor.DARK_AQUA + "Imported ban: " + ChatColor.GOLD + data[0]);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            scanner.close();
        }
    }

    /**
     * Imports all random afk messages from VoxelGuest 3.
     *
     * @param sender Sender to inform of imports.
     */
    public void importAfkMessages(final CommandSender sender)
    {
        final File msgFile = new File("plugins" + File.separator + "VoxelGuest" + File.separator + "afkmsg.txt");
        final Scanner scan;
        try
        {
            scan = new Scanner(new FileInputStream(msgFile));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            sender.sendMessage("Could not find afkmsg.txt file.");
            return;
        }

        try
        {
            while (scan.hasNextLine())
            {
                final String line = scan.nextLine();
                Persistence.getInstance().save(new AfkMessage(line));
                sender.sendMessage(ChatColor.DARK_AQUA + "Imported message: " + ChatColor.GRAY + line);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            scan.close();
        }
        return;
    }

    /**
     * Imports all greylists from prior versions of VoxelGuest.
     *
     * @param sender Sender to inform of imports.
     */
    public void importGreylist(final CommandSender sender)
    {
        final GreylistHelper greylistHelper = new GreylistHelper();
        final File msgFile = new File("plugins" + File.separator + "VoxelGuest" + File.separator + "greylist.txt");
        final Scanner scan;
        try
        {
            scan = new Scanner(new FileInputStream(msgFile));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            sender.sendMessage("Could not find greylist.txt file.");
            return;
        }

        try
        {
            while (scan.hasNextLine())
            {
                final String name = scan.nextLine();
                if (!greylistHelper.isOnPersistentGreylist(name))
                {
                    greylistHelper.greylist(name);
                    sender.sendMessage(ChatColor.DARK_AQUA + "Imported greylistee: " + ChatColor.GRAY + name);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            scan.close();
        }
        return;
    }
}
