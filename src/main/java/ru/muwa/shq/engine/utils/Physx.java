package ru.muwa.shq.engine.utils;

import ru.muwa.shq.engine.Game;
import ru.muwa.shq.entities.GameObject;
import ru.muwa.shq.entities.Player;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.HashMap;

import static ru.muwa.shq.entities.GameObject.Type.BUILDING;
import static ru.muwa.shq.entities.GameObject.Type.CREATURE;

public class Physx {

    public static boolean playerCollisions = true;
    public static void work()
    {
        updateHitBox();
        collisions();
        carsCollisions();
    }

    private static void carsCollisions() {
        var cars = Game.currentLevel.objects.stream()
                .filter(o->(o.name.contains("car") && o.solid)).toList();

        for (int i = 0; i < cars.size(); i++) {
            var car = cars.get(i);
            for (int j = 0; j < cars.size(); j++) {
                var car2 = cars.get(j);
                if(car.hitBox.intersects(car2.hitBox) && car!=car2) {
                   car2.speed = car.speed + 2;
                }
            }

        }
    }

    private static void collisions() {
        if(playerCollisions) playerCollisions();
    }

    private static void updateHitBox() {

        for (int i = 0; i < Game.currentLevel.objects.size(); i++) {

            GameObject o = Game.currentLevel.objects.get(i);

            o.hitBox.x = o.x + o.hitBoxXOffset; o.hitBox.y = o.y + o.hitBoxYOffset;

        }
    }

    public static void NPCCollisions(GameObject npc)
    {

        for (int i = 0; i < Game.currentLevel.objects.size(); i++) {

            GameObject object = Game.currentLevel.objects.get(i);

            if(!object.solid) continue;

            if(object.hitBox.intersects(npc.hitBox))
            {
                if(object == npc) continue;
                if(!npc.solid) continue;
                if(object.type == CREATURE || object == Game.player && !npc.enemy) AI.setRandomDestination(npc);

                // Определите горизонтальное и вертикальное перекрытие между объектами.
                double overlapX = 0;
                double overlapY = 0;

                if (npc.hitBox.getMaxX() > object.hitBox.getMinX() && npc.hitBox.getMinX() < object.hitBox.getMaxX()) {
                    // Столкновение по горизонтали
                    if (npc.hitBox.getCenterX() < object.hitBox.getCenterX()) {
                        // Столкновение справа
                        overlapX = npc.hitBox.getMaxX() - object.hitBox.getMinX();
                    } else {
                        // Столкновение слева
                        overlapX = object.hitBox.getMaxX() - npc.hitBox.getMinX();
                    }
                }

                if (npc.hitBox.getMaxY() > object.hitBox.getMinY() && npc.hitBox.getMinY() < object.hitBox.getMaxY()) {
                    // Столкновение по вертикали
                    if (npc.hitBox.getCenterY() < object.hitBox.getCenterY()) {
                        // Столкновение снизу
                        overlapY = npc.hitBox.getMaxY() - object.hitBox.getMinY();
                    } else {
                        // Столкновение сверху
                        overlapY = object.hitBox.getMaxY() - npc.hitBox.getMinY();
                    }
                }

                if (overlapX < overlapY) {
                    // Горизонтальное столкновение
                    if (npc.hitBox.getCenterX() < object.hitBox.getCenterX()) {
                        // Столкновение справа
                        npc.x = (int) (npc.x - overlapX);
                    } else {
                        // Столкновение слева
                        npc.x = (int) (npc.x + overlapX);
                    }
                } else {
                    // Вертикальное столкновение
                    if (npc.hitBox.getCenterY() < object.hitBox.getCenterY()) {
                        // Столкновение снизу
                        npc.y = (int) (npc.y - overlapY);
                    } else {
                        // Столкновение сверху
                        npc.y = (int) (npc.y + overlapY);
                    }
                }
            }

        }

    }

