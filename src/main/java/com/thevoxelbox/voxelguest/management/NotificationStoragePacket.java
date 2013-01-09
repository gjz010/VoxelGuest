/*
 * NotificationStoragePacket.java
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

import java.util.LinkedList;


public class NotificationStoragePacket {
    
    private final Notification note;
    private final LinkedList<String> listeners;

    public NotificationStoragePacket(Notification note, LinkedList<String> listeners)
    {
        this.note = note;
        this.listeners = listeners;
    }
    
    public Notification getNotification()
    {
        return note;
    }
    
    public LinkedList<String> getListeners()
    {
        return listeners;
    }
}
