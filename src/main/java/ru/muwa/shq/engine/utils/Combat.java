package ru.muwa.shq.engine.utils;
import ru.muwa.shq.engine.Game;
import ru.muwa.shq.engine.GameWindow;
import ru.muwa.shq.entities.GameObject;
import ru.muwa.shq.entities.Item;
import ru.muwa.shq.story.scripts.Script;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import static ru.muwa.shq.engine.utils.Animation.*;
import static ru.muwa.shq.engine.utils.Momma.Status.SHOOT;
import static ru.muwa.shq.entities.GameObject.Type.CREATURE;

public class Combat {
    public static double angle;
    public static Line2D line = new Line2D.Double();
    public static HashMap<GameObject , Double> bulletAngles = new HashMap<>();
    public static HashMap <GameObject,Double> bulletTimer = new HashMap<>();
    public  static HashMap<GameObject, Point2D> BInitialPoints = new HashMap<>();
    public static final double ATTACK_DISTANCE = 3.0;
    public static final int BULLET_SPEED = 150;
    private static final HashMap<Integer,Integer> ammoTypeMap = new HashMap<>();
    private static final HashMap<Integer,Long> wantedMap = new HashMap<>();
    private static final HashMap<GameObject,Long> corpseTimers = new HashMap<>();
    private static final long CORPSE_DELETE_INTERVAL = 60_000; // 1 min

    static
    {
        //Указываем связи патрон с оружием
        ammoTypeMap.put(3,9); //Макаров и 9х18 мм
        ammoTypeMap.put(92,107); //Патроны для обреза
        ammoTypeMap.put(126,127);//АК

        //Указываем связи убийств с беспределом
        wantedMap.put(6,15L); //Горожанин
        wantedMap.put(7,30L);//Мент
        wantedMap.put(129,50L);//Военный
    }

    public static void work()
    {
        calculateAngle();
        moveBullets();
        checkBullets();
        removeCorpses();
    }

    private static void removeCorpses() {
        for(var e : corpseTimers.entrySet()) {
            if (e.getValue() < System.currentTimeMillis()) {
                Game.currentLevel.objects.remove(e.getKey());

            }
        }
    }

    private static void checkBullets() {
        for(int i = 0; i < Game.currentLevel.objects.size(); i++)
        {
            GameObject bullet = Game.currentLevel.objects.get(i);
            if (bullet.id == 666 || bullet.id == 666_01)
            {
                for (int j = 0; j < Game.currentLevel.objects.size(); j++) {
                    GameObject creature = Game.currentLevel.objects.get(j);
                    if(creature.solid && creature.hitBox.contains(bullet.x,bullet.y))
                        if(bullet.id!=666_01)Game.currentLevel.objects.remove(bullet);
                    if (creature.type != CREATURE) continue;
                    if (creature == Game.player) continue;
                    if (!creature.hitBox.contains(new Point(bullet.x, bullet.y))) continue;
                    //Пуля игрока попала в существо
                    creature.x += 10 * Math.cos(bulletAngles.get(bullet));
                    creature.y += 10 * Math.sin(bulletAngles.get(bullet));
                    dealDamage(creature, 100 + ((bullet.id==666_01)?200:0));
                    if(bullet.id==666) {
                        Game.currentLevel.objects.remove(bullet);
                        bulletAngles.remove(bullet);
                        bulletTimer.remove(bullet);
                        BInitialPoints.remove(bullet);
                    }
                    creature.enemy = true;
                }
            }
            if(bullet.id == 777 || bullet.id == 146)
            {
                if(Game.player.hitBox.intersects(bullet.hitBox)){
                    Combat.dealDamage(Game.player, 25);
                    Game.currentLevel.objects.remove(bullet);
                    bulletAngles.remove(bullet);
                    bulletTimer.remove(bullet);
                    BInitialPoints.remove(bullet);
                }
                if(bullet.id != 146 && bullet.id != 666_01){
                    for (int j = 0; j < Game.currentLevel.objects.size(); j++) {
                        var object = Game.currentLevel.objects.get(j);
                        if(object.solid && object.hitBox.contains(bullet.x,bullet.y) && bulletTimer.get(bullet)>1)
                            Game.currentLevel.objects.remove(bullet);
                    }
                }
            }

        }
    }

