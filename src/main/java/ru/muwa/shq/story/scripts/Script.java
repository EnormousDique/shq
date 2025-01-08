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
 * Класс пользовательских сценариев
 */
public abstract class Script {
    /** Основной метод "выполнить" сценарий **/
    public abstract void execute();
    public boolean expired; //Поле, используемое для пометки сценария как "отработавший"
    public static HashMap<Integer,Script> repo = new HashMap<>(); //Хранилище сценариев
    public static HashMap<Integer,Long> momDrugs = new HashMap<>(),
                                         momFood = new HashMap<>();
    //Мапа для хранения паролей домофона. Ключ - координаты назначения домофона, значение - пароль.
    public static HashMap<Coordinates,String> padiquePasswords = new HashMap<>();
    public static int zekKilled = 0;
    public static int gopKilled = 0;
    //Список для хранения вещей игрока для респавна при битве с боссом
    private static List<Item> savedItems = new ArrayList<>();
    /** Метод проверки пароля домофона **/
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
        //Что мама ест и насколько ее это насыщает
        momFood.put(50,HOUR_LENGTH * 15);//Паста
        momFood.put(63,HOUR_LENGTH * 8);//Сырник
        momFood.put(57,HOUR_LENGTH * 15);//салат
        momFood.put(58,HOUR_LENGTH * 25);//жареная курица
        momFood.put(59,HOUR_LENGTH * 30);//стейк
        momFood.put(60,HOUR_LENGTH * 15);//котлета
        momFood.put(61,HOUR_LENGTH * 20);//куриный суп
        momFood.put(62,HOUR_LENGTH * 10);//картошка с лучком
        momFood.put(64,HOUR_LENGTH * 35);//борщ
        //Какие лекарства помогают маме и на какое время
        momDrugs.put(46,HOUR_LENGTH * 10);
        momDrugs.put(47,HOUR_LENGTH * 20);
        momDrugs.put(48,HOUR_LENGTH * 30);
        momDrugs.put(49,HOUR_LENGTH * 50);
        //Пароли от подъездов
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
         * СКРИПТЫ
         *
        **/

