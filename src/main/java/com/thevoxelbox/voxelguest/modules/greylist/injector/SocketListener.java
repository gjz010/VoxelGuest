package com.thevoxelbox.voxelguest.modules.greylist.injector;

import com.thevoxelbox.voxelguest.VoxelGuest;
import com.thevoxelbox.voxelguest.modules.greylist.GreylistModule;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Do not do any further development on this injection system. It will be replaced with a more secure one.
 *
 * @author Monofraps
 * @deprecated
 */
public class SocketListener implements Runnable
{
    private final GreylistModule module;
    private boolean run = true;
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
    public final void run()
    {
        while (run)
        {
            try
            {
                Socket client = server.accept();   // the blocking nature of this might become a problem
                ClientHandler clientHandler = new ClientHandler(client, module);
                Bukkit.getScheduler().runTaskAsynchronously(VoxelGuest.getPluginInstance(), clientHandler);
            } catch (IOException e)
            {
                Bukkit.getLogger().warning("Failed to accept greylist injection client.");
                e.printStackTrace();
            }
        }

        try
        {
            server.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void setRun(final boolean run)
    {
        this.run = run;
    }
}
