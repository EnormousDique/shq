package ru.muwa.shq.engine;

import ru.muwa.shq.engine.utils.GameTime;
import ru.muwa.shq.engine.utils.Input;
import ru.muwa.shq.engine.utils.saveload.Loader;
import ru.muwa.shq.entities.GameObject;
import ru.muwa.shq.entities.Item;
import ru.muwa.shq.entities.Level;
import ru.muwa.shq.entities.Player;
import ru.muwa.shq.story.Dialogue;
import ru.muwa.shq.story.Quest;
import ru.muwa.shq.textures.ItemTextures;
import ru.muwa.shq.textures.ObjectTextures;

import static ru.muwa.shq.entities.GameObject.MOM;
import static ru.muwa.shq.entities.Level.*;

/**
 *
 * Основной класс игры.
 * Содержит в себе ссылки на игровые потоки, службы, игрока и игровых персонажей,
 * а так же некоторые технические свойства игры.
 *
 */
public class Game {
    //Ссылки на потоки отрисовки и обновления
    public static final Updater updater = new Updater();
    public static final Renderer renderer = new Renderer();

    //Ссылки на основных нпс
    public static  GameObject mom = GameObject.get(MOM);
    public static  GameObject hacker = GameObject.get(1111);
    public static  GameObject hach = GameObject.get(2222);
    public static  GameObject butcher = GameObject.get(3333);
    public static  GameObject girl = GameObject.get(4444);
    public static  GameObject trap = GameObject.get(5555);
    public static  GameObject officer = GameObject.get(6666);
    public static  GameObject nurse = GameObject.get(7777);
    public static  GameObject mechanic = GameObject.get(8888);
    public static  GameObject pharmacist = GameObject.get(9999);
    public static  GameObject nazi = GameObject.get(9191);


    //Частота обновления потоков обновления и отрисовки
    public static int fps = 60, renderFPS = 120;

    //Пауза
    public static boolean pause;

    //Ссылка на игрока
    public static Player player = new Player();

    //Ссылка на текущий уровень
    public static Level currentLevel;

    //Путь до текстур внутри jar файла
    public static String imgPath = "textures/";//D:\\IdeaProjects\\shq\\src\\main\\resources\\textures\\";

    //Метод запуска игры
    public static void start()  {

        System.out.println("инициализация...");

        //инициализация static-блоков классов-служб
        Item.foo();
        GameObject.foo();
        Level.foo();
        ItemTextures.foo();
        ObjectTextures.foo();
        Dialogue.foo();
        Input.foo();

        //Установка текущего уровня в движок
        Game.currentLevel = Level.repo.get(HUB);

        //Добавляем игрока на позицию
        Level.repo.get(HUB).objects.add(player);
        player.x = 300; player.y = 200;

        //Добавляем стартовые квесты
        player.quests.add(Quest.get(1));
        //player.quests.add(Quest.get(2));
        //player.quests.add(Quest.get(3));
        //player.quests.add(Quest.get(4));
        player.quests.add(Quest.get(58));

        //Добавляем персонажей на позиции

        //Мама
        Game.mom.x = 125;
        Game.mom.y = 150;
        repo.get(HUB).objects.add(Game.mom);

        //Хакер
        Level.repo.get(HACKERS_PLACE).objects.add(hacker);
        hacker.x = 200; hacker.y = 200;

        //Хачик
        Level.repo.get(STREET_1).objects.add(hach);
        hach.x = 2190; hach.y = 5600;

        //Мясник
        Level.repo.get(BUTCHERY).objects.add(butcher);
        butcher.x = 200; butcher.y = 200;

        //Педовка
        //girl.x = 4400; girl.y = 3200;
        //Level.repo.get(STREET_1).objects.add(girl);
        //Педовка попадает на карту после общения со школьником

        //Ловушка из валберс
        Level.repo.get(WILDBERRIES).objects.add(trap);
        trap.x = 300; trap.y = 300;

        //Мент
        repo.get(POLICE_OFFICE).objects.add(officer);
        officer.x = 200; officer.y = 200;

        //Медбрат
        nurse.x = 600; nurse.y = 820;
        Level.repo.get(HOSPITAL).objects.add(nurse);
        //Тесть появляется после взятия квеста у медбрата

        //Аптекарша
        pharmacist.x = 200; pharmacist.y = 250;
        repo.get(DRUGSTORE).objects.add(pharmacist);

        //Фашик
        nazi.x = 300; nazi.y = 300;
        repo.get(SOCCER_FIELD).objects.add(nazi);

        //Остальные

        //Устанавливаем игровое время на начало игры
        GameTime.value += (int) (GameTime.HOUR_LENGTH * 8.0); //8:00
        //Запуск
        System.out.println("Запуск графического движка");
        renderer.thread.start();
        System.out.println("Запуск игрового движка");
        updater.thread.start();

        //Включаем стартовый диалог
        Dialogue.current = Dialogue.mom.get(0);
        Dialogue.companion = Dialogue.Companion.MOM;
        Dialogue.texture = Dialogue.textures.get(Game.mom.id);

    }
    public static void switchLevel(int id){
        Game.currentLevel.objects.remove(player);
        Game.currentLevel = repo.get(id);
        Game.currentLevel.objects.add(player);
    }

    public static void load() {

        System.out.println("инициализация...");

        Item.foo();
        GameObject.foo();
        ItemTextures.foo();
        ObjectTextures.foo();
        Input.foo();

        //Загрузка уровней и игрока из сейва
        Loader.load();
        Game.currentLevel.objects.add(player);

        //Ишем квестовых персов и сохраняем в ссылки
        for(var l : repo.values())
            for(var o : l.objects)
                switch (o.id){
                case 2 -> Game.mom = o;
                case 1111 -> Game.hacker =o;
                case 2222 -> Game.hach = o;
                case 3333 -> Game.butcher = o;
                case 4444 -> Game.girl = o;
                case 5555 -> Game.trap = o;
                case 6666 -> Game.officer = o;
                case 7777 -> Game.nurse = o;
                case 8888 -> Game.mechanic = o;
                case 9999 -> Game.pharmacist = o;
                case 9191 -> Game.nazi = o;
                }

        // Подкручиваем НПЦ диалоги
        if(Game.player.quests.stream().anyMatch(q-> q.id ==10 && q.completed))
             Game.hacker.dialogue = 4;
        if(Game.player.quests.stream().anyMatch(q->q.owner.equals("тесть")))
            Game.mechanic.dialogue = 7;
        if(Game.player.quests.stream().anyMatch(q-> q.id ==46 && q.completed))
            Game.mechanic.dialogue = 10;





        //Запуск
        System.out.println("Запуск графического движка");
        renderer.thread.start();
        System.out.println("Запуск игрового движка");
        updater.thread.start();

    }
}
