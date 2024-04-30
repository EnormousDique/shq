package ru.muwa.shq.engine.utils;

import ru.muwa.shq.engine.Game;
import ru.muwa.shq.story.Dialogue;

public class Momma {
    public static void work()
    {
        Game.player.mommaHealth -= (1_000 / 60);
        Game.player.mommaFullness -= (1_000 / 60);
        Game.player.mommaClean -= (1_000 / 60);
        if(Game.player.mommaHealth <= 0 || Game.player.mommaFullness <= 0 || Game.player.mommaClean <= 0)
        {
            Dialogue.current = Dialogue.repo.get(999);
        }
    }
}
