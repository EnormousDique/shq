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
    public static final int HUB = 0, // �������� ������� (����)
                            STREET_1 = 1, // �����
                            GROCERY = 2, // ������������ ������������
                            POST_OFFICE = 3, // ������������ �����
                            DRUGSTORE = 4, // ������������ ������
                            POLICE_OFFICE = 5, // �����. �����. ��.
                            BUILDING_1 = 6, // 5-������ ��� �������
                            BUTCHERY = 7, // ������ ���
                            HACKERS_PLACE = 8, // �������� ������
                            BUILDING_13 = 9,// 5-������ � �������
                            BUILDING_2 = 10, // 5-������ ������ �� ����
                            HOSPITAL = 11,//������������ ��������
                            BUILDING_15 = 12,//5-������ � ��. ������� (��� � �����)
                            BUTCHERS_PLACE = 13,//�������� �������
                            TRAP_HOUSE = 14,//��������-������
                            BUILDING_5 = 15,//9-������ � �������� (�� ������ ������ � ������������)
                            BUILDING_17 = 16,//9-������ � ������� (�� ��� ������ � ��������)
                            WILDBERRIES = 17,//��� �������
                            BUILDING_9 = 18,//5-������ � ������� �������� (� ��. �������)
                            MECH_FLAT = 19,//�������� ��������
                            SCHOOL = 20,//�����
                            PACAN_FLAT = 21,//�������� � ������� (��� �����)
                            GARAGE = 22,//����� �������
                            SALON_SVYAZI = 23,//����� �����
                            JAIL = 24, //������
                            SEWERS = 25,//�����������
                            CONSTRUCTION = 26,//�������
                            SOCCER_FIELD = 27,//���������� �������
                            TRAP_HOUSE_2 = 28, //������ ������ (�� ������ ���������)
                            BUILDING_16 = 29, //���������� �� ������� (�� ���������)
                            BANDIT_FLAT = 30, // �������� �������� ���������� (��� �������� �������)
                            BANDIT_FRIEND_FLAT = 31, // �������� ���������� ����������. (��� �������� ����)
                            ROOF_RUINS = 32, //����� �����, ��� ����� � ������
                            BUILDING_7 = 33, // 9-������ �� ���� �������� � ������ ��
                            BUILDING_8 = 34, //9-������ �� �������� (� ����� � ���� � �� �������� ���������� ??� ��������??)
                            BUILDING_3 = 35, //5-������ ����� �� ���� �������
                            BUILDING_4 = 36, //5-������ �����-������ �� ���� �������
                            BUILDING_6 = 37, //9-������ � ������ (������ �� ������������)
                            BUILDING_10 = 38, //9-������ � �������
                            BUILDING_11 = 39, //������� ����������� � �����������
                            BUILDING_12 = 40, //������ ����������� � �����������
                            BUILDING_14 = 41, // ����������� ����� ������� � ������
                            NURSE_FLAT = 42, //�������� ��������
                            RENT_APARTMENTS = 43, //��������, ������� ������� � ������
                            TRAP_DUDE_PLACE = 44; //�������� ��������

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



        System.out.println("������ ������� ������� ���������������");
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
            // �������� InputStream �� �������� � �������������� ClassLoader'�
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(filePath);
            // ������ ������ �� JSON ����� � �������������� �� � ������ �������� Item
            LevelSkeleton levelSkeleton = objectMapper.readValue(inputStream, LevelSkeleton.class);

                Level level = new Level(levelSkeleton);
                repo.put(level.id,level);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void foo(){}


}
