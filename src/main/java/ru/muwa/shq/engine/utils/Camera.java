package ru.muwa.shq.engine.utils;
import ru.muwa.shq.engine.Game;
import ru.muwa.shq.engine.GameWindow;
public class Camera {
    public static int x = 0, y = 0;
    enum Kink {LEFT, RIGHT, UP, DOWN}
    public static Kink kink;
    /**  **/
    public static void update(){
        x = (int) Game.player.hitBox.getCenterX() - (GameWindow.WIDTH/2);
        y = (int) Game.player.hitBox.getCenterY() - (GameWindow.HEIGHT/2);
    }
    /** Работа службы камеры **/
    public static void work() {
            if(kink == null) update();
                else
                //Если у Шкипера вертолеты
                switch (kink) {
                    case LEFT -> Camera.x -= 1;
                    case RIGHT -> Camera.x += 1;
                    case UP -> Camera.y -= 1;
                    case DOWN -> Camera.y += 1;
                }
    }
}
