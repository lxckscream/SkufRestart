package ru.screamoov.skufrestart.models;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import ru.screamoov.skufrestart.Main;
import ru.screamoov.skufrestart.configuration.Configuration;

import java.util.ArrayList;
import java.util.List;

import static ru.screamoov.skufrestart.utils.Hex.color;

public class RestartModel {
    public ArrayList<TimeStamp> timeStamps = new ArrayList<>();
    public ArrayList<Plugin> disablingPlugins = new ArrayList<>();
    public ArrayList<Action> actions = new ArrayList<>();
    public int restartDelay = 0;
    public int startDelay = 0;
    public BossBar bossBar;

    public RestartModel(ArrayList<TimeStamp> timeStamps, ArrayList<Plugin> disablingPlugins, ArrayList<Action> actions, int restartDelay) {
        this.timeStamps = timeStamps;
        this.actions = actions;
        this.disablingPlugins = disablingPlugins;
        this.restartDelay = restartDelay;
        startDelay = restartDelay;
    }

    public void start() {
        bossBar = Bukkit.createBossBar(color(Configuration.config.getString("bossbar.string").replaceAll("%time%", String.valueOf(restartDelay))),
                BarColor.valueOf(Configuration.config.getString("bossbar.color").toUpperCase()),
                BarStyle.valueOf(Configuration.config.getString("bossbar.style").toUpperCase()));
        Bukkit.getOnlinePlayers().forEach(bossBar::addPlayer);

        new BukkitRunnable() {
            @Override
            public void run() {
                bossBar.setTitle(color(Configuration.config.getString("bossbar.string").replaceAll("%time%", String.valueOf(restartDelay))));
                if (restartDelay <= 0) {
                    restart();
                } else {
                    actions.forEach(action -> {
                        if (action.timeOnAction==restartDelay) action.execute();
                    });
                    restartDelay--;
                    bossBar.setProgress((double) restartDelay / startDelay);
                }
            }
        }.runTaskTimer(Main.getInstance(), 20, 20);
    }

    public void stop() {
        bossBar.removeAll();
        bossBar = null;
    }

    public void restart() {
        Main.shutDown(true);
        Bukkit.shutdown();
    }
}
