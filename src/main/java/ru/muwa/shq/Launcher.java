package ru.muwa.shq;

import ru.muwa.shq.engine.Game;
import ru.muwa.shq.engine.GameWindow;
import ru.muwa.shq.engine.Renderer;
import ru.muwa.shq.engine.utils.saveload.Loader;
import ru.muwa.shq.engine.utils.saveload.Saver;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

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

        //������ � ���������������
        JPanel disclaimerPanel = new JPanel();
        launcher.add(disclaimerPanel);
        disclaimerPanel.setLayout(null);
        disclaimerPanel.setBounds(0,0,800,600);
        disclaimerPanel.setBackground(Color.BLACK);
        disclaimerPanel.setVisible(true);
        //�����
        /* String s = """
                ��������! ������ ����� ���� �������� �����,
                ������������ ���������� �������������� � ���������� ��������.
                
                ����� ������� ������������ ����������� �� ��������� � ��������
                ��� ��������, ������������ � �������� ����������, �������������� � ����.
                
                ����� ���� � �������� ������������� �� ���� ������� �� �������� ����������� ����������, 
                ������ ���� �����������, �������������� ��������� � ���������, �������� ������ ��� ��������� 
                ������� ������ �������� ��� ������ ���������.
                
                ������� ���� �������� �������������� ��������, �� ����� �������� ��������� � ����������.
                
                ��� ������� ���������� ��������� � ������� ������ ����������� �����������, ���� ��:
                - ���� � �������������� ������� �������� ��� ���������� �������������.
                - ���� � ������������ �����������.
                - �������, ��� ���� ������ 25 ���. 
                - ����, ����������� �� ���������� �/��� ��� ����������� ������ �� ������������ ���������� �� ������� �����
                
                ���� �� �� ������� ������� ���������� ������������� ������� ������������ �����������, 
                �� ��������� ������������ � ���, ��� � ��� �� ����� ���� �����-���� ��������� � ��� ������ � ����������.
                � ��� �� ������ �� ���� ��������������� �� ����� �������, ��������� ������ ����������� ������������ �� ����
                �������, ��������, ������� � ����. 
                
                �� ��������� � ������������ � ���, ��� ���������� ������� ������������ ����������� ��������
                ��������� � ����� �� ����� ���� ���������������� � �������� ����������� ��� ����������.
                
                ���� �� ������������ ����� ��� �����������, �������� ��� ������������, 
                ��������������� ��������� � ������� ������ ����������� �����������. 
                
                �������� ���� � ����� �������!
                
                """;

         */
        String s = "<html><style>body{ color: rgba(200, 140,140,0.4);  }</style><body>"
                + "<table width='100%'><tr><td width='50%'>"
                + "��������! ������ ����� ���� �������� �����,<br>"
                + "������������ ���������� �������������� � ���������� ��������.<br><br>"
                + "����� ������� ������������ ����������� �� ��������� � ��������<br>"
                + "��� ��������, ������������ � �������� ����������, �������������� � ����.<br><br>"
                + "����� ���� � �������� ������������� �� ���� ������� �� �������� ����������� ����������,<br>"
                + "������ ���� �����������, �������������� ��������� � ���������, �������� ������ ��� ���������<br>"
                + "������� ������ �������� ��� ������ ���������.<br><br>"
                + "������� ���� �������� �������������� ��������, �� ����� �������� ��������� � ����������.<br><br>"
                + "��� ������� ���������� ��������� � ������� ������ ����������� �����������, ���� ��:<br>"
                + "- ���� � �������������� ������� �������� ��� ���������� �������������.<br>"
                + "- ���� � ������������ �����������.<br>"
                + "- �������, ��� ���� ������ 25 ���.<br>"
                + "- ����, ����������� �� ���������� �/��� ��� ����������� ������ �� ������������ ���������� �� ������� �����<br><br>"
                + "</td><td width='50%'>"
                + "���� �� �� ������� ������� ���������� ������������� ������� ������������ �����������,<br>"
                + "�� ��������� ������������ � ���, ��� � ��� �� ����� ���� �����-���� ��������� � ��� ������ � ����������.<br>"
                + "� ����� ������ �� ���� ��������������� �� ����� �������, ��������� ������ ����������� ������������ �� ����<br>"
                + "�������, ��������, ������� � ����.<br><br>"
                + "�� ��������� � ������������ � ���, ��� ���������� ������� ������������ ����������� ��������<br>"
                + "��������� � ����� �� ����� ���� ���������������� � �������� ����������� ��� ����������.<br><br>"
                + "���� �� ������������ ����� ��� �����������, �������� ��� ������������,<br>"
                + "��������������� ��������� � ������� ������ ����������� �����������.<br><br>"
                + "�������� ���� � ����� �������!"
                + "</td></tr></table>"
                + "</body></html>";
        JLabel label = new JLabel(s);
        disclaimerPanel.add(label);
        label.setBounds(20,0,780,450);
        //������ ��������
        JButton okButton = new JButton("���������� � ���� ����������.");
        disclaimerPanel.add(okButton);
        okButton.setBounds(250,480,300,70);
        okButton.setBackground(new Color(200,169,200,200));

        //�������� ������
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

        // �������� ������ (��� ������)
        var labelVersion = new JLabel("  shq_rdr ver 0.1.2 (alpha-pve)");
        backgroundLabel.add(labelVersion);
        labelVersion.setBounds(20,450,170,30);
        labelVersion.setVisible(true);
        labelVersion.setOpaque(true);
        labelVersion.setBackground(new Color(250,200,200,200));




        //������ ������ ����
        JButton button = new JButton("������ ����");
        button.setBounds(20,50,200,50);
        button.setBackground(new Color(150,200,200,200));
        button.setOpaque(true);
        backgroundLabel.add(button);

        //������ ��������� ����
        JButton loadButton = new JButton("��������� ����");
        loadButton.setBounds(20,150,200,50);
        loadButton.setBackground(new Color(150,200,200,200));
        loadButton.setOpaque(true);
        backgroundLabel.add(loadButton);

        //������ ������� ���������
        JButton settingsButton = new JButton("���������");
        settingsButton.setBounds(20,250,200,50);
        settingsButton.setBackground(new Color(150,200,200,200));
        settingsButton.setOpaque(true);
        backgroundLabel.add(settingsButton);

        //������ ������� ���������
        JButton howToButton = new JButton("��� ������");
        howToButton.setBounds(20,350,200,50);
        howToButton.setBackground(new Color(150,200,200,200));
        howToButton.setOpaque(true);
        backgroundLabel.add(howToButton);








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

        //������ ��������
        var howToPanel = new JPanel();
        launcher.add(howToPanel);
        howToPanel.setBounds(0,0,800,600);
        howToPanel.setBackground(Color.WHITE);
        howToPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        howToPanel.setOpaque(true);
        howToPanel.setFocusable(true);
        howToPanel.setVisible(false);

        String howToString = "<html><style>body{ color: rgba(00, 0,0,0.9);  }</style><body>"
                + "<table width='100%'><tr><td width='50%'>"
                + "���������� <br><br>"
                + "W A S D - ������������<br>"
                + "E - �������������� � ��������� � �����������<br>"
                + "I - ������� ���������<br>"
                + "H - ������� HUD (���� ����)<br>"
                + "Q - ������� ����<br>"
                + "T - ����� / ���������� �����<br>"
                + "R - ����������� ������<br>"
                + "SPACE - ������������� �������� � ����<br>"
                + "P - ������ (��� ����)<br>"
                + "M - ����� / ����<br><br>"
                + "�������� ���� ���� - ���� �� �������. <br>"
                + "����� - ������� �� ���,<br>"
                + " ����� ������� ���� �� ���, <br>"
                + "��������� � ������� � ���<br>"
                + "���� �� ������ �������, ���� ������������.<br>"
                + "���-�� ����������� ������� ������� � ������� ���� �� HUD<br><br>"
                + "���������� ������ ����. ������� �� ��� ���?<br><br>"
                + "���� � ������ - ������� "
                + "����� ������� ����������, ��������� � ����. <br>"
                + "�������� ���-�� �� ��� ������� ��� ������ ����.<br><br>"
                + "���� ����������� ������������� ����� �������� ����<br>"
                + "������� �� ������������ ����������������� ������ � ������ <br>"
                + "�� ��������� ������� ������������� ��������� ����������� ������ � ������<br><br>"
                + "���������� ����������� ���� ��� � �� ������� ��� �� ���<br><br>"
                + "��� ������� ���������� ���������� ���������� 1280�720 � ���������� ������������� ����� � ����������"
                + "</td></tr></table>"
                + "</body></html>";
        JLabel howToLabel = new JLabel(howToString);
        howToPanel.add(howToLabel);
        JButton howToBackButton = new JButton("���������");
        howToPanel.add(howToBackButton);
        howToBackButton.setBounds(400,500,100,50);




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

        //������ ��������� ����
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Path jarPath = Paths.get(Saver.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
                    File saveDirectory = new File(jarPath.toFile(), "shq_save");
                    if (saveDirectory.exists() && saveDirectory.isDirectory()) {
                        launcher.setVisible(false);
                        Game.load();
                    } else {
                        JOptionPane.showMessageDialog(startPanel,
                                "��� ����������� ����!",
                                "��������",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }catch (Exception ex){}
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
                    toolkit.getScreenSize().setSize(new Dimension(800,600));
                    // ��������� ������� ������
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
                //�������� ������ ��������, ��������� ��������� ������
                startPanel.setVisible(true);
                settingsPanel.setVisible(false);
            }
        });
        //������ "��������"
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disclaimerPanel.setVisible(false);
                startPanel.setVisible(true);
            }
        });

        howToButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startPanel.setVisible(false);
                howToPanel.setVisible(true);
            }
        });

        howToBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startPanel.setVisible(true);
                howToPanel.setVisible(false);
            }
        });
    }



}
