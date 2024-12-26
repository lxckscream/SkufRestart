package ru.screamoov.skufrestart;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import ru.screamoov.skufrestart.configuration.Configuration;
import ru.screamoov.skufrestart.controllers.UserController;
import ru.screamoov.skufrestart.models.Action;
import ru.screamoov.skufrestart.models.RestartModel;
import ru.screamoov.skufrestart.models.TimeStamp;

import java.util.ArrayList;

public final class Main extends JavaPlugin {
    static Main instance;
    static RestartModel restartModel;

    @Override
    public void onEnable() {
        instance = this;
        Configuration.load();

        ArrayList<TimeStamp> timeStamps = new ArrayList<>();
        Configuration.config.getStringList("time-stamps").forEach(timeStamp -> {
            TimeStamp timeStampObj = new TimeStamp();
            String[] fimozik = timeStamp.split(":");
            if (fimozik[0].length() == 2) {
                try {
                    int hour = Integer.parseInt(fimozik[0]);
                    int minute = Integer.parseInt(fimozik[1]);
                    timeStampObj.hours = hour;
                    timeStampObj.minutes = minute;
                    timeStamps.add(timeStampObj);
                } catch (Exception e) {
                    getLogger().severe("Invalid time format: " + timeStamp);
                    shutDown(false);
                }
            } else {
                getLogger().severe("Invalid time format: " + timeStamp);
                shutDown(false);
            }
        });

        ArrayList<Plugin> plugins = new ArrayList<>();
        Configuration.config.getStringList("plugin-disables").forEach(plugin -> {
            Plugin pluginObj = getServer().getPluginManager().getPlugin(plugin);
            if (pluginObj != null) plugins.add(pluginObj);
            else {
                getLogger().severe("Invalid plugin in list: " + plugin);
                shutDown(false);
            }
        });

        ArrayList<Action> actions = new ArrayList<>();
        Configuration.config.getStringList("actions").forEach(action -> {
            Action actionObj = new Action(action);
            actions.add(actionObj);
        });

        int restartDelay = Configuration.config.getInt("restart-delay");
        restartModel = new RestartModel(timeStamps, plugins, actions, restartDelay);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (restartModel != null) {
                    restartModel.timeStamps.forEach(timeStamp -> {
                        if (timeStamp.isNow()) restartModel.start();
                    });
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(this, 0L, 1200);

        Bukkit.getPluginManager().registerEvents(new UserController(), this);
    }

    public static RestartModel getRestartModel() {
        return restartModel;
    }

    @Override
    public void onDisable() {
        shutDown(Configuration.config.getBoolean("disable-plugins-on-disable"));
    }

    public static void shutDown(boolean disablePlugins) {
        if (disablePlugins) restartModel.disablingPlugins.forEach(getInstance().getPluginLoader()::disablePlugin);
        getInstance().getLogger().info("Goodbye! SkufRestart v1.0 by lxckScream");
        getInstance().getPluginLoader().disablePlugin(instance);
    }

    public static Main getInstance() {
        return instance;
    }
}
