package ru.muwa.shq.engine.utils;
import com.sun.management.GarbageCollectorMXBean;
import ru.muwa.shq.engine.Game;
import ru.muwa.shq.engine.GameWindow;
import ru.muwa.shq.entities.GameObject;
import ru.muwa.shq.entities.Item;
import ru.muwa.shq.entities.Player;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.muwa.shq.entities.GameObject.Type.CREATURE;

public class Combat {
    public static double angle;
    public static Line2D line = new Line2D.Double();
    public static HashMap<GameObject , Double> bulletAngles = new HashMap<>();
    public static HashMap <GameObject,Double> bulletTimer = new HashMap<>();
    public  static HashMap<GameObject, Point2D> BInitialPoints = new HashMap<>();
    public static final double ATTACK_DISTANCE = 3.0;
    public static final int BULLET_SPEED = 120;
    private static final HashMap<Integer,Integer> ammoTypeMap = new HashMap<>();

    static
    {
        ammoTypeMap.put(3,9); //Макаров и 9х18 мм
    }

    public static void work()
    {
        calculateAngle();
        moveBullets();
        checkBullets();
    }

    private static void checkBullets() {
        for(int i = 0; i < Game.currentLevel.objects.size(); i++)
        {
            GameObject bullet = Game.currentLevel.objects.get(i);
            if (bullet.id == 666)
            {
                for (int j = 0; j < Game.currentLevel.objects.size(); j++) {
                    GameObject creature = Game.currentLevel.objects.get(j);
                    if (creature.type != CREATURE) continue;
                    if (creature == Game.player) continue;
                    if (!creature.hitBox.contains(new Point(bullet.x, bullet.y))) continue;
                    //Пуля игрока попала в существо
                    creature.x += 10 * Math.cos(bulletAngles.get(bullet));
                    creature.y += 10 * Math.sin(bulletAngles.get(bullet));
                    dealDamage(creature, 50);
                    Game.currentLevel.objects.remove(bullet);
                    bulletAngles.remove(bullet);
                    bulletTimer.remove(bullet);
                    BInitialPoints.remove(bullet);
                }
            }
            if(bullet.id == 777 && Game.player.hitBox.intersects(bullet.hitBox))
            {
                Combat.dealDamage(Game.player,10);
                Game.currentLevel.objects.remove(bullet);
                bulletAngles.remove(bullet);
                bulletTimer.remove(bullet);
                BInitialPoints.remove(bullet);
            }

        }
    }

    private static void moveBullets() {
        for (int i = 0; i < Game.currentLevel.objects.size(); i++) {

            GameObject bullet = Game.currentLevel.objects.get(i);
            if (bullet.id != 666 && bullet.id != 777) continue;
            if(!bulletTimer.containsKey(bullet)){ Game.currentLevel.objects.remove(bullet);continue;}
            bulletTimer.put(bullet, bulletTimer.get(bullet) + 0.1);
            if(bulletTimer.get(bullet) > 30.0){
                Game.currentLevel.objects.remove(bullet);
                bulletAngles.remove(bullet); bulletTimer.remove(bullet); BInitialPoints.remove(bullet);
                continue;
            }

            double angleInRadians = bulletAngles.get(bullet);
            double accelerationX = Math.cos(angleInRadians);
            double accelerationY = Math.sin(angleInRadians);

            bullet.x = (int) (BInitialPoints.get(bullet).getX() + (BULLET_SPEED * accelerationX) * bulletTimer.get(bullet)  );
            bullet.y = (int) (BInitialPoints.get(bullet).getY() + (BULLET_SPEED * accelerationY) * bulletTimer.get(bullet)  );

        }
    }

    private static void calculateAngle() {
        double centerX = (double) GameWindow.WIDTH / 2;
        double centerY = (double) GameWindow.HEIGHT / 2;

        double mouseX = Input.mouse.x - (GameWindow.fullscreen?0:6) ;
        double mouseY = Input.mouse.y - (GameWindow.fullscreen?0:30);

        // Вычисляем угол между векторами
        angle = Math.atan2(mouseY - centerY, mouseX - centerX);

        int px = (int) Game.player.hitBox.getCenterX();
        int py = (int) Game.player.hitBox.getCenterY();
        line = new Line2D.Double(px,py,mouseX+Camera.x,mouseY+Camera.y);

    }
    //Наносим урон
    public static void dealDamage(GameObject target, int damage) {
        //Если цель - игрок, понижаем урон на коэффициент его брони
        if(target == Game.player) damage = (damage * (100-Game.player.bonusHp)) / 100;
        //Снижаем здоровье цели
        target.hp = target.hp - damage;
        //Убираем цель (не игрока) с карты в случае смерти
        if(!target.equals(Game.player))
        {
            //Убийство
            if(target.hp <=0 ) {
                //Убираем с карты убитого
                Game.currentLevel.objects.remove(target);
                //TODO лучше увеличивать беспредел индивидуально в зависимости от убитого. Можно сделать через мапу
                Game.player.wanted += 25;
                //Спавним труп
                addCorpse(target);
            }
        }
    }

