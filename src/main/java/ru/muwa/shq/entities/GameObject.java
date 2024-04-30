package ru.muwa.shq.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.muwa.shq.Main;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class GameObject {
    public static final int MOM =2;

    public int id;

    public String name;
    public int x, y;
    public int destX = -1;
    public int destY = -1;
    public static final int ITEMS_CAPACITY = 8;
    public int hp, speed, sprintSpeed; //только для creature

    public int stamina;
    public int dialogue;
    public int minigameId;
    public Minigame minigame;
    public Rectangle hitBox;
    public int hitBoxXOffset, hitBoxYOffset;
    public boolean solid = false;
    public boolean opened; //только для CONTAINER
    public boolean enemy;
    public int damage;
    public ArrayList<Item> items = new ArrayList<>(); // только для type == CONTAINER

    public Coordinates coordinates;
    public int scriptId;
    public Type type;

    public enum Type
    {
        OBJECT,CONTAINER,ZONE,CREATURE,INTERACT,DOOR,BUILDING
    }

    public void moveLeft()
    {
        x -= speed;
    }

    public void moveRight()
    {
        x += speed;
    }

    public void moveUp()
    {
        y -= speed;
    }

    public void moveDown()
    {
        y += speed;
    }
    public static HashMap<Integer, GameObject> repo = new HashMap<>();
    static
    {
        String pathToJsonWithItems = "json/objects.json";
        readItemsFromJsonFile(pathToJsonWithItems);
        System.out.println("Список объектов успешно инициализирован");
    }
    public static GameObject get(int id)
    {
        GameObject templateObject = repo.get(id);
        return new GameObject(templateObject);
    }

    public GameObject(int id, String name, int x, int y, int hp, int stamina, int speed, int sprintSpeed, int dialogue, int minigameId, Minigame minigame, Rectangle hitBox, boolean solid, boolean opened, boolean enemy, int damage, ArrayList<Item> items, int scriptId, Type type, Coordinates coordinates, int hitBoxXOffset, int hitBoxYOffset) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.stamina = stamina;
        this.speed = speed;
        this.sprintSpeed = sprintSpeed;
        this.dialogue = dialogue;
        this.minigameId = minigameId;
        this.minigame = minigame;
        this.hitBox = new Rectangle(hitBox.x,hitBox.y,hitBox.width,hitBox.height);
        this.solid = solid;
        this.opened = opened;
        this.enemy = enemy;
        this.damage = damage;
        this.items = items;
        this.scriptId = scriptId;
        this.type = type;
        this.coordinates = coordinates;
        this.hitBoxXOffset = hitBoxXOffset;
        this.hitBoxYOffset = hitBoxYOffset;
    }
    public GameObject(GameObject o)
    {
        this(o.id,o.name,o.x,o.y,o.hp,o.stamina,o.speed,o.sprintSpeed,o.dialogue,o.minigameId,o.minigame,o.hitBox,o.solid,o.opened,o.enemy,o.damage,o.items,o.scriptId,o.type,o.coordinates, o.hitBoxXOffset,o.hitBoxYOffset);
    }
    public GameObject(){}

    private static void readItemsFromJsonFile(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Получаем InputStream из ресурсов с использованием ClassLoader'а
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(filePath);
            // Чтение данных из JSON файла и преобразование их в список объектов Item
            GameObject[] objects = objectMapper.readValue(inputStream, GameObject[].class);
            for (GameObject object : objects) {
                repo.put(object.id,object);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void foo(){}

    @Override
    public String toString() {
        return "GameObject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", hp=" + hp +
                ", stamina=" + stamina +
                ", speed=" + speed +
                ", sprintSpeed=" + sprintSpeed +
                ", hitBox=" + hitBox +
                ", solid=" + solid +
                ", items=" + items +
                ", scriptId=" + scriptId +
                ", type=" + type +
                ", coordinates=" + coordinates +
                '}';
    }

    public void addItem(Item item)
    {
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
}