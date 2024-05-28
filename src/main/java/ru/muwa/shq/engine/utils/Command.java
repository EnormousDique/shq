package ru.muwa.shq.engine.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.muwa.shq.engine.Game;
import ru.muwa.shq.engine.Renderer;
import ru.muwa.shq.entities.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Command {

    private static JButton button;
    public static TextField text;
    public static CommandWindow window = new CommandWindow();
    public static class CommandWindow extends JFrame
    {
        public CommandWindow(){
            setBounds(200,200,200,250);
            button = new JButton("выполнить");
            add(button);
            text = new TextField();
            add(text);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        execute(text.getText());
                    }catch (Exception ex)
                    {
                        System.out.println("ПИШИ КОМАНДУ ВЕРНО, ДОЛБОЁБ");
                    }
                    text.setText("");
                }
            });
            button.setBounds(100,120,100,50);
        }
    }
    public static void execute(String arg)
    {
        if(arg.equals("save_level")) saveLevel(); //usage: save_level
        if(arg.startsWith("add"))
            addObject(Integer.parseInt(arg.split("_")[1]),Integer.parseInt( arg.split("_")[2]),Integer.parseInt(arg.split("_")[3])); // usage: add_1_2_3 // 1 - id, 2 - x, 3 - y;
        if(arg.startsWith("load_level_")) loadLevel(Integer.parseInt(arg.split("load_level_")[1]));
        if(arg.startsWith("money=")) Game.player.money = Integer.parseInt(arg.split("money=")[1]);
        if(arg.startsWith("tp_")) {Game.player.x = Integer.parseInt(arg.split("_")[1]); Game.player.y = Integer.parseInt(arg.split("_")[2]);}
        if(arg.startsWith("x_give_")) for(int i=0;i<Integer.parseInt(arg.split("_")[3]);i++) Game.player.addItem(Item.get(Integer.parseInt(arg.split("_")[2])));
        if(arg.startsWith("give_")) Game.player.addItem(Item.get(Integer.parseInt(arg.split("_")[1])));
        if(arg.startsWith("brush_")) Game.player.brush = Integer.parseInt(arg.split("brush_")[1]);
        if(arg.equals("remove_last")) Game.currentLevel.objects.remove(Game.currentLevel.objects.size()-1);
        if(arg.equals("tcl")) Physx.playerCollisions = !Physx.playerCollisions;
        if(arg.equals("zone")) Renderer.drawZones = !Renderer.drawZones;
        if(arg.startsWith("clear_level_from_")) clearLevelFrom(arg.split("clear_level_from_")[1]);
        if(arg.equals("walls")) Renderer.drawWalls = !Renderer.drawWalls;
        if(arg.startsWith("minigame_")) Minigame.current = Minigame.get(Integer.parseInt(arg.split("minigame_")[1]));

    }

    private static void clearLevelFrom(String stringId) {
        int id = Integer.parseInt(stringId);
        for (int i = 0; i < Game.currentLevel.objects.size(); i++) {
            GameObject object = Game.currentLevel.objects.get(i);
            if(object.id == id){
                Game.currentLevel.objects.remove(object);
                i--;
            }
        }
    }

    private static void loadLevel(int loadLevel) {

        String pathToJsonWithItems = "D:\\shq\\src\\main\\resources\\json\\level_" + loadLevel + ".json";

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Чтение данных из JSON файла и преобразование их в список объектов Item
            LevelSkeleton levelSkeleton = objectMapper.readValue(new File(pathToJsonWithItems), LevelSkeleton.class);
            Game.currentLevel = new Level(levelSkeleton);
            Game.currentLevel.objects.add(Game.player);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void addObject(int id, int x, int y) {
        GameObject newObject = GameObject.get(id);
        newObject.x = x; newObject.y = y;
        newObject.hitBox.x = x; newObject.hitBox.y = y;
        Game.currentLevel.objects.add(newObject);
        System.out.println("добавлен объект " + newObject);
        for (GameObject o : Game.currentLevel.objects) System.out.println(o);
    }

    private static void saveLevel() {
        // Создаем объект ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        // Создаем экземпляр вашего класса для сериализации
        LevelSkeleton levelSkeleton = new LevelSkeleton(Game.currentLevel);

        // Попробуйте сериализовать объект в JSON и записать в файл
        try {
            // Сериализация объекта в JSON и запись в файл
            String filePathAndName = "D:\\shq\\src\\main\\resources\\json\\" + "level_"+levelSkeleton.levelId+".json";
            objectMapper.writeValue(new File(filePathAndName), levelSkeleton);
            System.out.println("Уровень успешно сериализован в JSON и записан в файл");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}