package ru.muwa.shq.engine.utils.saveload;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.muwa.shq.engine.Game;
import ru.muwa.shq.entities.*;
import ru.muwa.shq.story.Dialogue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import static ru.muwa.shq.entities.Level.STREET_1;

/**
 * ����� �������������� ���������� ����.
 */
public class Saver {
    private static String PATH = "\\shq_save";//"C:\\Program Files\\shq_save";

    public static void work()
    {
        if(save()) System.out.println("���� ��������� �������");
        else System.out.println("������ ��� ����������");


    }
    //����� ���������� ����. ���������� ������ �������� (����� / �� �����)
    public static boolean save(){
        try {
        // �������� ���� � �����, ��� ��������� JAR-����
        Path jarPath = Paths.get(Saver.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        File saveDirectory = new File(jarPath.toFile(), "shq_save");
        PATH = saveDirectory.getPath();

        // ������� �����, ���� ��� �� ����������
        if (!saveDirectory.exists()) {
            saveDirectory.mkdirs();
        }
        ObjectMapper objectMapper = new ObjectMapper();

            // ���������� ���� ������
            objectMapper.writeValue(new File(PATH + "\\items.json"), Game.player.items);
            // ���������� ������
            for(var l : Level.repo.values())
            {
                if(l.id==STREET_1) clearLevelFromCars(l);
                var skeleton = new LevelSkeleton(l);
                objectMapper.writeValue(new File(PATH + "\\level_"+l.id+".json"),skeleton);
            }
            // ���������� ������
            objectMapper.writeValue(new File(PATH + "\\quests.json"),Game.player.quests);

            // ���������� ������ ������
            objectMapper.writeValue(new File(PATH + "\\player.json" ), new PlayerData(Game.player));

            //��������� �������
            objectMapper.writeValue(new File(PATH + "\\mom_dialogues.json"), Dialogue.mom);
            objectMapper.writeValue(new File(PATH + "\\hach_dialogues.json"), Dialogue.hach);
            objectMapper.writeValue(new File(PATH + "\\hacker_dialogues.json"), Dialogue.hacker);
            objectMapper.writeValue(new File(PATH + "\\butcher_dialogues.json"), Dialogue.butcher);
            objectMapper.writeValue(new File(PATH + "\\girl_dialogues.json"), Dialogue.girl);
            objectMapper.writeValue(new File(PATH + "\\trap_dialogues.json"), Dialogue.trap);
            objectMapper.writeValue(new File(PATH + "\\ment_dialogues.json"), Dialogue.officer);
            objectMapper.writeValue(new File(PATH + "\\nurse_dialogues.json"), Dialogue.nurse);
            objectMapper.writeValue(new File(PATH + "\\mech_dialogues.json"), Dialogue.mech);
            objectMapper.writeValue(new File(PATH + "\\pharmacist_dialogues.json"), Dialogue.pharmacist);










            return true;
        } catch (Exception e){
            // ���� ���-�� ����� �� ���
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static void clearLevelFromCars(Level l) {
        ArrayList<GameObject> cars = new ArrayList<>();
        for(var o : l.objects) if(o.speed >= 10) cars.add(o);
        for(var o : l.objects) if(o.id == 8) cars.add(o); //����� ������� ������ � ��������
        for( var car : cars) l.objects.remove(car);
    }


}
