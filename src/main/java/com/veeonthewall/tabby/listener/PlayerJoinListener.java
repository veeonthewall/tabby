package com.veeonthewall.tabby.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import com.veeonthewall.tabby.Tabby;

public class PlayerJoinListener implements Listener {
    private Tabby plugin;

    // Constructor
    public PlayerJoinListener(Tabby plugin) {
        // Set the plugin, after this we can call its methods and variables from this class.
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Update the tablist for all players using the updateTablist method in Tabby.java
        plugin.updateTablist();

        // You can do other stuff here but that is an exercise left to the coder.
    }
}