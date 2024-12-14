package ru.muwa.shq.engine.utils;

import ru.muwa.shq.Main;
import ru.muwa.shq.engine.Renderer;
import ru.muwa.shq.entities.GameObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Animation {
    //Поля объекта анимации
    //Кадры
    public List<BufferedImage> frames = new ArrayList<>();
    //Длительность
    long startTime, duration; //В миллисекундах
    //Статические поля
    static boolean busy;
    //Константы id анимаций
    public static final int PL_PISTOL_FIRE = 1,PL_PUNCH = 2, PL_SMOKE = 3, PL_PISTOL_RELOAD = 4,PL_SHOTGUN_FIRE = 5,PL_SHOTGUN_RELOAD = 6, PL_PEE = 7, PL_ASSAULT_RIFLE_FIRE = 8;
    //Мапа текущих анимаций. Ключ - объект, значение - анимация
    public static HashMap<GameObject,Animation> current = new HashMap<>();
    //Мапа - хранилище всех анимаций. Ключ - id, значение - анимация
    private static HashMap<Integer,Animation> repo = new HashMap<>();
    //Статические методы службы
    //Получаем новый экземпляр анимации по id
    public static Animation get(int id)
    {
        Animation animation = new Animation();
        animation.frames = repo.get(id).frames;
        animation.duration = repo.get(id).duration;
        animation.startTime = System.currentTimeMillis();
        return animation;
    }
    //Добавляем анимацию в список рисуемых сейчас
    public static void addAnimation(GameObject object, Animation animation) {current.put(object,animation);}
    //Возвращаем кадр анимации для текущего времени у заданного объекта
    public static BufferedImage getCurrentFrame(GameObject object)
    {
            Animation anim = current.get(object);//Получили анимацию
            //Сперва проверим на просрок
            if(System.currentTimeMillis() >= anim.duration + anim.startTime){ current.remove(object);return anim.frames.get(anim.frames.size()-1);}
            int frame = 0;//Переменная, в которой хранится номер кадр
            //Считаем какой номер кадра нужно показывать
            long timePassed = System.currentTimeMillis() - anim.startTime;//Узнали сколько времени прошло
            //Считаем какой кадр нужно рисовать
            frame = (int) (timePassed / (anim.duration / (anim.frames.size())));
            if(frame >= anim.frames.size()){ current.remove(object); return anim.frames.get(anim.frames.size()-1);}
            return anim.frames.get(frame);
    }
    //Статический блок с инициализацией
    static
    {
        System.out.println("Начало загрузки анимаций");
        try {//Пытаться
            //Создаем класслоудер для загрузки ресурсов изнутри джарника
            ClassLoader classLoader = Main.class.getClassLoader();
            //Анимация выстрела из пистолета (Шкипер)
            //=======================================================================================================//
            Animation animation = new Animation();
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/pistol_fire_1.png")));
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/pistol_fire_2.png")));
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/pistol_fire_3.png")));
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/pistol_fire_4.png")));
            animation.duration = 500; //0.5 секунды
            repo.put(PL_PISTOL_FIRE,animation);
            //=======================================================================================================//
            //Анимация удара кулаком (Шкипер)
            //=======================================================================================================//
            animation = new Animation();
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/punch_1.png")));
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/punch_2.png")));
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/punch_3.png")));
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/punch_4.png")));
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/punch_5.png")));
            animation.duration = 350; //0.35 секунды
            repo.put(PL_PUNCH,animation);
            //=======================================================================================================//
            //Анимация курения (Шкипер)
            //=======================================================================================================//
            animation = new Animation();
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/smoke_1.png")));
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/smoke_2.png")));
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/smoke_3.png")));
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/smoke_4.png")));
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/smoke_5.png")));
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/smoke_6.png")));
            animation.duration = 2_500; //2.5 секунды
            repo.put(PL_SMOKE,animation);
            //=======================================================================================================//
            //Перезарядка пистолета (Шкипер)
            //=======================================================================================================//
            animation = new Animation();
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/pistol_reload_1.png")));
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/pistol_reload_2.png")));
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/pistol_reload_3.png")));
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/pistol_reload_4.png")));
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/pistol_reload_5.png")));

            animation.duration = 1_500; //1.5 секунды
            repo.put(PL_PISTOL_RELOAD,animation);
            //=======================================================================================================//
            //Выстрел из дробовика (Шкипер)
            //=======================================================================================================//
            animation = new Animation();
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/shotgun_fire_1.png")));
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/shotgun_fire_2.png")));
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/shotgun_fire_3.png")));

            animation.duration = 500; //0.5 секунды
            repo.put(PL_SHOTGUN_FIRE,animation);

            //TODO: сделать отельную анимацию
            animation = new Animation();
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/shotgun_fire_1.png")));
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/shotgun_fire_2.png")));
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/shotgun_fire_3.png")));

            animation.duration = 100; //0.1 секунды
            repo.put(PL_ASSAULT_RIFLE_FIRE,animation);

            //=======================================================================================================//
            //Перезарядка дробовика (Шкипер)
            //=======================================================================================================//
            animation = new Animation();
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/shotgun_reload_1.png")));
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/shotgun_reload_2.png")));
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/shotgun_reload_3.png")));
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/shotgun_reload_4.png")));
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/shotgun_reload_5.png")));

            animation.duration = 1_500; //1.5 секунды
            repo.put(PL_SHOTGUN_RELOAD,animation);
            //=======================================================================================================//
            //Анимация ссак (Шкипер)
            //=======================================================================================================//
            animation = new Animation();
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/pee_1.png")));
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/pee_2.png")));
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/pee_3.png")));
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/pee_4.png")));
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/pee_5.png")));
            animation.frames.add(ImageIO.read(classLoader.getResourceAsStream("textures/anim/player/pee_6.png")));
            animation.duration = 2_500; //2.5 секунды
            repo.put(PL_PEE,animation);
            //=======================================================================================================//

        }catch (Exception e) {/* Ловить */}
        System.out.println("Анимации загружены успешно");
    }
}
