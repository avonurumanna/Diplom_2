package pojo;

import java.util.ArrayList;
import java.util.List;

public class Order {
    List<String> ingredients;


    public Order() {
    }

    public Order(List<String> values) {
        ingredients = values;
    }

    public Order(String value) {
        ingredients = new ArrayList<String>();
        ingredients.add(value);
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

}
