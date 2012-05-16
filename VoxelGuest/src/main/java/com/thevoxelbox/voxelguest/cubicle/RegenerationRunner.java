/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thevoxelbox.voxelguest.cubicle;

import com.thevoxelbox.voxelguest.CubicleModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author Piotr <przerwap@gmail.com>
 */
public class RegenerationRunner implements Runnable {

    private int lowx;
    private int lowz;
    private int highx;
    private int highz;
    private int regenCount = 0;
    private int regenX;
    private int regenZ;
    private int maxChunk;
    private World cubeWorld;
    private Player user;
    private Cubicle cube;
    private int id;
    private boolean layerDone = false;

    public RegenerationRunner(Cubicle cb, Player cmdSender, World cWorld) {
        cubeWorld = cWorld;
        user = cmdSender;
        cube = cb;
        int otherx = (cb.getX() * CubicleModule.CUBICLE_SIZE) + (int) (Math.signum(cb.getX()) * (CubicleModule.CUBICLE_SIZE));
        int otherz = (cb.getZ() * CubicleModule.CUBICLE_SIZE) + (int) (Math.signum(cb.getZ()) * (CubicleModule.CUBICLE_SIZE));
        Chunk ch1 = cubeWorld.getChunkAt(cubeWorld.getBlockAt(otherx, 64, otherz));
        Chunk ch2 = cubeWorld.getChunkAt(cubeWorld.getBlockAt((cb.getX() * CubicleModule.CUBICLE_SIZE), 64, (cb.getZ() * CubicleModule.CUBICLE_SIZE)));
        lowx = ch1.getX() < ch2.getX() ? ch1.getX() : ch2.getX();
        lowz = ch1.getZ() < ch2.getZ() ? ch1.getZ() : ch2.getZ();
        highx = ch1.getX() > ch2.getX() ? ch1.getX() : ch2.getX();
        highz = ch1.getZ() > ch2.getZ() ? ch1.getZ() : ch2.getZ();
        regenX = lowx;
        regenZ = lowz;
        maxChunk = (CubicleModule.CUBICLE_SIZE / 16) * (CubicleModule.CUBICLE_SIZE / 16);
        user.sendMessage(ChatColor.GOLD + "Regeneration of Cubicle " + cube + ChatColor.GOLD + " has begun!");
    }

    public void setID(int myId) {
        id = myId;
    }

    @Override
    public void run() {
        if (regenX < highx && regenZ < highz) {
            if (layerDone) {
                regenX = lowx;
                regenZ++;
                layerDone = false;
            } else {
                regenX++;
            }

            if (regenX == highx) {
                layerDone = true;
            }

            regenCount++;
            cubeWorld.regenerateChunk(regenX, regenZ);

            if (regenCount % (maxChunk / 4) == 0) {
                switch ((int) regenCount / (maxChunk / 4)) {
                    case 1:
                        percent(25);
                        break;

                    case 2:
                        percent(50);
                        break;

                    case 3:
                        percent(75);
                        break;

                    case 4:
                        percent(100);
                        break;
                }
            }
        } else {
            user.sendMessage(ChatColor.GOLD + "Regeneration of Cubicle " + cube + ChatColor.GOLD + " has finished!");
            Bukkit.getScheduler().cancelTask(id);
        }
    }

    private void percent(int percent) {
        user.sendMessage(ChatColor.GREEN + "Regeneration in progress ... " + ChatColor.GOLD + percent + ChatColor.GREEN + "% complete.");
    }
}
