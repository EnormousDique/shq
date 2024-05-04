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
 * Класс пользовательских сценариев
 */
public abstract class Script {
    /** Основной метод "выполнить" сценарий **/
    public abstract void execute();
    public boolean expired; //Поле, используемое для пометки сценария как "отработавший"
    public static HashMap<Integer,Script> repo = new HashMap<>(); //Хранилище сценариев
    private static HashMap<Integer,Long> momDrugs = new HashMap<>(),
                                         momFood = new HashMap<>();
    //Мапа для хранения паролей домофона. Ключ - координаты назначения домофона, значение - пароль.
    private static HashMap<Coordinates,String> padiquePasswords = new HashMap<>();
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
        momFood.put(50,HOUR_LENGTH * 6);//Паста
        momFood.put(63,HOUR_LENGTH * 1);//Сырник
        momFood.put(57,HOUR_LENGTH * 1);//салат
        momFood.put(58,HOUR_LENGTH * 4);//жареная курица
        momFood.put(59,HOUR_LENGTH * 10);//стейк
        momFood.put(60,HOUR_LENGTH * 3);//котлеты
        momFood.put(61,HOUR_LENGTH * 5);//куриный суп
        momFood.put(62,HOUR_LENGTH * 2);//картошка с лучком
        momFood.put(64,HOUR_LENGTH * 12);//борщ
        //Какие лекарства помогают маме и на какое время
        momDrugs.put(46,HOUR_LENGTH * 6);
        momDrugs.put(47,HOUR_LENGTH * 12);
        momDrugs.put(48,HOUR_LENGTH * 24);
        momDrugs.put(49,HOUR_LENGTH * 36);
        //Пароли от подъездов
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
         * СКРИПТЫ
         *
         */
        /** Скрипт воскрешения **/
        Script script = new Script() {
            //Воскрешение
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
                }
                //Спим 4 часа
                if(Minigame.current.input.startsWith("4")) {
                    Game.player.sleepy -= 50;
                    GameTime.forward(4 * HOUR_LENGTH);
                }
                //Спим 8 часов
                if(Minigame.current.input.startsWith("8")) {
                    Game.player.sleepy = 0;
                    GameTime.forward(8 * HOUR_LENGTH);
                }
                //Спим 12 часов
                if(Minigame.current.input.startsWith("12")) {
                    Game.player.sleepy = 0;
                    Game.player.crazy = Math.min(0, Game.player.crazy - 50);
                    GameTime.forward(12 * HOUR_LENGTH);
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
                Game.player.stimulate += 20;
                //Повышаем дыхалку
                Game.player.stamina += 50;
                //Повышаем псих
                Game.player.crazy += 10;
                //Даем хп
                Game.player.hp += 20;
                //Даем сушняк
                Game.player.thirst += 10;
                //Снимаем сонливость
                if(Game.player.sleepy > 40) Game.player.sleepy -= 15;
                //Снимаем голод
                if(Game.player.hunger < 60) Game.player.hunger -= 10;
                //Забираем соль
                Game.player.equip.count -=1;
                if(Game.player.equip.count < 0){
                    Game.player.items.remove(Game.player.equip);
                    Game.player.equip = null;
                }
            }
        }; repo.put(5,script);

        /** Скрипт мини-игры с ментовской подставой ("красная" закладка) **/
        //TODO: перенести в хача???
        script = new Script() {
            @Override
            public void execute() {
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
                Game.player.psycho -= 10;
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
                            if(craftedItem.id == 63) craftedItem.count = 6;
                            if (craftedItem.id == 60) craftedItem.count = 3;
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
                    volume += 5; //Повышаем объем бензина
                    kanistra.description = "Заполнена:"+volume+"/100"; //Обновляем описание
                    Game.player.wanted += 20; //Мусоров такое может заебать
            }
        };repo.put(17,script);
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
                            Dialogue.current = null;
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
                            Dialogue.current = null;
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
                Game.player.mommaClean += DAY_LENGTH * 2;
                GameObject mom = Game.currentLevel.objects.stream().filter(o->o.name.equals("mom")).findFirst().get();
                mom.y=200;
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
                    mom.x=240;
                    Dialogue.current = null;
                } else{
                    Dialogue d = new Dialogue(); d.message = "Я недавно мылася";
                    d.responses = List.of(new Dialogue.Response("Ладно",0,0));
                    Dialogue.current = d;
                }
            }
        };
        repo.put(24,script);
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
                Game.player.addItem(Item.get(87));
            }
        };repo.put(88,script);
        /** Скрипт косяка**/
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
        /** Скрипт бумажки для самокруток **/
        script = new Script() {
            @Override
            public void execute() {
                //TODO:
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
                pc.x = 400; pc.y = 250;
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
                pc.x=400;pc.y=150;
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
                if(!bought) Dialogue.current = Dialogue.hach.get(3);
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
                d.responses = List.of(new Dialogue.Response("Где малолетки ошиваются?",0,3333_7));
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
                Level.repo.get(BUILDING_4).objects.add(door);
                //Добавляем реплику для сдачи квеста
                Dialogue.butcher.get(6).responses
                        .add(new Dialogue.Response("Пацаненок...",0,3333_15));
                //Закрываем диалог
                Dialogue.current = null;
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
                if(Game.player.items.stream().anyMatch(i->i.id==85)){
                    //Если принес, завершаем квест
                    for(var q:Game.player.quests) if(q.id==32) q.completed = true;
                    //Даем награду
                    Game.player.money += 10_000;
                    //Выводим диалог
                    Dialogue.current = Dialogue.butcher.get(12);
                    //Забираем пацана
                    var pacan = Game.player.items.stream().filter(i->i.id==85).findFirst().orElse(null);
                    if(pacan!=null) Game.currentLevel.objects.remove(pacan);
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
                    Dialogue.current = Dialogue.butcher.get(0);
                    //Удаляем реплику для сдачи квеста
                    Dialogue.Response response =
                            Dialogue.butcher.get(6).responses
                            .stream()
                            .filter(r -> r.text.equals("Водила.."))
                            .findFirst()
                            .orElse(null);
                    if(response!= null) Dialogue.butcher.get(6).responses.remove(response);
                }
                //Если нет, напоминаем о задании
                Dialogue.current = Dialogue.butcher.get(0);
            }
        };repo.put(3333_20,script);
        /** Шкипер берет квест "где деньги ч3" **/
        script = new Script() {
            @Override
            public void execute() {
                //TODO
                //Даем задание
                //Добавляем реплику для сдачи квеста
            }
        };repo.put(3333_21,script);
        /** Шкипер сдает квест "где деньги ч3" **/
        script = new Script() {
            @Override
            public void execute() {
                //TODO
                //Проверяем выполнение условий
                //Если да, выполняем квест
                //Даем награду
                //Выводим диалог
                //Отключаем таймер мамы
                //Удаляем реплику для сдачи квеста
                //Если нет, напоминаем о задании.
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
            }
        };repo.put(4444,script);
        /** Шкипер сказал педовке о папе **/
        script = new Script() {
            @Override
            public void execute() {
                //Выполняем задание найти дочку
                for(var q:Game.player.quests) if(q.id==6)q.completed=true;
                //Выводим следующий диалог
                Dialogue.current = Dialogue.girl.get(2);
            }
        };repo.put(4444_3,script);
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
                //Меняем педовке основной диалог

            }
        };repo.put(4444_6,script);
        /** Скрипт Кости **/
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
        /** **/

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
                    //TODO добавить елдак в интернет-каталог валберса
                    return;
                }
                //Если есть невыполненное (кроме адской дрочки) задание
                if(Game.player.quests.stream().anyMatch(q->q.owner.equals("трап") && !q.completed && q.id!=31))
                    //Выводим диалог
                    Dialogue.current = Dialogue.trap.get(8);
            }
            //Проверяем, есть ли у Шкипера невыполненые (кроме адской дрочки)

        };repo.put(5555,script);

        /** Ловушка из валберс проверяет какие поступили посылки **/
        script = new Script() {
            @Override
            public void execute() {
                //Находим склад
                GameObject container = null;//Level.repo.get(WILDBERRIES).objects.stream().filter(o->o.id==1).findFirst().orElse(null);
                //Если не нашли склад или там пусто, сообщаем Шкиперу
                if(container == null || container.items.isEmpty()) Dialogue.current = Dialogue.trap.get(3);
                else{
                    //Если посылка есть, но у шкипера нет места, сообщаем ему об этом
                    if(Game.player.items.size()>=ITEMS_CAPACITY) {Dialogue.current = Dialogue.trap.get(4); return;}
                    //Берем посылку
                    Item item = container.items.get(0);
                    //Даем игроку
                    Game.player.addItem(item);
                    //Списываем со склада
                    container.items.remove(item);
                    //Сообщаем игроку о выдаче посылки
                    Dialogue d = new Dialogue();
                    d.message = "Вот твоя посылка. Это - "+item.name+". "+item.description+". Приятного пользования!";
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
                //todo
                //Добавляем реплику на сдачу квеста 31 (адская дрочка)
                Dialogue.trap.get(1).responses.add(new Dialogue.Response("Отнес",0,5555_4));
            }
        };repo.put(5555_3,script);

        //TODO
        //Ловушка из валберс проверяет что Шкипер отнес тюбик
        script = new Script() {
            @Override
            public void execute() {

            }
        };repo.put(5555_4,script);
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
                //Проверяем, если у Шкипера нет ни одного задания (кроме догнаться)
                //Предлагаем квест с канистрой
                if(Game.player.quests.stream().noneMatch(q->q.owner.equals("тесть") && q.id!=46))
                    Dialogue.current = Dialogue.mech.get(11);
                //Если Шкипер выполнил задание с канистрой, предлагаем взять задание с трубами
                if(Game.player.quests.stream().anyMatch(q->q.id==47&&q.completed)
                && Game.player.quests.stream().noneMatch(q->q.id==48))
                    Dialogue.current = Dialogue.mech.get(0);
                //Если шкипер выполнил задание с трубами, предлагаем взять задание с деталями
                if(Game.player.quests.stream().anyMatch(q->q.id==48&&q.completed)
                        && Game.player.quests.stream().noneMatch(q->q.id==49))
                    Dialogue.current = Dialogue.mech.get(0);
                //Если шкипер выполнил задание с деталями, предлагаем взять задание с ураном
                if(Game.player.quests.stream().anyMatch(q->q.id==49&&q.completed)
                        && Game.player.quests.stream().noneMatch(q->q.id==50))
                    Dialogue.current = Dialogue.mech.get(0);
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
    }

    /** Бар **/
    public static void foo(){}
}
