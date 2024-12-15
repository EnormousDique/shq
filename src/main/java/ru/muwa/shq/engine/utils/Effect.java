package ru.muwa.shq.engine.utils;

import ru.muwa.shq.engine.Game;
import ru.muwa.shq.engine.Renderer;
import ru.muwa.shq.entities.*;
import ru.muwa.shq.story.Dialogue;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;

import static ru.muwa.shq.engine.utils.Camera.Kink.*;

public class Effect {

    /** ������ ������ �������� **/
    public static void work()
    {
        if(Game.player.hp > 100) Game.player.hp = 100;
        handleNoise();
        handleBusy();
        handleDeath();
        handleCop();
        handleHunger();
        handleSleepy();
        handleThirst();
        handleCrazy();
        handleStamina();
        handleStim();
        handleDrunk();
        handleSmoke();
        applyBonusesFromActiveEffects();
    }

    /** ������ ��������� ����� **/
    private static long drunkTimer = 0;
    /** ������ ��������� **/
    private static long drunkCameraTimer = 0;
    /** ������ �������� �� ���������� **/
    private static long drunkCameraReleaseTimer = 0;
    /** ���������� ������ **/
    private static void handleDrunk() {
        //��������� �������
        if(Game.player.drunk > 100) Game.player.drunk = 100;
        //��������� ������ �� 1 � 1 ��� (2 ������� ������)
        if(drunkTimer<System.currentTimeMillis()) {
            drunkTimer = System.currentTimeMillis() + 1_000;
            Game.player.drunk -=1;
        }
        //��������� �������
        if(Game.player.drunk < 0) Game.player.drunk = 0;
        //���������� �����������
        if(Game.player.drunk > 5 && drunkCameraReleaseTimer < System.currentTimeMillis()){
            if(Camera.kink == null) {
                drunkCameraTimer = (long) (System.currentTimeMillis() + Game.player.drunk * 100);
                double chance = Math.random();
                if(chance < 0.25) Camera.kink = LEFT;
                if(chance > 0.25 && chance < 0.5) Camera.kink = RIGHT;
                if(chance > 0.5 && chance < 0.75) Camera.kink = UP;
                if(chance > 0.75) Camera.kink = DOWN;
            }
        }
        if(drunkCameraTimer < System.currentTimeMillis() && drunkCameraReleaseTimer < System.currentTimeMillis()) {
            Camera.kink = null;
            drunkCameraReleaseTimer = (long) (System.currentTimeMillis() + 4_000 - (Game.player.drunk * 30));
        }
    }

    /** ������ �������� �� ������ ������ **/
    private static long newGlitchTimer = 0;
    /** ������ ������ ������ ���������� **/
    private static long glitchTimer = 0;
    /** ������ ������� ����� ��-�� ������������ (������) **/
    private static long smokeCalmTimer = 0;
    /** ������ ������� ������������ **/
    private static long smokeTimer = 0;
    /** ���������� ������������� **/
    private static void handleSmoke() {
        //��������� �������
        if(Game.player.smoke > 100) Game.player.smoke = 100;
        //������������ ������ �� 1 � 2 ��� (4 ������� ������)
        if(smokeTimer<System.currentTimeMillis()) {
            smokeTimer = System.currentTimeMillis() + 2_000;
            Game.player.smoke -=1;
        }
        //��������� �������
        if(Game.player.smoke < 0) Game.player.smoke = 0;
        //���� ������ ��������, �� ��� ���������
        if(Game.player.smoke > 1 && smokeCalmTimer < System.currentTimeMillis()) {
            Game.player.crazy -= 2;
            //���� ��������, ������
            if(Game.player.smoke > 70) Game.player.crazy += 2;
            //������ �� 750 ���� (�������)
            smokeCalmTimer = System.currentTimeMillis() + 750;
        }
        //��������� ������ ������
        if(!Renderer.smokedRendering && Game.player.smoke > 5 && newGlitchTimer<System.currentTimeMillis()) {
            Renderer.smokedRendering = true;
            glitchTimer = (long) (System.currentTimeMillis() + Game.player.smoke * 100);
        }
        if(Renderer.smokedRendering && glitchTimer < System.currentTimeMillis()) {
            Renderer.smokedRendering = false;
            newGlitchTimer = (long) (System.currentTimeMillis() + (1_500 - Game.player.smoke * 10));
        }
    }

