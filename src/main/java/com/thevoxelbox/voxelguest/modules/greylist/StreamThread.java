package com.thevoxelbox.voxelguest.modules.greylist;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @deprecated This will be replaced with a new and safer system.
 */
class StreamThread extends Thread
{

    private final GreylistModule module;
    private ServerSocket serverSocket;
    private StreamReader reader;

    public StreamThread(final GreylistModule module)
    {
        this.module = module;
        try
        {
            this.serverSocket = new ServerSocket(module.getConfig().getStreamPort());
        } catch (IOException ex)
        {
            this.serverSocket = null;
            //VoxelGuest.log(name, "Could not bind to port " + streamPort + ". Perhaps it is already in use?", 2);
        }
    }

    public final void killProcesses()
    {
        if (reader != null && reader.getStatus() == 100)
        {
            reader.interrupt();
        }
        this.interrupt();
        try
        {
            serverSocket.close();
        } catch (IOException ex)
        {
            //VoxelGuest.log(name, "Could not release port " + streamPort, 2);
        } catch (NullPointerException ex)
        {
            //VoxelGuest.log(name, "Could not release socket because it is null.", 2);
        }
    }

    @Override
    public final void run()
    {
        if (serverSocket == null)
        {
            return;
        }

        try
        {
            while (true)
            {
                reader = new StreamReader(serverSocket.accept(), module);
                reader.start();
            }

        } catch (IOException ex)
        {
            // Shutting down...
        }
    }
}
