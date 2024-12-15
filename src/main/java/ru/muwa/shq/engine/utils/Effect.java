package ru.muwa.shq.engine.utils;

import ru.muwa.shq.engine.Game;
import ru.muwa.shq.engine.Renderer;
import ru.muwa.shq.entities.*;
import ru.muwa.shq.story.Dialogue;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;

import static ru.muwa.shq.engine.utils.Camera.Kink.*;

public class Effect {

    /** РАБОТА СЛУЖБЫ ЭФФЕКТОВ **/
    public static void work()
    {
        if(Game.player.hp > 100) Game.player.hp = 100;
        handleNoise();
        handleBusy();
        handleDeath();
        handleCop();
        handleHunger();
        handleSleepy();
        handleThirst();
        handleCrazy();
        handleStamina();
        handleStim();
        handleDrunk();
        handleSmoke();
        applyBonusesFromActiveEffects();
    }

    /** Таймер убавления бухла **/
    private static long drunkTimer = 0;
    /** Таймер вертолета **/
    private static long drunkCameraTimer = 0;
    /** Таймер перерыва от вертолетов **/
    private static long drunkCameraReleaseTimer = 0;
    /** Управление бухлом **/
    private static void handleDrunk() {
        //Соблюдаем границы
        if(Game.player.drunk > 100) Game.player.drunk = 100;
        //Опьянение падает на 1 в 1 сек (2 игровые минуты)
        if(drunkTimer<System.currentTimeMillis()) {
            drunkTimer = System.currentTimeMillis() + 1_000;
            Game.player.drunk -=1;
        }
        //Соблюдаем границы
        if(Game.player.drunk < 0) Game.player.drunk = 0;
        //Упраявляем вертолетами
        if(Game.player.drunk > 5 && drunkCameraReleaseTimer < System.currentTimeMillis()){
            if(Camera.kink == null) {
                drunkCameraTimer = (long) (System.currentTimeMillis() + Game.player.drunk * 100);
                double chance = Math.random();
                if(chance < 0.25) Camera.kink = LEFT;
                if(chance > 0.25 && chance < 0.5) Camera.kink = RIGHT;
                if(chance > 0.5 && chance < 0.75) Camera.kink = UP;
                if(chance > 0.75) Camera.kink = DOWN;
            }
        }
        if(drunkCameraTimer < System.currentTimeMillis() && drunkCameraReleaseTimer < System.currentTimeMillis()) {
            Camera.kink = null;
            drunkCameraReleaseTimer = (long) (System.currentTimeMillis() + 4_000 - (Game.player.drunk * 30));
        }
    }

    /** Таймер ожидания до нового шлейфа **/
    private static long newGlitchTimer = 0;
    /** Таймер сброса шлейфа рендеринга **/
    private static long glitchTimer = 0;
    /** Таймер падения психа из-за накуренности (попуск) **/
    private static long smokeCalmTimer = 0;
    /** Таймер падения накуренности **/
    private static long smokeTimer = 0;
    /** Управление накуренностью **/
    private static void handleSmoke() {
        //Соблюдаем границы
        if(Game.player.smoke > 100) Game.player.smoke = 100;
        //Накуренность падает на 1 в 2 сек (4 игровые минуты)
        if(smokeTimer<System.currentTimeMillis()) {
            smokeTimer = System.currentTimeMillis() + 2_000;
            Game.player.smoke -=1;
        }
        //Соблюдаем границы
        if(Game.player.smoke < 0) Game.player.smoke = 0;
        //Если Шкипер подкурен, то его попускает
        if(Game.player.smoke > 1 && smokeCalmTimer < System.currentTimeMillis()) {
            Game.player.crazy -= 2;
            //Если перебрал, хуярит
            if(Game.player.smoke > 70) Game.player.crazy += 2;
            //Таймер на 750 мсек (кулдаун)
            smokeCalmTimer = System.currentTimeMillis() + 750;
        }
        //Обновляем таймер глюков
        if(!Renderer.smokedRendering && Game.player.smoke > 5 && newGlitchTimer<System.currentTimeMillis()) {
            Renderer.smokedRendering = true;
            glitchTimer = (long) (System.currentTimeMillis() + Game.player.smoke * 100);
        }
        if(Renderer.smokedRendering && glitchTimer < System.currentTimeMillis()) {
            Renderer.smokedRendering = false;
            newGlitchTimer = (long) (System.currentTimeMillis() + (1_500 - Game.player.smoke * 10));
        }
    }

