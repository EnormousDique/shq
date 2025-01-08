package ru.muwa.shq.entities;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.muwa.shq.Main;
import ru.muwa.shq.engine.Game;
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
        SHQUR, PADIQUE, MOM_CLEAN, DOOR, ELEVATOR,COOK,SHOP,BUS,SLEEP,SINK,TOILET,DEATH,GAS,SELL,INTERNET,WAIT
    }
    public int script;
    public String input = ""; // для padique и elevator
    public Rectangle window = new Rectangle(GameWindow.WIDTH / 3 - 75, GameWindow.HEIGHT / 5, 600,400);
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
        public String value, hiddenValue;
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
                    var buttonC = new InputButton("c");
                    buttonC.x = window.x + 100;
                    buttonC.y = window.y + 150;
                    inputButtons.add(buttonC);

                break;

            case BUS:

                inputButtons = new ArrayList<>();

                inputButtons.add(new InputButton("1_дом", new Rectangle(window.x+50,window.y+60,30,30)));
                inputButtons.add(new InputButton("2_магазин", new Rectangle(window.x + 320,window.y+80,30,30)));
                inputButtons.add(new InputButton("3_аптека", new Rectangle(window.x+310,window.y+250,30,30)));
                inputButtons.add(new InputButton("4_рынок", new Rectangle(window.x+150,window.y+200,30,30)));
                inputButtons.add(new InputButton("5_школа", new Rectangle(window.x+360,window.y+320,30,30)));
                inputButtons.add(new InputButton("6_больница", new Rectangle(window.x+500,window.y+280,30,30)));
                inputButtons.add(new InputButton("7_автовокзал", new Rectangle(window.x+50,window.y+320,30,30)));

                break;

            case SLEEP:

                inputButtons = new ArrayList<>();
                inputButtons.add(new InputButton("2_ч",new Rectangle(window.x + 50, window.y + 150,50,50)));
                inputButtons.add(new InputButton("4_ч",new Rectangle(window.x + 100, window.y + 150,50,50)));
                inputButtons.add(new InputButton("8_ч",new Rectangle(window.x + 150, window.y + 150,50,50)));
                inputButtons.add(new InputButton("12_ч",new Rectangle(window.x + 200, window.y + 150,50,50)));
                break;

            case WAIT:
                inputButtons = new ArrayList<>();
                inputButtons.add(new InputButton("1_ч",new Rectangle(window.x + 50, window.y + 150,50,50)));
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

            case INTERNET:
                inputButtons = new ArrayList<>();
                if(this.id ==19) { // Интернет. Заглавная страница
                    for (int i = 0; i < 4; i++) {
                        Minigame.InputButton b = new InputButton("<---");
                        for (int j = 0; j < i; j++) b.value += " ";
                        b.x = window.x + 200;
                        b.y = window.y + 145 + i * 35;
                        b.height = 14;
                        inputButtons.add(b);
                    }
                }
                if(this.id == 20){
                    // Интернет. Сайт с врачом
                    if(Game.player.quests.stream()
                            .noneMatch(q->q.id==20 && q.completed)){
                    Minigame.InputButton b = new InputButton("Оплатить");
                    b.x = window.x + (window.width/2);
                    b.y = window.y + (window.y - 50);
                    b.height=25;
                    inputButtons.add(b);}
                    else {
                        Minigame.InputButton b = new InputButton("Операция оплачена. Ждем вас в израиле");
                        b.x = window.x + (window.width/2);
                        b.y = window.y + (window.y - 50);
                        b.height=25;
                        inputButtons.add(b);
                    }
                }
                if(this.id == 21){// Интернет. Сайт валберс
                    purchaseButtons = new ArrayList<>();
                    PurchaseButton pb = new PurchaseButton(113);
                    pb.x = 0/4 * 75 + window.x;
                    pb.y = 0%4 * 50 + window.y;
                    pb.width = 50; pb.height = 45;
                    purchaseButtons.add(pb);

                    pb = new PurchaseButton(109);
                    pb.x = 1/4 * 75 + window.x;
                    pb.y = 1%4 * 50 + window.y;
                    pb.width = 50; pb.height = 45;
                    purchaseButtons.add(pb);

                    pb = new PurchaseButton(100);
                    pb.x = 2/4 * 75 + window.x;
                    pb.y = 2%4 * 50 + window.y;
                    pb.width = 50; pb.height = 45;
                    purchaseButtons.add(pb);

                    pb = new PurchaseButton(98);
                    pb.x = 3/4 * 75 + window.x;
                    pb.y = 3%4 * 50 + window.y;
                    pb.width = 50; pb.height = 45;
                    purchaseButtons.add(pb);

                    //Стиралка
                    pb = new PurchaseButton(128);
                    pb.x = 0/4 * 75 + window.x + 70;
                    pb.y = 3%4 * 50 + window.y;
                    pb.width = 50; pb.height = 45;
                    purchaseButtons.add(pb);

                    //Колеске
                    pb = new PurchaseButton(83);
                    pb.x = 2/4 * 75 + window.x +70;
                    pb.y = 3%4 * 50 + window.y;
                    pb.width = 50; pb.height = 45;
                    purchaseButtons.add(pb);


                }

                if(this.id == 22){
                    //Сайт с квартирой
                    if(!Game.player.rent) {
                        Minigame.InputButton b = new InputButton("Снять");
                        b.x = window.x + (window.width / 2);
                        b.y = window.y + (window.y - 50);
                        b.height = 25;
                        inputButtons.add(b);
                    }else {
                        Minigame.InputButton b = new InputButton("Квартира сдана!");
                        b.x = window.x + (window.width / 2);
                        b.y = window.y + (window.y - 50);
                        b.height = 25;
                        inputButtons.add(b);
                    }
                }

                //Кнопка выключения компьютера
                Minigame.InputButton b = new InputButton("x");
                b.x = window.x + window.width - b.width;
                b.y = window.y;
                inputButtons.add(b);
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

