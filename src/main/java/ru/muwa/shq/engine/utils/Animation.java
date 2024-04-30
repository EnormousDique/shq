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
    static final int PL_PISTOL_FIRE = 1;
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
            if(System.currentTimeMillis() > anim.duration + anim.startTime) current.remove(object);
            int frame = 0;//Переменная, в которой хранится номер кадр
            //Считаем какой номер кадра нужно показывать
            long timePassed = System.currentTimeMillis() - anim.startTime;//Узнали сколько времени прошло
            //Считаем какой кадр нужно рисовать
            frame = (int) (timePassed / (anim.duration / (anim.frames.size()-1)));
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
        }catch (Exception e) {/* Ловить */}
        System.out.println("Анимации загружены успешно");
    }
}
