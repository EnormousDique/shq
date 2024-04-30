package ru.muwa.shq.engine.utils;

import ru.muwa.shq.engine.Game;
import ru.muwa.shq.engine.GameWindow;
import ru.muwa.shq.entities.GameObject;
import ru.muwa.shq.entities.Level;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

import static ru.muwa.shq.entities.Level.STREET_1;

public class Spawner {
    public static void work()
    {
        pedestrianSpawn();
        copSpawn();
        removeNpcAtNight();
    }

    private static final List<Integer> goHomeIds = List.of(6);
    private static void removeNpcAtNight() {
        if(GameTime.getString().contains("22 h") || GameTime.getString().contains("23 h") || GameTime.getString().contains(": 0 h") || GameTime.getString().contains(": 1 h") || GameTime.getString().contains(": 2 h") || GameTime.getString().contains(": 3 h") || GameTime.getString().contains(": 4 h") || GameTime.getString().contains(": 5 h"))
        {
            Rectangle camera = new Rectangle(Camera.x,Camera.y,GameWindow.WIDTH,GameWindow.HEIGHT);
            for (int i = 0; i < Level.repo.get(STREET_1).objects.size(); i++) {
                var npc = Level.repo.get(STREET_1).objects.get(i);
                if(goHomeIds.contains(npc.id) && !npc.hitBox.intersects(camera))
                {
                    Level.repo.get(STREET_1).objects.remove(npc);
                }

            }
        }
    }

    private static long copSpawnTimer;
    private static final long COP_SPAWN_INTERVAL = 60_000; // 1 min
    private static long pedestrianSpawnTimer;
    private static final long PEDESTRIAN_SPAWN_INTERVAL = 10_000; // 10 sec

    private static final int PEDESTRIAN_SPAWN_ZONE_ID = 223;
    private static void pedestrianSpawn() {

        //Получаем список зон спавна прохожих на улице 1 (уровень 1)
        List<GameObject> pedestrianSpawnZones = Level.repo.get(1).objects.stream().filter(o->o.id==PEDESTRIAN_SPAWN_ZONE_ID).toList();

        Rectangle screen = new Rectangle(Camera.x,Camera.y, GameWindow.WIDTH,GameWindow.HEIGHT);

        for (GameObject z : pedestrianSpawnZones)
        {
            if(screen.intersects(z.hitBox)) continue;
            if(GameTime.value > pedestrianSpawnTimer)
            {
                GameObject pedestrian = GameObject.get(6);
                pedestrian.x = z.x;
                pedestrian.y = z.y;
                Level.repo.get(1).objects.add(pedestrian);
                pedestrianSpawnTimer = GameTime.value + PEDESTRIAN_SPAWN_INTERVAL;

            }
        }
    }
    private static final int INDOOR_COP_SPAWN_ZONE_ID = 228;
    private static void copSpawn()
    {
        if(Game.player.wanted > 99.9)
        {
            if(copSpawnTimer > GameTime.value) return;
            if(Game.currentLevel.isIndoors) {
                List<GameObject> copSpawnZone = Game.currentLevel.objects.stream().filter(o -> o.id == INDOOR_COP_SPAWN_ZONE_ID).toList();
                Rectangle screen = new Rectangle(Camera.x, Camera.y, GameWindow.WIDTH, GameWindow.HEIGHT);
                for (GameObject z : copSpawnZone) {
                    if (screen.intersects(z.hitBox)) continue;
                    if (Math.random() < 0.01) {
                        GameObject cop = GameObject.get(7);
                        cop.x = z.x;
                        cop.y = z.y;
                        Game.currentLevel.objects.add(cop);
                        copSpawnTimer = GameTime.value + COP_SPAWN_INTERVAL;
                    }
                }
            }
            if(Game.currentLevel.isStreet)
            {
                int x = Game.player.x + Math.random() > 0.5? GameWindow.WIDTH : -1 * GameWindow.WIDTH;
                int y = Game.player.y + Math.random() > 0.5? GameWindow.HEIGHT : -1 * GameWindow.HEIGHT;
                Rectangle screen = new Rectangle(Camera.x, Camera.y, GameWindow.WIDTH, GameWindow.HEIGHT);
                if(!screen.contains(new Point2D.Double(x,y)))
                {
                    GameObject cop = GameObject.get(7);
                    cop.x = x;
                    cop.y = y;
                    Game.currentLevel.objects.add(cop);
                    System.out.println("cop spawned");
                    copSpawnTimer = GameTime.value + COP_SPAWN_INTERVAL;
                }
            }
        }
        else{
            for (int i = 0; i < Level.repo.get(STREET_1).objects.size(); i++) {
                var npc = Level.repo.get(STREET_1).objects.get(i);
                if(npc.id!=7)continue;
                Level.repo.get(STREET_1).objects.remove(npc);
            }
        }
    }
}
