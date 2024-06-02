package ru.muwa.shq.engine;

import ru.muwa.shq.engine.utils.GameTime;
import ru.muwa.shq.engine.utils.Input;
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
 * �������� ����� ����.
 * �������� � ���� ������ �� ������� ������, ������, ������ � ������� ����������,
 * � ��� �� ��������� ����������� �������� ����.
 *
 */
public class Game {
    //������ �� ������ ��������� � ����������
    public static final Updater updater = new Updater();
    public static final Renderer renderer = new Renderer();

    //������ �� �������� ���
    public static final GameObject mom = GameObject.get(MOM);
    public static final GameObject hacker = GameObject.get(1111);
    public static final GameObject hach = GameObject.get(2222);
    public static final GameObject butcher = GameObject.get(3333);
    public static final GameObject girl = GameObject.get(4444);
    public static final GameObject trap = GameObject.get(5555);
    public static final GameObject officer = GameObject.get(6666);
    public static final GameObject nurse = GameObject.get(7777);
    public static final GameObject mechanic = GameObject.get(8888);
    public static final GameObject pharmacist = GameObject.get(9999);

    //������� ���������� ������� ���������� � ���������
    public static int fps = 60, renderFPS = 120;

    //�����
    public static boolean pause;

    //������ �� ������
    public static Player player = new Player();

    //������ �� ������� �������
    public static Level currentLevel;

    //���� �� ������ ������ jar �����
    public static String imgPath = "textures/";//D:\\IdeaProjects\\shq\\src\\main\\resources\\textures\\";

    //����� ������� ����
    public static void start()  {

        System.out.println("�������������...");

        //������������� static-������ �������-�����
        Item.foo();
        GameObject.foo();
        Level.foo();
        ItemTextures.foo();
        ObjectTextures.foo();
        Dialogue.foo();
        Input.foo();

        //��������� �������� ������ � ������
        Game.currentLevel = Level.repo.get(HUB);

        //��������� ������ �� �������
        Level.repo.get(HUB).objects.add(player);
        player.x = 300; player.y = 200;
        player.money = 5_000;

        //��������� ��������� ������
        player.quests.add(Quest.get(1));
        player.quests.add(Quest.get(2));
        player.quests.add(Quest.get(3));
        player.quests.add(Quest.get(4));

        //TODO: ������� ��������� ������

        //��������� ���������� �� �������

        //�����
        Level.repo.get(HACKERS_PLACE).objects.add(hacker);
        hacker.x = 200; hacker.y = 200;

        //�����
        Level.repo.get(STREET_1).objects.add(hach);
        hach.x = 2190; hach.y = 5600;

        //������
        Level.repo.get(BUTCHERY).objects.add(butcher);
        butcher.x = 200; butcher.y = 200;

        //�������
        //girl.x = 4400; girl.y = 3200;
        //Level.repo.get(STREET_1).objects.add(girl);
        //������� �������� �� ����� ����� ������� �� ����������

        //������� �� �������
        Level.repo.get(WILDBERRIES).objects.add(trap);
        trap.x = 300; trap.y = 300;

        //����
        repo.get(POLICE_OFFICE).objects.add(officer);
        officer.x = 200; officer.y = 200;

        //�������
        nurse.x = 600; nurse.y = 820;
        Level.repo.get(HOSPITAL).objects.add(nurse);
        //����� ���������� ����� ������ ������ � ��������

        //���������

        //������������� ������� ����� �� ������ ����
        GameTime.value += (int) (GameTime.HOUR_LENGTH * 8.0); //8:00
        //������
        System.out.println("������ ������������ ������");
        renderer.thread.start();
        System.out.println("������ �������� ������");
        updater.thread.start();

        //�������� ��������� ������
        Dialogue.current = Dialogue.mom.get(0);
        Dialogue.companion = Dialogue.Companion.MOM;
        Dialogue.texture = Dialogue.textures.get(Game.mom.id);

    }
}
