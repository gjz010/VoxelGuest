package com.thevoxelbox.voxelguest.modules.greylist;

import java.io.IOException;
import java.net.ServerSocket;

class StreamThread extends Thread {

    private final GreylistModule module;
    private ServerSocket serverSocket;
    private StreamReader reader;

    public StreamThread(GreylistModule module)
    {
        this.module = module;
        try {
            this.serverSocket = new ServerSocket(module.getConfig().getStreamPort());
        } catch (IOException ex) {
            this.serverSocket = null;
            //VoxelGuest.log(name, "Could not bind to port " + streamPort + ". Perhaps it is already in use?", 2);
        }
    }

    public void killProcesses()
    {
        if (reader != null && reader.getStatus() == 100)
        {
            reader.interrupt();
        }
        this.interrupt();
        try
        {
            serverSocket.close();
        }
        catch (IOException ex)
        {
            //VoxelGuest.log(name, "Could not release port " + streamPort, 2);
        } catch (NullPointerException ex) {
            //VoxelGuest.log(name, "Could not release socket because it is null.", 2);
        }
    }
    @Override
    public void run()
    {
        if (serverSocket == null)
        {
            return;
        }

        try {
            while (true)
            {
                reader = new StreamReader(serverSocket.accept(), module);
                reader.start();
            }

        } catch (IOException ex) {
            // Shutting down...
        }
    }
}