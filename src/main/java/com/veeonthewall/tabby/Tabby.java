package com.veeonthewall.tabby;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;



public class Tabby extends JavaPlugin implements Listener {
    private final long start = System.currentTimeMillis();
    String header = getConfig().getString("header");
    String footer = getConfig().getString("footer");

    @Override
    public void onEnable() {
        reloadConfig();

        long elapsed = System.currentTimeMillis() - start;
        getLogger().info("tabby is enabled!" + " (" + elapsed + "ms)");
        getServer().getPluginManager().registerEvents(this, this);
        updateTab();
        getLogger().info("Header:" + header);
        getLogger().info("Footer:" + footer);
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) refreshConfig();

    }


    @Override
    public void onDisable() {
        getLogger().info("tabby is disabled!");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updateTab();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        updateTab();
    }

    private void updateTab() {
        Bukkit.getScheduler().runTask(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                String name = ChatColor.GRAY + player.getName();player.setPlayerListName(name);

                String header = getConfig().getString("header", "Test Header");
                String footer = getConfig().getString("footer", "Test Footer");
                player.setPlayerListHeader(header); player.setPlayerListFooter(footer);

            }
        });
    }
    public void refreshConfig() {
        FileConfiguration config = getConfig();
        config.addDefault("header", "Test Header");
        config.addDefault("footer", "Test Footer");
        config.options().copyDefaults(true);
        saveConfig();
        }
    }
