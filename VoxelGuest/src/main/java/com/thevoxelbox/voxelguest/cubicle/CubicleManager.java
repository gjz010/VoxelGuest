/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thevoxelbox.voxelguest.cubicle;

import com.google.gson.Gson;
import com.thevoxelbox.voxelguest.CubicleModule;
import com.thevoxelbox.voxelguest.VoxelGuest;
import com.thevoxelbox.voxelguest.permissions.PermissionsManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;

/**
 *
 * @author Piotr <przerwap@gmail.com>
 */
public class CubicleManager {

    private HashMap<String, Cubicle> cubicles = new HashMap<String, Cubicle>();
    private HashMap<String, Cubicle> aliasNames = new HashMap<String, Cubicle>();
    private HashMap<String, Cubicle> playerCubes = new HashMap<String, Cubicle>();

    public CubicleManager() {
        File f = new File("plugins/VoxelGuest/cubicles/data.json");

        if (!f.exists()) {
            f.getParentFile().mkdirs();
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(CubicleManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            Scanner snr = new Scanner(f);

            Gson gson = new Gson();
            while (snr.hasNext()) {
                try {
                    Cubicle cb = gson.fromJson(snr.nextLine(), Cubicle.class);
                    if (cb != null) {
                        cubicles.put(cb.getKey(), cb);
                        if (cb.hasName()) {
                            aliasNames.put(cb.getName(), cb);
                        }
                        if (!cb.getOwner().equals("system.cube")) {
                            playerCubes.put(cb.getOwner(), cb);
                        }
                    }
                } catch (Exception e) {
                    Logger.getLogger(CubicleManager.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CubicleManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean hasCubicle(Location loc) {
        return cubicles.containsKey(getKey(loc));
    }

    public Cubicle getCubicle(Location loc) {
        return cubicles.get(getKey(loc));
    }

    public boolean hasCubicle(Player user) {
        return playerCubes.containsKey(user.getName());
    }

    public Cubicle getCubicle(Player user) {
        return playerCubes.get(user.getName());
    }

    public boolean hasCubicle(String userName) {
        return playerCubes.containsKey(userName);
    }

    public Cubicle getCubicle(String userName) {
        return playerCubes.get(userName);
    }

    public boolean hasAlias(String alias) {
        return aliasNames.containsKey(alias);
    }

    public Cubicle getAlias(String alias) {
        return aliasNames.get(alias);
    }

    public boolean hasCubicle(int x, int z) {
        return cubicles.containsKey(getKey(x, z));
    }

    public Cubicle getCubicle(int x, int z) {
        return cubicles.get(getKey(x, z));
    }

    public void createCubicle(Player user, World cubeWorld, boolean system) {
        if (hasCubicle(user) && !system) {
            user.sendMessage(ChatColor.RED + "You cannot own more than one cubicle!");
            return;
        }

        Cubicle cb = getFirstSpiral();
        if (!system) {
            cb.setOwner(user.getName());
            playerCubes.put(cb.getOwner(), cb);
            PermissionsManager.getHandler().givePermission(user.getName(), cb.getPermissionString());
            cb.teleport(user, cubeWorld);
            user.sendMessage(ChatColor.GOLD + "Your cubicle is ready to use!");
        } else {
            cb.teleport(user, cubeWorld);
            user.sendMessage(ChatColor.GOLD + "System cubicle created!");
        }
        cubicles.put(cb.getKey(), cb);
        saveCubicles();
    }
    
    public void removeCubicle(Player user, Cubicle cb) {
        cb.flushOwners();
        if(cb.isOwnedBy(user)) {
            PermissionsManager.getHandler().removePermission(user.getName(), cb.getPermissionString());
        }
        
        if(cb.hasName()) {
            aliasNames.remove(cb.getName());
        }
        
        playerCubes.remove(cb.getOwner());
        cubicles.remove(cb.getKey());
        saveCubicles();
        user.sendMessage(ChatColor.GOLD + "Cubicle " + cb + ChatColor.GOLD + " has been removed!");
    }

    public Cubicle getFirstSpiral() {
        int x = 0;
        int z = 0;

        int travel = 1;
        int steps = 0;
        int num = 0;
        boolean secondTurn = false;
        Direction dir = Direction.LEFT;

        while (cubicles.containsKey(x + "_" + z)) {
            if (steps < travel) {
                steps++;
                num++;
                switch (dir) {
                    case LEFT:
                        x--;
                        break;
                    case DOWN:
                        z--;
                        break;
                    case RIGHT:
                        x++;
                        break;
                    case UP:
                        z++;
                        break;
                }
            }

            if (steps == travel) {
                steps = 0;
                if (secondTurn) {
                    secondTurn = false;
                    travel++;
                    dir = dir.next();
                } else {
                    secondTurn = true;
                }
            }
        }
        return createNew(x, z, num);
    }

    public Cubicle getSpiralNumber(int number) {
        if (number < 0) {
            return null;
        }
        int x = 0;
        int z = 0;

        int travel = 1;
        int steps = 0;
        int num = 0;
        boolean secondTurn = false;
        Direction dir = Direction.LEFT;

        while (num < number) {
            if (steps < travel) {
                steps++;
                num++;
                switch (dir) {
                    case LEFT:
                        x--;
                        break;
                    case DOWN:
                        z--;
                        break;
                    case RIGHT:
                        x++;
                        break;
                    case UP:
                        z++;
                        break;
                }
            }

            if (steps == travel) {
                steps = 0;
                if (secondTurn) {
                    secondTurn = false;
                    travel++;
                    dir = dir.next();
                } else {
                    secondTurn = true;
                }
            }
        }
        return cubicles.get(getKey(x, z));
    }

    public void addAlias(Cubicle cb, String alias, Player cmdSender) {
        if (aliasNames.containsKey(alias)) {
            cmdSender.sendMessage(ChatColor.RED + "A Cubicle with that alias already exists!");
        } else {
            if (cb.hasName()) {
                aliasNames.remove(cb.getName());
            }
            cb.setName(alias);
            aliasNames.put(alias, cb);
            cmdSender.sendMessage(new String[]{ChatColor.GREEN + "Alias name \"" + ChatColor.GRAY + alias + ChatColor.GREEN + "\" has been registered for",
                        ChatColor.GREEN + "Cubicle " + ChatColor.GOLD + cb.getKey() + ChatColor.GREEN + " owned by: " + ChatColor.BLUE + cb.getOwner()});
        }
    }
    
    public void regenerateCubicle(Cubicle cb, World cubeWorld, Player user) {
        RegenerationRunner rr = new RegenerationRunner(cb, user, cubeWorld);
        rr.setID(Bukkit.getScheduler().scheduleSyncRepeatingTask(VoxelGuest.getInstance(), rr, CubicleModule.CUBICLE_REGEN_TIME, CubicleModule.CUBICLE_REGEN_TIME));
    }

    public void saveCubicles() {
        File f = new File("plugins/VoxelGuest/cubicles/data.json");

        if (!f.exists()) {
            f.getParentFile().mkdirs();
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(CubicleManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            PrintWriter pw = new PrintWriter(f);

            Gson gson = new Gson();
            for (Cubicle cb : cubicles.values()) {
                pw.println(gson.toJson(cb, Cubicle.class));
            }
            pw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CubicleManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Cubicle createNew(int x, int z, int id) {
        Cubicle cube = new Cubicle();
        cube.setID(id);
        cubicles.put(getKey(x, z), cube);
        return cube;
    }

    private String getKey(int x, int z) {
        return x + "_" + z;
    }

    private String getKey(Location loc) {
        return NumberConversions.floor(loc.getX() / CubicleModule.CUBICLE_SIZE)
                + "_"
                + NumberConversions.floor(loc.getZ() / CubicleModule.CUBICLE_SIZE);
    }

    private enum Direction {

        LEFT(0, 1),
        DOWN(1, 2),
        RIGHT(2, 3),
        UP(3, 0);
        private byte dir;
        private byte nextDir;

        private Direction(int d, int n) {
            dir = (byte) d;
            nextDir = (byte) n;
        }

        public Direction next() {
            return values()[nextDir];
        }
    }
}
