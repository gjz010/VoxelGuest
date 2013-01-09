/*
 * Notification.java
 *
 * Project: libpsanker
 *
 * Copyright (C) Patrick Anker 2011 - 2012. All rights reserved.
 * 
 * libpsanker by Patrick Anker is licensed under a Creative Commons 
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 *
 */

package com.thevoxelbox.voxelguest.management;

import java.util.Arrays;
import java.util.Random;
import org.bukkit.plugin.Plugin;


public final class Notification {
    
    private final String UUID;
    private final String message;
    private final String target;
    private final Plugin caller;
    
    private String sessionId;
    
    private NotificationProperty[] properties;
    
    public Notification(String uuid, Plugin plugin, String msg, String t, NotificationProperty... props)
    {
        UUID = uuid;
        message = msg;
        target = t;
        
        caller = plugin;
        
        properties = props;
        
        sessionId = renderSessionId();
    }
    
    public Notification(String session, String uuid, Plugin plugin, String msg, String t, NotificationProperty... props)
    {
        UUID = uuid;
        message = msg;
        target = t;
        
        caller = plugin;
        
        properties = props;
        
        sessionId = session;
    }
    
    public String getUUID()
    {
        return UUID;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public String getTarget()
    {
        return target;
    }
    
    public Plugin getCaller()
    {
        return caller;
    }
    
    public NotificationProperty[] getProperties()
    {
        return properties;
    }
    
    protected void setProperties(NotificationProperty[] props)
    {
        properties = props;
    }
    
    public String getSessionId()
    {
        return sessionId;
    }
    
    private String renderSessionId()
    {
        String sample = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        char[] key = new char[6];
        Random rand = new Random();

        for (int i = 0; i < 6; i++) {
            key[i] = sample.charAt(rand.nextInt(sample.length()));
        }

        String id = new String(key);
        return id;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj.getClass() != Notification.class)
            return false;
        
        if (!(obj instanceof Notification))
            return false;
        
        Notification note = (Notification) obj;
        
        if (!note.UUID.equals(this.UUID))
            return false;
        
        if (!note.message.equals(this.message))
            return false;
        
        if (!note.caller.getName().equals(this.caller.getName()))
            return false;
        
        if (!note.sessionId.equals(this.sessionId))
            return false;
        
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 61 * hash + (this.UUID != null ? this.UUID.hashCode() : 0);
        hash = 61 * hash + (this.message != null ? this.message.hashCode() : 0);
        hash = 61 * hash + (this.target != null ? this.target.hashCode() : 0);
        hash = 61 * hash + (this.caller != null ? this.caller.hashCode() : 0);
        hash = 61 * hash + (this.sessionId != null ? this.sessionId.hashCode() : 0);
        hash = 61 * hash + Arrays.deepHashCode(this.properties);
        return hash;
    }
    
    public enum NotificationProperty {
        COPY("copy"),
        SINGLE("single"),
        WEAK("weak"),
        RETAIN("retain"),
        STRONG("strong");
        
        private final String property;
        
        NotificationProperty(String prop)
        {
            property = prop;
        }
        
        public String getString()
        {
            return property;
        }
        
        public static NotificationProperty getPropertyForKey(String key)
        {
            for (NotificationProperty prop : NotificationProperty.values()) {
                if (prop.getString().equalsIgnoreCase(key))
                    return prop;
            }
            
            return null;
        }
    }
}