    private static void playerCollisions()
    {
        for (int i = 0; i < Game.currentLevel.objects.size(); i++) {

            GameObject object = Game.currentLevel.objects.get(i);
            Player player = Game.player;

            if (object.type == CREATURE) continue;

            if(object.solid && object.hitBox.intersects(player.hitBox))
            {
                // Определите горизонтальное и вертикальное перекрытие между объектами.
                double overlapX = 0;
                double overlapY = 0;

                if (player.hitBox.getMaxX() > object.hitBox.getMinX() && player.hitBox.getMinX() < object.hitBox.getMaxX()) {
                    // Столкновение по горизонтали
                    if (player.hitBox.getCenterX() < object.hitBox.getCenterX()) {
                        // Столкновение справа
                        overlapX = player.hitBox.getMaxX() - object.hitBox.getMinX();
                    } else {
                        // Столкновение слева
                        overlapX = object.hitBox.getMaxX() - player.hitBox.getMinX();
                    }
                }

                if (player.hitBox.getMaxY() > object.hitBox.getMinY() && player.hitBox.getMinY() < object.hitBox.getMaxY()) {
                    // Столкновение по вертикали
                    if (player.hitBox.getCenterY() < object.hitBox.getCenterY()) {
                        // Столкновение снизу
                        overlapY = player.hitBox.getMaxY() - object.hitBox.getMinY();
                    } else {
                        // Столкновение сверху
                        overlapY = object.hitBox.getMaxY() - player.hitBox.getMinY();
                    }
                }

                if (overlapX < overlapY) {
                    // Горизонтальное столкновение
                    if (player.hitBox.getCenterX() < object.hitBox.getCenterX()) {
                        // Столкновение справа
                        player.x = (int) (player.x - overlapX);
                    } else {
                        // Столкновение слева
                        player.x = (int) (player.x + overlapX);
                    }
                } else {
                    // Вертикальное столкновение
                    if (player.hitBox.getCenterY() < object.hitBox.getCenterY()) {
                        // Столкновение снизу
                        player.y = (int) (player.y - overlapY);
                    } else {
                        // Столкновение сверху
                        player.y = (int) (player.y + overlapY);
                    }
                }
            }

        }
    }
    private static Rectangle rotateHitBox(Rectangle hitBox){



        // Угол поворота в радианах
        double angle = Math.toRadians(Combat.angle);

        // Создаем объект AffineTransform и поворачиваем его
        AffineTransform transform = AffineTransform.getRotateInstance(angle, hitBox.getX() + hitBox.getWidth() / 2, hitBox.getY() + hitBox.getHeight() / 2);
        transform.rotate(angle, hitBox.getX() + hitBox.getWidth() / 2, hitBox.getY() + hitBox.getHeight() / 2);

        // Применяем трансформацию к вершинам прямоугольника
        double[] verticesX = {hitBox.x, hitBox.x + hitBox.width, hitBox.x + hitBox.width, hitBox.x};
        double[] verticesY = {hitBox.y, hitBox.y, hitBox.y + hitBox.height, hitBox.y + hitBox.height};

        transform.transform(verticesX, 0, verticesX, 0, 2);
        transform.transform(verticesY, 0, verticesY, 0, 2);

        // Находим новые координаты, ширину и высоту повернутого прямоугольника
        double minX = verticesX[0];
        double minY = verticesY[0];
        double maxX = verticesX[0];
        double maxY = verticesY[0];

        for (int i = 1; i < 4; i++) {
            minX = Math.min(minX, verticesX[i]);
            minY = Math.min(minY, verticesY[i]);
            maxX = Math.max(maxX, verticesX[i]);
            maxY = Math.max(maxY, verticesY[i]);
        }

        double rotatedX =  minX;
        double rotatedY =  minY;
        double rotatedWidth = (maxX - minX);
        double rotatedHeight =  (maxY - minY);

        // Создаем новый прямоугольник с повернутыми координатами, шириной и высотой
        Rectangle newHitBox = new Rectangle();

        return  new Rectangle();
    }
}
