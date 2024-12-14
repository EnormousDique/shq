package ru.muwa.shq.engine;

import ru.muwa.shq.engine.utils.*;
import ru.muwa.shq.entities.GameObject;
import ru.muwa.shq.entities.Item;
import ru.muwa.shq.entities.Minigame;
import ru.muwa.shq.story.Dialogue;
import ru.muwa.shq.story.Quest;
import ru.muwa.shq.story.scripts.Script;
import ru.muwa.shq.textures.ItemTextures;
import ru.muwa.shq.textures.MinigameTextures;
import ru.muwa.shq.textures.ObjectTextures;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import static ru.muwa.shq.engine.utils.GameTime.*;
import static ru.muwa.shq.entities.GameObject.Type.*;
import static ru.muwa.shq.entities.Item.Type.*;
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

    public static class BossIcon extends Rectangle{ public String bossName; public BossIcon(String bossName) {this.bossName=bossName; width = 75; height = 75;}}
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
                delta =0;//delta-=1;
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
            setDoubleBuffered(true);
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
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD,15));

            fillBG(offScreenGraphics);

            drawObjects(offScreenGraphics);
            drawPlayer(offScreenGraphics);
            drawAnimations(offScreenGraphics);
            drawBuilding(offScreenGraphics);
            drawWeather(offScreenGraphics);

            drawMinigame(offScreenGraphics);
            drawHud(offScreenGraphics);

            drawQuestJournal(offScreenGraphics);
            drawDescriptions(offScreenGraphics);


            if(drawZones) drawZones(offScreenGraphics);

            g2.drawImage(offScreenBuffer,0,0,null);

            //Высвобождение ресурсов
             g2.dispose();
             offScreenGraphics.dispose();

           //  try{ Thread.sleep(0,1);} catch(Exception e) {}

        }

        private void drawBuilding(Graphics g) {
            for (int i = 0; i < Game.currentLevel.objects.size(); i++) {
                var building = Game.currentLevel.objects.get(i);
                if(building.type!=BUILDING)continue;
                if(Game.player.y < building.hitBox.y)
                {
                    g.drawImage(ObjectTextures.transparentRepo.get(building.id),
                            building.x-Camera.x,building.y-Camera.y,null);
                }
            }
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
            if(drawing && !smokedRendering)
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
            if(!Animation.current.containsKey(Game.player)) g.drawImage(ObjectTextures.repo.get(PLAYER),0,0,17,39,null );
            else g.drawImage(Animation.getCurrentFrame(Game.player),0,0,null);
            ((Graphics2D)g).setTransform(new AffineTransform());
        }

        private void drawMinigame(Graphics g) {

            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD,15));

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

                case INTERNET:
                    if(Minigame.current.inputButtons != null && !Minigame.current.inputButtons.isEmpty())
                    {
                        if(Minigame.current.purchaseButtons!=null)
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
                    }
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
                case SELL:

                    Minigame.current.inputButtons = new ArrayList<>();

                    for (int i = 0; i < Game.player.items.size(); i++) {

                        var button = new Minigame.InputButton
                                ("Продать " + Game.player.items.get(i).name +
                                        " за " + (Game.player.items.get(i).price/4));

                        button.hiddenValue = Game.player.items.get(i).id +"";

                        button.x = Minigame.current.window.x + 50 + (200*(i/4));
                        button.y = Minigame.current.window.y + 50 + (75 * (i%4));

                        Minigame.current.inputButtons.add(button);

                        g.drawImage(ItemTextures.repo.get
                                (Game.player.items.get(i).id),button.x,button.y,50,50,null);

                        g.setColor(Color.black);
                        g.drawString(button.value,button.x, button.y + 65);
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
                    if(Game.player.crazy > 40 && Math.random()<0.005) continue; // слабые глюки
                    if(Game.player.crazy > 70 && Math.random()<0.01) continue; // средние глюки
                    if(smokedRendering && o.name.contains("bg")) continue; //глюки от травки

                    if(o == Game.player) continue;

                    if(o.type == BUILDING )
                    {
                        if(Game.player.y > o.hitBox.y) {
                            g.drawImage(ObjectTextures.repo.get(o.id), o.x - Camera.x, o.y - Camera.y, null);
                        }else continue;
                    }else
                    {
                        if(o.type==CREATURE) {


                                AffineTransform at = new AffineTransform();
                                at.translate((o.x + (double) o.hitBox.width / 3) - Camera.x, (o.y + (double) o.hitBox.height / 2) - Camera.y - 10);
                                at.rotate(Combat.calculateAngleForNpc(o), (double) o.hitBox.width / 3, (double) o.hitBox.height / 2);
                                ((Graphics2D) g).setTransform(at);
                                g.drawImage(ObjectTextures.repo.get(o.id), 0, 0, o.hitBox.width, o.hitBox.height, null);
                                ((Graphics2D) g).setTransform(new AffineTransform());


                            if(o.enemy){
                                g.setColor(Color.RED);
                                g.fillRect(o.x-Camera.x,o.y+o.hitBoxYOffset+o.hitBox.height-Camera.y,(int) (o.hitBox.width * ((o.hp*1.0)/o.startHp)),4);
                            }

                            if(!drawWalls) continue;
                            g.setColor(Color.red);
                            g.drawRect(o.x - Camera.x,o.y - Camera.y,o.hitBox.width,o.hitBox.height);
                            continue;
                        }
                        //Если объект имеет активную анимацию, не рисуем его стандартную текстуру
                        if(Animation.current.containsKey(o) ) {
                            //Получаем текущий кадр анимации для объекта и рисуем его
                            g.drawImage(Animation.getCurrentFrame(o), o.x -Camera.x, o.y - Camera.y, null);
                            continue;
                        }else //Иначе рисуем стандартную текстуру
                            g.drawImage(ObjectTextures.repo.get(o.id), o.x - Camera.x, o.y - Camera.y, o.hitBox.width+o.hitBoxXOffset,o.hitBox.height+o.hitBoxYOffset,null);
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

            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD,15));
            g.setColor(Color.WHITE);

            //Предмет в руках
            if(Game.player.equip!=null)
                g.drawString("в руках : " + Game.player.equip.name + (Game.player.equip.type==FIREARM?".  пули : " + Game.player.equip.ammo + "/"+Game.player.equip.maxAmmo : ""),320,20);

            //Рисуем статы игрока


            if(hud>0) {
                //ПОЛОСЫ (ЗДОРОВЬЕ, БРОНЯ, ДЫХАЛКА)
                //Здоровье
                g.setColor(Color.RED);
                int barX = GameWindow.WIDTH - 93;
                int barY = 20;
                g.fillRect(barX, barY, (int) (Game.player.hp * 0.7), 20);

                //Броня
                g.setColor(Color.BLUE);
                barY = 60;
                g.fillRect(barX, barY, (int) (Game.player.bonusHp * 0.7), 20);
                //Дыхалка
                g.setColor(Color.GREEN);
                barY = 100;
                g.fillRect(barX, barY, (int) ((Game.player.stamina / (Game.player.baseStamina + Game.player.bonusStamina)) * (70)), 20);


                //Голод
                g.setColor(new Color(80, 50, 40));
                barX = GameWindow.WIDTH - 142;
                barY = 143;
                g.fillRect(barX, barY, (int) ((100 - Game.player.hunger) / 2.4), 20);

                //Говно
                g.setColor(new Color(80, 50, 40));
                barY = 182;
                g.fillRect(barX, barY, (int) ((Game.player.poo) / 2.4), 20);

                //Сонливость
                g.setColor(new Color(80, 50, 210));
                barY = 210;
                g.fillRect(barX, barY, (int) ((Game.player.sleepy) / 2.4), 20);

                //Жажда
                g.setColor(new Color(0, 250, 240));
                barX = GameWindow.WIDTH - 72;
                barY = 143;
                g.fillRect(barX, barY, (int) ((100 - Game.player.thirst) / 2.4), 22);

                //Моча
                g.setColor(new Color(220, 220, 80));
                barY = 182;
                g.fillRect(barX, barY, (int) ((Game.player.pee) / 2.4), 22);

                //Псих
                g.setColor(Color.red);
                barY = 210;
                g.fillRect(barX, barY, (int) ((Game.player.crazy) / 2.4), 22);

                //Накур
                g.setColor(new Color(50, 75, 20));
                barX = GameWindow.WIDTH - 145;
                barY = 295;
                g.fillRect(barX, barY, (int) ((Game.player.smoke) / 2.4), 20);

                //Синька
                g.setColor(new Color(70, 120, 220));
                barY = 337;
                g.fillRect(barX, barY, (int) ((Game.player.drunk) / 2.4), 20);

                //Нанюх
                g.setColor(new Color(115, 175, 220));
                barX = GameWindow.WIDTH - 75;
                barY = 295;
                g.fillRect(barX, barY, (int) ((Game.player.stimulate) / 2.4), 20);

                //Мама
                //Лекарства
                g.setColor(new Color(250, 75, 20));
                barX = GameWindow.WIDTH - 105;
                barY = 377;
                g.fillRect(barX, barY, (int)(( Game.player.mommaHealth/ (DAY_LENGTH * 2.0))*62), 22);

                //Еда
                g.setColor(new Color(80, 50, 40));
                barY = 410;
                g.fillRect(barX, barY, (int)((Game.player.mommaFullness/ (DAY_LENGTH * 2.0))*62), 22);

                //Душ
                g.setColor(new Color(115, 175, 220));
                barY = 440;
                g.fillRect(barX, barY, (int)((Game.player.mommaClean/ (DAY_LENGTH * 2.0))*62), 22);

                //Отрисовка плашки худа статов
                if (hud > 0)
                    g.drawImage(ItemTextures.hudImage, GameWindow.WIDTH - 250/*широта */ - 15, 10, 250, 500, null);

                //Время
                g.setColor(Color.BLACK);
                g.drawString(GameTime.getString().substring(0, 8), GameWindow.WIDTH - 140, 475);
                g.drawString(GameTime.getString().substring(8), GameWindow.WIDTH - 140, 495);
                //Бабки $$$$$
                g.setColor(new Color(0, 100, 0));
                g.drawString(Game.player.money + "", GameWindow.WIDTH - 245, 30);
                //Шухер
                if (Game.currentLevel.isIndoors) {
                    g.setColor(Color.BLACK);
                    g.drawString((Game.currentLevel.noise + "").split("\\.")[0], GameWindow.WIDTH - 135, 270);
                }
                if (Game.player.wanted > 0) {
                    g.setColor(Color.BLACK);
                    g.drawString((Game.player.wanted + "").split("\\.")[0], GameWindow.WIDTH - 70, 270);
                }
                //Мама (остатки в часах)
                g.setColor(Color.BLACK);
                g.drawString(((int)(Game.player.mommaHealth/HOUR_LENGTH))+"",GameWindow.WIDTH-40,390);
                g.drawString(((int)(Game.player.mommaFullness/HOUR_LENGTH))+"",GameWindow.WIDTH-40,423);
                g.drawString(((int)(Game.player.mommaClean/HOUR_LENGTH))+"",GameWindow.WIDTH-40,455);

            }

            if(hud>1) {
                drawEffects(g);
            }
            if(hud>2) {
                //координаты мыши
                g.setColor(Color.WHITE);
                g.drawString("x:" + (Input.mouse.x + Camera.x) + " y:" + (Input.mouse.y + Camera.y - 30), GameWindow.WIDTH - 100, 530);
                g.drawString("x:" + Input.mouse.x + " y:" + (Input.mouse.y - 30), GameWindow.WIDTH - 100, 550);
            }

            //Рисуем окна открытых контейнеров.
            for (int i = 0; i < Game.currentLevel.objects.size(); i++) {
                GameObject o = Game.currentLevel.objects.get(i);
                if(o.opened && (o.type.equals(CONTAINER) || (o.type.equals(INTERACT) && o.minigameId != 3) || o.minigame.type == COOK ))
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
                            g.setColor(new Color(150,0,200,250));
                            g.setFont(new Font(g.getFont().getName(),Font.BOLD,20));
                            g.drawString("" + item.count,item.icon.x+15,item.icon.y+15);
                        }
                    }
                }
            }


            if(!itemVisible) return;

            //Отрисовка окна инвентаря
            g.drawImage(ItemTextures.inventory,0,0,null);
            g.drawImage(ItemTextures.clothes,107,0,null);

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
                if(item == Game.player.equip)
                {
                    g.setColor(new Color(0,150,0,100));
                    g.fillRect(item.icon.x, item.icon.y,item.icon.width,item.icon.height);
                }
            }
            //Отрисовка шмота
            if(Game.player.hat != null){
                Game.player.hat.icon.x = 188; Game.player.hat.icon.y = 50;
                g.drawImage(ItemTextures.repo.get(Game.player.hat.id),Game.player.hat.icon.x,Game.player.hat.icon.y,null);
            }
            if(Game.player.torso!= null){
                Game.player.torso.icon.x = 188; Game.player.torso.icon.y = 110;
                g.drawImage(ItemTextures.repo.get(Game.player.torso.id),Game.player.torso.icon.x,Game.player.torso.icon.y,null);
            }
            if(Game.player.foot!= null){
                Game.player.foot.icon.x = 188; Game.player.foot.icon.y = 170;
                g.drawImage(ItemTextures.repo.get(Game.player.foot.id),Game.player.foot.icon.x,Game.player.foot.icon.y,null);
            }
            if(Game.player.equip != null)
                g.drawImage(ItemTextures.repo.get(Game.player.equip.id),110,110,null);


        }

        private void drawEffects(Graphics g) {
            int row = 0;
            if(!Game.player.effects.isEmpty()) {
                g.drawString("активные эффекты : ",120,60);
                for (int i = 0; i < Game.player.effects.size(); i++) {
                    row = i;
                    Item.Effect effect = Game.player.effects.get(i);
                    g.drawString(effect.name, GameWindow.WIDTH-300, 80+ row*20 );
                }
            }
        }

        private void drawDescriptions(Graphics g) {

            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD,15));

            //Если навели на инвентарь
            if (itemVisible)
            {
                for (int i = 0; i < Game.player.items.size(); i++) {
                    Item item = Game.player.items.get(i);
                    if(item.icon.contains(Input.mouse.x, Input.mouse.y-30))
                        drawDescription(item,g);
                }
            }
            //если навели на контейнер
            for (int i = 0; i < Game.currentLevel.objects.size(); i++) {
                GameObject obj = Game.currentLevel.objects.get(i);
                if(obj != null && obj.opened && !obj.items.isEmpty())
                {
                    for (int j = 0; j < obj.items.size(); j++) {
                        Item item = obj.items.get(j);
                        if(item.icon.contains(Input.mouse.x, Input.mouse.y-30))
                            drawDescription(item,g);
                    }
                }
            }
            //магазин
            if(Minigame.current != null && (Minigame.current.type == SHOP || (Minigame.current.purchaseButtons!=null && !Minigame.current.purchaseButtons.isEmpty()) ) )
            {
                for (int i = 0; i < Minigame.current.purchaseButtons.size(); i++) {
                    Minigame.PurchaseButton button = Minigame.current.purchaseButtons.get(i);
                    Item item = Item.get(button.itemId);
                    if(button.contains(Input.mouse.x, Input.mouse.y-30))
                        drawDescription(item,g);
                }
            }
        }
        private void drawDescription(Item item, Graphics g)
        {
            g.setColor(new Color(150,150,110,220));
            g.fillRect(Input.mouse.x + 50, Input.mouse.y + 50,200,240);
            g.setColor(Color.BLACK);
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD,15));
            g.drawImage(ItemTextures.repo.get(item.id),Input.mouse.x + 51, Input.mouse.y + 51,null );
            g.drawString(item.name,Input.mouse.x + 60, Input.mouse.y + 121 );
            int y = Input.mouse.y + 140;
            int chunkSize = 20;
            List<String> chunks = new ArrayList<>();
            int length = item.description.length();
            for (int i = 0; i < length; i += chunkSize) {
                chunks.add(item.description.substring(i, Math.min(length, i + chunkSize)));
            }
            for (int i = 0; i < chunks.size(); i++) {
                g.drawString(chunks.get(i),Input.mouse.x + 60, y + (i* 20));
            }
            if(Script.momFood.keySet().stream().anyMatch(id-> id == item.id))
                g.drawString("- Мама это съест!",Input.mouse.x + 60, y + 80);
            if(Script.momDrugs.keySet().stream().anyMatch(id-> id == item.id))
                g.drawString("- Лекарство для мамы!",Input.mouse.x + 60, y + 80);
            if(item.type == MELEE)
                g.drawString("- Холодное оружие",Input.mouse.x + 60, y + 80);
            if(item.type == FIREARM)
                g.drawString("- Огнестрельное оружие",Input.mouse.x + 60, y + 80);
            if(item.type == DRUG)
                g.drawString("- Можно съесть",Input.mouse.x + 60, y + 80);
        }

        private void drawDialogue(Graphics g) {
            //ИНИЦИАЛИЗАЦИЯ
            g.setColor(new Color(200,201,169));
            g.fillRect(GameWindow.WIDTH / 3-200, GameWindow.HEIGHT - 300, GameWindow.WIDTH / 2 + 200 , 300);
            g.setColor(Color.BLACK);
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD,15));

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

            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD,15));

            //Рисуем само окно

            //Собираем квесты по боссам
            bossIcons = new ArrayList<>();
            for (int i = 0; i < Game.player.quests.size(); i++) {

                var quest = Game.player.quests.get(i);
                if(bossIcons.stream().noneMatch(icon -> icon.bossName.equals(quest.owner)))
                {
                    BossIcon bIcon = new BossIcon(quest.owner);
                    bossIcons.add(bIcon);
                    bIcon.x = (GameWindow.WIDTH/3) - 30 + ((bossIcons.indexOf(bIcon) / 4) * 80);
                    bIcon.y = (GameWindow.HEIGHT/5)+ 70 + (bossIcons.indexOf(bIcon) % 4 * 80);
                }
            }

            //Окно знакомства
            if(renderingBoss.isEmpty() && renderingQuest == null)
            {
                //Рисуем картинку окна
                g.drawImage(ItemTextures.friends,GameWindow.WIDTH/3-75,GameWindow.HEIGHT/5,null);
                for(BossIcon icon : bossIcons)
                {
                    switch (icon.bossName){
                        case "mom" ->{
                            g.drawImage(Dialogue.textures.get(2),icon.x,icon.y,icon.width, icon.height,null);
                            g.setColor(Color.BLACK);
                            g.drawString("Мама",icon.x+10,icon.y+70);
                            continue;
                        }
                        case "хачик" ->{
                            g.drawImage(Dialogue.textures.get(Game.hach.id),icon.x,icon.y,icon.width, icon.height,null);
                            g.setColor(Color.BLACK);
                            g.drawString("Хачик",icon.x+10,icon.y+70);
                            continue;
                        }
                        case "мясник" ->{
                            g.drawImage(Dialogue.textures.get(Game.butcher.id),icon.x,icon.y,icon.width, icon.height,null);
                            g.setColor(Color.BLACK);
                            g.drawString("Мясник",icon.x+10,icon.y+70);
                            continue;
                        }
                        case "хакер" ->{
                            g.drawImage(Dialogue.textures.get(Game.hacker.id),icon.x,icon.y,icon.width, icon.height,null);
                            g.setColor(Color.BLACK);
                            g.drawString("Компьютерщик",icon.x+10,icon.y+70);
                            continue;
                        }
                        case "педоффка" ->{
                            g.drawImage(Dialogue.textures.get(Game.girl.id),icon.x,icon.y,icon.width, icon.height,null);
                            g.setColor(Color.BLACK);
                            g.drawString("Педовка",icon.x+10,icon.y+70);
                            continue;
                        }
                        case "тесть" ->{
                            g.drawImage(Dialogue.textures.get(Game.mechanic.id),icon.x,icon.y,icon.width, icon.height,null);
                            g.setColor(Color.BLACK);
                            g.drawString("Тесть медбрата",icon.x+10,icon.y+70);
                            continue;
                        }
                        case "медбрат" ->{
                            g.drawImage(Dialogue.textures.get(Game.nurse.id),icon.x,icon.y,icon.width, icon.height,null);
                            g.setColor(Color.BLACK);
                            g.drawString("Медбрат",icon.x+10,icon.y+70);
                            continue;
                        }
                        case "аптекарша" ->{
                            g.drawImage(Dialogue.textures.get(Game.pharmacist.id),icon.x,icon.y,icon.width, icon.height,null);
                            g.setColor(Color.BLACK);
                            g.drawString("Аптекарша",icon.x+10,icon.y+70);
                            continue;
                        }
                        case "мент" ->{
                            g.drawImage(Dialogue.textures.get(Game.officer.id),icon.x,icon.y,icon.width, icon.height,null);
                            g.setColor(Color.BLACK);
                            g.drawString("Мент",icon.x+10,icon.y+70);
                            continue;
                        }
                        case "трап" ->{
                            g.drawImage(Dialogue.textures.get(Game.trap.id),icon.x,icon.y,icon.width, icon.height,null);
                            g.setColor(Color.BLACK);
                            g.drawString("Ловушка из валберс",icon.x+10,icon.y+70);
                            continue;

                        }
                        case "шаурма" -> {
                            g.drawImage(ObjectTextures.repo.get(132), icon.x, icon.y, icon.width, icon.height, null);
                            g.setColor(Color.BLACK);
                            g.drawString("Шаурмен", icon.x + 10, icon.y + 70);
                            continue;
                        }

                    }




                    g.setColor(Color.blue);
                    g.fillRect(icon.x,icon.y,icon.width,icon.height);
                    g.setColor(Color.PINK);
                    g.drawString(icon.bossName,icon.x+5, icon.y+20);
                }
            }
            //Задания

            if(!renderingBoss.isEmpty() && renderingQuest == null) {
                //Рисуем картинку окна
                g.drawImage(ItemTextures.quests, GameWindow.WIDTH / 3 - 75, GameWindow.HEIGHT / 5, null);

                int skipped = 0;
                int bossTextureId = 0;
                switch (renderingBoss)
                {
                    case"mom"->bossTextureId = 2;
                    case"педоффка"->bossTextureId = 4444;
                    case"хачик"->bossTextureId = 2222;
                    case"мент"->bossTextureId = 6666;
                    case"аптекарша"->bossTextureId = 9999;
                    case"тесть"->bossTextureId = 8888;
                    case"медбрат"->bossTextureId = 7777;
                    case"мясник"->bossTextureId = 3333;
                    case"трап"->bossTextureId = 5555;
                    case"хакер"->bossTextureId = 1111;
                }
                g.drawImage(Dialogue.textures.get(bossTextureId),GameWindow.WIDTH/3-80, GameWindow.HEIGHT/5 + 40,150,300,null);



                for (int i = 0; i < Game.player.quests.size(); i++) {
                    Quest quest = Game.player.quests.get(i);
                    if(!quest.owner.equals(renderingBoss)){ skipped++; continue;}
                    quest.icon.x = GameWindow.WIDTH/3 + 120;
                    quest.icon.y = GameWindow.HEIGHT/5 + 80 + (i-skipped)* 40;
                    g.setColor(new Color(50,50,30));
                    if(quest.completed) g.setColor(new Color(100,250,250,100));
                    g.fillRect(quest.icon.x,quest.icon.y,quest.icon.width,quest.icon.height);
                    g.setColor(new Color(170,170,230));
                    g.setFont(new Font(Font.SANS_SERIF,1,15));
                    g.drawString(quest.title, quest.icon.x + 5, quest.icon.y + 20);
                    if(quest.completed) g.drawImage(ItemTextures.done,quest.icon.x + quest.icon.width - 35, quest.icon.y,null);
                }
            }

            //Задание
            if(renderingQuest != null && renderingBoss.isEmpty())
            {
                //Рисуем картинку окна
                g.drawImage(ItemTextures.quest,GameWindow.WIDTH/3-75,GameWindow.HEIGHT/5,null);

                int bossTextureId = 0;
                switch (renderingQuest.owner)
                {
                    case"mom"->bossTextureId = 2;
                    case"педоффка"->bossTextureId = 5555;
                    case"хачик"->bossTextureId = 2222;
                    case"мент"->bossTextureId = 6666;
                    case"аптекарша"->bossTextureId = 9999;
                    case"тесть"->bossTextureId = 8888;
                    case"медбрат"->bossTextureId = 7777;
                    case"мясник"->bossTextureId = 3333;
                    case"трап"->bossTextureId = 5555;
                    case"хакер"->bossTextureId = 1111;
                }
                g.drawImage(Dialogue.textures.get(bossTextureId),GameWindow.WIDTH/3-80, GameWindow.HEIGHT/5 + 40,150,300,null);

                g.setColor(Color.BLACK);
                g.setFont(new Font(Font.SANS_SERIF,1,15));
                g.drawString(renderingQuest.owner + " просит " + renderingQuest.title,GameWindow.WIDTH/3 + 50,GameWindow.HEIGHT/5 + 85 );

                int chunkSize = 52;
                List<String> chunks = new ArrayList<>();
                int length = renderingQuest.description.length();
                for (int i = 0; i < length; i += chunkSize) {
                    chunks.add(renderingQuest.description.substring(i, Math.min(length, i + chunkSize)));
                }
                for (int i = 0; i < chunks.size(); i++) {
                    g.drawString(chunks.get(i),GameWindow.WIDTH/3 + 100,  + (GameWindow.HEIGHT/5 + 120) + (i* 20));
                }
            }

        }
    }
}