        /** Скрипт воскрешения **/
        Script script = new Script() {
            //Воскрешение
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
                    d.message = "Спустя 4 часа после госпитализации, Шкипер пришел в себя на больничной койке. \n За лечение выставили счет: " + fine + "р.";
                    d.responses.add(new Dialogue.Response("ладно.", 0, 0));
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
                    d.message = "Шкипер потерял сознание и попал во временную петлю \n Время вернулась назад до момента сражения";
                    d.responses.add(new Dialogue.Response("ладно",0,0));
                    Dialogue.current = d;

                }
            }
        };
        repo.put(1,script);

        /** ПРИМЕР СКРИПТА **/
        script = new Script() {
            @Override
            public void execute() {
                //Код скрипта
            }//Добавляем скрипт в репозиторий скрипта, задав ему уникальный script id
        }; repo.put(-1,script);

        /** Скрипт мини-игры СОН **/
        script = new Script() {
            //СПИМ
            @Override
            public void execute() {
                //Спим 2 часа
                if(Minigame.current.input.startsWith("2")) {
                    Game.player.sleepy -= 20;
                    GameTime.forward(2 * HOUR_LENGTH);
                    Game.player.crazy = Math.max(0, Game.player.crazy - 5);
                    Saver.work();
                }
                //Спим 4 часа
                if(Minigame.current.input.startsWith("4")) {
                    Game.player.sleepy -= 50;
                    GameTime.forward(4 * HOUR_LENGTH);
                    Game.player.crazy = Math.max(0, Game.player.crazy - 10);
                    Saver.work();
                }
                //Спим 8 часов
                if(Minigame.current.input.startsWith("8")) {
                    Game.player.sleepy = 0;
                    Game.player.crazy = Math.max(0, Game.player.crazy - 15);
                    GameTime.forward(8 * HOUR_LENGTH);
                    Saver.work();
                }
                //Спим 12 часов
                if(Minigame.current.input.startsWith("12")) {
                    Game.player.sleepy = 0;
                    Game.player.crazy = Math.max(0, Game.player.crazy - 30);
                    GameTime.forward(12 * HOUR_LENGTH);
                    Saver.work();
                }
                //Сбрасываем мини-игру
                Minigame.current.input = "";
                Minigame.current = null;
            }
        };
        repo.put(3,script);

        /** Скрипт мини-игры пьем из крана **/
        script = new Script() {
            //Пьем
            @Override
            public void execute() {
                Game.player.thirst = 0;
                Game.player.pee = Math.min(100,Game.player.pee + 50);
            }
        };
        repo.put(4,script);

        /** Скрипт соли **/
        script = new Script() {
            @Override
            public void execute() {
                //Повышаем нанюханность
                Game.player.stimulate += 30;
                //Повышаем дыхалку
                Game.player.stamina += 100;
                //Повышаем псих
                Game.player.crazy += 15;
                //Даем хп
                Game.player.hp += 50;
                //Даем сушняк
                Game.player.thirst += 20;
                //Снимаем сонливость
                if(Game.player.sleepy > 30) Game.player.sleepy -= 30;
                //Снимаем голод
                if(Game.player.hunger > 40) Game.player.hunger -= 30;
                //Забираем соль
                /* Game.player.equip.count -=1;
                if(Game.player.equip.count < 0){
                    Game.player.items.remove(Game.player.equip);
                    Game.player.equip = null;
                }
               */
            }
        }; repo.put(5,script);

        /** Скрипт съедания сырого овоща **/
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

        /** Скрипт мини-игры с ментовской подставой ("красная" закладка) **/
        //TODO: перенести в хача???
        //ВРЕМЕННО ДУМАЮ ОТКЛЮЧИТЬ ПОДСТАВУ В РАМКАХ УПРОЩЕНИЯ ИГРЫ
        script = new Script() {
            @Override
            public void execute() {

                if(true) return;        //ВРЕМЕННО ДУМАЮ ОТКЛЮЧИТЬ ПОДСТАВУ В РАМКАХ УПРОЩЕНИЯ ИГРЫ


                if(expired) return;
                //Созадем мента
                var ment = GameObject.get(7);
                ment.x = Game.player.x - 50; ment.y = Game.player.y + 70;
                //Добавляем на уровень
                Game.currentLevel.objects.add(ment);
                //Выводим диалог
                Dialogue.current = Dialogue.repo.get(25);
                Game.player.wanted = 110;
                //Скрипт одноразовый
                expired = true;
            }
        };
        repo.put(8,script);

        /** СКРИПТ ВОДКИ  **/
        script = new Script() {
            @Override
            public void execute() {
                //Игрок получает эффект
                Game.player.drunk += 35;
                Game.player.sleepy += 15;
                Game.player.hunger += 15;
                Game.player.thirst += 15;
                Game.player.crazy -= 25;
            }
        }; repo.put(9,script);

        /** Скрипт мини-игры шкуринга. Игрок снимает закладку  **/
        script = new Script() {
            @Override
            public void execute() {
                //todo: Добавить шанс на получение мусора вместо фитюли
                //Выдаем закладку (фитюлю)
                Game.player.addItem(Item.get(77));
            }
        };
        repo.put(10,script);

        /** Скрипт мини-игры ПЛИТА (готовим еду) **/
        script = new Script() {
            @Override
            public void execute() {
                //TODO хуёвый код. переписать
                //Находим печку на уровне
                for (int i = 0; i < Game.currentLevel.objects.size(); i++) {
                    GameObject o = Game.currentLevel.objects.get(i);
                    if (o.name.equals("stove"))
                    {
                        //Вызываем метод крафтинга, смотрим что получилось из ингридиентов на плите
                        if(Crafting.craft(o.items) != -1)
                        {
                            Item craftedItem = Item.get(Crafting.craft(o.items));
                            if(craftedItem.id == 63) craftedItem.count = 6;//Сырники
                            if (craftedItem.id == 60) craftedItem.count = 3;//Котлеты
                            if( craftedItem.id == 62) craftedItem.count = 2;//Картошка с луком
                            //Даем игроку скрафченные вещи
                            Game.player.addItem(craftedItem);
                            o.items = new ArrayList<>();
                            return;
                        } else return; //Или не даем (если он лох)
                    }
                }
            }
        };
        repo.put(11,script);

        /** Скрипт миниигры слив бензина **/
        script = new Script() {
            @Override
            public void execute() {
                    //Находим канистру в инвентаре
                    var kanistra = Game.player.items.stream()
                            .filter(i->i.id==91).findFirst().orElse(null);
                    //Если не нашли, нахуй
                    if(kanistra == null) return;
                    //Определяем текущий объем бенза в канистре по описанию
                    int volume = Integer.parseInt
                            (kanistra.description.split(":")[1].split("/")[0]);
                    volume +=10; //Повышаем объем бензина
                    kanistra.description = "Заполнена:"+volume+"/100"; //Обновляем описание
                    Game.player.wanted += 10; //Мусоров такое может заебать
            }
        };repo.put(17,script);

        /** Скрипт миниигры продажа (брат биолог) **/
        script = new Script() {
            @Override
            public void execute() {

            }
        }; repo.put(18,script);

        /** Скрипт компа **/
        script = new Script() {
            @Override
            public void execute() {
                //Находим объект компа
                var pc = Game.currentLevel.objects.stream()
                        .filter(o->o.id==41).findFirst().orElse(null);
                if(pc==null) return;
                // Проверяем, все ли детали собраны
                boolean
                        assembled = pc.items.stream().anyMatch(i -> i.id == 71) //ПРОЦ
                        && pc.items.stream().anyMatch(i -> i.id == 108) //ОПЕРАТИВА
                        && pc.items.stream().anyMatch(i -> i.id == 109) //МОНИТОР
                        && pc.items.stream().anyMatch(i -> i.id == 111) //КЛАВА
                        && pc.items.stream().anyMatch(i -> i.id == 112); //МЫШ
                // Если да, открываем интернет
                if(assembled) Minigame.current = Minigame.get(19);//Открыли интернет
                // Если нет, открываем контейнер для добавления деталей
                pc.opened = true;
            }
        }; repo.put(19,script);
        /** Шкипер кормит маму **/
        script = new Script() {
            //Проверка еды у Шкипера для мамы.
            @Override
            public void execute() {
                //Проверяем, что маме нужна еда (таймеру меньше 2-х дней)
                if(Game.player.mommaFullness < DAY_LENGTH * 2) {
                    //Если еда нужна, но в инвентаре её нет, выводим соответствующий диалог
                    if(Game.player.items.stream().noneMatch(i -> momFood.containsKey(i.id)))
                    {Dialogue.current = Dialogue.repo.get(3); return;}
                    for (int i = 0; i < Game.player.items.size(); i++) {
                        //Перебираем инвентарь
                        Item item = Game.player.items.get(i);
                        if(momFood.containsKey(item.id)) {
                            //Если нашлась еда в инвентаре, забираем его
                            if(item.count > 1) {
                                item.count -=1;
                            }else Game.player.items.remove(item);
                            //Увеличиваем таймер мамы на соотв. величину
                            Game.player.mommaFullness += momFood.get(item.id);
                            var d = new Dialogue(); d.message = "Мама съела "+item.name;
                            d.responses.add(new Dialogue.Response("ладно",0,0));
                            Dialogue.current = d;
                        }
                    }
                } else {
                    //Не нужна еда, открываем соответствующий диалог
                    Dialogue.current = Dialogue.repo.get(2);
                }
            }
        };
        repo.put(20,script);

        /** Шкипер дает маме лекарства **/
        script = new Script() {
            //Проверка лекарств у Шкипера для мамы
            @Override
            public void execute() {
                //Проверяем, что маме нужны лекарства (таймеру меньше 2-х дней)
                if(Game.player.mommaHealth < DAY_LENGTH * 2) {
                    //Если лекарства нужны, но в инвентаре их нет, выводим соответствующий диалог
                    if(Game.player.items.stream().noneMatch(i -> momDrugs.containsKey(i.id)))
                    {Dialogue.current = Dialogue.repo.get(3); return;}
                    for (int i = 0; i < Game.player.items.size(); i++) {
                        //Перебираем инвентарь
                        Item item = Game.player.items.get(i);
                        if(momDrugs.containsKey(item.id)) {
                            //Если нашлось лекарство в инвентаре, забираем его
                            if(item.count > 1) {
                                item.count -=1;
                            }else Game.player.items.remove(item);
                            //Увеличиваем таймер мамы на соотв. величину
                            Game.player.mommaHealth += momDrugs.get(item.id);
                            var d = new Dialogue(); d.message = "Мама приняла "+item.name;
                            d.responses.add(new Dialogue.Response("ладно",0,0));
                            Dialogue.current = d;
                        }
                    }
                } else {
                    //Не нужны лекарства, открываем соответствующий диалог
                    Dialogue.current = Dialogue.repo.get(2);
                }
            }
        };
        repo.put(21,script);

        /** Скрипт мини-игры "Шкипер моет маму" **/
        script = new Script() {
            @Override
            public void execute() {
                Game.player.mommaClean = DAY_LENGTH * 2;
                GameObject mom = Game.currentLevel.objects.stream().filter(o->o.name.equals("mom")).findFirst().get();
                mom.y=150; mom.x = 125;
            }
        };
        repo.put(22,script);

        /** Скрипт проверяет лежит ли мама в ванной **/
        script = new Script() {
            @Override
            public void execute() {
                GameObject mom = Game.currentLevel.objects.stream().filter(o->o.name.equals("mom")).findFirst().get();
                if(mom.y > 380) Minigame.current = Minigame.get(4);
            }
        };
        repo.put(23,script);

        /** Шкипер предлагает маме принять ванну **/
        script = new Script() {
            //Скрипт, запускаемый из диалога "мам, пошли в душ"
            @Override
            public void execute() {
                GameObject mom = Game.currentLevel.objects.stream().filter(o -> o.name.equals("mom")).findFirst().get();
                if(Game.player.mommaClean < DAY_LENGTH * 2) {
                    mom.y = 450;
                    mom.x=235;
                    Dialogue.current = null;
                } else{
                    Dialogue d = new Dialogue(); d.message = "Я недавно мылася";
                    d.responses = List.of(new Dialogue.Response("Ладно",0,0));
                    Dialogue.current = d;
                }
            }
        };
        repo.put(24,script);

        /** Мама спалила, что Шкипер нашёл врача в интернете **/
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
                        .filter(dd->dd.text.contains("помочь"))
                        .findFirst().orElse(null);
                Dialogue.mom.get(Game.mom.dialogue).responses.remove(r);
                d.responses.add(new Dialogue.Response("Колескеъ...",0,26));
                d.responses.add(new Dialogue.Response("Стиралка...",0,27));
                d.responses.add(new Dialogue.Response("врач...",0,28));
            }
        }; repo.put(25,script);

        /** Мама проверяет, принес ли Шкипер колескеъ **/
        script = new Script() {
            @Override
            public void execute() {
                var ride = Game.currentLevel.objects.stream().filter(o->o.id==47).findFirst().orElse(null);
                if(ride == null) {Dialogue.current = Dialogue.mom.get(15); return;}
                for(var q: Game.player.quests) if(q.id==14) q.completed=true;
                var d = Dialogue.mom.get(Game.mom.dialogue);
                var r = d.responses.stream()
                        .filter(dd->dd.text.contains("ъ"))
                        .findFirst().orElse(null);
                Dialogue.mom.get(Game.mom.dialogue).responses.remove(r);
                Dialogue.current = Dialogue.mom.get(14);
            }
        }; repo.put(26,script);

        /** Мама проверяет, принес ли Шкипер стиралку **/
        script = new Script() {
            @Override
            public void execute() {
                var wash = Game.currentLevel.objects.stream().filter(o->o.id==140).findFirst().orElse(null);
                if(wash == null) {Dialogue.current = Dialogue.mom.get(17); return;}
                for(var q: Game.player.quests) if(q.id==16) q.completed=true;
                var d = Dialogue.mom.get(Game.mom.dialogue);
                var r = d.responses.stream()
                        .filter(dd->dd.text.contains("ира"))
                        .findFirst().orElse(null);
                Dialogue.mom.get(Game.mom.dialogue).responses.remove(r);
                Dialogue.current = Dialogue.mom.get(16);
            }
        }; repo.put(27,script);

        /** Мама проверяет, оплатил ли Шкипер операцию **/
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

        /** Кнопка "оплатить" на сайте врача **/
        script = new Script() {
            @Override
            public void execute() {
                if(Game.player.money >= 100_000){
                    Game.player.money -= 100_000;
                    var d = new Dialogue();
                    d.message = "Платеж совершен успешно!";
                    d.responses.add(new Dialogue.Response("Ладно",0,0));
                    Dialogue.current = d;
                    for(var q: Game.player.quests) if(q.id==20)q.completed = true;
                    Minigame.current = Minigame.get(20);

                }else{
                    var d = new Dialogue();
                    d.message = "Недостаточно денег!";
                    d.responses.add(new Dialogue.Response("Ладно",0,0));
                    Dialogue.current = d;
                }
            }
        }; repo.put(29,script);

        /** Скрипт мини-игры Домофон. Проверяем введенный пароль **/
        script = new Script() {
            @Override
            public void execute() {
                //Проходимся по мапе с паролями
                for (Map.Entry<Coordinates,String> domofon : padiquePasswords.entrySet())
                {
                    if(checkDomofonPass(domofon))
                    {
                        //Если введенный пароль подошел, переносим игрока.
                        Game.currentLevel.objects.remove(Game.player);
                        Game.currentLevel = Level.repo.get(domofon.getKey().levelId);
                        Game.player.x = domofon.getKey().x;
                        Game.player.y = domofon.getKey().y;
                        Game.currentLevel.objects.add(Game.player);
                    }
                }
                Minigame.current.input = ""; //Сбрасываем поле ввода
            }
        };
        repo.put(30,script);

        /** После оплаты операции мать проверяет, собрал ли Шкипер стиралку и колескеъ **/
        script = new Script() {
            @Override
            public void execute() {
                var ride = Game.player.quests.stream().anyMatch(q->(q.id==14&& q.completed));
                var wash = Game.player.quests.stream().anyMatch(q->(q.id==16&& q.completed));

                if(ride && wash){
                    Dialogue.current = Dialogue.mom.get(20);

                    var d = Dialogue.mom.get(Game.mom.dialogue);
                    var r = d.responses.stream()
                            .filter(dd->dd.text.contains("ач"))
                            .findFirst().orElse(null);
                    Dialogue.mom.get(Game.mom.dialogue).responses.remove(r);

                    Game.player.quests.add(Quest.get(19));


                }else{
                    var d = Dialogue.mom.get(19);
                    d.message = "Не все еще собрали.. \n Уж как смогу я выйти-то из дома так... \n Сперва мне нужно "
                            + " достать" + (!ride?" колескеъ":"") + ((!ride && !wash)?" и ":"") + (!wash?" стиральную машину":"");
                    Dialogue.current = d;
                }
            }
        }; repo.put(31,script);

        /** Скрипт молока **/
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

        /** Скрипт творога (и сырника) **/
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

        /** Скрипт сыра **/
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

        /** Скрипт хлеба **/
        script = new Script() {
            @Override
            public void execute() {

                if(Game.player.poo > 99) {Game.player.equip.count +=1; return;}
                Game.player.poo += 30;
                Game.player.hunger -= 15;
                Game.player.thirst += 30;
            }
        }; repo.put(43,script);

        /** Эффект пасты помодорро **/
        script = new Script() {
            @Override
            public void execute() {
            Game.player.poo += 25;
            Game.player.hunger -= 35;
            Game.player.hp += 15;
            }
        };repo.put(50,script);

        /** Эффект салата **/
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

        /** Эффект жареной курицы **/
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

        /** Эффект стейка **/
        script = new Script() {
            @Override
            public void execute() {
                if(Game.player.poo > 99) {Game.player.equip.count +=1; return;}
                Game.player.hp += 50;
                Game.player.hunger -= 70;
                Game.player.poo += 50;
            }
        }; repo.put(59,script);

        /** Эффект котлеты **/
        script = new Script() {
            @Override
            public void execute() {
                if(Game.player.poo > 99) {Game.player.equip.count +=1; return;}
                Game.player.hp += 20;
                Game.player.poo += 30;
                Game.player.hunger -= 30;
            }
        }; repo.put(60,script);

        /** Эффект куриного супа **/
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

        /** Эффект жареной картошки **/
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

        /** Скрипт борща **/
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

        /** Скрипт сигарет **/
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

        /** Скрипт двери в гараж **/
        script = new Script() {
            @Override
            public void execute() {
                //Перемещаем игрока в гараж
                Game.currentLevel.objects.remove(Game.player);
                Game.currentLevel = Level.repo.get(GARAGE);
                Game.currentLevel.objects.add(Game.player);
                Game.player.x = 200; Game.player.y = 200;
            }
        }; repo.put(70,script);

        /** Скрипт объекта труб **/
        script = new Script() {
            @Override
            public void execute() {
                //Ищем трубу
                var pipe = Game.currentLevel.objects.stream()
                        .filter(o->o.id==73).findFirst().orElse(null);
                if(pipe == null) return;
                //Убираем трубу с уровня
                Game.currentLevel.objects.remove(pipe);
                //Даем трубы Шкиперу как предмет
                Game.player.addItem(Item.get(95));
            }
        }; repo.put(73,script);

        /** Скрипт распаковки фитюли **/
        script = new Script() {
            //Распаковка фитюли
            @Override
            public void execute() {
                Game.player.addItem(Math.random() > 0.5 ? Item.get(1) : Item.get(7));// 50%50 трава или мука
            }
        };
        repo.put(77,script);

        /** Шкипер пьет бутылку пива **/
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

        /** Скрипт секатора **/
        script = new Script() {
            @Override
            public void execute() {
                //Проверяем, есть ли поблизости цветы
                //Создаем поле действия секатора (100x100 вокруг игрока)
                Rectangle zone =
                        new Rectangle(Game.player.x - 50, Game.player.y-50,100,100);
                //Находим куст, входящий в зону действия секатора
                GameObject bush = Game.currentLevel.objects.stream()
                        .filter(o->o.hitBox.intersects(zone) && o.id==67).findFirst().orElse(null);
                if(bush == null) return;
                //Если куст нашелся, удаляем его с уровня
                Game.currentLevel.objects.remove(bush);
                //Даем Шкиперу срезанный куст как предмет
                Game.player.addItem(Item.get(75));
            }
        };repo.put(88,script);
        /** Скрипт косяка**/
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
        /** Скрипт бумажки для самокруток **/
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
        /** Скрипт канистры **/
        script = new Script() {
            @Override
            public void execute() {
                //Зона действия канистры
                Rectangle zone =
                        new Rectangle(Game.player.x - 50, Game.player.y-50,100,100);
                //Ищем машину в зоне действия
                GameObject car = Game.currentLevel.objects.stream()
                        .filter(o->o.hitBox.intersects(zone) && o.name.contains("car")).findFirst().orElse(null);
                //Если нет, пока
                if(car == null) return;
                //Включаем миниигру слив бензина и сохраняем ссылку на нее в тачку
                if(car.minigame!= null) Minigame.current = car.minigame;
                else {
                    Minigame.current = Minigame.get(17);
                    car.minigame = Minigame.current;
                }
            }
        };repo.put(91,script);
        /** Скрипт садовой лопатки **/
        script = new Script() {
            @Override
            public void execute() {
                //Копать можно только на улице
                if(Game.currentLevel.id != STREET_1) return;
                //Копать можно только в зоне парка (id ==69)
                if(Game.currentLevel.objects.stream()
                        .noneMatch(o -> o.id==69 && o.hitBox.intersects(Game.player.hitBox)))
                    return;
                //Зона действия лопаты
                Rectangle zone =
                        new Rectangle(Game.player.x - 50, Game.player.y-50,100,100);
                //Ищем яму в зоне действия
                GameObject pit = Game.currentLevel.objects.stream()
                        .filter(o->o.hitBox.intersects(zone) && o.name.contains("pit")).findFirst().orElse(null);
                if(pit != null) return; //Если в зоне действия есть яма, выкопать новую нельзя
                //Иначе копаем яму
                pit = GameObject.get(66);//Создали новую яму
                //Ставим на уровень рядом со Шкипером
                pit.x = Game.player.x; pit.y = Game.player.y + Game.player.hitBox.height;
                Game.currentLevel.objects.add(pit);
                //С шансом даем шкиперу закладку
                if(Math.random() > 0.8) Game.player.addItem(Item.get(77));
                //Агрим мусоров
                Game.player.wanted += 20;
            }
        }; repo.put(93,script);
        /** Шкипер пьет бутылку воды **/
        script = new Script() {
            @Override
            public void execute() {
                if(Game.player.pee > 99){ Game.player.addItem(Item.get(101)); return;}
                Game.player.thirst -= 100;
                Game.player.pee += 30;
                Game.player.stamina += 200;
            }
        }; repo.put(101,script);


        /** Шкипер использует кошачий корм **/
        script = new Script() {
            @Override
            public void execute() {
                //Область действия
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

        /** Эффект шаурмы **/
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

        /** Эффект витаминов **/
        script= new Script() {
            @Override
            public void execute() {
                Game.player.sleepy += 40;
                Game.player.crazy -= 40;
            }
        }; repo.put(119,script);

        /** Эффект предмета стиральной машины **/
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

        /** Эффект кнопочного телефона мента **/
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
                    d.message = "Нет сигнала";
                    d.responses.add(new Dialogue.Response("ладно",0,0));
                    Dialogue.current = d;
                }
            }
        }; repo.put(129,script);


        /** Скрипт ларька с шаурмой **/
        script = new Script() {
            @Override
            public void execute() {
                Dialogue d = new Dialogue();
                d.message = "Эй ууажаэмий! Брэтэн, послушэй, тут туэкэя тэма йэсть... \n куорочэ... у мэна тут мйесо зэкончилос.. \n Я тэкую шэурму тут кручу... кэукэскую... \n уобщем.. йэсли ты мнэ мэса прынесьош, ну нэрмэльного \n Я тебе всеуо за сто рублэу зэкручу \n эбэлденную!";
                d.responses.addAll(List.of(new Dialogue.Response("ладно",0,0),
                                        new Dialogue.Response("вот мясо..",0,132_01)));
                Dialogue.current = d;
                Dialogue.companion = null;
                if(Game.player.quests.stream().noneMatch(q->q.id==56))
                    Game.player.quests.add(Quest.get(56));
            }
        }; repo.put(132,script);

        /** Шаурмен берет мясо **/
        script = new Script() {
            @Override
            public void execute() {

                Dialogue d = new Dialogue();

                var dogMeat = Game.player.items.stream()
                        .filter(i->i.id==117).findFirst().orElse(null);

                if(dogMeat!=null){

                    if(Game.player.money < 100) {
                        d.message = "Брэтэн, йэ жэ скэзэл тэбэ сто рублеу!";
                        d.responses.add(new Dialogue.Response("ладно",0,0));
                        Dialogue.current = d;
                        return;
                    }

                    dogMeat.count -= 1;

                    if(dogMeat.count <= 0)
                    { Game.player.items.remove(dogMeat); if(Game.player.equip == dogMeat) Game.player.equip = null; }

                    Game.player.addItem(Item.get(118));

                    Game.player.money -= 100;

                    d.message = "лээ, уот это нэрмэльнэе мэсо! \n Дэржи, как и обэщал. \n Похауай ";
                    d.responses.add(new Dialogue.Response("давай",0,0));
                    Dialogue.current = d;

                }else{
                    d.message = "Брэтэн, йэ жэ скэзэл - нэдэ нэрмэльнойэ мэсо!!";
                    d.responses.add(new Dialogue.Response("ладно",0,0));
                    Dialogue.current = d;
                }
            }
        }; repo.put(132_01,script);


        /** Вход на коробку **/
        script = new Script() {
            @Override
            public void execute() {
                Game.currentLevel.objects.remove(Game.player);
                Game.currentLevel = Level.repo.get(SOCCER_FIELD);
                Game.currentLevel.objects.add(Game.player);
                Game.player.x = 300; Game.player.y =300;
                //Если идет бой с фашиком, и он жив,
                // то при уходе/возвращении на коробку тот восстанавливает здоровье.
                if(Game.nazi.enemy && Game.nazi.hp > 0) Game.nazi.hp = GameObject.get(9191).hp;
            }
        };repo.put(136,script);

        /** Выход с коробки **/
        script = new Script() {
            @Override
            public void execute() {
                Game.currentLevel.objects.remove(Game.player);
                Game.currentLevel = Level.repo.get(STREET_1);
                Game.currentLevel.objects.add(Game.player);
                Game.player.x = 4050; Game.player.y =3900;
            }
        };repo.put(137,script);

        /** Шкипер пьет банку энергетоса **/
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

        /** Объект стиральной машины **/
        script = new Script() {
            @Override
            public void execute() {
                if(Game.currentLevel.id != TRAP_DUDE_PLACE) return;
                if(Game.player.quests.stream().noneMatch(q->q.id==62)) return;
                if(Game.player.items.size() >= ITEMS_CAPACITY) return;
                var machine = Game.currentLevel.objects.stream().filter(o->o.id==140).findFirst().get();
                Game.currentLevel.objects.remove(machine);
                Game.player.addItem(Item.get(128));
                var r = Dialogue.trap.get(Game.trap.dialogue).responses.stream().filter(res->res.text.contains("аме")).findFirst().orElse(null);
                Dialogue.trap.get(Game.trap.dialogue).responses.remove(r);
            }
        }; repo.put(140,script);

        /** Дверь в квартиру друга блатного авторитета **/
        script = new Script() {
            @Override
            public void execute() {
                var d = new Dialogue();
                d.message = "Слыш. Ты кто? Че ломишься?";
                d.responses.add(new Dialogue.Response("Открой",0,141_01));
                d.responses.add(new Dialogue.Response("<< Дернуть дверь >>",0,141_02));
                Dialogue.current = d;
            }
        }; repo.put(141,script);
        //Просим открыть
        script = new Script() {
            @Override
            public void execute() {
                var d = new Dialogue();
                d.message = "Куда? Ты че? \n Мужик, ты кто такой? \n Чего?! \n Ща, пдджи, \n Чего, Серёг? А?!... Водки?! Какой водки?.. \n А-а-а... водки! \n Слыш, мужик. А у тебя водка есть?";
                if(Game.player.items.stream().anyMatch(i->i.id==78))
                    d.responses.add(new Dialogue.Response("Есть",0,141_01_01));
                d.responses.add(new Dialogue.Response("Нет",0,141_01_02));
                Dialogue.current = d;
            }
        }; repo.put(141_01,script);
        //Даем водку
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
                d.message = "Опа, мужик \n А эттты нрмально зашел \n А?! ЧЕ?! Да, Серёг! \n Да прнёс водку, да \n Да иду, заебал, иду";
                d.responses.add(new Dialogue.Response("Ладно",0,0));
                Dialogue.current = d;
            }
        }; repo.put(141_01_01,script);

        //Не даем водку
        script = new Script() {
            @Override
            public void execute() {
                var d = new Dialogue();
                d.message = "Бля, а чет тогда прпёрса \n А?! \n Ну шшёл ннахуй отсюда";
                d.responses.add(new Dialogue.Response("ладно",0,0));
                d.responses.add(new Dialogue.Response("<< Дернуть дверь >>",0,141_02));
                Dialogue.current = d;
            }
        }; repo.put(141_01_02,script);

        //Дернули дверь
        script = new Script() {
            @Override
            public void execute() {
                var d = new Dialogue();
                d.message = "Слыш! Одурел? \n Ты хули дергаешь? \n А ну съебал отсюда";
                d.responses.add(new Dialogue.Response("ладно",0,0));
                d.responses.add(new Dialogue.Response("<< Дернуть дверь >>",0,141_02_01));
                Dialogue.current = d;
            }
        }; repo.put(141_02,script);
        //Нас впустили пиздить
        script = new Script() {
            @Override
            public void execute() {
                var d = new Dialogue();
                d.message = "Ну всё, тебе пизда";
                d.responses.add(new Dialogue.Response("ладно",0,0));
                Dialogue.current = d;
                Game.switchLevel(BANDIT_FRIEND_FLAT);
                Game.player.x = 440; Game.player.y = 440;
            }
        }; repo.put(141_02_01,script);

        /** Скрипт двери в пустую квартиру блатного авторитета  **/
        script = new Script() {
            @Override
            public void execute() {
                var d = new Dialogue();
                d.message = "Дверь заперта. Нужен ключ";
                d.responses.add(new Dialogue.Response("ладно",0,0));
                if(Game.player.items.stream().anyMatch(i->i.id==130))
                    d.responses.add(new Dialogue.Response("Открыть дверь ключом",0,142_01));
                Dialogue.current = d;

                }
        }; repo.put(142,script);
        //Открыли дверь ключом
        script = new Script() {
            @Override
            public void execute() {
                Game.switchLevel(BANDIT_FLAT);
                Game.player.y = 290; Game.player.x = 500;
            }
        }; repo.put(142_01,script);

        /** Скрипт выхода на руины крыши (зона) **/
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
        /** Скрипт люка из города в канализацию **/
        script = new Script() {
            @Override
            public void execute() {
                if(Game.player.hat == null || Game.player.hat.id != 6){
                    var d= new Dialogue();
                    d.message = "Из люка жестко разит. \n Без противогаза там не выжить";
                    d.responses.add(new Dialogue.Response("ладно",0,0));
                    Dialogue.current = d;
                }else{
                    Game.switchLevel(SEWERS);
                    Game.player.x = 130;
                    Game.player.y = 230;
                }
            }
        }; repo.put(149,script);
        /** скрипт выхода в канализацию из склада пятерочки **/
        script = new Script() {
            @Override
            public void execute() {
                Game.switchLevel(SEWERS);
                Game.player.x = 1200;
                Game.player.y = 2200;
            }
        }; repo.put(150,script);
        /** Скрипт объекта урана **/
        script = new Script() {
            @Override
            public void execute() {
                var uranObj = Game.currentLevel.objects.stream().filter(o->o.id==152).findFirst().orElse(null);
                if(uranObj != null) Game.currentLevel.objects.remove(uranObj);
                Game.player.addItem(Item.get(97));
            }
        }; repo.put(152,script);

        /** Скрипт банкомата **/
        script = new Script() {
            @Override
            public void execute() {
                //TODO: переделать под миниигру
                var d = new Dialogue();
                d.message = "Меню банкомата \n ";
                if(Game.player.items.stream().anyMatch(i->i.id==74))
                    d.responses.add(new Dialogue.Response("Вставить карту",0,154_01));
                if(Game.player.items.stream().anyMatch(i->i.id==76))
                    d.responses.add(new Dialogue.Response("Установить устройство",0,154_02));
                d.responses.add(new Dialogue.Response("Выйти (ладно)",0,0));
                Dialogue.current = d;
            }
        }; repo.put(154,script);
        //Вставили карту в банкомат
        script = new Script() {
            @Override
            public void execute() {
                if(Game.player.atm){
                    //если атм крякнутый
                    var c = Game.player.items.stream().filter(i->i.id==74).findFirst().get();
                    c.count -=1; if(c.count<1) Game.player.items.remove(c); if(Game.player.equip==c) Game.player.equip = null;
                    int withdraw = 500 + (int)(Math.random() * 4_500);
                    Game.player.money += withdraw;
                    Game.player.wanted += 13;
                    var d = new Dialogue();
                    d.message = "Банкомат зажевал карту, \n но выдал деньги. \n Сумма: " + withdraw;
                    d.responses.add(new Dialogue.Response("Ладно",0,0));
                    Dialogue.current = d;
                }else{
                    var d = new Dialogue();
                    d.message = "Пин-код введен неверно. ";
                    d.responses.add(new Dialogue.Response("Ладно",0,0));
                    Dialogue.current = d;
                }
            }
        }; repo.put(154_01,script);
        //Установили устройство на банкомат
        script = new Script() {
            @Override
            public void execute() {
                var device = Game.player.items.stream().filter(i->i.id==76).findFirst().get();
                Game.player.items.remove(device); if(Game.player.equip==device) Game.player.equip = null;
                Game.player.atm = true;
                var d = new Dialogue();
                d.message = "Устройство установлено. ";
                d.responses.add(new Dialogue.Response("Ладно",0,0));
                Dialogue.current = d;
                for (var q: Game.player.quests) if(q.id == 29) q.completed = true;

            }
        }; repo.put(154_02,script);

        //==========================================================================================//
        /**
         *
         * СКРИПТЫ ХАКЕРА (КОМПЬЮТЕРЩИКА)
         *
         */
        /** Хакер выдает задания
        // <<Мне бы маме помочь>> **/
        script = new Script() {
            @Override
            public void execute() {
                //Если выполнил последний квест
                if(Game.player.quests.stream().anyMatch(q-> q.id == 29 && q.completed)){
                    var d = new Dialogue();
                    d.message = "Чел, слушай. \n Я не знаю что там у твоей мамы\n но я знаю, что деньги могут решить почти любую проблему \n Ты можешь снять деньги с любой карты в нашем банкомате \n Благодаря тому установленному устройству \n Короче я не знаю чего ты ждешь, чувак...";
                    d.responses.add(new Dialogue.Response("Ладно",0,0));
                    Dialogue.current = d;
                }
                //Если нет никаких заданий (кроме "собрать комп" и "взбодриться")
                if(Game.player.quests.stream().noneMatch(q->(q.owner.equals("хакер") && (q.id!=5&&q.id!=54))))
                {
                    //Выдаем задание 11 (срочные покупки)
                    Game.player.quests.add(Quest.get(11));
                    //Добавляем реплику для сдачи квеста
                    Dialogue.hacker.get(4).responses.add(new Dialogue.Response("Купил..",0,1111_4));
                    //Выводим соответствующий диалог
                    Dialogue.current = Dialogue.hacker.get(9);
                    return;
                }
                //Если у Шкипера есть задание от хакера, которое тот еще не выполнил (кроме сборки компа и размута скорости).
                if(Game.player.quests.stream().anyMatch(q->(q.owner.equals("хакер")&& (q.id!=5 && q.id!=54) && !q.completed)))
                {
                    //Выводим соответствующий диалог
                    Dialogue.current = Dialogue.hacker.get(7);
                    return;
                }
                //Если Шкипер получил и выполнил квест со смазкой (срочные покупки) и не получал квест с посылкой
                if(Game.player.quests.stream().anyMatch(q->q.id==11&&q.completed) && Game.player.quests.stream().noneMatch(q->q.id==26))
                {
                    //Даём квест 26 (Посылка с валберса)
                    Game.player.quests.add(Quest.get(26));
                    //Выводим соответствующий диалог
                    Dialogue.current = Dialogue.hacker.get(10);
                    //Добавляем реплику в диалог с ловушкой из валберс
                    Dialogue.trap.get(1).responses.add(new Dialogue.Response("Код 8814",0,5555_2));
                    //Добавляем реплику для сдачи квеста
                    Dialogue.hacker.get(4).responses.add(new Dialogue.Response("Посылка",0,1111_7));
                    return;
                }
                //Если Шкипер получил и выполнил квест с посылкой и не получал квеста с кремнием
                if(Game.player.quests.stream().anyMatch(q->q.id==26&&q.completed) && Game.player.quests.stream().noneMatch(q->q.id==13))
                {
                    //Выдаем квест с кремнием 13 (кремниевая плотина)
                    Game.player.quests.add(Quest.get(13));
                    //Выводим диалог
                    Dialogue.current = Dialogue.hacker.get(11);
                    //Добавляем кремень в озеро
                    GameObject rockSi = GameObject.get(42);
                    rockSi.x=9000;rockSi.y=5000;
                    Level.repo.get(STREET_1).objects.add(rockSi);
                    //Добавляем реплику на сдачу задания
                    Dialogue.hacker.get(4).responses.add(new Dialogue.Response("Камень...",0,1111_11));
                }
                //Если Шкипер выполнил квест с кремнием 13 (Кремниевая плотина)
                if(Game.player.quests.stream().anyMatch(q->q.id==13&&q.completed) && Game.player.quests.stream().noneMatch(q->q.id==27))
                {
                    //Выдаем квест 27 (собрать пластик)
                    Game.player.quests.add(Quest.get(27));
                    //Выводим диалог
                    Dialogue.current = Dialogue.hacker.get(13);
                    //Добавляем реплику для сдачи квеста
                    Dialogue.hacker.get(4).responses.add(new Dialogue.Response("Пластик..",0,1111_12));
                }
                //Если Шкипер выполнил квест с пластиком (27)
                if(Game.player.quests.stream().anyMatch(q->q.id==27&&q.completed) && Game.player.quests.stream().noneMatch(q->q.id==28))
                {
                    //Выдаем квест 28 (Подарить цветы)
                    Game.player.quests.add(Quest.get(28));
                    //Выводим диалог
                    Dialogue.current = Dialogue.hacker.get(20);
                    //Добавляем реплику ловушке из валберс (для сдачи квеста)
                    Dialogue.trap.get(1).responses.add(new Dialogue.Response("Цветы.",0,5555_3));
                }
                //Если Шкипер выполнил квест с цветами (28)
                if(Game.player.quests.stream().anyMatch(q->q.id==28&&q.completed) && Game.player.quests.stream().noneMatch(q->q.id==29))
                {
                    //Выдаем квест 29 (установить устройство)
                    Game.player.quests.add(Quest.get(29));
                    //Выводим диалог
                    Dialogue.current = Dialogue.hacker.get(21);
                    //Даем игроку устройство
                    Game.player.items.add(Item.get(76));
                }
            }
        };repo.put(1111,script);

        /** Хакер проверяет, принес ли Шкипер фитюлю **/
        script = new Script() {
            @Override
            public void execute() {
                //Ищем фитюлю
                Item flour = null;
                for(var i : Game.player.items) if(i.id==77) flour = i;
                if(flour != null) {
                    //Если нашлась, отнимаем одну
                    if(flour.count == 1) Game.player.items.remove(flour);
                    flour.count -= 1;
                    //включаем диалог
                    Dialogue.current = Dialogue.hacker.get(3);
                    //Выполняем 10 квест (принести фитюлю)
                    for(var q: Game.player.quests) if(q.id==10) q.completed = true;
                    //Устанавливаем хакеру другой диалог как коренной
                    Game.hacker.dialogue = 4;
                    //Даем игроку деньги
                    Game.player.money += 2_000;
                }else{
                    //Если не нашлась фитюля
                    //Выводим соответствующее сообщение
                    Dialogue.current = Dialogue.hacker.get(2);
                }
            }
        };
        repo.put(1111_2,script);

        /** Хакер выдает Шкиперу задание собрать комп **/
        script = new Script() {
            @Override
            public void execute() {
                //Если скрипт уже срабатывал, закрываем диалог и выходим из скрипта
                if(expired) {Dialogue.current=null; return;}
                //Выдали задание
                Game.player.quests.add(Quest.get(5));
                //Создаем объект компа
                GameObject pc = GameObject.get(40);
                pc.x = 330; pc.y = 250;
                //И добавляем на уровень
                Level.repo.get(HACKERS_PLACE).objects.add(pc);
                //Закрываем диалог
                Dialogue.current = null;
                //Скрипт одноразовый
                expired=true;
            }
        }; repo.put(1111_3,script);

        /** Хакер проверяет, принес ли Шкипер морковь и смазку... **/
        script = new Script() {
            @Override
            public void execute() {
                //Ищем морковь
                Item carrot = null; for(var i:Game.player.items) if(i.id==56) carrot = i;
                //И смазку
                Item lube = null; for(var i:Game.player.items) if(i.id==65) lube = i;
                //Если и то и то в наличии
                if(carrot != null && lube != null) {
                    //Квест выполнен
                    for(var q:Game.player.quests)if(q.id==11)q.completed = true;
                    //Забираем морковь и смазку
                    Game.player.items.remove(carrot); Game.player.items.remove(lube);
                    if(Game.player.equip == carrot || Game.player.equip == lube) Game.player.equip = null;
                    //Игрок получает компенсацию затрат
                    Game.player.money += Item.get(56).price; //За морковь
                    Game.player.money += Item.get(65).price; //И смазку
                    //Удаляем фразу для сдачи квеста
                    Dialogue.Response r = Dialogue.hacker.get(4).responses.stream().filter(rr->rr.text.contains("упил")).findFirst().orElse(null);
                    if(r!=null) Dialogue.hacker.get(4).responses.remove(r);
                    //Выводим соответствующий диалог
                    Dialogue.current = Dialogue.hacker.get(8);
                }
                else {
                    //Если смазки нет, выводим диалог
                    Dialogue d = new Dialogue();
                    d.message = "Чел, ты ничего не купил? \n Блин, я же просил тебя, мне срочно как бы..";
                    d.responses = List.of(new Dialogue.Response("А..",0,0));
                    Dialogue.current =d;
                }
            }
        };repo.put(1111_4,script);

        /** Скрипт компьютера-объекта
        //Вызывается когда шкипер берет комп Хакера **/
        script = new Script() {
            @Override
            public void execute() {
                //Если у игрока нет места, он не может взять комп
                if(Game.player.items.size()>=ITEMS_CAPACITY) return;
                //Удаляем объект компа с уровня
                GameObject pc = null; for(var o:Level.repo.get(HACKERS_PLACE).objects) if(o.id==40) pc=o;
                if(pc!=null) Level.repo.get(HACKERS_PLACE).objects.remove(pc);
                //Добавляем комп как предмет Шкиперу в инвентарь
                Game.player.items.add(Item.get(72));
            }
        };repo.put(1111_5,script);

        /** Скрипт распаковки компьютера
        //Вызывается когда шкипер использует комп из инвентаря **/
        script = new Script() {
            @Override
            public void execute() {
                //Проверяем, дома ли мы
                if(Game.currentLevel.id!=HUB) return; //Поставить комп можно только дома!
                //Если мы дома, ставим комп дома
                GameObject pc = GameObject.get(41);
                pc.x=280;pc.y=100;
                Level.repo.get(HUB).objects.add(pc);
                //И забираем его из рук игрока
                Game.player.items.remove(Game.player.equip);
                Game.player.equip=null;
            }
        };repo.put(1111_6,script);

        /** Хакер проверяет, принес ли Шкипер посылку **/
        script = new Script() {
            @Override
            public void execute() {
                //Смотрим, есть ли посылка в инвентаре
                Item delivery = null;
                for(var i:Game.player.items) if(i.id==73) delivery = i;
                //Если посылка есть
                if(delivery != null) {
                    //Забираем посылку
                    Game.player.items.remove(delivery);
                    //И из рук тоже
                    if(Game.player.equip==delivery) Game.player.equip=null;
                    //Даем деньги
                    Game.player.money += 2_500;
                    //Выполняем квест
                    for(var q:Game.player.quests)if(q.id==26)q.completed = true;
                    //Выводим диалог
                    Dialogue.current = Dialogue.hacker.get(14);
                    //Удаляем старую реплику
                    //Сперва ищем ее
                    Dialogue.Response response =
                            Dialogue.hacker.get(4).responses.stream()
                                    .filter(r->r.text.contains("ылка"))
                                    .findFirst().orElse(null);
                    //Если нашлась, удаляем
                    if(response!=null) Dialogue.hacker.get(4).responses.remove(response);
                } else{
                    Dialogue d = new Dialogue();
                    d.message = "Ну да, посылка. С Валберса... \n Ты ее забрал?";
                    d.responses = List.of(new Dialogue.Response("а, да, ща...",0,0));
                    Dialogue.current =d;
                }
            }
        };repo.put(1111_7,script);

        /** Хакер рассказывает Шкиперу - что тот принес.
        //И открывает возможность продажи. **/
        script = new Script() {
            @Override
            public void execute() {
                //Рассказываем Шкиперу про спиды.
                Dialogue.current = Dialogue.hacker.get(15);
                //Меняем диалог, добавляем реплику для продажи.
                Dialogue.Response response = null;
                for(var r:Dialogue.hacker.get(4).responses) if(r.text.contains("принес")) response = r;
                if(response != null) {response.text = "Принес тебе ещё.";response.script=11119;}
                //Добавляем квест Шкиперу (взбодриться)
                Game.player.quests.add(Quest.get(54));
            }
        }; repo.put(1111_8,script);

        /** Хакер покупает скорость у шкипера **/
        script = new Script() {
            @Override
            public void execute() {
                //Ищем спиды в инвентаре Шкипера
                Item flour = Game.player.items.stream().filter(i->i.id==1).findFirst().orElse(null);
                //Если ее нет, сообщаем об этом Шкиперу
                if(flour==null) {Dialogue.current = Dialogue.hacker.get(16);return;}
                //Если есть, забираем (и из рук)
                Game.player.items.remove(flour);
                if(Game.player.equip==flour) Game.player.equip=null;
                //Даем шкиперу деньги
                int reward = 2_000 * flour.count;
                Game.player.money += reward;
                //Выводим сообщение
                Dialogue d = new Dialogue();
                d.message = "О! Ахуенно. Вот, твои "+reward+" р.";
                d.responses = List.of(new Dialogue.Response("Ага",0,0));
                Dialogue.current = d;
            }
        };repo.put(1111_9,script);

        /** Скрипт камня кремния когда его поднимаешь **/
        script = new Script() {
            @Override
            public void execute() {
                //Если нет места в карманах, не поднимаем
                if(Game.player.items.size()>=ITEMS_CAPACITY) return;
                //Если место есть, поднимаем камень
                GameObject rockSi = Game.currentLevel.objects.stream().filter(i->i.id==42).findFirst().orElse(null);
                if(rockSi!=null) Game.currentLevel.objects.remove(rockSi);
                //И даем игроку в карман
                Game.player.items.add(Item.get(70));
            }
        }; repo.put(1111_10,script);

        /** Хакер проверяет, принес ли Шкипер кремний **/
        script = new Script() {
            @Override
            public void execute() {
                //Ищем кремний в инвентаре
                Item si = Game.player.items.stream().filter(i->i.id==70).findFirst().orElse(null);
                //Если не нашли, напоминаем Шкиперу - что да как
                if(si==null) {Dialogue.current = Dialogue.hacker.get(17);return;}
                //Если кремний нашелся, забираем его
                Game.player.items.remove(si); if(Game.player.equip==si) Game.player.equip=null;
                //Даем игроку процессор
                Game.player.addItem(Item.get(71));
                //Выводим диалог
                Dialogue.current = Dialogue.hacker.get(12);
                //Выполняем квест
                for (var q: Game.player.quests) if(q.id==13) q.completed = true;
                //Удаляем реплику для сдачи квеста
                Dialogue.Response response = Dialogue.hacker.get(4).responses.stream()
                        .filter(r->r.text.contains("амен")).findFirst().orElse(null);
                if(response!=null) Dialogue.hacker.get(4).responses.remove(response);
            }
        }; repo.put(1111_11,script);

        /** Хакер проверяет, принес ли Шкипер пластиковые карты **/
        script = new Script() {
            @Override
            public void execute() {
                //Ищем карточки у Шкипера
                Item cards = Game.player.items.stream().filter(i->i.id==74).findFirst().orElse(null);
                //Если не нашлись, или их мало, сообщаем Шкиперу
                if(cards==null || cards.count < 10) {Dialogue.current = Dialogue.hacker.get(18);return;}
                //Если нашлись, забираем
                Game.player.items.remove(cards); if(Game.player.equip==cards) Game.player.equip=null;
                //Выполняем квест
                for (var q:Game.player.quests) if(q.id==27) q.completed = true;
                //Выводим сообщение
                Dialogue.current = Dialogue.hacker.get(19);
                //Удаляем реплику для сдачи квеста
                Dialogue.Response response = Dialogue.hacker.get(4).responses.stream()
                        .filter(r->r.text.contains("астик")).findFirst().orElse(null);
                if(response!=null) Dialogue.hacker.get(4).responses.remove(response);
            }
        };repo.put(1111_12,script);
        //==========================================================================================//
        /**
         *
         * СКРИПТЫ ХАЧА
         *
         */
        /**ШКИПЕР взял тестовый квест **/
        script = new Script() {
            @Override
            public void execute() {
                //Даем игроку тестовый квест
                Game.player.quests.add(Quest.get(55));
                //Выводим тестовую реплику
                Dialogue.current = Dialogue.hach.get(0);
                //Добавляем реплику для сдачи квеста
                Dialogue.hach.get(1).responses.add(new Dialogue.Response("СДЕЛАЛ",0,1489));

            }
        }; repo.put(1488,script);

        /** Шкипер пытается сдать тестовый квест **/
        script = new Script() {
            @Override
            public void execute() {
                //Проверям, выполнил ли Шкипер задание
                if(true /*Проверка условия квеста */){
                    //Выполняем квест если да
                    for(var q:Game.player.quests) if(q.id == 55) q.completed = true;
                    //Удаляем реплику для сдачи квеста из диалога
                    Dialogue.Response response = Dialogue.hach.get(1).responses.stream()
                            .filter(r->r.text.equals("СДЕЛАЛ")).findFirst().orElse(null);
                    if(response != null) {
                        Dialogue.hach.get(1).responses.remove(response);
                    }
                }
                Dialogue.current = Dialogue.hach.get(3);
            }
        }; repo.put(1489,script);


        /** Выдача нового задания
        // <<Мне бы маме помочь>> **/
        script = new Script() {
            @Override
            public void execute() {
                //Если у игрока нет квестов хача
                if (Game.player.quests.stream().noneMatch(q->(q.owner.equals("хачик")&& q.id!=35))) {
                    Game.player.quests.add(Quest.get(9)); //Выдаем первый квест
                    Dialogue.current = Dialogue.hach.get(5); //Выводим соответствующий диалог
                    //Добавляем ответ в основной корень диалога (для сдачи квеста)
                    Dialogue.hach.get(Game.hach.dialogue).responses.add(new Dialogue.Response("Вот, нашел.",0,22221));
                    Game.player.passwords.add("Под. с фитюлей - 417");
                    return;
                }
                //Если игрок взял квест хача, но не выполнил него
                if(Game.player.quests.stream().anyMatch(q->q.owner.equals("хачик")&& !q.completed && q.id!=35))
                {
                    Dialogue.current = Dialogue.hach.get(6);
                    return;
                }
                //Если игрок выполнил квесты с фитюлей и ждет новый
                if(Game.player.quests.stream().anyMatch(q->q.id==9 && q.completed)
                    && Game.player.quests.stream().anyMatch(q->q.id==10 && q.completed))
                {
                    //Даем игроку квест 36 (Раскопки и поиски)
                    Game.player.quests.add(Quest.get(36));
                    //Выводим соответствующую реплику
                    Dialogue.current = Dialogue.hach.get(10);
                    //Дополняем основной диалог хача для сдачи задания
                    Dialogue.hach.get(Game.hach.dialogue).responses.add(new Dialogue.Response("Я нашёл",0,22223));
                }
            }
        };
        repo.put(2222,script);

        /** Проверка выполнения задания 9 (Сгонять на адрес) **/
        script = new Script() {
            @Override
            public void execute() {
                //Проверяем что игрок принес фитюлю
                boolean got = Game.player.items.stream().anyMatch(i -> i.id==77);
                //Если нет
                if(!got) {
                    //Сообщаем о необходимости принести фитюлюpo0[[[
                    Dialogue d = new Dialogue();
                    d.message = "Брат, ну что, где там эта штучка, почему ты еще ее не принес?";
                    d.responses = List.of(new Dialogue.Response("Ща",0,0));
                    Dialogue.current = d;
                    return;
                }
                //Если да
                //Выполняем задание
                for(var quest : Game.player.quests) if(quest.id==9) quest.completed = true;
                //Удаляем реплику для сдачи задания
                Dialogue.hach.get(Game.hach.dialogue).responses
                        .remove(Dialogue.hach.get(Game.hach.dialogue).responses.size()-1);
                //Включаем диалог
                Dialogue.current = Dialogue.hach.get(7);
                //Даем задание 10 (отнести фитюлю)
                Game.player.quests.add(Quest.get(10));
                //Добавляем реплику хакеру для сдачи квеста
                Dialogue.hacker.get(1).responses
                        .add(new Dialogue.Response("Вот. Принёс",0,11112));
                Game.player.passwords.add("Хакер - 666");
            }

        }; repo.put(2222_1,script);

        /** Шкипер согласился купить кроссовки Хачика **/
        script = new Script() {
            @Override
            public void execute() {
                //Добавляем квест (купить кроссовки)
                if(!expired) Game.player.quests.add(Quest.get(35));
                //Сбрасываем текущий диалог
                Dialogue.current = null;
                //Ставим отметку о выполнении скрипта (скрипт одноразовый)
                expired = true;
            }
        }; repo.put(2222_5,script);

        /** Хачик вспоминает, покупал и носит ли сейчас Шкипер его кроссы. **/
        script = new Script() {
            @Override
            public void execute() {
                //Проверяем, покупал ли Шкипер тапки вовсе
                boolean bought = Game.player.quests.stream().anyMatch(q->q.id==35&&q.completed);
                //Проверяем, носит ли он их сейчас
                boolean wears = Game.player.foot != null && Game.player.foot.id == 4;
                //Выводим соответствующие реплики
                if(!bought && !wears) Dialogue.current = Dialogue.hach.get(3);
                else if(bought && !wears) Dialogue.current = Dialogue.hach.get(8) ;
                else if(wears) Dialogue.current = Dialogue.hach.get(9);
            }
        }; repo.put(2222_6,script);

        /** Шкипер толкает траву Хачику**/
        script = new Script() {
            @Override
            public void execute() {
                //Проверяем есть ли трава
                Item weed = null;
                for(var i : Game.player.items) if(i.id==7) weed = i;
                //Если есть
                if(weed != null) {
                    //Забираем траву
                    Game.player.items.remove(weed);
                    if(Game.player.equip==weed) Game.player.equip = null;
                    //Даем бабки
                    Game.player.money += 1_000 * weed.count;
                    //Выводим сообщение
                    Dialogue d = new Dialogue();
                    d.message = "А бля отдуши братуха!!! \n Вот это ТЫ КОНКРЕТНО КРАСАВА ВНАТУРЕ. \n Твои " + (weed.count * 1_000 + " р.");
                    d.responses = List.of(new Dialogue.Response("Ага",0,0));
                    Dialogue.current = d;
                }
                //Если нет
                if (weed == null) {
                    //Выводим сообщение
                    Dialogue d = new Dialogue();
                    d.message = "Братан, я же тебе объяснял. \n Меня интересует только зеленая тема \n Это мой кайф. \n Гашик, шишки, вот это все, понял. \n Ты пошурши поиши в городе вот эти свертки шмертки \n Посмотри, если там запаковано что-то зеленое \n липкое, пахучее \n что-то сочное \n то неси эту радость сразу ко мне, беги мигом";
                    d.responses = List.of(new Dialogue.Response("Ладно",0,0));
                    Dialogue.current = d;
                }
            }
        };
        repo.put(22223,script);
        //==========================================================================================//

        /**
         *
         *
         * СКРИПТЫ МЯСНИКА
         *
         */
        /** Скрипт выдачи заданий
         * <<Мне бы маме помочь>> **/
        script = new Script() {

            @Override
            public void execute() {
                //Если у Шкипера нет квеста Мясника
                if(Game.player.quests.stream().noneMatch(q->q.owner.equals("мясник")))
                //Выводим диалог с предложением взять первый
                    Dialogue.current = Dialogue.butcher.get(1);
                //Если Шкипер взял квест найти дочку и не нашел
                if(Game.player.quests.stream().anyMatch(q->q.id==6&&!q.completed))
                    //Напоминаем ему об этом
                    Dialogue.current = Dialogue.butcher.get(5);
                //Если Шкипер нашел дочку, и еще не брал квест "кража пацана"
                if(Game.player.quests.stream().anyMatch(q->q.id==6&&q.completed)
                && Game.player.quests.stream().noneMatch(q->q.id==32))
                    //Выводим диалог с предложением взять "кража пацана"
                    Dialogue.current = Dialogue.butcher.get(8);
                //Если Шкипер выполнил "кража пацана", но не взял "где деньги 1"
                if(Game.player.quests.stream().noneMatch(q->q.id==12) && Game.player.quests.stream().anyMatch(q->q.id==32&&q.completed))
                    //Прелагаем взять квест "где деньги ч1"
                    Dialogue.current = Dialogue.butcher.get(13);
                //Если Шкипер выполнил "где деньги ч1", но не взял ч2
                if(Game.player.quests.stream().noneMatch(q->q.id==33) && Game.player.quests.stream().anyMatch(q->q.id==12&&q.completed))
                    Dialogue.current = Dialogue.butcher.get(20);
                if(Game.player.quests.stream().anyMatch(q->q.id==33&&q.completed) && Game.player.quests.stream().noneMatch(q->q.id==34)){
                    repo.get(3333_21).execute();
                }
            }
        }; repo.put(3333,script);
        /** Мясник выдает задание найти дочку **/
        script = new Script() {
            @Override
            public void execute() {
                //Даем задание
                Game.player.quests.add(Quest.get(6));
                //И закрываем диалог
                Dialogue.current = null;

            }
        }; repo.put(3333_1,script);

        /** Скрипт зоны входа в школу **/
        script = new Script() {
            @Override
            public void execute() {
                //Школа открыта только днем
                if(GameTime.getTimeOfTheDay()!=DAY) return;
                //Убираем Шкипера с улицы
                Game.currentLevel.objects.remove(Game.player);
                //Ставим в школу
                Game.currentLevel = Level.repo.get(SCHOOL);
                Game.currentLevel.objects.add(Game.player);
                Game.player.x = 200; Game.player.y = 330;
            }
        }; repo.put(3333_2,script);
        /** Скрипт зоны выхода из школы **/
        script = new Script() {
            @Override
            public void execute() {
                //Убираем Шкипера из школы
                Game.currentLevel.objects.remove(Game.player);
                //Ставим на улицу
                Game.currentLevel = Level.repo.get(STREET_1);
                Game.currentLevel.objects.add(Game.player);
                Game.player.x = 6600; Game.player.y = 8000;
            }
        }; repo.put(3333_4,script);
        /** Скрипт учительницы **/
        script = new Script() {
            @Override
            public void execute() {
                //Выводим диалог учительницы
                Dialogue d = new Dialogue();
                d.message = "Молодой человек, вы ничего не перепутали? \n А ну выметайтесь из моего класса! \n Я Вас не знаю! \n Иначе вызову охрану! \n Уходите";
                d.responses = List.of(new Dialogue.Response("Извините..",0,0));
                Dialogue.current = d;
            }
        };repo.put(3333_5,script);
        /** Скрипт школьника 1 **/
        script = new Script() {
            @Override
            public void execute() {
                //Выводим первую фразу школьника
                Dialogue d = new Dialogue();
                d.message = "Дядь, ты че? Это школа...";
                if(Game.player.quests.stream().anyMatch(q->q.id==6))
                    d.responses.add(new Dialogue.Response("Где малолетки ошиваются?",0,3333_7));
                d.responses.add(new Dialogue.Response("ладно",0,0));
                Dialogue.current = d;
            }
        };repo.put(3333_6,script);
        /** Скрипт школьника 2 **/
        script = new Script() {
            @Override
            public void execute() {
                //Выводим предложение купить пива
                Dialogue d = new Dialogue();
                d.message = "Ага, так я те все и рассказал. \n Короче, дядя. Слушай. \n Если те чет от меня надо, метнись и купи мне пивка \n А там посмотрим буду я с тобой говорить или нет";
                d.responses = List.of(new Dialogue.Response("Ладно",0,3333_8));
                Dialogue.current = d;
            }
        };repo.put(3333_7,script);
        /** Скрипт школьника 3 **/
        script = new Script() {
            @Override
            public void execute() {
                //Проверяем, принес ли шкипер пиво
                Item beer = Game.player.items.stream().filter(i->i.id==84).findFirst().orElse(null);
                //Готовим диалог
                Dialogue d = new Dialogue();
                //Если пиво есть, рассказываем где тусят пиздюки и предлагаем отвести.
                if(beer!=null) {
                    d.message = "О, норм. Пивко. Давай его сюда. \n Так, короче, смотри. Есть тут место, где обычно собираются бухать \n Не ебу зачем тебе, но если надо, могу туда отвести. \n Ты иди со мной, только молча и позади. \n У тебя такое ебало стремное, с тобой вряд ли захотят вообще пиздеть.. \n Короче я тебя представлю, дальше там уж как пойдет. \n Ну, че стоим?";
                    d.responses = List.of(new Dialogue.Response("Пошли", 0, 3333_9));
                    Game.player.items.remove(beer);
                    if(Game.player.equip==beer)Game.player.equip=null;
                }else{
                    //Если пива нет, шлём нахуй
                    d.message = "Прохладно, епт! \n Те че от меня надо, лысый? \n Давай пиздуй за пивом или разговор окончен.";
                    d.responses = List.of(new Dialogue.Response("ладно", 0, 0));
                }
                Dialogue.current = d;
            }
        };repo.put(3333_8,script);
        /** Скрипт школьника 4, отводим на площадку к другим пиздюкам **/
        script = new Script() {
            @Override
            public void execute() {
                //Удаляем Шкипера с уровня
                Game.currentLevel.objects.remove(Game.player);
                //И школотрона
                GameObject scholar = Game.currentLevel.objects.stream().filter(o->o.id==3333_2).findFirst().orElse(null);
                if(scholar!=null) Game.currentLevel.objects.remove(scholar);
                //Ставим в движок уровень улицу
                Game.currentLevel = Level.repo.get(STREET_1);
                //И добавляем туда наших ребят
                Game.currentLevel.objects.add(Game.player);
                Game.currentLevel.objects.add(scholar);
                Game.player.x = 4500; Game.player.y = 3100;
                scholar.x = 4500; scholar.y = 3100;
                //И бухающую школоту
                Game.girl.x = 4400; Game.girl.y = 3200;
                Level.repo.get(STREET_1).objects.add(Game.girl);
                GameObject guy = GameObject.get(4444_1); guy.x = 4350; guy.y = 3150; Level.repo.get(STREET_1).objects.add(guy);
                guy = GameObject.get(4444_2); guy.x = 4250; guy.y = 3100; Level.repo.get(STREET_1).objects.add(guy);
                //Выводим новый диалог.
                Dialogue d = new Dialogue();
                d.message = "Ну вот. Обычно все из нашей школы собираются бухать вот тут. \n Ща тут как раз есть несколько ребят. \n Костян, Володя, и, вот - Лера. \n Ладно, давай, я похуячу по домам. \n А то мамашка опиздюлит";
                d.responses = List.of(new Dialogue.Response("ладно",0,0));
                Dialogue.current = d;
            }
        }; repo.put(3333_9,script);
        /** Дверь в школе в класс / из класса **/
        script = new Script() {
            @Override
            public void execute() {
                if(Game.player.y > 250) Game.player.y = 150;
                else Game.player.y = 320;
            }
        }; repo.put(3333_10,script);
        /** Шкипер согласился на кражу пацана **/
        script = new Script() {
            @Override
            public void execute() {
                //Выдаем квест
                Game.player.quests.add(Quest.get(32));
                //Добавляем заскриптованную дверь на уровень
                GameObject door = GameObject.get(60);
                door.x = 400; door.y = 190;
                Level.repo.get(BUILDING_15).objects.add(door);
                //Добавляем реплику для сдачи квеста
                Dialogue.butcher.get(6).responses
                        .add(new Dialogue.Response("Пацаненок...",0,3333_15));
                //Закрываем диалог
                Dialogue.current = null;
                Game.player.passwords.add("Подъезд с малюткой - 358");
            }
        }; repo.put(3333_11,script);
        /** Скрипт двери квартиры пацана **/
        script = new Script() {
            @Override
            public void execute() {
                //Проверяем в то ли время пришел Шкипер
                boolean goodTime = GameTime.getString().contains(": 6 h") || GameTime.getString().contains(": 7 h");
                //Если Шкипер пришел в нужное время
                if(goodTime){
                    //Предлагаем выломать дверь
                    Dialogue d = new Dialogue();
                    d.message = "Дверь заперта. Попробовать выломать?";
                    d.responses = List.of(new Dialogue.Response("Да",0,3333_13), new Dialogue.Response("Нет",0,0));
                    Dialogue.current = d;
                } else{ //В противном случае
                    //Шкиперу отвечают жильцы квартиры
                    Dialogue d = new Dialogue();
                    d.message = "Слышь, ты кто такой? \n Чего в квартиру долбишься? \n А ну съебался нахуй. \n Я сейчас мусоров вызову";
                    d.responses = List.of(new Dialogue.Response("ладно",0,0),new Dialogue.Response("Открывай нахуй",0,3333_14));
                    Dialogue.current = d;
                }
            }
        }; repo.put(3333_12,script);
        /** Скрипт выламывания двери **/
        script = new Script() {
            @Override
            public void execute() {
                //Поднимаем уровень шума в подъезде
                Game.currentLevel.noise += 200;
                //Проверяем, есть ли у Шкипера лом
                if(Game.player.items.stream().anyMatch(i->i.id==85)){
                    //Пускаем Шкипера в квартиру
                    Game.currentLevel.objects.remove(Game.player);
                    Game.currentLevel = Level.repo.get(PACAN_FLAT);
                    Game.currentLevel.objects.add(Game.player);
                    Game.player.x = 200; Game.player.y = 200;
                }else {
                    //Выводим сообщение, что не удалось выломать дверь
                    Dialogue d = new Dialogue();
                    d.message = "Не удалось выломать дверь \n Просто так её не выбьешь. \n Возможно, требуется какой-то инструмент";
                    d.responses = List.of(new Dialogue.Response("ладно",0,0));
                    Dialogue.current = d;
                }
            }
        };repo.put(3333_13,script);
        /** Шкипер приходит в квартиру пацана невовремя и грубит **/
        script = new Script() {
            @Override
            public void execute() {
                //Вызываем полицию
                Game.player.wanted += 200;
                //Выводим диалог
                Dialogue d = new Dialogue();
                d.message = "Ну все, мужик, ты сам напросился. \n Я звоню в мусарню!";
                d.responses = List.of(new Dialogue.Response("ладно",0,0));
                Dialogue.current = d;
            }
        }; repo.put(3333_14,script);
        /** Шкипер сдает квест "кража пацана" **/
        script = new Script() {
            @Override
            public void execute() {
                //Проверяем, принес ли Шкипер пацана
                if(Game.player.items.stream().anyMatch(i->i.id==86)){
                    //Если принес, завершаем квест
                    for(var q:Game.player.quests) if(q.id==32) q.completed = true;
                    //Даем награду
                    Game.player.money += 10_000;
                    //Выводим диалог
                    Dialogue.current = Dialogue.butcher.get(12);
                    //Забираем пацана
                    var pacan = Game.player.items.stream().filter(i->i.id==86).findFirst().orElse(null);
                    if(pacan!=null) Game.player.items.remove(pacan);
                    if(Game.player.equip == pacan) Game.player.equip = null;
                    //Удаляем реплику для сдачи квеста
                    Dialogue.Response r = Dialogue.butcher.get(6).responses
                            .stream().filter(re->re.text.contains("Пацан")).findFirst().orElse(null);
                    if(r!=null) Dialogue.butcher.get(6).responses.remove(r);
                }else{
                    //В противном случае напоминаем о задании
                    Dialogue.current = Dialogue.butcher.get(11);
                }
            }
        }; repo.put(3333_15,script);
        /** Шкипер берет задание "где деньги 1" **/
        script = new Script() {
            @Override
            public void execute() {
                //Выдаем задание
                Game.player.quests.add(Quest.get(12));
                //Добавляем реплику для сдачи задания
                Dialogue.butcher.get(6).responses
                        .add(new Dialogue.Response("Насчет мокрухи...",0,3333_17));
                //Закрываем текущий диалог
                Dialogue.current = null;

            }
        }; repo.put(3333_16,script);
        /** Шкипер сдает задание "где деньги 1" **/
        script = new Script() {
            @Override
            public void execute() {
                //Проверяем, убил ли Шкипер кассира
                if(Level.repo.get(GROCERY).objects.stream().noneMatch(o->o.id==3333_4)
                && Level.repo.get(STREET_1).objects.stream().noneMatch(o->o.id==3333_4)){
                    //Если убил, выполняем задание
                    for(var q:Game.player.quests)if(q.id==12)q.completed=true;
                    //Даем награду
                    Game.player.money += 15_000;
                    //Выводим диалог
                    Dialogue.current = Dialogue.butcher.get(18);
                    //Удаляем реплику для сдачи квеста
                    Dialogue.Response response =
                            Dialogue.butcher.get(6).responses.stream()
                                    .filter(r->r.text.contains("крух")).findFirst().orElse(null);
                    if(response != null) Dialogue.butcher.get(Game.butcher.dialogue).responses.remove(response);
                }else{
                    //В противном случае напоминаем о задании
                    Dialogue.current = Dialogue.butcher.get(19);
                }
            }
        }; repo.put(3333_17,script);
        /** Шкипер предлагает мерчандайзеру покурить **/
        script = new Script() {
            @Override
            public void execute() {
                //Проверяем, есть ли у Шкипера сигареты
                var smokes = Game.player.items.stream()
                        .filter(i->i.id==66).findFirst().orElse(null);
                //Если есть сигареты, и есть квест "где деньги ч1"
                if(smokes!=null && Game.player.quests.stream().anyMatch(q->q.id==12)){
                    //Забираем сигу
                    smokes.count -=1; if(smokes.count<=0){ Game.player.items.remove(smokes);if(Game.player.equip==smokes)Game.player.equip = null; }
                    //Перемещаем мерчандайзера и Шкипера на улицу
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
                    //Выводим диалог
                    Dialogue d = new Dialogue();
                    d.message = "Слыш, вот ты на вид дибил дибилом \n А сигареты куришь нормальные...";
                    d.responses = List.of(new Dialogue.Response(" ... ",0,0));
                    Dialogue.current = d;

                }else{
                    //Если нет, выводим диалог
                    Dialogue d = new Dialogue();
                    d.message = "Какой покурить? Ты ебанулся? Я работаю";
                    d.responses = List.of(new Dialogue.Response("ладно",0,0));
                    Dialogue.current = d;

                }

            }
        };repo.put(3333_18,script);
        /** Шкипер берет задание "где деньги ч2" **/
        script = new Script() {
            @Override
            public void execute() {
                //Выдаем квест
                Game.player.quests.add(Quest.get(33));
                //Добавляем реплику для сдачи квеста
                Dialogue.butcher.get(6).responses
                        .add(new Dialogue.Response("Водила..",0,3333_20));
                //Закрываем диалог
                Dialogue.current = null;
                var guy = GameObject.get(3333_5);
                guy.x = 9160; guy.y = 700;
                guy.speed = 1;
                Level.repo.get(STREET_1).objects.add(guy);
            }
        }; repo.put(3333_19,script);
        /** Шкипер сдает задание "где деньги ч2" **/
        script = new Script() {
            @Override
            public void execute() {
                //Проверяем что на улице нет живого водителя
                if(Level.repo.get(STREET_1).objects.stream().noneMatch(o->o.id==3333_5))
                {
                    //Если все ок, выполняем задание
                    for(var q: Game.player.quests) if(q.id==33) q.completed = true;
                    //Даем награду
                    Game.player.money += 10_000;
                    //Выводим диалог
                    Dialogue.current = Dialogue.butcher.get(27);
                    //Удаляем реплику для сдачи квеста
                    Dialogue.Response response =
                            Dialogue.butcher.get(6).responses
                            .stream()
                            .filter(r -> r.text.equals("Водила.."))
                            .findFirst()
                            .orElse(null);
                    if(response!= null) Dialogue.butcher.get(6).responses.remove(response);

                    return;
                }
                //Если нет, напоминаем о задании
                Dialogue.current = Dialogue.butcher.get(19);
            }
        };repo.put(3333_20,script);
        /** Шкипер берет квест "где деньги ч3" **/
        script = new Script() {
            @Override
            public void execute() {
                //TODO
                //Даем задание
                Game.player.quests.add(Quest.get(34));
                //Добавляем реплику для сдачи квеста
                Dialogue.butcher.get(Game.butcher.dialogue)
                        .responses.add(new Dialogue.Response("документы",0,3333_22));
                //Выводим диалог с объяснением
                Dialogue.current = Dialogue.butcher.get(24);
            }
        };repo.put(3333_21,script);
        /** Шкипер сдает квест "где деньги ч3" **/
        script = new Script() {
            @Override
            public void execute() {
                //TODO
                //Проверяем выполнение условий
                var docs = Game.player.items
                        .stream().filter(i->i.id==133)
                        .findFirst().orElse(null);
                if(docs == null) {
                    Dialogue.current = Dialogue.butcher.get(28);
                    //не принес
                    return;
                }
                //Если да, выполняем квест
                for(var q: Game.player.quests) if(q.id==34) q.completed = true;
                //Даем награду
                //Выводим диалог
                //Отключаем таймер мамы
                //Удаляем реплику для сдачи квеста
                Dialogue.Response response =
                        Dialogue.butcher.get(6).responses
                                .stream()
                                .filter(r -> r.text.contains("оку"))
                                .findFirst()
                                .orElse(null);
                if(response!= null)
                    Dialogue.butcher.get(Game.butcher.dialogue).responses.remove(response);

                response = Dialogue.butcher.get
                                (Game.butcher.dialogue)
                                .responses
                                .stream()
                                .filter(r -> r.text.contains("аме"))
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
         * СКРИПТ ПЕДОВКИ
         *
         *
         */
        /** Педовка выдает задания
         * <<Мне бы маме помочь>>**/
        script = new Script() {
            @Override
            public void execute() {
                //Если у Шкипера есть невыполненное задание, напоминаем ему об этом
                if(Game.player.quests.stream().anyMatch(q->q.owner.equals("педоффка")&&!q.completed))
                    Dialogue.current = Dialogue.girl.get(4);
                //Если Шкипер выполнил квест с травкой, и не взял квест с вейпом
                if(Game.player.quests.stream().noneMatch(q->q.id==7)
                && Game.player.quests.stream().anyMatch(q->q.id==8&&q.completed))
                    //Предлагаем взять
                    Dialogue.current = Dialogue.girl.get(8);
                //Если Шкипер выполнил квест с вейпом, и не взял квест с бывшим (ВРЕМЕННО ОТКЛЮЧЕН!!!)
               // if(Game.player.quests.stream().noneMatch(q->q.id==23)
               //         && Game.player.quests.stream().anyMatch(q->q.id==7&&q.completed) && false)
                    //Предлагаем взять
                    //Dialogue.current = Dialogue.girl.get(13);
                //Если Шкипер выполнил квест с бывшим(нет, вейпом!!!ВРЕМЕННО), и не взял квест с вебкой
                if(Game.player.quests.stream().noneMatch(q->q.id==24)
                        && Game.player.quests.stream().anyMatch(q->q.id==7&&q.completed))
                    //Предлагаем взять
                    Dialogue.current = Dialogue.girl.get(18);
                //Если Шкипер выполнил квест с вебкой, и не взял квест с хатой
                if(Game.player.quests.stream().noneMatch(q->q.id==25)
                        && Game.player.quests.stream().anyMatch(q->q.id==24&&q.completed))
                    //Предлагаем взять
                    Dialogue.current = Dialogue.girl.get(23);
                //Если выполнили квест с хатой, проверяем на хорошую неканоничную концовку
                if(Game.player.quests.stream().anyMatch(q->q.id==25&&q.completed))
                    repo.get(4444_15).execute();
            }
        };repo.put(4444,script);
        /** Шкипер сказал педовке о папе **/
        script = new Script() {
            @Override
            public void execute() {
                //Добавляем реплику мяснику для сдачи квеста
                Dialogue.butcher.get(Game.butcher.dialogue)
                        .responses.add(new Dialogue.Response("нашёл",0,4444_3_01));
                //Выводим следующий диалог
                Dialogue.current = Dialogue.girl.get(2);
            }
        };repo.put(4444_3,script);
        //Мясник принимает квест
        script = new Script() {
            @Override
            public void execute() {
                //Выполняем задание найти дочку
                for(var q:Game.player.quests) if(q.id==6)q.completed=true;
                Dialogue.current = Dialogue.butcher.get(23);
                var d = Dialogue.butcher.get(Game.butcher.dialogue);
                var r = d.responses.stream().filter(rsp->rsp.text.contains("наш"))
                        .findFirst().orElse(null);
                if(r!=null) d.responses.remove(r);
                Game.player.addItem(Item.get(52));
                Game.player.addItem(Item.get(52));
            }
        }; repo.put(4444_3_01,script);
        /** Шкипер соглашается пойти на хату **/
        script = new Script() {
            @Override
            public void execute() {
                //Убираем ребят с улицы и перемещаем в квартиру
                Level street = Level.repo.get(STREET_1);//Улицв
                Level flat = Level.repo.get(TRAP_HOUSE);
                street.objects.remove(Game.player); //Шкипер
                flat.objects.add(Game.player);
                Game.player.x = 400; Game.player.y = 600;
                street.objects.remove(Game.girl);//Педовка
                flat.objects.add(Game.girl);
                Game.girl.x = 450; Game.girl.y = 600;
                GameObject guy = street.objects.stream().filter(o->o.id==4444_1).findFirst().orElse(null);
                if(guy!=null){ Game.currentLevel.objects.remove(guy);//Костя
                    flat.objects.add(guy); guy.x = 500; guy.y = 600; }
                guy = street.objects.stream().filter(o->o.id==4444_2).findFirst().orElse(null);
                if(guy!=null) {
                    Game.currentLevel.objects.remove(guy);//Володя
                    flat.objects.add(guy); guy.x = 500; guy.y = 600;
                }
                Game.currentLevel = flat;//Переключаем уровень на квартиру
                Dialogue.current = Dialogue.girl.get(3);//Выводим диалог
                //Меняем педовке основной диалог
                Game.girl.dialogue = 5;
            }
        };repo.put(4444_5,script);
        /** Шкипер соглашается найти травку **/
        script = new Script() {
            @Override
            public void execute() {
                //Добавляем квест
                Game.player.quests.add(Quest.get(8));
                //Закрываем текущий диалог
                Dialogue.current = null;
                //Добавляем реплику для сдачи квеста
                Dialogue.girl.get(Game.girl.dialogue).responses
                        .add(new Dialogue.Response("травка...",0,4444_2));
                Game.player.passwords.add("Притон (педовка) - 420");

            }
        };repo.put(4444_6,script);
        /** Скрипт Кости **/
        //TODO: возможно вообще лишнее и стоит удалить
        script = new Script() {
            @Override
            public void execute() {
                //Если Шкипер еще не получил задание достать покурить
                if(Game.player.quests.stream().noneMatch(q->q.id==8))
                {
                    //Говорим "отмазку"
                    Dialogue d = new Dialogue();
                    d.message = "Слыш, мужик, мы тут нормально бухаем. \n Че те надо?... Иди, а...";
                    d.responses = List.of(new Dialogue.Response("ладно",0,0));
                    Dialogue.current = d;
                }
            }
        };repo.put(4444_1,script);
        /** Шкипер сдает квест с травкой **/
        script = new Script() {
            @Override
            public void execute() {
                //Проверяем, принес ли Шкипер искомые вещи
                var weed = Game.player.items.stream().filter(i->i.id==7).findFirst().orElse(null);
                var paper = Game.player.items.stream().filter(i->i.id==90).findFirst().orElse(null);
                //Если не принес, напоминаем ему об этом
                if(weed==null || paper==null){Dialogue.current = Dialogue.girl.get(6); return;}
                //Если принес, забираем травку и бумагу
                weed.count -=1; if(weed.count <1) Game.player.items.remove(weed); if(Game.player.equip==weed) Game.player.equip = null;
                paper.count -=1; if(paper.count <1) Game.player.items.remove(paper); if(Game.player.equip==paper) Game.player.equip = null;
                //Даем косяк
                Game.player.addItem(Item.get(89));
                //Добавляем накуренность
                Game.player.smoke += 45;
                //Выполняем квест
                for(var q : Game.player.quests)if(q.id == 8)q.completed=true;
                //Удаляем реплику для сдачи квеста
                Dialogue.Response response = Dialogue.girl.get(Game.girl.dialogue).responses.stream()
                        .filter(r->r.text.equals("травка...")).findFirst().orElse(null);
                if(response!=null)Dialogue.girl.get(Game.girl.dialogue).responses.remove(response);
                //Слова благодарности
                Dialogue.current = Dialogue.girl.get(7);
            }
        }; repo.put(4444_2,script);
        /** Шкипер соглашается найти вапе **/
        script = new Script() {
            @Override
            public void execute() {
                //Добавляем квест
                Game.player.quests.add(Quest.get(7));
                //Закрываем текущий диалог
                Dialogue.current = null;
                //Добавляем реплику для сдачи квеста
                Dialogue.girl.get(Game.girl.dialogue).responses
                        .add(new Dialogue.Response("вейп...",0,4444_8));
            }
        };repo.put(4444_7,script);
        /** Шкипер сдает квест с вейпом **/
        script = new Script() {
            @Override
            public void execute() {
                //Проверяем, принес ли Шкипер искомые вещи
                var vape = Game.player.items.stream().filter(i->i.id==99).findFirst().orElse(null);
                //Если не принес, напоминаем ему об этом
                if(vape==null) {Dialogue.current = Dialogue.girl.get(29); return;}
                //Если принес, забираем травку и бумагу
                vape.count -=1; if(vape.count <1) Game.player.items.remove(vape); if(Game.player.equip==vape) Game.player.equip = null;
                //Выполняем квест
                for(var q : Game.player.quests)if(q.id == 7)q.completed=true;
                //Удаляем реплику для сдачи квеста
                Dialogue.Response response = Dialogue.girl.get(Game.girl.dialogue).responses.stream()
                        .filter(r->r.text.equals("вейп...")).findFirst().orElse(null);
                if(response!=null)Dialogue.girl.get(Game.girl.dialogue).responses.remove(response);
                //Слова благодарности
                Dialogue.current = Dialogue.girl.get(12);
            }
        }; repo.put(4444_8,script);
        /** Шкипер соглашается разобраться с бывшим **/
        script = new Script() {
            @Override
            public void execute() {
                //Добавляем квест
                Game.player.quests.add(Quest.get(23));
                //Закрываем текущий диалог
                Dialogue.current = null;
                //Добавляем реплику для сдачи квеста
                Dialogue.girl.get(Game.girl.dialogue).responses
                        .add(new Dialogue.Response("бывший...",0,4444_10));
            }
        };repo.put(4444_9,script);
        /** Шкипер сдает квест с бывшим **/
        script = new Script() {
            @Override
            public void execute() {
                //Проверяем, убил ли Шкипер бывшего
                //Если не, напоминаем ему об этом
                if(false) {Dialogue.current = Dialogue.girl.get(16); return;}
                //Выполняем квест
                for(var q : Game.player.quests)if(q.id == 23)q.completed=true;
                //Удаляем реплику для сдачи квеста
                Dialogue.Response response = Dialogue.girl.get(Game.girl.dialogue).responses.stream()
                        .filter(r->r.text.equals("бывший...")).findFirst().orElse(null);
                if(response!=null)Dialogue.girl.get(Game.girl.dialogue).responses.remove(response);
                //Слова благодарности
                Dialogue.current = Dialogue.girl.get(17);
            }
        }; repo.put(4444_10,script);
        /** Шкипер соглашается купить вебку **/
        script = new Script() {
            @Override
            public void execute() {
                //Добавляем квест
                Game.player.quests.add(Quest.get(24));
                //Закрываем текущий диалог
                Dialogue.current = null;
                //Добавляем реплику для сдачи квеста
                Dialogue.girl.get(Game.girl.dialogue).responses
                        .add(new Dialogue.Response("камера...",0,4444_12));
            }
        };repo.put(4444_11,script);
        /** Шкипер сдает квест с вебкой **/
        script = new Script() {
            @Override
            public void execute() {
                //Проверяем принес ли Шкипер камеру
                var webcam = Game.player.items.stream().filter(i->i.id==100).findFirst().orElse(null);
                //Если не, напоминаем ему об этом
                if(webcam==null) {
                    Dialogue.current = Dialogue.girl.get(21);
                    return;
                }
                //Выполняем квест
                for(var q : Game.player.quests)if(q.id == 24)q.completed=true;
                //Удаляем реплику для сдачи квеста
                Dialogue.Response response = Dialogue.girl.get(Game.girl.dialogue).responses.stream()
                        .filter(r->r.text.equals("камера...")).findFirst().orElse(null);
                if(response!=null)Dialogue.girl.get(Game.girl.dialogue).responses.remove(response);
                //Слова благодарности
                Dialogue.current = Dialogue.girl.get(22);
                //забираем камеру
                Game.player.items.remove(webcam);
                if(Game.player.equip==webcam) Game.player.equip=null;

            }
        }; repo.put(4444_12,script);
        /** Шкипер соглашается снять квартиру **/
        script = new Script() {
            @Override
            public void execute() {
                //Добавляем квест
                Game.player.quests.add(Quest.get(25));
                //Закрываем текущий диалог
                Dialogue.current = null;
                //Добавляем реплику для сдачи квеста
                Dialogue.girl.get(Game.girl.dialogue).responses
                        .add(new Dialogue.Response("квартира...",0,4444_14));
            }
        };repo.put(4444_13,script);
        /** Шкипер сдает квест с квартирой **/
        script = new Script() {
            @Override
            public void execute() {
                //Проверяем снял ли Шкипер квартиру
                //Если не, напоминаем ему об этом
                if(!Game.player.rent) {Dialogue.current = Dialogue.girl.get(26); return;}
                //Выполняем квест
                for(var q : Game.player.quests)if(q.id == 25)q.completed=true;
                //Удаляем реплику для сдачи квеста
                Dialogue.Response response = Dialogue.girl.get(Game.girl.dialogue).responses.stream()
                        .filter(r->r.text.equals("квартира...")).findFirst().orElse(null);
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
        /** Проверка на хорошую концовку **/
        script = new Script() {
            @Override
            public void execute() {
                //todo: проверка на сложный режим игры
                boolean hardcore = true;
                //пройдена ли линейка Мясника
                boolean butcher = Game.player.quests.stream().anyMatch(q->q.id==34&& q.completed);
                //пройдена ли линейка Аптекарши
                boolean pharmacist = Game.player.quests.stream().anyMatch(q -> q.id == 53 && q.completed);
                //Выводим диалог
                Dialogue d = new Dialogue();
                d.message = Dialogue.girl.get(28).message;
                if(hardcore && butcher && pharmacist) {
                    d.responses.add(new Dialogue.Response("ладно",0,4444_16));
                }else{
                    String s = "Не могу. \n Кто-то должен " + (!butcher?"кормить ":"") + ((!butcher&&!pharmacist)?",":"") + (!pharmacist?" давать лекарства и мыть ":"") + "маму.";
                    d.responses.add(new Dialogue.Response(s,0,0));
                }
                Dialogue.current = d;
            }
        };repo.put(4444_15,script);
        /** Шкипер выбрал остаться жить у педовки **/
        script = new Script() {
            @Override
            public void execute() {
                Game.player.good = true;
            }
        }; repo.put(4444_16,script);

        /** Шкипер снял квартиру **/
        script = new Script() {
            @Override
            public void execute() {
                if (Game.player.rent) return;
                if(Game.player.money< 50_000 ){
                    var d = new Dialogue();
                    d.message = "Недостаточно средстВ!";
                    d.responses.add(new Dialogue.Response("ладно",0,0));
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
                d.message = "Квартира снята!";
                d.responses.add(new Dialogue.Response("ладно",0,0));
                Dialogue.current = d;

            }
        }; repo.put(4444_22,script);
        //==========================================================================================//
        /**
         *
         * СКРИПТЫ ЛОВУШКИ ИЗ ВАЛБЕРС
         *
         */
        /** Ловушка из валберс дает задания
        // <<Мне бы маме помочь>> **/
        script = new Script() {
            @Override
            public void execute() {
                //Проверяем, есть ли у Шкипера задания от Ловушки из валберс (Кроме адской дрочки)
                if(Game.player.quests.stream().noneMatch(q->q.id!=31 && q.owner.equals("трап")))
                {
                    //Если заданий (кроме адской дрочки) нет, выдаем задание 30 (купить елдак)
                    Game.player.quests.add(Quest.get(30));
                    //Выводим диалог
                    Dialogue.current = Dialogue.trap.get(7);
                    //Добавляем реплику для сдачи
                    Dialogue.trap.get(Game.trap.dialogue).responses.add(new Dialogue.Response("заказал тебе...",0,5555_6));
                    return;
                }
                //Если заказали и отдали трапу елдак
                if(Game.player.quests.stream().anyMatch(q->q.id==30&&q.completed) && Game.player.quests.stream().noneMatch(q->q.id==61)){
                    //Предлагаем испробовать
                    Game.player.quests.add(Quest.get(61));
                    var r = new Dialogue.Response("пойдем к тебе..",0,5555_7);
                    Dialogue.trap.get(Game.trap.dialogue).responses.add(r);
                    var d = new Dialogue();
                    d.message = "Ой, слушай. Мальчик мой.. любимый..\n Может зайдешь ко мне сегодня после смены? \n Я живу неподалеку \n Мне нужна твоя помощь. \n Понимаешь, я так давно  не кончала\n А сама не могу. \n Меня заводит только когда меня ебет самотыком в жопу \n крепкий мужчина вроде тебя.. \n Ну что, пойдем?.. Тебе даже трогать меня не придется. Выебешь елдаком. \n А я хуй в кулачок свой подрочу и все. \n Пойдем?";
                    d.responses.add(new Dialogue.Response("Не пойду",0,0));
                    d.responses.add(r);
                    Dialogue.current = d;
                    return;
                }
                //Если мы уже выебали трапа в жопу, выдаем квест вынести стиралку на помойку
                if(Game.player.quests.stream().anyMatch(q->q.id==61&&q.completed) && Game.player.quests.stream().noneMatch(q->q.id==62)){
                    Game.player.quests.add(Quest.get(62));
                    var d = new Dialogue();
                    d.message = "Слушай, мне тоже твоя помощь нужна. \n В одном небольшом деле. \n У меня дома в дальней комнате стоит старая стиральная мащина\n Забери ее. Помоги на помойку вынести";
                    d.responses.add(new Dialogue.Response("ладно",0,5555_8));
                    Dialogue.current = d;
                    return;
                }
                //Если есть невыполненное (кроме адской дрочки) задание
                if(Game.player.quests.stream().anyMatch(q->q.owner.equals("трап") && !q.completed && q.id!=31))
                    //Выводим диалог
                    Dialogue.current = Dialogue.trap.get(8);
                //Если задание с покупкой елдака выполнено
            }
            //Проверяем, есть ли у Шкипера невыполненые (кроме адской дрочки)

        };repo.put(5555,script);

        /** Ловушка из валберс проверяет какие поступили посылки **/
        script = new Script() {
            @Override
            public void execute() {
                //Находим склад
                GameObject container = Level.repo.get(WILDBERRIES).objects.stream().filter(o->o.id==1).findFirst().orElse(null);
                //Если не нашли склад или там пусто, сообщаем Шкиперу
                if(container == null || container.items.isEmpty()) Dialogue.current = Dialogue.trap.get(3);
                else{
                    //Если посылка есть, но у шкипера нет места, сообщаем ему об этом
                    if(Game.player.items.size()+container.items.size()>ITEMS_CAPACITY) {Dialogue.current = Dialogue.trap.get(4); return;}
                    //Берем посылку
                    for(var i: container.items)Game.player.addItem(i);
                    //чистим склад
                    container.items = new ArrayList<>();
                    //Сообщаем игроку о выдаче посылки
                    Dialogue d = new Dialogue();
                    d.message = "Вот твоя посылка. Приятного пользования!";
                    d.responses.add(new Dialogue.Response("ладно",0,0));
                    Dialogue.current = d;
                }
            }
        };repo.put(5555_1,script);

        /** Ловушка из валберс выдает заказ Компьютерщика **/
        script = new Script() {
            @Override
            public void execute() {
                //Проверяем что игрок не перегружен
                if(Game.player.items.size()>=ITEMS_CAPACITY) {Dialogue.current = Dialogue.trap.get(4); return;}
                //Выдаем посылку
                Game.player.addItem(Item.get(73));
                //Выводим сообщение
                Dialogue d = new Dialogue(); d.message = "Пожалуйста. Посылка по номеру 8814";
                d.responses = List.of(new Dialogue.Response("Ага",0,0));
                Dialogue.current = d;
                //Удаляем реплику о 8814 заказе из диалога
                Dialogue.Response r = null; //Ищем саму реплику
                for(var rr : Dialogue.trap.get(1).responses)  if(rr.text.contains("8814")) r =rr;
                //И удаляем
                if(r!=null) Dialogue.trap.get(1).responses.remove(r);
            }
        }; repo.put(5555_2,script);

        /** Ловушка из валберс отвечает на вопрос про цветы **/
        script = new Script() {
            @Override
            public void execute() {
                //Ищем цветы
                Item flowers = Game.player.items.stream().filter(i->i.id==75).findFirst().orElse(null);
                //Нет цветов - пошел на
                if(flowers == null){ Dialogue.current = Dialogue.trap.get(5); return;}
                //Есть цветы - дари
                Game.player.items.remove(flowers); if(Game.player.equip==flowers) Game.player.equip = null;
                //Удаляем реплику о дарении цветов
                Dialogue.Response r = null; //Ищем саму реплику
                for(var rr : Dialogue.trap.get(1).responses)  if(rr.text.contains("вет")) r =rr;
                //И удаляем
                if(r!=null) Dialogue.trap.get(1).responses.remove(r);
                //Выполняем квест
                for (var q:Game.player.quests) if(q.id==28) q.completed = true;
                //И говорим "спасибо"
                Dialogue.current = Dialogue.trap.get(6);
                //Даем Шкиперу квест (Адская дрочка)
                Game.player.quests.add(Quest.get(31));
                //И даем тюбик адской смазки
                Game.player.addItem(Item.get(136));
                //Добавляем реплику на сдачу квеста 31 (адская дрочка)
                Dialogue.trap.get(1).responses.add(new Dialogue.Response("Отнес",0,5555_4));
            }
        };repo.put(5555_3,script);


        /** Ловушка из валберс проверяет что Шкипер отнес тюбик **/
        script = new Script() {
            @Override
            public void execute() {
                var tumba = Level.repo.get(HACKERS_PLACE).objects.stream().filter(o->o.id==1).findFirst().orElse(null);
                if(tumba.items.stream().anyMatch(i->i.id==136)){
                    var d = new Dialogue();
                    d.message = "Ой, ну наконец-то. \n МОжет хоть теперь этот дрочила прекратит свой.. \n дроч марафон.";
                    d.responses.add(new Dialogue.Response("ладно",0,0));
                    Dialogue.current = d;
                    for(var q: Game.player.quests) if(q.id==31) q.completed = true;
                }else{
                    var d = new Dialogue();
                    d.message = "Точно отнес? \n А че глаза забегали?";
                    d.responses.add(new Dialogue.Response("ой.. бля...",0,0));
                    Dialogue.current = d;
                }
            }
        };repo.put(5555_4,script);

        /** Петух проверяет, лежит ли на складе елдак и забирает его по квесту **/
        script = new Script() {
            @Override
            public void execute() {
                var warehouse = Level.repo.get(WILDBERRIES).objects.stream().filter(o->o.id==1).findFirst().orElse(null);
                var eldak = warehouse.items.stream().filter(i->i.id==113).findFirst().orElse(null);
                if(eldak!=null){
                    warehouse.items.remove(eldak);
                    var d = new Dialogue();
                    d.message = "Да, спасибо, я видела.. \n Ну все, иди, не смущай";
                    d.responses.add(new Dialogue.Response("ладно",0,0));
                    Dialogue.current = d;
                    for(var q: Game.player.quests) if(q.id==30)q.completed=true;
                    var r = Dialogue.trap.get(Game.trap.dialogue).responses.stream().filter(o->o.text.contains("заказал")).findFirst().orElse(null);
                    Dialogue.trap.get(Game.trap.dialogue).responses.remove(r);
                    return;
                }else {
                    var d = new Dialogue();
                    d.message = "Если бы ты заказал, я бы знала \n все, иди..";
                    d.responses.add(new Dialogue.Response("ладно",0,0));
                    Dialogue.current = d;
                }
            }
        };repo.put(5555_6,script);

        /** Шкипер соглашается идти проверять игрушку **/
        script = new Script() {
            @Override
            public void execute() {
                Game.switchLevel(TRAP_DUDE_PLACE);
                Level.repo.get(TRAP_DUDE_PLACE).objects.add(Game.trap);
                Game.player.x = 200; Game.player.y = 200;
                Game.trap.x = 250; Game.trap.y = 220;
                var d = new Dialogue();
                d.message = "Ох, ебать, мужик. \n Вот это ты нормально отработал. \n Я так много последний раз спускал \n Ну хуй знает \n В дестве еще \n Когда папаша нажирался \n и отправлялся пиздить мать \n и я любил вот подрочитьь под эти крики. \n Вот с тех пор так не кончал. \n Спасибо, брат";
                d.responses.add(new Dialogue.Response("ладно..",0,0));
                Dialogue.current = d;
                for(var q: Game.player.quests)if(q.id==61)q.completed = true;
                var r = Dialogue.trap.get(Game.trap.dialogue).responses.stream().filter(o->o.text.contains("к теб")).findFirst().orElse(null);
                Dialogue.trap.get(Game.trap.dialogue).responses.remove(r);

            }
        }; repo.put(5555_7,script);
        /** Шкипер согласился взять квест со стриралкой **/
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
         * СКРИПТЫ МЕНТА
         *
         */
        /** Скрипт выдачи заданий
         // <<Мне бы маме помочь>> **/
        script = new Script() {
            @Override
            public void execute() {
                if(Game.player.quests.stream().noneMatch(q->q.id==19)){
                    //Если есть квест с заграном
                    Dialogue.current = Dialogue.officer.get(2);
                    return;
                }
                if(Game.player.quests.stream().anyMatch(q->q.id==19)
                        && Game.player.quests.stream().noneMatch(q->q.owner.equals("мент"))){
                    //Если есть квест с заграном, и нет ни одного квеста мента.
                    //Предлагаем взять квест с регистрацией
                    Dialogue.current = Dialogue.officer.get(3);
                    return;
                }
                if(Game.player.quests.stream().anyMatch(q->!q.completed && q.owner.equals("мент"))) {
                    //Если есть невыполненый квест, напоминаем о нем
                    Dialogue.current = Dialogue.officer.get(11);
                    return;
                }
                if(Game.player.quests.stream().noneMatch(q->q.id==59)
                && Game.player.quests.stream().anyMatch(q->q.id==37&&q.completed)){
                    //Если квест с регистрацией выполнен, а квест с зеками не взят.
                    //Даем квест с зеками
                    repo.get(6666_3).execute();
                    return;
                }
                if(Game.player.quests.stream().noneMatch(q->q.id==41)
                && Game.player.quests.stream().anyMatch(q->q.id==59&&q.completed)){
                    //Если квест с гопниками не взят, а квест с зеками выполнен
                    //Предлагаем квест с гопниками
                    Game.player.quests.add(Quest.get(41));
                    Dialogue.current = Dialogue.officer.get(17);
                    Dialogue.officer.get(Game.officer.dialogue)
                            .responses.add(new Dialogue.Response("Бандиты..",0,6666_7));
                    gopKilled = 0;
                    return;
                }
                if(Game.player.quests.stream().noneMatch(q->q.id==38)
                && Game.player.quests.stream().anyMatch(q->q.id==41&&q.completed)){
                    //Если квест с гопниками выполнен, а квест с подбросом не взят
                    //Даем квест с подбросом
                    Game.player.quests.add(Quest.get(38));
                    Dialogue.current = Dialogue.officer.get(22);
                    Dialogue.officer.get(Game.officer.dialogue)
                            .responses.add(new Dialogue.Response("подбросил",0,6666_8));
                    Game.player.passwords.add("Подъезд авторитета - 111");
                    Game.player.passwords.add("Подъезд с бандитами - 883");
                    return;
                }
                if(Game.player.quests.stream().anyMatch(q->q.id==38 || q.completed)){
                    //Если Шкипер выполнил (последний) квест с подбросом.
                    //Рассказываем про маму.
                    Dialogue.current = Dialogue.officer.get(31);

                }
            }
        }; repo.put(6666,script);


        /** Мент дает задание изъять регистрации **/
        script = new Script() {
            @Override
            public void execute() {
                Game.player.quests.add(Quest.get(37));
                Dialogue.officer.get(Game.officer.dialogue)
                        .responses.add(new Dialogue.Response("изъял",0,6666_2));
                Dialogue.current = null;
            }
        }; repo.put(6666_1, script);

        /** Мент проверяет, собраны ли регистрации **/
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
                            .filter(dd->dd.text.contains("ял"))
                            .findFirst().orElse(null);
                    Dialogue.officer.get(Game.officer.dialogue).responses.remove(r);

                }else {
                    Dialogue.current = Dialogue.officer.get(10);
                }
            }
        }; repo.put(6666_2,script);

        /** Мент дает задание разобраться с зеками **/
        script = new Script() {
            @Override
            public void execute() {
                Dialogue.current = Dialogue.officer.get(12);
                Dialogue.officer.get(Game.officer.dialogue)
                        .responses.add(new Dialogue.Response("Зеки...",0,6666_5));
                Game.player.quests.add(Quest.get(59));
            }
        }; repo.put(6666_3,script);

        /** Шкипер едет с ментом в колонию (усмирять зеков) **/
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

        /** Мент проверяет, что Шкипер угомонил бунт **/
        script = new Script() {
            @Override
            public void execute() {
                if(Level.repo.get(JAIL).objects.stream().noneMatch(o->o.id==124)
                && zekKilled >= 20){
                    Dialogue.current = Dialogue.officer.get(16);
                    for(var q: Game.player.quests) if(q.id == 59) q.completed = true;
                    var d = Dialogue.officer.get(Game.officer.dialogue);
                    var r = d.responses.stream()
                            .filter(dd->dd.text.contains("ек"))
                            .findFirst().orElse(null);
                    Dialogue.officer.get(Game.officer.dialogue).responses.remove(r);
                }else{
                    Dialogue.current = Dialogue.officer.get(15);
                }
            }
        }; repo.put(6666_5, script);

        /** Мент проверяет, что Шкипер убил гопников **/
        script = new Script() {
            @Override
            public void execute() {
                    if(gopKilled >= 30){
                        for(var q: Game.player.quests) if(q.id==41) q.completed=true;
                        Dialogue.current = Dialogue.officer.get(21);
                        var d = Dialogue.officer.get(Game.officer.dialogue);
                        var r = d.responses.stream()
                                .filter(dd->dd.text.contains("ит"))
                                .findFirst().orElse(null);
                        Dialogue.officer.get(Game.officer.dialogue).responses.remove(r);
                    }else{
                        Dialogue.current = Dialogue.officer.get(20);
                    }
            }
        }; repo.put(6666_7,script);

        /** Проверяем, подбросил ли Шкипер наркоту **/
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
                    d.responses.add(new Dialogue.Response("Да",0,6666_9));
                    Dialogue.current = d;
                }else{
                    Dialogue.current = Dialogue.officer.get(29);
                }
            }
        }; repo.put(6666_8,script);

        /** Завершаем квест с подбросом наркоты **/
        script = new Script() {
            @Override
            public void execute() {
                for (var q: Game.player.quests)
                    if(q.id == 38) q.completed = true;
                Dialogue.current = null;
                Dialogue.current = Dialogue.officer.get(9);
                var d = Dialogue.officer.get(Game.officer.dialogue);
                var r = d.responses.stream()
                        .filter(dd->dd.text.contains("брос"))
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
         * СКРИПТЫ МЕДБРАТА
         *
         */
        /** Скрипт выдачи заданий
        // <<Мне бы маме помочь>> **/
        script = new Script() {
            @Override
            public void execute() {
                //Если у Шкипера нет заданий Медбрата
                if(Game.player.quests.stream().noneMatch(q->q.owner.equals("медбрат")))
                {
                    //Даем квест 43 (выпить с тестем)
                    Game.player.quests.add(Quest.get(43));
                    //Даем квест 18 (найти врача в интернете)
                    Game.player.quests.add(Quest.get(18));
                    //Выводим диалог
                    Dialogue.current = Dialogue.nurse.get(2);
                    //Перемещаем медбрата на место встречи
                    Game.currentLevel.objects.remove(Game.nurse);
                    Game.nurse.x = 6150; Game.nurse.y = 3500;
                    Level.repo.get(STREET_1).objects.add(Game.nurse);
                    //И тестя с ним же
                    Level.repo.get(STREET_1).objects.add(Game.mechanic);
                    Game.mechanic.x = 6220; Game.mechanic.y = 3500;
                    return;
                }
                //Если Шкипер взял, но не завершил задание выпить с тестем
                if(Game.player.quests.stream().anyMatch(q->q.id==43 && !q.completed))
                {
                    Dialogue.current = Dialogue.nurse.get(3);
                    return;
                }
                //Если Шкипер завершил задание с тестем и не брал задание с уколом бабушки (коляской)
                if(Game.player.quests.stream().anyMatch(q->q.id==43 && q.completed)
                && Game.player.quests.stream().noneMatch(q->q.id==44))
                {
                    //Выдаем квест
                    Game.player.quests.add(Quest.get(44));
                    //Выводим диалог
                    Dialogue.current = Dialogue.nurse.get(4);
                    //Даем Шкиперу ключ-карту
                    Game.player.addItem(Item.get(79));
                    //И халат
                    Game.player.addItem(Item.get(82));
                    return;
                }
                //Если Шкипер взял квест уколоть бабулю, но еще не уколол
                if(Game.player.quests.stream().anyMatch(q->q.id==44 && !q.completed))
                {
                    //Выводим диалог
                    Dialogue.current = Dialogue.nurse.get(5);
                    return;
                }
                //Если шкипер выполнил задание с уколом бабушки (коляской) и не получал задания с кольцом
                if(Game.player.quests.stream().anyMatch(q->q.id==44 && q.completed)
                && Game.player.quests.stream().noneMatch(q->q.id==45))
                {
                    //Выдаем квест
                    Game.player.quests.add(Quest.get(45));
                    //Выводим сообщение
                    Dialogue.current = Dialogue.nurse.get(6);
                    //Добавляем реплику для сдачи квеста
                    Dialogue.nurse.get(1).responses.add(new Dialogue.Response("Кольцо",0,7777_11));
                    //Даем ключ
                    Game.player.addItem(Item.get(132));
                    // Код
                    Game.player.passwords.add("Подъезд медбрата - 112");
                    return;
                }
                //Если Шкипер взял квест с кольцом
                if(Game.player.quests.stream().anyMatch(q->q.id==45))
                    Dialogue.current = Dialogue.nurse.get(7); //Выводим реплику
            }
        }; repo.put(7777,script);
        /** Скрипт шприца из больницы **/
        script = new Script() {
            @Override
            public void execute() {
                //Чертим прямоугольник вокруг игрока (зона действия шприца)
                Rectangle r = new Rectangle(Game.player.x-100,Game.player.y-100,200,200);
                //Проверяем, попадает ли в зону действия шприца койка с бабулей
                for(var o: Game.currentLevel.objects) if(o.id==44 && r.intersects(o.hitBox)) {
                    //Если Шкипер попал уколом в бабулю, выполняем квест с уколом бабули
                    for(var q: Game.player.quests) if(q.id==44) q.completed = true;
                    //Забираем шприц
                    Item syringe = Game.player.items.stream().filter(i->i.id==81).findFirst().orElse(null);
                    if(syringe != null) Game.player.items.remove(syringe);
                    if(Game.player.equip==syringe) Game.player.equip = null;
                }
            }
        }; repo.put(7777_1,script);
        /** Скрипт колескеъ - объекта **/
        script = new Script() {
            @Override
            public void execute() {
                //Если карманы забиты, взять коляску не получится.
                if(Game.player.items.size()>=ITEMS_CAPACITY) return;
                //Находим колескеъ
                GameObject ride = Game.currentLevel.objects.stream().filter(i->i.id==47).findFirst().orElse(null);
                //И удаляем с уровня
                if(ride != null) Game.currentLevel.objects.remove(ride);
                //Добавляем колескеъ Шкиперу в инвентарь как предмет
                Game.player.addItem(Item.get(83));
            }
        }; repo.put(7777_2,script);
        /** Скрипт зоны входа в больницу **/
        script = new Script() {
            @Override
            public void execute() {
                //Если сейчас не день/вечер, больница закрыта.
                if(GameTime.getTimeOfTheDay()!=DAY && GameTime.getTimeOfTheDay()!=SUNSET)
                    //Без ключ-карты шкипер не может войти
                    if(Game.player.items.stream().noneMatch(i->i.id!=79)) return;
                //Иначе впускаем шкипера внутрь
                Game.currentLevel.objects.remove(Game.player); Game.currentLevel = Level.repo.get(HOSPITAL);
                Game.player.x = 100; Game.player.y = 850; //И помещаем у входа
                Game.currentLevel.objects.add(Game.player);
                //Если шкипер пришел ночью без халата, бьем тревогу
                if((GameTime.getTimeOfTheDay()!=DAY && GameTime.getTimeOfTheDay()!=SUNSET)
                && (Game.player.torso == null || Game.player.torso.id!=82)){
                    //Спавним 5 копов
                    for (int i = 0; i < 5; i++) {
                        //Создаем копа
                        GameObject cop = GameObject.get(7);
                        //Помещаем его в больницу
                        cop.x = 100 + (i*60); cop.y = 850;
                        Level.repo.get(HOSPITAL).objects.add(cop);
                    }
                    //И добавляем 200 очков беспредела
                    Game.player.wanted += 200;
                }
            }
        }; repo.put(7777_3,script);
        /** Скрипт зоны выхода из больницы **/
        script = new Script() {
            @Override
            public void execute() {
                //Ставим Шкипера на улицу у двери больницы
                Game.currentLevel.objects.remove(Game.player);//Убрали с текущего уровня
                Game.player.y = 8_000; Game.player.x = 7_600;//Поставили координаты входа с больницу
                Game.currentLevel = Level.repo.get(STREET_1);//Включили в движок уровень - улицу
                Game.currentLevel.objects.add(Game.player);//Поставили игрока на улицу
            }
        }; repo.put(7777_4,script);
        /** Скрипт зоны выхода на лестницу с 1-го этажа больницы **/
        script = new Script() {
            @Override
            public void execute() {
                //Без ключ-карты туда не попасть
                if(Game.player.items.stream().noneMatch(i->i.id==79)) return;
                //Ключ-карта работает только ночью
                if(GameTime.getTimeOfTheDay()==DAY||GameTime.getTimeOfTheDay()==SUNSET) return;
                //Если Шкипер на 1-м этаже, ставим его на лестницу
                if(Game.player.y > 720) Game.player.y = 650;
                //Если Шкипер на лестнице, ставим его на 1-й этаж
                else Game.player.y = 800;
            }
        }; repo.put(7777_5,script);
        /** Скрипт зоны двери с лестницы на 2-й этаж больницы **/
        script = new Script() {
            @Override
            public void execute() {
                //Без ключ-карты туда не попасть
                if(Game.player.items.stream().noneMatch(i->i.id==79)) return;
                //Если Шкипер на 2-м этаже, ставим его на лестницу
                if(Game.player.x < 780) Game.player.x = 830;
                //Если Шкипер на лестнице, ставим его на 2-й этаж
                else Game.player.x = 700;
            }
        }; repo.put(7777_6,script);
        /** Скрипт зоны двери палаты в больнице **/
        script = new Script() {
            @Override
            public void execute() {
                //Без ключ-карты туда не попасть
                if(Game.player.items.stream().noneMatch(i->i.id==79)) return;
                //Если Шкипер на 2-м этаже, ставим его в палату
                if(Game.player.y < 480) Game.player.y = 540;
                //Если Шкипер в палате, ставим его на 2-й этаж
                else Game.player.y = 380;
            }
        }; repo.put(7777_8,script);
        /** Скрипт колескеъ - предмета при использовании из руки **/
        script = new Script() {
            @Override
            public void execute() {
                //Колескеъ можно поставить только дома
                if(Game.currentLevel.id != HUB) return;
                //Забираем коляску
                Game.player.items.remove(Game.player.equip);
                Game.player.equip = null;
                //Выполняем квест с коляской
                for(var q:Game.player.quests) if(q.id==14) q.completed = true;
                //Ставим коляску дома
                var ride = GameObject.get(47); ride.scriptId = 0;
                ride.x = 250; ride.y = 180; Level.repo.get(HUB).objects.add(ride);
            }
        }; repo.put(7777_9,script);
        /** Скрипт ключа от сейфа в больнице **/
        script = new Script() {
            @Override
            public void execute() {
                //Чертим прямоугольник вокруг игрока (зона действия ключа)
                Rectangle r = new Rectangle(Game.player.x-100,Game.player.y-100,200,200);
                //Проверяем, попадает ли запертый сейф в зону действия ключа
                GameObject locker = Game.currentLevel.objects.stream()
                        .filter(i->i.id==45 && i.hitBox.intersects(r)).findFirst().orElse(null);
                //Если сейфа рядом нет, ничего не делаем
                if(locker == null) return;
                //Создаем новый открытый сейф
                GameObject openedLocker = GameObject.get(46);
                //Удаляем закрытый сейф с уровня
                Level.repo.get(HOSPITAL).objects.remove(locker);
                //Добавляем на его место открытый
                openedLocker.x = locker.x; openedLocker.y = locker.y;
                Level.repo.get(HOSPITAL).objects.add(openedLocker);
                //Добавляем в сейф лекарства //TODO (и можно че-нибудь еще)
                openedLocker.addItem(Item.get(81));
            }
        }; repo.put(7777_10,script);
        /** Скрипт сдачи квеста с кольцом **/
        script = new Script() {
            @Override
            public void execute() {
                //Выводим реплику
                Dialogue.current = Dialogue.nurse.get(8);
            }
        }; repo.put(7777_11,script);
        //==========================================================================================//
        /**
         *
         * СКРИПТЫ ТЕСТЯ МЕДБРАТА (МЕХАНИКА)
         *
         */
        //Механик выдает новые задания
        //<<Мне бы маме помочь>>
        script = new Script() {
            @Override
            public void execute() {
                if(Game.player.quests.stream().anyMatch(q->q.owner.equals("тесть")&&!q.completed)){
                    var d = new Dialogue();
                    d.message = "Маме, маме... \n Давай не юли. Ты мне тут в одном деле обещал \n Подсобить..\n А настоящий мужик он... настоящий он.. \n Настояшщий мужик сказал он сделал \n А ты сделстоящий мужик?";
                    d.responses.add(new Dialogue.Response("ладно",0,0));
                    Dialogue.current = d;
                }
                //Проверяем, если у Шкипера нет ни одного задания (кроме догнаться)
                //Предлагаем квест с канистрой
                if(Game.player.quests.stream().noneMatch(q->q.owner.equals("тесть") && q.id!=46))
                    Dialogue.current = Dialogue.mech.get(11);
                //Если Шкипер выполнил задание с канистрой, предлагаем взять задание с трубами
                if(Game.player.quests.stream().anyMatch(q->q.id==47&&q.completed)
                && Game.player.quests.stream().noneMatch(q->q.id==48))
                    Dialogue.current = Dialogue.mech.get(18);
                //Если шкипер выполнил задание с трубами, предлагаем взять задание с деталями
                if(Game.player.quests.stream().anyMatch(q->q.id==48&&q.completed)
                        && Game.player.quests.stream().noneMatch(q->q.id==49))
                    Dialogue.current = Dialogue.mech.get(23);
                //Если шкипер выполнил задание с деталями, предлагаем взять задание с ураном
                if(Game.player.quests.stream().anyMatch(q->q.id==49&&q.completed)
                        && Game.player.quests.stream().noneMatch(q->q.id==50))
                    Dialogue.current = Dialogue.mech.get(28);
            }
        }; repo.put(8888,script);
        /** Шкипер пьет стопочку с механиком **/
        script = new Script() {
            @Override
            public void execute() {
                //Шкипер пьянеет
                Game.player.drunk += 15.0;
                //Проходит время
                GameTime.forward(HOUR_LENGTH); //1 час
            }
        }; repo.put(8888_1,script);

        /** Механик выдает задание 46 (взять догнаться) **/
        script = new Script() {
            @Override
            public void execute() {
                //Выдаем квест
                Game.player.quests.add(Quest.get(46));
                //Перемещаем механика с медбратом в квартиру
                Level.repo.get(STREET_1).objects.remove(Game.nurse);
                Level.repo.get(STREET_1).objects.remove(Game.mechanic);
                Game.mechanic.x=150;Game.mechanic.y=200;
                Game.nurse.x=300;Game.nurse.y=200;
                Level.repo.get(MECH_FLAT).objects.add(Game.nurse);
                Level.repo.get(MECH_FLAT).objects.add(Game.mechanic);
                //Сменяем диалог механику на другой (для сдачи квеста)
                Game.mechanic.dialogue = 7;
                Game.player.passwords.add("Тесть медбрата - 999");

            }
        }; repo.put(8888_2,script);

        /** Механик проверяет, принес ли Шкипер водку **/
        script = new Script() {
            @Override
            public void execute() {
                //Если не принес, спрашиваем схуяли
                if(Game.player.items.stream().noneMatch(i->i.id==78))
                {Dialogue.current = Dialogue.mech.get(8); return;}
                //Если принес, завершаем квесты механика и медбрата (взять водки и выпить с тестем)
                for(var q:Game.player.quests) if(q.id==46 || q.id==43) q.completed = true;
                //И забираем водку
                Item vodka = Game.player.items.stream().filter(i->i.id==78).findFirst().orElse(null);
                vodka.count-=1;if(vodka.count<=0)Game.player.items.remove(vodka);
                if(Game.player.equip==vodka) Game.player.equip = null;
                //Выводим диалог
                Dialogue.current = Dialogue.mech.get(9);
                //Меняем технику начальный диалог
                Game.mechanic.dialogue = 10;
            }
        }; repo.put(8888_3,script);
        /** Шкипер соглашается пройти в гараж с Техником **/
        script = new Script() {
            @Override
            public void execute() {
                //Если мы в квартире механика
                if(Game.currentLevel.id == MECH_FLAT) {
                    //Перемещаем Техника из квартиры в гараж
                    Level.repo.get(MECH_FLAT).objects.remove(Game.mechanic);
                    Level.repo.get(GARAGE).objects.add(Game.mechanic);
                    //Перемещаем Шкипера из квартиры в гараж
                    Level.repo.get(MECH_FLAT).objects.remove(Game.player);
                    Level.repo.get(GARAGE).objects.add(Game.player);
                    Game.currentLevel = Level.repo.get(GARAGE);
                    //Выводим продолжение диалога
                    Dialogue.current = Dialogue.mech.get(12);
                    //Добавляем дверь в гараж для последующих посещений
                    var door = GameObject.get(70);
                    door.x = 6200; door.y = 3370;
                    Level.repo.get(STREET_1).objects.add(door);
                }else{//Если нет, то мы уже в гараже
                    //Иначе выводим другой диалог
                    Dialogue.current = Dialogue.mech.get(13);
                }
            }
        }; repo.put(8888_4,script);
        /** Шкипер берет квест с канистрой **/
        script = new Script() {
            @Override
            public void execute() {
                //Дали квест
                Game.player.quests.add(Quest.get(47));
                //Закрыли диалог
                Dialogue.current = null;
                //Добавили реплику для сдачи
                Dialogue.mech.get(Game.mechanic.dialogue).responses.add(
                        new Dialogue.Response("Канистра...",0,8888_6)
                );
                //Дали канистру
                Game.player.addItem(Item.get(91));
            }
        }; repo.put(8888_5,script);
        /** Шкипер сдает квест с канистрой **/
        script = new Script() {
            @Override
            public void execute() {
                //Проверяем принес ли шкипер канистру
                var kanistra = Game.player.items.stream()
                        .filter(i->i.id==91).findFirst().orElse(null);
                if(kanistra == null){
                    //Если не принес
                    Dialogue.current = Dialogue.mech.get(15);
                    return;
                }
                if(Integer.parseInt(kanistra.description.split(":")[1].split("/")[0])<100) {
                    //Если принес пустую
                    Dialogue.current = Dialogue.mech.get(16);
                    return;
                }
                if(Integer.parseInt(kanistra.description.split(":")[1].split("/")[0])>=100) {
                    //Если принес полную
                    Dialogue.current = Dialogue.mech.get(17);
                    for(var q:Game.player.quests)if(q.id==47)q.completed=true;
                    //Удаляем реплику для сдачи квеста
                    Dialogue.Response response =
                            Dialogue.mech.get(Game.mechanic.dialogue).responses.stream()
                                    .filter(r->r.text.equals("Канистра...")).findFirst().orElse(null);
                    if(response!=null) Dialogue.mech.get(Game.mechanic.dialogue).responses.remove(response);
                    //Забираем канистру
                    Game.player.items.remove(kanistra);
                    Game.player.equip = null;
                }
            }
        }; repo.put(8888_6,script);
        /** Шкипер берет квест с Трубами **/
        script = new Script() {
            @Override
            public void execute() {
                //Добавялем квест
                Game.player.quests.add(Quest.get(48));
                //Добавляем реплику для сдачи квеста
                Dialogue.mech.get(Game.mechanic.dialogue).responses.add(new Dialogue.Response("труба",0,8888_9));
                //Закрываем диалог
                Dialogue.current = null;
            }
        };repo.put(8888_7,script);
        /** Шкипер сдает квест с Трубами **/
        script = new Script() {
            @Override
            public void execute() {
                //Если Шкипер принес трубу
                if(Game.player.items.stream().anyMatch(i->i.id==95)) {
                    //Выводим диалог
                    Dialogue.current = Dialogue.mech.get(22);
                    //Завершаем квест
                    for(var q:Game.player.quests)if(q.id==48)q.completed=true;
                    //Удаляем реплику для сдачи квеста
                    Dialogue.Response response = Dialogue.mech.get(Game.mechanic.dialogue).responses.stream()
                            .filter(r->r.text.equals("труба")).findFirst().orElse(null);
                    if(response!=null)Dialogue.mech.get(Game.mechanic.dialogue).responses.remove(response);
                    //забираем трубу
                    var pipe = Game.player.items.stream().filter(i->i.id==95).findFirst().orElse(null);
                    if(pipe!=null)Game.player.items.remove(pipe);if(Game.player.equip==pipe)Game.player.equip=null;
                }else {
                    //Если не принес, напоминаем
                    Dialogue.current = Dialogue.mech.get(21);
                }
            }
        };repo.put(8888_9,script);
        /** Шкипер берет квест с Гейгером **/
        script = new Script() {
            @Override
            public void execute() {
                //Добавялем квест
                Game.player.quests.add(Quest.get(49));
                //Добавляем реплику для сдачи квеста
                Dialogue.mech.get(Game.mechanic.dialogue).responses.add(new Dialogue.Response("счетчик",0,8888_11));
                //Закрываем диалог
                Dialogue.current = null;
                var specialist = Level.repo.get(SALON_SVYAZI).objects.stream().filter(o->o.id==8888_01).findFirst().get();
                var d = Dialogue.repo.get(specialist.dialogue);
                d.responses.add(new Dialogue.Response("прибор...",0,8888_10_01));
            }
        };repo.put(8888_10,script);
        //Специалист дает прибор
        script = new Script() {
            @Override
            public void execute() {
                var specialist = Level.repo.get(SALON_SVYAZI).objects.stream().filter(o->o.id==8888_01).findFirst().get();
                var d = Dialogue.repo.get(specialist.dialogue);
                var r = d.responses.stream().filter(resp -> resp.text.contains("бор")).findFirst().get();
                d.responses.remove(r);
                Game.player.addItem(Item.get(96));
                d = new Dialogue();
                d.message = "А, ты от дяди Толи.. \n Да, держи. Вот прибор который он заказывал";
                d.responses.add(new Dialogue.Response("ладно",0,0));
                Dialogue.current = d;
            }
        };repo.put(8888_10_01,script);
        /** Шкипер сдает квест с Гейгером **/
        script = new Script() {
            @Override
            public void execute() {
                //Если Шкипер принес счетчик Гейгера
                if(Game.player.items.stream().anyMatch(i->i.id==96)) {
                    //Выводим диалог
                    Dialogue.current = Dialogue.mech.get(27);
                    //Завершаем квест
                    for(var q:Game.player.quests)if(q.id==49)q.completed=true;
                    //Удаляем реплику для сдачи квеста
                    Dialogue.Response response = Dialogue.mech.get(Game.mechanic.dialogue).responses.stream()
                            .filter(r->r.text.equals("счетчик")).findFirst().orElse(null);
                    if(response!=null)Dialogue.mech.get(Game.mechanic.dialogue).responses.remove(response);
                    //забираем счетчик
                    var xrayMeter = Game.player.items.stream().filter(i->i.id==96).findFirst().orElse(null);
                    if(xrayMeter!=null)Game.player.items.remove(xrayMeter);if(Game.player.equip==xrayMeter)Game.player.equip=null;

                }else {
                    //Если не принес, напоминаем
                    Dialogue.current = Dialogue.mech.get(26);
                }
            }
        };repo.put(8888_11,script);
        /** Шкипер берет квест с ураном **/
        script = new Script() {
            @Override
            public void execute() {
                //Добавялем квест
                Game.player.quests.add(Quest.get(50));
                //Добавляем реплику для сдачи квеста
                Dialogue.mech.get(Game.mechanic.dialogue).responses.add(new Dialogue.Response("уран",0,8888_13));
                //Закрываем диалог
                Dialogue.current = null;
            }
        };repo.put(8888_12,script);
        /** Шкипер сдает квест с ураном **/
        script = new Script() {
            @Override
            public void execute() {
                //Если Шкипер принес уран
                if(Game.player.items.stream().anyMatch(i->i.id==97)) {
                    //Выводим диалог
                    Dialogue.current = Dialogue.mech.get(33);
                    //Завершаем квест
                    for(var q:Game.player.quests)if(q.id==50)q.completed=true;
                    //Удаляем реплику для сдачи квеста
                    Dialogue.Response response = Dialogue.mech.get(Game.mechanic.dialogue).responses.stream()
                            .filter(r->r.text.equals("уран")).findFirst().orElse(null);
                    if(response!=null)Dialogue.mech.get(Game.mechanic.dialogue).responses.remove(response);
                    //забираем уран
                    var uran = Game.player.items.stream().filter(i->i.id==97).findFirst().orElse(null);
                    if(uran!=null)Game.player.items.remove(uran);if(Game.player.equip==uran)Game.player.equip=null;
                    var shop = Level.repo.get(GARAGE).objects.stream().filter(o->o.id==74).findFirst().get();
                    shop.items.add(Item.get(134));//Добавляем огнемет в продажу
                }
                //Если не принес, напоминаем
                Dialogue.current = Dialogue.mech.get(26);
            }
        };repo.put(8888_13,script);
        //==========================================================================================//
        /**
         *
         * СКРИПТЫ ФАШИКА
         *
         */
        /** Фашик отвечает на приветствие Шкипера **/
        script = new Script() {
            @Override
            public void execute() {

                var dialog = new Dialogue();
                dialog.message = "Слышь, дядя, ты кто? \n А ну иди отсюда!";
                Dialogue.current = dialog;
                Dialogue.companion = Dialogue.Companion.NAZI;
                //Есть ли задание от аптекарши на драку
                var quest = Game.player.quests.stream().filter(q->q.id==53).findFirst().orElse(null);
                //Если нет, нам нечего сказать
                if(quest == null){
                    dialog.responses.add(new Dialogue.Response("ладно",0,0));
                }
                //Если есть
                else {
                    //Можно предъявить
                    dialog.responses.add(new Dialogue.Response("хватит обижать малых",0,9191_1));
                }
            }
        }; repo.put(9191,script);

        /** Фашик отвечает на предъяву **/
        script = new Script() {
            @Override
            public void execute() {

                var dialog = new Dialogue();
                dialog.message = "Хуя се, вот это предъява! \n Ты че, сука, самый смелый? \n Ну вот ща и посмотрим - ты кент или шестерка. \n Давай ща нахуй выскочим раз на раз! \n Че, зассал? Зассал нахуй, чмо? \n Ты у меня сука будешь кровью мочиться нахуй, если ноги унесешь. \n Даю тебе последний шанс, слышишь, дура? \n Извиняйся нахуй передо мной прямо сейчас, \n а там решать будем как ты за эту дерзость мне отработаешь ";
                Dialogue.current = dialog;
                Dialogue.companion = Dialogue.Companion.NAZI;

                dialog.responses.add(new Dialogue.Response("Тебе пизда",0,9191_2));
                dialog.responses.add(new Dialogue.Response("бля извини...",9191_3,0));


            }
        }; repo.put(9191_1,script);

        /** Начинается драка с Фашиком **/
        script = new Script() {
            @Override
            public void execute() {
                Game.nazi.enemy = true;
            }
        }; repo.put(9191_2,script);

        /** Фашик принимает извинения и дает задание **/
        script = new Script() {
            @Override
            public void execute() {
                var dialog = new Dialogue();
                dialog.message = "<<Сплёвывает под ноги>> \n А я так и знал что ты шнырь. Шестерка \n В общем так, извинения твои не приняты. \n Пока. \n Я тебя, шлюха, оставляю в живых, поняла? \n И за тобой должок. Ценою в жизнь \n Этот долг ты сможешь вернуть только кровью \n Заодно и сделаешь правое дело. Очистишь улицы \n Тебе нужно будет убить торговца с рынками. Хозяина палаток. \n Ты его знаешь. Ты меня понял. Или ты или он.  ";
                Dialogue.current = dialog;
                Dialogue.companion = Dialogue.Companion.NAZI;
                dialog.responses.add(new Dialogue.Response("ладно",0,0));

                Game.player.quests.add(Quest.get(57));

                Game.nazi.scriptId = 9191_4;
            }
        };repo.put(9191_3,script);

        /** Фашик спрашивает про задание **/
        script = new Script() {
            @Override
            public void execute() {
                var dialog = new Dialogue();
                dialog.message = "Э! << плюёт >> Дура, ты че приперлась?";
                Dialogue.current = dialog;
                Dialogue.companion = Dialogue.Companion.NAZI;
                dialog.responses.add(new Dialogue.Response("все готово",0,9191_5));
            }
        };repo.put(9191_4,script);

        /** Фашик првоеряет все ли готово **/
        script = new Script() {
            @Override
            public void execute() {
                    if(Game.hach.hp <= 0){
                        var dialog = new Dialogue();
                        dialog.message = "Да, я понял. \n Все, пиздуй отсюда, пока я добрый. \n Бегом!";
                        Dialogue.current = dialog;
                        Dialogue.companion = Dialogue.Companion.NAZI;
                        dialog.responses.add(new Dialogue.Response("ладно",0,0));
                        Game.nazi.scriptId = 0;
                    }else{
                        var dialog = new Dialogue();
                        dialog.message = "Нахуя ты мне пиздишь?! \n Пока этот торгаш черножопый не кормит червей \n тебе должно быть страшно на глаза мне появляться";
                        Dialogue.current = dialog;
                        Dialogue.companion = Dialogue.Companion.NAZI;
                        dialog.responses.add(new Dialogue.Response("ладно",0,0));
                    }
            }
        };repo.put(9191_5,script);

        //=========================================================================================//
        /**
         *
         * СКРИПТЫ АПТЕКАРШИ
         *
         */
        /** Аптекарша проверяет задания << мне бы маме помочь >>**/
        script = new Script() {
            @Override
            public void execute() {
                //Если не выдано заданий от аптекарши
                if(Game.player.quests.stream().noneMatch(q->q.owner.equals("аптекарша")))
                    // Предлагаем взять задане с торчками
                    Dialogue.current = Dialogue.pharmacist.get(4);
                //Если нет заданий с кормом
                if(Game.player.quests.stream().anyMatch(q->q.id==51&&q.completed)
                    && Game.player.quests.stream().noneMatch(q->q.id == 52)) {
                    //предлагаем задание с кормом
                    Dialogue.current = Dialogue.pharmacist.get(8);
                }
                //Если нет заданий с фашиком
                if(Game.player.quests.stream().anyMatch(q->q.id==52&&q.completed)
                        && Game.player.quests.stream().noneMatch(q->q.id == 53)) {
                    //предлагаем задание с фашиком
                    Dialogue.current = Dialogue.pharmacist.get(12);
                }

                if(Game.player.quests.stream().anyMatch(q->!q.completed && q.owner.equals("аптекарша")))
                    Dialogue.current = Dialogue.pharmacist.get(14);

            }
        }; repo.put(9999,script);

        /** Выдаем задание с торчками**/
        script = new Script() {
            @Override
            public void execute() {
                Game.player.quests.add(Quest.get(51));
                Dialogue.pharmacist.get(1).responses
                        .add(new Dialogue.Response("таблетки",0,9999_4));
                Dialogue.current = null;
                Game.player.passwords.add("Притон (аптекарша) - 880");
            }
        }; repo.put(9999_1,script);

        /** Выдаем задание с котятами**/
        script = new Script() {
            @Override
            public void execute() {
                Game.player.quests.add(Quest.get(52));
                Dialogue.pharmacist.get(1).responses
                        .add(new Dialogue.Response("кошки",0,9999_5));
                Dialogue.current = null;
                for (int i = 0; i < 10; i++) {
                    Game.player.addItem(Item.get(119));
                }

            }
        }; repo.put(9999_2,script);

        /** Выдаем задание с фашиком**/
        script = new Script() {
            @Override
            public void execute() {
                Game.player.quests.add(Quest.get(53));
                Dialogue.pharmacist.get(1).responses
                        .add(new Dialogue.Response("хулиган",0,9999_6));
                Dialogue.current = null;

            }
        }; repo.put(9999_3,script);

        /** Проверяем квест с торчками **/
        script = new Script() {
            @Override
            public void execute() {
                var meds = Game.player.items.stream().filter(i->i.id == 119).findFirst().orElse(null);
                if(meds != null){

                    Game.player.items.remove(meds);
                    if(Game.player.equip==meds) Game.player.equip = null;

                    Dialogue d = new Dialogue();
                    d.message = "Так, таблеточки.. \n Ну упыри, сколько сожрали то. \n Я то думала побольше останется.. \n Ну вот, как и обещала, твои деньги. \n По закупочной 200р пачка. \n Держи свои " + (meds.count * 200);
                    d.responses.add(new Dialogue.Response("ага",0,0));
                    Dialogue.current = d;

                    Game.player.money += meds.count * 200;

                    for (var q : Game.player.quests) if(q.id==51) q.completed = true;

                    Dialogue.Response response =
                            Dialogue.pharmacist.get(1).responses.stream()
                            .filter(r->r.text.contains("таблетки"))
                            .findFirst().orElse(null);
                    if(response!=null)
                        Dialogue.pharmacist.get(1).responses.remove(response);

                }else{
                    Dialogue d = new Dialogue();
                    d.message = "Так, а где таблеточки..? \n Не пОняла? \n А ты че приперся то? \n С пустыми руками то";
                    d.responses.add(new Dialogue.Response("аааааа",0,0));
                    Dialogue.current = d;
                }
            }
        };repo.put(9999_4,script);

        /** проверяем мисочки **/
        script = new Script() {
            @Override
            public void execute() {
                if(Level.repo.get(STREET_1).objects.stream().noneMatch(o->o.id==126)){
                    Dialogue d = new Dialogue();
                    d.message = "Умничка ты наш! \n Всех котяток покормил? \n Ну молодоец, ну молодец!";
                    d.responses.add(new Dialogue.Response("ладно",0,0));
                    Dialogue.current = d;

                    for (var q : Game.player.quests) if(q.id==52) q.completed = true;

                    Dialogue.Response response =
                            Dialogue.pharmacist.get(1).responses.stream()
                                    .filter(r->r.text.contains("кошки"))
                                    .findFirst().orElse(null);
                    if(response!=null)
                        Dialogue.pharmacist.get(1).responses.remove(response);


                }else {
                    Dialogue d = new Dialogue();
                    d.message = "Ты точно везде оставил корм?";
                    d.responses.add(new Dialogue.Response("не знаю..",0,0));
                    Dialogue.current = d;
                }
            }
        }; repo.put(9999_5,script);


        /** проверяем квест отпиздить фашика **/
        script = new Script() {
            @Override
            public void execute() {
                if (Game.nazi.hp <= 0) {

                    var d = new Dialogue();
                    d.message = "Ну что, ты с ним разобрался? \n Проучил его, правда? \n Ах, какой мужчина.. какой мужчина! \n Этож ты как мне то помог. Сынку то моему непутевому как помог!...\n Это корзина то моя тебе по гроб жизни обязана будет! \n Ой как я рада, как я благодарна. \n Ой, а что я могу-то тебе сделать, мил человек.. \n Знаю! Что ты говоришь там с твоей матушкой? \n Знаешь, я вообще-то тоже не пальцем деланная. \n Дамочка с образованием! Фельдшерским. \n Я за твоей мамой могу помочь поухаживать. \n Лекарства там или еще чего. В душ смогу сводить подмыть \n А ты мужик взрослый, нечего тебе с ней дома сидеть! \n Деньги иди зарабатывай! ";
                    d.responses.add(new Dialogue.Response("ладно",0,0));
                    Dialogue.current = d;
                    Dialogue.companion = Dialogue.Companion.PHARMACIST;

                    Dialogue.Response response =
                            Dialogue.pharmacist.get(1).responses.stream()
                                    .filter(r -> r.text.contains("хулиган"))
                                    .findFirst().orElse(null);
                    if (response != null)
                        Dialogue.pharmacist.get(1).responses.remove(response);

                    Game.player.quests.stream()
                            .filter(q->q.id==53)
                            .forEach(q-> q.completed = true);


                }else{
                    var d = new Dialogue();
                    d.message = "Ну что, ты с ним разобрался? ";
                    d.responses.add(new Dialogue.Response("а, бля..",0,0));
                    Dialogue.current = d;
                    Dialogue.companion = Dialogue.Companion.PHARMACIST;

                }
            }
        }; repo.put(9999_6,script);

    }
    /** Бар **/
    public static void foo(){}
}
