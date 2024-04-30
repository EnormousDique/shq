package ru.muwa.shq.engine.utils;

import ru.muwa.shq.engine.Game;
import ru.muwa.shq.entities.GameObject;
import ru.muwa.shq.entities.Level;
import ru.muwa.shq.entities.Minigame;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static ru.muwa.shq.engine.utils.GameTime.DAY_LENGTH;
import static ru.muwa.shq.entities.Minigame.Type.SHQUR;

public class Kldmn {
    private static final Map<GameObject,Long> shqUpdateMap = new HashMap<>();
    private static final long updatePeriod =  DAY_LENGTH * 3 ;
    public static void mdk()
    {
        fillShqUpdateMap();
        updateShqMinigames();
    }

    private static void updateShqMinigames() {
        //проходимся по мапе, если наступило время, обнуляем миниигру
        for (Map.Entry<GameObject,Long> entry : shqUpdateMap.entrySet())
        {
            if(entry.getValue() < GameTime.value - updatePeriod) {
                GameObject object = entry.getKey();
                object.minigame = null;
                shqUpdateMap.put(object,GameTime.value);
            }
        }
    }

    private static void fillShqUpdateMap() {
        //Проходимся по всем уровням подъездов и собираем в мапу объекты шукра, в которых проинициирована миниигра
        for(Map.Entry<Integer, Level> entry : Level.repo.entrySet())
        {
            Level level = entry.getValue();
            if(level.isIndoors)
            {
                for (int i = 0; i < level.objects.size(); i++) {
                    GameObject object = level.objects.get(i);

                    if(object.minigame != null && object.minigame.type == SHQUR && !shqUpdateMap.containsKey(object))
                    {
                        shqUpdateMap.put(object,GameTime.value);
                    }
                }
            }
        }
    }
}