    /** ������ ������� ������������ **/
    private static long stimTimer = 0;
    /** ������ ���������� �������� ������������� **/
    private static long stimStaminaTimer = 0;
    /** ������ ���������� ������ ������������� **/
    private static long stimCrazyTimer = 0;
    /** ���������� ������������� **/
    private static void handleStim() {
        //��������� �������
        if(Game.player.stimulate > 100) Game.player.stimulate = 100;
        //������������ ������ �� 1 � 1.5 ��� (3 ������� ������)
        if(stimTimer<System.currentTimeMillis()){ stimTimer = System.currentTimeMillis() + 1_500;Game.player.stimulate -=1;}
        //��������� �������
        if(Game.player.stimulate < 0) Game.player.stimulate = 0;
        //� ������ ������ % ���������� ��������������� 1 ������� ��� � (150 - % ������������) ��������
        if(Game.player.stimulate > 5 && stimStaminaTimer < System.currentTimeMillis()){
            if(Game.player.stimulate > Math.random()*100) {
                stimStaminaTimer = (long)
                        (System.currentTimeMillis() + (150 - Game.player.stimulate));
                Game.player.stamina += 3;
                Game.player.hp += 1;
                if(Game.player.stimulate > 50) Game.player.hp += 1;
            }
        }
        //���� ������ ���������, �������� �����
        if(Game.player.stimulate > 66 && stimCrazyTimer < System.currentTimeMillis()) {
            if(Game.player.stimulate > Math.random()*100) {
                stimCrazyTimer = (long)
                        (System.currentTimeMillis() + (2000- Game.player.stimulate*15));
                Game.player.crazy += 1;
            }
        }
    }


    /** ������ � �������� �������� (������� ���� � �-��) **/
    private static void applyBonusesFromActiveEffects() {

        //���� �������, ������� ����� ������ �� ������ ��� ������
        int totalBonusHp = 0, totalBonusStamina = 0, totalBonusPsycho = 0, totalBonusSpeed = 0, totalBonusSprint = 0,
                totalCrazyBonus = 0;

        //��������� �������� �������� � ������� ������
        //����� ������
        for (int i = 0; i < Game.player.effects.size(); i++) {
            Item.Effect e = Game.player.effects.get(i);
            //��������� ����� � ���� ��
            if(e.hpBonus != 0) totalBonusHp += e.hpBonus;
            //��������� ����� � ���� ������ ���
            if(e.staminaBonus != 0) totalBonusStamina += e.staminaBonus;
            //��������� ����� � ����������
            if(e.psychoBonus != 0) totalBonusPsycho += e.psychoBonus;
            //��������� ����� � �������� ������
            if(e.speedBonus != 0) totalBonusSpeed += e.speedBonus;
            //��������� ����� � �������� ����
            if(e.sprintBonus != 0) totalBonusSprint += e.sprintBonus;
        }
        //��������� ������� �������� � ���������� �������
        //����� ���������
        if(Game.player.stimulate > 5) totalBonusStamina += Game.player.stimulate;
        if(Game.player.stimulate > 10) totalBonusSpeed += 1 + Game.player.stimulate > 30?1:0 + Game.player.stimulate > 55?1:0;
        if(Game.player.stimulate > 35) totalBonusSprint += 1 + Game.player.stimulate > 30?1:0 + Game.player.stimulate > 55?1:0;
        if(Game.player.stimulate > 55) totalBonusSprint += 1;
        //����� �����
        if(Game.player.smoke > 70) totalBonusStamina -= (int) (Game.player.smoke - 40);
        //����� �����
        if(Game.player.drunk > 10) totalBonusHp += (int) (Game.player.drunk / 5);
        //��������� ������ �������� ��������
        Game.player.bonusHp = totalBonusHp;
        Game.player.bonusStamina = totalBonusStamina;
        Game.player.bonusPsycho = totalBonusPsycho;
        Game.player.bonusSpeed = totalBonusSpeed;
        Game.player.bonusSprint = totalBonusSprint;
    }

    /** ������ ������� **/
    private static long staminaTimer = 0;
    /** ���������� �������� **/
    private static void handleStamina() {
        if(Game.player.stamina>Game.player.baseStamina + Game.player.bonusStamina)
            Game.player.stamina = Game.player.baseStamina+Game.player.bonusStamina;
        if(Game.player.stamina<0) Game.player.stamina = 0;
        if(staminaTimer > System.currentTimeMillis()) return;
        if (Game.player.thirst > 99) return;
        if (Game.player.hunger > 99) return;
        if(Input.keyboard.map.get(Input.keyboard.SHIFT)) return;
        Game.player.stamina += 1;
        staminaTimer = System.currentTimeMillis() + Game.player.thirst<50?20:100;
    }

    /** ������ ����� **/
    private static long crazyTimer = 0;
    /** ���������� ������ **/
    private static void handleCrazy() {
        //��������� �������
        if(Game.player.crazy > 100) Game.player.crazy = 100;
        if(Game.player.crazy < 0) Game.player.crazy = 0;
        if(Game.player.sleepy < 30 && crazyTimer < System.currentTimeMillis()){
            //���� ������ �� ������, ��������������� 1 ���� ��� � 3 ��� (6 ������� ������)
            Game.player.crazy -= 1;
            crazyTimer = System.currentTimeMillis() + 3_000;
        } else if (Game.player.sleepy > 99 && crazyTimer < System.currentTimeMillis()) {
            //���� ������� �������� � ���, �� �������� 1 ���� ��� � 1 ��� (2 ������� ������)
            Game.player.crazy += 1;
            crazyTimer = System.currentTimeMillis() + 1_500;
        }
    }

