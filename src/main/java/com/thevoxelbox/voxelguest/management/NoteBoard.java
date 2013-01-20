/*
 * NoteBoard.java
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.thevoxelbox.voxelguest.players.GuestPlayer;

import org.bukkit.entity.Player;


public class NoteBoard {
    
    private Player parent;
    private List<Notification> activeNotes = new ArrayList<Notification>();
    private Map<Notification, Marking> flagging = new HashMap<Notification, Marking>();
    
    public NoteBoard(Player player)
    {
        parent = player;
    }
    
    public void addNotification(Notification note)
    {
        if (Arrays.asList(note.getProperties()).contains(Notification.NotificationProperty.WEAK)) {
            message(note);
            return;
        }
        
        if (!activeNotes.contains(note)) {
            activeNotes.add(note);
            flagging.put(note, Marking.NORMAL);
            message(note);
        }
    }
    
    public void silentAddNotification(Notification note)
    {   
        if (!activeNotes.contains(note)) {
            activeNotes.add(note);
            flagging.put(note, Marking.NORMAL);
        }
    }
    
    public void removeNotification(Notification note)
    {
        if (activeNotes.contains(note)) {
            activeNotes.remove(note);
            flagging.remove(note);
            NotificationCentre.sharedCentre().didRemoveNotificationFromPlayer((GuestPlayer) parent, note);
        }
    }
    
    public void forceRemoveNotification(Notification note)
    {
        if (activeNotes.contains(note)) {
            activeNotes.remove(note);
            flagging.remove(note);
        }
    }
    public void markNotification(Notification note, Marking marking)
    {
        if (marking == Marking.READ && Arrays.asList(note.getProperties()).contains(Notification.NotificationProperty.RETAIN)) {
            removeNotification(note);
            return;
        }
        
        flagging.put(note, marking);
    }
    
    protected Notification getNotificationForKey(int key)
    {
        return activeNotes.get(key);
    }
    
    protected void message(Notification note)
    {
        parent.sendMessage("§8[§6NC§8] §b" + note.getCaller().getName() + ": " + note.getMessage());
    }
    
    protected void panelList()
    {
        if (!activeNotes.isEmpty()) {
            int i = 1;

            for (Iterator<Notification> it = activeNotes.iterator(); it.hasNext();) {
                Notification note = it.next();
                Marking marking = flagging.get(note);

                if (marking == Marking.NORMAL)
                    parent.sendMessage(i + ".§8) §b" + note.getCaller().getName() + ": " + note.getMessage());
                else if (marking == Marking.FLAGGED)
                    parent.sendMessage(i + ".§8) §c" + note.getCaller().getName() + ": " + note.getMessage());
                else if (marking == Marking.READ)
                    parent.sendMessage(i + ".§8) §7" + note.getCaller().getName() + ": " + note.getMessage());

                ++i;
            }
        } else {
            parent.sendMessage("§7No notifications");
        }
    }

    public void sendPanel()
    {
        parent.sendMessage("§8====================");
        parent.sendMessage("§6Notice Board");
        parent.sendMessage("§6");

        panelList();
        parent.sendMessage("§8====================");
    }
    
    public enum Marking
    {
        NORMAL,
        READ,
        FLAGGED
    }
}
