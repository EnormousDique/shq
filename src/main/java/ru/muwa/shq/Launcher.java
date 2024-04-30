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
        //��������� ������
        JPanel startPanel = new JPanel();
        launcher.add(startPanel);
        startPanel.setLayout(null);
        startPanel.setBounds(0,0,800,600);
        startPanel.setBackground(Color.WHITE);
        startPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        startPanel.setOpaque(true);
        startPanel.setFocusable(true);
        startPanel.setVisible(true);

        //������ ������ ����
        JButton button = new JButton("������ ����");
        startPanel.add(button);
        button.setBounds(200,100,400,80);


        //������ ������� ���������
        JButton settingsButton = new JButton("���������");
        startPanel.add(settingsButton);
        settingsButton.setBounds(200,280,400,80);


        //������ ��������
        JPanel settingsPanel = new JPanel();
        launcher.add(settingsPanel);
        settingsPanel.setBounds(0,0,800,600);
        settingsPanel.setBackground(Color.WHITE);
        settingsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        settingsPanel.setOpaque(true);
        settingsPanel.setFocusable(true);
        settingsPanel.setVisible(false);


        //������ ������������� �����
        JCheckBox fullscreen = new JCheckBox("������������� �����",false);
        settingsPanel.add(fullscreen);
        fullscreen.setBounds(100,100,400,80);

        //������ ���������
        JButton applyButton = new JButton("���������");
        applyButton.setBounds(100,250,150,80);
        settingsPanel.add(applyButton);

        //������ ��������� � ����
        JButton backButton = new JButton("����� � ����");
        backButton.setBounds(100,400,150,80);
        settingsPanel.add(backButton);


        //��� ������� ������

        //������ ������ ����
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //������ ����
                launcher.setVisible(false);
                Game.start();
            }
        });

        //������ ������� ���������
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //�������� ��������� ������, ��������� ������ ��������
                startPanel.setVisible(false);
                settingsPanel.setVisible(true);
            }
        });
        //������ ��������� ���������
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(fullscreen.isSelected())
                {
                    GameWindow.fullscreen = true;
                    // ��������� ���������� Toolkit
                    Toolkit toolkit = Toolkit.getDefaultToolkit();
                    // ��������� ������� ������
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
                //�������� ������ ��������, ��������� ��������� ������
                startPanel.setVisible(true);
                settingsPanel.setVisible(false);
            }
        });
    }



}
