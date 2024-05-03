package ru.muwa.shq.engine.utils;

import ru.muwa.shq.engine.Game;
import ru.muwa.shq.entities.GameObject;
import ru.muwa.shq.entities.Player;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.muwa.shq.entities.GameObject.Type.CREATURE;

/**
 *
 * Класс ИИ.aaqq
 * Управляет игровыми персонажами
 *
 */
public class AI {

    //ПОЛЯ
    //Мапа ожиданий. (ключ - ждущий персонаж, значение - таймер)
    public final static HashMap<GameObject, Long> waitMap = new HashMap<>();
    //Интервал ожидания (для таймера ожиданий)
    public static final long WAIT_INTERVAL = 500; // 0.5 sec
    //Список id стреляющих НПС
    public static final List<Integer> shootingNPC = List.of(7);
    //Мапа ожиданий после выстрела. (ключ - стрельнувший нпс. значение - таймер до сл. выстрела)
    public static final Map<GameObject,Long> shotsTimer = new HashMap<>();
    //Безопасная дистанция по мнению стреляющих нпс (они бегут от Шкипера если расстояние не безопасно)
    public static final int SAFE_DIST = 300;

    //Основной метод. Вызывает остальные методы службы ИИ
    public static void work()
    {
        movePedestrian();
        moveEnemy();
        moveShootingEnemy();
        callbackCollisions();
        traffic();
    }

    //Управляем машинами
    private static void traffic() {

    }
    //Проходимся по НПЦ и передаем их службе физики чтобы проверить столкновения
    private static void callbackCollisions() {
        for (int i = 0; i <Game.currentLevel.objects.size(); i++) {
            GameObject object = Game.currentLevel.objects.get(i);
            if(object.type == CREATURE && Game.player != object)
                Physx.NPCCollisions(object);
        }
    }

    //Двигаем пешеходов
    private static void movePedestrian() {
        List<GameObject> pedestrians = Game.currentLevel.objects.stream().filter(o->(!o.enemy && o.type == CREATURE)).toList();
        for (GameObject p : pedestrians)
        {

            if(p == Game.player) continue;
            if(p.hitBox.contains(new Point(p.destX,p.destY)))
            {
                p.destX = -1; p.destY =-1;
            }


            if (p.destY == -1 && p.destX == -1){ if(Math.random() < 0.05) setRandomDestination(p); continue;};


            if (p.hitBox.getCenterX() < p.destX) {
                p.moveRight();
            } else {
                p.moveLeft();
            }
            if (p.hitBox.getCenterY() < p.destY) {
                p.moveDown();
            } else {
                p.moveUp();
            }

        }
    }

    //Двигаем врагов
    private static void moveEnemy() {

        for (int i = 0; i < Game.currentLevel.objects.size(); i++) {

            GameObject o = Game.currentLevel.objects.get(i);

            if(o == Game.player) continue;


            if(!o.type.equals(CREATURE)) continue;
            if(shootingNPC.contains(o.id)) continue;
            if(waitMap.containsKey(o) && waitMap.get(o) > GameTime.value) continue;

            if(o.enemy)
            {

                if(o.hitBox.intersects(Game.player.hitBox))
                {
                    Combat.dealDamage(Game.player,o.damage);
                    waitMap.put(o,GameTime.value + WAIT_INTERVAL);
                }
                if(seesPlayer(o)){
                    o.destX = (int) Game.player.hitBox.getCenterX();
                    o.destY = (int) Game.player.hitBox.getCenterY();
                }

                if(o.hitBox.contains(new Point(o.destX,o.destY)))
                {
                    o.destX = -1; o.destY =-1;
                }
                if(o.destX == -1 && o.destY == -1)
                {
                    setRandomDestination(o);
                }

                if (o.hitBox.getCenterX() < o.destX) {
                    o.moveRight();
                } else {
                    o.moveLeft();
                }
                if (o.hitBox.getCenterY() < o.destY) {
                    o.moveDown();
                } else {
                    o.moveUp();
                }

            }
            
        }
    }


    private static void moveShootingEnemy() {
        for (int i = 0; i < Game.currentLevel.objects.size(); i++) {
            GameObject object = Game.currentLevel.objects.get(i);
            if(!shootingNPC.contains(object.id)) continue;

            if(object.hitBox.intersects(Game.player.hitBox))
            {
                Combat.dealDamage(Game.player,object.damage);
                waitMap.put(object,GameTime.value + WAIT_INTERVAL);
            }
            if(seesPlayer(object)){
                object.destX = (int) Game.player.hitBox.getCenterX();
                object.destY = (int) Game.player.hitBox.getCenterY();
            }

            if(object.hitBox.contains(new Point(object.destX,object.destY)))
            {
                object.destX = -1; object.destY =-1;
            }
            if(object.destX == -1 && object.destY == -1)
            {
                setRandomDestination(object);
            }

            Rectangle safeZone = new Rectangle(object.x-SAFE_DIST/2,object.y-SAFE_DIST/2,SAFE_DIST,SAFE_DIST);
            if(Game.player.hitBox.intersects(safeZone))
            {
                if (object.hitBox.getCenterX() < Game.player.x) {
                    object.moveLeft();
                } else {
                    object.moveRight();
                }
                if (object.hitBox.getCenterY() < Game.player.y) {
                    object.moveUp();
                } else {
                    object.moveDown();
                }
            }
            else

            if (object.hitBox.getCenterX() < object.destX) {
                object.moveRight();
            } else {
                object.moveLeft();
            }
            if (object.hitBox.getCenterY() < object.destY) {
                object.moveDown();
            } else {
                object.moveUp();
            }

            if(!seesPlayer(object)) setRandomDestination(object);

            //shoot logic
            if(seesPlayer(object) &&(!shotsTimer.containsKey(object) || shotsTimer.get(object) < GameTime.value))
            {
                 Combat.enemyShoot(object);
                 shotsTimer.put(object,GameTime.value + WAIT_INTERVAL * 5);
            }
        }
    }

    public static void setRandomDestination(GameObject o) {

        int x = (int) (1+(Math.random() * 6) * (0.3+Math.random() * 100) * (Math.random()>0.5? 1 : -1));
        int y = (int) (1+(Math.random() * 6) * (0.3+Math.random() * 100) * (Math.random()>0.5? 1 : -1));

        Line2D line = new Line2D.Double(o.x+x,o.y+y,o.x,o.y);

        for (int i = 0; i < Game.currentLevel.objects.size(); i++) {
            if(Game.currentLevel.objects.get(i).type == CREATURE) continue;
            if(Game.currentLevel.objects.get(i).solid && line.intersects(Game.currentLevel.objects.get(i).hitBox))
            {
                o.destY = -1;
                o.destX = -1;
                return;
            }
        }

        o.destX = o.x + x;
        o.destY = o.y + y;
    }

    private static boolean seesPlayer(GameObject o) {

        Player p = Game.player;
        Line2D line = new Line2D.Double(o.hitBox.getCenterX(), o.hitBox.getCenterY(), p.hitBox.getCenterX(), p.hitBox.getCenterY());

        for (int i = 0; i < Game.currentLevel.objects.size(); i++) {

            GameObject obst = Game.currentLevel.objects.get(i);

            if(obst == o || obst == p) continue;
            if(!obst.solid) continue;

            // Проверяем пересечение линии с хитбоксом объекта obst
            if (line.intersects(obst.hitBox)) {
                return false; // Если есть пересечение, возвращаем false
            }

        }
        // Если не найдено пересечений, возвращаем true
        return true;
    }
}