    private static void moveBullets() {
        for (int i = 0; i < Game.currentLevel.objects.size(); i++) {

            GameObject bullet = Game.currentLevel.objects.get(i);
            if (bullet.id != 666 && bullet.id != 777 && bullet.id != 146 && bullet.id != 666_01) continue;
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

            if (bullet.id == 146) {

                bullet.x = (int) (BInitialPoints.get(bullet).getX() + ((BULLET_SPEED/1.5) * accelerationX) * bulletTimer.get(bullet)  );
                bullet.y = (int) (BInitialPoints.get(bullet).getY() + ((BULLET_SPEED/1.5) * accelerationY) * bulletTimer.get(bullet)  );

            }else if(bullet.id == 666_01){
                bullet.x = (int) (BInitialPoints.get(bullet).getX() + (BULLET_SPEED / 5.0 * accelerationX) * bulletTimer.get(bullet)  );
                bullet.y = (int) (BInitialPoints.get(bullet).getY() + (BULLET_SPEED / 5.0 * accelerationY) * bulletTimer.get(bullet)  );

            }
            else{
                bullet.x = (int) (BInitialPoints.get(bullet).getX() + (BULLET_SPEED * accelerationX) * bulletTimer.get(bullet)  );
                bullet.y = (int) (BInitialPoints.get(bullet).getY() + (BULLET_SPEED * accelerationY) * bulletTimer.get(bullet)  );

            }

        }
    }

    private static void calculateAngle() {
        double centerX = (double) GameWindow.WIDTH / 2;
        double centerY = (double) GameWindow.HEIGHT / 2;

        double mouseX = Input.mouse.x - 6;
        double mouseY = Input.mouse.y - 30;

        // Вычисляем угол между векторами
        if(!Game.player.busy) angle = Math.atan2(mouseY - centerY, mouseX - centerX);

        int px = (int) Game.player.hitBox.getCenterX();
        int py = (int) Game.player.hitBox.getCenterY();
        line = new Line2D.Double(px,py,mouseX+Camera.x,mouseY+Camera.y);

    }
    //Наносим урон
    public static void dealDamage(GameObject target, int damage) {
        //Если цель - игрок, понижаем урон на коэффициент его брони
        if(target == Game.player){
            damage = (damage * (100-Game.player.bonusHp)) / 100;
            //И повышаем нервозность
            Game.player.crazy += 0.5;
        }
        //Если цель - босс мама, тут своя логика
        if(target == Momma.boss)
        {
            //Если сбиваем хп ниже 75%, мама переходит в режим стрельбы
            if(Momma.boss.hp > ((Momma.boss.startHp * 74) / 100)
            && Momma.boss.hp - damage < ((Momma.boss.startHp * 75) / 100)){
                Momma.status = SHOOT;
                Momma.shootPhaseTimer = System.currentTimeMillis();

            }

            //Если сбиваем хп ниже 50%, мама переходит в режим стрельбы
            if(Momma.boss.hp > ((Momma.boss.startHp * 49) / 100)
                    && Momma.boss.hp - damage < ((Momma.boss.startHp * 50) / 100)) {
                Momma.status = SHOOT;
                Momma.shootPhaseTimer = System.currentTimeMillis();
            }
            //Если сбиваем хп ниже 25%, мама переходит в режим стрельбы
            if(Momma.boss.hp > ((Momma.boss.startHp * 24) / 100)
                    && Momma.boss.hp - damage < ((Momma.boss.startHp * 25) / 100)) {
                Momma.status = SHOOT;
                Momma.shootPhaseTimer = System.currentTimeMillis();

            }

        }
        //Снижаем здоровье цели
        target.hp = target.hp - damage;
        //Убираем цель (не игрока) с карты в случае смерти
        if(!target.equals(Game.player))
        {
            //Убийство
            if(target.hp <=0 ) {
                //Убираем с карты убитого
                Game.currentLevel.objects.remove(target);
                //Добавляем беспредел
                Game.player.wanted += wantedMap.get(target.id)==null?0:wantedMap.get(target.id);
                //Спавним труп
                addCorpse(target);
                if(target.id == 124) Script.zekKilled += 1;
                if(target.id == 4 || target.id == 128) Script.gopKilled += 1;
            }
        }

    }

    public static void addCorpse(GameObject target) {
        if(target.id == 151)
        {
            //босс мутант
            var uranium = GameObject.get(152);
            uranium.x = target.x;
            uranium.y = target.y;
            Game.currentLevel.objects.add(uranium);
            return;
        }
        GameObject corpse = GameObject.get(8); //TODO: сделать зависимость id трупа от id цели
        corpse.x = target.x;
        corpse.y = target.y;
        addLoot(corpse,target);
        Game.currentLevel.objects.add(corpse);
        corpseTimers.put(corpse , System.currentTimeMillis() + CORPSE_DELETE_INTERVAL);
    }

