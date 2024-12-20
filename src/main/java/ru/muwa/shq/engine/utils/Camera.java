package ru.muwa.shq.engine.utils;
import ru.muwa.shq.engine.Game;
import ru.muwa.shq.engine.GameWindow;

public class Camera {
    public static int x = 0, y = 0;
    public static final double FOLLOW_SPEED = 0.05; // Чем меньше значение, тем медленнее камера будет следовать за игроком
    public static final double LOOK_AHEAD_DISTANCE = 200.0; // Расстояние, на которое камера заглядывает вперед
    public static final double CURSOR_THRESHOLD_DISTANCE = 200.0; // Пороговое расстояние для проверки курсора

    enum Kink {LEFT, RIGHT, UP, DOWN}
    public static Kink kink;

    public static void update() {
        int targetX = (int) Game.player.hitBox.getCenterX() - (GameWindow.WIDTH / 2);
        int targetY = (int) Game.player.hitBox.getCenterY() - (GameWindow.HEIGHT / 2);

        // Вычисление расстояния от игрока до курсора
        double cursorX = Input.mouse.x + Camera.x;
        double cursorY = Input.mouse.y + Camera.y;
        double distanceToCursor = Math.sqrt(Math.pow(cursorX - Game.player.hitBox.getCenterX(), 2) +
                Math.pow(cursorY - Game.player.hitBox.getCenterY(), 2));


        // Учет угла, под которым смотрит игрок
        if (distanceToCursor > CURSOR_THRESHOLD_DISTANCE) {
            double angle = Combat.angle; // Предполагается, что angle в радианах
            targetX += (int) (Math.cos(angle) * LOOK_AHEAD_DISTANCE);
            targetY += (int) (Math.sin(angle) * LOOK_AHEAD_DISTANCE);
        }

        // Интерполяция позиции камеры к цели
        x += (int) ((targetX - x) * FOLLOW_SPEED);
        y += (int) ((targetY - y) * FOLLOW_SPEED);

        // Эффект "сумасшествия"
        if (Game.player.crazy > 50 && Game.player.crazy < 75) {
            y += (Math.random() > 0.5 ? 1 : -1);
            x += (Math.random() > 0.5 ? 1 : -1);
        }
        if (Game.player.crazy > 75) {
            y += (Math.random() > 0.5 ? 2 : -2);
            x += (Math.random() > 0.5 ? 2 : -2);
        }
    }

    public static void work() {
        if (kink == null) {
            update();
        } else {
            // Если у Шкипера вертолеты
            switch (kink) {
                case LEFT -> Camera.x -= 1;
                case RIGHT -> Camera.x += 1;
                case UP -> Camera.y -= 1;
                case DOWN -> Camera.y += 1;
            }
        }
    }
}
