package ru.muwa.shq.engine.utils;

import ru.muwa.shq.engine.Game;
import ru.muwa.shq.engine.GameWindow;
import ru.muwa.shq.engine.Renderer;
import ru.muwa.shq.entities.GameObject;
import ru.muwa.shq.entities.Level;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

import static ru.muwa.shq.engine.utils.GameTime.TimeOfTheDay.NIGHT;
import static ru.muwa.shq.entities.Level.STREET_1;

public class Spawner {
    public static void work()
    {
        pedestrianSpawn();
        copSpawn();
        removeNpcAtNight();
        spawnCars();
        spawnRats();
        spawnZek();
        spawnHach();
        spawnGop();
    }
    private static long gopSpawnTimer =0;
    private static void spawnGop() {
        if(Game.currentLevel.id != STREET_1) return;
        if(GameTime.getTimeOfTheDay() == NIGHT || GameTime.getTimeOfTheDay() == GameTime.TimeOfTheDay.SUNSET) {
            if (gopSpawnTimer > System.currentTimeMillis()) return;
            int x = Game.player.x + Camera.x + (Math.random() > 0.5?
                    (GameWindow.WIDTH/2)+100 : (-1 * GameWindow.WIDTH/2)-100);
            int y = Game.player.y + Camera.y + (Math.random() > 0.5?
                    (GameWindow.HEIGHT/2)+100 : (-1 * GameWindow.HEIGHT/2)-100);
            //Создаем новый экземпляр гопника и помещаем на карту
            GameObject gop = GameObject.get(4);
            gop.x = x;
            gop.y = y;
            Game.currentLevel.objects.add(gop);

            gopSpawnTimer = System.currentTimeMillis() + (GameTime.getTimeOfTheDay() == NIGHT? 1_500 : 4_000);

        } else if( GameTime.getTimeOfTheDay() == GameTime.TimeOfTheDay.SUNRISE || GameTime.getTimeOfTheDay() == GameTime.TimeOfTheDay.DAY)
        {
            for (int i = 0; i < Game.currentLevel.objects.size(); i++) {
                var gop = Game.currentLevel.objects.get(i);
                if(gop.id !=4) return;
                var screen = new Rectangle(Camera.x,Camera.y,GameWindow.WIDTH,GameWindow.HEIGHT);
                if(screen.intersects(gop.hitBox)) continue;
                Game.currentLevel.objects.remove(gop);
            }
        }
    }


    private static long hachSpawnTimer = 0;
    private static void spawnHach() {
        if(Game.currentLevel.id != Level.JAIL && Game.currentLevel.id != STREET_1) return;
        if(GameTime.getTimeOfTheDay() == NIGHT) return;
        if(hachSpawnTimer > System.currentTimeMillis()) return;
        for (int i = 0; i < Game.currentLevel.objects.size(); i++) {
            var zone = Game.currentLevel.objects.get(i);
            if(zone.id != 125) continue;
            if(Game.player.x - zone.x > 750 || Game.player.x - zone.x < -750) continue;
            if(Game.player.y - zone.y > 750 || Game.player.y - zone.y < -750) continue;

            var hach = GameObject.get(124);
            hach.x = zone.x; hach.y = zone.y;
            Game.currentLevel.objects.add(hach);
            hachSpawnTimer = System.currentTimeMillis() + 6_000;
        }
    }

    private static long zekSpawnTimer = 0;
    private static void spawnZek() {
        if(Game.currentLevel.id != Level.JAIL) return;
        if(zekSpawnTimer > System.currentTimeMillis()) return;
        for (int i = 0; i < Game.currentLevel.objects.size(); i++) {
            var zone = Game.currentLevel.objects.get(i);
            if(zone.id != 122) continue;
            if(Game.player.x - zone.x > 500 || Game.player.x - zone.x < -500) continue;
            if(Game.player.y - zone.y > 500 || Game.player.y - zone.y < -500) continue;

            var zek = GameObject.get(124);
            zek.x = zone.x; zek.y = zone.y;
            Game.currentLevel.objects.add(zek);
            zekSpawnTimer = System.currentTimeMillis() + 6_000;
        }
    }

    private static long ratSpawnTimer = 0;
    private static void spawnRats() {
        if(Game.currentLevel.id != Level.SEWERS) return;
        if(ratSpawnTimer > System.currentTimeMillis()) return;
        for (int i = 0; i < Game.currentLevel.objects.size(); i++) {
            var zone = Game.currentLevel.objects.get(i);
            if(zone.id != 120) continue;
            if(Game.player.x - zone.x > 500 || Game.player.x - zone.x < -500) continue;
            if(Game.player.y - zone.y > 500 || Game.player.y - zone.y < -500) continue;

            var rat = GameObject.get(123);
            rat.x = zone.x; rat.y = zone.y;
            Game.currentLevel.objects.add(rat);
            ratSpawnTimer = System.currentTimeMillis() + 3_000;
        }
    }