    /** Таймер падения нанюханности **/
    private static long stimTimer = 0;
    /** Таймер управления дыхалкой нанюханностью **/
    private static long stimStaminaTimer = 0;
    /** Таймер управления психом нанюханностью **/
    private static long stimCrazyTimer = 0;
    /** Управление нанюханностью **/
    private static void handleStim() {
        //Соблюдаем границы
        if(Game.player.stimulate > 100) Game.player.stimulate = 100;
        //Нанюханность падает на 1 в 1.5 сек (3 игровые минуты)
        if(stimTimer<System.currentTimeMillis()){ stimTimer = System.currentTimeMillis() + 1_500;Game.player.stimulate -=1;}
        //Соблюдаем границы
        if(Game.player.stimulate < 0) Game.player.stimulate = 0;
        //С шансом равным % стимуляции восстанавливаем 1 стамину раз в (150 - % нанюханности) миллисек
        if(Game.player.stimulate > 5 && stimStaminaTimer < System.currentTimeMillis()){
            if(Game.player.stimulate > Math.random()*100) {
                stimStaminaTimer = (long)
                        (System.currentTimeMillis() + (150 - Game.player.stimulate));
                Game.player.stamina += 3;
                Game.player.hp += 1;
                if(Game.player.stimulate > 50) Game.player.hp += 1;
            }
        }
        //Если Шкипер перенюхал, повышаем психа
        if(Game.player.stimulate > 66 && stimCrazyTimer < System.currentTimeMillis()) {
            if(Game.player.stimulate > Math.random()*100) {
                stimCrazyTimer = (long)
                        (System.currentTimeMillis() + (2000- Game.player.stimulate*15));
                Game.player.crazy += 1;
            }
        }
    }


    /** Бонусы с активных эффектов (надетый шмот и в-ва) **/
    private static void applyBonusesFromActiveEffects() {

        //Типы бонусов, которые могут прийти со шмоток или объеба
        int totalBonusHp = 0, totalBonusStamina = 0, totalBonusPsycho = 0, totalBonusSpeed = 0, totalBonusSprint = 0,
                totalCrazyBonus = 0;

        //Обработка активных эффектов с надетых шмоток
        //Баффы шмоток
        for (int i = 0; i < Game.player.effects.size(); i++) {
            Item.Effect e = Game.player.effects.get(i);
            //Учитываем бонус к макс ХП
            if(e.hpBonus != 0) totalBonusHp += e.hpBonus;
            //Учитываем бонус к макс запасу сил
            if(e.staminaBonus != 0) totalBonusStamina += e.staminaBonus;
            //Учитываем бонус к психометру
            if(e.psychoBonus != 0) totalBonusPsycho += e.psychoBonus;
            //Учитываем бонус к скорости ходьбы
            if(e.speedBonus != 0) totalBonusSpeed += e.speedBonus;
            //Учитываем бонус к скорости бега
            if(e.sprintBonus != 0) totalBonusSprint += e.sprintBonus;
        }
        //Проверяем степень объебоса и довешиваем эффекты
        //Баффы скоростей
        if(Game.player.stimulate > 5) totalBonusStamina += Game.player.stimulate;
        if(Game.player.stimulate > 10) totalBonusSpeed += 1 + Game.player.stimulate > 30?1:0 + Game.player.stimulate > 55?1:0;
        if(Game.player.stimulate > 35) totalBonusSprint += 1 + Game.player.stimulate > 30?1:0 + Game.player.stimulate > 55?1:0;
        if(Game.player.stimulate > 55) totalBonusSprint += 1;
        //Баффы шмали
        if(Game.player.smoke > 70) totalBonusStamina -= (int) (Game.player.smoke - 40);
        //Баффы бухла
        if(Game.player.drunk > 10) totalBonusHp += (int) (Game.player.drunk / 5);
        //Применяем бонусы активных эффектов
        Game.player.bonusHp = totalBonusHp;
        Game.player.bonusStamina = totalBonusStamina;
        Game.player.bonusPsycho = totalBonusPsycho;
        Game.player.bonusSpeed = totalBonusSpeed;
        Game.player.bonusSprint = totalBonusSprint;
    }

    /** Таймер дыхалки **/
    private static long staminaTimer = 0;
    /** Управление дыхалкой **/
    private static void handleStamina() {
        if(Game.player.stamina>Game.player.baseStamina + Game.player.bonusStamina)
            Game.player.stamina = Game.player.baseStamina+Game.player.bonusStamina;
        if(Game.player.stamina<0) Game.player.stamina = 0;
        if(staminaTimer > System.currentTimeMillis()) return;
        if (Game.player.thirst > 99) return;
        if (Game.player.hunger > 99) return;
        if(Input.keyboard.map.get(Input.keyboard.SHIFT)) return;
        Game.player.stamina += 1;
        staminaTimer = System.currentTimeMillis() + Game.player.thirst<50?20:100;
    }

    /** Таймер психа **/
    private static long crazyTimer = 0;
    /** Управление психом **/
    private static void handleCrazy() {
        //Соблюдаем границы
        if(Game.player.crazy > 100) Game.player.crazy = 100;
        if(Game.player.crazy < 0) Game.player.crazy = 0;
        if(Game.player.sleepy < 30 && crazyTimer < System.currentTimeMillis()){
            //Если Шкипер не сонный, восстанавливаем 1 псих раз в 3 сек (6 игровых минуты)
            Game.player.crazy -= 1;
            crazyTimer = System.currentTimeMillis() + 3_000;
        } else if (Game.player.sleepy > 99 && crazyTimer < System.currentTimeMillis()) {
            //Если Шкипера вырубает в сон, он получает 1 псих раз в 1 сек (2 игровых минуты)
            Game.player.crazy += 1;
            crazyTimer = System.currentTimeMillis() + 1_500;
        }
    }

