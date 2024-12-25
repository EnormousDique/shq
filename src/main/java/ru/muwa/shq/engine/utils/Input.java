package ru.muwa.shq.engine.utils;

import jdk.jshell.spi.ExecutionControl;
import ru.muwa.shq.engine.Game;
import ru.muwa.shq.engine.GameWindow;
import ru.muwa.shq.engine.Renderer;
import ru.muwa.shq.entities.*;
import ru.muwa.shq.story.scripts.Script;
import ru.muwa.shq.story.Dialogue;

import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ru.muwa.shq.engine.GameWindow.fullscreen;
import static ru.muwa.shq.engine.utils.Animation.*;
import static ru.muwa.shq.engine.utils.GameTime.HOUR_LENGTH;
import static ru.muwa.shq.engine.utils.Input.KListener.*;
import static ru.muwa.shq.entities.GameObject.ITEMS_CAPACITY;
import static ru.muwa.shq.entities.GameObject.Type.DOOR;
import static ru.muwa.shq.entities.GameObject.Type.*;
import static ru.muwa.shq.entities.Item.Type.*;
import static ru.muwa.shq.entities.Level.*;
import static ru.muwa.shq.entities.Minigame.Type.*;
import static ru.muwa.shq.story.Dialogue.Companion.*;

public class Input {

    public static final KListener keyboard = new KListener();
    public static final MListener mouse = new MListener();

    public static void work()
    {
        keyboardInput();
    }
    public static void pause(){
        for (Integer i : keyboard.buttonList) {
            if (keyboard.map.get(i)) {
                if (i == KListener.M) {
                    Game.pause = !Game.pause;
                }
            }
        }
    }

    /** Метод обработки нажатий с клавиатуры **/
    private static void keyboardInput() {
        try {


            for (Integer i : keyboard.buttonList) {
                if (keyboard.map.get(i)) {
                    switch (i) {
                        case KListener.W:
                            w();
                            break;
                        case KListener.A:
                            a();
                            break;
                        case KListener.S:
                            s();
                            break;
                        case KListener.D:
                            d();
                            break;
                        case KListener.SPACE:
                            space();
                            keyboard.map.put(i, false);
                            break;
                        case KListener.SHIFT:
                            break;
                        case KListener.Q:
                            q();
                            break;
                        case KListener.E:
                            e();
                            keyboard.map.put(i, false);
                            break;

                        case KListener.I:
                            i();
                            keyboard.map.put(i, false);
                            break;

                        case KListener.J:
                            j();
                            keyboard.map.put(i, false);
                            break;

                        case KListener.C:
                            c();
                            keyboard.map.put(i, false);
                            break;

                        case ADD:
                            add();
                            keyboard.map.put(i, false);
                            break;

                        case P:
                            q();
                            Minigame.current = Minigame.get(11);
                            break;

                        case REMOVE:
                            remove();
                            break;

                        case H:
                            h();
                            keyboard.map.put(i, false);
                            break;

                    }
                }
            }
        }catch (Exception e){}
    }

    private static void h() {
        Renderer.switchHud();
    }

    private static void add() {
        if (Game.player.brush == -1) return;
        GameObject object = GameObject.get(Game.player.brush);
        object.x = Input.mouse.x + Camera.x - 6;
        object.y = Input.mouse.y + Camera.y - 30;
        Game.currentLevel.objects.add(object);
    }
    private static void remove()
    {
        System.out.println("remove");
        for (int i = 0; i < Game.currentLevel.objects.size(); i++) {
            GameObject o = Game.currentLevel.objects.get(i);
            if(o.id!=Game.player.brush) continue;
            if(o.hitBox.contains(mouse.x + Camera.x -6 ,mouse.y + Camera.y - 30)) Game.currentLevel.objects.remove(o);
        }
    }


    private static void c() {
        Command.window.setVisible(!Command.window.isVisible());
    }

