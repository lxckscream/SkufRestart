package ru.screamoov.skufrestart.models;

import org.bukkit.Bukkit;
import ru.screamoov.skufrestart.Main;

import static ru.screamoov.skufrestart.utils.Hex.color;

public class Action {
    public String strAction;
    public ActionType actionType;
    public String action;
    public int timeOnAction;

    public Action(String strAction) {
        Main.getInstance().getLogger().info("[ActionManager] Loading action: " + strAction);
        this.strAction = strAction;
        String[] fimoz = strAction.split("->> ");
        String[] fimozik = fimoz[0].split(":");
        try {
            timeOnAction = Integer.parseInt(fimozik[0]);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
        this.actionType = ActionType.valueOf(fimozik[1].toUpperCase());
        this.action = fimoz[1];
    }

    public void execute() {
        switch (actionType) {
            case BROADCAST:
                Bukkit.broadcastMessage(color(action));
                break;
            case CONSOLE:
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), color(action));
                break;
        }
    }
}
