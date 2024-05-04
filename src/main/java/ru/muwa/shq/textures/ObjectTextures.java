package ru.muwa.shq.textures;

import ru.muwa.shq.Main;
import ru.muwa.shq.engine.Game;

import javax.imageio.ImageIO;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class ObjectTextures {
    public static HashMap<Integer, BufferedImage> repo = new HashMap<>(),
                                                  transparentRepo = new HashMap<>();

    public static final int PLAYER = 0;
    static
    {
        
        try {
        
            //Загрузка картинок в ОЗУ
            ClassLoader classLoader = Main.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(Game.imgPath+"postbox_game.png");
            BufferedImage image;

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "shquiper.png");
            image = ImageIO.read(inputStream);  repo.put(PLAYER, image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "tumba.png");
            image = ImageIO.read(inputStream);  repo.put(1, image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "mom.png");
            image = ImageIO.read(inputStream);  repo.put(2, image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "postbox.png");
            image = ImageIO.read(inputStream);  repo.put(3, image); image = ImageIO.read(inputStream);  repo.put(16,image); image = ImageIO.read(inputStream);  repo.put(44441,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "gopNick.png");
            image = ImageIO.read(inputStream);  repo.put(4,image); image = ImageIO.read(inputStream);  repo.put(33331,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "schitok.png");
            image = ImageIO.read(inputStream);  repo.put(5,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "citizen.png");
            image = ImageIO.read(inputStream);  repo.put(6,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "cop_handgun.png");
            image = ImageIO.read(inputStream);  repo.put(7,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "corpse.png");
            image = ImageIO.read(inputStream);  repo.put(8,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "school_building.png");
            image = ImageIO.read(inputStream);  repo.put(9,image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "school_building_trans.png");
            image = ImageIO.read(inputStream);  transparentRepo.put(9,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "9_floor.png");
            image = ImageIO.read(inputStream);  repo.put(10,image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "9_floor_trans.png");
            image = ImageIO.read(inputStream);  transparentRepo.put(10,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "9_floor_pados.png");
            image = ImageIO.read(inputStream);  repo.put(11,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "elevator_door.png");
            image = ImageIO.read(inputStream);  repo.put(12,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "butchers_place.png");
            image = ImageIO.read(inputStream);  repo.put(13,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "trap_house.png");
            image = ImageIO.read(inputStream);  repo.put(14,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "metal_fence.png");
            image = ImageIO.read(inputStream);  repo.put(17,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "metal_fence_vert.png");
            image = ImageIO.read(inputStream);  repo.put(18,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "hospital_building.png");
            image = ImageIO.read(inputStream);  repo.put(19,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "grocery_goods.png");
            image = ImageIO.read(inputStream);  repo.put(20,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "drugstore_goods.png");
            image = ImageIO.read(inputStream);  repo.put(21,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "shop_floor.png");
            image = ImageIO.read(inputStream);  repo.put(22,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "h_w_400_60.png");
            image = ImageIO.read(inputStream);  repo.put(23,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "register.png");
            image = ImageIO.read(inputStream);  repo.put(24,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "v_w_60_400.png");
            image = ImageIO.read(inputStream);  repo.put(25,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "shop_building.png");
            image = ImageIO.read(inputStream);  repo.put(26,image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "shop_building_trans.png");
            image = ImageIO.read(inputStream);  transparentRepo.put(26,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "drugstore_building.png");
            image = ImageIO.read(inputStream);  repo.put(27,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "bus_stop.png");
            image = ImageIO.read(inputStream);  repo.put(28,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "market_building.png");
            image = ImageIO.read(inputStream);  repo.put(29,image);
            inputStream = classLoader.getResourceAsStream(Game.imgPath + "market_building_trans.png");
            image = ImageIO.read(inputStream);  transparentRepo.put(29,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "market_floor.png");
            image = ImageIO.read(inputStream);  repo.put(30,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "meat_shelf.png");
            image = ImageIO.read(inputStream);  repo.put(31,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "market_clothes.png");
            image = ImageIO.read(inputStream);  repo.put(32,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "market_veg.png");
            image = ImageIO.read(inputStream);  repo.put(33,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "stone_fence.png");
            image = ImageIO.read(inputStream);  repo.put(34,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "stone_fence_vert.png");
            image = ImageIO.read(inputStream);  repo.put(35,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "ambulance.png");
            image = ImageIO.read(inputStream);  repo.put(36,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "garage.png");
            image = ImageIO.read(inputStream);  repo.put(37,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "garage_vert.png");
            image = ImageIO.read(inputStream);  repo.put(38,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "brick_building.png");
            image = ImageIO.read(inputStream);  repo.put(39,image); image = ImageIO.read(inputStream);  transparentRepo.put(39,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "pc_object.png");
            image = ImageIO.read(inputStream);  repo.put(40,image); image = ImageIO.read(inputStream);  repo.put(41,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "rock_si.png");
            image = ImageIO.read(inputStream);  repo.put(42,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "hospital_bg.png");
            image = ImageIO.read(inputStream);  repo.put(43,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "hospital_bed.png");
            image = ImageIO.read(inputStream);  repo.put(44,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "hospital_cabinet_closed.png");
            image = ImageIO.read(inputStream);  repo.put(45,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "hospital_cabinet_opened.png");
            image = ImageIO.read(inputStream);  repo.put(46,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "ride_obj.png");
            image = ImageIO.read(inputStream);  repo.put(47,image);

            image = ImageIO.read(classLoader.getResourceAsStream(Game.imgPath+"car_2.png"));
            repo.put(53,image);

            image = ImageIO.read(classLoader.getResourceAsStream(Game.imgPath+"car_3.png"));
            repo.put(54,image);

            image = ImageIO.read(classLoader.getResourceAsStream(Game.imgPath+"car_5.png"));
            repo.put(55,image);

            image = ImageIO.read(classLoader.getResourceAsStream(Game.imgPath+"school_bg.png"));
            repo.put(58,image);

            image = ImageIO.read(classLoader.getResourceAsStream(Game.imgPath+"home_1.png"));
            repo.put(62,image); transparentRepo.put(62,image);

            image = ImageIO.read(classLoader.getResourceAsStream(Game.imgPath+"home_2.png"));
            repo.put(63,image); transparentRepo.put(63,image);

            image = ImageIO.read(classLoader.getResourceAsStream(Game.imgPath+"home_3.png"));
            repo.put(64,image); transparentRepo.put(64,image);

            image = ImageIO.read(classLoader.getResourceAsStream(Game.imgPath+"wood_fence.png"));
            repo.put(65,image);

            image = ImageIO.read(classLoader.getResourceAsStream(Game.imgPath+"wood_fence_vert.png"));
            repo.put(66,image);

            image = ImageIO.read(classLoader.getResourceAsStream(Game.imgPath+"roses_object.png"));
            repo.put(67,image);

            image = ImageIO.read(classLoader.getResourceAsStream(Game.imgPath+"garage_door.png"));
            repo.put(70,image);

            image = ImageIO.read(classLoader.getResourceAsStream(Game.imgPath+"kotlovan.png"));
            repo.put(71,image);

            image = ImageIO.read(classLoader.getResourceAsStream(Game.imgPath+"construction.png"));
            repo.put(72,image);

            image = ImageIO.read(classLoader.getResourceAsStream(Game.imgPath+"pipes.png"));
            repo.put(73,image);

            image = ImageIO.read(classLoader.getResourceAsStream(Game.imgPath+"garage_goods.png"));
            repo.put(74,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "indoor/hub_floor.png");
            image = ImageIO.read(inputStream);  repo.put(100,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "indoor/fridge.png");
            image = ImageIO.read(inputStream);  repo.put(101,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "indoor/stove.png");
            image = ImageIO.read(inputStream);  repo.put(102,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "indoor/pan.png");
            image = ImageIO.read(inputStream);  repo.put(103,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "indoor/hub_wall_b.png");
            image = ImageIO.read(inputStream);  repo.put(104,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "indoor/hub_wall_t.png");
            image = ImageIO.read(inputStream);  repo.put(105,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "indoor/hub_wall_r.png");
            image = ImageIO.read(inputStream);  repo.put(106,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "indoor/hub_wall_l.png");
            image = ImageIO.read(inputStream);  repo.put(107,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "indoor/door.png");
            image = ImageIO.read(inputStream);  repo.put(108,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "bed.png");
            image = ImageIO.read(inputStream);  repo.put(110,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "5_floor.png");
            image = ImageIO.read(inputStream);  repo.put(200,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "5_floor_trans.png");
            image = ImageIO.read(inputStream);  transparentRepo.put(200,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "padik_door.png");
            image = ImageIO.read(inputStream);  repo.put(201,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "5_floor_pados.png");
            image = ImageIO.read(inputStream);  repo.put(202,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "heater.png");
            image = ImageIO.read(inputStream);  repo.put(207,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "windowsill.png");
            image = ImageIO.read(inputStream);  repo.put(208,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "GarbageChute.png");
            image = ImageIO.read(inputStream);  repo.put(226,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "heater.png");
            image = ImageIO.read(inputStream);  repo.put(227,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "bullet.png");
            image = ImageIO.read(inputStream);  repo.put(666,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "bullet.png");
            image = ImageIO.read(inputStream);  repo.put(777,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "bg_STREET.png");
            image = ImageIO.read(inputStream);  repo.put(999,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "gopNick.png");
            image = ImageIO.read(inputStream);  repo.put(1111,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "gopNick.png");
            image = ImageIO.read(inputStream);  repo.put(2222,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "gopNick.png");
            image = ImageIO.read(inputStream);  repo.put(3333,image);

            image = ImageIO.read(classLoader.getResourceAsStream(Game.imgPath+"gopNick.png"));
            repo.put(3333_1,image);repo.put(3333_2,image);repo.put(3333_4,image); repo.put(3333_5,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "gopNick.png");
            image = ImageIO.read(inputStream);  repo.put(4444,image); repo.put(4444_1,image); repo.put(4444_2,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "gopNick.png");
            image = ImageIO.read(inputStream);  repo.put(5555,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "gopNick.png");
            image = ImageIO.read(inputStream);  repo.put(7777,image);

            inputStream = classLoader.getResourceAsStream(Game.imgPath + "gopNick.png");
            image = ImageIO.read(inputStream);  repo.put(8888,image);
            
            System.out.println("изображения объектов успешно загружены");
        } catch (IOException e) {
            System.out.println("ошибка при загрузке изображений");
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public static void foo(){}

}
