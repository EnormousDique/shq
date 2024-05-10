package ru.muwa.shq.engine;

import ru.muwa.shq.Main;
import ru.muwa.shq.engine.utils.*;
import ru.muwa.shq.entities.Player;

import static ru.muwa.shq.engine.Game.pause;

public class Updater implements Runnable{

    Thread thread;
    Updater(){
        thread = new Thread(this);
    }

    @Override
    public void run() {
        System.out.println("поток обновлени€ запущен");

        double drawInterval = (double) 1_000_000_000 / Game.fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currTime;

        while (thread != null) {

            currTime = System.nanoTime();
            delta += (currTime - lastTime) / drawInterval;
            lastTime = currTime;
            if (delta >= 1) {
                work();
                delta-=1;
            }else if (delta > 0) {
                long remainingTime = (long) ((1 - delta) * drawInterval / 1_000_000); // переводим в миллисекунды
                try {
                    Thread.sleep(remainingTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void work() {

        try {
            // од, выполн€емый несмотр€ на паузу
            //TODO: добавить отдельно вызов обработки команд консоли

            if (pause) return;
            // од, замирающий при паузе
            Momma.work();
            Camera.work();
            Effect.work();
            Input.work();
            GameTime.work();
            Physx.work();
            Combat.work();
            AI.work();
            Spawner.work();
            Kldmn.mdk();
        }catch (Exception e){}
    }

}