    private static void addCorpse(GameObject target) {
        GameObject corpse = GameObject.get(8); //TODO: сделать зависимость id трупа от id цели
        corpse.x = target.x;
        corpse.y = target.y;
        addLoot(corpse,target);
        Game.currentLevel.objects.add(corpse);
    }

    private static void addLoot(GameObject corpse, GameObject target) {
        var loot = lootChances.get(target.id);
        if(loot==null) return;
        for (Map.Entry<Integer,Double> entry : loot.entrySet())
        {
            var chance = Math.random();
            System.out.println("chance : " +chance + " entry : "+ entry.getKey() + " - " + entry.getValue());
            if(chance > entry.getValue()) continue;
            corpse.addItem(Item.get(entry.getKey()));
        }

    }

    public static void shoot()
    {
        //Если стреляем с дробовика
        if(Game.player.equip.id == 92){
            //Делаем 3 пули
            for (int i = 0; i < 3; i++) {
                GameObject bullet = GameObject.get(666);
                bullet.x = (int) Game.player.hitBox.getCenterX();
                bullet.y = (int) Game.player.hitBox.getCenterY();
                bulletAngles.put(bullet,angle + ((-1 + i)* 0.1));
                bulletTimer.put(bullet,0.0);
                BInitialPoints.put(bullet,new Point2D.Double(bullet.x,bullet.y));
                Game.currentLevel.objects.add(bullet);
            }
        }else {
            //Иначе делаем одну
            GameObject bullet = GameObject.get(666);
            bullet.x = (int) Game.player.hitBox.getCenterX();
            bullet.y = (int) Game.player.hitBox.getCenterY();
            bulletAngles.put(bullet, angle);
            bulletTimer.put(bullet, 0.0);
            BInitialPoints.put(bullet, new Point2D.Double(bullet.x, bullet.y));
            Game.currentLevel.objects.add(bullet);
        }
    }
    public static void enemyShoot(GameObject npc)
    {
        double customAngle = Math.atan2(Game.player.hitBox.getCenterY() - npc.hitBox.getCenterY(), Game.player.hitBox.getCenterX() - npc.hitBox.getCenterX());
        GameObject bullet = GameObject.get(777);
        bullet.x = (int) npc.hitBox.getCenterX();
        bullet.y = (int) npc.hitBox.getCenterY();
        bulletAngles.put(bullet,customAngle);
        bulletTimer.put(bullet,0.0);
        BInitialPoints.put(bullet,new Point2D.Double(bullet.x,bullet.y));
        Game.currentLevel.objects.add(bullet);
    }

    public static void kickback(GameObject target,int distance, double angle) {
        target.x += (int) (Math.cos(angle)* distance);
        target.y += (int) (Math.sin(angle)*distance);
    }

    public static void Reload(Item gun, Item ammo) {
        if(gun == null || ammo == null) return;
        if(!ammoTypeMap.containsValue(ammo.id) || !ammoTypeMap.containsKey(gun.id)) return;
        if(gun.ammo == gun.maxAmmo) return;
        if(ammo.id == ammoTypeMap.get(gun.id))
        {
            gun.ammo = gun.maxAmmo;
            if(ammo.count > 1) ammo.count -= 1;
            else Game.player.items.remove(ammo);
        }
    }

    private static final HashMap<Integer, HashMap<Integer,Double>> lootChances = new HashMap<>(); //В мапе хранится ключ - id моба. значение - мапа где ключ - id айтема , значение - шанс дропа (от 0.0 до 1.0)
    static
    {
        // Пешеход (6)
        var loot = new HashMap<Integer,Double>();
        loot.put(8,0.5); // 100 рублей
        loot.put(67,0.1); // зажигалка
        loot.put(66,0.05); // Сигареты
        lootChances.put(6, loot);

        // Кассир (33331)
        loot = new HashMap<>();
        loot.put(68,1.0); // Бейдж
        lootChances.put(33331,loot);

        //мент (7)
        loot = new HashMap<>();
        loot.put(3,0.1);
        loot.put(9,0.33);
        loot.put(8,0.5); // 100 рублей
        lootChances.put(7,loot);

    }
}