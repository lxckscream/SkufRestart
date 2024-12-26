package ru.screamoov.skufrestart.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import ru.screamoov.skufrestart.Main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ru.screamoov.skufrestart.utils.Hex.color;

public class Configuration {
    public static FileConfiguration config;
    public static File file;

    public static void load() {
        file = new File(Main.getInstance().getDataFolder(), "config.yml");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        config = YamlConfiguration.loadConfiguration(file);

        setIfNotExists("time-stamps", List.of("22:00", "12:00"));
        setIfNotExists("plugin-disables", List.of("AntiRelog"));
        setIfNotExists("restart-delay", 300);
        setIfNotExists("disable-plugins-on-disable", true);
        setIfNotExists("actions", List.of(
                "10:BROADCAST->>&6Перезагрузка сервера через 10 секунд",
                "5:BROADCAST->>&6Перезагрузка сервера через 5 секунд",
                "3:BROADCAST->>&6Перезагрузка сервера через 3 секунды",
                "2:BROADCAST->>&6Перезагрузка сервера через 2 секунды",
                "1:BROADCAST->>&6Перезагрузка сервера через 1 секунду"
        ));
        setIfNotExists("bossbar.string", "&4&lВНИМАНИЕ!!! &fПерезагрузка сервера через &6&l%time% сек.");
        setIfNotExists("bossbar.color", "RED");
        setIfNotExists("bossbar.style", "SOLID");
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(Player player, String path) {
        player.sendMessage(color(config.getString(path)).replaceAll("%player%", player.getName()));
    }

    public static String getMessage(String path) {
        return color(config.getString(path));
    }

    public static List<String> getList(String path) {
        return color(config.getStringList(path));
    }

    private static void setIfNotExists(String path, Object value) {
        if (config.get(path) == null) {
            config.set(path, value);
        }
    }
}
