package ru.muwa.shq.story;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.muwa.shq.Main;
import ru.muwa.shq.engine.utils.GameTime;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class Quest {
    public int id;
    public boolean completed, hasDeadline;
    public long startTime, expirationTime, duration;
    public String owner, title, description;
    @JsonIgnore
    public Rectangle icon = new Rectangle();

    public static HashMap<Integer,Quest> repo = new HashMap<>();
    static
    {
        String pathToJsonWithItems = "json/quests.json";
        readQuestsFromJsonFile(pathToJsonWithItems);
        System.out.println("Список заданий успешно инициализирован");

    }

    private static void readQuestsFromJsonFile(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Получаем InputStream из ресурсов с использованием ClassLoader'а
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(filePath);
            // Чтение данных из JSON файла и преобразование их в список объектов Quest
            Quest[] quests = objectMapper.readValue(inputStream, Quest[].class);
            for (Quest quest :quests ) {
                repo.put(quest.id,quest);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Quest get(int id)
    {
        return new Quest(repo.get(id));
    }


    public Quest (Quest quest)
    {
        this.description = quest.description;
        this.duration = quest.duration;
        this.id = quest.id;
        this.expirationTime = quest.expirationTime;
        this.hasDeadline = quest.hasDeadline;
        this.owner = quest.owner;
        this.startTime = GameTime.get();
        this.title = quest.title;
        this.icon.width = 150;this.icon.height = 35;
    }
    public Quest(){
        this.icon.width = 150;this.icon.height = 35;
    }
    public static void foo(){}
}
