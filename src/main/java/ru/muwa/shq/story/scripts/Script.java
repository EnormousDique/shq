package ru.muwa.shq.story.scripts;

import ru.muwa.shq.engine.Game;
import ru.muwa.shq.engine.utils.Animation;
import ru.muwa.shq.engine.utils.GameTime;
import ru.muwa.shq.engine.utils.Momma;
import ru.muwa.shq.engine.utils.saveload.Saver;
import ru.muwa.shq.entities.*;
import ru.muwa.shq.story.Dialogue;
import ru.muwa.shq.story.Quest;

import java.awt.*;
import java.lang.invoke.TypeDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.muwa.shq.engine.utils.GameTime.*;
import static ru.muwa.shq.engine.utils.GameTime.TimeOfTheDay.DAY;
import static ru.muwa.shq.engine.utils.GameTime.TimeOfTheDay.SUNSET;
import static ru.muwa.shq.engine.utils.Momma.Status.GO_RIGHT;
import static ru.muwa.shq.engine.utils.Momma.Status.MOM;
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
    public static HashMap<Integer,Long> momDrugs = new HashMap<>(),
                                         momFood = new HashMap<>();
    //���� ��� �������� ������� ��������. ���� - ���������� ���������� ��������, �������� - ������.
    public static HashMap<Coordinates,String> padiquePasswords = new HashMap<>();
    public static int zekKilled = 0;
    public static int gopKilled = 0;
    //������ ��� �������� ����� ������ ��� �������� ��� ����� � ������
    private static List<Item> savedItems = new ArrayList<>();
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
        momFood.put(50,HOUR_LENGTH * 15);//�����
        momFood.put(63,HOUR_LENGTH * 8);//������
        momFood.put(57,HOUR_LENGTH * 15);//�����
        momFood.put(58,HOUR_LENGTH * 25);//������� ������
        momFood.put(59,HOUR_LENGTH * 30);//�����
        momFood.put(60,HOUR_LENGTH * 15);//�������
        momFood.put(61,HOUR_LENGTH * 20);//������� ���
        momFood.put(62,HOUR_LENGTH * 10);//�������� � ������
        momFood.put(64,HOUR_LENGTH * 35);//����
        //����� ��������� �������� ���� � �� ����� �����
        momDrugs.put(46,HOUR_LENGTH * 10);
        momDrugs.put(47,HOUR_LENGTH * 20);
        momDrugs.put(48,HOUR_LENGTH * 30);
        momDrugs.put(49,HOUR_LENGTH * 50);
        //������ �� ���������
        padiquePasswords.put(new Coordinates(550,1400,BUILDING_1),"228");
        padiquePasswords.put(new Coordinates(1185,1400,BUILDING_1),"229");
        padiquePasswords.put(new Coordinates(1820,1400,BUILDING_1),"230");
        padiquePasswords.put(new Coordinates(2455,1400,BUILDING_1),"231");
        padiquePasswords.put(new Coordinates(550,1400, BUILDING_2),"555");
        padiquePasswords.put(new Coordinates(1185,1400, BUILDING_2),"556");
        padiquePasswords.put(new Coordinates(1820,1400, BUILDING_2),"557");
        padiquePasswords.put(new Coordinates(2455,1400, BUILDING_2),"558");
        padiquePasswords.put(new Coordinates(750,2400,BUILDING_5),"417");
        padiquePasswords.put(new Coordinates(1580,2400,BUILDING_5),"418");
        padiquePasswords.put(new Coordinates(2370,2400,BUILDING_5),"419");
        padiquePasswords.put(new Coordinates(3200,2400,BUILDING_5),"420");
        padiquePasswords.put(new Coordinates(750,2400, BUILDING_8),"110");
        padiquePasswords.put(new Coordinates(1580,2400, BUILDING_8),"111");
        padiquePasswords.put(new Coordinates(2370,2400, BUILDING_8),"112");
        padiquePasswords.put(new Coordinates(3200,2400, BUILDING_8),"113");
        padiquePasswords.put(new Coordinates(550,1400, BUILDING_9),"997");
        padiquePasswords.put(new Coordinates(1185,1400, BUILDING_9),"998");
        padiquePasswords.put(new Coordinates(1820,1400, BUILDING_9),"999");
        padiquePasswords.put(new Coordinates(2455,1400, BUILDING_9),"000");
        padiquePasswords.put(new Coordinates(550,1400, BUILDING_13),"144");
        padiquePasswords.put(new Coordinates(1185,1400, BUILDING_13),"145");
        padiquePasswords.put(new Coordinates(1820,1400, BUILDING_13),"146");
        padiquePasswords.put(new Coordinates(2455,1400, BUILDING_13),"147");
        padiquePasswords.put(new Coordinates(550,1400, BUILDING_15),"358");
        padiquePasswords.put(new Coordinates(1185,1400, BUILDING_15),"359");
        padiquePasswords.put(new Coordinates(1820,1400, BUILDING_15),"360");
        padiquePasswords.put(new Coordinates(2455,1400, BUILDING_15),"361");
        padiquePasswords.put(new Coordinates(550,1400,BUILDING_16),"880");
        padiquePasswords.put(new Coordinates(1185,1400,BUILDING_16),"881");
        padiquePasswords.put(new Coordinates(1820,1400,BUILDING_16),"882");
        padiquePasswords.put(new Coordinates(2455,1400,BUILDING_16),"883");
        padiquePasswords.put(new Coordinates(750,2400, BUILDING_17),"665");
        padiquePasswords.put(new Coordinates(1580,2400, BUILDING_17),"666");
        padiquePasswords.put(new Coordinates(2370,2400, BUILDING_17),"667");
        padiquePasswords.put(new Coordinates(3200,2400, BUILDING_17),"668");

        /**
         *
         *
         * �������
         *
        **/

        /** ������ ����������� **/
        Script script = new Script() {
            //�����������
            @Override
            public void execute() {
                if(Momma.status == MOM) {
                    Game.currentLevel.objects.remove(Game.player);
                    Game.currentLevel = Level.repo.get(HOSPITAL);
                    Game.player.x = 1170;
                    Game.player.y = 250;
                    Game.currentLevel.objects.add(Game.player);
                    Game.player.hp = 80;
                    Game.player.crazy += 10;
                    Game.player.hunger = 30;
                    Game.player.thirst = 20;
                    Game.player.sleepy = 35;
                    Game.player.pee = 0;
                    Game.player.poo = 0;
                    GameTime.forward(4 * HOUR_LENGTH);
                    Game.player.wanted = 0;

                    int fine = (int) (Game.player.money * 0.15);
                    Game.player.money -= fine;

                    Dialogue d = new Dialogue();
                    d.message = "������ 4 ���� ����� ��������������, ������ ������ � ���� �� ���������� �����. \n �� ������� ��������� ����: " + fine + "�.";
                    d.responses.add(new Dialogue.Response("�����.", 0, 0));
                    Dialogue.current = d;
                    Dialogue.companion = null;
                    Saver.work();
                }else{
                    Game.switchLevel(HUB);
                    Game.player.x = 200;
                    Game.player.y =200;
                    Game.player.items = new ArrayList<>();
                    Game.player.hp = 100;
                    Game.player.hunger = 0;
                    Game.player.thirst = 0;
                    Game.player.crazy = 0;
                    Game.player.stimulate = 0;
                    Game.player.smoke = 0;
                    Game.player.poo = 0;
                    Game.player.pee = 0;
                    Game.player.sleepy = 0;
                    Game.player.drunk = 0;
                    for(var i: savedItems)
                        Game.player.addItem(new Item(i));
                    Momma.boss.hp = Momma.boss.startHp;
                    Momma.boss.x = 500;
                    Momma.boss.y = 300;
                    Momma.status = GO_RIGHT;
                    var d = new Dialogue();
                    d.message = "������ ������� �������� � ����� �� ��������� ����� \n ����� ��������� ����� �� ������� ��������";
                    d.responses.add(new Dialogue.Response("�����",0,0));
                    Dialogue.current = d;

                }
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
                    Game.player.crazy = Math.max(0, Game.player.crazy - 5);
                    Saver.work();
                }
                //���� 4 ����
                if(Minigame.current.input.startsWith("4")) {
                    Game.player.sleepy -= 50;
                    GameTime.forward(4 * HOUR_LENGTH);
                    Game.player.crazy = Math.max(0, Game.player.crazy - 10);
                    Saver.work();
                }
                //���� 8 �����
                if(Minigame.current.input.startsWith("8")) {
                    Game.player.sleepy = 0;
                    Game.player.crazy = Math.max(0, Game.player.crazy - 15);
                    GameTime.forward(8 * HOUR_LENGTH);
                    Saver.work();
                }
                //���� 12 �����
                if(Minigame.current.input.startsWith("12")) {
                    Game.player.sleepy = 0;
                    Game.player.crazy = Math.max(0, Game.player.crazy - 30);
                    GameTime.forward(12 * HOUR_LENGTH);
                    Saver.work();
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
                Game.player.stimulate += 30;
                //�������� �������
                Game.player.stamina += 100;
                //�������� ����
                Game.player.crazy += 15;
                //���� ��
                Game.player.hp += 50;
                //���� ������
                Game.player.thirst += 20;
                //������� ����������
                if(Game.player.sleepy > 30) Game.player.sleepy -= 30;
                //������� �����
                if(Game.player.hunger > 40) Game.player.hunger -= 30;
                //�������� ����
                /* Game.player.equip.count -=1;
                if(Game.player.equip.count < 0){
                    Game.player.items.remove(Game.player.equip);
                    Game.player.equip = null;
                }
               */
            }
        }; repo.put(5,script);

        /** ������ �������� ������ ����� **/
        script = new Script() {
            @Override
            public void execute() {
                if(Game.player.poo > 99){
                    Game.player.addItem(Item.get(Game.player.equip.id));
                    return;
                }
                Game.player.hunger -=1;
                Game.player.thirst -= 2;
                Game.player.poo +=5;
                Game.player.pee +=2;
            }
        };repo.put(6,script);

        /** ������ ����-���� � ���������� ��������� ("�������" ��������) **/
        //TODO: ��������� � ����???
        //�������� ����� ��������� �������� � ������ ��������� ����
        script = new Script() {
            @Override
            public void execute() {

                if(true) return;        //�������� ����� ��������� �������� � ������ ��������� ����


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
                Game.player.crazy -= 25;
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
                            if(craftedItem.id == 63) craftedItem.count = 6;//�������
                            if (craftedItem.id == 60) craftedItem.count = 3;//�������
                            if( craftedItem.id == 62) craftedItem.count = 2;//�������� � �����
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
                    volume +=10; //�������� ����� �������
                    kanistra.description = "���������:"+volume+"/100"; //��������� ��������
                    Game.player.wanted += 10; //������� ����� ����� �������
            }
        };repo.put(17,script);

        /** ������ �������� ������� (���� ������) **/
        script = new Script() {
            @Override
            public void execute() {

            }
        }; repo.put(18,script);

        /** ������ ����� **/
        script = new Script() {
            @Override
            public void execute() {
                //������� ������ �����
                var pc = Game.currentLevel.objects.stream()
                        .filter(o->o.id==41).findFirst().orElse(null);
                if(pc==null) return;
                // ���������, ��� �� ������ �������
                boolean
                        assembled = pc.items.stream().anyMatch(i -> i.id == 71) //����
                        && pc.items.stream().anyMatch(i -> i.id == 108) //���������
                        && pc.items.stream().anyMatch(i -> i.id == 109) //�������
                        && pc.items.stream().anyMatch(i -> i.id == 111) //�����
                        && pc.items.stream().anyMatch(i -> i.id == 112); //���
                // ���� ��, ��������� ��������
                if(assembled) Minigame.current = Minigame.get(19);//������� ��������
                // ���� ���, ��������� ��������� ��� ���������� �������
                pc.opened = true;
            }
        }; repo.put(19,script);
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
                            var d = new Dialogue(); d.message = "���� ����� "+item.name;
                            d.responses.add(new Dialogue.Response("�����",0,0));
                            Dialogue.current = d;
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
                            var d = new Dialogue(); d.message = "���� ������� "+item.name;
                            d.responses.add(new Dialogue.Response("�����",0,0));
                            Dialogue.current = d;
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
                Game.player.mommaClean = DAY_LENGTH * 2;
                GameObject mom = Game.currentLevel.objects.stream().filter(o->o.name.equals("mom")).findFirst().get();
                mom.y=150; mom.x = 125;
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
                    mom.x=235;
                    Dialogue.current = null;
                } else{
                    Dialogue d = new Dialogue(); d.message = "� ������� ������";
                    d.responses = List.of(new Dialogue.Response("�����",0,0));
                    Dialogue.current = d;
                }
            }
        };
        repo.put(24,script);

        /** ���� �������, ��� ������ ����� ����� � ��������� **/
        script = new Script() {
            @Override
            public void execute() {
                Dialogue.current = Dialogue.mom.get(13);
                for (var q: Game.player.quests) if(q.id == 18 || q.id == 58) q.completed = true;
                Game.player.quests.add(Quest.get(20));
                Game.player.quests.add(Quest.get(14));
                Game.player.quests.add(Quest.get(16));
                var d = Dialogue.mom.get(Game.mom.dialogue);
                var r = d.responses.stream()
                        .filter(dd->dd.text.contains("������"))
                        .findFirst().orElse(null);
                Dialogue.mom.get(Game.mom.dialogue).responses.remove(r);
                d.responses.add(new Dialogue.Response("��������...",0,26));
                d.responses.add(new Dialogue.Response("��������...",0,27));
                d.responses.add(new Dialogue.Response("����...",0,28));
            }
        }; repo.put(25,script);

        /** ���� ���������, ������ �� ������ �������� **/
        script = new Script() {
            @Override
            public void execute() {
                var ride = Game.currentLevel.objects.stream().filter(o->o.id==47).findFirst().orElse(null);
                if(ride == null) {Dialogue.current = Dialogue.mom.get(15); return;}
                for(var q: Game.player.quests) if(q.id==14) q.completed=true;
                var d = Dialogue.mom.get(Game.mom.dialogue);
                var r = d.responses.stream()
                        .filter(dd->dd.text.contains("�"))
                        .findFirst().orElse(null);
                Dialogue.mom.get(Game.mom.dialogue).responses.remove(r);
                Dialogue.current = Dialogue.mom.get(14);
            }
        }; repo.put(26,script);

        /** ���� ���������, ������ �� ������ �������� **/
        script = new Script() {
            @Override
            public void execute() {
                var wash = Game.currentLevel.objects.stream().filter(o->o.id==140).findFirst().orElse(null);
                if(wash == null) {Dialogue.current = Dialogue.mom.get(17); return;}
                for(var q: Game.player.quests) if(q.id==16) q.completed=true;
                var d = Dialogue.mom.get(Game.mom.dialogue);
                var r = d.responses.stream()
                        .filter(dd->dd.text.contains("���"))
                        .findFirst().orElse(null);
                Dialogue.mom.get(Game.mom.dialogue).responses.remove(r);
                Dialogue.current = Dialogue.mom.get(16);
            }
        }; repo.put(27,script);

        /** ���� ���������, ������� �� ������ �������� **/
        script = new Script() {
            @Override
            public void execute() {
                var q = Game.player.quests.stream()
                        .filter(qu->qu.id==20)
                        .findFirst().orElse(null);
                if(q.completed){
                    Dialogue.current = Dialogue.mom.get(18);
                }else{
                    Dialogue.current = Dialogue.mom.get(22);
                }
            }
        }; repo.put(28,script);

        /** ������ "��������" �� ����� ����� **/
        script = new Script() {
            @Override
            public void execute() {
                if(Game.player.money >= 100_000){
                    Game.player.money -= 100_000;
                    var d = new Dialogue();
                    d.message = "������ �������� �������!";
                    d.responses.add(new Dialogue.Response("�����",0,0));
                    Dialogue.current = d;
                    for(var q: Game.player.quests) if(q.id==20)q.completed = true;
                    Minigame.current = Minigame.get(20);

                }else{
                    var d = new Dialogue();
                    d.message = "������������ �����!";
                    d.responses.add(new Dialogue.Response("�����",0,0));
                    Dialogue.current = d;
                }
            }
        }; repo.put(29,script);

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

        /** ����� ������ �������� ���� ���������, ������ �� ������ �������� � �������� **/
        script = new Script() {
            @Override
            public void execute() {
                var ride = Game.player.quests.stream().anyMatch(q->(q.id==14&& q.completed));
                var wash = Game.player.quests.stream().anyMatch(q->(q.id==16&& q.completed));

                if(ride && wash){
                    Dialogue.current = Dialogue.mom.get(20);

                    var d = Dialogue.mom.get(Game.mom.dialogue);
                    var r = d.responses.stream()
                            .filter(dd->dd.text.contains("��"))
                            .findFirst().orElse(null);
                    Dialogue.mom.get(Game.mom.dialogue).responses.remove(r);

                    Game.player.quests.add(Quest.get(19));


                }else{
                    var d = Dialogue.mom.get(19);
                    d.message = "�� ��� ��� �������.. \n �� ��� ����� � �����-�� �� ���� ���... \n ������ ��� ����� "
                            + " �������" + (!ride?" ��������":"") + ((!ride && !wash)?" � ":"") + (!wash?" ���������� ������":"");
                    Dialogue.current = d;
                }
            }
        }; repo.put(31,script);

        /** ������ ������ **/
        script = new Script() {
            @Override
            public void execute() {
                if(Game.player.poo>99||Game.player.pee>99) {
                    Game.player.equip.count +=1;
                    return;
                }
                Game.player.hunger -=10;
                Game.player.thirst -= 40;
                Game.player.poo +=5;
                Game.player.pee +=15;
                Game.player.sleepy +=2;
            }
        }; repo.put(36,script);

        /** ������ ������� (� �������) **/
        script = new Script() {
            @Override
            public void execute() {
                if(Game.player.poo>99) {
                    Game.player.equip.count +=1;
                    return;
                }
                Game.player.hunger -=20;
                Game.player.hp += 5;
                Game.player.poo +=15;
            }
        }; repo.put(41,script);

        /** ������ ���� **/
        script = new Script() {
            @Override
            public void execute() {
                if(Game.player.poo>99) {
                    Game.player.equip.count +=1;
                    return;
                }
                Game.player.hunger -=10;
                Game.player.thirst += 25;
                Game.player.poo +=40;
            }
        }; repo.put(42,script);

        /** ������ ����� **/
        script = new Script() {
            @Override
            public void execute() {

                if(Game.player.poo > 99) {Game.player.equip.count +=1; return;}
                Game.player.poo += 30;
                Game.player.hunger -= 15;
                Game.player.thirst += 30;
            }
        }; repo.put(43,script);

        /** ������ ����� ��������� **/
        script = new Script() {
            @Override
            public void execute() {
            Game.player.poo += 25;
            Game.player.hunger -= 35;
            Game.player.hp += 15;
            }
        };repo.put(50,script);

        /** ������ ������ **/
        script = new Script() {
            @Override
            public void execute() {
                if(Game.player.poo > 99) {Game.player.equip.count +=1; return;}
                Game.player.poo += 10;
                Game.player.hunger -= 15;
                Game.player.thirst -= 15;
                Game.player.hp += 20;

            }
        }; repo.put(57,script);

        /** ������ ������� ������ **/
        script = new Script() {
            @Override
            public void execute() {
                if(Game.player.poo > 99) {Game.player.equip.count +=1; return;}
                Game.player.hp += 15;
                Game.player.hunger -= 50;
                Game.player.thirst += 30;
                Game.player.poo += 40;
            }
        }; repo.put(58,script);

        /** ������ ������ **/
        script = new Script() {
            @Override
            public void execute() {
                if(Game.player.poo > 99) {Game.player.equip.count +=1; return;}
                Game.player.hp += 50;
                Game.player.hunger -= 70;
                Game.player.poo += 50;
            }
        }; repo.put(59,script);

        /** ������ ������� **/
        script = new Script() {
            @Override
            public void execute() {
                if(Game.player.poo > 99) {Game.player.equip.count +=1; return;}
                Game.player.hp += 20;
                Game.player.poo += 30;
                Game.player.hunger -= 30;
            }
        }; repo.put(60,script);

        /** ������ �������� ���� **/
        script = new Script() {
            @Override
            public void execute() {
                if(Game.player.poo > 99) {Game.player.equip.count +=1; return;}
                Game.player.poo += 30;
                Game.player.hunger -= 40;
                Game.player.thirst -= 30;
                Game.player.hp += 25;
            }
        }; repo.put(61,script);

        /** ������ ������� �������� **/
        script = new Script() {
            @Override
            public void execute() {
                if(Game.player.poo > 99) {Game.player.equip.count +=1; return;}
                Game.player.poo += 25;
                Game.player.hunger -= 30;
                Game.player.thirst += 10;
                Game.player.hp += 10;
            }
        }; repo.put(62,script);

        /** ������ ����� **/
        script = new Script() {
            @Override
            public void execute() {
                if(Game.player.poo > 99) {Game.player.equip.count +=1; return;}
                Game.player.poo += 30;
                Game.player.hunger -= 70;
                Game.player.thirst -= 30;
                Game.player.hp += 35;
            }
        }; repo.put(64,script);

        /** ������ ������� **/
        script = new Script() {
            @Override
            public void execute() {
                var matches = Game.player.items.stream().filter(i->i.id==67).findFirst().orElse(null);
                if(matches==null) {Game.player.addItem(Item.get(66)); return;}

                Game.player.thirst += 10;
                Game.player.hunger += 5;
                Game.player.crazy -= 10;

                matches.count-=1;
                if(matches.count<=0) Game.player.items.remove(matches);

                Animation.addAnimation(Game.player,Animation.get(Animation.PL_SMOKE));
            }
        };repo.put(66,script);

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

        /** ������ ���� ������� ���� **/
        script = new Script() {
            @Override
            public void execute() {
                if(Game.player.pee > 99){ Game.player.addItem(Item.get(84)); return;}
                Game.player.thirst -= 33;
                Game.player.pee += 33;
                Game.player.drunk += 15;
                Game.player.crazy -= 10;
                Game.player.hunger += 5;
                Game.player.sleepy += 10;
            }
        }; repo.put(84,script);

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
                Game.player.addItem(Item.get(75));
            }
        };repo.put(88,script);
        /** ������ ������**/
        script = new Script() {
            @Override
            public void execute() {

                var matches = Game.player.items.stream().filter(i->i.id==67).findFirst().orElse(null);
                if(matches==null){Game.player.addItem(Item.get(89)); return;}
                if(matches==null){Game.player.addItem(Item.get(89)); return;}

                Game.player.smoke += 40;
                Game.player.thirst += 20;
                Game.player.hunger += 20;
                Game.player.sleepy += 7;
                Game.player.crazy += 7;

                matches.count-=1;
                if(matches.count<=0) Game.player.items.remove(matches);

                Animation.addAnimation(Game.player,Animation.get(Animation.PL_SMOKE));

            }
        }; repo.put(89,script);
        /** ������ ������� ��� ���������� **/
        script = new Script() {
            @Override
            public void execute() {
                var weed = Game.player.items.stream().filter(i->i.id==7).findFirst().orElse(null);
                if(weed == null) return;
                weed.count -=1;
                if(weed.count <= 0) Game.player.items.remove(weed);
                for (int i = 0; i < 2; i++) Game.player.addItem(Item.get(89));
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
        /** ������ ���� ������� ���� **/
        script = new Script() {
            @Override
            public void execute() {
                if(Game.player.pee > 99){ Game.player.addItem(Item.get(101)); return;}
                Game.player.thirst -= 100;
                Game.player.pee += 30;
                Game.player.stamina += 200;
            }
        }; repo.put(101,script);


        /** ������ ���������� ������� ���� **/
        script = new Script() {
            @Override
            public void execute() {
                //������� ��������
                Rectangle r = new Rectangle(Game.player.x-100,Game.player.y-100,200,200);
                var catPlate = Game.currentLevel.objects.stream().filter(o->o.id==126&&r.intersects(o.hitBox)).findFirst().orElse(null);
                if(catPlate==null) return;
                var catPlateFull = GameObject.get(127);
                catPlateFull.x = catPlate.x;
                catPlateFull.y = catPlate.y;
                Game.currentLevel.objects.remove(catPlate);
                Game.currentLevel.objects.add(catPlateFull);
                Game.player.equip.count -= 1;
                if(Game.player.equip.count <= 0){ Game.player.items.remove(Game.player.equip); Game.player.equip = null;}
            }
        }; repo.put(116,script);

        /** ������ ������ **/
        script = new Script() {
            @Override
            public void execute() {
                if(Game.player.poo>99)
                { Game.player.addItem(Item.get(118)); return;}
                Game.player.hp =100;
                Game.player.poo +=30;
                Game.player.hunger = 0;
            }
        }; repo.put(118,script);

        /** ������ ��������� **/
        script= new Script() {
            @Override
            public void execute() {
                Game.player.sleepy += 40;
                Game.player.crazy -= 40;
            }
        }; repo.put(119,script);

        /** ������ �������� ���������� ������ **/
        script = new Script() {
            @Override
            public void execute() {
                if(Game.currentLevel.id != HUB) return;
                Game.player.items.remove(Game.player.equip);
                Game.player.equip = null;
                var washingMachine = GameObject.get(140);
                washingMachine.x = 155;
                washingMachine.y = 385;
                Level.repo.get(HUB).objects.add(washingMachine);
                for(var q : Game.player.quests) if(q.id == 16) q.completed = true;
            }
        }; repo.put(128,script);

        /** ������ ���������� �������� ����� **/
        script = new Script() {
            @Override
            public void execute() {
                if(Game.currentLevel.id == JAIL
                && Script.zekKilled >= 20
                && Game.currentLevel.objects.stream().noneMatch(o->o.id==124)){
                    Game.switchLevel(POLICE_OFFICE);
                    Game.player.x = 200; Game.player.y = 200;
                    repo.get(6666_5).execute();

                }else{
                    var d = new Dialogue();
                    d.message = "��� �������";
                    d.responses.add(new Dialogue.Response("�����",0,0));
                    Dialogue.current = d;
                }
            }
        }; repo.put(129,script);


        /** ������ ������ � ������� **/
        script = new Script() {
            @Override
            public void execute() {
                Dialogue d = new Dialogue();
                d.message = "�� ���������! ������, ��������, ��� ������ ���� �����... \n �������... � ���� ��� ����� ����������.. \n � ����� ������ ��� �����... ���������... \n ������.. ����� �� ��� ���� ���������, �� ����������� \n � ���� ����� �� ��� ������ ������� \n ����������!";
                d.responses.addAll(List.of(new Dialogue.Response("�����",0,0),
                                        new Dialogue.Response("��� ����..",0,132_01)));
                Dialogue.current = d;
                Dialogue.companion = null;
                if(Game.player.quests.stream().noneMatch(q->q.id==56))
                    Game.player.quests.add(Quest.get(56));
            }
        }; repo.put(132,script);

        /** ������� ����� ���� **/
        script = new Script() {
            @Override
            public void execute() {

                Dialogue d = new Dialogue();

                var dogMeat = Game.player.items.stream()
                        .filter(i->i.id==117).findFirst().orElse(null);

                if(dogMeat!=null){

                    if(Game.player.money < 100) {
                        d.message = "������, �� �� ������ ���� ��� ������!";
                        d.responses.add(new Dialogue.Response("�����",0,0));
                        Dialogue.current = d;
                        return;
                    }

                    dogMeat.count -= 1;

                    if(dogMeat.count <= 0)
                    { Game.player.items.remove(dogMeat); if(Game.player.equip == dogMeat) Game.player.equip = null; }

                    Game.player.addItem(Item.get(118));

                    Game.player.money -= 100;

                    d.message = "���, ��� ��� ���������� ����! \n �����, ��� � ������. \n ������� ";
                    d.responses.add(new Dialogue.Response("�����",0,0));
                    Dialogue.current = d;

                }else{
                    d.message = "������, �� �� ������ - ���� ����������� ����!!";
                    d.responses.add(new Dialogue.Response("�����",0,0));
                    Dialogue.current = d;
                }
            }
        }; repo.put(132_01,script);


        /** ���� �� ������� **/
        script = new Script() {
            @Override
            public void execute() {
                Game.currentLevel.objects.remove(Game.player);
                Game.currentLevel = Level.repo.get(SOCCER_FIELD);
                Game.currentLevel.objects.add(Game.player);
                Game.player.x = 300; Game.player.y =300;
                //���� ���� ��� � �������, � �� ���,
                // �� ��� �����/����������� �� ������� ��� ��������������� ��������.
                if(Game.nazi.enemy && Game.nazi.hp > 0) Game.nazi.hp = GameObject.get(9191).hp;
            }
        };repo.put(136,script);

        /** ����� � ������� **/
        script = new Script() {
            @Override
            public void execute() {
                Game.currentLevel.objects.remove(Game.player);
                Game.currentLevel = Level.repo.get(STREET_1);
                Game.currentLevel.objects.add(Game.player);
                Game.player.x = 4050; Game.player.y =3900;
            }
        };repo.put(137,script);

        /** ������ ���� ����� ���������� **/
        script = new Script() {
            @Override
            public void execute() {
                if(Game.player.pee > 99){ Game.player.addItem(Item.get(137)); return;}
                Game.player.pee += 33;
                Game.player.crazy += 3;
                Game.player.hunger -= 2;
                if(Game.player.sleepy>25) Game.player.sleepy -= 10;
                if(Game.player.stimulate < 20) Game.player.stimulate += 7; else  Game.player.stimulate +=2;
                Game.player.stamina += 200;
            }
        }; repo.put(137_01,script);

        /** ������ ���������� ������ **/
        script = new Script() {
            @Override
            public void execute() {
                if(Game.currentLevel.id != TRAP_DUDE_PLACE) return;
                if(Game.player.quests.stream().noneMatch(q->q.id==62)) return;
                if(Game.player.items.size() >= ITEMS_CAPACITY) return;
                var machine = Game.currentLevel.objects.stream().filter(o->o.id==140).findFirst().get();
                Game.currentLevel.objects.remove(machine);
                Game.player.addItem(Item.get(128));
                var r = Dialogue.trap.get(Game.trap.dialogue).responses.stream().filter(res->res.text.contains("���")).findFirst().orElse(null);
                Dialogue.trap.get(Game.trap.dialogue).responses.remove(r);
            }
        }; repo.put(140,script);

        /** ����� � �������� ����� �������� ���������� **/
        script = new Script() {
            @Override
            public void execute() {
                var d = new Dialogue();
                d.message = "����. �� ���? �� ��������?";
                d.responses.add(new Dialogue.Response("������",0,141_01));
                d.responses.add(new Dialogue.Response("<< ������� ����� >>",0,141_02));
                Dialogue.current = d;
            }
        }; repo.put(141,script);
        //������ �������
        script = new Script() {
            @Override
            public void execute() {
                var d = new Dialogue();
                d.message = "����? �� ��? \n �����, �� ��� �����? \n ����?! \n ��, �����, \n ����, ����? �?!... �����?! ����� �����?.. \n �-�-�... �����! \n ����, �����. � � ���� ����� ����?";
                if(Game.player.items.stream().anyMatch(i->i.id==78))
                    d.responses.add(new Dialogue.Response("����",0,141_01_01));
                d.responses.add(new Dialogue.Response("���",0,141_01_02));
                Dialogue.current = d;
            }
        }; repo.put(141_01,script);
        //���� �����
        script = new Script() {
            @Override
            public void execute() {
                var vodka = Game.player.items.stream()
                        .filter(i->i.id==78)
                        .findFirst().orElse(null);
                if(vodka==null) return;
                Game.player.items.remove(vodka);
                if(Game.player.equip==vodka) Game.player.equip = null;
                for(var o: Level.repo.get(BANDIT_FRIEND_FLAT).objects)
                    o.enemy = false;
                Game.switchLevel(BANDIT_FRIEND_FLAT);
                Game.player.x = 440; Game.player.y = 440;
                var d = new Dialogue();
                d.message = "���, ����� \n � ����� �������� ����� \n �?! ��?! ��, ����! \n �� ���� �����, �� \n �� ���, ������, ���";
                d.responses.add(new Dialogue.Response("�����",0,0));
                Dialogue.current = d;
            }
        }; repo.put(141_01_01,script);

        //�� ���� �����
        script = new Script() {
            @Override
            public void execute() {
                var d = new Dialogue();
                d.message = "���, � ��� ����� ������ \n �?! \n �� ���� ������ ������";
                d.responses.add(new Dialogue.Response("�����",0,0));
                d.responses.add(new Dialogue.Response("<< ������� ����� >>",0,141_02));
                Dialogue.current = d;
            }
        }; repo.put(141_01_02,script);

        //������� �����
        script = new Script() {
            @Override
            public void execute() {
                var d = new Dialogue();
                d.message = "����! ������? \n �� ���� ��������? \n � �� ������ ������";
                d.responses.add(new Dialogue.Response("�����",0,0));
                d.responses.add(new Dialogue.Response("<< ������� ����� >>",0,141_02_01));
                Dialogue.current = d;
            }
        }; repo.put(141_02,script);
        //��� �������� �������
        script = new Script() {
            @Override
            public void execute() {
                var d = new Dialogue();
                d.message = "�� ��, ���� �����";
                d.responses.add(new Dialogue.Response("�����",0,0));
                Dialogue.current = d;
                Game.switchLevel(BANDIT_FRIEND_FLAT);
                Game.player.x = 440; Game.player.y = 440;
            }
        }; repo.put(141_02_01,script);

        /** ������ ����� � ������ �������� �������� ����������  **/
        script = new Script() {
            @Override
            public void execute() {
                var d = new Dialogue();
                d.message = "����� �������. ����� ����";
                d.responses.add(new Dialogue.Response("�����",0,0));
                if(Game.player.items.stream().anyMatch(i->i.id==130))
                    d.responses.add(new Dialogue.Response("������� ����� ������",0,142_01));
                Dialogue.current = d;

                }
        }; repo.put(142,script);
        //������� ����� ������
        script = new Script() {
            @Override
            public void execute() {
                Game.switchLevel(BANDIT_FLAT);
                Game.player.y = 290; Game.player.x = 500;
            }
        }; repo.put(142_01,script);

        /** ������ ������ �� ����� ����� (����) **/
        script = new Script() {
            @Override
            public void execute() {
                Game.switchLevel(ROOF_RUINS);
                Game.player.x = 700; Game.player.y = 400;
                savedItems = new ArrayList<>();
                for(var i: Game.player.items){
                    savedItems.add(new Item(i));
                }
            }
        }; repo.put(144,script);
        /** ������ ���� �� ������ � ����������� **/
        script = new Script() {
            @Override
            public void execute() {
                if(Game.player.hat == null || Game.player.hat.id != 6){
                    var d= new Dialogue();
                    d.message = "�� ���� ������ �����. \n ��� ����������� ��� �� ������";
                    d.responses.add(new Dialogue.Response("�����",0,0));
                    Dialogue.current = d;
                }else{
                    Game.switchLevel(SEWERS);
                    Game.player.x = 130;
                    Game.player.y = 230;
                }
            }
        }; repo.put(149,script);
        /** ������ ������ � ����������� �� ������ ��������� **/
        script = new Script() {
            @Override
            public void execute() {
                Game.switchLevel(SEWERS);
                Game.player.x = 1200;
                Game.player.y = 2200;
            }
        }; repo.put(150,script);
        /** ������ ������� ����� **/
        script = new Script() {
            @Override
            public void execute() {
                var uranObj = Game.currentLevel.objects.stream().filter(o->o.id==152).findFirst().orElse(null);
                if(uranObj != null) Game.currentLevel.objects.remove(uranObj);
                Game.player.addItem(Item.get(97));
            }
        }; repo.put(152,script);

        /** ������ ��������� **/
        script = new Script() {
            @Override
            public void execute() {
                //TODO: ���������� ��� ��������
                var d = new Dialogue();
                d.message = "���� ��������� \n ";
                if(Game.player.items.stream().anyMatch(i->i.id==74))
                    d.responses.add(new Dialogue.Response("�������� �����",0,154_01));
                if(Game.player.items.stream().anyMatch(i->i.id==76))
                    d.responses.add(new Dialogue.Response("���������� ����������",0,154_02));
                d.responses.add(new Dialogue.Response("����� (�����)",0,0));
                Dialogue.current = d;
            }
        }; repo.put(154,script);
        //�������� ����� � ��������
        script = new Script() {
            @Override
            public void execute() {
                if(Game.player.atm){
                    //���� ��� ���������
                    var c = Game.player.items.stream().filter(i->i.id==74).findFirst().get();
                    c.count -=1; if(c.count<1) Game.player.items.remove(c); if(Game.player.equip==c) Game.player.equip = null;
                    int withdraw = 500 + (int)(Math.random() * 4_500);
                    Game.player.money += withdraw;
                    Game.player.wanted += 13;
                    var d = new Dialogue();
                    d.message = "�������� ������� �����, \n �� ����� ������. \n �����: " + withdraw;
                    d.responses.add(new Dialogue.Response("�����",0,0));
                    Dialogue.current = d;
                }else{
                    var d = new Dialogue();
                    d.message = "���-��� ������ �������. ";
                    d.responses.add(new Dialogue.Response("�����",0,0));
                    Dialogue.current = d;
                }
            }
        }; repo.put(154_01,script);
        //���������� ���������� �� ��������
        script = new Script() {
            @Override
            public void execute() {
                var device = Game.player.items.stream().filter(i->i.id==76).findFirst().get();
                Game.player.items.remove(device); if(Game.player.equip==device) Game.player.equip = null;
                Game.player.atm = true;
                var d = new Dialogue();
                d.message = "���������� �����������. ";
                d.responses.add(new Dialogue.Response("�����",0,0));
                Dialogue.current = d;
                for (var q: Game.player.quests) if(q.id == 29) q.completed = true;

            }
        }; repo.put(154_02,script);

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
                //���� �������� ��������� �����
                if(Game.player.quests.stream().anyMatch(q-> q.id == 29 && q.completed)){
                    var d = new Dialogue();
                    d.message = "���, ������. \n � �� ���� ��� ��� � ����� ����\n �� � ����, ��� ������ ����� ������ ����� ����� �������� \n �� ������ ����� ������ � ����� ����� � ����� ��������� \n ��������� ���� �������������� ���������� \n ������ � �� ���� ���� �� �����, �����...";
                    d.responses.add(new Dialogue.Response("�����",0,0));
                    Dialogue.current = d;
                }
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
                pc.x = 330; pc.y = 250;
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
                pc.x=280;pc.y=100;
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
                    Game.player.passwords.add("���. � ������� - 417");
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
                //��������� ������� ������ ��� ����� ������
                Dialogue.hacker.get(1).responses
                        .add(new Dialogue.Response("���. �����",0,11112));
                Game.player.passwords.add("����� - 666");
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
                if(!bought && !wears) Dialogue.current = Dialogue.hach.get(3);
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
                if(Game.player.quests.stream().anyMatch(q->q.id==33&&q.completed) && Game.player.quests.stream().noneMatch(q->q.id==34)){
                    repo.get(3333_21).execute();
                }
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
                if(Game.player.quests.stream().anyMatch(q->q.id==6))
                    d.responses.add(new Dialogue.Response("��� ��������� ���������?",0,3333_7));
                d.responses.add(new Dialogue.Response("�����",0,0));
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
                    Game.player.items.remove(beer);
                    if(Game.player.equip==beer)Game.player.equip=null;
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
                Level.repo.get(BUILDING_15).objects.add(door);
                //��������� ������� ��� ����� ������
                Dialogue.butcher.get(6).responses
                        .add(new Dialogue.Response("���������...",0,3333_15));
                //��������� ������
                Dialogue.current = null;
                Game.player.passwords.add("������� � �������� - 358");
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
                if(Game.player.items.stream().anyMatch(i->i.id==86)){
                    //���� ������, ��������� �����
                    for(var q:Game.player.quests) if(q.id==32) q.completed = true;
                    //���� �������
                    Game.player.money += 10_000;
                    //������� ������
                    Dialogue.current = Dialogue.butcher.get(12);
                    //�������� ������
                    var pacan = Game.player.items.stream().filter(i->i.id==86).findFirst().orElse(null);
                    if(pacan!=null) Game.player.items.remove(pacan);
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
                    if(response != null) Dialogue.butcher.get(Game.butcher.dialogue).responses.remove(response);
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
                var guy = GameObject.get(3333_5);
                guy.x = 9160; guy.y = 700;
                guy.speed = 1;
                Level.repo.get(STREET_1).objects.add(guy);
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
                    Dialogue.current = Dialogue.butcher.get(27);
                    //������� ������� ��� ����� ������
                    Dialogue.Response response =
                            Dialogue.butcher.get(6).responses
                            .stream()
                            .filter(r -> r.text.equals("������.."))
                            .findFirst()
                            .orElse(null);
                    if(response!= null) Dialogue.butcher.get(6).responses.remove(response);

                    return;
                }
                //���� ���, ���������� � �������
                Dialogue.current = Dialogue.butcher.get(19);
            }
        };repo.put(3333_20,script);
        /** ������ ����� ����� "��� ������ �3" **/
        script = new Script() {
            @Override
            public void execute() {
                //TODO
                //���� �������
                Game.player.quests.add(Quest.get(34));
                //��������� ������� ��� ����� ������
                Dialogue.butcher.get(Game.butcher.dialogue)
                        .responses.add(new Dialogue.Response("���������",0,3333_22));
                //������� ������ � �����������
                Dialogue.current = Dialogue.butcher.get(24);
            }
        };repo.put(3333_21,script);
        /** ������ ����� ����� "��� ������ �3" **/
        script = new Script() {
            @Override
            public void execute() {
                //TODO
                //��������� ���������� �������
                var docs = Game.player.items
                        .stream().filter(i->i.id==133)
                        .findFirst().orElse(null);
                if(docs == null) {
                    Dialogue.current = Dialogue.butcher.get(28);
                    //�� ������
                    return;
                }
                //���� ��, ��������� �����
                for(var q: Game.player.quests) if(q.id==34) q.completed = true;
                //���� �������
                //������� ������
                //��������� ������ ����
                //������� ������� ��� ����� ������
                Dialogue.Response response =
                        Dialogue.butcher.get(6).responses
                                .stream()
                                .filter(r -> r.text.contains("���"))
                                .findFirst()
                                .orElse(null);
                if(response!= null)
                    Dialogue.butcher.get(Game.butcher.dialogue).responses.remove(response);

                response = Dialogue.butcher.get
                                (Game.butcher.dialogue)
                                .responses
                                .stream()
                                .filter(r -> r.text.contains("���"))
                                .findFirst()
                                .orElse(null);
                if(response!= null)
                    Dialogue.butcher.get(Game.butcher.dialogue).responses.remove(response);

                Dialogue.current = Dialogue.butcher.get(29);
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
                //���� ������ �������� ����� � �������, � �� ���� ����� � ������
                if(Game.player.quests.stream().noneMatch(q->q.id==7)
                && Game.player.quests.stream().anyMatch(q->q.id==8&&q.completed))
                    //���������� �����
                    Dialogue.current = Dialogue.girl.get(8);
                //���� ������ �������� ����� � ������, � �� ���� ����� � ������ (�������� ��������!!!)
               // if(Game.player.quests.stream().noneMatch(q->q.id==23)
               //         && Game.player.quests.stream().anyMatch(q->q.id==7&&q.completed) && false)
                    //���������� �����
                    //Dialogue.current = Dialogue.girl.get(13);
                //���� ������ �������� ����� � ������(���, ������!!!��������), � �� ���� ����� � ������
                if(Game.player.quests.stream().noneMatch(q->q.id==24)
                        && Game.player.quests.stream().anyMatch(q->q.id==7&&q.completed))
                    //���������� �����
                    Dialogue.current = Dialogue.girl.get(18);
                //���� ������ �������� ����� � ������, � �� ���� ����� � �����
                if(Game.player.quests.stream().noneMatch(q->q.id==25)
                        && Game.player.quests.stream().anyMatch(q->q.id==24&&q.completed))
                    //���������� �����
                    Dialogue.current = Dialogue.girl.get(23);
                //���� ��������� ����� � �����, ��������� �� ������� ������������ ��������
                if(Game.player.quests.stream().anyMatch(q->q.id==25&&q.completed))
                    repo.get(4444_15).execute();
            }
        };repo.put(4444,script);
        /** ������ ������ ������� � ���� **/
        script = new Script() {
            @Override
            public void execute() {
                //��������� ������� ������� ��� ����� ������
                Dialogue.butcher.get(Game.butcher.dialogue)
                        .responses.add(new Dialogue.Response("�����",0,4444_3_01));
                //������� ��������� ������
                Dialogue.current = Dialogue.girl.get(2);
            }
        };repo.put(4444_3,script);
        //������ ��������� �����
        script = new Script() {
            @Override
            public void execute() {
                //��������� ������� ����� �����
                for(var q:Game.player.quests) if(q.id==6)q.completed=true;
                Dialogue.current = Dialogue.butcher.get(23);
                var d = Dialogue.butcher.get(Game.butcher.dialogue);
                var r = d.responses.stream().filter(rsp->rsp.text.contains("���"))
                        .findFirst().orElse(null);
                if(r!=null) d.responses.remove(r);
                Game.player.addItem(Item.get(52));
                Game.player.addItem(Item.get(52));
            }
        }; repo.put(4444_3_01,script);
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
                //������ ������� �������� ������
                Game.girl.dialogue = 5;
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
                //��������� ������� ��� ����� ������
                Dialogue.girl.get(Game.girl.dialogue).responses
                        .add(new Dialogue.Response("������...",0,4444_2));
                Game.player.passwords.add("������ (�������) - 420");

            }
        };repo.put(4444_6,script);
        /** ������ ����� **/
        //TODO: �������� ������ ������ � ����� �������
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
        /** ������ ����� ����� � ������� **/
        script = new Script() {
            @Override
            public void execute() {
                //���������, ������ �� ������ ������� ����
                var weed = Game.player.items.stream().filter(i->i.id==7).findFirst().orElse(null);
                var paper = Game.player.items.stream().filter(i->i.id==90).findFirst().orElse(null);
                //���� �� ������, ���������� ��� �� ����
                if(weed==null || paper==null){Dialogue.current = Dialogue.girl.get(6); return;}
                //���� ������, �������� ������ � ������
                weed.count -=1; if(weed.count <1) Game.player.items.remove(weed); if(Game.player.equip==weed) Game.player.equip = null;
                paper.count -=1; if(paper.count <1) Game.player.items.remove(paper); if(Game.player.equip==paper) Game.player.equip = null;
                //���� �����
                Game.player.addItem(Item.get(89));
                //��������� ������������
                Game.player.smoke += 45;
                //��������� �����
                for(var q : Game.player.quests)if(q.id == 8)q.completed=true;
                //������� ������� ��� ����� ������
                Dialogue.Response response = Dialogue.girl.get(Game.girl.dialogue).responses.stream()
                        .filter(r->r.text.equals("������...")).findFirst().orElse(null);
                if(response!=null)Dialogue.girl.get(Game.girl.dialogue).responses.remove(response);
                //����� �������������
                Dialogue.current = Dialogue.girl.get(7);
            }
        }; repo.put(4444_2,script);
        /** ������ ����������� ����� ���� **/
        script = new Script() {
            @Override
            public void execute() {
                //��������� �����
                Game.player.quests.add(Quest.get(7));
                //��������� ������� ������
                Dialogue.current = null;
                //��������� ������� ��� ����� ������
                Dialogue.girl.get(Game.girl.dialogue).responses
                        .add(new Dialogue.Response("����...",0,4444_8));
            }
        };repo.put(4444_7,script);
        /** ������ ����� ����� � ������ **/
        script = new Script() {
            @Override
            public void execute() {
                //���������, ������ �� ������ ������� ����
                var vape = Game.player.items.stream().filter(i->i.id==99).findFirst().orElse(null);
                //���� �� ������, ���������� ��� �� ����
                if(vape==null) {Dialogue.current = Dialogue.girl.get(29); return;}
                //���� ������, �������� ������ � ������
                vape.count -=1; if(vape.count <1) Game.player.items.remove(vape); if(Game.player.equip==vape) Game.player.equip = null;
                //��������� �����
                for(var q : Game.player.quests)if(q.id == 7)q.completed=true;
                //������� ������� ��� ����� ������
                Dialogue.Response response = Dialogue.girl.get(Game.girl.dialogue).responses.stream()
                        .filter(r->r.text.equals("����...")).findFirst().orElse(null);
                if(response!=null)Dialogue.girl.get(Game.girl.dialogue).responses.remove(response);
                //����� �������������
                Dialogue.current = Dialogue.girl.get(12);
            }
        }; repo.put(4444_8,script);
        /** ������ ����������� ����������� � ������ **/
        script = new Script() {
            @Override
            public void execute() {
                //��������� �����
                Game.player.quests.add(Quest.get(23));
                //��������� ������� ������
                Dialogue.current = null;
                //��������� ������� ��� ����� ������
                Dialogue.girl.get(Game.girl.dialogue).responses
                        .add(new Dialogue.Response("������...",0,4444_10));
            }
        };repo.put(4444_9,script);
        /** ������ ����� ����� � ������ **/
        script = new Script() {
            @Override
            public void execute() {
                //���������, ���� �� ������ �������
                //���� ��, ���������� ��� �� ����
                if(false) {Dialogue.current = Dialogue.girl.get(16); return;}
                //��������� �����
                for(var q : Game.player.quests)if(q.id == 23)q.completed=true;
                //������� ������� ��� ����� ������
                Dialogue.Response response = Dialogue.girl.get(Game.girl.dialogue).responses.stream()
                        .filter(r->r.text.equals("������...")).findFirst().orElse(null);
                if(response!=null)Dialogue.girl.get(Game.girl.dialogue).responses.remove(response);
                //����� �������������
                Dialogue.current = Dialogue.girl.get(17);
            }
        }; repo.put(4444_10,script);
        /** ������ ����������� ������ ����� **/
        script = new Script() {
            @Override
            public void execute() {
                //��������� �����
                Game.player.quests.add(Quest.get(24));
                //��������� ������� ������
                Dialogue.current = null;
                //��������� ������� ��� ����� ������
                Dialogue.girl.get(Game.girl.dialogue).responses
                        .add(new Dialogue.Response("������...",0,4444_12));
            }
        };repo.put(4444_11,script);
        /** ������ ����� ����� � ������ **/
        script = new Script() {
            @Override
            public void execute() {
                //��������� ������ �� ������ ������
                var webcam = Game.player.items.stream().filter(i->i.id==100).findFirst().orElse(null);
                //���� ��, ���������� ��� �� ����
                if(webcam==null) {
                    Dialogue.current = Dialogue.girl.get(21);
                    return;
                }
                //��������� �����
                for(var q : Game.player.quests)if(q.id == 24)q.completed=true;
                //������� ������� ��� ����� ������
                Dialogue.Response response = Dialogue.girl.get(Game.girl.dialogue).responses.stream()
                        .filter(r->r.text.equals("������...")).findFirst().orElse(null);
                if(response!=null)Dialogue.girl.get(Game.girl.dialogue).responses.remove(response);
                //����� �������������
                Dialogue.current = Dialogue.girl.get(22);
                //�������� ������
                Game.player.items.remove(webcam);
                if(Game.player.equip==webcam) Game.player.equip=null;

            }
        }; repo.put(4444_12,script);
        /** ������ ����������� ����� �������� **/
        script = new Script() {
            @Override
            public void execute() {
                //��������� �����
                Game.player.quests.add(Quest.get(25));
                //��������� ������� ������
                Dialogue.current = null;
                //��������� ������� ��� ����� ������
                Dialogue.girl.get(Game.girl.dialogue).responses
                        .add(new Dialogue.Response("��������...",0,4444_14));
            }
        };repo.put(4444_13,script);
        /** ������ ����� ����� � ��������� **/
        script = new Script() {
            @Override
            public void execute() {
                //��������� ���� �� ������ ��������
                //���� ��, ���������� ��� �� ����
                if(!Game.player.rent) {Dialogue.current = Dialogue.girl.get(26); return;}
                //��������� �����
                for(var q : Game.player.quests)if(q.id == 25)q.completed=true;
                //������� ������� ��� ����� ������
                Dialogue.Response response = Dialogue.girl.get(Game.girl.dialogue).responses.stream()
                        .filter(r->r.text.equals("��������...")).findFirst().orElse(null);
                if(response!=null)Dialogue.girl.get(Game.girl.dialogue).responses.remove(response);
                Game.currentLevel.objects.remove(Game.girl);
                Game.switchLevel(RENT_APARTMENTS);
                Game.currentLevel.objects.add(Game.girl);
                Game.player.x = 200;
                Game.player.y = 200;
                Game.girl.x = 250;
                Game.girl.y = 200;

                Dialogue.current = Dialogue.girl.get(27);
            }
        }; repo.put(4444_14,script);
        /** �������� �� ������� �������� **/
        script = new Script() {
            @Override
            public void execute() {
                //todo: �������� �� ������� ����� ����
                boolean hardcore = true;
                //�������� �� ������� �������
                boolean butcher = Game.player.quests.stream().anyMatch(q->q.id==34&& q.completed);
                //�������� �� ������� ���������
                boolean pharmacist = Game.player.quests.stream().anyMatch(q -> q.id == 53 && q.completed);
                //������� ������
                Dialogue d = new Dialogue();
                d.message = Dialogue.girl.get(28).message;
                if(hardcore && butcher && pharmacist) {
                    d.responses.add(new Dialogue.Response("�����",0,4444_16));
                }else{
                    String s = "�� ����. \n ���-�� ������ " + (!butcher?"������� ":"") + ((!butcher&&!pharmacist)?",":"") + (!pharmacist?" ������ ��������� � ���� ":"") + "����.";
                    d.responses.add(new Dialogue.Response(s,0,0));
                }
                Dialogue.current = d;
            }
        };repo.put(4444_15,script);
        /** ������ ������ �������� ���� � ������� **/
        script = new Script() {
            @Override
            public void execute() {
                Game.player.good = true;
            }
        }; repo.put(4444_16,script);

        /** ������ ���� �������� **/
        script = new Script() {
            @Override
            public void execute() {
                if (Game.player.rent) return;
                if(Game.player.money< 50_000 ){
                    var d = new Dialogue();
                    d.message = "������������ �������!";
                    d.responses.add(new Dialogue.Response("�����",0,0));
                    Dialogue.current = d;
                    return;
                }
                Game.player.rent = true;
                var door = GameObject.get(108);

                door.x = 1100; door.y = 150;
                door.coordinates = new Coordinates(300,300,RENT_APARTMENTS);
                Level.repo.get(BUILDING_15).objects.add(door);

                door = GameObject.get(108);
                door.x = 100; door.y = 330;
                door.coordinates = new Coordinates(1100,300,BUILDING_15);
                Level.repo.get(RENT_APARTMENTS).objects.add(door);

                Game.player.money -= 50_000;

                var d = new Dialogue();
                d.message = "�������� �����!";
                d.responses.add(new Dialogue.Response("�����",0,0));
                Dialogue.current = d;

            }
        }; repo.put(4444_22,script);
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
                    //��������� ������� ��� �����
                    Dialogue.trap.get(Game.trap.dialogue).responses.add(new Dialogue.Response("������� ����...",0,5555_6));
                    return;
                }
                //���� �������� � ������ ����� �����
                if(Game.player.quests.stream().anyMatch(q->q.id==30&&q.completed) && Game.player.quests.stream().noneMatch(q->q.id==61)){
                    //���������� �����������
                    Game.player.quests.add(Quest.get(61));
                    var r = new Dialogue.Response("������ � ����..",0,5555_7);
                    Dialogue.trap.get(Game.trap.dialogue).responses.add(r);
                    var d = new Dialogue();
                    d.message = "��, ������. ������� ���.. �������..\n ����� ������� �� ��� ������� ����� �����? \n � ���� ���������� \n ��� ����� ���� ������. \n ���������, � ��� �����  �� �������\n � ���� �� ����. \n ���� ������� ������ ����� ���� ���� ��������� � ���� \n ������� ������� ����� ����.. \n �� ���, ������?.. ���� ���� ������� ���� �� ��������. ������� �������. \n � � ��� � ������� ���� ������� � ���. \n ������?";
                    d.responses.add(new Dialogue.Response("�� �����",0,0));
                    d.responses.add(r);
                    Dialogue.current = d;
                    return;
                }
                //���� �� ��� ������� ����� � ����, ������ ����� ������� �������� �� �������
                if(Game.player.quests.stream().anyMatch(q->q.id==61&&q.completed) && Game.player.quests.stream().noneMatch(q->q.id==62)){
                    Game.player.quests.add(Quest.get(62));
                    var d = new Dialogue();
                    d.message = "������, ��� ���� ���� ������ �����. \n � ����� ��������� ����. \n � ���� ���� � ������� ������� ����� ������ ���������� ������\n ������ ��. ������ �� ������� �������";
                    d.responses.add(new Dialogue.Response("�����",0,5555_8));
                    Dialogue.current = d;
                    return;
                }
                //���� ���� ������������� (����� ������ ������) �������
                if(Game.player.quests.stream().anyMatch(q->q.owner.equals("����") && !q.completed && q.id!=31))
                    //������� ������
                    Dialogue.current = Dialogue.trap.get(8);
                //���� ������� � �������� ������ ���������
            }
            //���������, ���� �� � ������� ������������ (����� ������ ������)

        };repo.put(5555,script);

        /** ������� �� ������� ��������� ����� ��������� ������� **/
        script = new Script() {
            @Override
            public void execute() {
                //������� �����
                GameObject container = Level.repo.get(WILDBERRIES).objects.stream().filter(o->o.id==1).findFirst().orElse(null);
                //���� �� ����� ����� ��� ��� �����, �������� �������
                if(container == null || container.items.isEmpty()) Dialogue.current = Dialogue.trap.get(3);
                else{
                    //���� ������� ����, �� � ������� ��� �����, �������� ��� �� ����
                    if(Game.player.items.size()+container.items.size()>ITEMS_CAPACITY) {Dialogue.current = Dialogue.trap.get(4); return;}
                    //����� �������
                    for(var i: container.items)Game.player.addItem(i);
                    //������ �����
                    container.items = new ArrayList<>();
                    //�������� ������ � ������ �������
                    Dialogue d = new Dialogue();
                    d.message = "��� ���� �������. ��������� �����������!";
                    d.responses.add(new Dialogue.Response("�����",0,0));
                    Dialogue.current = d;
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
                Game.player.addItem(Item.get(136));
                //��������� ������� �� ����� ������ 31 (������ ������)
                Dialogue.trap.get(1).responses.add(new Dialogue.Response("�����",0,5555_4));
            }
        };repo.put(5555_3,script);


        /** ������� �� ������� ��������� ��� ������ ����� ����� **/
        script = new Script() {
            @Override
            public void execute() {
                var tumba = Level.repo.get(HACKERS_PLACE).objects.stream().filter(o->o.id==1).findFirst().orElse(null);
                if(tumba.items.stream().anyMatch(i->i.id==136)){
                    var d = new Dialogue();
                    d.message = "��, �� �������-��. \n ����� ���� ������ ���� ������� ��������� ����.. \n ���� �������.";
                    d.responses.add(new Dialogue.Response("�����",0,0));
                    Dialogue.current = d;
                    for(var q: Game.player.quests) if(q.id==31) q.completed = true;
                }else{
                    var d = new Dialogue();
                    d.message = "����� �����? \n � �� ����� ��������?";
                    d.responses.add(new Dialogue.Response("��.. ���...",0,0));
                    Dialogue.current = d;
                }
            }
        };repo.put(5555_4,script);

        /** ����� ���������, ����� �� �� ������ ����� � �������� ��� �� ������ **/
        script = new Script() {
            @Override
            public void execute() {
                var warehouse = Level.repo.get(WILDBERRIES).objects.stream().filter(o->o.id==1).findFirst().orElse(null);
                var eldak = warehouse.items.stream().filter(i->i.id==113).findFirst().orElse(null);
                if(eldak!=null){
                    warehouse.items.remove(eldak);
                    var d = new Dialogue();
                    d.message = "��, �������, � ������.. \n �� ���, ���, �� ������";
                    d.responses.add(new Dialogue.Response("�����",0,0));
                    Dialogue.current = d;
                    for(var q: Game.player.quests) if(q.id==30)q.completed=true;
                    var r = Dialogue.trap.get(Game.trap.dialogue).responses.stream().filter(o->o.text.contains("�������")).findFirst().orElse(null);
                    Dialogue.trap.get(Game.trap.dialogue).responses.remove(r);
                    return;
                }else {
                    var d = new Dialogue();
                    d.message = "���� �� �� �������, � �� ����� \n ���, ���..";
                    d.responses.add(new Dialogue.Response("�����",0,0));
                    Dialogue.current = d;
                }
            }
        };repo.put(5555_6,script);

        /** ������ ����������� ���� ��������� ������� **/
        script = new Script() {
            @Override
            public void execute() {
                Game.switchLevel(TRAP_DUDE_PLACE);
                Level.repo.get(TRAP_DUDE_PLACE).objects.add(Game.trap);
                Game.player.x = 200; Game.player.y = 200;
                Game.trap.x = 250; Game.trap.y = 220;
                var d = new Dialogue();
                d.message = "��, �����, �����. \n ��� ��� �� ��������� ���������. \n � ��� ����� ��������� ��� ������� \n �� ��� ����� \n � ������ ��� \n ����� ������ ��������� \n � ����������� ������� ���� \n � � ����� ��� ���������� ��� ��� �����. \n ��� � ��� ��� ��� �� ������. \n �������, ����";
                d.responses.add(new Dialogue.Response("�����..",0,0));
                Dialogue.current = d;
                for(var q: Game.player.quests)if(q.id==61)q.completed = true;
                var r = Dialogue.trap.get(Game.trap.dialogue).responses.stream().filter(o->o.text.contains("� ���")).findFirst().orElse(null);
                Dialogue.trap.get(Game.trap.dialogue).responses.remove(r);

            }
        }; repo.put(5555_7,script);
        /** ������ ���������� ����� ����� �� ���������� **/
        script = new Script() {
            @Override
            public void execute() {
                Game.player.quests.add(Quest.get(62));
                Dialogue.current = null;
            }
        }; repo.put(5555_8,script);
        //==========================================================================================//
        /**
         *
         * ������� �����
         *
         */
        /** ������ ������ �������
         // <<��� �� ���� ������>> **/
        script = new Script() {
            @Override
            public void execute() {
                if(Game.player.quests.stream().noneMatch(q->q.id==19)){
                    //���� ���� ����� � ��������
                    Dialogue.current = Dialogue.officer.get(2);
                    return;
                }
                if(Game.player.quests.stream().anyMatch(q->q.id==19)
                        && Game.player.quests.stream().noneMatch(q->q.owner.equals("����"))){
                    //���� ���� ����� � ��������, � ��� �� ������ ������ �����.
                    //���������� ����� ����� � ������������
                    Dialogue.current = Dialogue.officer.get(3);
                    return;
                }
                if(Game.player.quests.stream().anyMatch(q->!q.completed && q.owner.equals("����"))) {
                    //���� ���� ������������ �����, ���������� � ���
                    Dialogue.current = Dialogue.officer.get(11);
                    return;
                }
                if(Game.player.quests.stream().noneMatch(q->q.id==59)
                && Game.player.quests.stream().anyMatch(q->q.id==37&&q.completed)){
                    //���� ����� � ������������ ��������, � ����� � ������ �� ����.
                    //���� ����� � ������
                    repo.get(6666_3).execute();
                    return;
                }
                if(Game.player.quests.stream().noneMatch(q->q.id==41)
                && Game.player.quests.stream().anyMatch(q->q.id==59&&q.completed)){
                    //���� ����� � ��������� �� ����, � ����� � ������ ��������
                    //���������� ����� � ���������
                    Game.player.quests.add(Quest.get(41));
                    Dialogue.current = Dialogue.officer.get(17);
                    Dialogue.officer.get(Game.officer.dialogue)
                            .responses.add(new Dialogue.Response("�������..",0,6666_7));
                    gopKilled = 0;
                    return;
                }
                if(Game.player.quests.stream().noneMatch(q->q.id==38)
                && Game.player.quests.stream().anyMatch(q->q.id==41&&q.completed)){
                    //���� ����� � ��������� ��������, � ����� � ��������� �� ����
                    //���� ����� � ���������
                    Game.player.quests.add(Quest.get(38));
                    Dialogue.current = Dialogue.officer.get(22);
                    Dialogue.officer.get(Game.officer.dialogue)
                            .responses.add(new Dialogue.Response("���������",0,6666_8));
                    Game.player.passwords.add("������� ���������� - 111");
                    Game.player.passwords.add("������� � ��������� - 883");
                    return;
                }
                if(Game.player.quests.stream().anyMatch(q->q.id==38 || q.completed)){
                    //���� ������ �������� (���������) ����� � ���������.
                    //������������ ��� ����.
                    Dialogue.current = Dialogue.officer.get(31);

                }
            }
        }; repo.put(6666,script);


        /** ���� ���� ������� ������ ����������� **/
        script = new Script() {
            @Override
            public void execute() {
                Game.player.quests.add(Quest.get(37));
                Dialogue.officer.get(Game.officer.dialogue)
                        .responses.add(new Dialogue.Response("�����",0,6666_2));
                Dialogue.current = null;
            }
        }; repo.put(6666_1, script);

        /** ���� ���������, ������� �� ����������� **/
        script = new Script() {
            @Override
            public void execute() {
                var reg = Game.player.items
                        .stream().filter(i->i.id==121)
                        .findFirst().orElse(null);

                if(reg!=null && reg.count >= 15){
                    Game.player.items.remove(reg);
                    if(Game.player.equip == reg) Game.player.equip = null;
                    for (var q: Game.player.quests) if(q.id==37) q.completed = true;
                    Dialogue.current = Dialogue.officer.get(9);
                    var d = Dialogue.officer.get(Game.officer.dialogue);
                    var r = d.responses.stream()
                            .filter(dd->dd.text.contains("��"))
                            .findFirst().orElse(null);
                    Dialogue.officer.get(Game.officer.dialogue).responses.remove(r);

                }else {
                    Dialogue.current = Dialogue.officer.get(10);
                }
            }
        }; repo.put(6666_2,script);

        /** ���� ���� ������� ����������� � ������ **/
        script = new Script() {
            @Override
            public void execute() {
                Dialogue.current = Dialogue.officer.get(12);
                Dialogue.officer.get(Game.officer.dialogue)
                        .responses.add(new Dialogue.Response("����...",0,6666_5));
                Game.player.quests.add(Quest.get(59));
            }
        }; repo.put(6666_3,script);

        /** ������ ���� � ������ � ������� (�������� �����) **/
        script = new Script() {
            @Override
            public void execute() {
                Game.switchLevel(JAIL);
                Game.player.x = 400; Game.player.y = 250;
                zekKilled = 0;
                Dialogue.current = null;
                Game.player.addItem(Item.get(129));
            }
        }; repo.put(6666_4,script);

        /** ���� ���������, ��� ������ �������� ���� **/
        script = new Script() {
            @Override
            public void execute() {
                if(Level.repo.get(JAIL).objects.stream().noneMatch(o->o.id==124)
                && zekKilled >= 20){
                    Dialogue.current = Dialogue.officer.get(16);
                    for(var q: Game.player.quests) if(q.id == 59) q.completed = true;
                    var d = Dialogue.officer.get(Game.officer.dialogue);
                    var r = d.responses.stream()
                            .filter(dd->dd.text.contains("��"))
                            .findFirst().orElse(null);
                    Dialogue.officer.get(Game.officer.dialogue).responses.remove(r);
                }else{
                    Dialogue.current = Dialogue.officer.get(15);
                }
            }
        }; repo.put(6666_5, script);

        /** ���� ���������, ��� ������ ���� �������� **/
        script = new Script() {
            @Override
            public void execute() {
                    if(gopKilled >= 30){
                        for(var q: Game.player.quests) if(q.id==41) q.completed=true;
                        Dialogue.current = Dialogue.officer.get(21);
                        var d = Dialogue.officer.get(Game.officer.dialogue);
                        var r = d.responses.stream()
                                .filter(dd->dd.text.contains("��"))
                                .findFirst().orElse(null);
                        Dialogue.officer.get(Game.officer.dialogue).responses.remove(r);
                    }else{
                        Dialogue.current = Dialogue.officer.get(20);
                    }
            }
        }; repo.put(6666_7,script);

        /** ���������, ��������� �� ������ ������� **/
        script = new Script() {
            @Override
            public void execute() {
                var tumba = Level.repo.get(BANDIT_FLAT)
                        .objects.stream().filter(o->o.id==1)
                        .findFirst().orElse(null);
                if(tumba == null) return;
                var stuff = tumba.items.stream()
                        .filter(i->i.id==1||i.id==7)
                        .findFirst().orElse(null);
                if(stuff != null){
                    var d = Dialogue.officer.get(29);
                    d.responses.add(new Dialogue.Response("��",0,6666_9));
                    Dialogue.current = d;
                }else{
                    Dialogue.current = Dialogue.officer.get(29);
                }
            }
        }; repo.put(6666_8,script);

        /** ��������� ����� � ��������� ������� **/
        script = new Script() {
            @Override
            public void execute() {
                for (var q: Game.player.quests)
                    if(q.id == 38) q.completed = true;
                Dialogue.current = null;
                Dialogue.current = Dialogue.officer.get(9);
                var d = Dialogue.officer.get(Game.officer.dialogue);
                var r = d.responses.stream()
                        .filter(dd->dd.text.contains("����"))
                        .findFirst().orElse(null);
                Dialogue.officer.get(Game.officer.dialogue).responses.remove(r);
                Game.player.quests.add(Quest.get(60));
                var door = Level.repo.get(HUB).objects
                        .stream().filter(o->o.id==108)
                        .findFirst().orElse(null);
                if(door != null)
                    Level.repo.get(HUB).objects.remove(door);
                var ride = Level.repo.get(HUB).objects
                        .stream().filter(o->o.id==47)
                        .findFirst().orElse(null);
                if(ride != null)
                    Level.repo.get(HUB).objects.remove(ride);
                var machine = Level.repo.get(HUB).objects
                        .stream().filter(o->o.id==140)
                        .findFirst().orElse(null);
                if(machine != null)
                    Level.repo.get(HUB).objects.remove(machine);
                Level.repo.get(HUB).objects.remove(Game.mom);
                var ruin = GameObject.get(143);
                ruin.x = 400; ruin.y = 0;
                Level.repo.get(HUB).objects.add(ruin);
                var zone = GameObject.get(144);
                zone.x = 370; zone.y = 0;
                Level.repo.get(HUB).objects.add(zone);
                Level.repo.get(ROOF_RUINS)
                        .objects.add(Momma.boss);
                Momma.boss.x= 800; Momma.boss.y = 300;
                Momma.status = GO_RIGHT;
            }
        }; repo.put(6666_9,script);



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
                    //���� ����
                    Game.player.addItem(Item.get(132));
                    // ���
                    Game.player.passwords.add("������� �������� - 112");
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
                if(Game.player.quests.stream().anyMatch(q->q.owner.equals("�����")&&!q.completed)){
                    var d = new Dialogue();
                    d.message = "����, ����... \n ����� �� ���. �� ��� ��� � ����� ���� ������ \n ���������..\n � ��������� ����� ��... ��������� ��.. \n ���������� ����� ������ �� ������ \n � �� ����������� �����?";
                    d.responses.add(new Dialogue.Response("�����",0,0));
                    Dialogue.current = d;
                }
                //���������, ���� � ������� ��� �� ������ ������� (����� ���������)
                //���������� ����� � ���������
                if(Game.player.quests.stream().noneMatch(q->q.owner.equals("�����") && q.id!=46))
                    Dialogue.current = Dialogue.mech.get(11);
                //���� ������ �������� ������� � ���������, ���������� ����� ������� � �������
                if(Game.player.quests.stream().anyMatch(q->q.id==47&&q.completed)
                && Game.player.quests.stream().noneMatch(q->q.id==48))
                    Dialogue.current = Dialogue.mech.get(18);
                //���� ������ �������� ������� � �������, ���������� ����� ������� � ��������
                if(Game.player.quests.stream().anyMatch(q->q.id==48&&q.completed)
                        && Game.player.quests.stream().noneMatch(q->q.id==49))
                    Dialogue.current = Dialogue.mech.get(23);
                //���� ������ �������� ������� � ��������, ���������� ����� ������� � ������
                if(Game.player.quests.stream().anyMatch(q->q.id==49&&q.completed)
                        && Game.player.quests.stream().noneMatch(q->q.id==50))
                    Dialogue.current = Dialogue.mech.get(28);
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
                Game.player.passwords.add("����� �������� - 999");

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
        /** ������ ����� ����� � ������� **/
        script = new Script() {
            @Override
            public void execute() {
                //��������� �����
                Game.player.quests.add(Quest.get(48));
                //��������� ������� ��� ����� ������
                Dialogue.mech.get(Game.mechanic.dialogue).responses.add(new Dialogue.Response("�����",0,8888_9));
                //��������� ������
                Dialogue.current = null;
            }
        };repo.put(8888_7,script);
        /** ������ ����� ����� � ������� **/
        script = new Script() {
            @Override
            public void execute() {
                //���� ������ ������ �����
                if(Game.player.items.stream().anyMatch(i->i.id==95)) {
                    //������� ������
                    Dialogue.current = Dialogue.mech.get(22);
                    //��������� �����
                    for(var q:Game.player.quests)if(q.id==48)q.completed=true;
                    //������� ������� ��� ����� ������
                    Dialogue.Response response = Dialogue.mech.get(Game.mechanic.dialogue).responses.stream()
                            .filter(r->r.text.equals("�����")).findFirst().orElse(null);
                    if(response!=null)Dialogue.mech.get(Game.mechanic.dialogue).responses.remove(response);
                    //�������� �����
                    var pipe = Game.player.items.stream().filter(i->i.id==95).findFirst().orElse(null);
                    if(pipe!=null)Game.player.items.remove(pipe);if(Game.player.equip==pipe)Game.player.equip=null;
                }else {
                    //���� �� ������, ����������
                    Dialogue.current = Dialogue.mech.get(21);
                }
            }
        };repo.put(8888_9,script);
        /** ������ ����� ����� � �������� **/
        script = new Script() {
            @Override
            public void execute() {
                //��������� �����
                Game.player.quests.add(Quest.get(49));
                //��������� ������� ��� ����� ������
                Dialogue.mech.get(Game.mechanic.dialogue).responses.add(new Dialogue.Response("�������",0,8888_11));
                //��������� ������
                Dialogue.current = null;
                var specialist = Level.repo.get(SALON_SVYAZI).objects.stream().filter(o->o.id==8888_01).findFirst().get();
                var d = Dialogue.repo.get(specialist.dialogue);
                d.responses.add(new Dialogue.Response("������...",0,8888_10_01));
            }
        };repo.put(8888_10,script);
        //���������� ���� ������
        script = new Script() {
            @Override
            public void execute() {
                var specialist = Level.repo.get(SALON_SVYAZI).objects.stream().filter(o->o.id==8888_01).findFirst().get();
                var d = Dialogue.repo.get(specialist.dialogue);
                var r = d.responses.stream().filter(resp -> resp.text.contains("���")).findFirst().get();
                d.responses.remove(r);
                Game.player.addItem(Item.get(96));
                d = new Dialogue();
                d.message = "�, �� �� ���� ����.. \n ��, �����. ��� ������ ������� �� ���������";
                d.responses.add(new Dialogue.Response("�����",0,0));
                Dialogue.current = d;
            }
        };repo.put(8888_10_01,script);
        /** ������ ����� ����� � �������� **/
        script = new Script() {
            @Override
            public void execute() {
                //���� ������ ������ ������� �������
                if(Game.player.items.stream().anyMatch(i->i.id==96)) {
                    //������� ������
                    Dialogue.current = Dialogue.mech.get(27);
                    //��������� �����
                    for(var q:Game.player.quests)if(q.id==49)q.completed=true;
                    //������� ������� ��� ����� ������
                    Dialogue.Response response = Dialogue.mech.get(Game.mechanic.dialogue).responses.stream()
                            .filter(r->r.text.equals("�������")).findFirst().orElse(null);
                    if(response!=null)Dialogue.mech.get(Game.mechanic.dialogue).responses.remove(response);
                    //�������� �������
                    var xrayMeter = Game.player.items.stream().filter(i->i.id==96).findFirst().orElse(null);
                    if(xrayMeter!=null)Game.player.items.remove(xrayMeter);if(Game.player.equip==xrayMeter)Game.player.equip=null;

                }else {
                    //���� �� ������, ����������
                    Dialogue.current = Dialogue.mech.get(26);
                }
            }
        };repo.put(8888_11,script);
        /** ������ ����� ����� � ������ **/
        script = new Script() {
            @Override
            public void execute() {
                //��������� �����
                Game.player.quests.add(Quest.get(50));
                //��������� ������� ��� ����� ������
                Dialogue.mech.get(Game.mechanic.dialogue).responses.add(new Dialogue.Response("����",0,8888_13));
                //��������� ������
                Dialogue.current = null;
            }
        };repo.put(8888_12,script);
        /** ������ ����� ����� � ������ **/
        script = new Script() {
            @Override
            public void execute() {
                //���� ������ ������ ����
                if(Game.player.items.stream().anyMatch(i->i.id==97)) {
                    //������� ������
                    Dialogue.current = Dialogue.mech.get(33);
                    //��������� �����
                    for(var q:Game.player.quests)if(q.id==50)q.completed=true;
                    //������� ������� ��� ����� ������
                    Dialogue.Response response = Dialogue.mech.get(Game.mechanic.dialogue).responses.stream()
                            .filter(r->r.text.equals("����")).findFirst().orElse(null);
                    if(response!=null)Dialogue.mech.get(Game.mechanic.dialogue).responses.remove(response);
                    //�������� ����
                    var uran = Game.player.items.stream().filter(i->i.id==97).findFirst().orElse(null);
                    if(uran!=null)Game.player.items.remove(uran);if(Game.player.equip==uran)Game.player.equip=null;
                    var shop = Level.repo.get(GARAGE).objects.stream().filter(o->o.id==74).findFirst().get();
                    shop.items.add(Item.get(134));//��������� ������� � �������
                }
                //���� �� ������, ����������
                Dialogue.current = Dialogue.mech.get(26);
            }
        };repo.put(8888_13,script);
        //==========================================================================================//
        /**
         *
         * ������� ������
         *
         */
        /** ����� �������� �� ����������� ������� **/
        script = new Script() {
            @Override
            public void execute() {

                var dialog = new Dialogue();
                dialog.message = "�����, ����, �� ���? \n � �� ��� ������!";
                Dialogue.current = dialog;
                Dialogue.companion = Dialogue.Companion.NAZI;
                //���� �� ������� �� ��������� �� �����
                var quest = Game.player.quests.stream().filter(q->q.id==53).findFirst().orElse(null);
                //���� ���, ��� ������ �������
                if(quest == null){
                    dialog.responses.add(new Dialogue.Response("�����",0,0));
                }
                //���� ����
                else {
                    //����� ����������
                    dialog.responses.add(new Dialogue.Response("������ ������� �����",0,9191_1));
                }
            }
        }; repo.put(9191,script);

        /** ����� �������� �� �������� **/
        script = new Script() {
            @Override
            public void execute() {

                var dialog = new Dialogue();
                dialog.message = "��� ��, ��� ��� ��������! \n �� ��, ����, ����� ������? \n �� ��� �� � ��������� - �� ���� ��� ��������. \n ����� �� ����� �������� ��� �� ���! \n ��, ������? ������ �����, ���? \n �� � ���� ���� ������ ������ �������� �����, ���� ���� �������. \n ��� ���� ��������� ����, �������, ����? \n ��������� ����� ������ ���� ����� ������, \n � ��� ������ ����� ��� �� �� ��� �������� ��� ����������� ";
                Dialogue.current = dialog;
                Dialogue.companion = Dialogue.Companion.NAZI;

                dialog.responses.add(new Dialogue.Response("���� �����",0,9191_2));
                dialog.responses.add(new Dialogue.Response("��� ������...",9191_3,0));


            }
        }; repo.put(9191_1,script);

        /** ���������� ����� � ������� **/
        script = new Script() {
            @Override
            public void execute() {
                Game.nazi.enemy = true;
            }
        }; repo.put(9191_2,script);

        /** ����� ��������� ��������� � ���� ������� **/
        script = new Script() {
            @Override
            public void execute() {
                var dialog = new Dialogue();
                dialog.message = "<<��������� ��� ����>> \n � � ��� � ���� ��� �� �����. �������� \n � ����� ���, ��������� ���� �� �������. \n ����. \n � ����, �����, �������� � �����, ������? \n � �� ����� ������. ����� � ����� \n ���� ���� �� ������� ������� ������ ������ \n ������ � �������� ������ ����. �������� ����� \n ���� ����� ����� ����� �������� � �������. ������� �������. \n �� ��� ������. �� ���� �����. ��� �� ��� ��.  ";
                Dialogue.current = dialog;
                Dialogue.companion = Dialogue.Companion.NAZI;
                dialog.responses.add(new Dialogue.Response("�����",0,0));

                Game.player.quests.add(Quest.get(57));

                Game.nazi.scriptId = 9191_4;
            }
        };repo.put(9191_3,script);

        /** ����� ���������� ��� ������� **/
        script = new Script() {
            @Override
            public void execute() {
                var dialog = new Dialogue();
                dialog.message = "�! << ����� >> ����, �� �� ����������?";
                Dialogue.current = dialog;
                Dialogue.companion = Dialogue.Companion.NAZI;
                dialog.responses.add(new Dialogue.Response("��� ������",0,9191_5));
            }
        };repo.put(9191_4,script);

        /** ����� ��������� ��� �� ������ **/
        script = new Script() {
            @Override
            public void execute() {
                    if(Game.hach.hp <= 0){
                        var dialog = new Dialogue();
                        dialog.message = "��, � �����. \n ���, ������ ������, ���� � ������. \n �����!";
                        Dialogue.current = dialog;
                        Dialogue.companion = Dialogue.Companion.NAZI;
                        dialog.responses.add(new Dialogue.Response("�����",0,0));
                        Game.nazi.scriptId = 0;
                    }else{
                        var dialog = new Dialogue();
                        dialog.message = "����� �� ��� �������?! \n ���� ���� ������ ���������� �� ������ ������ \n ���� ������ ���� ������� �� ����� ��� ����������";
                        Dialogue.current = dialog;
                        Dialogue.companion = Dialogue.Companion.NAZI;
                        dialog.responses.add(new Dialogue.Response("�����",0,0));
                    }
            }
        };repo.put(9191_5,script);

        //=========================================================================================//
        /**
         *
         * ������� ���������
         *
         */
        /** ��������� ��������� ������� << ��� �� ���� ������ >>**/
        script = new Script() {
            @Override
            public void execute() {
                //���� �� ������ ������� �� ���������
                if(Game.player.quests.stream().noneMatch(q->q.owner.equals("���������")))
                    // ���������� ����� ������ � ��������
                    Dialogue.current = Dialogue.pharmacist.get(4);
                //���� ��� ������� � ������
                if(Game.player.quests.stream().anyMatch(q->q.id==51&&q.completed)
                    && Game.player.quests.stream().noneMatch(q->q.id == 52)) {
                    //���������� ������� � ������
                    Dialogue.current = Dialogue.pharmacist.get(8);
                }
                //���� ��� ������� � �������
                if(Game.player.quests.stream().anyMatch(q->q.id==52&&q.completed)
                        && Game.player.quests.stream().noneMatch(q->q.id == 53)) {
                    //���������� ������� � �������
                    Dialogue.current = Dialogue.pharmacist.get(12);
                }

                if(Game.player.quests.stream().anyMatch(q->!q.completed && q.owner.equals("���������")))
                    Dialogue.current = Dialogue.pharmacist.get(14);

            }
        }; repo.put(9999,script);

        /** ������ ������� � ��������**/
        script = new Script() {
            @Override
            public void execute() {
                Game.player.quests.add(Quest.get(51));
                Dialogue.pharmacist.get(1).responses
                        .add(new Dialogue.Response("��������",0,9999_4));
                Dialogue.current = null;
                Game.player.passwords.add("������ (���������) - 880");
            }
        }; repo.put(9999_1,script);

        /** ������ ������� � ��������**/
        script = new Script() {
            @Override
            public void execute() {
                Game.player.quests.add(Quest.get(52));
                Dialogue.pharmacist.get(1).responses
                        .add(new Dialogue.Response("�����",0,9999_5));
                Dialogue.current = null;
                for (int i = 0; i < 10; i++) {
                    Game.player.addItem(Item.get(119));
                }

            }
        }; repo.put(9999_2,script);

        /** ������ ������� � �������**/
        script = new Script() {
            @Override
            public void execute() {
                Game.player.quests.add(Quest.get(53));
                Dialogue.pharmacist.get(1).responses
                        .add(new Dialogue.Response("�������",0,9999_6));
                Dialogue.current = null;

            }
        }; repo.put(9999_3,script);

        /** ��������� ����� � �������� **/
        script = new Script() {
            @Override
            public void execute() {
                var meds = Game.player.items.stream().filter(i->i.id == 119).findFirst().orElse(null);
                if(meds != null){

                    Game.player.items.remove(meds);
                    if(Game.player.equip==meds) Game.player.equip = null;

                    Dialogue d = new Dialogue();
                    d.message = "���, ����������.. \n �� �����, ������� ������� ��. \n � �� ������ �������� ���������.. \n �� ���, ��� � �������, ���� ������. \n �� ���������� 200� �����. \n ����� ���� " + (meds.count * 200);
                    d.responses.add(new Dialogue.Response("���",0,0));
                    Dialogue.current = d;

                    Game.player.money += meds.count * 200;

                    for (var q : Game.player.quests) if(q.id==51) q.completed = true;

                    Dialogue.Response response =
                            Dialogue.pharmacist.get(1).responses.stream()
                            .filter(r->r.text.contains("��������"))
                            .findFirst().orElse(null);
                    if(response!=null)
                        Dialogue.pharmacist.get(1).responses.remove(response);

                }else{
                    Dialogue d = new Dialogue();
                    d.message = "���, � ��� ����������..? \n �� ������? \n � �� �� �������� ��? \n � ������� ������ ��";
                    d.responses.add(new Dialogue.Response("������",0,0));
                    Dialogue.current = d;
                }
            }
        };repo.put(9999_4,script);

        /** ��������� ������� **/
        script = new Script() {
            @Override
            public void execute() {
                if(Level.repo.get(STREET_1).objects.stream().noneMatch(o->o.id==126)){
                    Dialogue d = new Dialogue();
                    d.message = "������� �� ���! \n ���� ������� ��������? \n �� ��������, �� �������!";
                    d.responses.add(new Dialogue.Response("�����",0,0));
                    Dialogue.current = d;

                    for (var q : Game.player.quests) if(q.id==52) q.completed = true;

                    Dialogue.Response response =
                            Dialogue.pharmacist.get(1).responses.stream()
                                    .filter(r->r.text.contains("�����"))
                                    .findFirst().orElse(null);
                    if(response!=null)
                        Dialogue.pharmacist.get(1).responses.remove(response);


                }else {
                    Dialogue d = new Dialogue();
                    d.message = "�� ����� ����� ������� ����?";
                    d.responses.add(new Dialogue.Response("�� ����..",0,0));
                    Dialogue.current = d;
                }
            }
        }; repo.put(9999_5,script);


        /** ��������� ����� ��������� ������ **/
        script = new Script() {
            @Override
            public void execute() {
                if (Game.nazi.hp <= 0) {

                    var d = new Dialogue();
                    d.message = "�� ���, �� � ��� ����������? \n ������� ���, ������? \n ��, ����� �������.. ����� �������! \n ���� �� ��� ��� �� �����. ����� �� ����� ���������� ��� �����!...\n ��� ������� �� ��� ���� �� ���� ����� ������� �����! \n �� ��� � ����, ��� � ����������. \n ��, � ��� � ����-�� ���� �������, ��� �������.. \n ����! ��� �� �������� ��� � ����� ��������? \n ������, � ������-�� ���� �� ������� ��������. \n ������� � ������������! ������������. \n � �� ����� ����� ���� ������ �����������. \n ��������� ��� ��� ��� ����. � ��� ����� ������� ������� \n � �� ����� ��������, ������ ���� � ��� ���� ������! \n ������ ��� �����������! ";
                    d.responses.add(new Dialogue.Response("�����",0,0));
                    Dialogue.current = d;
                    Dialogue.companion = Dialogue.Companion.PHARMACIST;

                    Dialogue.Response response =
                            Dialogue.pharmacist.get(1).responses.stream()
                                    .filter(r -> r.text.contains("�������"))
                                    .findFirst().orElse(null);
                    if (response != null)
                        Dialogue.pharmacist.get(1).responses.remove(response);

                    Game.player.quests.stream()
                            .filter(q->q.id==53)
                            .forEach(q-> q.completed = true);


                }else{
                    var d = new Dialogue();
                    d.message = "�� ���, �� � ��� ����������? ";
                    d.responses.add(new Dialogue.Response("�, ���..",0,0));
                    Dialogue.current = d;
                    Dialogue.companion = Dialogue.Companion.PHARMACIST;

                }
            }
        }; repo.put(9999_6,script);

    }
    /** ��� **/
    public static void foo(){}
}
