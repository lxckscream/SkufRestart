package ru.screamoov.skufrestart.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Hex {
    public Hex() {
    }

    public static String color(String text) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

        for(Matcher matcher = pattern.matcher(text); matcher.find(); matcher = pattern.matcher(text)) {
            String hexCode = text.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');
            StringBuilder builder = new StringBuilder();
            replaceSharp.chars().forEach((c) -> {
                builder.append("&").append((char)c);
            });
            text = text.replace(hexCode, builder.toString());
        }

        return ChatColor.translateAlternateColorCodes('&', text).replace("&", "");
    }

    public static List<String> color(List<String> text) {
        return (List)text.stream().map(Hex::color).collect(Collectors.toList());
    }
}
