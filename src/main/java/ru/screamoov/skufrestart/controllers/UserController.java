package ru.screamoov.skufrestart.controllers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.screamoov.skufrestart.Main;

public class UserController implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (Main.getRestartModel().bossBar != null) {
            Main.getRestartModel().bossBar.addPlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (Main.getRestartModel().bossBar != null) {
            Main.getRestartModel().bossBar.removePlayer(event.getPlayer());
        }
    }
}
