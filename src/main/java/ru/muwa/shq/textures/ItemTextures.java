package ru.muwa.shq.textures;

import ru.muwa.shq.Main;
import ru.muwa.shq.engine.Game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class ItemTextures {

    //Ключ - item id, значение - изображение (текстура).
    public static HashMap<Integer, BufferedImage> repo = new HashMap<>();
    public static BufferedImage containerBG, inventory;

    static
    {
        try {
            ClassLoader classLoader = Main.class.getClassLoader();

            //Загрузка картинок в ОЗУ
            InputStream inputStream = classLoader.getResourceAsStream(Game.imgPath + "flour.png");
            BufferedImage image = ImageIO.read(inputStream); repo.put(1,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "containerBG.png");
            containerBG = ImageIO.read(inputStream);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "inventory.png");
            inventory = ImageIO.read(inputStream);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "knife_icon.png");
            image = ImageIO.read(inputStream); repo.put(2, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "makarov.png");
            image = ImageIO.read(inputStream); repo.put(3, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "sneakers.png");
            image = ImageIO.read(inputStream); repo.put(4, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "leather.png");
            image = ImageIO.read(inputStream); repo.put(5, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "gasmask.png");
            image = ImageIO.read(inputStream); repo.put(6, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "weed.png");
            image = ImageIO.read(inputStream); repo.put(7, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "100_rub.png");
            image = ImageIO.read(inputStream); repo.put(8, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "apple.png");
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "potato.png");
            image = ImageIO.read(inputStream); repo.put(31, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "cabbage.png");
            image = ImageIO.read(inputStream); repo.put(32, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "macaroni.png");
            image = ImageIO.read(inputStream); repo.put(33, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "egg.png");
            image = ImageIO.read(inputStream); repo.put(34, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "rice.png");
            image = ImageIO.read(inputStream); repo.put(35, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "milk.png");
            image = ImageIO.read(inputStream); repo.put(36, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "tomato.png");
            image = ImageIO.read(inputStream); repo.put(37, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "butter.png");
            image = ImageIO.read(inputStream); repo.put(38, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "oil.png");
            image = ImageIO.read(inputStream); repo.put(39, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "beetroot.png");
            image = ImageIO.read(inputStream); repo.put(40, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "cottage_cheese.png");
            image = ImageIO.read(inputStream); repo.put(41, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "cheese.png");
            image = ImageIO.read(inputStream); repo.put(42, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "bread_w.png");
            image = ImageIO.read(inputStream); repo.put(43, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "bread_b.png");
            image = ImageIO.read(inputStream); repo.put(44, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "flour_baking.png");
            image = ImageIO.read(inputStream); repo.put(45, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "herbs.png");
            image = ImageIO.read(inputStream); repo.put(46, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "valokordin.png");
            image = ImageIO.read(inputStream); repo.put(47, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "validol.png");
            image = ImageIO.read(inputStream); repo.put(48, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "korvalol.png");
            image = ImageIO.read(inputStream); repo.put(49, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "pasta.png");
            image = ImageIO.read(inputStream); repo.put(50, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "farsh.png");
            image = ImageIO.read(inputStream); repo.put(51, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "beef.png");
            image = ImageIO.read(inputStream); repo.put(52, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "chicken.png");
            image = ImageIO.read(inputStream); repo.put(53, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "gurken.png");
            image = ImageIO.read(inputStream); repo.put(54, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "onion.png");
            image = ImageIO.read(inputStream); repo.put(55, image);

            System.out.println("изображения предметов успешно загружены");

        } catch (IOException e) {

            System.out.println("ошибка при загрузке изображений предметов");
            throw new RuntimeException(e);

        }
    }
    public static void foo(){}

}
