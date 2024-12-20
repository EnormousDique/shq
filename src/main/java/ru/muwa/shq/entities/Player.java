package ru.muwa.shq.entities;

import ru.muwa.shq.engine.Game;
import ru.muwa.shq.engine.utils.Animation;
import ru.muwa.shq.engine.utils.Combat;
import ru.muwa.shq.engine.utils.Input;
import ru.muwa.shq.story.Quest;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static ru.muwa.shq.engine.utils.Animation.PL_PUNCH;
import static ru.muwa.shq.engine.utils.GameTime.DAY_LENGTH;
import static ru.muwa.shq.engine.utils.Input.KListener.SHIFT;
import static ru.muwa.shq.entities.Item.Type.MELEE;
import static ru.muwa.shq.entities.Item.Type.MONEY;

public class Player extends GameObject {

    public int id = 0;
    public int brush = -1;
    public double pee=0,poo=0;
    public int money = 0;
    public int attackDist = 100;
    public final int ITEMS_CAPACITY = 10;
    public double hunger = 0, thirst = 0, /* nuts */ crazy = 0, sleepy = 0, wanted = 0,smoke = 0, stimulate = 0, drunk = 0;
    public boolean busy,dead,rent,good, atm;
    public ArrayList<Item.Effect> effects = new ArrayList<>();
    public ArrayList<Item> items = new ArrayList<>();
    public ArrayList<Quest> quests = new ArrayList<>();
    public Item equip;
    public int bonusHp, baseHp,bonusPsycho,basePsycho, baseSpeed,baseSprint = 6, bonusSpeed, bonusSprint;
    public int busFare = 100;

    public double stamina, bonusStamina,baseStamina = 100;
    public long mommaFullness = DAY_LENGTH * 2,
                mommaHealth = DAY_LENGTH * 2,
                mommaClean = DAY_LENGTH * 2;

    public Item hat,torso,foot;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private boolean attackInProgress = false;


    public Player()
    {
        name = "player";
        hitBox = new Rectangle(x,y,24,18);
        baseSpeed =4;
        //baseSpeed = 25;
        speed = baseSpeed;
        baseHp = 100;
        hp = baseHp;
        type = Type.CREATURE;
        damage = 5;
    }

    @Override
    public void moveDown() {
        if (busy) return;
        boolean isDiagonal = Input.keyboard.map.get(Input.KListener.A) || Input.keyboard.map.get(Input.KListener.D);

        if (Input.keyboard.map.get(SHIFT) && stamina > 1 && items.size() <= ITEMS_CAPACITY) {
            speed = baseSprint + bonusSprint;
            if (isDiagonal) stamina -= 0.25; else stamina -= 0.5;
        } else {
            speed = baseSpeed;
        }

        double moveSpeed = isDiagonal ? (speed + bonusSpeed) / Math.sqrt(2) : speed + bonusSpeed;
        y += moveSpeed;

        if (stamina < 1) stamina = 0;
    }

    @Override
    public void moveLeft() {
        if (busy) return;
        boolean isDiagonal = Input.keyboard.map.get(Input.KListener.W) || Input.keyboard.map.get(Input.KListener.S);

        if (Input.keyboard.map.get(SHIFT) && stamina > 1 && items.size() <= ITEMS_CAPACITY) {
            speed = baseSprint + bonusSprint;
            if (isDiagonal) stamina -= 0.25; else stamina -= 0.5;
        } else {
            speed = baseSpeed;
        }

        double moveSpeed = isDiagonal ? (speed + bonusSpeed) / Math.sqrt(2) : speed + bonusSpeed;
        x -= moveSpeed;

        if (stamina < 1) stamina = 0;
    }

    @Override
    public void moveRight() {
        if (busy) return;
        boolean isDiagonal = Input.keyboard.map.get(Input.KListener.W) || Input.keyboard.map.get(Input.KListener.S);

        if (Input.keyboard.map.get(SHIFT) && stamina > 1 && items.size() <= ITEMS_CAPACITY) {
            speed = baseSprint + bonusSprint;
            if (isDiagonal) stamina -= 0.25; else stamina -= 0.5;
        } else {
            speed = baseSpeed;
        }

        double moveSpeed = isDiagonal ? (speed + bonusSpeed) / Math.sqrt(2) : speed + bonusSpeed;
        x += moveSpeed;

        if (stamina < 1) stamina = 0;
    }

    @Override
    public void moveUp() {
        if (busy) return;
        boolean isDiagonal = Input.keyboard.map.get(Input.KListener.A) || Input.keyboard.map.get(Input.KListener.D);

        if (Input.keyboard.map.get(SHIFT) && stamina > 1 && items.size() <= ITEMS_CAPACITY) {
            speed = baseSprint + bonusSprint;
            if (isDiagonal) stamina -= 0.25; else stamina -= 0.5;
        } else {
            speed = baseSpeed;
        }

        double moveSpeed = isDiagonal ? (speed + bonusSpeed) / Math.sqrt(2) : speed + bonusSpeed;
        y -= moveSpeed;

        if (stamina < 1) stamina = 0;}

    @Override
    public void addItem(Item item)
    {
        if(item.id==4) for (var q : Game.player.quests) if(q.id==35)q.completed=true;
        if(item.type == MONEY)
        {
            Game.player.money += item.price;
            return;
        }
        if(item.stackable)
        {
            Item duplicate = null;
            for (int i = 0; i < items.size(); i++) {
                Item iterator = items.get(i);
                if(iterator.id == item.id) duplicate = iterator;

            }
            if(duplicate != null) duplicate.count += item.count;
            else items.add(item);
        }
        else items.add(item);

    }

    public void attemptMelee() {
        if (attackInProgress) return; // Предотвращение повторной атаки, если одна уже запланирована

        attackInProgress = true;
        scheduler.schedule(this::melee, 150, TimeUnit.MILLISECONDS);
    }
    public void shutdown() {
        scheduler.shutdown();
    }

    public void melee() {

        for (int i = 0; i < Game.currentLevel.objects.size(); i++) {

            GameObject target = Game.currentLevel.objects.get(i);

            if(target == Game.player) continue;

            if(!target.type.equals(Type.CREATURE)) continue;

            if(!Combat.line.intersects(target.hitBox))continue;

            Point2D p1 = new Point((int) hitBox.getCenterX(), (int) hitBox.getCenterY());
            Point2D p2 = new Point((int) target.hitBox.getCenterX(), (int) target.hitBox.getCenterY());

            if(p1.distance(p2) > attackDist) continue;

            Combat.kickback(target,15,Combat.angle);
            Combat.dealDamage(target, equip!=null && equip.type.equals(MELEE)?damage+ equip.damage : damage);
            //Тот, кого мы ударили, становится врагом
            target.enemy = true;
        }
        attackInProgress = false; // Разрешаем следующую атаку после завершения текущей
    }



    public void removeEffect(String name)
    {
        for (int i = 0; i < effects.size(); i++) {
            var effect = effects.get(i);
            if(effect.name.equals(name)) effects.remove(effect);
        }
    }
}
