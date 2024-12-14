package ru.muwa.shq.engine.utils;

import ru.muwa.shq.engine.Game;
import ru.muwa.shq.engine.Updater;
import ru.muwa.shq.entities.Level;

import java.awt.*;
import java.util.Map;

public class GameTime {

    public static long value = 0;
    public static int day;

    public static final long GAME_START = 0, DAY_LENGTH = 720_000, GAME_END = GAME_START + DAY_LENGTH * 30, HOUR_LENGTH = DAY_LENGTH / 24, TICK_LENGTH = (DAY_LENGTH / 24 / 60) / Game.fps;



    public static void work() {
        if(Game.player.smoke < 1 || Math.random() < 0.34 )
            value += TICK_LENGTH / 2;
    }

    public static long get() {
        return value;
    }

    public static String getString() {
        long day = value / DAY_LENGTH + 1;
        long time = value % DAY_LENGTH;
        long hours = time / 60_000;
        String minutes = "" + (int) (time % 60_000) * 2;

        if (minutes.length() < 4) minutes = "00";
        else {
            if (minutes.length() < 5) minutes = "0" + minutes.charAt(0);
            else minutes = minutes.charAt(0) + "" + minutes.charAt(1);
        }

        return "day : " + day + " time : " + hours + " h " + minutes + " min";
    }

    public static void forward(long amount) {
        value += amount;
        Game.player.mommaClean -= amount;
        Game.player.mommaFullness -= amount;
        Game.player.mommaHealth -= amount;
        for(Map.Entry<Integer, Level> e : Level.repo.entrySet())
            if(e.getValue().isIndoors && e.getValue().noise > 0)
                e.getValue().noise = Math.max(0,e.getValue().noise - ((double) value /HOUR_LENGTH));
        Game.player.wanted = Math.max(0,Game.player.wanted - ((double) value /HOUR_LENGTH));
    }

    public enum TimeOfTheDay{SUNRISE,DAY,SUNSET,NIGHT}
    public static TimeOfTheDay getTimeOfTheDay()
    {
        if(getString().contains("19 h") || getString().contains("20 h") || getString().contains("21 h") || getString().contains("22 h"))
            return TimeOfTheDay.SUNSET;
        if(/*getString().contains("22 h") ||*/ getString().contains("23 h") || getString().contains(": 0 h") || getString().contains(": 1 h") || getString().contains(": 2 h") || getString().contains(": 3 h"))
            return TimeOfTheDay.NIGHT;
        if(getString().contains(": 4 h") || getString().contains(": 5 h") || getString().contains(": 6 h") || getString().contains(": 7 h"))
            return TimeOfTheDay.SUNRISE;
        return TimeOfTheDay.DAY;

    }
}
