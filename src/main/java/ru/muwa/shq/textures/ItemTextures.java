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
    public static BufferedImage containerBG, inventory,hudImage,clothes,friends,quests,quest,done;

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


            inputStream = classLoader.getResourceAsStream(Game.imgPath + "hud_stats.png");
            hudImage = ImageIO.read(inputStream);


            inputStream = classLoader.getResourceAsStream(Game.imgPath + "clothes_hud.png");
            clothes = ImageIO.read(inputStream);


            inputStream = classLoader.getResourceAsStream(Game.imgPath + "friends.png");
            friends = ImageIO.read(inputStream);


            inputStream = classLoader.getResourceAsStream(Game.imgPath + "quests.png");
            quests = ImageIO.read(inputStream);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "quest.png");
            quest = ImageIO.read(inputStream);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "done.png");
            done = ImageIO.read(inputStream);



            inputStream = classLoader.getResourceAsStream(Game.imgPath + "knife_icon.png");
            image = ImageIO.read(inputStream); repo.put(2, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "makarov.png");
            image = ImageIO.read(inputStream); repo.put(3, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "sneakers_bad.png");
            image = ImageIO.read(inputStream); repo.put(4, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "leather.png");
            image = ImageIO.read(inputStream); repo.put(5, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "gasmask.png");
            image = ImageIO.read(inputStream); repo.put(6, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "weed.png");
            image = ImageIO.read(inputStream); repo.put(7, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "100_rub.png");
            image = ImageIO.read(inputStream); repo.put(8, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "ammo_9_18.png");
            image = ImageIO.read(inputStream); repo.put(9, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "apple.png");
            image = ImageIO.read(inputStream); repo.put(30, image);
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

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "carrot.png");
            image = ImageIO.read(inputStream); repo.put(56, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "cabbage.png");
            image = ImageIO.read(inputStream); repo.put(57, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "chicken_fry.png");
            image = ImageIO.read(inputStream); repo.put(58, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "steak.png");
            image = ImageIO.read(inputStream); repo.put(59, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "cutlet.png");
            image = ImageIO.read(inputStream); repo.put(60, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "chicken_soup.png");
            image = ImageIO.read(inputStream); repo.put(61, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "potato_fry.png");
            image = ImageIO.read(inputStream); repo.put(62, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "syrnique.png");
            image = ImageIO.read(inputStream); repo.put(63, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "borsch.png");
            image = ImageIO.read(inputStream); repo.put(64, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "lube.png");
            image = ImageIO.read(inputStream); repo.put(65, image); repo.put(136,image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "smokes.png");
            image = ImageIO.read(inputStream); repo.put(66, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "matches.png");
            image = ImageIO.read(inputStream); repo.put(67, image);
            //inputStream = classLoader.getResourceAsStream(Game.imgPath + ".png");
            //image = ImageIO.read(inputStream); repo.put(68, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "joint.png");
            image = ImageIO.read(inputStream); repo.put(69, image);
            //inputStream = classLoader.getResourceAsStream(Game.imgPath + ".png");
            //image = ImageIO.read(inputStream); repo.put(70, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "cpu.png");
            image = ImageIO.read(inputStream); repo.put(71, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "pc_icon.png");
            image = ImageIO.read(inputStream); repo.put(72, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "box.png");
            image = ImageIO.read(inputStream); repo.put(73, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "bank_card.png");
            image = ImageIO.read(inputStream); repo.put(74, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "flowers.png");
            image = ImageIO.read(inputStream); repo.put(75, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "skimmer.png");
            image = ImageIO.read(inputStream); repo.put(76, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "pack.png");
            image = ImageIO.read(inputStream); repo.put(77, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "vodka.png");
            image = ImageIO.read(inputStream); repo.put(78, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "card_key.png");
            image = ImageIO.read(inputStream); repo.put(79, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "key.png");
            image = ImageIO.read(inputStream); repo.put(80, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "syringe.png");
            image = ImageIO.read(inputStream); repo.put(81, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "scrub.png");
            image = ImageIO.read(inputStream); repo.put(82, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "ride_icon.png");
            image = ImageIO.read(inputStream); repo.put(83, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "beer.png");
            image = ImageIO.read(inputStream); repo.put(84, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "lom.png");
            image = ImageIO.read(inputStream); repo.put(85, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "todler.png");
            image = ImageIO.read(inputStream); repo.put(86, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "flowers.png");
            image = ImageIO.read(inputStream); repo.put(87, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "sekator.png");
            image = ImageIO.read(inputStream); repo.put(88, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "joint.png");
            image = ImageIO.read(inputStream); repo.put(89, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "rolling_paper.png");
            image = ImageIO.read(inputStream); repo.put(90, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "gas_can.png");
            image = ImageIO.read(inputStream); repo.put(91, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "shotgun.png");
            image = ImageIO.read(inputStream); repo.put(92, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "spade.png");
            image = ImageIO.read(inputStream); repo.put(93, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "army_boots.png");
            image = ImageIO.read(inputStream); repo.put(94, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "pipes_icon.png");
            image = ImageIO.read(inputStream); repo.put(95, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "gayger.png");
            image = ImageIO.read(inputStream); repo.put(96, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "uran.png");
            image = ImageIO.read(inputStream); repo.put(97, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "sneakers_good.png");
            image = ImageIO.read(inputStream); repo.put(98, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "vape.png");
            image = ImageIO.read(inputStream); repo.put(99, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "webcam.png");
            image = ImageIO.read(inputStream); repo.put(100, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "bottle.png");
            image = ImageIO.read(inputStream); repo.put(101, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "bottle.png");
            image = ImageIO.read(inputStream); repo.put(102, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "500_rub.png");
            image = ImageIO.read(inputStream); repo.put(103, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "1000_rub.png");
            image = ImageIO.read(inputStream); repo.put(104, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "5000_rub.png");
            image = ImageIO.read(inputStream); repo.put(105, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "phone.png");
            image = ImageIO.read(inputStream); repo.put(106, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "shotgun_ammo.png");
            image = ImageIO.read(inputStream); repo.put(107, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "ram.png");
            image = ImageIO.read(inputStream); repo.put(108, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "monitor.png");
            image = ImageIO.read(inputStream); repo.put(109, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "gpu.png");
            image = ImageIO.read(inputStream); repo.put(110, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "keyboard.png");
            image = ImageIO.read(inputStream); repo.put(111, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "mouse.png");
            image = ImageIO.read(inputStream); repo.put(112, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "dildo.png");
            image = ImageIO.read(inputStream); repo.put(113, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "zatochka.png");
            image = ImageIO.read(inputStream); repo.put(114, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "finka.png");
            image = ImageIO.read(inputStream); repo.put(115, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "cat_food.png");
            image = ImageIO.read(inputStream); repo.put(116, image); repo.put(120,image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "dog_meat.png");
            image = ImageIO.read(inputStream); repo.put(117, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "shawarma.png");
            image = ImageIO.read(inputStream); repo.put(118, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "trunk.png");
            image = ImageIO.read(inputStream); repo.put(119, image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "docs.png");
            image = ImageIO.read(inputStream); repo.put(121, image); repo.put(133,image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "armor.png");
            image = ImageIO.read(inputStream); repo.put(122, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "build_hat.png");
            image = ImageIO.read(inputStream); repo.put(123, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "hockey_hat.png");
            image = ImageIO.read(inputStream); repo.put(124, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "army_hat.png");
            image = ImageIO.read(inputStream); repo.put(125, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "ak.png");
            image = ImageIO.read(inputStream); repo.put(126, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "ak_ammo.png");
            image = ImageIO.read(inputStream); repo.put(127, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "wash.png");
            image = ImageIO.read(inputStream); repo.put(128, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "phone_old.png");
            image = ImageIO.read(inputStream); repo.put(129, image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "ring.png");
            image = ImageIO.read(inputStream); repo.put(131, image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "flamethrower.png");
            image = ImageIO.read(inputStream); repo.put(134, image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "olymp.png");
            image = ImageIO.read(inputStream); repo.put(135, image);


            inputStream = classLoader.getResourceAsStream(Game.imgPath + "e_drink.png");
            image = ImageIO.read(inputStream); repo.put(137, image);






            System.out.println("изображения предметов успешно загружены");

        } catch (IOException e) {

            System.out.println("ошибка при загрузке изображений предметов");
            throw new RuntimeException(e);

        }
    }
    public static void foo(){}

}
