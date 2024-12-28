package ru.muwa.shq.engine.utils.saveload;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.muwa.shq.engine.Game;
import ru.muwa.shq.engine.utils.GameTime;
import ru.muwa.shq.entities.Item;
import ru.muwa.shq.entities.Level;
import ru.muwa.shq.entities.LevelSkeleton;
import ru.muwa.shq.entities.Player;
import ru.muwa.shq.story.Quest;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Loader {
    private static String PATH = "";

    public static boolean load() {
        try{
            // �������� ���� �� ���������� � ���������
            Path jarPath = Paths.get(Saver.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
            // � ���������� � ������
            File saveDirectory = new File(jarPath.toFile(), "shq_save");
            PATH = saveDirectory.getPath();

            ObjectMapper objectMapper = new ObjectMapper();

            //��������� ���� ���������
            Game.player.items =
                    objectMapper.readValue(
                            new File(PATH + "\\items.json"),
                            new TypeReference<>() {
                            }
                    );

            //��������� ������
            //������ �������
            ArrayList<File> levelFiles = new ArrayList<>();
            //���������� �� ������ � ����� ���������� � ������� ������ �������
            DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(PATH));
            for (Path p : stream) if (p.toString().contains("level"))
                levelFiles.add(p.toFile());
            //������� ������ ������ � ��������.
            // ������ ��������� �� ��� ������ � ������.
            for (File f : levelFiles){
                LevelSkeleton levelSkeleton = objectMapper.readValue(f,LevelSkeleton.class);
                Level l = new Level(levelSkeleton);
                Level.repo.put(l.id,l);
            }

            //��������� ������
            Game.player.quests =
                    objectMapper.readValue(
                            new File(PATH + "\\quests.json"),
                            new TypeReference<>() {
                            }
                    );

            //��������� ������ ���������� �� ������
            PlayerData data = objectMapper.readValue(
                    new File(PATH + "\\player.json"),
                    new TypeReference<>() {
                    }
            );
            setPlayerUp(data);

        }catch (Exception e){
            return false;
        }


        return true;
    }

    private static void setPlayerUp(PlayerData data) {
        Game.player.x = data.x;
        Game.player.y = data.y;
        Game.player.pee = data.pee;
        Game.player.poo = data.poo;
        Game.player.money = data.money;
        Game.player.hunger = data.hunger;
        Game.player.thirst = data.thirst;
        Game.player.crazy = data.crazy;
        Game.player.sleepy = data.sleepy;
        Game.player.wanted = data.wanted;
        Game.player.smoke = data.smoke;
        Game.player.stimulate = data.stimulate;
        Game.player.drunk = data.drunk;
        Game.player.busy = data.busy;
        Game.player.dead = data.dead;
        Game.player.rent = data.rent;
        Game.player.good = data.good;
        Game.player.atm = data.atm;
        Game.player.equip = data.equip;
        Game.player.busFare = data.busFare;
        Game.player.mommaFullness = data.mommaFullness;
        Game.player.mommaHealth = data.mommaHealth;
        Game.player.mommaClean = data.mommaClean;
        Game.player.hat = data.hat;
        Game.player.torso = data.torso;
        Game.player.foot = data.foot;
        Game.currentLevel =
                Level.repo.get(data.currentLevelId);
        GameTime.value = data.time;
    }
}
