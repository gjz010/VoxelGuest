package com.thevoxelbox.voxelguest.modules.greylist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * @deprecated This will be replaced with a new and safer system.
 */
class StreamReader extends Thread {

    private final Socket socket;
    private int status = -1;
    private final GreylistModule module;

    public StreamReader(Socket socket, GreylistModule module)
    {
        this.module = module;
        this.socket = socket;
    }

    public int getStatus()
    {
        // -1 : Not yet called
        // 100: In process
        // 200: Exited with no error
        // 201: Exited for no greylist to add
        // 202: Exited with socket being null
        // 222: Exited with error

        return this.status;
    }

    @Override
    public void run()
    {
        status = 100;
        try {
            //VoxelGuest.log(name, "Accepted client on port " + streamPort, 0);
            List<String> list = this.readSocket(socket);
            this.socket.close();

            if (list == null || list.isEmpty()) {
                status = 201;
                return;
            }

            for (String name : list)
            {
                this.module.greylist(name);
                Bukkit.broadcastMessage(ChatColor.GRAY + name + ChatColor.DARK_GRAY + " was added to the greylist.");
            }

        } catch (IOException ex) {
            //VoxelGuest.log(name, "Could not close client stream socket", 2);
            status = 222;
        }
    }

    private synchronized List<String> readSocket(Socket socket)
    {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            List<String> list = new ArrayList<String>();
            String line = null;

            while ((line = in.readLine()) != null) {
                String toAdd = interpretStreamInput(line);

                if (toAdd != null) {
                    if (!list.contains(toAdd)) {
                        list.add(toAdd);
                    }
                }

                out.println(line);
            }

            in.close();
            out.close();
            socket.close();
            return list;
        } catch (SocketException ex) {
            //VoxelGuest.log(name, "Stream closed while reading stream", 1);
            return null;
        } catch (IOException ex) {
            return null;
        }
    }

    private String interpretStreamInput(String input)
    {
        String[] args = input.split("\\:");

        if (args[0].equals(this.module.getConfig().getStreamPasswordHash())) {
            String user = args[1];
            boolean accepted = Boolean.parseBoolean(args[2]);

            if (accepted) {
                return user;
            }
        }

        return null;
    }
}
