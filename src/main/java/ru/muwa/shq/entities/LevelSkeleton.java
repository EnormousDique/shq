package ru.muwa.shq.entities;

import ru.muwa.shq.engine.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelSkeleton {
    public int levelId;
    public boolean isIndoors, isStreet;
    public List<GameObjectPlacement> objectPlacements = new ArrayList<>();
    public static class GameObjectPlacement{
        public int id, x, y;
        public Map<Integer,Integer> items; //key - id; value - count;
        public Coordinates coordinates;

        public GameObjectPlacement(){}
        public GameObjectPlacement(int id,int x,int y, Map<Integer,Integer> items, Coordinates coordinates)
        {
            this.id = id; this.x = x; this.y = y; this.items = items; this.coordinates = coordinates;
        }
    }

    public LevelSkeleton(){}
    public LevelSkeleton(Level level)
    {
        levelId = level.id;
        isIndoors = level.isIndoors;
        isStreet = level.isStreet;

        Map<Integer,Integer> itemMap = new HashMap<>();

        for (int i = 0; i < level.objects.size(); i++) {

            GameObject object = level.objects.get(i);

            if(object.equals(Game.player)) continue;

            itemMap = new HashMap<>();
            if(object.items!= null && !object.items.isEmpty()) for(Item item : object.items) itemMap.put(item.id,item.count);

            Coordinates coordinates = null;

            if(object.coordinates != null) coordinates = object.coordinates;

            objectPlacements.add(new GameObjectPlacement(object.id,object.x,object.y, itemMap,coordinates ));

        }
    }
}
