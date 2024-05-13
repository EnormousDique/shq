package ru.muwa.shq.engine;

import ru.muwa.shq.engine.utils.*;
import ru.muwa.shq.entities.GameObject;
import ru.muwa.shq.entities.Item;
import ru.muwa.shq.entities.Minigame;
import ru.muwa.shq.story.Dialogue;
import ru.muwa.shq.story.Quest;
import ru.muwa.shq.textures.ItemTextures;
import ru.muwa.shq.textures.MinigameTextures;
import ru.muwa.shq.textures.ObjectTextures;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import static ru.muwa.shq.entities.GameObject.Type.*;
import static ru.muwa.shq.entities.Item.Type.FIREARM;
import static ru.muwa.shq.entities.Minigame.Type.COOK;
import static ru.muwa.shq.entities.Minigame.Type.SHOP;
import static ru.muwa.shq.textures.ObjectTextures.PLAYER;

public class Renderer implements Runnable{
    Thread thread;
    public GameWindow window;
    public Screen screen;
    public static boolean itemVisible, questVisible;
    public static Quest renderingQuest = null;
    public static String renderingBoss = "";
    public static boolean showDescriptions;
    public static int hud = 0;
    public static boolean smokedRendering = false;

    public static boolean drawZones = false, drawWalls = false;

    public static void switchHud() {
        switch (hud)
        {
            case 0 -> hud = 1;
            case 1 -> hud = 2;
            case 2 -> hud = 3;
            case 3 -> hud = 0;
        }
    }

    public static class BossIcon extends Rectangle{ public String bossName; public BossIcon(String bossName) {this.bossName=bossName; width = 50; height = 50;}}
    public static List<BossIcon> bossIcons = new ArrayList<>();



    Renderer() {

        window = new GameWindow();
        screen = new Screen();
        if(GameWindow.fullscreen) window.setLocation(0,0);
        window.add(screen);
        screen.setBounds(0,0,GameWindow.WIDTH,GameWindow.HEIGHT);
        window.addKeyListener(Input.keyboard);
        window.addMouseListener(Input.mouse);
        window.addMouseMotionListener(Input.mouse);
        thread = new Thread(this);

    }

