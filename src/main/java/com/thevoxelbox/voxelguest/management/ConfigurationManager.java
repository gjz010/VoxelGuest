/*
 * Copyright (C) 2011 - 2012, psanker and contributors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are 
 * permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice, this list of 
 *   conditions and the following 
 * * Redistributions in binary form must reproduce the above copyright notice, this list of 
 *   conditions and the following disclaimer in the documentation and/or other materials 
 *   provided with the distribution.
 * * Neither the name of The VoxelPlugineering Team nor the names of its contributors may be 
 *   used to endorse or promote products derived from this software without specific prior 
 *   written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS 
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE 
 * COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR 
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.thevoxelbox.voxelguest.management;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.bukkit.Bukkit;

import com.thevoxelbox.voxelguest.modules.Setting;

public class ConfigurationManager {

    protected HashMap<String, Object> config = new HashMap<String, Object>();
    public static final String directory = "plugins";
    private String target;
    private String destination;
    public ConfigurationManager() {
    	
    }

    public ConfigurationManager(String name)
    {
        config = (HashMap<String, Object>) load(name, "/VoxelGuest");
    }

    public HashMap<String, Object> getAllEntries()
    {
        return config;
    }

    public Object getEntry(String key)
    {
    	if (config == null)
            return null;
        else if (!config.containsKey(key))
            return null;
        else
            return config.get(key);
    }
    public void removeEntry(String key) 
    {
        config.remove(key);
    }
    public boolean hasEntry(String key) {
        return config.containsKey(key);
    }
    public boolean isEmpty() {
    	return config.isEmpty();
    }

    public String getString(String key) {
        if (config == null)
            return null;
        else if (!config.containsKey(key))
            return null;
        else if (!(config.get(key) instanceof String))
            return null;
        else {
            String v = config.get(key).toString();
            v = Formatter.encodeColors(v);
            config.put(key, v); // Since Java props actually store \u00a7
            
            return config.get(key).toString();
        }
    }
    
    public boolean getBoolean(String key) {
        if (config == null)
            return false;
        else if (!config.containsKey(key))
            return false;
        else if (!(config.get(key) instanceof Boolean))
            return false;
        else
            return ((Boolean) config.get(key)).booleanValue();
    }
    
    public int getInt(String key) {
        if (config == null)
            return -1;
        else if (!config.containsKey(key))
            return -1;
        else if (!(config.get(key) instanceof Integer))
            return -1;
        else
            return ((Integer) config.get(key)).intValue();
    }
    
    public double getDouble(String key) {
        if (config == null)
            return -1;
        else if (!config.containsKey(key))
            return -1;
        else if (!(config.get(key) instanceof Double))
            return -1;
        else
            return ((Double) config.get(key)).doubleValue();
    }
    
    public void setEntry(String key, Object value) {
        if (config == null)
            return;
        
        config.put(key, value);
    }
    
    public void setString(String key, String value) {
        if (config == null)
            return;
        
        config.put(key, value);
    }
    
    public void setBoolean(String key, boolean value) {
        if (config == null)
            return;
        
        config.put(key, Boolean.valueOf(value));
    }
    
    public void setInt(String key, int value) {
        if (config == null)
            return;
        
        config.put(key, Integer.valueOf(value));
    }
    
    public void setDouble(String key, double value) {
        if (config == null)
            return;
        
        config.put(key, Double.valueOf(value));
    }
    
    public void assignTarget(String target) 
    {
        this.target = target;
    }
    
    public void assignDestination(String dest) 
    {
        this.destination = dest;
    }

    public void load()
    {
    	if(target == null || destination == null) return;
        registerFieldSettings(this.getClass());
        HashMap<String, Object> data = (HashMap<String, Object>) load(target, destination);
        
        if (data == null)
            return;
        else {
            // Populate map
            
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (entry.getValue() != null)
                    setEntry(entry.getKey(), entry.getValue());
            }
        }
    }

    /*public void reset()
    {
        registerFieldSettings(getClass());
    }*/

    public void save()
    {
    	if (config == null || config.isEmpty()) {
            clear();
        }
    	
    	save(target, config, destination);
    }
    
    public void clear()
    {
    	if (!config.isEmpty())
    		config.clear();
        
        File f = new File("plugins" + destination + "/" + target + ".properties");
        f.delete();
        
        try {
            f.createNewFile();
        } catch (IOException ex) {
            // Oh well.
        }
    }

    private void registerFieldSettings(Class<? extends ConfigurationManager> cls)
    {
        for (Field field : cls.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                registerFieldSetting(field);
            } catch (IllegalArgumentException ex) {
                continue;
            } catch (IllegalAccessException ex) {
                continue;
            }
        }
    }

    private void registerFieldSetting(Field field) throws IllegalArgumentException,
            IllegalAccessException
    {

        if (!field.isAnnotationPresent(Setting.class)) {
            return;
        }

        if (!field.isAccessible()) {
            field.setAccessible(true);
        }

        Setting setting = field.getAnnotation(Setting.class);
        String key = setting.value();
        Object value = field.get(this);

        setEntry(key, value);
    }
    
    public static Map<String, Object> load(String target, String destination) {
    	if(target == null || destination == null) return null;
    	if(target.startsWith("/")) target = target.substring(1);
        Map<String, Object> map = new HashMap<String, Object>();
        map.clear();

        File f = new File(directory + destination + "/" + target + ".properties");
        FileInputStream fi = null;

        if (f.exists()) {
            try {
                Properties props = new Properties();
                fi = new FileInputStream(f);

                props.load(fi);

                for (Map.Entry<Object, Object> entry : props.entrySet()) {
                    String key = entry.getKey().toString();

                    try {
                        if (entry.getValue().toString().contains(".") || (Double.parseDouble(entry.getValue().toString()) > 2147483647 || Double.parseDouble(entry.getValue().toString()) < -2147483648)) {
                            Double d = Double.parseDouble(entry.getValue().toString());
                            map.put(key, d);
                            continue;
                        }
                        
                        Integer i = Integer.parseInt(entry.getValue().toString());
                        map.put(key, i);
                    } catch (NumberFormatException ex) {
                        if (entry.getValue().toString().equals(Boolean.TRUE.toString()) || entry.getValue().toString().equals(Boolean.FALSE.toString())) {
                            Boolean bool = Boolean.parseBoolean(entry.getValue().toString());
                            map.put(key, bool);
                            continue;
                        }

                        map.put(key, entry.getValue().toString());
                        continue;
                    }
                }
            } catch (FileNotFoundException ex) {
            	Bukkit.getLogger().warning("[VoxelGuest] File not found: " + f.getAbsolutePath());
            } catch (IOException ex) {
            	Bukkit.getLogger().warning("[VoxelGuest] Incorrectly loaded properties from " + f.getAbsolutePath());
            } finally {
                try {
                    if (fi != null) {
                        fi.close();
                    }
                } catch (IOException ex) {
                	Bukkit.getLogger().severe("##### -- FATAL ERROR -- ##### Failed to store data to " + f.getAbsolutePath());
                    ex.printStackTrace();
                }
            }
        }
        return map;
    }
    public static void save(String target, Map<String, Object> data, String destination) {

    	if(target == null || destination == null) return;
        File f = new File(directory + destination + "/" + target + ".properties");
        FileOutputStream fo = null;

        try {
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                f.createNewFile();
            }

            Properties props = new Properties();
            fo = new FileOutputStream(f);

            for (Map.Entry<String, Object> entry : data.entrySet()) {
                String key = entry.getKey();

                props.setProperty(key, entry.getValue().toString());
            }

            props.store(fo, null);
        } catch (IOException ex) {
        	Bukkit.getLogger().warning("[VoxelGuest] Could not create file " + f.getAbsolutePath());
        } finally {
            try {
                if (fo != null) {
                    fo.close();
                }
            } catch (IOException ex) {
            	Bukkit.getLogger().severe("##### -- FATAL ERROR -- ##### Failed to store data to " + f.getAbsolutePath());
                ex.printStackTrace();
            }
        }
    }
}
