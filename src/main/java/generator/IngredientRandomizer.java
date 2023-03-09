package generator;

import pojo.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IngredientRandomizer {

    private static final Random random = new Random();


    public static String getIngredient(List<Data> ingredients) {
        return ingredients.get(random.nextInt(ingredients.size() - 1)).getId();
    }

    public static List<String> getIngredients(List<Data> ingredients, int numberOfIngredients) {
        List<String> selectedIngredients = new ArrayList<String>();
        for (int i = 0; i < numberOfIngredients; i++) {

            selectedIngredients.add(ingredients.get(random.nextInt(ingredients.size() - 1)).getId());
        }
        return selectedIngredients;
    }

}
