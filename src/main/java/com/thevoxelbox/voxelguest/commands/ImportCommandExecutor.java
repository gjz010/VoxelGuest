package com.thevoxelbox.voxelguest.commands;

import com.thevoxelbox.voxelguest.modules.asshat.ban.Banlist;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles /vgimport commands.
 * @author Monofraps
 */
public final class ImportCommandExecutor implements TabExecutor
{
    @Override
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] args)
    {
        if (args.length != 1)
        {
            commandSender.sendMessage("Wrong number of parameters.");
            return false;
        }

        switch (args[0])
        {
            case "bans":
            {
                // import bans
                final Banlist banlist = new Banlist();

                final File banfile = new File("plugins/VoxelGuest/banned.txt");
                final Scanner scanner;
                try
                {
                    scanner = new Scanner(new FileInputStream(banfile));
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                    commandSender.sendMessage("Could not find banned.txt file.");
                    return true;
                }

                while (scanner.hasNextLine())
                {
                    final String line = scanner.nextLine();
                    final String[] data = line.split(">");
                    banlist.ban(data[0], data[1]);
                    commandSender.sendMessage("Importet ban: " + data[0]);
                }
                return true;
            }

            default:
                commandSender.sendMessage("Unknown import statement.");
                return false;
        }
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final Command command, final String s, final String[] strings)
    {
        List<String> options = new ArrayList<>();
        options.add("bans");

        return options;
    }
}
