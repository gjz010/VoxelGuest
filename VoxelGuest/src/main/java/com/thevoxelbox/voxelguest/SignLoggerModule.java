package com.thevoxelbox.voxelguest;

import com.thevoxelbox.voxelguest.modules.BukkitEventWrapper;
import com.thevoxelbox.voxelguest.modules.MetaData;
import com.thevoxelbox.voxelguest.modules.Module;
import com.thevoxelbox.voxelguest.modules.ModuleEvent;
import com.thevoxelbox.voxelguest.modules.ModuleException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 *
 * @author thedeadlybutter
 */

@MetaData(name="SignLogger", description="Logs all signs")
public class SignLoggerModule extends Module {

    protected List<String> signLog = new ArrayList<String>();
    
    public SignLoggerModule() {
        super(SignLoggerModule.class.getAnnotation(MetaData.class));
    }
    
    @Override
    public void enable() throws ModuleException {
        File directory = new File("plugins/VoxelGuest/signlogger/Log.txt");
        
        if(!directory.exists())
            directory.mkdir();
            Bukkit.getLogger().info("SignLogger file does not exist thus it was created.");
            
            try {
               Scanner scanner = new Scanner(directory);
               
               while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    signLog.add(line);
               }
               
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            
        
    }

    @Override
    public String getLoadMessage() {
        return "SignLogger Module enabled";
    }

    @Override
    public void disable() throws ModuleException {
        File directory = new File("plugins/VoxelGuest/signlogger/Log.txt");
        
        if(!directory.exists())
            directory.mkdir();
            Bukkit.getLogger().info("SignLogger file does not exist thus it was created.");
        
        try{
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(directory));
            
            for(String logLine : signLog){
                writer.write(logLine);
                writer.newLine();
            }
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    @ModuleEvent(event=BlockPlaceEvent.class)
    public void onBlockPlace(BukkitEventWrapper wrapper) {
        
        BlockPlaceEvent event = (BlockPlaceEvent) wrapper.getEvent();
        
        if(event.getBlockPlaced().getTypeId() == 323){
            String pname = event.getPlayer().getName();
            Sign placedSign = (Sign) event.getBlockPlaced();
            String line1 = placedSign.getLine(0);
            String line2 = placedSign.getLine(1);
            String line3 = placedSign.getLine(2);
            String line4 = placedSign.getLine(3);
            int xCord = event.getBlock().getX();
            int yCord = event.getBlock().getY();
            int zCord = event.getBlock().getZ();
            World world = event.getBlock().getWorld();
            
            String logString = getLogString(pname, line1, line2, line3, line4, xCord, yCord, zCord, world);
            addSignLog(logString);
        }
        
        
    }
    
    
    public void addSignLog(String logString){
        signLog.add(logString);
    }
    
    private String getLogString(String pname, String line1, String line2, String line3, String line4, int xCord, int yCord, int zCord, World world){
        Calendar Current = Calendar.getInstance();
                int MO = Current.get(Calendar.MONTH);
                int D = Current.get(Calendar.DAY_OF_MONTH);
                int H = Current.get(Calendar.HOUR_OF_DAY);
                int M = Current.get(Calendar.MINUTE);
                int S = Current.get(Calendar.SECOND);
        return "[" + MO + "/" + D + "/" + H + ":" + M + ":" + S + "]" + " | " + pname + " | " + "'" + line1 + "' " + "'" + line2 + "' " + line3 + "'" + line4 + "'" + " | " + "( " + xCord + "," + yCord + "," + zCord + ", " + world.getName() + " )";        
    }
    
    
    
}

//String format: [time stamp]|Name|"Messages"|(x,y,z, world)