    private static void addLoot(GameObject corpse, GameObject target) {
        var loot = lootChances.get(target.id);
        if(loot==null) return;
        for (Map.Entry<Integer,Double> entry : loot.entrySet())
        {
            var chance = Math.random();
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
            //иначе делаем одну
            GameObject bullet = GameObject.get(666);
            if(Game.player.equip.id == 134) bullet = GameObject.get(666_01);
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
        if(npc == Momma.boss) bullet = GameObject.get(146);
        bullet.x = (int) (npc.hitBox.getCenterX());
        bullet.y = (int) (npc.hitBox.getCenterY());
        bulletAngles.put(bullet, customAngle);
        bulletTimer.put(bullet, 0.0);
        BInitialPoints.put(bullet, new Point2D.Double(bullet.x, bullet.y));
        Game.currentLevel.objects.add(bullet);
        if(npc==Momma.boss
        && Momma.status == SHOOT
        && Momma.boss.hp < Momma.boss.startHp /2 + 100){
            for (int i = 0; i < 3;i++) {
                GameObject momBullet = GameObject.get(146);
                momBullet.x = (int) npc.hitBox.getCenterX();
                momBullet.y = (int) npc.hitBox.getCenterY();
                bulletAngles.put(momBullet,customAngle + ((-1 + i)* 0.1));
                bulletTimer.put(momBullet,0.0);
                BInitialPoints.put(momBullet,new Point2D.Double(momBullet.x,momBullet.y));
                Game.currentLevel.objects.add(momBullet);
            }
        }
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
        if(gun.id==3) Animation.addAnimation(Game.player,Animation.get(PL_PISTOL_RELOAD));
        if(gun.id==92 || gun.id == 126) Animation.addAnimation(Game.player,Animation.get(PL_SHOTGUN_RELOAD));

    }

    private static final HashMap<Integer, HashMap<Integer,Double>> lootChances = new HashMap<>(); //В мапе хранится ключ - id моба. значение - мапа где ключ - id айтема , значение - шанс дропа (от 0.0 до 1.0)
    static
    {
        // Пешеход (6)
        var loot = new HashMap<Integer,Double>();
        loot.put(8,0.75); // 100 рублей
        loot.put(103,0.2); // 500 рублей
        loot.put(104,0.1); // 1000 рублей
        loot.put(105,0.05); // 5000 рублей
        loot.put(74,0.4); // Банковская карта
        loot.put(67,0.2); // зажигалка
        loot.put(66,0.1); // Сигареты
        loot.put(106,0.1); // Телефон
        lootChances.put(6, loot);

        // Кассир (33331)
        loot = new HashMap<>();
        loot.put(68,1.0); // Бейдж
        lootChances.put(33331,loot);

        //мент (7)
        loot = new HashMap<>();
        loot.put(3,0.2); // Макарыч
        loot.put(9,0.66); // Патроны для макара
        loot.put(8,0.75); // 100 рублей
        loot.put(103,0.2); // 500 рублей
        loot.put(104,0.1); // 1000 рублей
        loot.put(105,0.05); // 5000 рублей
        loot.put(74,0.4); // Банковская карта
        loot.put(67,0.2); // зажигалка
        loot.put(66,0.1); // Сигареты
        lootChances.put(7,loot);


        //Собака (130)
        loot = new HashMap<>();
        loot.put(117,0.3); //Собачье мясо
        lootChances.put(130,loot);

        //Военный (129)
        loot = new HashMap<>();
        loot.put(126,0.25); //AK 74
        loot.put(127,0.5);//Патроны для ак
        loot.put(125,0.25);//шлем (каска военная)
        loot.put(122,0.1);//Броня
        loot.put(94,0.25);//Берцы
        lootChances.put(129,loot);

        //гопник (синий)
        loot = new HashMap<>();
        loot.put(8,0.75); // 100 рублей
        loot.put(103,0.2); // 500 рублей
        loot.put(67,0.4); // зажигалка
        loot.put(66,0.2); // Сигареты
        lootChances.put(4,loot);

        //гопник (серый)
        loot = new HashMap<>();
        loot.put(8,0.8); // 100 рублей
        loot.put(103,0.4); // 500 рублей
        loot.put(67,0.4); // зажигалка
        loot.put(66,0.3); // Сигареты
        loot.put(114,0.3);//заточка
        lootChances.put(128,loot);

        //Иммигрант (138)
        loot = new HashMap<>();
        loot.put(121,0.4); // Поддельная регистрация
        loot.put(8,0.4); // 100 рублей
        loot.put(123,0.1);//Каска
        lootChances.put(138,loot);

        //Фашик (9191)
        loot = new HashMap<>();
        loot.put(135,1.0); // Поддельная регистрация
        lootChances.put(9191,loot);



    }

    public static double calculateAngleForNpc(GameObject o) {

        if(!AI.seesPlayer(o))
        {
            return Math.atan2(o.destY - o.hitBox.getCenterY() , o.destX - o.hitBox.getCenterX());
        }

        double centerX = o.x + (double) o.hitBox.width /2;
        double centerY = o.y + (double) o.hitBox.height/2;

        double playerX = Game.player.x + 20;
        double playerY = Game.player.y + 10;

        // Вычисляем угол между векторами
        return Math.atan2(playerY - centerY, playerX - centerX);
    }
}