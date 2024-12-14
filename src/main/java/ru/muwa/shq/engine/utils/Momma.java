package ru.muwa.shq.engine.utils;

import ru.muwa.shq.engine.Game;
import ru.muwa.shq.entities.GameObject;
import ru.muwa.shq.entities.Level;
import ru.muwa.shq.story.Dialogue;
import ru.muwa.shq.story.scripts.Script;

import static ru.muwa.shq.engine.utils.GameTime.DAY_LENGTH;
import static ru.muwa.shq.engine.utils.Momma.Status.*;
import static ru.muwa.shq.entities.Level.ROOF_RUINS;

public class Momma {
    public enum Status { MOM, GO_LEFT, GO_RIGHT, SHOOT}
    public static GameObject boss = GameObject.get(147);
    private static final int BOSS_VERTICAL_SPEED = 4;
    public static long shootPhaseTimer = 0L;
    private static long shootTimer = 0L;
    private static final long SHOOT_INTERVAL = 666;
    public static Status status = MOM;
    public static void work() {

        if (status != MOM && Game.currentLevel == Level.repo.get(ROOF_RUINS)) {

            if(boss.hp <= 0){
                Game.player.busy = true;
                var d = new Dialogue();
                d.message = "Ну вот. Все кончено. \n Игра пройдена на плохую (каноничную) концовку";
                Dialogue.current = d;
                return;
            }

            //Поведение мамы в состоянии босса
            //Идем вправо
            if(status == GO_RIGHT){
                boss.x += boss.speed;
                if(boss.y > Game.player.y)
                    boss.y -= BOSS_VERTICAL_SPEED;
                else boss.y += BOSS_VERTICAL_SPEED;
                if(boss.x > 1360) status = GO_LEFT;
                if(boss.hp < boss.startHp/2){
                    if(shootTimer < System.currentTimeMillis()){
                        Combat.enemyShoot(boss);
                        shootTimer = System.currentTimeMillis() + SHOOT_INTERVAL;
                    }
                }
            }
            if(status == GO_LEFT)
            {
                boss.x -= boss.speed;
                if(boss.y > Game.player.y)
                    boss.y -= BOSS_VERTICAL_SPEED;
                else boss.y += BOSS_VERTICAL_SPEED;
                if(boss.x < 370) status = GO_RIGHT;
                if(boss.hp < boss.startHp/2 ){
                    if(shootTimer < System.currentTimeMillis()){
                        Combat.enemyShoot(boss);
                        shootTimer = System.currentTimeMillis() + 1_000;
                    }
                }
            }
            if(status == SHOOT){
                if(shootPhaseTimer + 60_000 < System.currentTimeMillis()) {
                    boss.x = 400;
                    status = GO_RIGHT;
                }
                if(boss.x > 200)
                    boss.x -= boss.speed;
                if(shootTimer < System.currentTimeMillis()){
                    Combat.enemyShoot(boss);
                    shootTimer = System.currentTimeMillis() + 1_000;
                }
            }

            if(boss.hitBox.intersects(Game.player.hitBox)) {
                Combat.dealDamage(Game.player,20);
                Game.player.y += ((Game.player.y < 350)?(100):(100*-1));
            }

        } else {
            //Обычное поведение мамы
            if (Game.player.smoke < 1) {
                if (Game.player.quests.stream().noneMatch(q -> q.id == 53 && q.completed)) {
                    Game.player.mommaHealth -= (1_000 / 60);
                    Game.player.mommaClean -= (1_000 / 60);
                } else {
                    Game.player.mommaHealth = DAY_LENGTH * 2;
                    Game.player.mommaClean = DAY_LENGTH * 2;
                }
                if(Game.player.quests.stream().noneMatch(q->q.id == 34 && q.completed)) {
                    Game.player.mommaFullness -= (1_000 / 60);
                }else{
                    Game.player.mommaFullness = DAY_LENGTH * 2;
                }
            }
            //TODO: добавить уровень сложности ???
            if (Game.player.mommaHealth <= 0 || Game.player.mommaFullness <= 0 || Game.player.mommaClean <= 0 || Game.mom.hp <= 0 || GameTime.value > (DAY_LENGTH * 30)) {
                Dialogue.current = Dialogue.repo.get(999);
            }

        }
        if(Game.player.good){
            var d = new Dialogue();
            d.message = "Шкипер решил, что с него хватит. \n Он знает, что сделал все что мог. \n Он нашёл надежных людей, которые будут ухаживать за матерью. \n А большего он сделать не может. \n Да и будто бы не должен. \n Это его жизнь, и он должен прожить ее для себя. \n Пора браться за ум, остепениться... \n А тут и девочка хорошая. \n Игра завершилась хорошей (но не каноничной) концовкой.";
            Dialogue.current = d;

        }
    }
}
