package ru.muwa.shq.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.muwa.shq.Main;
import ru.muwa.shq.engine.utils.GameTime;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class Item {
    public int id;
    @JsonIgnore
    public Rectangle icon = new Rectangle(-100,-100,2,2);
    public String name, description;
    public boolean stackable;
    public int scriptId;
    public int count; //count == 1 если stackable == false;
    public int ammo, maxAmmo; // != 0 только для type == FIREARM
    public int damage; // != 0 только для type == MELEE
    public int price;
    public Type type;
    public Effect effect; // Поле effect != null только если type == FOOD;
    public enum Type
    {
        FOOD,MELEE,FIREARM,FOOTWEAR,TORSO,HAT,PACK,MONEY,AMMO,ITEM,SCRIPT,DRUG
    }
    public static class Effect {
        public String name;
        public int hpBonus, staminaBonus,speedBonus,sprintBonus,regHpBonus,regStaminaBonus,cameraShake,cameraDelay,psychoBonus;
        public boolean permanent;
        public long expirationTime;
        public long duration;

        public double poo,pee,hunger,sleepy,crazy,thirst;
        public Effect(){}
        public Effect(Effect e)
        {
            this.name = e.name;
            this.cameraDelay = e.cameraDelay;
            this.cameraShake = e.cameraShake;
            this.duration = e.duration;
            this.hpBonus = e.hpBonus;
            this.permanent = e.permanent;
            this.regHpBonus = e.regHpBonus;
            this.staminaBonus = e.staminaBonus;
            this.regStaminaBonus = e.regStaminaBonus;
            this.psychoBonus = e.psychoBonus;
            this.speedBonus = e.speedBonus;
            this.sprintBonus = e.sprintBonus;
            this.expirationTime = GameTime.get() + duration;
            this.crazy = e.crazy;
            this.pee = e.pee;
            this.poo = e.poo;
            this.thirst = e.thirst;
            this.hunger = e.hunger;
            this.sleepy = e.sleepy;

        }
    }
    public static Item get(int id)
    {
        return new Item( repo.get(id) );
    }
    private static HashMap<Integer,Item> repo = new HashMap<>();
    static
    {
        String pathToJsonWithItems = "json/items.json";
        readItemsFromJsonFile(pathToJsonWithItems);
        System.out.println("Список предметов успешно инициализирован");
    }
    private static void readItemsFromJsonFile(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Получаем InputStream из ресурсов с использованием ClassLoader'а
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(filePath);
            // Чтение данных из JSON файла и преобразование их в список объектов Item
            Item[] items = objectMapper.readValue(inputStream, Item[].class);
            for (Item item : items) {
                repo.put(item.id,item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void foo(){}

    public Item(){}

    public Item(Item i) {
        this.id = i.id;
        this.icon = i.icon;
        this.name = i.name;
        this.description = i.description;
        this.stackable = i.stackable;
        this.count = i.count;
        this.ammo = i.ammo;
        this.maxAmmo = i.maxAmmo;
        this.damage = i.damage;
        this.price = i.price;
        this.type = i.type;
        this.scriptId = i.scriptId;
        if(i.effect != null) this.effect = new Item.Effect(i.effect);

    }
}
