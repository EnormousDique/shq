package ru.muwa.shq;

import ru.muwa.shq.engine.Game;
import ru.muwa.shq.engine.GameWindow;
import ru.muwa.shq.engine.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Launcher extends JFrame {
    public Launcher()
    {
        this.setBounds(100,100,800,600);
        this.setVisible(true);
        this.setLayout(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        initLauncher(this);
    }
    private void initLauncher(Launcher launcher) {
        //Начальная панель
        JPanel startPanel = new JPanel();
        launcher.add(startPanel);
        startPanel.setLayout(null);
        startPanel.setBounds(0,0,800,600);
        startPanel.setBackground(Color.WHITE);
        startPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        startPanel.setOpaque(true);
        startPanel.setFocusable(true);
        startPanel.setVisible(true);

        //Кнопка начала игры
        JButton button = new JButton("Начать игру");
        startPanel.add(button);
        button.setBounds(200,100,400,80);


        //Кнопка открыть настройки
        JButton settingsButton = new JButton("Настройки");
        startPanel.add(settingsButton);
        settingsButton.setBounds(200,280,400,80);


        //Панель настроек
        JPanel settingsPanel = new JPanel();
        launcher.add(settingsPanel);
        settingsPanel.setBounds(0,0,800,600);
        settingsPanel.setBackground(Color.WHITE);
        settingsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        settingsPanel.setOpaque(true);
        settingsPanel.setFocusable(true);
        settingsPanel.setVisible(false);


        //Кнопка полноэкранный режим
        JCheckBox fullscreen = new JCheckBox("Полноэкранный режим",false);
        settingsPanel.add(fullscreen);
        fullscreen.setBounds(100,100,400,80);

        //Кнопка применить
        JButton applyButton = new JButton("Применить");
        applyButton.setBounds(100,250,150,80);
        settingsPanel.add(applyButton);

        //Кнопка вернуться в меню
        JButton backButton = new JButton("Назад в меню");
        backButton.setBounds(100,400,150,80);
        settingsPanel.add(backButton);


        //Код нажатий кнопок

        //Кнопка начать игру
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Запуск игры
                launcher.setVisible(false);
                Game.start();
            }
        });

        //Кнопка открыть настройки
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Скрываем стартовую панель, открываем панель настроек
                startPanel.setVisible(false);
                settingsPanel.setVisible(true);
            }
        });
        //Кнопка применить настройки
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(fullscreen.isSelected())
                {
                    GameWindow.fullscreen = true;
                    // Получение экземпляра Toolkit
                    Toolkit toolkit = Toolkit.getDefaultToolkit();
                    // Получение размера экрана
                    Dimension screenSize = toolkit.getScreenSize();
                    GameWindow.WIDTH = screenSize.width;
                    GameWindow.HEIGHT = screenSize.height;
                }else {
                    GameWindow.fullscreen = false;
                    GameWindow.WIDTH = 1280;
                    GameWindow.HEIGHT = 800;
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Скрываем панель настроек, открываем начальную панель
                startPanel.setVisible(true);
                settingsPanel.setVisible(false);
            }
        });
    }



}
