package ru.muwa.shq.engine.utils.saveload;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.muwa.shq.engine.Game;
import ru.muwa.shq.entities.Item;
import ru.muwa.shq.entities.Level;
import ru.muwa.shq.entities.LevelSkeleton;
import ru.muwa.shq.entities.Player;
import ru.muwa.shq.story.Dialogue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

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
                var skeleton = new LevelSkeleton(l);
                objectMapper.writeValue(new File(PATH + "\\level_"+l.id+".json"),skeleton);
            }
            // ���������� ������
            objectMapper.writeValue(new File(PATH + "\\quests.json"),Game.player.quests);

            objectMapper.writeValue(new File(PATH + "\\player.json" ), new PlayerData(Game.player));

            //��������� �������
            objectMapper.writeValue(new File(PATH + "\\mom_dialogues.json"), Dialogue.mom);
            objectMapper.writeValue(new File(PATH + "\\hach_dialogues.json"), Dialogue.hach);
            objectMapper.writeValue(new File(PATH + "\\hacker_dialogues.json"), Dialogue.hacker);



            return true;
        } catch (Exception e){
            // ���� ���-�� ����� �� ���
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }


    }


}
