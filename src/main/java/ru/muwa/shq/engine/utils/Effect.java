package ru.muwa.shq.engine.utils;

import ru.muwa.shq.engine.Game;
import ru.muwa.shq.engine.Renderer;
import ru.muwa.shq.entities.GameObject;
import ru.muwa.shq.entities.Item;
import ru.muwa.shq.entities.Level;
import ru.muwa.shq.entities.Minigame;
import ru.muwa.shq.story.Dialogue;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;

public class Effect {

    public static void work()
    {
        handleNoise();
        handleBusy();
        handleDeath();
        handleCop();

        if(Game.player.hunger < 0) Game.player.hunger = 0;
        if(Game.player.thirst < 0) Game.player.thirst = 0;
        if(Game.player.sleepy < 0) Game.player.sleepy = 0;
        if(Game.player.crazy < 0) Game.player.crazy = 0;

        //Инициализация переменных в которых будем считать сумму бонусов
        int totalBonusHp = 0, totalBonusStamina = 0, totalBonusPsycho = 0, totalBonusSpeed = 0, totalBonusSprint = 0,
                totalCrazyBonus = 0;

        //Восстановление запаса сил
        if(Game.player.stamina < Game.player.baseStamina + Game.player.bonusStamina) Game.player.stamina += 0.3 * ((Game.player.baseStamina+Game.player.bonusStamina)/100);

        //Рост голода, жажды и сонливости
        // +=0.005 дает примерно 50% за игровые 5:30, 100% за 11 часов. +=0.002 дает примерно 25% за 7:30 игровых часов
        if(Game.player.hunger < 101) Game.player.hunger +=0.003;
        if(Game.player.thirst < 101) Game.player.thirst +=0.005; //50% за 5:30ч; 100% за 11ч
        if(Game.player.sleepy < 101) Game.player.sleepy +=0.0022; //27% за 7:30ч; 55% за 15ч; 100% за 30ч //Расчет в том, что за 15 часов накапает 55% сна. Для сна на 8 часов нужно 56% сна. Выходит идеальное соотношение для здорового сна 16:8

        //Обработка активных эффектов
        for (int i = 0; i < Game.player.effects.size(); i++) {

            Item.Effect e = Game.player.effects.get(i);

            //Удаляем эффект, если закончилось время его действия
            if(e.expirationTime <= GameTime.get() && !e.permanent) Game.player.effects.remove(e);

            //Применяем восстановление параметров игрока из эффекта.
            if(e.regHpBonus > 0)
            {
                Game.player.hp = Math.min( Game.player.hp + e.regHpBonus , Game.player.baseHp );
                e.regHpBonus = 0;
            }
            if(e.crazy != 0)
            {
                Game.player.crazy = Math.min(Game.player.crazy+e.crazy, 100);
                e.crazy = 0;
            }
            if(e.regStaminaBonus != 0)
            {
                Game.player.stamina = Math.min( Game.player.stamina + e.regStaminaBonus , Game.player.baseStamina);
                e.regStaminaBonus = 0;
            }
            if(e.pee != 0)
            {
                Game.player.pee += e.pee;
                e.pee = 0;
            }
            if(e.poo != 0)
            {
                Game.player.poo += e.poo;
                e.poo = 0;
            }
            if(e.hunger != 0)
            {
                Game.player.hunger += Math.min(100, e.hunger);
                e.hunger = 0;
            }
            if(e.thirst != 0)
            {
                Game.player.thirst += Math.min(100, e.thirst);
                e.thirst = 0;
            }
            if(e.sleepy != 0)
            {
                Game.player.sleepy += Math.min(100, e.sleepy);
                e.sleepy = 0;
            }


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

        //Применяем бонусы активных эффектов
        Game.player.bonusHp = totalBonusHp;
        Game.player.bonusStamina = totalBonusStamina;
        Game.player.bonusPsycho = totalBonusPsycho;
        Game.player.bonusSpeed = totalBonusSpeed;
        Game.player.bonusSprint = totalBonusSprint;
    }

    private static void handleCop() {
        if(Game.player.wanted > 0) Game.player.wanted -=0.0005;
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

    private static void handleNoise() {

        //Если шухер высокий, то будем спавнить жителей.
        if(Game.currentLevel.isIndoors && Game.currentLevel.noise > 99)
        {
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
            if(level.isIndoors && level.noise > 0)  level.noise -=0.0005;

        }

    }
}
