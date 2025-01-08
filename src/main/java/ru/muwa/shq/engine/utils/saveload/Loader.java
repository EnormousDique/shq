package ru.muwa.shq.engine.utils.saveload;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.muwa.shq.engine.Game;
import ru.muwa.shq.engine.utils.GameTime;
import ru.muwa.shq.entities.Item;
import ru.muwa.shq.entities.Level;
import ru.muwa.shq.entities.LevelSkeleton;
import ru.muwa.shq.entities.Player;
import ru.muwa.shq.story.Dialogue;
import ru.muwa.shq.story.Quest;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Loader {
    private static String PATH = "";

    public static boolean load() {
        try{
            // Получаем путь до директории с джарником
            Path jarPath = Paths.get(Saver.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
            // и директории с сейвом
            File saveDirectory = new File(jarPath.toFile(), "shq_save");
            PATH = saveDirectory.getPath();

            ObjectMapper objectMapper = new ObjectMapper();

            //Загружаем вещи инвентаря
            Game.player.items =
                    objectMapper.readValue(
                            new File(PATH + "\\items.json"),
                            new TypeReference<>() {
                            }
                    );

            //Загружаем уровни
            //Список уровней
            ArrayList<File> levelFiles = new ArrayList<>();
            //Проходимся по файлам в папке сохранений в поисках файлов уровней
            DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(PATH));
            for (Path p : stream) if (p.toString().contains("level"))
                levelFiles.add(p.toFile());
            //Собрали список файлов с уровнями.
            // Теперь загружаем их как уровни в движок.
            for (File f : levelFiles){
                LevelSkeleton levelSkeleton = objectMapper.readValue(f,LevelSkeleton.class);
                Level l = new Level(levelSkeleton);
                Level.repo.put(l.id,l);
                System.out.println("Загружен уровень: "+l.id);
            }


            //Загружаем квесты
            Game.player.quests =
                    objectMapper.readValue(
                            new File(PATH + "\\quests.json"),
                            new TypeReference<>() {
                            }
                    );
            System.out.println("Загружены квесты: ");


            //Загружаем прочую информацию об игроке
            PlayerData data = objectMapper.readValue(
                    new File(PATH + "\\player.json"),
                    PlayerData.class
            );
            setPlayerUp(data);

            //Восстанавливаем состояние диалогов
            Dialogue.mom = objectMapper.readValue(new File(PATH + "\\mom_dialogues.json"), new TypeReference<>() {});
            Dialogue.hach = objectMapper.readValue(new File(PATH + "\\hach_dialogues.json"), new TypeReference<>() {});
            Dialogue.hacker = objectMapper.readValue(new File(PATH + "\\hacker_dialogues.json"), new TypeReference<>() {});
            Dialogue.butcher = objectMapper.readValue(new File(PATH + "\\butcher_dialogues.json"), new TypeReference<>() {});
            Dialogue.girl = objectMapper.readValue(new File(PATH + "\\girl_dialogues.json"),new TypeReference<>() {});
            Dialogue.trap = objectMapper.readValue(new File(PATH + "\\trap_dialogues.json"), new TypeReference<>() {});
            Dialogue.officer = objectMapper.readValue(new File(PATH + "\\ment_dialogues.json"), new TypeReference<>() {});
            Dialogue.nurse = objectMapper.readValue(new File(PATH + "\\nurse_dialogues.json"),new TypeReference<>() {});
            Dialogue.mech = objectMapper.readValue(new File(PATH + "\\mech_dialogues.json"), new TypeReference<>() {});
            Dialogue.pharmacist = objectMapper.readValue(new File(PATH + "\\pharmacist_dialogues.json"),new TypeReference<>() {});



            //Восстанавливаем текущий уровень из сохранения и ставим в движок.
            Game.currentLevel =
                    Level.repo.get(data.currentLevelId);
            System.out.println("current level: "+ Game.currentLevel);


            return true;

        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }

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

        GameTime.value = data.time;
        Collections.copy(data.passwords,Game.player.passwords);
    }
}