    private static long carSpawnTimer = 0;
    private static final long CAR_SPAWN_INTERVAL = 3_500;
    private static void spawnCars() {

        if(Game.currentLevel.id != STREET_1) return;
        if(carSpawnTimer > System.currentTimeMillis()) return;
        var zones = Game.currentLevel.objects.stream()
                .filter(o->o.name.contains("zone_car_spawn")).toList();
        for(var zone : zones) {
            if(zone.name.contains("right")) {
                int id = 0; double chance = Math.random();
                if(chance < 0.33) id = 88;
                else if(chance < 0.66) id = 89;
                else id = 90;
                var car = GameObject.get(id);
                car.speed = 15;
                car.x = zone.x; car.y = zone.y;
                Game.currentLevel.objects.add(car);
            }
            if(zone.name.contains("left")) {
                int id = 0; double chance = Math.random();
                if(chance < 0.33) id = 53;
                else if(chance < 0.66) id = 54;
                else id = 96;
                var car = GameObject.get(id);
                car.speed = 15;
                car.x = zone.x; car.y = zone.y;
                Game.currentLevel.objects.add(car);
            }
            if(zone.name.contains("down")) {
                int id = 0; double chance = Math.random();
                if(chance < 0.33) id = 55;
                else if(chance < 0.66) id = 95;
                else id = 94;
                var car = GameObject.get(id);
                car.speed = 15;
                car.x = zone.x; car.y = zone.y;
                Game.currentLevel.objects.add(car);
            }
            if(zone.name.contains("up")) {
                int id = 0; double chance = Math.random();
                if(chance < 0.33) id = 93;
                else if(chance < 0.66) id = 91;
                else id = 92;
                var car = GameObject.get(id);
                car.speed = 15;
                car.x = zone.x; car.y = zone.y;
                Game.currentLevel.objects.add(car);

            }
        }
        carSpawnTimer = System.currentTimeMillis() + CAR_SPAWN_INTERVAL;
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
    private static long copSpawnInterval; // 1 min
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
        //Спавним мусоров при соответствующем уровне беспредела
        if(Game.player.wanted > 10)
        {
            //Если время не настало, ждем
            if(copSpawnTimer > GameTime.value) return;

            //Спавн в зданиях
            if(Game.currentLevel.isIndoors) {
                List<GameObject> copSpawnZone = Game.currentLevel.objects.stream().filter(o -> o.id == INDOOR_COP_SPAWN_ZONE_ID).toList();
                Rectangle screen = new Rectangle(Camera.x, Camera.y, GameWindow.WIDTH, GameWindow.HEIGHT);
                for (GameObject z : copSpawnZone) {
                        GameObject cop = GameObject.get(7);
                        cop.x = z.x;
                        cop.y = z.y;
                        Game.currentLevel.objects.add(cop);
                }
            }
            //Спавн на улице
            if(Game.currentLevel.isStreet)
            {
                //Определяем какого копа спавним
                int copId = Game.player.wanted > 299? 7 : 7; //TODO сделать военных
                //Считаем какое кол-во копов спавним
                int times = 1;
                if(Game.player.wanted > 10) times ++;
                if(Game.player.wanted > 100) times ++;
                if(Game.player.wanted > 200) times ++;
                //TODO проверить есть ли копы далеко от Шкипера, чтобы вместо спавна подтянуть потеряшек
                //Спавним копов указанное количество раз
                for (int i = 0; i < times; i++) {
                    //Определяем место для спавна
                    int x = Game.player.x + Camera.x + (Math.random() > 0.5?
                            (GameWindow.WIDTH/2)+100 : (-1 * GameWindow.WIDTH/2)-100);
                    int y = Game.player.y + Camera.y + (Math.random() > 0.5?
                            (GameWindow.HEIGHT/2)+100 : (-1 * GameWindow.HEIGHT/2)-100);
                    //Создаем новый экземпляр копа и помещаем на карту
                    GameObject cop = GameObject.get(copId);
                    cop.x = x;
                    cop.y = y;
                    Game.currentLevel.objects.add(cop);
                }
            }
            int interval = 10_000;//По умолчанию 10 сек
            if(Game.player.wanted > 150) interval -= 1_500; // -1.5 сек за каждый шаг
            if(Game.player.wanted > 200) interval -= 1_500;
            if(Game.player.wanted > 250) interval -= 1_500;
            if(Game.player.wanted > 300) interval -= 1_500;
            copSpawnTimer = GameTime.value + interval;
        }
        else{
            //Если беспредел низкий, убираем мусоров
            for (int i = 0; i < Level.repo.get(STREET_1).objects.size(); i++) {
                var npc = Level.repo.get(STREET_1).objects.get(i);
                if(npc.id!=7)continue;
                Level.repo.get(STREET_1).objects.remove(npc);
            }
        }
    }
}
