package ru.muwa.shq.story;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.muwa.shq.Main;
import ru.muwa.shq.engine.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Dialogue {
    public int id;
    public String message;
    public List<Response> responses = new ArrayList<>();
    public static List<ResponseButton> buttons = new ArrayList<>();
    public static BufferedImage texture;



    public enum Companion {MOM,BUTCHER,HACH,OFFICER,GIRL,TRAP,PHARMACIST,HACKER,MECHANIC,NURSE,NAZI}
    public static Companion companion;
    public static final HashMap<Integer, BufferedImage> textures = new HashMap<>();
    static
    {
        try {
            ClassLoader classLoader = Main.class.getClassLoader();
            BufferedImage image = ImageIO.read(classLoader.getResourceAsStream(Game.imgPath + "hacker_face.png"));
            textures.put(1111, image);
            image = ImageIO.read(classLoader.getResourceAsStream(Game.imgPath + "momma_face.png"));
            textures.put(2, image);
            image = ImageIO.read(classLoader.getResourceAsStream(Game.imgPath + "hach_face.png"));
            textures.put(2222, image);
            image = ImageIO.read(classLoader.getResourceAsStream(Game.imgPath + "butcher_face.png"));
            textures.put(3333, image);
            image = ImageIO.read(classLoader.getResourceAsStream(Game.imgPath + "pedoffka_face_.png"));
            textures.put(4444, image);
            image = ImageIO.read(classLoader.getResourceAsStream(Game.imgPath + "trap_face.png"));
            textures.put(5555, image);
            image = ImageIO.read(classLoader.getResourceAsStream(Game.imgPath + "ment_face.png"));
            textures.put(6666, image);
            image = ImageIO.read(classLoader.getResourceAsStream(Game.imgPath + "nurse_face.png"));
            textures.put(7777, image);
            image = ImageIO.read(classLoader.getResourceAsStream(Game.imgPath + "mech_face.png"));
            textures.put(8888, image);
            image = ImageIO.read(classLoader.getResourceAsStream(Game.imgPath + "pharma_face.png"));
            textures.put(9999, image);
        }catch (Exception e)
        {
            System.out.println("������ ��� �������� �������� ������������");
        }
    }
    public static class Response
    {
        public String text;
        public int nextMessage;
        public int script;

        @Override
        public String toString() {
            return "Response{" +
                    "text='" + text + '\'' +
                    ", nextMessage=" + nextMessage +
                    ", script=" + script +
                    '}';
        }
        public Response(){}
        public Response(String text, int nextMessage, int script){
            this.text = text;
            this.nextMessage = nextMessage;
            this.script = script;
        }
    }
    public static class ResponseButton extends Rectangle
    {
        public ResponseButton(Response response)
        {
            this.text = response.text;
            this.nextMessage = response.nextMessage;
            this.script = response.script;
        }
        public int nextMessage;
        public String text;
        public int script;
    }
    public static Dialogue current;
    public static HashMap<Integer,Dialogue> repo = new HashMap<>();
    public static HashMap<Integer,Dialogue> mom = new HashMap<>();
    public static HashMap<Integer,Dialogue> hach = new HashMap<>();
    public static HashMap<Integer,Dialogue> hacker = new HashMap<>();
    public static HashMap<Integer,Dialogue> girl = new HashMap<>();
    public static HashMap<Integer,Dialogue> trap = new HashMap<>();
    public static HashMap<Integer,Dialogue> nurse = new HashMap<>();
    public static HashMap<Integer,Dialogue> mech = new HashMap<>();
    public static HashMap<Integer,Dialogue> butcher = new HashMap<>();
    public static HashMap<Integer,Dialogue> pharmacist = new HashMap<>();
    public static HashMap<Integer,Dialogue> officer = new HashMap<>();
    static
    {
        fillDialogueMapFromJSON(mom,"json/mom_dialogues.json");

        String pathToJsonWithItems = "json/dialogues.json";
        readDialoguesFromJsonFile(pathToJsonWithItems);
        pathToJsonWithItems = "json/hacker_dialogues.json";
        fillDialogueMapFromJSON(hacker,pathToJsonWithItems);
        pathToJsonWithItems = "json/hach_dialogues.json";
        fillDialogueMapFromJSON(hach,pathToJsonWithItems);
        pathToJsonWithItems = "json/trap_dialogues.json";
        fillDialogueMapFromJSON(trap,pathToJsonWithItems);
        pathToJsonWithItems = "json/nurse_dialogues.json";
        fillDialogueMapFromJSON(nurse,pathToJsonWithItems);
        pathToJsonWithItems = "json/mech_dialogues.json";
        fillDialogueMapFromJSON(mech,pathToJsonWithItems);
        fillDialogueMapFromJSON(butcher,"json/butchers_dialogues.json");
        fillDialogueMapFromJSON(girl,"json/girl_dialogues.json");
        fillDialogueMapFromJSON(pharmacist,"json/pharmacist_dialogues.json");
        fillDialogueMapFromJSON(officer,"json/ment_dialogues.json");
        System.out.println("������ �������� ������� ��������");
    }
    @Override
    public String toString() {
        return "Dialogue{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", responses=" + responses +
                '}';
    }
    private static void readDialoguesFromJsonFile(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // �������� InputStream �� �������� � �������������� ClassLoader'�
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(filePath);
            // ������ ������ �� JSON ����� � �������������� �� � ������ �������� Dialogue
            Dialogue[] dialogues = objectMapper.readValue(inputStream, Dialogue[].class);
            for (Dialogue dialogue : dialogues) {
                repo.put(dialogue.id,dialogue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void fillDialogueMapFromJSON(HashMap<Integer,Dialogue> dialogueMap, String pathToJSON) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // �������� InputStream �� �������� � �������������� ClassLoader'�
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(pathToJSON);
            // ������ ������ �� JSON ����� � �������������� �� � ������ �������� Dialogue
            Dialogue[] dialogues = objectMapper.readValue(inputStream, Dialogue[].class);
            for (Dialogue dialogue : dialogues) {
                dialogueMap.put(dialogue.id,dialogue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Image getTexture() {
        if (companion == null) return null;
        switch (companion){
            case MOM -> {
                return textures.get(Game.mom.id);
            }
            case HACH -> {
                return textures.get(Game.hach.id);
            }
            case HACKER-> {
                return textures.get(Game.hacker.id);
            }
            case BUTCHER -> {
                return textures.get(Game.butcher.id);
            }
            case GIRL -> {
                return textures.get(Game.girl.id);
            }
            case TRAP -> {
                return textures.get(Game.trap.id);
            }
            case OFFICER -> {
                return textures.get(Game.officer.id);
            }
            case NURSE -> {
                return textures.get(Game.nurse.id);
            }
            case MECHANIC -> {
                return textures.get(Game.mechanic.id);
            }
            case PHARMACIST -> {
                return textures.get(Game.pharmacist.id);
            }
            case NAZI -> {
                return textures.get(Game.nazi.id);
            }
        }
        return null;
    }
    public static void foo(){}
}