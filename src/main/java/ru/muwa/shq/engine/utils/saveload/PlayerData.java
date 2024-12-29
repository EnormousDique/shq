package ru.muwa.shq.engine.utils.saveload;

import ru.muwa.shq.engine.Game;
import ru.muwa.shq.engine.utils.GameTime;
import ru.muwa.shq.entities.Item;
import ru.muwa.shq.entities.Player;

public class PlayerData {
    public int x,y;
    public double pee=0,poo=0;
    public int money = 0;
    public double hunger = 0, thirst = 0, /* nuts */ crazy = 0, sleepy = 0, wanted = 0,smoke = 0, stimulate = 0, drunk = 0;
    public boolean busy,dead,rent,good, atm;
    public Item equip;
    public int busFare = 100;
    public long mommaFullness,
            mommaHealth,
            mommaClean ;
    public Item hat,torso,foot;
    public int currentLevelId;
    public long time;
    public PlayerData(){}
    public PlayerData(Player player){
        x = player.x;
        y = player.y;
        pee = player.pee;
        poo = player.poo;
        money = player.money;
        hunger = player.hunger;
        thirst = player.thirst;
        crazy = player.crazy;
        sleepy = player.sleepy;
        wanted = player.wanted;
        smoke = player.smoke;
        stimulate = player.stimulate;
        drunk = player.drunk;
        busy = player.busy;
        dead = player.dead;
        rent = player.rent;
        good = player.good;
        atm = player.atm;
        equip = player.equip;
        busFare = player.busFare;
        mommaFullness = player.mommaFullness;
        mommaHealth = player.mommaHealth;
        mommaClean = player.mommaClean;
        hat = player.hat;
        torso = player.torso;
        foot = player.foot;
        currentLevelId = Game.currentLevel.id;
        time = GameTime.get();

    }
}