    private static void q() {
        Renderer.itemVisible = false;
        Minigame.current = null;
        Dialogue.current = null;
        Dialogue.buttons = new ArrayList<>();
        Renderer.questVisible = false;
        Renderer.renderingQuest = null;
        Renderer.renderingBoss = "";
        for (int i = 0; i < Game.currentLevel.objects.size(); i++) {
            GameObject o = Game.currentLevel.objects.get(i);
            o.opened = false;
        }
    }

    private static void j() {
        Renderer.questVisible = !Renderer.questVisible;
        if(Renderer.renderingQuest != null) Renderer.renderingQuest = null;
    }

    private static void e() {
        //ИНИЦИАЛИЗАЦИЯ
        GameObject o = null;
        //Ищем объект, по которому прошло нажатие
        for (int i = 0; i < Game.currentLevel.objects.size(); i++)
        {
            if(Game.currentLevel.objects.get(i).hitBox.contains(mouse.x + Camera.x - (fullscreen?4:0),mouse.y - 30 + Camera.y))
            {
                o = Game.currentLevel.objects.get(i);
            }
        }
        if(o == null) return;
        //смотрим, дотягивается ли игрок до объекта
        boolean reachable = true;
        for (int i = 0; i < Game.currentLevel.objects.size(); i++) {
            GameObject object = Game.currentLevel.objects.get(i);
            if(object.type == BUILDING) continue;
            if(o != object && object.solid && (Combat.line.intersects(object.hitBox)&&!o.hitBox.intersects(Game.player.hitBox)))
            {
                reachable = false;
            }
        }
        Point2D playerPoint = new Point2D.Double(Game.player.hitBox.getCenterX(), Game.player.hitBox.getCenterY());
        Point2D objectPoint = new Point2D.Double(o.hitBox.getCenterX(),o.hitBox.getCenterY());
        int bonusDist = 0; if(o.hitBox.width > 70) bonusDist += o.hitBox.width/3; if(o.hitBox.height > 70) bonusDist += o.hitBox.height/3;
        if(playerPoint.distance(objectPoint) > 70 +bonusDist ) reachable = false;

        if(!reachable) return;

        //Если дотягиваемся
        if(o!=null && o.type == GameObject.Type.CONTAINER)
        {
            o.opened = !o.opened;
        }


        if(o!=null && o.type == DOOR && o.coordinates != null)
        {
            Game.currentLevel.objects.remove(Game.player);
            Game.currentLevel = Level.repo.get(o.coordinates.levelId);
            Game.currentLevel.objects.add(Game.player);
            Game.player.x = o.coordinates.x;
            Game.player.y = o.coordinates.y;
        }

        if(o!=null && o.type == GameObject.Type.CREATURE)
        {
            if(o.dialogue != 0)
            {
                Dialogue.current = Dialogue.repo.get(o.dialogue);
                Dialogue.companion=null;

                if(o==Game.mom) {Dialogue.current = Dialogue.mom.get(o.dialogue); Dialogue.companion = MOM;}
                if(o==Game.hach) {Dialogue.current = Dialogue.hach.get(o.dialogue);Dialogue.companion=HACH;}
                if(o==Game.hacker) {Dialogue.current = Dialogue.hacker.get(o.dialogue);Dialogue.companion=HACKER;}
                if(o==Game.trap) {Dialogue.current = Dialogue.trap.get(o.dialogue);Dialogue.companion=TRAP;}
                if(o==Game.nurse) {Dialogue.current = Dialogue.nurse.get(o.dialogue);Dialogue.companion=NURSE;}
                if(o==Game.mechanic) {Dialogue.current = Dialogue.mech.get(o.dialogue);Dialogue.companion=MECHANIC;}
                if(o==Game.butcher) {Dialogue.current = Dialogue.butcher.get(o.dialogue);Dialogue.companion=BUTCHER;}
                if(o==Game.girl) {Dialogue.current = Dialogue.girl.get(o.dialogue);Dialogue.companion=GIRL;}
                if(o==Game.pharmacist) {Dialogue.current = Dialogue.pharmacist.get(o.dialogue);Dialogue.companion=PHARMACIST;}
                if(o==Game.officer) {Dialogue.current = Dialogue.officer.get(o.dialogue);Dialogue.companion=OFFICER;}


                if(Dialogue.textures.get(o.id)==null) Dialogue.texture = null; //НЕ ЕБАНЕТ???
                Dialogue.texture = Dialogue.textures.get(o.id);

            }
        }

        if(o!= null && (o.type.equals(INTERACT) || o.type == ZONE) && o.minigameId != 0)
        {
            Minigame.current = o.minigame == null? Minigame.get(o.minigameId) : o.minigame;
            o.minigame = Minigame.current;
            if(o.minigame.type == SHQUR && Game.currentLevel.isIndoors) Game.currentLevel.noise += 5;
            if(o.minigame.type == PADIQUE) Minigame.current.coordinates = o.coordinates;
            if (o.minigame.type.equals(SHOP) || o.minigame.type == COOK) o.opened = true;
        }
        if(o!= null && (o.type.equals(INTERACT)||o.type.equals(ZONE)||o.type.equals(CREATURE)) && o.scriptId != 0)
        {
            ru.muwa.shq.story.scripts.Script.repo.get(o.scriptId).execute();
        }
    }

