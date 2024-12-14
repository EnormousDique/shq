package ru.muwa.shq.entities;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.muwa.shq.Main;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Level {
    public static HashMap<Integer,Level> repo = new HashMap<>();
    public static final int HUB = 0, // Квартира Шкипера (мамы)
                            STREET_1 = 1, // Улица
                            GROCERY = 2, // Внутренность продуктового
                            POST_OFFICE = 3, // Внутренность почты
                            DRUGSTORE = 4, // Внутренность аптеки
                            POLICE_OFFICE = 5, // Внутр. полиц. уч.
                            BUILDING_1 = 6, // 5-этажка дом Шкипера
                            BUTCHERY = 7, // Мясной цех
                            HACKERS_PLACE = 8, // Квартира хакера
                            BUILDING_13 = 9,// 5-этажка с аптекой
                            BUILDING_2 = 10, // 5-этажка справа от дома
                            HOSPITAL = 11,//Внутренности больницы
                            BUILDING_15 = 12,//5-этажка с кв. мясника (дом у школы)
                            BUTCHERS_PLACE = 13,//Квартира мясника
                            TRAP_HOUSE = 14,//Квартира-притон
                            BUILDING_5 = 15,//9-этажка с притоном (на севере города у продуктового)
                            BUILDING_17 = 16,//9-этажка с хакером (на юге города у больницы)
                            WILDBERRIES = 17,//ПВЗ ВАЛБЕРС
                            BUILDING_9 = 18,//5-этажка у детской площадки (с кв. техника)
                            MECH_FLAT = 19,//Квартира механика
                            SCHOOL = 20,//Школа
                            PACAN_FLAT = 21,//Квартира с пацаном (для кражи)
                            GARAGE = 22,//Гараж техника
                            SALON_SVYAZI = 23,//Салон связи
                            JAIL = 24, //Тюрьма
                            SEWERS = 25,//Канализация
                            CONSTRUCTION = 26,//Стройка
                            SOCCER_FIELD = 27,//Футбольная коробка
                            TRAP_HOUSE_2 = 28, //Второй притон (по квесту аптекарши)
                            BUILDING_16 = 29, //Пятиэтажка на озерной (за больницей)
                            BANDIT_FLAT = 30, // Квартира блатного авторитета (для подброса наркоты)
                            BANDIT_FRIEND_FLAT = 31, // Квартира подельника авторитета. (Там охраняют ключ)
                            ROOF_RUINS = 32, //Руины крыши, где драка с боссом
                            BUILDING_7 = 33, // 9-этажка на углу сквозной и речной ул
                            BUILDING_8 = 34, //9-этажка на парковой (у входа в парк с кв блатного авторитета ??и медбрата??)
                            BUILDING_3 = 35, //5-этажка снизу от дома Шкипера
                            BUILDING_4 = 36, //5-этажка снизу-справа от дома Шкипера
                            BUILDING_6 = 37, //9-этажка с почтой (справа от продуктового)
                            BUILDING_10 = 38, //9-этажка с валберс
                            BUILDING_11 = 39, //Верхняя многоэтажка у автовокзала
                            BUILDING_12 = 40, //Нижняя многоэтажка у автовокзала
                            BUILDING_14 = 41, // Многоэтажка между аптекой и школой
                            NURSE_FLAT = 42, //Квартира медбрата
                            RENT_APARTMENTS = 43, //Квартира, которая сдается в аренду
                            TRAP_DUDE_PLACE = 44; //Квартира пидараса

    public int id;

    public List<GameObject> objects = new ArrayList<>();
    public boolean isStreet, isIndoors;
    public double noise = 0.0;

    static
    {
        String pathToJsonWithItems = "json/level_0.json";
        readItemsFromJsonFile(pathToJsonWithItems);

        pathToJsonWithItems = "json/level_1.json";
        readItemsFromJsonFile(pathToJsonWithItems);

        pathToJsonWithItems = "json/level_2.json";
        readItemsFromJsonFile(pathToJsonWithItems);

        pathToJsonWithItems = "json/level_3.json";
        readItemsFromJsonFile(pathToJsonWithItems);

        pathToJsonWithItems = "json/level_4.json";
        readItemsFromJsonFile(pathToJsonWithItems);

        pathToJsonWithItems = "json/level_5.json";
        readItemsFromJsonFile(pathToJsonWithItems);

        pathToJsonWithItems = "json/level_6.json";
        readItemsFromJsonFile(pathToJsonWithItems);

        pathToJsonWithItems = "json/level_7.json";
        readItemsFromJsonFile(pathToJsonWithItems);

        pathToJsonWithItems = "json/level_8.json";
        readItemsFromJsonFile(pathToJsonWithItems);

        pathToJsonWithItems = "json/level_9.json";
        readItemsFromJsonFile(pathToJsonWithItems);

        pathToJsonWithItems = "json/level_10.json";
        readItemsFromJsonFile(pathToJsonWithItems);

        pathToJsonWithItems = "json/level_11.json";
        readItemsFromJsonFile(pathToJsonWithItems);

        pathToJsonWithItems = "json/level_12.json";
        readItemsFromJsonFile(pathToJsonWithItems);

        pathToJsonWithItems = "json/level_13.json";
        readItemsFromJsonFile(pathToJsonWithItems);

        pathToJsonWithItems = "json/level_14.json";
        readItemsFromJsonFile(pathToJsonWithItems);

        pathToJsonWithItems = "json/level_15.json";
        readItemsFromJsonFile(pathToJsonWithItems);

        pathToJsonWithItems = "json/level_16.json";
        readItemsFromJsonFile(pathToJsonWithItems);

        pathToJsonWithItems = "json/level_17.json";
        readItemsFromJsonFile(pathToJsonWithItems);

        pathToJsonWithItems = "json/level_18.json";
        readItemsFromJsonFile(pathToJsonWithItems);

        pathToJsonWithItems = "json/level_19.json";
        readItemsFromJsonFile(pathToJsonWithItems);

        pathToJsonWithItems = "json/level_20.json";
        readItemsFromJsonFile(pathToJsonWithItems);

        readItemsFromJsonFile("json/level_21.json");
        readItemsFromJsonFile("json/level_22.json");
        readItemsFromJsonFile("json/level_23.json");
        readItemsFromJsonFile("json/level_24.json");
        readItemsFromJsonFile("json/level_25.json");
        readItemsFromJsonFile("json/level_26.json");
        readItemsFromJsonFile("json/level_27.json");
        readItemsFromJsonFile("json/level_28.json");
        readItemsFromJsonFile("json/level_29.json");

        readItemsFromJsonFile("json/level_30.json");
        readItemsFromJsonFile("json/level_31.json");
        readItemsFromJsonFile("json/level_32.json");

        readItemsFromJsonFile("json/level_34.json");

        readItemsFromJsonFile("json/level_42.json");
        readItemsFromJsonFile("json/level_43.json");
        readItemsFromJsonFile("json/level_44.json");



        System.out.println("Список уровней успешно инициализирован");
    }

    public Level(){}
    public Level(LevelSkeleton levelSkeleton) {
        id = levelSkeleton.levelId;
        isIndoors = levelSkeleton.isIndoors;
        isStreet = levelSkeleton.isStreet;
        for(LevelSkeleton.GameObjectPlacement op : levelSkeleton.objectPlacements)
        {
            GameObject obj = GameObject.get(op.id);

            obj.x=op.x; obj.y = op.y;

            obj.items = new ArrayList<>();

            if(op.items != null && !op.items.isEmpty()) for (Map.Entry<Integer,Integer> item :op.items.entrySet()){
                Item newItem = Item.get(item.getKey());
                newItem.count = item.getValue();
                obj.items.add(newItem);
            }

            if(op.coordinates != null) obj.coordinates = op.coordinates;

            objects.add(obj);
        }
    }

    private static void readItemsFromJsonFile(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Получаем InputStream из ресурсов с использованием ClassLoader'а
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(filePath);
            // Чтение данных из JSON файла и преобразование их в список объектов Item
            LevelSkeleton levelSkeleton = objectMapper.readValue(inputStream, LevelSkeleton.class);

                Level level = new Level(levelSkeleton);
                repo.put(level.id,level);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void foo(){}


}
