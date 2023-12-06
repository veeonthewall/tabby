package com.veeonthewall.tabby;

import com.veeonthewall.tabby.util.MessageUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

// Classes from our own package
import com.veeonthewall.tabby.command.TabbyCommand;
import com.veeonthewall.tabby.listener.PlayerJoinListener;
import com.veeonthewall.tabby.listener.PlayerQuitListener;
import org.checkerframework.checker.nullness.qual.NonNull;

public class Tabby extends JavaPlugin implements Listener {
    // Declare variables that will be used later
    private long start;
    public String prefix;
    public String header;
    public String footer;
    public long updateTicks;
    public boolean isPlaceholderAPIEnabled;

    public int refreshConfig() {
        try {
            // Loads the config file and parses it in proper format for later functions
            reloadConfig();

            // Save prefix, it will be converted to a Component later
            prefix = getConfig().getString("prefix");

            // Header and footer are stored as multiple strings.
            // This loop combines them into one string with newlines in between each line.
            header = String.join("\n", getConfig().getStringList("header"));
            footer = String.join("\n", getConfig().getStringList("footer"));

            // Set other appropriate variables 
            start = System.currentTimeMillis();

            // Start the tablist update loop
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::updateTablist, 0L, 20L);

            // If no exception occurred, return 0 to indicate success
            return 0;
        } catch (Exception e) {
            // If an exception occurred, return 1 to indicate failure
            return 1;
        }
    }

    @Override
    public void onEnable() {
        // Create config file if it doesn't exist
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }
        
        // Load config file
        int res = refreshConfig();
        if (res == 0) {
            getLogger().info("Successfully loaded config file.");
        } else {
            getLogger().warning("Failed to load config file. Shutting down.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Check if PlaceholderAPI is installed
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().warning("PlaceholderAPI not found. Placeholders will be limited.");
            isPlaceholderAPIEnabled = false;
        } else {
            getLogger().info("PlaceholderAPI found and enabled.");
            isPlaceholderAPIEnabled = true;
        }
        
        // Register events, events are in listeners/PlayerJoinEvent.java and listeners/PlayerQuitEvent.java
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);

        // Register commands, commands are in commands/TabbyCommand.java
        getCommand("tabby").setExecutor(new TabbyCommand(this));

        // Log that the plugin has been enabled
        getLogger().info("tabby is enabled!");
    }

    public void onDisable() {
        // Log that the plugin has been disabled
        getLogger().info("tabby is disabled!");
    }

    // Update the tablist for all players
    public void updateTablist() {
        // Finally, set the header and footer for all players
        for (Player player : Bukkit.getOnlinePlayers()) {
            // Set default placeholders
            String header = this.header;
            String footer = this.footer;

            // Create an audience for the player
            Audience audience = Bukkit.getPlayer(player.getUniqueId());

            header = header.replaceAll("%player%", player.getName());
            footer = footer.replaceAll("%player%", player.getName());

            header = header.replaceAll("%online%", String.valueOf(Bukkit.getOnlinePlayers().size()));
            footer = footer.replaceAll("%online%", String.valueOf(Bukkit.getOnlinePlayers().size()));

            header = header.replaceAll("%max%", String.valueOf(Bukkit.getMaxPlayers()));
            footer = footer.replaceAll("%max%", String.valueOf(Bukkit.getMaxPlayers()));

            if (isPlaceholderAPIEnabled) {
                // First, get and replace all the PlaceholderAPI placeholders in the header and footer
                header = PlaceholderAPI.setPlaceholders(player, this.header);
                footer = PlaceholderAPI.setPlaceholders(player, this.footer);
            }

            // Next, convert the header and footer to a Component, we use this for efficiency and to support hex colors. Make sure to trim the newlines at the end.
            Component componentHeader = MessageUtil.createColoredMessage(header);
            Component componentFooter = MessageUtil.createColoredMessage(footer);

            assert audience != null;
            audience.sendPlayerListHeader(componentHeader);
            audience.sendPlayerListFooter(componentFooter);
        }
    }

    // Get prefix
    public String getPrefix() {
        return prefix;
    }
}