    private static void i() {
        Renderer.itemVisible = !Renderer.itemVisible;
    }

    private static void a() {
        Game.player.moveLeft();
    }
    private static void d() {
        Game.player.moveRight();
    }
    /** Обработка нажатия на пробел **/
    private static void space() {
        //Если Шкипер занят, кнопка не работает
        if(Game.player.busy || Animation.current.containsKey(Game.player)) return;
        //Если в руках Шкипера ничего нет или холодное оружие,
        //Шкипер наносит удар врукопашную
        if(Game.player.equip == null  || Game.player.equip.type.equals(MELEE)){
            //Воспроизводим анимацию атаки
            Animation.addAnimation(Game.player,Animation.get(PL_PUNCH));
            //Ищем цель и наносим урон
            Game.player.attemptMelee();
            return;
        }
        //Если в руках еда (ВОЗМОЖНО УСТАРЕЛО В СВЯЗИ С DRUG И МОИМ ЖЕЛАНИЕМ ИЗМЕНИТЬ ЛОГИКУ ЭФФЕКТОВ)
        if(Game.player.equip.type == FOOD)
        {
            //Если Шкипер хочет ссать или срать, не даем есть/пить еду
            if(Game.player.equip.effect.pee + Game.player.pee >100) return;
            if(Game.player.equip.effect.poo + Game.player.poo >100) return;
            //Отнимаем вещь / уменьшаем ее кол-во
            if(Game.player.equip.stackable && Game.player.equip.count >1)
            {
                Game.player.effects.add(new Item.Effect( Game.player.equip.effect));
                Game.player.equip.count -= 1;
            }
            else
            {
                Game.player.effects.add(new Item.Effect(Game.player.equip.effect));
                Game.player.items.remove(Game.player.equip);
                Game.player.equip = null;
                return;
            }
        }
        //Если в руках предмет типа "упаковка" или "лекарство"
        if(Game.player.equip.type == PACK || Game.player.equip.type == DRUG)
        {
            //Вызываем скрипт, указанный в предмете
            Script.repo.get(Game.player.equip.scriptId).execute();
            //Отнимаем предмет / уменьшаем кол-во
            if(Game.player.equip.stackable && Game.player.equip.count > 1) Game.player.equip.count -=1;
            else{ Game.player.items.remove(Game.player.equip); Game.player.equip = null; }
            return;
        }
        //Если в руках предмет типа "скрипт", просто вызываем его скрипт
        // весь нужный код должен уже быть в нём.
        if(Game.player.equip.type==SCRIPT){ Script.repo.get(Game.player.equip.scriptId).execute();return;}
        //Если в руках огнестрел
        if(Game.player.equip.type == FIREARM)
        {
            //Если нет патрон, не стреляем
            if(Game.player.equip.ammo == 0) return;
            //А так стреляем
            Combat.shoot();
            Game.player.equip.ammo -=1;
            //И запускаем анимацию
            Animation animation = Animation.get(PL_PISTOL_FIRE);
            if(Game.player.equip.id == 92) animation = Animation.get(PL_SHOTGUN_FIRE);
            if(Game.player.equip.id == 126) animation = Animation.get(PL_ASSAULT_RIFLE_FIRE);
            Animation.addAnimation(Game.player,animation);
        }
    }
    private static void s() {
        Game.player.moveDown();
    }
    private static void w() {
        Game.player.moveUp();
    }
    public static class KListener implements KeyListener
    {
        public static final int
                W = 87,
                A = 65,
                S = 83,
                D = 68,
                SPACE = 32,
                E = 69,
                I = 73,
                Q = 81, //Было будто бы 49 вместо 81
                ENTER = 10,
                P = 80,
                T = 84,
                SHIFT = 16,
                V = 86,
                U = 85,
                J =  74,
                M=77,
                C=67,
                ADD = 46,
                REMOVE = 44,
                H = 72;
        public HashMap<Integer,Boolean> map;
        public List<Integer> buttonList;
        KListener()
        {
            map = new HashMap<>();
            map.put(W,false);
            map.put(A,false);
            map.put(S,false);
            map.put(D,false);
            map.put(I,false);
            map.put(E,false);
            map.put(SPACE,false);
            map.put(SHIFT,false);
            map.put(M,false);
            map.put(ENTER,false);
            map.put(J,false);
            map.put(Q,false);
            map.put(C,false);
            map.put(ADD,false);
            map.put(P,false);
            map.put(H,false);
            map.put(REMOVE,false);
            map.put(M,false);
            buttonList = List.of(W,A,S,D,SPACE,I,E,J,Q,C,SHIFT,ADD,P,REMOVE,H,M);
            System.out.println("Ввод с клавиатуры инициализирован");
        }


        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            //System.out.println("key code: " + e.getKeyCode());
            map.put(e.getKeyCode(),true);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            map.put(e.getKeyCode(),false);
        }
    }

    public static class MListener implements MouseInputListener{

        public int x, y;


        HashMap<Integer,Boolean> keys = new HashMap<>();

        MListener()
        {
            keys.put(1,false);
            keys.put(3,false);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            maintainClique(e);

            if(e.getButton()==3){
                Renderer.showDescriptions = !Renderer.showDescriptions;
            }

        }

        private void maintainClique(MouseEvent e) {
            //Точка клика (х,у)
            Point point = new Point(e.getPoint());

            if(!fullscreen){ point.y -= 30; point.x -= 6;} //Так будто-бы точнее. Возможно, из-за JPanel

            inventoryClique(point);
            questClique(point);
            dialogueClique(point);
            minigameClique(point);

        }

        private void minigameClique(Point point) {
            if(Minigame.current == null) return;
            if(Minigame.current.type == INTERNET){ internetClique(point); return;}
            if(Minigame.current.type == BUS){ busClique(point); return;} //TODO: переделать остальные так же
            if(Minigame.current.type == SLEEP){sleepClique(point); return;}
            if (Minigame.current.type == TOILET){toiletClique(point); return;}
            if (Minigame.current.type == SHQUR && Game.currentLevel.isIndoors) Game.currentLevel.noise += 3.0;
            if(Minigame.current.type == ELEVATOR) {elevatorClique(point);return;}
            if(Minigame.current.type == SELL) {sellClique(point);return;}

            if(Minigame.current.inputButtons != null )
            {

                for (int i = 0; i < Minigame.current.inputButtons.size(); i++)
                {
                    Minigame.InputButton button = Minigame.current.inputButtons.get(i);
                    if(button.contains(point)){ Minigame.current.input += button.value; }
                }
            }
            if(Minigame.current.success != null && Minigame.current.success.contains(point))
            {
                if(Minigame.current.id == 16) Script.repo.get(8).execute();
                Script.repo.get(Minigame.current.script).execute();

                if (Minigame.current.type.equals(Minigame.Type.SHQUR) || Minigame.current.type==GAS) {
                    Minigame.current.success.width = 0;
                    Minigame.current.success.height = 0;
                }
                if(Minigame.current.type != COOK && Minigame.current.type != SHOP && Minigame.current.type != BUS && Minigame.current.type != SHQUR)
                    Minigame.current = null;
            }
            if (Minigame.current != null && Minigame.current.type == SHOP)
            {
                for (Minigame.PurchaseButton button : Minigame.current.purchaseButtons)
                {
                    if(button.contains(point) && Game.player.money >= Item.get(button.itemId).price)
                    {
                        if(Game.player.items.size() >= Game.player.ITEMS_CAPACITY) return;
                        Game.player.money -= Item.get(button.itemId).price;
                        Game.player.addItem(Item.get(button.itemId));
                    }
                }
            }
        }

        private void sellClique(Point point) {
            for (int i = 0; i < Minigame.current.inputButtons.size(); i++) {
                var button = Minigame.current.inputButtons.get(i);
                if(button.contains(point))
                {
                    var item = Game.player.items.stream()
                            .filter(item1->item1.id == Integer.parseInt(button.hiddenValue))
                            .findFirst().orElse(null);

                    if (item==null) return;

                    item.count -= 1;
                    if(item.count <= 0) Game.player.items.remove(item);
                    if(Game.player.equip==item) Game.player.equip = null;

                    Game.player.money += (item.price / 4);
                }
            }
        }

        private void internetClique(Point point) {
            for (int i = 0; i < Minigame.current.inputButtons.size(); i++) {
                Minigame.InputButton b = Minigame.current.inputButtons.get(i);
                if(!b.contains(point))continue;
                if(b.value.equals("x")) {Minigame.current = null;return;}
                if(b.value.equals("<---")) {Minigame.current = Minigame.get(20); if(Game.player.quests.stream().noneMatch(q->(q.id == 18 || q.id== 58)&& q.completed)) {Script.repo.get(25).execute();} return;}
                if(b.value.equals("<--- ")) {Minigame.current = Minigame.get(21);return;}
                if(b.value.equals("<---  ")) {Minigame.current = Minigame.get(22);return;}
                if(b.value.equals("<---   ")) {Minigame.current = Minigame.get(23);return;}
                if(b.value.contains("тить")) {Script.repo.get(29).execute(); return;}
                if(b.value.contains("нят")) {Script.repo.get(4444_22).execute(); return;}
            }
            if(Minigame.current.purchaseButtons != null && !Minigame.current.purchaseButtons.isEmpty())
            {
                for (Minigame.PurchaseButton button : Minigame.current.purchaseButtons)
                {
                    if(button.contains(point) && Game.player.money >= Item.get(button.itemId).price)
                    {
                        Game.player.money -= Item.get(button.itemId).price;
                        var warehouse = Level.repo.get(WILDBERRIES).objects.stream()
                                .filter(o->o.id==1).findFirst().orElse(null);
                        warehouse.addItem(Item.get(button.itemId));
                    }
                }
            }
        }

        private void elevatorClique(Point point) {
            for (int i = 0; i < Minigame.current.inputButtons.size(); i++) {
                var button = Minigame.current.inputButtons.get(i);
                if(button.contains(point))
                {
                    switch (button.value)
                    {
                        case "1" -> Game.player.y = 2255;
                        case "2" -> Game.player.y = 1990;
                        case "3" -> Game.player.y = 1730;
                        case "4" -> Game.player.y = 1460;
                        case "5" -> Game.player.y = 1190;
                        case "6" -> Game.player.y = 920;
                        case "7" -> Game.player.y = 670;
                        case "8" -> Game.player.y = 390;
                        case "9" -> Game.player.y = 130;
                    }

                }
            }
        }

        private void toiletClique(Point point) {
            for (Minigame.InputButton button : Minigame.current.inputButtons)
            {
                if (button.contains(point))
                    switch (button.value)
                    {
                        case "ссать":
                            if(Game.player.pee > 15)
                            {
                                Game.player.pee = 0;
                                if (Game.currentLevel.isIndoors) Game.currentLevel.noise +=30;
                                if(Game.currentLevel.id == STREET_1) Game.player.wanted += 10;
                                Animation.addAnimation(Game.player,Animation.get(PL_PEE));
                            }
                            break;
                        case "срать":
                            if(Game.player.poo > 15)
                            {
                                Game.player.poo = 0;
                                if (Game.currentLevel.isIndoors) Game.currentLevel.noise +=60;
                            }
                            break;
                    }
                Minigame.current = null;
            }
        }

        private void sleepClique(Point point) {
            for (Minigame.InputButton b : Minigame.current.inputButtons)
            {
                if(b.contains(point)) Minigame.current.input = b.value;
            }
            if(Minigame.current.success != null && Minigame.current.success.contains(point))
            {
                Script.repo.get(Minigame.current.script).execute();
                Minigame.current = null;
            }
        }

        private void busClique(Point point) {
            for (Minigame.InputButton button : Minigame.current.inputButtons)
            {
                if (button.contains(point))
                {
                    String where = button.value;
                    if(Game.player.busFare > Game.player.money) return;
                    if(where.contains("1_"))
                    {
                        Game.player.money -= Game.player.busFare;
                        Game.player.busFare += 2;
                        Game.player.x = 1700;
                        Game.player.y = 1700;
                        GameTime.forward(HOUR_LENGTH /2);
                        break;
                    }
                    if(where.contains("2_"))
                    {
                        Game.player.money -= Game.player.busFare;
                        Game.player.busFare += 2;

                        Game.player.x = 5500;
                        Game.player.y = 2000;
                        GameTime.forward(HOUR_LENGTH/2);
                        break;
                    }
                    if(where.contains("3_"))
                    {
                        Game.player.money -= Game.player.busFare;
                        Game.player.busFare += 2;

                        Game.player.x = 5350;
                        Game.player.y = 6150;
                        GameTime.forward(HOUR_LENGTH/2);
                        break;
                    }
                    if (where.contains("4_"))
                    {
                        Game.player.money -= Game.player.busFare;
                        Game.player.busFare += 2;

                        Game.player.x = 2900;
                        Game.player.y = 5150;
                        GameTime.forward(HOUR_LENGTH/2);
                        break;
                    }
                    if (where.contains("5_"))
                    {
                        Game.player.money -= Game.player.busFare;
                        Game.player.busFare += 2;

                        Game.player.x = 5900;
                        Game.player.y = 8000;
                        GameTime.forward(HOUR_LENGTH/2);
                        break;
                    }
                    if (where.contains("6_"))
                    {
                        Game.player.money -= Game.player.busFare;
                        Game.player.busFare += 2;

                        Game.player.x = 8500;
                        Game.player.y = 7250;
                        GameTime.forward(HOUR_LENGTH/2);
                        break;
                    }
                    if (where.contains("7_"))
                    {
                        Game.player.money -= Game.player.busFare;
                        Game.player.busFare += 2;
                        Game.player.y = 7800;
                        Game.player.x = 1800;
                        GameTime.forward(HOUR_LENGTH/2);
                        break;
                    }
                }
            }
        }

        /** Обработка клика по окну диалога **/
        private void dialogueClique(Point point) {
            //Проходимся по кнопкам ответа текущего диалога, ищем ту, по которой нажали
            for (int i = 0; i < Dialogue.buttons.size(); i++) {
                //Кнопка
                Dialogue.ResponseButton button = Dialogue.buttons.get(i);
                //Если по кнопке попал щелчок мышью
                if(button.contains(point))
                {
                    //Обнуляем список кнопок с ответами (т.к. будем менять диалог)
                    Dialogue.buttons = new ArrayList<>();
                    //Если в нажатой кнопке нет ссылки на продолжение или скрипт
                    if(button.nextMessage == 0 && button.script == 0)
                    {
                        //Значит это конец диалога
                        Dialogue.current = null;
                        return;
                    }
                    //Если в нажатой кнопке есть скрипт
                    if(button.script != 0)
                    {
                        //Мы вызываем и исполняем этот скрипт
                        Script.repo.get(button.script).execute();
                    }
                    //Если нет ссылки на продолжение, заканчиваем
                    if(button.nextMessage==0) return;
                    //Если же ссылка на продолжение есть
                    //Определяем компаньона
                    if(Dialogue.companion==null)
                        Dialogue.current = Dialogue.repo.get(button.nextMessage);
                    //В зависимости от компаньона выбираем в каком из наборов мы ищем диалог по ссылке
                    switch (Dialogue.companion)
                    {
                        case MOM -> Dialogue.current = Dialogue.mom.get(button.nextMessage);
                        case HACH -> Dialogue.current = Dialogue.hach.get(button.nextMessage);
                        case HACKER -> Dialogue.current = Dialogue.hacker.get(button.nextMessage);
                        case TRAP -> Dialogue.current = Dialogue.trap.get(button.nextMessage);
                        case MECHANIC -> Dialogue.current = Dialogue.mech.get(button.nextMessage);
                        case NURSE -> Dialogue.current = Dialogue.nurse.get(button.nextMessage);
                        case BUTCHER -> Dialogue.current = Dialogue.butcher.get(button.nextMessage);
                        case GIRL -> Dialogue.current = Dialogue.girl.get(button.nextMessage);
                        case PHARMACIST -> Dialogue.current = Dialogue.pharmacist.get(button.nextMessage);
                        case OFFICER -> Dialogue.current = Dialogue.officer.get(button.nextMessage);
                    }
                }
            }
        }

        /** Обработка клика по окну инвентаря ("вещи") **/
        private void inventoryClique(Point point) {

            boolean inContainer = false;
            GameObject container = null;

            for (int i = 0; i < Game.currentLevel.objects.size(); i++) {

                GameObject o = Game.currentLevel.objects.get(i);
                if(((o.type == CONTAINER || o.type == INTERACT) || (o.minigame!= null && o.minigame.type == COOK)) && o.opened)
                {
                    inContainer = true;
                    container = o;
                }
            }
            if(inContainer)
            {
                for (int i = 0; i < container.items.size(); i++) {
                    Item item = container.items.get(i);
                        if(item.icon.contains(point))
                        {
                            boolean canTake = true;
                            if(Game.player.items.size()>=Game.player.ITEMS_CAPACITY)
                            {
                                if(!item.stackable) canTake = false;
                                if(item.stackable && Game.player.items.stream().noneMatch(item1 -> item1.id == item.id))
                                    canTake = false;
                            }
                            if(item.name.contains("rub")) canTake = true;
                            if (!canTake) return;
                            if (!item.stackable || item.count == 1) {
                                container.items.remove(item);
                                Game.player.addItem(item);
                            } else {
                                item.count -= 1;
                                Game.player.addItem(Item.get(item.id));
                            }
                            return;
                        }
                }
            }

            if(!Renderer.itemVisible) return;

            List<Item> items = new ArrayList<>(Game.player.items);
            if(Game.player.hat != null) items.add(Game.player.hat);
            if(Game.player.torso != null) items.add(Game.player.torso);
            if(Game.player.foot != null) items.add(Game.player.foot);

            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);

                if (item.icon.contains(point)) {

                    if(inContainer && container.items.size() < ITEMS_CAPACITY) {
                            if (!item.stackable || item.count ==1) {
                                if(container.id!=75)
                                    container.addItem(item);
                                Game.player.items.remove(item);
                            } else {
                                if(container.id!=75)
                                    container.addItem(Item.get(item.id));
                                item.count -= 1;
                            }
                            if(item==Game.player.equip){ Game.player.effects.remove(Game.player.equip.effect);  Game.player.equip = null;}
                            return;
                    }

                    if(item.type == AMMO) Combat.Reload(Game.player.equip, item);

                    if(item.type == HAT)
                    {
                        if(Game.player.hat == item)
                        {
                            if(Game.currentLevel.id == SEWERS) return;//Нельзя снять шляпу (противогаз) в канализации
                            Game.player.hat = null;
                            Game.player.addItem(item);
                            Game.player.effects.remove(item.effect);
                            return;
                        }
                        else if(Game.player.hat != null)
                        {
                            return;
                        }
                        else {
                            Game.player.items.remove(item);
                            Game.player.hat = item;
                            Game.player.effects.add(item.effect);
                            return;
                        }
                    }

                    if(item.type == TORSO)
                    {
                        if(Game.player.torso == item)
                        {
                            Game.player.torso = null;
                            Game.player.addItem(item);
                            Game.player.effects.remove(item.effect);

                            return;
                        }
                        else if(Game.player.torso != null)
                        {
                            return;
                        }
                        else {
                            Game.player.items.remove(item);
                            Game.player.torso = item;
                            Game.player.effects.add(item.effect);
                            return;
                        }
                    }

                    if(item.type == FOOTWEAR)
                    {
                        if(Game.player.foot == item)
                        {
                            Game.player.foot = null;
                            Game.player.addItem(item);
                            Game.player.effects.remove(item.effect);

                            return;
                        }
                        else if(Game.player.foot != null)
                        {
                            return;
                        }
                        else {
                            Game.player.items.remove(item);
                            Game.player.foot = item;
                            Game.player.effects.add(item.effect);

                            return;
                        }
                    }



                    if (Game.player.equip == item) {
                        Game.player.equip = null;
                        return;
                    }
                    if (/*Game.player.equip == null &&*/ (item.type != FOOTWEAR && item.type != TORSO && item.type != HAT && item.type != AMMO)) {
                        Game.player.equip = item;
                        return;
                    }
                }
            }
        }

        private void questClique(Point point) {
            if(!Renderer.questVisible) return;

            if(Renderer.renderingBoss.isEmpty() && Renderer.renderingQuest == null)
            {
                for (int i = 0; i < Renderer.bossIcons.size(); i++) {
                    var icon = Renderer.bossIcons.get(i);
                    if(icon.contains(point)) Renderer.renderingBoss = icon.bossName;

                }
                return;
            }
            if(!Renderer.renderingBoss.isEmpty())
            {
                for (int i = 0; i < Game.player.quests.size(); i++) {
                    var quest = Game.player.quests.get(i);
                    if(!quest.owner.equals(Renderer.renderingBoss)) continue;
                    if(quest.icon.contains(point))
                    {
                        Renderer.renderingBoss = "";
                        Renderer.renderingQuest = quest;
                    }
                }
            }
        }
        @Override
        public void mousePressed(MouseEvent e) {
            keys.put(e.getButton(),true);
        }
        @Override
        public void mouseReleased(MouseEvent e) {
            keys.put(e.getButton(),false);
        }
        @Override
        public void mouseEntered(MouseEvent e) {
        }
        @Override
        public void mouseExited(MouseEvent e) {
        }
        @Override
        public void mouseDragged(MouseEvent e) {
        }
        @Override
        public void mouseMoved(MouseEvent e) {
            if(fullscreen){
                x = e.getX(); y = e.getY()+30;
            }else {
                x = e.getX();
                y = e.getY();
            }
        }
    }
    public static void foo(){}
}
