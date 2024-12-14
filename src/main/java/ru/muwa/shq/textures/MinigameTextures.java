package ru.muwa.shq.textures;

import ru.muwa.shq.Main;
import ru.muwa.shq.engine.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class MinigameTextures {
    public static HashMap<Integer, BufferedImage> repo = new HashMap<>();
    static
    {
        try
        {
            //Загрузка картинок в ОЗУ
            ClassLoader classLoader = Main.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(Game.imgPath+"postbox_game.png");
            BufferedImage image = ImageIO.read(inputStream); repo.put(1,image); repo.put(16,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath+"cook_game.png");
            image = ImageIO.read(inputStream); repo.put(2,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath+"bath_game.png");
            image = ImageIO.read(inputStream); repo.put(4,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath+"windowsill_game.png");
            image = ImageIO.read(inputStream); repo.put(6,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath+"bus_game.png");
            image = ImageIO.read(inputStream); repo.put(7,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath+"schitok_game.png");
            image = ImageIO.read(inputStream); repo.put(8,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath+"sink_game.png");
            image = ImageIO.read(inputStream); repo.put(10,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath+"toilet_game.png");
            image = ImageIO.read(inputStream); repo.put(11,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath+"GarbageChute_game.png");
            image = ImageIO.read(inputStream); repo.put(12,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath+"heater_game.png");
            image = ImageIO.read(inputStream); repo.put(13,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath+"dead.png");
            image = ImageIO.read(inputStream); repo.put(14,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath+"internet_index.png");
            image = ImageIO.read(inputStream); repo.put(19,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath+"internet_doc.png");
            image = ImageIO.read(inputStream); repo.put(20,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath+"internet_shop.png");
            image = ImageIO.read(inputStream); repo.put(21,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath+"internet_rent.png");
            image = ImageIO.read(inputStream); repo.put(22,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath+"internet_porn.png");
            image = ImageIO.read(inputStream); repo.put(23,image);

        }
        catch (IOException e)
        {
            System.out.println("ошибка при чтении файлов текстур миниигр");
        }

         

    }
}