    /** ������ ������ **/
    private static long hungerTimer = 0;
    /** ���������� ������� **/
    private static void handleHunger() {
        //���� ����� ���� 100, ������� �� 100
        if(Game.player.hunger > 100) Game.player.hunger = 100;
        //���� ����� ���� 0, �������� �� 0
        if(Game.player.hunger < 0) Game.player.hunger = 0;
        //��������� ������ ������
        if(hungerTimer > System.currentTimeMillis()) return;
        Game.player.hunger +=0.4; //�������� ����� �� 1
        hungerTimer = System.currentTimeMillis() + 4_000; //������ 4 ������� (8 ������� �����)
        //���� ������ ���, ���������� ��
        if(Game.player.hunger < 25 && Game.player.hp < 99)  Game.player.hp += 1;
        if(Game.player.hunger < 50 && Game.player.hp < 99) Game.player.hp += 2;
        //���� ����� �� ������, �������� 1 �� ������ 10 ��� (20 ������� �����)
        if(Game.player.hunger > 99){Game.player.hp -= 1; hungerTimer = System.currentTimeMillis() + 10_000;}
    }
    /** ������ ����� **/
    private static long thirstTimer = 0;
    /** ���������� ������ **/
    private static void handleThirst() {
        //���� ����� ���� 100, ������� �� 100
        if(Game.player.thirst > 100) Game.player.thirst = 100;
        //���� ����� ���� 0, �������� �� 0
        if(Game.player.thirst < 0) Game.player.thirst = 0;
        //��������� ������ �����
        if(thirstTimer > System.currentTimeMillis()) return;
        Game.player.thirst +=0.3; //�������� ����� �� 1
        thirstTimer = System.currentTimeMillis() + 2_000; //������ 2 ������� (4 ������� �����)
        }

    /** ������ ���������� **/
    private static long sleepyTimer = 0;
    /** ���������� ����������� **/
    private static void handleSleepy() {
        //���� ���������� ���� 100, ������� �� 100
        if(Game.player.sleepy > 100) Game.player.sleepy = 100;
        //���� ���������� ���� 0, �������� �� 0
        if(Game.player.sleepy < 0) Game.player.sleepy = 0;
        //��������� ������ ����������
        if(sleepyTimer > System.currentTimeMillis()) return;
        Game.player.sleepy +=0.4; //�������� ���������� �� 1
        sleepyTimer = System.currentTimeMillis() + 5_150; //������ 5.15 ������ (~10 ������� �����)
    }

    private static void handleCop() {
        if(Game.player.wanted > 0) Game.player.wanted -= 0.005 * (Game.player.wanted/10);
    }

    private static void handleDeath() {
        if(Game.player.hp <= 0)
            Minigame.current = Minigame.get(14);
    }

    private static void handleBusy() {
        boolean busy = false;
        if(Minigame.current != null) busy = true;
        for (int i = 0; i < Game.currentLevel.objects.size(); i++)
            if (Game.currentLevel.objects.get(i).opened) {
                busy = true;
                break;
            }
        if(Dialogue.current != null) busy = true;
        Game.player.busy = busy;
    }

    //TODO �� ���� ��� ������ ���� ������ � �������� ��� ���
    private static void handleNoise() {
        //���� ����� �������, �� ����� �������� �������.
        if(Game.currentLevel.isIndoors && Game.currentLevel.noise > 99)
        {
            //TODO: ������ � ����� "����� �������"
            //���� ��������� � ������ ���� ������
            GameObject nearestSpawnZone = null;
            List<GameObject> zones = Game.currentLevel.objects.stream().filter(o -> o.id == 222).toList();
            if (!zones.isEmpty())
            {
                nearestSpawnZone = zones.get(0);
                for (GameObject z : zones) {

                    Point2D playerCenter = new Point2D.Double(Game.player.hitBox.getCenterX(),Game.player.hitBox.getCenterY());
                    Point2D zoneCenter = new Point2D.Double(z.hitBox.getCenterX(),z.hitBox.getCenterY());
                    Point2D nearestZoneCenter = new Point2D.Double(nearestSpawnZone.hitBox.getCenterX(),nearestSpawnZone.hitBox.getCenterY());

                    if(playerCenter.distance(nearestZoneCenter) > playerCenter.distance(zoneCenter)) nearestSpawnZone = z;

                }
            }
            //���� ���� �������
            if(nearestSpawnZone != null)
            {
                if(Math.random() < 0.001) {
                    //� ��������� ������ ��������� �����
                    GameObject enemy = GameObject.get(4);
                    enemy.x = nearestSpawnZone.x;
                    enemy.y = nearestSpawnZone.y;
                    Game.currentLevel.objects.add(enemy);
                }
            }
        }
        for (Map.Entry<Integer, Level> entry : Level.repo.entrySet())
        {
            //���������� �� ������� � ������� �����
            Level level = entry.getValue();
            if(level.isIndoors && level.noise > 0)  level.noise -=0.001;

        }

    }
}
