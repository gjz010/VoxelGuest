/*
 * SQLiteDriver.java
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

import java.io.File;
import java.io.IOException;
import java.sql.*;

import org.bukkit.Bukkit;


public class SQLiteDriver extends SQLDriver {
    
    private String database;
    private File sqlFile;
    
    public SQLiteDriver(String db, String directory)
    {
        this.database = db;
        
        database = database.replace("/", "");
        database = database.replace("\\", "");
        database = database.replace(".db", "");
        
        File dir = new File(directory);
        
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
        
        sqlFile = new File(directory + "/" + database + ".db");
        
        if (!sqlFile.exists()) {
            try {
                sqlFile.createNewFile();
            } catch (IOException ex) {
            	Bukkit.getLogger().warning("Could not create SQLite file \"" + database + "\"");
            }
        }
    }
    
    @Override
    public Connection getConnection() throws SQLException
    {
        if (openDriver("org.sqlite.JDBC")) {
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + sqlFile.getAbsolutePath());
            return this.connection;
        }
        
        throw new SQLException("JDBC Driver for SQLit is not installed on this system.");
    }
}
