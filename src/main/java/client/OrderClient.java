package client;

import io.restassured.response.ValidatableResponse;
import pojo.Ingredients;
import pojo.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {
    public static final String ORDER_PATH = "api/orders";
    public static final String INGREDIENTS_GET_PATH = "api/ingredients";

    public ValidatableResponse create(Order order, String token) {
        if (token != null)
            return given()
                    .spec(getSpec())
                    .header("Authorization", token)
                    .body(order)
                    .when()
                    .post(ORDER_PATH)
                    .then();
        else
            return given()
                    .spec(getSpec())
                    .body(order)
                    .when()
                    .post(ORDER_PATH)
                    .then();

    }

    public ValidatableResponse getOrders(String token) {
        if (token != null)
            return given()
                    .spec(getSpec())
                    .header("Authorization", token)
                    .when()
                    .get(ORDER_PATH)
                    .then();
        else
            return given()
                    .spec(getSpec())
                    .when()
                    .get(ORDER_PATH)
                    .then();
    }

    public Ingredients getIngredients() {
        return given()
                .spec(getSpec())
                .when()
                .get(INGREDIENTS_GET_PATH)
                .body().as(Ingredients.class);
    }


}
