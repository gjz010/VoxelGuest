package com.thevoxelbox.voxelguest.modules.general;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionEventListener implements Listener {

    private GeneralModule module;
    
    public ConnectionEventListener(final GeneralModule generalModule) {
        this.module = generalModule;
    }
    
    @EventHandler
    public final void onPlayerJoin(final PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        event.setJoinMessage(this.formatJoinLeaveMessage(GeneralModule.JOIN_FORMAT, player.getName()));
        
        if(module.ovanished.contains(player.getName())) {
            module.vanished.add(player.getName());
            module.ovanished.remove(player.getName());
            module.hidePlayerForAll(player);
        }
        
        module.hideAllForPlayer(player);
        
        if(module.ofakequit.contains(player.getName())) {
            module.fakequit.add(player.getName());
            module.ofakequit.remove(player.getName());
            event.setJoinMessage("");
        }
    }
    
    @EventHandler
    public final void onPlayerQuit(final PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        event.setQuitMessage(this.formatJoinLeaveMessage(GeneralModule.LEAVE_FORMAT, player.getName()));
        
        if(module.vanished.contains(player.getName())) {
            module.ovanished.add(player.getName());
            module.vanished.remove(player.getName());
        }
        
        if(module.fakequit.contains(player.getName())) {
            module.ofakequit.add(player.getName());
            module.fakequit.remove(player.getName());
            event.setQuitMessage("");
        }
    }
    
    @EventHandler
    public final void onPlayerKick(final PlayerKickEvent event) {
        Player player = event.getPlayer();
        
        event.setLeaveMessage(this.formatJoinLeaveMessage(GeneralModule.KICK_FORMAT, player.getName()));
        
        if(module.vanished.contains(player.getName())) {
            module.ovanished.add(player.getName());
            module.vanished.remove(player.getName());
        }
        
        if(module.fakequit.contains(player.getName())) {
            module.ofakequit.add(player.getName());
            module.fakequit.remove(player.getName());
            event.setLeaveMessage("");
        }
    }
    
    public String formatJoinLeaveMessage(String msg, String playerName) {
        return msg.replace("$no", Integer.toString(Bukkit.getOnlinePlayers().length).replace("$n", playerName));
    }
}