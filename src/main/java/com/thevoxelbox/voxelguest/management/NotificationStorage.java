/*
 * NotificationStorage.java
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.thevoxelbox.voxelguest.VoxelGuest;


public class NotificationStorage {
    private final SQLDriver driver;
    private final String TABLE = "notification_centre_storage";
    
    public NotificationStorage(SQLDriver sqlDriver)
    {
        this.driver = sqlDriver;
    }
    
    public void save(Notification[] notifications)
    {
        if (!driver.checkTable(TABLE)) {
            driver.createTable("CREATE TABLE " + TABLE + "(" +
                    "UUID varchar(255)," +
                    "Message varchar(255)," +
                    "Target varchar(255)," +
                    "Caller varchar(255)," +
                    "Properties varchar(255)," +
                    "Listeners varchar(1023)," +
                    "Session varchar(16)" +
                    ")");
        } else {
            driver.clearTable(TABLE);
        }
        
        String _uuid;
        String _message;
        String _target;
        String _caller;
        String _properties;
        String _listeners;
        String _session;
        
        for (Notification note : notifications) {
            _uuid = note.getUUID();
            _message = note.getMessage();
            _target = note.getTarget();
            
            if (_target == null)
                _target = driver.getNullString();
            
            _caller = note.getCaller().getName();
            _properties = "";
            
            for (Notification.NotificationProperty prop : note.getProperties()) {
                _properties = _properties + prop.getString() + driver.getListDivider();
            }
            
            _listeners = "";
            
            List<String> l = NotificationCentre.sharedCentre().getListenersForNotification(note.getUUID());
            
            if (l == null) {
                VoxelGuest.log("Invalid number of listeners (0) in called Notification \"" + _uuid + "\"", 2);
                continue;
            } else {
                for (String str : l) {
                    _listeners = _listeners + str + driver.getListDivider();
                }
            }
            
            _session = note.getSessionId();
            
            try {
                Statement statement = null;
                ResultSet result = null;

                try {
                    driver.getConnection();
                    
                    // Check for duplicate values
                    statement = driver.getStatement();
                    result = statement.executeQuery("SELECT * FROM " + TABLE + "WHERE Session='" + _session + "'");
                    
                    boolean dupe = false;
                    
                    while (result.next()) {
                        dupe = true;
                        break;
                    }
                    
                    if (!dupe) {
                        statement.execute("INSERT INTO " + TABLE + " VALUES (" +
                                        "'" + _uuid + "', " +
                                        "'" + _message + "', " +
                                        "'" + _target + "', " +
                                        "'" + _caller + "', " +
                                        "'" + _properties + "', " +
                                        "'" + _listeners + "', " +
                                        "'" + _session + "')");
                    }
                    
                } catch (SQLException ex) {
                    VoxelGuest.log("SQLException caught in MySQLNotificationStorage.save(): " + ex.getMessage());
                } finally {
                    driver.release();
                    
                    if (result != null) {
                        result.close();
                    }
                }
            } catch (SQLException ex) {
            	VoxelGuest.log("SQLException caught in MySQLNotificationStorage.save(): " + ex.getMessage());
            }
        }
    }
    public NotificationStoragePacket[] load()
    {
        if (!driver.checkTable(TABLE)) {
            driver.createTable("CREATE TABLE " + TABLE + "(" +
                    "UUID varchar(255)," +
                    "Message varchar(255)," +
                    "Target varchar(255)," +
                    "Caller varchar(255)," +
                    "Properties varchar(255)," +
                    "Listeners varchar(1023)," +
                    "Session varchar(16)" +
                    ")");
            
            return null;
        }
        
        List<NotificationStoragePacket> l = new ArrayList<NotificationStoragePacket>();
        
        NotificationStoragePacket[] packs;
        LinkedList<String> listeners;
        
        String _uuid;
        String _message;
        String _target;
        String _caller;
        String _properties;
        String _listeners;
        String _session;
        
        try {
            Statement statement;
            ResultSet result;

            try {
                driver.getConnection();
                statement = driver.getStatement();
                result = statement.executeQuery("SELECT * FROM " + TABLE);
                
                while (result.next()) {
                    _uuid = result.getString("UUID");
                    _message = result.getString("Message");
                    _target = result.getString("Target");
                    _caller = result.getString("Caller");
                    _properties = result.getString("Properties");
                    _listeners = result.getString("Listeners");
                    _session = result.getString("Session");

                    if (_target.equals(driver.getNullString()))
                        _target = null;

                    Plugin caller = Bukkit.getPluginManager().getPlugin(_caller);

                    String[] _props = _properties.split(driver.getListDivider());
                    Notification.NotificationProperty[] props = new Notification.NotificationProperty[_props.length];

                    for (int i = 0; i < _props.length; ++i) {
                        props[i] = Notification.NotificationProperty.getPropertyForKey(_props[i]);
                    }

                    listeners = new LinkedList<String>(Arrays.asList(_listeners.split(driver.getListDivider())));

                    Notification note = new Notification(_session, _uuid, caller, _message, _target, props);
                    NotificationStoragePacket packet = new NotificationStoragePacket(note, listeners);
                    l.add(packet);
                }

            } catch (SQLException ex) {
            	VoxelGuest.log("SQLException caught in NotificationStorage.load(): " + ex.getMessage());
            } finally {
                driver.release();
            }
        } catch (SQLException ex) {
        	VoxelGuest.log("SQLException caught in NotificationStorage.load(): " + ex.getMessage());
        }
        
        if (l != null) {
            packs = new NotificationStoragePacket[l.size()];
            return l.toArray(packs);
        }
        
        return null;
    }
}