    @Override
    public void run() {
        System.out.println("поток отрисовки запущен");

        double drawInterval = (double) 1_000_000_000 / Game.renderFPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currTime;

        while (thread != null) {

            currTime = System.nanoTime();
            delta += (currTime - lastTime) / drawInterval;
            lastTime = currTime;
            if (delta >= 1) {
                work();
                delta =-1;
            }else{
                long remainingTime = (long) ((1 - delta) * drawInterval / 1_000_000); // переводим в миллисекунды
                try {
                    Thread.sleep(remainingTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    private void work()
    {
        try {
            screen.repaint();
        }catch (Exception e){}
    }


    public static class Screen extends JPanel
    {

        public Screen()
        {
        }

        BufferedImage offScreenBuffer;

        @Override
        public void paint(Graphics g) {


            if (offScreenBuffer == null) {
                offScreenBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            }
            Graphics offScreenGraphics = offScreenBuffer.getGraphics();
            // Выбираем шрифт, поддерживающий русский язык
            Font font = new Font("Arial", Font.PLAIN, 15); // Или другой шрифт по вашему выбору
            offScreenGraphics.setFont(font);


            //Отрисовка
            Graphics2D g2 = (Graphics2D)g;
            fillBG(offScreenGraphics);
            drawObjects(offScreenGraphics);
            drawPlayer(offScreenGraphics);
            drawWeather(offScreenGraphics);
            drawHud(offScreenGraphics);
            drawQuestJournal(offScreenGraphics);
            drawMinigame(offScreenGraphics);
            drawAnimations(offScreenGraphics);
            if(showDescriptions) drawDescriptions(offScreenGraphics);
            if(drawZones) drawZones(offScreenGraphics);

            g2.drawImage(offScreenBuffer,0,0,null);

            //Высвобождение ресурсов
             g2.dispose();
             offScreenGraphics.dispose();

             try{ Thread.sleep(0,1);} catch(Exception e) {}

        }

        private void drawAnimations(Graphics offScreenGraphics) {
        }


        private void drawWeather(Graphics g) {
            if(!Game.currentLevel.isStreet) return;

            Rectangle nightFilter = new Rectangle(0,0,GameWindow.WIDTH,GameWindow.HEIGHT);
            boolean drawing = false;

            switch (GameTime.getTimeOfTheDay()) {

                case SUNSET ->  {
                    g.setColor(new Color(10, 10, 10, 100));
                    drawing = true;
                }
                case NIGHT ->  {
                    g.setColor(new Color(10, 10, 10, 200));
                    drawing = true;
                }
                case SUNRISE ->  {
                    g.setColor(new Color(100, 30, 30, 70));
                    drawing = true;
                }
            }
            if(drawing)
                g.fillRect(nightFilter.x,nightFilter.y,nightFilter.width,nightFilter.height);

        }

        private void drawZones(Graphics g) {
            List<GameObject> zones = Game.currentLevel.objects.stream().filter(o -> o.type == ZONE).toList();

            for (GameObject z : zones)
            {
                g.setColor(new Color(150,0,120,100));
                g.fillRect(z.x-Camera.x,z.y-Camera.y,z.hitBox.width,z.hitBox.height);
                g.setColor(Color.CYAN);
                g.drawString(z.name,z.x-Camera.x,z.y-Camera.y+10);
            }
        }

        private void drawPlayer(Graphics g)
        {
            AffineTransform at = new AffineTransform();

            at.translate(Game.player.x-Camera.x,Game.player.y-Camera.y-10);
            at.rotate(Combat.angle,10,20);
            ((Graphics2D)g).setTransform(at);
            if(!Animation.current.containsKey(Game.player)) g.drawImage(ObjectTextures.repo.get(PLAYER),0,0,null );
            else g.drawImage(Animation.getCurrentFrame(Game.player),0,0,null);
            ((Graphics2D)g).setTransform(new AffineTransform());
        }

        private void drawMinigame(Graphics g) {

            g.setFont(new Font(Font.SANS_SERIF,1,15));

            if(Minigame.current == null) return;
            g.setColor(new Color(250,250,0,150));
            g.fillRect(Minigame.current.window.x-20,Minigame.current.window.y-20,Minigame.current.window.width+40,Minigame.current.window.height+40);
            g.drawImage(MinigameTextures.repo.get(Minigame.current.id), Minigame.current.window.x,Minigame.current.window.y,Minigame.current.window.width,Minigame.current.window.height,null);

            switch (Minigame.current.type)
            {
                case SHQUR :
                    g.setColor(Color.blue);
                    g.fillRect(Minigame.current.success.x,Minigame.current.success.y,Minigame.current.success.width,Minigame.current.success.height);
                    break;

                case GAS:
                case SINK:
                case MOM_CLEAN:
                case COOK:
                case DEATH:
                    g.setColor(Color.green);
                    g.fillRect(Minigame.current.success.x,Minigame.current.success.y,Minigame.current.success.width,Minigame.current.success.height);
                    break;

                case SHOP:
                    g.setColor(new Color(100,100,100,100));
                    g.fillRect(Minigame.current.window.x,Minigame.current.window.y,Minigame.current.window.width,Minigame.current.window.height);
                    GameObject shop = null;
                    for (int i = 0; i < Game.currentLevel.objects.size(); i++) {
                        GameObject o = Game.currentLevel.objects.get(i);
                        if(o.opened && o.minigame.type.equals(SHOP)) shop = o;
                    }
                    if(shop == null) return;
                    Minigame.current.purchaseButtons = new ArrayList<>();
                    for (Item item : shop.items)
                    {
                        Minigame.current.purchaseButtons.add(new Minigame.PurchaseButton(item.id));
                    }
                    for (int i = 0; i < Minigame.current.purchaseButtons.size(); i++) {
                        Minigame.PurchaseButton button = Minigame.current.purchaseButtons.get(i);
                        button.x = i/4 * 75 + Minigame.current.window.x;
                        button.y = i%4 * 50 + Minigame.current.window.y;
                        button.width = 50; button.height = 45;
                        g.drawImage(ItemTextures.repo.get(button.itemId),button.x,button.y,null);
                        g.setColor(Color.GREEN);
                        g.setFont(new Font(Font.SANS_SERIF,1,15));
                        g.drawString("" + Item.get(button.itemId).price, button.x + 50, (int) button.getCenterY());
                    }
                    break;

                case PADIQUE:

                    g.setColor(new Color(100,100,100,100));
                    g.fillRect(Minigame.current.window.x,Minigame.current.window.y,Minigame.current.window.width,Minigame.current.window.height);

                    for (int i = 0; i < Minigame.current.inputButtons.size(); i++) {
                        Minigame.InputButton button = Minigame.current.inputButtons.get(i);
                        g.setColor(Color.WHITE);
                        g.fillRect(button.x, button.y, button.width, button.height);
                        g.setColor(Color.BLACK);
                        g.drawString(button.value, (int) button.getCenterX(), (int) button.getCenterY());
                    }

                    g.setColor(Color.green);
                    g.fillRect(Minigame.current.success.x,Minigame.current.success.y,Minigame.current.success.width,Minigame.current.success.height);
                    g.drawString(Minigame.current.input, (int) (Minigame.current.window.getMaxX()-50),Minigame.current.window.y+20);
                    break;

                case TOILET:
                case BUS:
                case ELEVATOR:

                    for(Minigame.InputButton button : Minigame.current.inputButtons)
                    {
                        g.setColor(new Color(0,255,150,125));
                        g.fillRect(button.x,button.y, button.width, button.height);
                        g.setColor(Color.BLACK);
                        g.drawString(button.value, button.x + 5, button.y+15);
                    }
                    break;
                case SLEEP:
                    for (Minigame.InputButton button : Minigame.current.inputButtons )
                    {
                        g.setColor(button.value.equals(Minigame.current.input)? new Color(0,100,100,100) : Color.GRAY);
                        g.fillRect(button.x,button.y, button.width,button.height);
                        g.setColor(Color.RED);
                        g.drawString(button.value,button.x + 10, (int)button.getCenterY());

                        if (Minigame.current.input == null) break;

                        int chosenHours = -1;
                        if(Minigame.current.input.startsWith("12")) chosenHours = 12;
                        if(Minigame.current.input.startsWith("8")) chosenHours = 8;
                        if(Minigame.current.input.startsWith("4")) chosenHours = 4;
                        if(Minigame.current.input.startsWith("2")) chosenHours = 2;

                        //Показываем ли кнопку "спать"
                        //Показываем если кол-во выбранных часов для сна
                        boolean showSuccess = chosenHours * 7 < Game.player.sleepy && chosenHours !=-1;

                        if (showSuccess)
                        {
                            Minigame.current.success = new Rectangle(Minigame.current.window.x+250,Minigame.current.window.y+50,50,50);
                            g.setColor(Color.GREEN);
                            g.fillRect(Minigame.current.success.x,Minigame.current.success.y,50,50);
                            g.setColor(Color.MAGENTA);
                            g.drawString("СПАТЬ " + chosenHours + " ч.",Minigame.current.success.x + 10,Minigame.current.success.y+20);
                        }
                        else Minigame.current.success = null;

                    }
                    break;

            }
        }


        private  void fillBG(Graphics g)
        {
            if(smokedRendering) return;
            g.setColor(Color.BLACK);
            g.fillRect(0,0,GameWindow.WIDTH,GameWindow.HEIGHT);
        }

        private void drawObjects(Graphics g) {

            Rectangle camera = new Rectangle(Camera.x,Camera.y,GameWindow.WIDTH,GameWindow.HEIGHT);

            for (int i = 0; i < Game.currentLevel.objects.size(); i++)
            {

                GameObject o = Game.currentLevel.objects.get(i);

                if(camera.intersects(new Rectangle(o.hitBox.x - o.hitBoxXOffset, o.hitBox.y-o.hitBoxYOffset,o.hitBox.width+o.hitBoxXOffset,o.hitBox.height+o.hitBoxYOffset)))
                {
                    if(Game.player.crazy > 33 && Math.random()<0.01) continue; // слабые глюки
                    if(Game.player.crazy > 70 && Math.random()<0.025) continue; // средние глюки
                    if(smokedRendering && o.name.contains("bg")) continue; //глюки от травки

                    if(o == Game.player) continue;

                    if(o.type == BUILDING && Game.player.y < o.hitBox.y)
                    {
                        g.drawImage(ObjectTextures.transparentRepo.get(o.id), o.x - Camera.x, o.y - Camera.y, null);
                    }else
                    {
                        //Если объект имеет активную анимацию, не рисуем его стандартную текстуру
                        if(Animation.current.containsKey(o)) {
                            //Получаем текущий кадр анимации для объекта и рисуем его
                            g.drawImage(Animation.getCurrentFrame(o), o.x -Camera.x, o.y - Camera.y, null);
                            continue;
                        }else //Иначе рисуем стандартную текстуру
                            g.drawImage(ObjectTextures.repo.get(o.id), o.x - Camera.x, o.y - Camera.y, null);
                        //Для зон входа рисуем области
                        if((o.name.contains("enter")||o.name.contains("exit"))&&o.type == ZONE)
                        {
                            Color oldColor = g.getColor();
                            g.setColor(new Color(0,255,150,25));
                            g.fillRect(o.hitBox.x-Camera.x,o.hitBox.y-Camera.y,o.hitBox.width,o.hitBox.height);
                            g.setColor(oldColor);
                        }
                    }
                    if(!drawWalls) continue;
                    g.setColor(Color.red);
                    g.drawRect(o.x - Camera.x,o.y - Camera.y,o.hitBox.width,o.hitBox.height);
                }
            }
        }

        private void drawHud(Graphics g) {


            if(Dialogue.current != null)
            {
                drawDialogue(g);
            }

            g.setColor(Color.WHITE);

            //Рисуем время
            g.drawString(GameTime.getString(), GameWindow.WIDTH - 150, 10);

            //Рисуем статы игрока
            if(hud > 0){
                //ХП
                g.drawString(" \u2764 : " + Game.player.hp + " / " + Game.player.baseHp , GameWindow.WIDTH -150, 30 );
                g.drawString(" KINK : " + Camera.kink, GameWindow.WIDTH -150, 50 );
                //БРОНЯ
                //g.drawString(" \uD83D\uDEE1 : " + Game.player.bonusHp, GameWindow.WIDTH-150, 50);
                //ДЫХАЛКА
                String staminaStr = Double.toString(Game.player.stamina).substring(0,3);
                g.drawString("дыхалка : " + staminaStr + "/"+(Game.player.baseStamina+Game.player.bonusStamina), GameWindow.WIDTH-160,320);
                //Рисуем активные эффекты (при наличии)
                drawEffects(g);
                //Уровни шухера
                if(Game.currentLevel.isIndoors) {
                    g.drawString("шухер : " + Double.toString(Game.currentLevel.noise).substring(0,3) + "/100",GameWindow.WIDTH-180,360);
                }
                //и беспредела
                g.drawString(" беспредел : " + Double.toString(Game.player.wanted).substring(0,3) + "/100",GameWindow.WIDTH-180,340);
                //Бабки $$$$$
                g.drawString("Руб : " + Game.player.money,GameWindow.WIDTH-150, 70);
                if(hud>1){
                    //И статы мамы
                    g.drawString("Покорми маму : " + GameTime.longToString(Game.player.mommaFullness), GameWindow.WIDTH-200, 100);
                    g.drawString("Дай лекарства маме : " + GameTime.longToString(Game.player.mommaHealth), GameWindow.WIDTH-230, 120);
                    g.drawString("Помой маму : " + GameTime.longToString(Game.player.mommaClean), GameWindow.WIDTH-200, 140);
                 if(hud>2){
                     g.drawString("говно : " + Double.toString(Game.player.poo).substring(0,3) + "/100", GameWindow.WIDTH-150,200);
                     g.drawString("моча : " + Double.toString(Game.player.pee).substring(0,3) + "/100", GameWindow.WIDTH-150,220);
                     g.drawString("голод : " + Double.toString(Game.player.hunger).substring(0,3) + "/100", GameWindow.WIDTH-150,240);
                     g.drawString("жажда : " + Double.toString(Game.player.thirst).substring(0,3) + "/100", GameWindow.WIDTH-150,260);
                     g.drawString("сонливость : " + Double.toString(Game.player.sleepy).substring(0,3) + "/100", GameWindow.WIDTH-170,280);
                     String crazyStr = Double.toString(Game.player.crazy).substring(0,3);
                     g.drawString("псих : " + crazyStr + "/100", GameWindow.WIDTH-150,300);
                 }
                }
            }

            //координаты мыши
            g.drawString("x:"+ (Input.mouse.x+Camera.x) + " y:"+ (Input.mouse.y+Camera.y-30),GameWindow.WIDTH-100, 160);
            g.drawString("x:"+ Input.mouse.x + " y:"+ (Input.mouse.y-30),GameWindow.WIDTH-100, 180);

            //Предмет в руках
            if(Game.player.equip!=null)
                g.drawString("в руках : " + Game.player.equip.name + (Game.player.equip.type==FIREARM?".  пули : " + Game.player.equip.ammo + "/"+Game.player.equip.maxAmmo : ""),120,20);

            //Рисуем окна открытых контейнеров.
            for (int i = 0; i < Game.currentLevel.objects.size(); i++) {
                GameObject o = Game.currentLevel.objects.get(i);
                if(o.opened && (o.type.equals(CONTAINER) || o.minigame.type == COOK))
                {
                    int cx = Camera.x, cy = Camera.y;
                    g.setColor(Color.WHITE);
                    g.drawImage(ItemTextures.containerBG,o.x-cx,o.y-150-cy,null);

                    for (int j = 0; j < o.items.size(); j++) {

                        Item item = o.items.get(j);
                        item.icon.x = o.x + j/2 * 50 - cx;
                        item.icon.y = (o.y-150) + j%2 * 50 - cy;
                        item.icon.width = 50; item.icon.height = 50;
                        g.drawImage(ItemTextures.repo.get(item.id),item.icon.x,item.icon.y,null);
                        if(item.stackable) {
                            g.setColor(Color.CYAN);
                            g.drawString("" + item.count,item.icon.x+15,item.icon.y+15);
                        }
                    }
                }
            }


            if(!itemVisible) return;

            //Отрисовка окна инвентаря
            g.drawImage(ItemTextures.inventory,0,0,null);

            //Отрисовка иконок предметов инвентаря
            for (int i = 0; i < Game.player.items.size(); i++) {

                Item item = Game.player.items.get(i);

                item.icon = new Rectangle(i%2*50,i/2*50,50,50);
                g.drawImage(ItemTextures.repo.get(item.id),item.icon.x,item.icon.y,null);
                g.setColor(Color.YELLOW); g.drawRect(item.icon.x,item.icon.y,item.icon.width,item.icon.height);

                if(item.stackable) {

                    g.setColor(Color.CYAN);
                    g.drawString("" + item.count,  item.icon.x+15,  item.icon.y+15);
                }

                if(item == Game.player.equip || item == Game.player.hat || item == Game.player.torso || item == Game.player.foot)
                {
                    g.setColor(new Color(0,150,0,100));
                    g.fillRect(item.icon.x, item.icon.y,item.icon.width-20,item.icon.height-20);
                }
            }

        }

        private void drawEffects(Graphics g) {
            int row = 0;
            if(!Game.player.effects.isEmpty()) {
                g.drawString("активные эффекты : ",120,60);
                for (int i = 0; i < Game.player.effects.size(); i++) {
                    row = i;
                    Item.Effect effect = Game.player.effects.get(i);
                    g.drawString(effect.name, 120, 80+ row*20 );
                }
            }
            row++;
            if(Game.player.hunger>25) {g.drawString("Проголодался", 120, 80+ row*20);row++;}
            if(Game.player.hunger>50) {g.drawString("Голоден", 120, 80+ row*20);row++;}
            if(Game.player.hunger>98) {g.drawString("Смертельно голоден", 120, 80+ row*20);row++;}
            if(Game.player.thirst>50) {g.drawString("Сушняк", 120, 80+ row*20);row++;}
            if(Game.player.thirst>95) {g.drawString("Обезвоживание", 120, 80+ row*20);row++;}
            if(Game.player.stimulate>0) {g.drawString("Бодрит", 120, 80+ row*20);row++;}
            if(Game.player.stimulate>25) {g.drawString("Ускорился", 120, 80+ row*20);row++;}
            if(Game.player.stimulate>50) {g.drawString("Обнюхан", 120, 80+ row*20);row++;}
            if(Game.player.stimulate>75) {g.drawString("Перенюхал", 120, 80+ row*20);row++;}
            if(Game.player.smoke>0) {g.drawString("Подкурен", 120, 80+ row*20);row++;}
            if(Game.player.smoke>25) {g.drawString("Накурился", 120, 80+ row*20);row++;}
            if(Game.player.smoke>70) {g.drawString("Перекурил", 120, 80+ row*20);row++;}
            if(Game.player.drunk>0) {g.drawString("Пьяненький", 120, 80+ row*20);row++;}
            if(Game.player.drunk>15) {g.drawString("Пьяный", 120, 80+ row*20);row++;}










        }

        private void drawDescriptions(Graphics g) {
            //Если навели на инвентарь
            if (itemVisible)
            {
                for (int i = 0; i < Game.player.items.size(); i++) {
                    Item item = Game.player.items.get(i);
                    if(item.icon.contains(Input.mouse.x, Input.mouse.y-30))
                        g.drawString(item.name + " | " + item.description,Input.mouse.x, Input.mouse.y+30);

                }
            }
            //если навели на контейнер
            for (int i = 0; i < Game.currentLevel.objects.size(); i++) {
                GameObject obj = Game.currentLevel.objects.get(i);
                if(obj != null && obj.opened && !obj.items.isEmpty())
                {
                    for (int j = 0; i < obj.items.size(); i++) {
                        Item item = obj.items.get(j);
                        if(item.icon.contains(Input.mouse.x, Input.mouse.y-30))
                            g.drawString(item.name + " | " + item.description,Input.mouse.x, Input.mouse.y+30);
                    }
                }

            }
            //магазин
            if(Minigame.current != null && Minigame.current.type == SHOP)
            {
                for (int i = 0; i < Minigame.current.purchaseButtons.size(); i++) {
                    Minigame.PurchaseButton button = Minigame.current.purchaseButtons.get(i);
                    Item item = Item.get(button.itemId);
                    if(button.contains(Input.mouse.x, Input.mouse.y-30))
                        g.drawString(item.name + " | " + item.description,Input.mouse.x + 50, Input.mouse.y+50);

                }
            }
        }

        private void drawDialogue(Graphics g) {
            //ИНИЦИАЛИЗАЦИЯ
            g.setColor(Color.WHITE);
            g.fillRect(GameWindow.WIDTH / 3-200, GameWindow.HEIGHT - 300, GameWindow.WIDTH / 5 * 2 + 200 , 300);
            g.setColor(new Color(50,100,250,250));
            g.setFont(new Font(Font.SANS_SERIF,1,15));

            //ОТРИСОВКА ПОРТРЕРА
            g.drawImage(Dialogue.texture,GameWindow.WIDTH/3-200,GameWindow.HEIGHT-300,200,300,null);

            for(int i = 0;i< Dialogue.current.message.split("\n").length ; i++)
            {//ОТРИСОВКА СООБЩЕНИЯ
                g.drawString(Dialogue.current.message.split("\n")[i],GameWindow.WIDTH /3 + 20, GameWindow.HEIGHT - 300 + 20 + i * 18);
            }
            Dialogue.buttons = new ArrayList<>();
            for (int i = 0; i < Dialogue.current.responses.size(); i++)
            {//ИНИЦИАЛИЗАЦИЯ КНОПОК ОТВЕТОВ

                Dialogue.Response response = Dialogue.current.responses.get(i);
                Dialogue.ResponseButton button = new Dialogue.ResponseButton(response);
                int x = 0; for (Dialogue.ResponseButton b : Dialogue.buttons) x += b.text.length() * 9;
                button.x = (GameWindow.WIDTH / 3) + 10 + (Dialogue.current.responses.indexOf(response)==0? 0 : x);
                button.y = (GameWindow.HEIGHT - 50);
                button.width = button.text.length() * 8; button.height = 35;
                Dialogue.buttons.add(button);
            }
             for(Dialogue.ResponseButton button : Dialogue.buttons)
            {
                //ОТриСОВКА КНОПОК ОТВЕТОВ
                g.setColor(Color.GREEN);
                g.fillRect(button.x,button.y,button.width,button.height);
                g.setColor(Color.BLACK);
                g.setFont(new Font(Font.SANS_SERIF,1,15));
                g.drawString(button.text,button.x , button.y + 15);
            }


        }

        private void drawQuestJournal(Graphics g) {
            if(!questVisible) return;

            //Рисуем само окно
            g.setColor(new Color(250,250,250,100));
            g.fillRect(GameWindow.WIDTH/6,GameWindow.HEIGHT/6,GameWindow.WIDTH/3 *2,GameWindow.HEIGHT/3*2);

            //Собираем квесты по боссам
            bossIcons = new ArrayList<>();
            for (int i = 0; i < Game.player.quests.size(); i++) {

                var quest = Game.player.quests.get(i);
                if(bossIcons.stream().noneMatch(icon -> icon.bossName.equals(quest.owner)))
                {
                    BossIcon bIcon = new BossIcon(quest.owner);
                    bossIcons.add(bIcon);
                    bIcon.x = 350 + ((bossIcons.indexOf(bIcon) / 4) * 50) + 10;
                    bIcon.y = 150 + 50 * (bossIcons.indexOf(bIcon) % 4) + 10;
                }
            }

            if(renderingBoss.isEmpty() && renderingQuest == null)
            {
                for(BossIcon icon : bossIcons)
                {
                    g.setColor(Color.blue);
                    g.fillRect(icon.x,icon.y,icon.width,icon.height);
                    g.setColor(Color.PINK);
                    g.drawString(icon.bossName,icon.x+5, icon.y+20);
                }
            }
            if(!renderingBoss.isEmpty() && renderingQuest == null)
            {
                int skipped = 0;
                for (int i = 0; i < Game.player.quests.size(); i++) {
                    Quest quest = Game.player.quests.get(i);
                    if(!quest.owner.equals(renderingBoss)){ skipped++; continue;}
                    quest.icon.x = GameWindow.WIDTH/6 + 20;
                    quest.icon.y = GameWindow.HEIGHT/6 + 40 + (i-skipped)* 40;
                    g.setColor(quest.completed? Color.GREEN : Color.YELLOW);
                    g.fillRect(quest.icon.x,quest.icon.y,quest.icon.width,quest.icon.height);
                    g.setColor(Color.BLACK);
                    g.setFont(new Font(Font.SANS_SERIF,1,15));
                    g.drawString(quest.title, quest.icon.x + 5, quest.icon.y + 20);
                }
            }

            if(renderingQuest != null && renderingBoss.isEmpty())
            {
                g.setColor(Color.BLACK);
                g.setFont(new Font(Font.SANS_SERIF,1,15));
                g.drawString(renderingQuest.owner + " просит " + renderingQuest.title,GameWindow.WIDTH/6 + 10,GameWindow.HEIGHT/6 + 20 );
                for(int i = 0;i< renderingQuest.description.split("\n").length ; i ++)
                {
                    String s = renderingQuest.description.split("\n")[i];
                    g.drawString(s,GameWindow.WIDTH/6 + 250,GameWindow.HEIGHT/6 + 40 + i * 20 );
                }
            }

        }
    }
}
