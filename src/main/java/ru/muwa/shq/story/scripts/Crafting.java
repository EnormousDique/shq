package ru.muwa.shq.story.scripts;

import ru.muwa.shq.entities.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Crafting {
    // Ключ - результат крафта. Значение - список необходимых ингредиентов
    public static HashMap<Integer, List<Integer>> recipes = new HashMap<>();
    static {
        recipes.put(50,List.of(33,37,42)); //Паста помодорро
        recipes.put(57,List.of(37,54,55));// Салат
        recipes.put(58, List.of(53,39));//Жареная курица
        recipes.put(59, List.of(52,39));//Говяжьи стейки
        recipes.put(60, List.of(51,39,55,43));//Котлеты
        recipes.put(61, List.of(53,55,56,31));//Куриный Суп
        recipes.put(62, List.of(31,39,55));//Картошечка с лучком
        recipes.put(63, List.of(38,45,41,34));//Сырники
        recipes.put(64, List.of(52,31,55,32,37,40,56));//Борщ


    }
    public static int craft(List<Item> items)
    {
        for (Map.Entry<Integer, List<Integer>> entry : recipes.entrySet()) {
            int resultItemId = entry.getKey();
            List<Integer> ingredientIds = entry.getValue();
            // Проверка, что набор предметов соответствует текущему рецепту
            if (isMatchingRecipe(items, ingredientIds)) {
                return resultItemId;
            }
        }
        // Ни один рецепт не соответствует набору предметов
        return -1;
    }
    private static boolean isMatchingRecipe(List<Item> items, List<Integer> ingredientIds) {
        // Создаем копию списка предметов для верстака
        List<Integer> craftingTableItemIds = items.stream().map(i->i.id).collect(Collectors.toList());
        // Проверка, что в верстаке есть все необходимые ингредиенты для рецепта
        for (Integer ingredientId : ingredientIds) {
            if (!craftingTableItemIds.contains(ingredientId)) {
                return false;
            }
            // Удаляем использованный ингредиент из копии списка предметов
            craftingTableItemIds.remove(ingredientId);
        }
        return true;
    }
}
