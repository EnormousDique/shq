package ru.muwa.shq;

import ru.muwa.shq.engine.Game;
import ru.muwa.shq.engine.GameWindow;
import ru.muwa.shq.engine.Renderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

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

        //Панель с предупреждением
        JPanel disclaimerPanel = new JPanel();
        launcher.add(disclaimerPanel);
        disclaimerPanel.setLayout(null);
        disclaimerPanel.setBounds(0,0,800,600);
        disclaimerPanel.setBackground(Color.BLACK);
        disclaimerPanel.setVisible(true);
        //ТЕКСТ
        /* String s = """
                Внимание! Данная видео игра содержит сцены,
                изображающие совершение противоправных и аморальный действий.
                
                Автор данного программного обеспечения не разделяет и осуждает
                все действия, высказывания и ценности персонажей, представленных в игре.
                
                Сюжет игры и элементы повествования ни коим образом не являются пропагандой наркотиков, 
                любого вида экстремизма, нетрадиционных отношений и ценностей, попыткой задеть или оскорбить 
                чувства любого индивида или группы индивидов.
                
                События игры являются художественным вымыслом, не имеют никакого отношения к реальности.
                
                Вам следует немедленно выключить и удалить данное программное обеспечение, если Вы:
                - Лицо с чувствительной нервной системой или повышенной возбудимостью.
                - Лицо с психическими нарушениями.
                - Женщина, или лицо моложе 25 лет. 
                - Лицо, находящееся на территории и/или под юрисдикцией любого из существующих государств на планете Земля
                
                Если же Вы приняли решение продолжить использование данного программного обеспечения, 
                Вы полностью соглашаетесь с тем, что у Вас не может быть каких-либо претензий к его автору и содержанию.
                А так же берете на себя ответственность за любое влияние, оказанное данным программным обеспечением на Вашу
                психику, здоровье, чувства и душу. 
                
                Вы понимаете и соглашаетесь с тем, что содержание данного программного обеспечения является
                фантазией и никак не может быть интерпретировано в качестве оскорбления или пропаганды.
                
                Если Вы почувствуете любой вид дискомфорта, смущения или недовольства, 
                НЕЗАМЕДЛИТЕЛЬНО выключите и удалите данное программное обеспечение. 
                
                БЕРЕГИТЕ СЕБЯ И СВОИХ БЛИЗКИХ!
                
                """;

         */
        String s = "<html><style>body{ color: rgba(200, 140,140,0.4);  }</style><body>"
                + "<table width='100%'><tr><td width='50%'>"
                + "Внимание! Данная видео игра содержит сцены,<br>"
                + "изображающие совершение противоправных и аморальных действий.<br><br>"
                + "Автор данного программного обеспечения не разделяет и осуждает<br>"
                + "все действия, высказывания и ценности персонажей, представленных в игре.<br><br>"
                + "Сюжет игры и элементы повествования ни коим образом не являются пропагандой наркотиков,<br>"
                + "любого вида экстремизма, нетрадиционных отношений и ценностей, попыткой задеть или оскорбить<br>"
                + "чувства любого индивида или группы индивидов.<br><br>"
                + "События игры являются художественным вымыслом, не имеют никакого отношения к реальности.<br><br>"
                + "Вам следует немедленно выключить и удалить данное программное обеспечение, если Вы:<br>"
                + "- Лицо с чувствительной нервной системой или повышенной возбудимостью.<br>"
                + "- Лицо с психическими нарушениями.<br>"
                + "- Женщина, или лицо моложе 25 лет.<br>"
                + "- Лицо, находящееся на территории и/или под юрисдикцией любого из существующих государств на планете Земля<br><br>"
                + "</td><td width='50%'>"
                + "Если же Вы приняли решение продолжить использование данного программного обеспечения,<br>"
                + "Вы полностью соглашаетесь с тем, что у Вас не может быть каких-либо претензий к его автору и содержанию.<br>"
                + "А также берете на себя ответственность за любое влияние, оказанное данным программным обеспечением на Вашу<br>"
                + "психику, здоровье, чувства и душу.<br><br>"
                + "Вы понимаете и соглашаетесь с тем, что содержание данного программного обеспечения является<br>"
                + "фантазией и никак не может быть интерпретировано в качестве оскорбления или пропаганды.<br><br>"
                + "Если Вы почувствуете любой вид дискомфорта, смущения или недовольства,<br>"
                + "НЕЗАМЕДЛИТЕЛЬНО выключите и удалите данное программное обеспечение.<br><br>"
                + "БЕРЕГИТЕ СЕБЯ И СВОИХ БЛИЗКИХ!"
                + "</td></tr></table>"
                + "</body></html>";
        JLabel label = new JLabel(s);
        disclaimerPanel.add(label);
        label.setBounds(20,0,780,450);
        //Кнопка принимаю
        JButton okButton = new JButton("Соглашаюсь и хочу продолжить.");
        disclaimerPanel.add(okButton);
        okButton.setBounds(250,480,300,70);
        okButton.setBackground(new Color(200,169,200,200));

        //Основная панель
        JPanel startPanel = new JPanel();
        launcher.add(startPanel);
        startPanel.setLayout(null);
        startPanel.setBounds(0,0,800,600);
        startPanel.setBackground(Color.WHITE);
        startPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        startPanel.setOpaque(true);
        startPanel.setFocusable(true);
        startPanel.setVisible(false);

        ImageIcon backgroundImageIcon = null;
        ClassLoader classLoader = Main.class.getClassLoader();
        InputStream inputStream = null;
        try {
            inputStream = classLoader.getResourceAsStream("textures/launcher_bg.png");
            backgroundImageIcon = new ImageIcon(ImageIO.read(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel backgroundLabel = new JLabel(backgroundImageIcon);
        startPanel.add(backgroundLabel);
        backgroundLabel.setBounds(0,0,800,600);
        backgroundLabel.setOpaque(false);

        // Указание версии (для сборки)
        var labelVersion = new JLabel("  shq_rdr ver 0.1 (alpha-pve)");
        backgroundLabel.add(labelVersion);
        labelVersion.setBounds(20,450,170,30);
        labelVersion.setVisible(true);
        labelVersion.setOpaque(true);
        labelVersion.setBackground(new Color(250,200,200,200));




        //Кнопка начала игры
        JButton button = new JButton("Начать игру");
        button.setBounds(20,50,200,50);
        button.setBackground(new Color(150,200,200,200));
        button.setOpaque(true);
        backgroundLabel.add(button);



        //Кнопка открыть настройки
        JButton settingsButton = new JButton("Настройки");
        settingsButton.setBounds(20,150,200,50);
        settingsButton.setBackground(new Color(150,200,200,200));
        settingsButton.setOpaque(true);
        backgroundLabel.add(settingsButton);

        //Кнопка "принимаю" делает видимой эту панель
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disclaimerPanel.setVisible(false);
                startPanel.setVisible(true);
            }
        });



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
                    toolkit.getScreenSize().setSize(new Dimension(800,600));
                    // Получение размера экрана
                    Dimension screenSize = toolkit.getScreenSize();
                    GameWindow.WIDTH = screenSize.width;
                    GameWindow.HEIGHT = screenSize.height;
                }else {
                    GameWindow.fullscreen = false;
                    GameWindow.WIDTH = 1280;
                    GameWindow.HEIGHT = 720;
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
