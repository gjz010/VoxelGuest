package com.thevoxelbox.voxelguest.modules.greylist.injector;

import com.thevoxelbox.voxelguest.modules.greylist.GreylistModule;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @author Monofraps
 */
public class ClientHandler implements Runnable
{
    private final Socket socket;
    private final GreylistModule module;

    public ClientHandler(final Socket socket, final GreylistModule module)
    {
        this.socket = socket;
        this.module = module;
    }

    @Override
    public void run()
    {
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            final String authToken = reader.readLine();
            final String greylistee = reader.readLine();

            if (authToken.equals(module.getAuthToken()))
            {
                if (!module.isOnPersistentGreylist(greylistee))
                {
                    module.greylist(greylistee);
                    Bukkit.broadcastMessage(String.format("Added %s to greylist.", greylistee));
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            if (!socket.isClosed())
            {
                socket.close();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
