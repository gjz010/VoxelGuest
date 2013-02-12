package com.thevoxelbox.voxelguest.modules.greylist.injector;

import com.thevoxelbox.voxelguest.VoxelGuest;
import com.thevoxelbox.voxelguest.modules.greylist.GreylistModule;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Monofraps
 */
public class SocketListener implements Runnable
{
    private final GreylistModule module;
    private ServerSocket server;

    public SocketListener(int port, final GreylistModule module)
    {
        this.module = module;
        try
        {
            server = new ServerSocket(port);
        } catch (IOException e)
        {
            Bukkit.getLogger().severe("Failed to initialize greylist server.");
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                Socket client = server.accept();
                ClientHandler clientHandler = new ClientHandler(client, module);
                Bukkit.getScheduler().runTaskAsynchronously(VoxelGuest.getPluginInstance(), clientHandler);
            } catch (IOException e)
            {
                Bukkit.getLogger().warning("Failed to accept greylist injection client.");
                e.printStackTrace();
            }
        }
    }
}
