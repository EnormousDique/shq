package ru.muwa.shq.engine.utils;

import ru.muwa.shq.engine.Game;
import ru.muwa.shq.engine.GameWindow;

public class Camera {

    public static int x = 0, y = 0;

    enum Kink {LEFT, RIGHT, UP, DOWN}

    static Kink kink;

    public static void update(){
        x = (int) Game.player.hitBox.getCenterX() - (GameWindow.WIDTH/2);
        y = (int) Game.player.hitBox.getCenterY() - (GameWindow.HEIGHT/2);

    }


    /** Работа службы камеры **/
    public static void work() {

        if(Game.player.drunk>0){//Если Шкипер пьян
            //Камера может уплывать
            double r = Math.random();
            if(kink == null && r<0.251) kink = Kink.LEFT;
            if(kink == null && r<0.51) kink = Kink.RIGHT;
            if(kink == null && r<0.751) kink = Kink.LEFT;
            if(kink == null && r<1) kink = Kink.DOWN;
            switch (kink)
            {
                case LEFT: Camera.x -=1;
                case RIGHT: Camera.x+=1;
                case UP: Camera.y -=1;
                case DOWN: Camera.y+=1;
            }
            //Камера зависать с шансом равным % опьянения
            if(Math.random() * 100 - Game.player.drunk > Game.player.drunk) if(Math.random() * 100 - Game.player.drunk > Game.player.drunk) {
                update();
                kink = null;
            }
        }else update(); //иначе камера обновляется стандартно
    }
}