    /** Таймер голода **/
    private static long hungerTimer = 0;
    /** Управление голодом **/
    private static void handleHunger() {
        //Если голод выше 100, снижаем до 100
        if(Game.player.hunger > 100) Game.player.hunger = 100;
        //Если голод нише 0, повышаем до 0
        if(Game.player.hunger < 0) Game.player.hunger = 0;
        //Проверяем таймер голода
        if(hungerTimer > System.currentTimeMillis()) return;
        Game.player.hunger +=0.4; //Повышаем голод на 1
        hungerTimer = System.currentTimeMillis() + 4_000; //Каждые 4 секунды (8 игровых минут)
        //Если Шкипер сыт, восполняем хп
        if(Game.player.hunger < 25 && Game.player.hp < 99)  Game.player.hp += 1;
        if(Game.player.hunger < 50 && Game.player.hp < 99) Game.player.hp += 2;
        //Если голод на максах, отнимаем 1 хп каждые 10 сек (20 игровых минут)
        if(Game.player.hunger > 99){Game.player.hp -= 1; hungerTimer = System.currentTimeMillis() + 10_000;}
    }
    /** Таймер жажды **/
    private static long thirstTimer = 0;
    /** Управление жаждой **/
    private static void handleThirst() {
        //Если жажда выше 100, снижаем до 100
        if(Game.player.thirst > 100) Game.player.thirst = 100;
        //Если жажда нише 0, повышаем до 0
        if(Game.player.thirst < 0) Game.player.thirst = 0;
        //Проверяем таймер жажды
        if(thirstTimer > System.currentTimeMillis()) return;
        Game.player.thirst +=0.3; //Повышаем жажду на 1
        thirstTimer = System.currentTimeMillis() + 2_000; //Каждые 2 секунды (4 игровых минут)
        }

    /** Таймер сонливости **/
    private static long sleepyTimer = 0;
    /** Управление сонливостью **/
    private static void handleSleepy() {
        //Если сонливость выше 100, снижаем до 100
        if(Game.player.sleepy > 100) Game.player.sleepy = 100;
        //Если сонливость нише 0, повышаем до 0
        if(Game.player.sleepy < 0) Game.player.sleepy = 0;
        //Проверяем таймер сонливости
        if(sleepyTimer > System.currentTimeMillis()) return;
        Game.player.sleepy +=0.4; //Повышаем сонливость на 1
        sleepyTimer = System.currentTimeMillis() + 5_150; //Каждые 5.15 секунд (~10 игровых минут)
    }

    private static void handleCop() {
        if(Game.player.wanted > 0) Game.player.wanted -= 0.005 * (Game.player.wanted/10);
    }

    private static void handleDeath() {
        if(Game.player.hp <= 0)
            Minigame.current = Minigame.get(14);
    }

    private static void handleBusy() {
        boolean busy = false;
        if(Minigame.current != null) busy = true;
        for (int i = 0; i < Game.currentLevel.objects.size(); i++)
            if (Game.currentLevel.objects.get(i).opened) {
                busy = true;
                break;
            }
        if(Dialogue.current != null) busy = true;
        Game.player.busy = busy;
    }

    //TODO по идее это должно быть скорее в спавнере чем тут
    private static void handleNoise() {
        //Если шухер высокий, то будем спавнить жителей.
        if(Game.currentLevel.isIndoors && Game.currentLevel.noise > 99)
        {
            //TODO: вынеси в метод "спавн соседей"
            //Ищем ближайшую к игроку зону спавна
            GameObject nearestSpawnZone = null;
            List<GameObject> zones = Game.currentLevel.objects.stream().filter(o -> o.id == 222).toList();
            if (!zones.isEmpty())
            {
                nearestSpawnZone = zones.get(0);
                for (GameObject z : zones) {

                    Point2D playerCenter = new Point2D.Double(Game.player.hitBox.getCenterX(),Game.player.hitBox.getCenterY());
                    Point2D zoneCenter = new Point2D.Double(z.hitBox.getCenterX(),z.hitBox.getCenterY());
                    Point2D nearestZoneCenter = new Point2D.Double(nearestSpawnZone.hitBox.getCenterX(),nearestSpawnZone.hitBox.getCenterY());

                    if(playerCenter.distance(nearestZoneCenter) > playerCenter.distance(zoneCenter)) nearestSpawnZone = z;

                }
            }
            //Если зона нашлась
            if(nearestSpawnZone != null)
            {
                if(Math.random() < 0.001) {
                    //С небольшим шансом добавляем врага
                    GameObject enemy = GameObject.get(4);
                    enemy.x = nearestSpawnZone.x;
                    enemy.y = nearestSpawnZone.y;
                    Game.currentLevel.objects.add(enemy);
                }
            }
        }
        for (Map.Entry<Integer, Level> entry : Level.repo.entrySet())
        {
            //Проходимся по уровням и снижаем шухер
            Level level = entry.getValue();
            if(level.isIndoors && level.noise > 0)  level.noise -=0.001;

        }

    }
}
