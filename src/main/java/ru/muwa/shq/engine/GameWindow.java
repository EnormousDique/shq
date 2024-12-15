package ru.muwa.shq.engine;

import javax.swing.*;

/**
 *
 * Класс игрового окна
 *
 */
public class GameWindow extends JFrame {

    //Широта и высота экрана (для оконного режима)
    public static int WIDTH = 800;
    public static int HEIGHT = 600;

    //Полноэкранный режим
    public static boolean fullscreen = false;

    //Конструктор
    GameWindow()
    {
        //Устанавливаем размер границ окна
        this.setBounds(100,100,WIDTH,HEIGHT+30);
        if(fullscreen) {
            //При полноэкранном режиме скрываем рамку окна и ставим в начало экрана
            setUndecorated(true);
            setLocation(0,0);
        }
        this.setVisible(true);//Окно видимо
        this.setLayout(null);//Отключаем авто-раскладку элементов
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);//Выключаем прогу при закрытии окна
        System.out.println("создано игровое окно");
    }
}
