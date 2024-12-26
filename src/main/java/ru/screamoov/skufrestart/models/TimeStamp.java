package ru.screamoov.skufrestart.models;

import java.time.LocalTime;

public class TimeStamp {
    public int hours,minutes;

    public boolean isNow() {
        LocalTime now = LocalTime.now();
        return now.getHour() == hours && now.getMinute() == minutes;
    }
}
