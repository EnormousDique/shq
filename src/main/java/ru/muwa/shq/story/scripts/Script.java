package ru.muwa.shq.story.scripts;

import ru.muwa.shq.engine.Game;
import ru.muwa.shq.engine.utils.GameTime;
import ru.muwa.shq.entities.*;
import ru.muwa.shq.story.Dialogue;
import ru.muwa.shq.story.Quest;

import javax.print.attribute.standard.MediaPrintableArea;
import java.awt.*;
import java.beans.beancontext.BeanContextServiceAvailableEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.muwa.shq.engine.utils.GameTime.*;
import static ru.muwa.shq.engine.utils.GameTime.TimeOfTheDay.DAY;
import static ru.muwa.shq.engine.utils.GameTime.TimeOfTheDay.SUNSET;
import static ru.muwa.shq.entities.GameObject.ITEMS_CAPACITY;
import static ru.muwa.shq.entities.Level.*;

/**
 * ����� ���������������� ���������
 */
public abstract class Script {
    /** �������� ����� "���������" �������� **/
    public abstract void execute();
    public boolean expired; //����, ������������ ��� ������� �������� ��� "������������"
    public static HashMap<Integer,Script> repo = new HashMap<>(); //��������� ���������
    private static HashMap<Integer,Long> momDrugs = new HashMap<>(),
                                         momFood = new HashMap<>();
    //���� ��� �������� ������� ��������. ���� - ���������� ���������� ��������, �������� - ������.
    private static HashMap<Coordinates,String> padiquePasswords = new HashMap<>();
    /** ����� �������� ������ �������� **/
    private static boolean checkDomofonPass(Map.Entry<Coordinates,String> map)
    {
        if(map.getKey().levelId == Minigame.current.coordinates.levelId &&
           map.getKey().x == Minigame.current.coordinates.x &&
           map.getKey().y == Minigame.current.coordinates.y &&
           map.getValue().equals(Minigame.current.input)) return true;
        return false;
    }
    static
    {
        //��� ���� ��� � ��������� �� ��� ��������
        momFood.put(50,HOUR_LENGTH * 6);//�����
        momFood.put(63,HOUR_LENGTH * 1);//������
        momFood.put(57,HOUR_LENGTH * 1);//�����
        momFood.put(58,HOUR_LENGTH * 4);//������� ������
        momFood.put(59,HOUR_LENGTH * 10);//�����
        momFood.put(60,HOUR_LENGTH * 3);//�������
        momFood.put(61,HOUR_LENGTH * 5);//������� ���
        momFood.put(62,HOUR_LENGTH * 2);//�������� � ������
        momFood.put(64,HOUR_LENGTH * 12);//����
        //����� ��������� �������� ���� � �� ����� �����
        momDrugs.put(46,HOUR_LENGTH * 6);
        momDrugs.put(47,HOUR_LENGTH * 12);
        momDrugs.put(48,HOUR_LENGTH * 24);
        momDrugs.put(49,HOUR_LENGTH * 36);
        //������ �� ���������
        padiquePasswords.put(new Coordinates(550,1400,BUILDING_1),"228");
        padiquePasswords.put(new Coordinates(1185,1400,BUILDING_1),"229");
        padiquePasswords.put(new Coordinates(1820,1400,BUILDING_1),"230");
        padiquePasswords.put(new Coordinates(2455,1400,BUILDING_1),"231");
        padiquePasswords.put(new Coordinates(550,1400,BUILDING_2),"144");
        padiquePasswords.put(new Coordinates(1185,1400,BUILDING_2),"145");
        padiquePasswords.put(new Coordinates(1820,1400,BUILDING_2),"146");
        padiquePasswords.put(new Coordinates(2455,1400,BUILDING_2),"147");
        padiquePasswords.put(new Coordinates(550,1400,BUILDING_3),"555");
        padiquePasswords.put(new Coordinates(1185,1400,BUILDING_3),"556");
        padiquePasswords.put(new Coordinates(1820,1400,BUILDING_3),"557");
        padiquePasswords.put(new Coordinates(2455,1400,BUILDING_3),"558");
        padiquePasswords.put(new Coordinates(550,1400,BUILDING_4),"358");
        padiquePasswords.put(new Coordinates(1185,1400,BUILDING_4),"359");
        padiquePasswords.put(new Coordinates(1820,1400,BUILDING_4),"360");
        padiquePasswords.put(new Coordinates(2455,1400,BUILDING_4),"361");
        padiquePasswords.put(new Coordinates(750,2400,BUILDING_5),"417");
        padiquePasswords.put(new Coordinates(1580,2400,BUILDING_5),"418");
        padiquePasswords.put(new Coordinates(2370,2400,BUILDING_5),"419");
        padiquePasswords.put(new Coordinates(3200,2400,BUILDING_5),"420");
        padiquePasswords.put(new Coordinates(750,2400,BUILDING_6),"665");
        padiquePasswords.put(new Coordinates(1580,2400,BUILDING_6),"666");
        padiquePasswords.put(new Coordinates(2370,2400,BUILDING_6),"667");
        padiquePasswords.put(new Coordinates(3200,2400,BUILDING_6),"668");
        padiquePasswords.put(new Coordinates(550,1400,BUILDING_7),"997");
        padiquePasswords.put(new Coordinates(1185,1400,BUILDING_7),"998");
        padiquePasswords.put(new Coordinates(1820,1400,BUILDING_7),"999");
        padiquePasswords.put(new Coordinates(2455,1400,BUILDING_7),"000");

        /**
         *
         *
         * �������
         *
         */
        /** ������ ����������� **/
        Script script = new Script() {
            //�����������
            @Override
            public void execute() {
                Game.currentLevel.objects.remove(Game.player);
                Game.currentLevel = Level.repo.get(Level.STREET_1);
                Game.player.x = 7900; Game.player.y = 8000;
                Game.currentLevel.objects.add(Game.player);
                Game.player.hp = 50;
                Game.player.crazy += 10;
                Game.player.hunger = 0;
                Game.player.thirst = 0;
                Game.player.sleepy = 0;
                Game.player.pee = 0;
                Game.player.poo = 0;
                GameTime.forward(8 * HOUR_LENGTH);
            }
        };
        repo.put(1,script);

        /** ������ ������� **/
        script = new Script() {
            @Override
            public void execute() {
                //��� �������
            }//��������� ������ � ����������� �������, ����� ��� ���������� script id
        }; repo.put(-1,script);

        /** ������ ����-���� ��� **/
        script = new Script() {
            //����
            @Override
            public void execute() {
                //���� 2 ����
                if(Minigame.current.input.startsWith("2")) {
                    Game.player.sleepy -= 20;
                    GameTime.forward(2 * HOUR_LENGTH);
                }
                //���� 4 ����
                if(Minigame.current.input.startsWith("4")) {
                    Game.player.sleepy -= 50;
                    GameTime.forward(4 * HOUR_LENGTH);
                }
                //���� 8 �����
                if(Minigame.current.input.startsWith("8")) {
                    Game.player.sleepy = 0;
                    GameTime.forward(8 * HOUR_LENGTH);
                }
                //���� 12 �����
                if(Minigame.current.input.startsWith("12")) {
                    Game.player.sleepy = 0;
                    Game.player.crazy = Math.min(0, Game.player.crazy - 50);
                    GameTime.forward(12 * HOUR_LENGTH);
                }
                //���������� ����-����
                Minigame.current.input = "";
                Minigame.current = null;
            }
        };
        repo.put(3,script);

        /** ������ ����-���� ���� �� ����� **/
        script = new Script() {
            //����
            @Override
            public void execute() {
                Game.player.thirst = 0;
                Game.player.pee = Math.min(100,Game.player.pee + 50);
            }
        };
        repo.put(4,script);

        /** ������ ���� **/
        script = new Script() {
            @Override
            public void execute() {
                //�������� ������������
                Game.player.stimulate += 20;
                //�������� �������
                Game.player.stamina += 50;
                //�������� ����
                Game.player.crazy += 10;
                //���� ��
                Game.player.hp += 20;
                //���� ������
                Game.player.thirst += 10;
                //������� ����������
                if(Game.player.sleepy > 40) Game.player.sleepy -= 15;
                //������� �����
                if(Game.player.hunger < 60) Game.player.hunger -= 10;
                //�������� ����
                Game.player.equip.count -=1;
                if(Game.player.equip.count < 0){
                    Game.player.items.remove(Game.player.equip);
                    Game.player.equip = null;
                }
            }
        }; repo.put(5,script);

        /** ������ ����-���� � ���������� ��������� ("�������" ��������) **/
        //TODO: ��������� � ����???
        script = new Script() {
            @Override
            public void execute() {
                if(expired) return;
                //������� �����
                var ment = GameObject.get(7);
                ment.x = Game.player.x - 50; ment.y = Game.player.y + 70;
                //��������� �� �������
                Game.currentLevel.objects.add(ment);
                //������� ������
                Dialogue.current = Dialogue.repo.get(25);
                Game.player.wanted = 110;
                //������ �����������
                expired = true;
            }
        };
        repo.put(8,script);

        /** ������ �����  **/
        script = new Script() {
            @Override
            public void execute() {
                //����� �������� ������
                Game.player.drunk += 35;
                Game.player.sleepy += 15;
                Game.player.hunger += 15;
                Game.player.thirst += 15;
                Game.player.psycho -= 10;
            }
        }; repo.put(9,script);

        /** ������ ����-���� ��������. ����� ������� ��������  **/
        script = new Script() {
            @Override
            public void execute() {
                //todo: �������� ���� �� ��������� ������ ������ ������
                //������ �������� (������)
                Game.player.addItem(Item.get(77));
            }
        };
        repo.put(10,script);

        /** ������ ����-���� ����� (������� ���) **/
        script = new Script() {
            @Override
            public void execute() {
                //TODO ����� ���. ����������
                //������� ����� �� ������
                for (int i = 0; i < Game.currentLevel.objects.size(); i++) {
                    GameObject o = Game.currentLevel.objects.get(i);
                    if (o.name.equals("stove"))
                    {
                        //�������� ����� ���������, ������� ��� ���������� �� ������������ �� �����
                        if(Crafting.craft(o.items) != -1)
                        {
                            Item craftedItem = Item.get(Crafting.craft(o.items));
                            if(craftedItem.id == 63) craftedItem.count = 6;
                            if (craftedItem.id == 60) craftedItem.count = 3;
                            //���� ������ ����������� ����
                            Game.player.addItem(craftedItem);
                            o.items = new ArrayList<>();
                            return;
                        } else return; //��� �� ���� (���� �� ���)
                    }
                }
            }
        };
        repo.put(11,script);

        /** ������ �������� ���� ������� **/
        script = new Script() {
            @Override
            public void execute() {
                    //������� �������� � ���������
                    var kanistra = Game.player.items.stream()
                            .filter(i->i.id==91).findFirst().orElse(null);
                    //���� �� �����, �����
                    if(kanistra == null) return;
                    //���������� ������� ����� ����� � �������� �� ��������
                    int volume = Integer.parseInt
                            (kanistra.description.split(":")[1].split("/")[0]);
                    volume += 5; //�������� ����� �������
                    kanistra.description = "���������:"+volume+"/100"; //��������� ��������
                    Game.player.wanted += 20; //������� ����� ����� �������
            }
        };repo.put(17,script);
        /** ������ ������ ���� **/
        script = new Script() {
            //�������� ��� � ������� ��� ����.
            @Override
            public void execute() {
                //���������, ��� ���� ����� ��� (������� ������ 2-� ����)
                if(Game.player.mommaFullness < DAY_LENGTH * 2) {
                    //���� ��� �����, �� � ��������� � ���, ������� ��������������� ������
                    if(Game.player.items.stream().noneMatch(i -> momFood.containsKey(i.id)))
                    {Dialogue.current = Dialogue.repo.get(3); return;}
                    for (int i = 0; i < Game.player.items.size(); i++) {
                        //���������� ���������
                        Item item = Game.player.items.get(i);
                        if(momFood.containsKey(item.id)) {
                            //���� ������� ��� � ���������, �������� ���
                            if(item.count > 1) {
                                item.count -=1;
                            }else Game.player.items.remove(item);
                            //����������� ������ ���� �� �����. ��������
                            Game.player.mommaFullness += momFood.get(item.id);
                            Dialogue.current = null;
                        }
                    }
                } else {
                    //�� ����� ���, ��������� ��������������� ������
                    Dialogue.current = Dialogue.repo.get(2);
                }
            }
        };
        repo.put(20,script);

        /** ������ ���� ���� ��������� **/
        script = new Script() {
            //�������� �������� � ������� ��� ����
            @Override
            public void execute() {
                //���������, ��� ���� ����� ��������� (������� ������ 2-� ����)
                if(Game.player.mommaHealth < DAY_LENGTH * 2) {
                    //���� ��������� �����, �� � ��������� �� ���, ������� ��������������� ������
                    if(Game.player.items.stream().noneMatch(i -> momDrugs.containsKey(i.id)))
                    {Dialogue.current = Dialogue.repo.get(3); return;}
                    for (int i = 0; i < Game.player.items.size(); i++) {
                        //���������� ���������
                        Item item = Game.player.items.get(i);
                        if(momDrugs.containsKey(item.id)) {
                            //���� ������� ��������� � ���������, �������� ���
                            if(item.count > 1) {
                                item.count -=1;
                            }else Game.player.items.remove(item);
                            //����������� ������ ���� �� �����. ��������
                            Game.player.mommaHealth += momDrugs.get(item.id);
                            Dialogue.current = null;
                        }
                    }
                } else {
                    //�� ����� ���������, ��������� ��������������� ������
                    Dialogue.current = Dialogue.repo.get(2);
                }
            }
        };
        repo.put(21,script);

        /** ������ ����-���� "������ ���� ����" **/
        script = new Script() {
            @Override
            public void execute() {
                Game.player.mommaClean += DAY_LENGTH * 2;
                GameObject mom = Game.currentLevel.objects.stream().filter(o->o.name.equals("mom")).findFirst().get();
                mom.y=200;
            }
        };
        repo.put(22,script);

        /** ������ ��������� ����� �� ���� � ������ **/
        script = new Script() {
            @Override
            public void execute() {
                GameObject mom = Game.currentLevel.objects.stream().filter(o->o.name.equals("mom")).findFirst().get();
                if(mom.y > 380) Minigame.current = Minigame.get(4);
            }
        };
        repo.put(23,script);

        /** ������ ���������� ���� ������� ����� **/
        script = new Script() {
            //������, ����������� �� ������� "���, ����� � ���"
            @Override
            public void execute() {
                GameObject mom = Game.currentLevel.objects.stream().filter(o -> o.name.equals("mom")).findFirst().get();
                if(Game.player.mommaClean < DAY_LENGTH * 2) {
                    mom.y = 450;
                    mom.x=240;
                    Dialogue.current = null;
                } else{
                    Dialogue d = new Dialogue(); d.message = "� ������� ������";
                    d.responses = List.of(new Dialogue.Response("�����",0,0));
                    Dialogue.current = d;
                }
            }
        };
        repo.put(24,script);
        /** ������ ����-���� �������. ��������� ��������� ������ **/
        script = new Script() {
            @Override
            public void execute() {
                //���������� �� ���� � ��������
                for (Map.Entry<Coordinates,String> domofon : padiquePasswords.entrySet())
                {
                    if(checkDomofonPass(domofon))
                    {
                        //���� ��������� ������ �������, ��������� ������.
                        Game.currentLevel.objects.remove(Game.player);
                        Game.currentLevel = Level.repo.get(domofon.getKey().levelId);
                        Game.player.x = domofon.getKey().x;
                        Game.player.y = domofon.getKey().y;
                        Game.currentLevel.objects.add(Game.player);
                    }
                }
                Minigame.current.input = ""; //���������� ���� �����
            }
        };
        repo.put(30,script);

        /** ������ ����� � ����� **/
        script = new Script() {
            @Override
            public void execute() {
                //���������� ������ � �����
                Game.currentLevel.objects.remove(Game.player);
                Game.currentLevel = Level.repo.get(GARAGE);
                Game.currentLevel.objects.add(Game.player);
                Game.player.x = 200; Game.player.y = 200;
            }
        }; repo.put(70,script);

        /** ������ ������� ���� **/
        script = new Script() {
            @Override
            public void execute() {
                //���� �����
                var pipe = Game.currentLevel.objects.stream()
                        .filter(o->o.id==73).findFirst().orElse(null);
                if(pipe == null) return;
                //������� ����� � ������
                Game.currentLevel.objects.remove(pipe);
                //���� ����� ������� ��� �������
                Game.player.addItem(Item.get(95));
            }
        }; repo.put(73,script);

        /** ������ ���������� ������ **/
        script = new Script() {
            //���������� ������
            @Override
            public void execute() {
                Game.player.addItem(Math.random() > 0.5 ? Item.get(1) : Item.get(7));// 50%50 ����� ��� ����
            }
        };
        repo.put(77,script);

        /** ������ �������� **/
        script = new Script() {
            @Override
            public void execute() {
                //���������, ���� �� ���������� �����
                //������� ���� �������� �������� (100x100 ������ ������)
                Rectangle zone =
                        new Rectangle(Game.player.x - 50, Game.player.y-50,100,100);
                //������� ����, �������� � ���� �������� ��������
                GameObject bush = Game.currentLevel.objects.stream()
                        .filter(o->o.hitBox.intersects(zone) && o.id==67).findFirst().orElse(null);
                if(bush == null) return;
                //���� ���� �������, ������� ��� � ������
                Game.currentLevel.objects.remove(bush);
                //���� ������� ��������� ���� ��� �������
                Game.player.addItem(Item.get(87));
            }
        };repo.put(88,script);
        /** ������ ������**/
        script = new Script() {
            @Override
            public void execute() {
                Game.player.smoke += 20;
                Game.player.thirst += 20;
                Game.player.hunger += 10;
                Game.player.sleepy += 7;
                Game.player.crazy += 7;
            }
        }; repo.put(89,script);
        /** ������ ������� ��� ���������� **/
        script = new Script() {
            @Override
            public void execute() {
                //TODO:
            }
        }; repo.put(90,script);
        /** ������ �������� **/
        script = new Script() {
            @Override
            public void execute() {
                //���� �������� ��������
                Rectangle zone =
                        new Rectangle(Game.player.x - 50, Game.player.y-50,100,100);
                //���� ������ � ���� ��������
                GameObject car = Game.currentLevel.objects.stream()
                        .filter(o->o.hitBox.intersects(zone) && o.name.contains("car")).findFirst().orElse(null);
                //���� ���, ����
                if(car == null) return;
                //�������� �������� ���� ������� � ��������� ������ �� ��� � �����
                if(car.minigame!= null) Minigame.current = car.minigame;
                else {
                    Minigame.current = Minigame.get(17);
                    car.minigame = Minigame.current;
                }
            }
        };repo.put(91,script);
        /** ������ ������� ������� **/
        script = new Script() {
            @Override
            public void execute() {
                //������ ����� ������ �� �����
                if(Game.currentLevel.id != STREET_1) return;
                //������ ����� ������ � ���� ����� (id ==69)
                if(Game.currentLevel.objects.stream()
                        .noneMatch(o -> o.id==69 && o.hitBox.intersects(Game.player.hitBox)))
                    return;
                //���� �������� ������
                Rectangle zone =
                        new Rectangle(Game.player.x - 50, Game.player.y-50,100,100);
                //���� ��� � ���� ��������
                GameObject pit = Game.currentLevel.objects.stream()
                        .filter(o->o.hitBox.intersects(zone) && o.name.contains("pit")).findFirst().orElse(null);
                if(pit != null) return; //���� � ���� �������� ���� ���, �������� ����� ������
                //����� ������ ���
                pit = GameObject.get(66);//������� ����� ���
                //������ �� ������� ����� �� ��������
                pit.x = Game.player.x; pit.y = Game.player.y + Game.player.hitBox.height;
                Game.currentLevel.objects.add(pit);
                //� ������ ���� ������� ��������
                if(Math.random() > 0.8) Game.player.addItem(Item.get(77));
                //����� �������
                Game.player.wanted += 20;
            }
        }; repo.put(93,script);
        //==========================================================================================//
        /**
         *
         * ������� ������ (�������������)
         *
         */
        /** ����� ������ �������
        // <<��� �� ���� ������>> **/
        script = new Script() {
            @Override
            public void execute() {
                //���� ��� ������� ������� (����� "������� ����" � "�����������")
                if(Game.player.quests.stream().noneMatch(q->(q.owner.equals("�����") && (q.id!=5&&q.id!=54))))
                {
                    //������ ������� 11 (������� �������)
                    Game.player.quests.add(Quest.get(11));
                    //��������� ������� ��� ����� ������
                    Dialogue.hacker.get(4).responses.add(new Dialogue.Response("�����..",0,1111_4));
                    //������� ��������������� ������
                    Dialogue.current = Dialogue.hacker.get(9);
                    return;
                }
                //���� � ������� ���� ������� �� ������, ������� ��� ��� �� �������� (����� ������ ����� � ������� ��������).
                if(Game.player.quests.stream().anyMatch(q->(q.owner.equals("�����")&& (q.id!=5 && q.id!=54) && !q.completed)))
                {
                    //������� ��������������� ������
                    Dialogue.current = Dialogue.hacker.get(7);
                    return;
                }
                //���� ������ ������� � �������� ����� �� ������� (������� �������) � �� ������� ����� � ��������
                if(Game.player.quests.stream().anyMatch(q->q.id==11&&q.completed) && Game.player.quests.stream().noneMatch(q->q.id==26))
                {
                    //��� ����� 26 (������� � ��������)
                    Game.player.quests.add(Quest.get(26));
                    //������� ��������������� ������
                    Dialogue.current = Dialogue.hacker.get(10);
                    //��������� ������� � ������ � �������� �� �������
                    Dialogue.trap.get(1).responses.add(new Dialogue.Response("��� 8814",0,5555_2));
                    //��������� ������� ��� ����� ������
                    Dialogue.hacker.get(4).responses.add(new Dialogue.Response("�������",0,1111_7));
                    return;
                }
                //���� ������ ������� � �������� ����� � �������� � �� ������� ������ � ��������
                if(Game.player.quests.stream().anyMatch(q->q.id==26&&q.completed) && Game.player.quests.stream().noneMatch(q->q.id==13))
                {
                    //������ ����� � �������� 13 (���������� �������)
                    Game.player.quests.add(Quest.get(13));
                    //������� ������
                    Dialogue.current = Dialogue.hacker.get(11);
                    //��������� ������� � �����
                    GameObject rockSi = GameObject.get(42);
                    rockSi.x=9000;rockSi.y=5000;
                    Level.repo.get(STREET_1).objects.add(rockSi);
                    //��������� ������� �� ����� �������
                    Dialogue.hacker.get(4).responses.add(new Dialogue.Response("������...",0,1111_11));
                }
                //���� ������ �������� ����� � �������� 13 (���������� �������)
                if(Game.player.quests.stream().anyMatch(q->q.id==13&&q.completed) && Game.player.quests.stream().noneMatch(q->q.id==27))
                {
                    //������ ����� 27 (������� �������)
                    Game.player.quests.add(Quest.get(27));
                    //������� ������
                    Dialogue.current = Dialogue.hacker.get(13);
                    //��������� ������� ��� ����� ������
                    Dialogue.hacker.get(4).responses.add(new Dialogue.Response("�������..",0,1111_12));
                }
                //���� ������ �������� ����� � ��������� (27)
                if(Game.player.quests.stream().anyMatch(q->q.id==27&&q.completed) && Game.player.quests.stream().noneMatch(q->q.id==28))
                {
                    //������ ����� 28 (�������� �����)
                    Game.player.quests.add(Quest.get(28));
                    //������� ������
                    Dialogue.current = Dialogue.hacker.get(20);
                    //��������� ������� ������� �� ������� (��� ����� ������)
                    Dialogue.trap.get(1).responses.add(new Dialogue.Response("�����.",0,5555_3));
                }
                //���� ������ �������� ����� � ������� (28)
                if(Game.player.quests.stream().anyMatch(q->q.id==28&&q.completed) && Game.player.quests.stream().noneMatch(q->q.id==29))
                {
                    //������ ����� 29 (���������� ����������)
                    Game.player.quests.add(Quest.get(29));
                    //������� ������
                    Dialogue.current = Dialogue.hacker.get(21);
                    //���� ������ ����������
                    Game.player.items.add(Item.get(76));
                }
            }
        };repo.put(1111,script);

        /** ����� ���������, ������ �� ������ ������ **/
        script = new Script() {
            @Override
            public void execute() {
                //���� ������
                Item flour = null;
                for(var i : Game.player.items) if(i.id==77) flour = i;
                if(flour != null) {
                    //���� �������, �������� ����
                    if(flour.count == 1) Game.player.items.remove(flour);
                    flour.count -= 1;
                    //�������� ������
                    Dialogue.current = Dialogue.hacker.get(3);
                    //��������� 10 ����� (�������� ������)
                    for(var q: Game.player.quests) if(q.id==10) q.completed = true;
                    //������������� ������ ������ ������ ��� ��������
                    Game.hacker.dialogue = 4;
                    //���� ������ ������
                    Game.player.money += 2_000;
                }else{
                    //���� �� ������� ������
                    //������� ��������������� ���������
                    Dialogue.current = Dialogue.hacker.get(2);
                }
            }
        };
        repo.put(1111_2,script);

        /** ����� ������ ������� ������� ������� ���� **/
        script = new Script() {
            @Override
            public void execute() {
                //���� ������ ��� ����������, ��������� ������ � ������� �� �������
                if(expired) {Dialogue.current=null; return;}
                //������ �������
                Game.player.quests.add(Quest.get(5));
                //������� ������ �����
                GameObject pc = GameObject.get(40);
                pc.x = 400; pc.y = 250;
                //� ��������� �� �������
                Level.repo.get(HACKERS_PLACE).objects.add(pc);
                //��������� ������
                Dialogue.current = null;
                //������ �����������
                expired=true;
            }
        }; repo.put(1111_3,script);

        /** ����� ���������, ������ �� ������ ������� � ������... **/
        script = new Script() {
            @Override
            public void execute() {
                //���� �������
                Item carrot = null; for(var i:Game.player.items) if(i.id==56) carrot = i;
                //� ������
                Item lube = null; for(var i:Game.player.items) if(i.id==65) lube = i;
                //���� � �� � �� � �������
                if(carrot != null && lube != null) {
                    //����� ��������
                    for(var q:Game.player.quests)if(q.id==11)q.completed = true;
                    //�������� ������� � ������
                    Game.player.items.remove(carrot); Game.player.items.remove(lube);
                    if(Game.player.equip == carrot || Game.player.equip == lube) Game.player.equip = null;
                    //����� �������� ����������� ������
                    Game.player.money += Item.get(56).price; //�� �������
                    Game.player.money += Item.get(65).price; //� ������
                    //������� ����� ��� ����� ������
                    Dialogue.Response r = Dialogue.hacker.get(4).responses.stream().filter(rr->rr.text.contains("����")).findFirst().orElse(null);
                    if(r!=null) Dialogue.hacker.get(4).responses.remove(r);
                    //������� ��������������� ������
                    Dialogue.current = Dialogue.hacker.get(8);
                }
                else {
                    //���� ������ ���, ������� ������
                    Dialogue d = new Dialogue();
                    d.message = "���, �� ������ �� �����? \n ����, � �� ������ ����, ��� ������ ��� ��..";
                    d.responses = List.of(new Dialogue.Response("�..",0,0));
                    Dialogue.current =d;
                }
            }
        };repo.put(1111_4,script);

        /** ������ ����������-�������
        //���������� ����� ������ ����� ���� ������ **/
        script = new Script() {
            @Override
            public void execute() {
                //���� � ������ ��� �����, �� �� ����� ����� ����
                if(Game.player.items.size()>=ITEMS_CAPACITY) return;
                //������� ������ ����� � ������
                GameObject pc = null; for(var o:Level.repo.get(HACKERS_PLACE).objects) if(o.id==40) pc=o;
                if(pc!=null) Level.repo.get(HACKERS_PLACE).objects.remove(pc);
                //��������� ���� ��� ������� ������� � ���������
                Game.player.items.add(Item.get(72));
            }
        };repo.put(1111_5,script);

        /** ������ ���������� ����������
        //���������� ����� ������ ���������� ���� �� ��������� **/
        script = new Script() {
            @Override
            public void execute() {
                //���������, ���� �� ��
                if(Game.currentLevel.id!=HUB) return; //��������� ���� ����� ������ ����!
                //���� �� ����, ������ ���� ����
                GameObject pc = GameObject.get(41);
                pc.x=400;pc.y=150;
                Level.repo.get(HUB).objects.add(pc);
                //� �������� ��� �� ��� ������
                Game.player.items.remove(Game.player.equip);
                Game.player.equip=null;
            }
        };repo.put(1111_6,script);

        /** ����� ���������, ������ �� ������ ������� **/
        script = new Script() {
            @Override
            public void execute() {
                //�������, ���� �� ������� � ���������
                Item delivery = null;
                for(var i:Game.player.items) if(i.id==73) delivery = i;
                //���� ������� ����
                if(delivery != null) {
                    //�������� �������
                    Game.player.items.remove(delivery);
                    //� �� ��� ����
                    if(Game.player.equip==delivery) Game.player.equip=null;
                    //���� ������
                    Game.player.money += 2_500;
                    //��������� �����
                    for(var q:Game.player.quests)if(q.id==26)q.completed = true;
                    //������� ������
                    Dialogue.current = Dialogue.hacker.get(14);
                    //������� ������ �������
                    //������ ���� ��
                    Dialogue.Response response =
                            Dialogue.hacker.get(4).responses.stream()
                                    .filter(r->r.text.contains("����"))
                                    .findFirst().orElse(null);
                    //���� �������, �������
                    if(response!=null) Dialogue.hacker.get(4).responses.remove(response);
                } else{
                    Dialogue d = new Dialogue();
                    d.message = "�� ��, �������. � ��������... \n �� �� ������?";
                    d.responses = List.of(new Dialogue.Response("�, ��, ��...",0,0));
                    Dialogue.current =d;
                }
            }
        };repo.put(1111_7,script);

        /** ����� ������������ ������� - ��� ��� ������.
        //� ��������� ����������� �������. **/
        script = new Script() {
            @Override
            public void execute() {
                //������������ ������� ��� �����.
                Dialogue.current = Dialogue.hacker.get(15);
                //������ ������, ��������� ������� ��� �������.
                Dialogue.Response response = null;
                for(var r:Dialogue.hacker.get(4).responses) if(r.text.contains("������")) response = r;
                if(response != null) {response.text = "������ ���� ���.";response.script=11119;}
                //��������� ����� ������� (�����������)
                Game.player.quests.add(Quest.get(54));
            }
        }; repo.put(1111_8,script);

        /** ����� �������� �������� � ������� **/
        script = new Script() {
            @Override
            public void execute() {
                //���� ����� � ��������� �������
                Item flour = Game.player.items.stream().filter(i->i.id==1).findFirst().orElse(null);
                //���� �� ���, �������� �� ���� �������
                if(flour==null) {Dialogue.current = Dialogue.hacker.get(16);return;}
                //���� ����, �������� (� �� ���)
                Game.player.items.remove(flour);
                if(Game.player.equip==flour) Game.player.equip=null;
                //���� ������� ������
                int reward = 2_000 * flour.count;
                Game.player.money += reward;
                //������� ���������
                Dialogue d = new Dialogue();
                d.message = "�! �������. ���, ���� "+reward+" �.";
                d.responses = List.of(new Dialogue.Response("���",0,0));
                Dialogue.current = d;
            }
        };repo.put(1111_9,script);

        /** ������ ����� ������� ����� ��� ���������� **/
        script = new Script() {
            @Override
            public void execute() {
                //���� ��� ����� � ��������, �� ���������
                if(Game.player.items.size()>=ITEMS_CAPACITY) return;
                //���� ����� ����, ��������� ������
                GameObject rockSi = Game.currentLevel.objects.stream().filter(i->i.id==42).findFirst().orElse(null);
                if(rockSi!=null) Game.currentLevel.objects.remove(rockSi);
                //� ���� ������ � ������
                Game.player.items.add(Item.get(70));
            }
        }; repo.put(1111_10,script);

        /** ����� ���������, ������ �� ������ ������� **/
        script = new Script() {
            @Override
            public void execute() {
                //���� ������� � ���������
                Item si = Game.player.items.stream().filter(i->i.id==70).findFirst().orElse(null);
                //���� �� �����, ���������� ������� - ��� �� ���
                if(si==null) {Dialogue.current = Dialogue.hacker.get(17);return;}
                //���� ������� �������, �������� ���
                Game.player.items.remove(si); if(Game.player.equip==si) Game.player.equip=null;
                //���� ������ ���������
                Game.player.addItem(Item.get(71));
                //������� ������
                Dialogue.current = Dialogue.hacker.get(12);
                //��������� �����
                for (var q: Game.player.quests) if(q.id==13) q.completed = true;
                //������� ������� ��� ����� ������
                Dialogue.Response response = Dialogue.hacker.get(4).responses.stream()
                        .filter(r->r.text.contains("����")).findFirst().orElse(null);
                if(response!=null) Dialogue.hacker.get(4).responses.remove(response);
            }
        }; repo.put(1111_11,script);

        /** ����� ���������, ������ �� ������ ����������� ����� **/
        script = new Script() {
            @Override
            public void execute() {
                //���� �������� � �������
                Item cards = Game.player.items.stream().filter(i->i.id==74).findFirst().orElse(null);
                //���� �� �������, ��� �� ����, �������� �������
                if(cards==null || cards.count < 10) {Dialogue.current = Dialogue.hacker.get(18);return;}
                //���� �������, ��������
                Game.player.items.remove(cards); if(Game.player.equip==cards) Game.player.equip=null;
                //��������� �����
                for (var q:Game.player.quests) if(q.id==27) q.completed = true;
                //������� ���������
                Dialogue.current = Dialogue.hacker.get(19);
                //������� ������� ��� ����� ������
                Dialogue.Response response = Dialogue.hacker.get(4).responses.stream()
                        .filter(r->r.text.contains("�����")).findFirst().orElse(null);
                if(response!=null) Dialogue.hacker.get(4).responses.remove(response);
            }
        };repo.put(1111_12,script);
        //==========================================================================================//
        /**
         *
         * ������� ����
         *
         */
        /**������ ���� �������� ����� **/
        script = new Script() {
            @Override
            public void execute() {
                //���� ������ �������� �����
                Game.player.quests.add(Quest.get(55));
                //������� �������� �������
                Dialogue.current = Dialogue.hach.get(0);
                //��������� ������� ��� ����� ������
                Dialogue.hach.get(1).responses.add(new Dialogue.Response("������",0,1489));

            }
        }; repo.put(1488,script);

        /** ������ �������� ����� �������� ����� **/
        script = new Script() {
            @Override
            public void execute() {
                //��������, �������� �� ������ �������
                if(true /*�������� ������� ������ */){
                    //��������� ����� ���� ��
                    for(var q:Game.player.quests) if(q.id == 55) q.completed = true;
                    //������� ������� ��� ����� ������ �� �������
                    Dialogue.Response response = Dialogue.hach.get(1).responses.stream()
                            .filter(r->r.text.equals("������")).findFirst().orElse(null);
                    if(response != null) {
                        Dialogue.hach.get(1).responses.remove(response);
                    }
                }
                Dialogue.current = Dialogue.hach.get(3);
            }
        }; repo.put(1489,script);


        /** ������ ������ �������
        // <<��� �� ���� ������>> **/
        script = new Script() {
            @Override
            public void execute() {
                //���� � ������ ��� ������� ����
                if (Game.player.quests.stream().noneMatch(q->(q.owner.equals("�����")&& q.id!=35))) {
                    Game.player.quests.add(Quest.get(9)); //������ ������ �����
                    Dialogue.current = Dialogue.hach.get(5); //������� ��������������� ������
                    //��������� ����� � �������� ������ ������� (��� ����� ������)
                    Dialogue.hach.get(Game.hach.dialogue).responses.add(new Dialogue.Response("���, �����.",0,22221));
                    return;
                }
                //���� ����� ���� ����� ����, �� �� �������� ����
                if(Game.player.quests.stream().anyMatch(q->q.owner.equals("�����")&& !q.completed && q.id!=35))
                {
                    Dialogue.current = Dialogue.hach.get(6);
                    return;
                }
                //���� ����� �������� ������ � ������� � ���� �����
                if(Game.player.quests.stream().anyMatch(q->q.id==9 && q.completed)
                    && Game.player.quests.stream().anyMatch(q->q.id==10 && q.completed))
                {
                    //���� ������ ����� 36 (�������� � ������)
                    Game.player.quests.add(Quest.get(36));
                    //������� ��������������� �������
                    Dialogue.current = Dialogue.hach.get(10);
                    //��������� �������� ������ ���� ��� ����� �������
                    Dialogue.hach.get(Game.hach.dialogue).responses.add(new Dialogue.Response("� �����",0,22223));
                }
            }
        };
        repo.put(2222,script);

        /** �������� ���������� ������� 9 (������� �� �����) **/
        script = new Script() {
            @Override
            public void execute() {
                //��������� ��� ����� ������ ������
                boolean got = Game.player.items.stream().anyMatch(i -> i.id==77);
                //���� ���
                if(!got) {
                    //�������� � ������������� �������� ������po0[[[
                    Dialogue d = new Dialogue();
                    d.message = "����, �� ���, ��� ��� ��� ������, ������ �� ��� �� �� ������?";
                    d.responses = List.of(new Dialogue.Response("��",0,0));
                    Dialogue.current = d;
                    return;
                }
                //���� ��
                //��������� �������
                for(var quest : Game.player.quests) if(quest.id==9) quest.completed = true;
                //������� ������� ��� ����� �������
                Dialogue.hach.get(Game.hach.dialogue).responses
                        .remove(Dialogue.hach.get(Game.hach.dialogue).responses.size()-1);
                //�������� ������
                Dialogue.current = Dialogue.hach.get(7);
                //���� ������� 10 (������� ������)
                Game.player.quests.add(Quest.get(10));
            }

        }; repo.put(2222_1,script);

        /** ������ ���������� ������ ��������� ������ **/
        script = new Script() {
            @Override
            public void execute() {
                //��������� ����� (������ ���������)
                if(!expired) Game.player.quests.add(Quest.get(35));
                //���������� ������� ������
                Dialogue.current = null;
                //������ ������� � ���������� ������� (������ �����������)
                expired = true;
            }
        }; repo.put(2222_5,script);

        /** ����� ����������, ������� � ����� �� ������ ������ ��� ������. **/
        script = new Script() {
            @Override
            public void execute() {
                //���������, ������� �� ������ ����� �����
                boolean bought = Game.player.quests.stream().anyMatch(q->q.id==35&&q.completed);
                //���������, ����� �� �� �� ������
                boolean wears = Game.player.foot != null && Game.player.foot.id == 4;
                //������� ��������������� �������
                if(!bought) Dialogue.current = Dialogue.hach.get(3);
                else if(bought && !wears) Dialogue.current = Dialogue.hach.get(8) ;
                else if(wears) Dialogue.current = Dialogue.hach.get(9);
            }
        }; repo.put(2222_6,script);

        /** ������ ������� ����� ������**/
        script = new Script() {
            @Override
            public void execute() {
                //��������� ���� �� �����
                Item weed = null;
                for(var i : Game.player.items) if(i.id==7) weed = i;
                //���� ����
                if(weed != null) {
                    //�������� �����
                    Game.player.items.remove(weed);
                    if(Game.player.equip==weed) Game.player.equip = null;
                    //���� �����
                    Game.player.money += 1_000 * weed.count;
                    //������� ���������
                    Dialogue d = new Dialogue();
                    d.message = "� ��� ������ �������!!! \n ��� ��� �� ��������� ������� �������. \n ���� " + (weed.count * 1_000 + " �.");
                    d.responses = List.of(new Dialogue.Response("���",0,0));
                    Dialogue.current = d;
                }
                //���� ���
                if (weed == null) {
                    //������� ���������
                    Dialogue d = new Dialogue();
                    d.message = "������, � �� ���� ��������. \n ���� ���������� ������ ������� ���� \n ��� ��� ����. \n �����, �����, ��� ��� ���, �����. \n �� ������� ����� � ������ ��� ��� ������� ������� \n ��������, ���� ��� ���������� ���-�� ������� \n ������, ������� \n ���-�� ������ \n �� ���� ��� ������� ����� �� ���, ���� �����";
                    d.responses = List.of(new Dialogue.Response("�����",0,0));
                    Dialogue.current = d;
                }
            }
        };
        repo.put(22223,script);
        //==========================================================================================//

        /**
         *
         *
         * ������� �������
         *
         */
        /** ������ ������ �������
         * <<��� �� ���� ������>> **/
        script = new Script() {

            @Override
            public void execute() {
                //���� � ������� ��� ������ �������
                if(Game.player.quests.stream().noneMatch(q->q.owner.equals("������")))
                //������� ������ � ������������ ����� ������
                    Dialogue.current = Dialogue.butcher.get(1);
                //���� ������ ���� ����� ����� ����� � �� �����
                if(Game.player.quests.stream().anyMatch(q->q.id==6&&!q.completed))
                    //���������� ��� �� ����
                    Dialogue.current = Dialogue.butcher.get(5);
                //���� ������ ����� �����, � ��� �� ���� ����� "����� ������"
                if(Game.player.quests.stream().anyMatch(q->q.id==6&&q.completed)
                && Game.player.quests.stream().noneMatch(q->q.id==32))
                    //������� ������ � ������������ ����� "����� ������"
                    Dialogue.current = Dialogue.butcher.get(8);
                //���� ������ �������� "����� ������", �� �� ���� "��� ������ 1"
                if(Game.player.quests.stream().noneMatch(q->q.id==12) && Game.player.quests.stream().anyMatch(q->q.id==32&&q.completed))
                    //��������� ����� ����� "��� ������ �1"
                    Dialogue.current = Dialogue.butcher.get(13);
                //���� ������ �������� "��� ������ �1", �� �� ���� �2
                if(Game.player.quests.stream().noneMatch(q->q.id==33) && Game.player.quests.stream().anyMatch(q->q.id==12&&q.completed))
                    Dialogue.current = Dialogue.butcher.get(20);
            }
        }; repo.put(3333,script);
        /** ������ ������ ������� ����� ����� **/
        script = new Script() {
            @Override
            public void execute() {
                //���� �������
                Game.player.quests.add(Quest.get(6));
                //� ��������� ������
                Dialogue.current = null;
            }
        }; repo.put(3333_1,script);

        /** ������ ���� ����� � ����� **/
        script = new Script() {
            @Override
            public void execute() {
                //����� ������� ������ ����
                if(GameTime.getTimeOfTheDay()!=DAY) return;
                //������� ������� � �����
                Game.currentLevel.objects.remove(Game.player);
                //������ � �����
                Game.currentLevel = Level.repo.get(SCHOOL);
                Game.currentLevel.objects.add(Game.player);
                Game.player.x = 200; Game.player.y = 330;
            }
        }; repo.put(3333_2,script);
        /** ������ ���� ������ �� ����� **/
        script = new Script() {
            @Override
            public void execute() {
                //������� ������� �� �����
                Game.currentLevel.objects.remove(Game.player);
                //������ �� �����
                Game.currentLevel = Level.repo.get(STREET_1);
                Game.currentLevel.objects.add(Game.player);
                Game.player.x = 6600; Game.player.y = 8000;
            }
        }; repo.put(3333_4,script);
        /** ������ ����������� **/
        script = new Script() {
            @Override
            public void execute() {
                //������� ������ �����������
                Dialogue d = new Dialogue();
                d.message = "������� �������, �� ������ �� ����������? \n � �� ����������� �� ����� ������! \n � ��� �� ����! \n ����� ������ ������! \n �������";
                d.responses = List.of(new Dialogue.Response("��������..",0,0));
                Dialogue.current = d;
            }
        };repo.put(3333_5,script);
        /** ������ ��������� 1 **/
        script = new Script() {
            @Override
            public void execute() {
                //������� ������ ����� ���������
                Dialogue d = new Dialogue();
                d.message = "����, �� ��? ��� �����...";
                d.responses = List.of(new Dialogue.Response("��� ��������� ���������?",0,3333_7));
                Dialogue.current = d;
            }
        };repo.put(3333_6,script);
        /** ������ ��������� 2 **/
        script = new Script() {
            @Override
            public void execute() {
                //������� ����������� ������ ����
                Dialogue d = new Dialogue();
                d.message = "���, ��� � �� ��� � ���������. \n ������, ����. ������. \n ���� �� ��� �� ���� ����, ������� � ���� ��� ����� \n � ��� ��������� ���� � � ����� �������� ��� ���";
                d.responses = List.of(new Dialogue.Response("�����",0,3333_8));
                Dialogue.current = d;
            }
        };repo.put(3333_7,script);
        /** ������ ��������� 3 **/
        script = new Script() {
            @Override
            public void execute() {
                //���������, ������ �� ������ ����
                Item beer = Game.player.items.stream().filter(i->i.id==84).findFirst().orElse(null);
                //������� ������
                Dialogue d = new Dialogue();
                //���� ���� ����, ������������ ��� ����� ������� � ���������� �������.
                if(beer!=null) {
                    d.message = "�, ����. �����. ����� ��� ����. \n ���, ������, ������. ���� ��� �����, ��� ������ ���������� ������ \n �� ��� ����� ����, �� ���� ����, ���� ���� �������. \n �� ��� �� ����, ������ ����� � ������. \n � ���� ����� ����� ��������, � ����� ���� �� ������� ������ �������.. \n ������ � ���� ����������, ������ ��� �� ��� ������. \n ��, �� �����?";
                    d.responses = List.of(new Dialogue.Response("�����", 0, 3333_9));
                }else{
                    //���� ���� ���, ��� �����
                    d.message = "���������, ���! \n �� �� �� ���� ����, �����? \n ����� ������ �� ����� ��� �������� �������.";
                    d.responses = List.of(new Dialogue.Response("�����", 0, 0));
                }
                Dialogue.current = d;
            }
        };repo.put(3333_8,script);
        /** ������ ��������� 4, ������� �� �������� � ������ �������� **/
        script = new Script() {
            @Override
            public void execute() {
                //������� ������� � ������
                Game.currentLevel.objects.remove(Game.player);
                //� ����������
                GameObject scholar = Game.currentLevel.objects.stream().filter(o->o.id==3333_2).findFirst().orElse(null);
                if(scholar!=null) Game.currentLevel.objects.remove(scholar);
                //������ � ������ ������� �����
                Game.currentLevel = Level.repo.get(STREET_1);
                //� ��������� ���� ����� �����
                Game.currentLevel.objects.add(Game.player);
                Game.currentLevel.objects.add(scholar);
                Game.player.x = 4500; Game.player.y = 3100;
                scholar.x = 4500; scholar.y = 3100;
                //� �������� �������
                Game.girl.x = 4400; Game.girl.y = 3200;
                Level.repo.get(STREET_1).objects.add(Game.girl);
                GameObject guy = GameObject.get(4444_1); guy.x = 4350; guy.y = 3150; Level.repo.get(STREET_1).objects.add(guy);
                guy = GameObject.get(4444_2); guy.x = 4250; guy.y = 3100; Level.repo.get(STREET_1).objects.add(guy);
                //������� ����� ������.
                Dialogue d = new Dialogue();
                d.message = "�� ���. ������ ��� �� ����� ����� ���������� ������ ��� ���. \n �� ��� ��� ��� ���� ��������� �����. \n ������, ������, �, ��� - ����. \n �����, �����, � ������� �� �����. \n � �� ������� ���������";
                d.responses = List.of(new Dialogue.Response("�����",0,0));
                Dialogue.current = d;
            }
        }; repo.put(3333_9,script);
        /** ����� � ����� � ����� / �� ������ **/
        script = new Script() {
            @Override
            public void execute() {
                if(Game.player.y > 250) Game.player.y = 150;
                else Game.player.y = 320;
            }
        }; repo.put(3333_10,script);
        /** ������ ���������� �� ����� ������ **/
        script = new Script() {
            @Override
            public void execute() {
                //������ �����
                Game.player.quests.add(Quest.get(32));
                //��������� ��������������� ����� �� �������
                GameObject door = GameObject.get(60);
                door.x = 400; door.y = 190;
                Level.repo.get(BUILDING_4).objects.add(door);
                //��������� ������� ��� ����� ������
                Dialogue.butcher.get(6).responses
                        .add(new Dialogue.Response("���������...",0,3333_15));
                //��������� ������
                Dialogue.current = null;
            }
        }; repo.put(3333_11,script);
        /** ������ ����� �������� ������ **/
        script = new Script() {
            @Override
            public void execute() {
                //��������� � �� �� ����� ������ ������
                boolean goodTime = GameTime.getString().contains(": 6 h") || GameTime.getString().contains(": 7 h");
                //���� ������ ������ � ������ �����
                if(goodTime){
                    //���������� �������� �����
                    Dialogue d = new Dialogue();
                    d.message = "����� �������. ����������� ��������?";
                    d.responses = List.of(new Dialogue.Response("��",0,3333_13), new Dialogue.Response("���",0,0));
                    Dialogue.current = d;
                } else{ //� ��������� ������
                    //������� �������� ������ ��������
                    Dialogue d = new Dialogue();
                    d.message = "�����, �� ��� �����? \n ���� � �������� ���������? \n � �� �������� �����. \n � ������ ������� ������";
                    d.responses = List.of(new Dialogue.Response("�����",0,0),new Dialogue.Response("�������� �����",0,3333_14));
                    Dialogue.current = d;
                }
            }
        }; repo.put(3333_12,script);
        /** ������ ����������� ����� **/
        script = new Script() {
            @Override
            public void execute() {
                //��������� ������� ���� � ��������
                Game.currentLevel.noise += 200;
                //���������, ���� �� � ������� ���
                if(Game.player.items.stream().anyMatch(i->i.id==85)){
                    //������� ������� � ��������
                    Game.currentLevel.objects.remove(Game.player);
                    Game.currentLevel = Level.repo.get(PACAN_FLAT);
                    Game.currentLevel.objects.add(Game.player);
                    Game.player.x = 200; Game.player.y = 200;
                }else {
                    //������� ���������, ��� �� ������� �������� �����
                    Dialogue d = new Dialogue();
                    d.message = "�� ������� �������� ����� \n ������ ��� � �� �������. \n ��������, ��������� �����-�� ����������";
                    d.responses = List.of(new Dialogue.Response("�����",0,0));
                    Dialogue.current = d;
                }
            }
        };repo.put(3333_13,script);
        /** ������ �������� � �������� ������ ��������� � ������ **/
        script = new Script() {
            @Override
            public void execute() {
                //�������� �������
                Game.player.wanted += 200;
                //������� ������
                Dialogue d = new Dialogue();
                d.message = "�� ���, �����, �� ��� ����������. \n � ����� � �������!";
                d.responses = List.of(new Dialogue.Response("�����",0,0));
                Dialogue.current = d;
            }
        }; repo.put(3333_14,script);
        /** ������ ����� ����� "����� ������" **/
        script = new Script() {
            @Override
            public void execute() {
                //���������, ������ �� ������ ������
                if(Game.player.items.stream().anyMatch(i->i.id==85)){
                    //���� ������, ��������� �����
                    for(var q:Game.player.quests) if(q.id==32) q.completed = true;
                    //���� �������
                    Game.player.money += 10_000;
                    //������� ������
                    Dialogue.current = Dialogue.butcher.get(12);
                    //�������� ������
                    var pacan = Game.player.items.stream().filter(i->i.id==85).findFirst().orElse(null);
                    if(pacan!=null) Game.currentLevel.objects.remove(pacan);
                    if(Game.player.equip == pacan) Game.player.equip = null;
                    //������� ������� ��� ����� ������
                    Dialogue.Response r = Dialogue.butcher.get(6).responses
                            .stream().filter(re->re.text.contains("�����")).findFirst().orElse(null);
                    if(r!=null) Dialogue.butcher.get(6).responses.remove(r);
                }else{
                    //� ��������� ������ ���������� � �������
                    Dialogue.current = Dialogue.butcher.get(11);
                }
            }
        }; repo.put(3333_15,script);
        /** ������ ����� ������� "��� ������ 1" **/
        script = new Script() {
            @Override
            public void execute() {
                //������ �������
                Game.player.quests.add(Quest.get(12));
                //��������� ������� ��� ����� �������
                Dialogue.butcher.get(6).responses
                        .add(new Dialogue.Response("������ �������...",0,3333_17));
                //��������� ������� ������
                Dialogue.current = null;

            }
        }; repo.put(3333_16,script);
        /** ������ ����� ������� "��� ������ 1" **/
        script = new Script() {
            @Override
            public void execute() {
                //���������, ���� �� ������ �������
                if(Level.repo.get(GROCERY).objects.stream().noneMatch(o->o.id==3333_4)
                && Level.repo.get(STREET_1).objects.stream().noneMatch(o->o.id==3333_4)){
                    //���� ����, ��������� �������
                    for(var q:Game.player.quests)if(q.id==12)q.completed=true;
                    //���� �������
                    Game.player.money += 15_000;
                    //������� ������
                    Dialogue.current = Dialogue.butcher.get(18);
                    //������� ������� ��� ����� ������
                    Dialogue.Response response =
                            Dialogue.butcher.get(6).responses.stream()
                                    .filter(r->r.text.contains("����")).findFirst().orElse(null);
                }else{
                    //� ��������� ������ ���������� � �������
                    Dialogue.current = Dialogue.butcher.get(19);
                }
            }
        }; repo.put(3333_17,script);
        /** ������ ���������� ������������� �������� **/
        script = new Script() {
            @Override
            public void execute() {
                //���������, ���� �� � ������� ��������
                var smokes = Game.player.items.stream()
                        .filter(i->i.id==66).findFirst().orElse(null);
                //���� ���� ��������, � ���� ����� "��� ������ �1"
                if(smokes!=null && Game.player.quests.stream().anyMatch(q->q.id==12)){
                    //�������� ����
                    smokes.count -=1; if(smokes.count<=0){ Game.player.items.remove(smokes);if(Game.player.equip==smokes)Game.player.equip = null; }
                    //���������� ������������� � ������� �� �����
                    var merch = Game.currentLevel.objects.stream()
                            .filter(o->o.id==3333_4).findFirst().orElse(null);
                    Game.currentLevel.objects.remove(Game.player);
                    if(merch!=null) {
                        Game.currentLevel.objects.remove(merch);
                        merch.x = 5900; merch.y = 2100;
                        Level.repo.get(STREET_1).objects.add(merch);
                    }
                    Game.currentLevel = Level.repo.get(STREET_1);
                    Game.currentLevel.objects.add(Game.player);
                    Game.player.x = 5850; Game.player.y = 2100;
                    //������� ������
                    Dialogue d = new Dialogue();
                    d.message = "����, ��� �� �� ��� ����� ������� \n � �������� ������ ����������...";
                    d.responses = List.of(new Dialogue.Response(" ... ",0,0));
                    Dialogue.current = d;

                }else{
                    //���� ���, ������� ������
                    Dialogue d = new Dialogue();
                    d.message = "����� ��������? �� ��������? � �������";
                    d.responses = List.of(new Dialogue.Response("�����",0,0));
                    Dialogue.current = d;

                }

            }
        };repo.put(3333_18,script);
        /** ������ ����� ������� "��� ������ �2" **/
        script = new Script() {
            @Override
            public void execute() {
                //������ �����
                Game.player.quests.add(Quest.get(33));
                //��������� ������� ��� ����� ������
                Dialogue.butcher.get(6).responses
                        .add(new Dialogue.Response("������..",0,3333_20));
                //��������� ������
                Dialogue.current = null;
            }
        }; repo.put(3333_19,script);
        /** ������ ����� ������� "��� ������ �2" **/
        script = new Script() {
            @Override
            public void execute() {
                //��������� ��� �� ����� ��� ������ ��������
                if(Level.repo.get(STREET_1).objects.stream().noneMatch(o->o.id==3333_5))
                {
                    //���� ��� ��, ��������� �������
                    for(var q: Game.player.quests) if(q.id==33) q.completed = true;
                    //���� �������
                    Game.player.money += 10_000;
                    //������� ������
                    Dialogue.current = Dialogue.butcher.get(0);
                    //������� ������� ��� ����� ������
                    Dialogue.Response response =
                            Dialogue.butcher.get(6).responses
                            .stream()
                            .filter(r -> r.text.equals("������.."))
                            .findFirst()
                            .orElse(null);
                    if(response!= null) Dialogue.butcher.get(6).responses.remove(response);
                }
                //���� ���, ���������� � �������
                Dialogue.current = Dialogue.butcher.get(0);
            }
        };repo.put(3333_20,script);
        /** ������ ����� ����� "��� ������ �3" **/
        script = new Script() {
            @Override
            public void execute() {
                //TODO
                //���� �������
                //��������� ������� ��� ����� ������
            }
        };repo.put(3333_21,script);
        /** ������ ����� ����� "��� ������ �3" **/
        script = new Script() {
            @Override
            public void execute() {
                //TODO
                //��������� ���������� �������
                //���� ��, ��������� �����
                //���� �������
                //������� ������
                //��������� ������ ����
                //������� ������� ��� ����� ������
                //���� ���, ���������� � �������.
            }
        };repo.put(3333_22,script);
        //==========================================================================================//
        /**
         *
         *
         * ������ �������
         *
         *
         */
        /** ������� ������ �������
         * <<��� �� ���� ������>>**/
        script = new Script() {
            @Override
            public void execute() {
                //���� � ������� ���� ������������� �������, ���������� ��� �� ����
                if(Game.player.quests.stream().anyMatch(q->q.owner.equals("��������")&&!q.completed))
                    Dialogue.current = Dialogue.girl.get(4);
            }
        };repo.put(4444,script);
        /** ������ ������ ������� � ���� **/
        script = new Script() {
            @Override
            public void execute() {
                //��������� ������� ����� �����
                for(var q:Game.player.quests) if(q.id==6)q.completed=true;
                //������� ��������� ������
                Dialogue.current = Dialogue.girl.get(2);
            }
        };repo.put(4444_3,script);
        /** ������ ����������� ����� �� ���� **/
        script = new Script() {
            @Override
            public void execute() {
                //������� ����� � ����� � ���������� � ��������
                Level street = Level.repo.get(STREET_1);//�����
                Level flat = Level.repo.get(TRAP_HOUSE);
                street.objects.remove(Game.player); //������
                flat.objects.add(Game.player);
                Game.player.x = 400; Game.player.y = 600;
                street.objects.remove(Game.girl);//�������
                flat.objects.add(Game.girl);
                Game.girl.x = 450; Game.girl.y = 600;
                GameObject guy = street.objects.stream().filter(o->o.id==4444_1).findFirst().orElse(null);
                if(guy!=null){ Game.currentLevel.objects.remove(guy);//�����
                    flat.objects.add(guy); guy.x = 500; guy.y = 600; }
                guy = street.objects.stream().filter(o->o.id==4444_2).findFirst().orElse(null);
                if(guy!=null) {
                    Game.currentLevel.objects.remove(guy);//������
                    flat.objects.add(guy); guy.x = 500; guy.y = 600;
                }
                Game.currentLevel = flat;//����������� ������� �� ��������
                Dialogue.current = Dialogue.girl.get(3);//������� ������
            }
        };repo.put(4444_5,script);
        /** ������ ����������� ����� ������ **/
        script = new Script() {
            @Override
            public void execute() {
                //��������� �����
                Game.player.quests.add(Quest.get(8));
                //��������� ������� ������
                Dialogue.current = null;
                //������ ������� �������� ������

            }
        };repo.put(4444_6,script);
        /** ������ ����� **/
        script = new Script() {
            @Override
            public void execute() {
                //���� ������ ��� �� ������� ������� ������� ��������
                if(Game.player.quests.stream().noneMatch(q->q.id==8))
                {
                    //������� "�������"
                    Dialogue d = new Dialogue();
                    d.message = "����, �����, �� ��� ��������� ������. \n �� �� ����?... ���, �...";
                    d.responses = List.of(new Dialogue.Response("�����",0,0));
                    Dialogue.current = d;
                }
            }
        };repo.put(4444_1,script);
        /** **/

        //==========================================================================================//
        /**
         *
         * ������� ������� �� �������
         *
         */
        /** ������� �� ������� ���� �������
        // <<��� �� ���� ������>> **/
        script = new Script() {
            @Override
            public void execute() {
                //���������, ���� �� � ������� ������� �� ������� �� ������� (����� ������ ������)
                if(Game.player.quests.stream().noneMatch(q->q.id!=31 && q.owner.equals("����")))
                {
                    //���� ������� (����� ������ ������) ���, ������ ������� 30 (������ �����)
                    Game.player.quests.add(Quest.get(30));
                    //������� ������
                    Dialogue.current = Dialogue.trap.get(7);
                    //TODO �������� ����� � ��������-������� ��������
                    return;
                }
                //���� ���� ������������� (����� ������ ������) �������
                if(Game.player.quests.stream().anyMatch(q->q.owner.equals("����") && !q.completed && q.id!=31))
                    //������� ������
                    Dialogue.current = Dialogue.trap.get(8);
            }
            //���������, ���� �� � ������� ������������ (����� ������ ������)

        };repo.put(5555,script);

        /** ������� �� ������� ��������� ����� ��������� ������� **/
        script = new Script() {
            @Override
            public void execute() {
                //������� �����
                GameObject container = null;//Level.repo.get(WILDBERRIES).objects.stream().filter(o->o.id==1).findFirst().orElse(null);
                //���� �� ����� ����� ��� ��� �����, �������� �������
                if(container == null || container.items.isEmpty()) Dialogue.current = Dialogue.trap.get(3);
                else{
                    //���� ������� ����, �� � ������� ��� �����, �������� ��� �� ����
                    if(Game.player.items.size()>=ITEMS_CAPACITY) {Dialogue.current = Dialogue.trap.get(4); return;}
                    //����� �������
                    Item item = container.items.get(0);
                    //���� ������
                    Game.player.addItem(item);
                    //��������� �� ������
                    container.items.remove(item);
                    //�������� ������ � ������ �������
                    Dialogue d = new Dialogue();
                    d.message = "��� ���� �������. ��� - "+item.name+". "+item.description+". ��������� �����������!";
                }
            }
        };repo.put(5555_1,script);

        /** ������� �� ������� ������ ����� ������������� **/
        script = new Script() {
            @Override
            public void execute() {
                //��������� ��� ����� �� ����������
                if(Game.player.items.size()>=ITEMS_CAPACITY) {Dialogue.current = Dialogue.trap.get(4); return;}
                //������ �������
                Game.player.addItem(Item.get(73));
                //������� ���������
                Dialogue d = new Dialogue(); d.message = "����������. ������� �� ������ 8814";
                d.responses = List.of(new Dialogue.Response("���",0,0));
                Dialogue.current = d;
                //������� ������� � 8814 ������ �� �������
                Dialogue.Response r = null; //���� ���� �������
                for(var rr : Dialogue.trap.get(1).responses)  if(rr.text.contains("8814")) r =rr;
                //� �������
                if(r!=null) Dialogue.trap.get(1).responses.remove(r);
            }
        }; repo.put(5555_2,script);

        /** ������� �� ������� �������� �� ������ ��� ����� **/
        script = new Script() {
            @Override
            public void execute() {
                //���� �����
                Item flowers = Game.player.items.stream().filter(i->i.id==75).findFirst().orElse(null);
                //��� ������ - ����� ��
                if(flowers == null){ Dialogue.current = Dialogue.trap.get(5); return;}
                //���� ����� - ����
                Game.player.items.remove(flowers); if(Game.player.equip==flowers) Game.player.equip = null;
                //������� ������� � ������� ������
                Dialogue.Response r = null; //���� ���� �������
                for(var rr : Dialogue.trap.get(1).responses)  if(rr.text.contains("���")) r =rr;
                //� �������
                if(r!=null) Dialogue.trap.get(1).responses.remove(r);
                //��������� �����
                for (var q:Game.player.quests) if(q.id==28) q.completed = true;
                //� ������� "�������"
                Dialogue.current = Dialogue.trap.get(6);
                //���� ������� ����� (������ ������)
                Game.player.quests.add(Quest.get(31));
                //� ���� ����� ������ ������
                //todo
                //��������� ������� �� ����� ������ 31 (������ ������)
                Dialogue.trap.get(1).responses.add(new Dialogue.Response("�����",0,5555_4));
            }
        };repo.put(5555_3,script);

        //TODO
        //������� �� ������� ��������� ��� ������ ����� �����
        script = new Script() {
            @Override
            public void execute() {

            }
        };repo.put(5555_4,script);
        //==========================================================================================//
        /**
         *
         * ������� ��������
         *
         */
        /** ������ ������ �������
        // <<��� �� ���� ������>> **/
        script = new Script() {
            @Override
            public void execute() {
                //���� � ������� ��� ������� ��������
                if(Game.player.quests.stream().noneMatch(q->q.owner.equals("�������")))
                {
                    //���� ����� 43 (������ � ������)
                    Game.player.quests.add(Quest.get(43));
                    //���� ����� 18 (����� ����� � ���������)
                    Game.player.quests.add(Quest.get(18));
                    //������� ������
                    Dialogue.current = Dialogue.nurse.get(2);
                    //���������� �������� �� ����� �������
                    Game.currentLevel.objects.remove(Game.nurse);
                    Game.nurse.x = 6150; Game.nurse.y = 3500;
                    Level.repo.get(STREET_1).objects.add(Game.nurse);
                    //� ����� � ��� ��
                    Level.repo.get(STREET_1).objects.add(Game.mechanic);
                    Game.mechanic.x = 6220; Game.mechanic.y = 3500;
                    return;
                }
                //���� ������ ����, �� �� �������� ������� ������ � ������
                if(Game.player.quests.stream().anyMatch(q->q.id==43 && !q.completed))
                {
                    Dialogue.current = Dialogue.nurse.get(3);
                    return;
                }
                //���� ������ �������� ������� � ������ � �� ���� ������� � ������ ������� (��������)
                if(Game.player.quests.stream().anyMatch(q->q.id==43 && q.completed)
                && Game.player.quests.stream().noneMatch(q->q.id==44))
                {
                    //������ �����
                    Game.player.quests.add(Quest.get(44));
                    //������� ������
                    Dialogue.current = Dialogue.nurse.get(4);
                    //���� ������� ����-�����
                    Game.player.addItem(Item.get(79));
                    //� �����
                    Game.player.addItem(Item.get(82));
                    return;
                }
                //���� ������ ���� ����� ������� ������, �� ��� �� ������
                if(Game.player.quests.stream().anyMatch(q->q.id==44 && !q.completed))
                {
                    //������� ������
                    Dialogue.current = Dialogue.nurse.get(5);
                    return;
                }
                //���� ������ �������� ������� � ������ ������� (��������) � �� ������� ������� � �������
                if(Game.player.quests.stream().anyMatch(q->q.id==44 && q.completed)
                && Game.player.quests.stream().noneMatch(q->q.id==45))
                {
                    //������ �����
                    Game.player.quests.add(Quest.get(45));
                    //������� ���������
                    Dialogue.current = Dialogue.nurse.get(6);
                    //��������� ������� ��� ����� ������
                    Dialogue.nurse.get(1).responses.add(new Dialogue.Response("������",0,7777_11));
                    return;
                }
                //���� ������ ���� ����� � �������
                if(Game.player.quests.stream().anyMatch(q->q.id==45))
                    Dialogue.current = Dialogue.nurse.get(7); //������� �������
            }
        }; repo.put(7777,script);
        /** ������ ������ �� �������� **/
        script = new Script() {
            @Override
            public void execute() {
                //������ ������������� ������ ������ (���� �������� ������)
                Rectangle r = new Rectangle(Game.player.x-100,Game.player.y-100,200,200);
                //���������, �������� �� � ���� �������� ������ ����� � �������
                for(var o: Game.currentLevel.objects) if(o.id==44 && r.intersects(o.hitBox)) {
                    //���� ������ ����� ������ � ������, ��������� ����� � ������ ������
                    for(var q: Game.player.quests) if(q.id==44) q.completed = true;
                    //�������� �����
                    Item syringe = Game.player.items.stream().filter(i->i.id==81).findFirst().orElse(null);
                    if(syringe != null) Game.player.items.remove(syringe);
                    if(Game.player.equip==syringe) Game.player.equip = null;
                }
            }
        }; repo.put(7777_1,script);
        /** ������ �������� - ������� **/
        script = new Script() {
            @Override
            public void execute() {
                //���� ������� ������, ����� ������� �� ���������.
                if(Game.player.items.size()>=ITEMS_CAPACITY) return;
                //������� ��������
                GameObject ride = Game.currentLevel.objects.stream().filter(i->i.id==47).findFirst().orElse(null);
                //� ������� � ������
                if(ride != null) Game.currentLevel.objects.remove(ride);
                //��������� �������� ������� � ��������� ��� �������
                Game.player.addItem(Item.get(83));
            }
        }; repo.put(7777_2,script);
        /** ������ ���� ����� � �������� **/
        script = new Script() {
            @Override
            public void execute() {
                //���� ������ �� ����/�����, �������� �������.
                if(GameTime.getTimeOfTheDay()!=DAY && GameTime.getTimeOfTheDay()!=SUNSET)
                    //��� ����-����� ������ �� ����� �����
                    if(Game.player.items.stream().noneMatch(i->i.id!=79)) return;
                //����� �������� ������� ������
                Game.currentLevel.objects.remove(Game.player); Game.currentLevel = Level.repo.get(HOSPITAL);
                Game.player.x = 100; Game.player.y = 850; //� �������� � �����
                Game.currentLevel.objects.add(Game.player);
                //���� ������ ������ ����� ��� ������, ���� �������
                if((GameTime.getTimeOfTheDay()!=DAY && GameTime.getTimeOfTheDay()!=SUNSET)
                && (Game.player.torso == null || Game.player.torso.id!=82)){
                    //������� 5 �����
                    for (int i = 0; i < 5; i++) {
                        //������� ����
                        GameObject cop = GameObject.get(7);
                        //�������� ��� � ��������
                        cop.x = 100 + (i*60); cop.y = 850;
                        Level.repo.get(HOSPITAL).objects.add(cop);
                    }
                    //� ��������� 200 ����� ����������
                    Game.player.wanted += 200;
                }
            }
        }; repo.put(7777_3,script);
        /** ������ ���� ������ �� �������� **/
        script = new Script() {
            @Override
            public void execute() {
                //������ ������� �� ����� � ����� ��������
                Game.currentLevel.objects.remove(Game.player);//������ � �������� ������
                Game.player.y = 8_000; Game.player.x = 7_600;//��������� ���������� ����� � ��������
                Game.currentLevel = Level.repo.get(STREET_1);//�������� � ������ ������� - �����
                Game.currentLevel.objects.add(Game.player);//��������� ������ �� �����
            }
        }; repo.put(7777_4,script);
        /** ������ ���� ������ �� �������� � 1-�� ����� �������� **/
        script = new Script() {
            @Override
            public void execute() {
                //��� ����-����� ���� �� �������
                if(Game.player.items.stream().noneMatch(i->i.id==79)) return;
                //����-����� �������� ������ �����
                if(GameTime.getTimeOfTheDay()==DAY||GameTime.getTimeOfTheDay()==SUNSET) return;
                //���� ������ �� 1-� �����, ������ ��� �� ��������
                if(Game.player.y > 720) Game.player.y = 650;
                //���� ������ �� ��������, ������ ��� �� 1-� ����
                else Game.player.y = 800;
            }
        }; repo.put(7777_5,script);
        /** ������ ���� ����� � �������� �� 2-� ���� �������� **/
        script = new Script() {
            @Override
            public void execute() {
                //��� ����-����� ���� �� �������
                if(Game.player.items.stream().noneMatch(i->i.id==79)) return;
                //���� ������ �� 2-� �����, ������ ��� �� ��������
                if(Game.player.x < 780) Game.player.x = 830;
                //���� ������ �� ��������, ������ ��� �� 2-� ����
                else Game.player.x = 700;
            }
        }; repo.put(7777_6,script);
        /** ������ ���� ����� ������ � �������� **/
        script = new Script() {
            @Override
            public void execute() {
                //��� ����-����� ���� �� �������
                if(Game.player.items.stream().noneMatch(i->i.id==79)) return;
                //���� ������ �� 2-� �����, ������ ��� � ������
                if(Game.player.y < 480) Game.player.y = 540;
                //���� ������ � ������, ������ ��� �� 2-� ����
                else Game.player.y = 380;
            }
        }; repo.put(7777_8,script);
        /** ������ �������� - �������� ��� ������������� �� ���� **/
        script = new Script() {
            @Override
            public void execute() {
                //�������� ����� ��������� ������ ����
                if(Game.currentLevel.id != HUB) return;
                //�������� �������
                Game.player.items.remove(Game.player.equip);
                Game.player.equip = null;
                //��������� ����� � ��������
                for(var q:Game.player.quests) if(q.id==14) q.completed = true;
                //������ ������� ����
                var ride = GameObject.get(47); ride.scriptId = 0;
                ride.x = 250; ride.y = 180; Level.repo.get(HUB).objects.add(ride);
            }
        }; repo.put(7777_9,script);
        /** ������ ����� �� ����� � �������� **/
        script = new Script() {
            @Override
            public void execute() {
                //������ ������������� ������ ������ (���� �������� �����)
                Rectangle r = new Rectangle(Game.player.x-100,Game.player.y-100,200,200);
                //���������, �������� �� �������� ���� � ���� �������� �����
                GameObject locker = Game.currentLevel.objects.stream()
                        .filter(i->i.id==45 && i.hitBox.intersects(r)).findFirst().orElse(null);
                //���� ����� ����� ���, ������ �� ������
                if(locker == null) return;
                //������� ����� �������� ����
                GameObject openedLocker = GameObject.get(46);
                //������� �������� ���� � ������
                Level.repo.get(HOSPITAL).objects.remove(locker);
                //��������� �� ��� ����� ��������
                openedLocker.x = locker.x; openedLocker.y = locker.y;
                Level.repo.get(HOSPITAL).objects.add(openedLocker);
                //��������� � ���� ��������� //TODO (� ����� ��-������ ���)
                openedLocker.addItem(Item.get(81));
            }
        }; repo.put(7777_10,script);
        /** ������ ����� ������ � ������� **/
        script = new Script() {
            @Override
            public void execute() {
                //������� �������
                Dialogue.current = Dialogue.nurse.get(8);
            }
        }; repo.put(7777_11,script);
        //==========================================================================================//
        /**
         *
         * ������� ����� �������� (��������)
         *
         */
        //������� ������ ����� �������
        //<<��� �� ���� ������>>
        script = new Script() {
            @Override
            public void execute() {
                //���������, ���� � ������� ��� �� ������ ������� (����� ���������)
                //���������� ����� � ���������
                if(Game.player.quests.stream().noneMatch(q->q.owner.equals("�����") && q.id!=46))
                    Dialogue.current = Dialogue.mech.get(11);
                //���� ������ �������� ������� � ���������, ���������� ����� ������� � �������
                if(Game.player.quests.stream().anyMatch(q->q.id==47&&q.completed)
                && Game.player.quests.stream().noneMatch(q->q.id==48))
                    Dialogue.current = Dialogue.mech.get(0);
                //���� ������ �������� ������� � �������, ���������� ����� ������� � ��������
                if(Game.player.quests.stream().anyMatch(q->q.id==48&&q.completed)
                        && Game.player.quests.stream().noneMatch(q->q.id==49))
                    Dialogue.current = Dialogue.mech.get(0);
                //���� ������ �������� ������� � ��������, ���������� ����� ������� � ������
                if(Game.player.quests.stream().anyMatch(q->q.id==49&&q.completed)
                        && Game.player.quests.stream().noneMatch(q->q.id==50))
                    Dialogue.current = Dialogue.mech.get(0);
            }
        }; repo.put(8888,script);
        /** ������ ���� �������� � ��������� **/
        script = new Script() {
            @Override
            public void execute() {
                //������ �������
                Game.player.drunk += 15.0;
                //�������� �����
                GameTime.forward(HOUR_LENGTH); //1 ���
            }
        }; repo.put(8888_1,script);

        /** ������� ������ ������� 46 (����� ���������) **/
        script = new Script() {
            @Override
            public void execute() {
                //������ �����
                Game.player.quests.add(Quest.get(46));
                //���������� �������� � ��������� � ��������
                Level.repo.get(STREET_1).objects.remove(Game.nurse);
                Level.repo.get(STREET_1).objects.remove(Game.mechanic);
                Game.mechanic.x=150;Game.mechanic.y=200;
                Game.nurse.x=300;Game.nurse.y=200;
                Level.repo.get(MECH_FLAT).objects.add(Game.nurse);
                Level.repo.get(MECH_FLAT).objects.add(Game.mechanic);
                //������� ������ �������� �� ������ (��� ����� ������)
                Game.mechanic.dialogue = 7;
            }
        }; repo.put(8888_2,script);

        /** ������� ���������, ������ �� ������ ����� **/
        script = new Script() {
            @Override
            public void execute() {
                //���� �� ������, ���������� ������
                if(Game.player.items.stream().noneMatch(i->i.id==78))
                {Dialogue.current = Dialogue.mech.get(8); return;}
                //���� ������, ��������� ������ �������� � �������� (����� ����� � ������ � ������)
                for(var q:Game.player.quests) if(q.id==46 || q.id==43) q.completed = true;
                //� �������� �����
                Item vodka = Game.player.items.stream().filter(i->i.id==78).findFirst().orElse(null);
                vodka.count-=1;if(vodka.count<=0)Game.player.items.remove(vodka);
                if(Game.player.equip==vodka) Game.player.equip = null;
                //������� ������
                Dialogue.current = Dialogue.mech.get(9);
                //������ ������� ��������� ������
                Game.mechanic.dialogue = 10;
            }
        }; repo.put(8888_3,script);
        /** ������ ����������� ������ � ����� � �������� **/
        script = new Script() {
            @Override
            public void execute() {
                //���� �� � �������� ��������
                if(Game.currentLevel.id == MECH_FLAT) {
                    //���������� ������� �� �������� � �����
                    Level.repo.get(MECH_FLAT).objects.remove(Game.mechanic);
                    Level.repo.get(GARAGE).objects.add(Game.mechanic);
                    //���������� ������� �� �������� � �����
                    Level.repo.get(MECH_FLAT).objects.remove(Game.player);
                    Level.repo.get(GARAGE).objects.add(Game.player);
                    Game.currentLevel = Level.repo.get(GARAGE);
                    //������� ����������� �������
                    Dialogue.current = Dialogue.mech.get(12);
                    //��������� ����� � ����� ��� ����������� ���������
                    var door = GameObject.get(70);
                    door.x = 6200; door.y = 3370;
                    Level.repo.get(STREET_1).objects.add(door);
                }else{//���� ���, �� �� ��� � ������
                    //����� ������� ������ ������
                    Dialogue.current = Dialogue.mech.get(13);
                }
            }
        }; repo.put(8888_4,script);
        /** ������ ����� ����� � ��������� **/
        script = new Script() {
            @Override
            public void execute() {
                //���� �����
                Game.player.quests.add(Quest.get(47));
                //������� ������
                Dialogue.current = null;
                //�������� ������� ��� �����
                Dialogue.mech.get(Game.mechanic.dialogue).responses.add(
                        new Dialogue.Response("��������...",0,8888_6)
                );
                //���� ��������
                Game.player.addItem(Item.get(91));
            }
        }; repo.put(8888_5,script);
        /** ������ ����� ����� � ��������� **/
        script = new Script() {
            @Override
            public void execute() {
                //��������� ������ �� ������ ��������
                var kanistra = Game.player.items.stream()
                        .filter(i->i.id==91).findFirst().orElse(null);
                if(kanistra == null){
                    //���� �� ������
                    Dialogue.current = Dialogue.mech.get(15);
                    return;
                }
                if(Integer.parseInt(kanistra.description.split(":")[1].split("/")[0])<100) {
                    //���� ������ ������
                    Dialogue.current = Dialogue.mech.get(16);
                    return;
                }
                if(Integer.parseInt(kanistra.description.split(":")[1].split("/")[0])>=100) {
                    //���� ������ ������
                    Dialogue.current = Dialogue.mech.get(17);
                    for(var q:Game.player.quests)if(q.id==47)q.completed=true;
                    //������� ������� ��� ����� ������
                    Dialogue.Response response =
                            Dialogue.mech.get(Game.mechanic.dialogue).responses.stream()
                                    .filter(r->r.text.equals("��������...")).findFirst().orElse(null);
                    if(response!=null) Dialogue.mech.get(Game.mechanic.dialogue).responses.remove(response);
                    //�������� ��������
                    Game.player.items.remove(kanistra);
                    Game.player.equip = null;
                }
            }
        }; repo.put(8888_6,script);
    }

    /** ��� **/
    public static void foo(){}
}
