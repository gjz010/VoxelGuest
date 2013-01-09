/*
 * NotificationCentre.java
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

import java.util.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.thevoxelbox.voxelguest.VoxelGuest;
import com.thevoxelbox.voxelguest.commands.Command;
import com.thevoxelbox.voxelguest.players.GuestPlayer;


public class NotificationCentre {
    
    private static NotificationCentre singleton;
    private HashMap<String, LinkedList<String>> notificationCentre = new HashMap<String, LinkedList<String>>();
    private List<Notification> activeNotifications = new LinkedList<Notification>();
    private NotificationStorage storageServices;
    
    public static NotificationCentre sharedCentre()
    {
        if (singleton != null)
            return singleton;
        else {
            singleton = new NotificationCentre(); 
            
            if (VoxelGuest.getConfigData().getString("notification-storage-type").equalsIgnoreCase("sqlite")) {
                singleton.storageServices = new NotificationStorage(new SQLiteDriver(VoxelGuest.getConfigData().getString("sqlite-database"), "plugins/libpsanker"));
                NotificationStoragePacket[] packs = singleton.storageServices.load();
                
                if (packs == null) {
                    return singleton;
                }
                
                for (NotificationStoragePacket pack : packs) {
                    singleton.activeNotifications.add(pack.getNotification());
                    
                    if (!singleton.notificationCentre.containsKey(pack.getNotification().getUUID()))
                        singleton.notificationCentre.put(pack.getNotification().getUUID(), pack.getListeners());
                }
            } else if (VoxelGuest.getConfigData().getString("notification-storage-type").equalsIgnoreCase("mysql")) {
                NotificationStorage storage = new NotificationStorage(new MySQLDriver(VoxelGuest.getConfigData().getString("mysql-username"), 
                																	  VoxelGuest.getConfigData().getString("mysql-password"), 
                																	  VoxelGuest.getConfigData().getString("mysql-hostname"),
                																	  VoxelGuest.getConfigData().getString("mysql-port"),
                																	  VoxelGuest.getConfigData().getString("mysql-database")));
                NotificationStoragePacket[] packs = storage.load();
                
                if (packs == null) {
                    return singleton;
                }
                
                for (NotificationStoragePacket pack : packs) {
                    singleton.activeNotifications.add(pack.getNotification());
                    singleton.notificationCentre.put(pack.getNotification().getUUID(), pack.getListeners());
                }
            } else {
                // Session notifications only
            }
            
            return singleton;
        }
    }
    
    public void shutdownCentre()
    {
        if (VoxelGuest.getConfigData().getString("notification-storage-type").equalsIgnoreCase("sqlite")) {
            Notification[] notifications = new Notification[singleton.activeNotifications.size()];
            notifications = singleton.activeNotifications.toArray(notifications);
            
            singleton.storageServices.save(notifications);
        } else if (VoxelGuest.getConfigData().getString("notification-storage-type").equalsIgnoreCase("mysql")) {
            Notification[] notifications = new Notification[singleton.activeNotifications.size()];
            notifications = singleton.activeNotifications.toArray(notifications);
            
            singleton.storageServices.save(notifications);
        } else {
            // Session notifications only
        }
    }
    
    public void addObserver(String name, String UUID)
    {
        if (!singleton.notificationCentre.containsKey(UUID)) {
            LinkedList<String> l = new LinkedList<String>();
            
            if (!l.contains(name))
                l.add(name);
            
            singleton.notificationCentre.put(UUID, l);
        } else {
            LinkedList<String> l = singleton.notificationCentre.get(UUID);
            
            if (!l.contains(name))
                l.add(name);
            
            singleton.notificationCentre.put(UUID, l);
        }
    }
    
    public void removeObserver(String name, String UUID)
    {
        if (singleton.notificationCentre.containsKey(UUID)) {
            LinkedList<String> l = singleton.notificationCentre.get(UUID);
            
            if (l.contains(name))
                l.remove(name);
            
            singleton.notificationCentre.put(UUID, l);
        }
    }
    
    public void call(Notification note)
    {
        if (note.getProperties() == null && note.getTarget() == null) {
            note.setProperties(new Notification.NotificationProperty[] {Notification.NotificationProperty.COPY, Notification.NotificationProperty.RETAIN});
        } else if (note.getProperties() == null && note.getTarget() != null) {
            note.setProperties(new Notification.NotificationProperty[] {Notification.NotificationProperty.SINGLE, Notification.NotificationProperty.RETAIN});
        }
        
        if (Arrays.asList(note.getProperties()).contains(Notification.NotificationProperty.COPY) && Arrays.asList(note.getProperties()).contains(Notification.NotificationProperty.SINGLE)) {
            VoxelGuest.log("Error with notification \"" + note.getUUID() + "\": Cannot have properties COPY and SINGLE", 2);
            return;
        }
        
        if (Arrays.asList(note.getProperties()).contains(Notification.NotificationProperty.WEAK) && Arrays.asList(note.getProperties()).contains(Notification.NotificationProperty.RETAIN)) {
        	VoxelGuest.log("Error with notification \"" + note.getUUID() + "\": Cannot have properties WEAK and RETAIN", 2);
            return;
        }
        
        if (Arrays.asList(note.getProperties()).contains(Notification.NotificationProperty.STRONG) && Arrays.asList(note.getProperties()).contains(Notification.NotificationProperty.RETAIN)) {
        	VoxelGuest.log("Error with notification \"" + note.getUUID() + "\": Cannot have properties STRONG and RETAIN", 2);
            return;
        }
        
        if (Arrays.asList(note.getProperties()).contains(Notification.NotificationProperty.STRONG) && Arrays.asList(note.getProperties()).contains(Notification.NotificationProperty.WEAK)) {
        	VoxelGuest.log("Error with notification \"" + note.getUUID() + "\": Cannot have properties STRONG and WEAK", 2);
            return;
        }
        
        if (note.getTarget() == null) {
            for (Iterator<String> it = singleton.notificationCentre.get(note.getUUID()).iterator(); it.hasNext();) {
                String str = it.next();
                OfflinePlayer op = Bukkit.getServer().getOfflinePlayer(str);
                
                if (op.getPlayer() != null) {
                    GuestPlayer lp = VoxelGuest.getGuestPlayer(op.getPlayer());
                    lp.addNotification(note);
                }
            }
            
            activeNotifications.add(note);
        } else {
            OfflinePlayer op = Bukkit.getServer().getOfflinePlayer(note.getTarget());
                
            if (op.getPlayer() != null) {
                GuestPlayer lp = VoxelGuest.getGuestPlayer(op.getPlayer());
                lp.addNotification(note);
            }
        }
    }
    
    public void cancelNotification(String sessionId)
    {
        for (Notification note : singleton.activeNotifications) {
            if (note.getSessionId().equals(sessionId)) {
                for (Iterator<String> it = singleton.notificationCentre.get(note.getUUID()).iterator(); it.hasNext();) {
                    String str = it.next();
                    OfflinePlayer op = Bukkit.getServer().getOfflinePlayer(str);

                    if (op.getPlayer() != null) {
                        GuestPlayer lp = VoxelGuest.getGuestPlayer(op.getPlayer());
                        lp.getNoteBoard().forceRemoveNotification(note);
                    }
                }
                
                singleton.activeNotifications.remove(note);
                break;
            }
        }
    }
    
    protected List<String> getListenersForNotification(String UUID)
    {
        if (notificationCentre.containsKey(UUID)) {
            return notificationCentre.get(UUID);
        }
        
        return null;
    }
    
    // -----
    // Commands
    // -----
    
    @Command(
        aliases={"noticeboard", "noteboard", "nb"},
        bounds={0,3},
        help="§c/noticeboard §fbrings up your notice board\n"
            +"§c/noticeboard mark [read, flagged] # §fmarks your notifications",
        playerOnly=true)
    public void noticeBoardCommand(CommandSender cs, String[] args)
    {
        GuestPlayer lp = VoxelGuest.getGuestPlayer((Player) cs);
        
        if (args == null || args.length == 0) {
            return;
        }
        
        if (args.length != 3) {
            lp.sendMessage("§cForm must be /nb mark [read, flagged] <number>");
            return;
        }
        
        int key = Integer.parseInt(args[2]);
        --key;
        
        if ("read".equalsIgnoreCase(args[1])) {
            lp.getNoteBoard().markNotification(lp.getNoteBoard().getNotificationForKey(key), NoteBoard.Marking.READ);
        } if ("flagged".equalsIgnoreCase(args[1])) {
            lp.getNoteBoard().markNotification(lp.getNoteBoard().getNotificationForKey(key), NoteBoard.Marking.FLAGGED);
        }
    }
    
    // ------
    // Events
    // ------
    
    public void didFinishLoading()
    {
        for (Player p : Bukkit.getOnlinePlayers()) {
            playerSessionDidStart(p);
        }
    }
    
    public void playerSessionDidStart(Player p)
    {
        GuestPlayer lp = VoxelGuest.getGuestPlayer(p);
            
        for (Map.Entry<String, LinkedList<String>> entry : notificationCentre.entrySet()) {
            if (entry.getValue().contains(lp.getName())) {
                for (Notification note : activeNotifications) {
                    if (note.getUUID().equals(entry.getKey())) {
                        lp.getNoteBoard().silentAddNotification(note);
                    }
                }
            }
        }
    }
    
    protected void didRemoveNotificationFromPlayer(GuestPlayer player, Notification note)
    {
        if (note.getTarget() != null) {
            singleton.activeNotifications.remove(note);
        } else if (Arrays.asList(note.getProperties()).contains(Notification.NotificationProperty.SINGLE)) {
            singleton.activeNotifications.remove(note);
            
            for (Iterator<String> it = singleton.notificationCentre.get(note.getUUID()).iterator(); it.hasNext();) {
                String str = it.next();
                OfflinePlayer op = Bukkit.getServer().getOfflinePlayer(str);
                
                if (op.getPlayer() != null) {
                    GuestPlayer lp = VoxelGuest.getGuestPlayer(op.getPlayer());
                    lp.getNoteBoard().forceRemoveNotification(note);
                }
            }
            
            singleton.cancelNotification(note.getSessionId());
        }
    }
}
