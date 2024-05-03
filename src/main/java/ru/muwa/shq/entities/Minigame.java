package ru.muwa.shq.entities;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.muwa.shq.Main;
import ru.muwa.shq.engine.GameWindow;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Minigame
{
    public Minigame(){}

    public static Minigame current;

    public int id;
    public Type type;
    public enum Type
    {
        SHQUR, PADIQUE, MOM_CLEAN, DOOR, ELEVATOR,COOK,SHOP,BUS,SLEEP,SINK,TOILET,DEATH,GAS
    }
    public int script;
    public String input = ""; // для padique и elevator
    public Rectangle window = new Rectangle(GameWindow.WIDTH / 4, GameWindow.HEIGHT / 4, 300,200);
    public Rectangle success = new Rectangle();
    public Rectangle exit;
    public ArrayList<InputButton> inputButtons;
    public ArrayList<PurchaseButton> purchaseButtons;
    public Coordinates coordinates;

    public static class InputButton extends Rectangle
    {
        public InputButton(String value)
        {
            this.value = value;
            width = 50; height = 50;
        }
        public String value;
        public InputButton(String value,Rectangle bounds)
        {
            this.value = value;
            width = bounds.width; height = bounds.height;
            this.x = bounds.x; this.y = bounds.y;
        }
    }
    public static class PurchaseButton extends Rectangle{
        public PurchaseButton(int id)
        {
            itemId = id;
        }
        public int itemId;
    }

    public static HashMap<Integer,Minigame> repo = new HashMap<>();

    static
    {
        String pathToJsonWithItems = "json/minigames.json";
        readMinigamesFromJsonFile(pathToJsonWithItems);
    }

    private static void readMinigamesFromJsonFile(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Получаем InputStream из ресурсов с использованием ClassLoader'а
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(filePath);
            // Чтение данных из JSON файла и преобразование их в список объектов Minigame
            Minigame[] minigames = objectMapper.readValue(inputStream, Minigame[].class);
            for (Minigame minigame : minigames) {
                repo.put(minigame.id,minigame);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Minigame get(int i)
    {
        return new Minigame(repo.get(i));
    }

    public Minigame(Minigame minigame) {

        this.id = minigame.id;
        this.script = minigame.script;
        this.input = minigame.input;
        this.window = minigame.window;
        this.success = minigame.success;
        this.exit = minigame.exit;
        this.inputButtons = minigame.inputButtons;
        this.coordinates = minigame.coordinates;
        this.type = minigame.type;

        switch (type)
        {
            case SHQUR :

                if(Math.random() < 0.8 && id!=16)
                {
                    success = new Rectangle(window.x,window.y,0,0);
                    break;
                }
                int x = (int)(Math.random() * 300) + window.x;
                int y = (int)(Math.random() * 200) + window.y;
                success = new Rectangle(x,y,16,8);
                break;

            case DEATH:
            case SINK:
            case MOM_CLEAN:
            case COOK:
            case GAS:
                success = new Rectangle(window.x + 200,window.y+150,40,40);
                break;

            case PADIQUE:
                inputButtons = new ArrayList<>();
                success = new Rectangle((int)window.getMaxX()-50,(int)window.getMaxY()-50,50,50);
                for (int i = 0; i < 10; i++) {
                    Minigame.InputButton button = new Minigame.InputButton("" + i);
                    button.x = window.x + i%3*50; button.y = window.y + i/3 * 50;
                    inputButtons.add(button);
                }
                break;

            case BUS:

                inputButtons = new ArrayList<>();

                inputButtons.add(new InputButton("1_дом", new Rectangle(window.x+20,window.y+30,30,30)));
                inputButtons.add(new InputButton("2_магазин", new Rectangle(window.x + 150,window.y+50,30,30)));
                inputButtons.add(new InputButton("3_аптека", new Rectangle(window.x+150,window.y+120,30,30)));
                inputButtons.add(new InputButton("4_рынок", new Rectangle(window.x+25,window.y+100,30,30)));
                inputButtons.add(new InputButton("5_школа", new Rectangle(window.x+150,window.y+150,30,30)));
                inputButtons.add(new InputButton("6_больница", new Rectangle(window.x+250,window.y+120,30,30)));
                inputButtons.add(new InputButton("7_метро", new Rectangle(window.x+25,window.y+150,30,30)));

                break;

            case SLEEP:

                inputButtons = new ArrayList<>();
                inputButtons.add(new InputButton("2_ч",new Rectangle(window.x + 50, window.y + 150,50,50)));
                inputButtons.add(new InputButton("4_ч",new Rectangle(window.x + 100, window.y + 150,50,50)));
                inputButtons.add(new InputButton("8_ч",new Rectangle(window.x + 150, window.y + 150,50,50)));
                inputButtons.add(new InputButton("12_ч",new Rectangle(window.x + 200, window.y + 150,50,50)));

                break;

            case TOILET:
                inputButtons = new ArrayList<>();
                inputButtons.add(new InputButton("ссать",new Rectangle(window.x+50,window.y+50,50,50)));
                inputButtons.add(new InputButton("срать",new Rectangle(window.x+120,window.y+50,50,50)));

                break;

            case ELEVATOR:
                inputButtons = new ArrayList<>();
                for (int i = 9; i > 0; i--) {
                    Minigame.InputButton button = new Minigame.InputButton("" + (i));
                    button.x = window.x + (9-i)%2*50; button.y = window.y + (9-i)   /2 * 50;
                    inputButtons.add(button);
                }
                break;

        }
    }

    @Override
    public String toString() {
        return "Minigame{" +
                "id=" + id +
                ", type=" + type +
                ", script=" + script +
                ", input='" + input + '\'' +
                ", window=" + window +
                ", success=" + success +
                ", exit=" + exit +
                ", buttons=" + inputButtons +
                ", coordinates=" + coordinates +
                '}';
    }
}

