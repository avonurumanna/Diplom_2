import client.OrderClient;
import client.UserClient;
import generator.IngredientRandomizer;
import generator.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.Data;
import pojo.Order;
import pojo.User;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderTest {

    List<Data> ingredients;
    private User user;
    private UserClient userClient;
    private Order order;
    private OrderClient orderClient;
    private String token;

    @Before
    public void setUp() {

        orderClient = new OrderClient();
        ingredients = orderClient.getIngredients().getData();
        user = UserGenerator.getValidUser();
        userClient = new UserClient();
        token = null;
    }

    @Test
    @DisplayName("Check that order can not be created by unauthorized user")
    public void orderCanNotBeCreatedByUnauthorizedUser() {
        order = new Order(IngredientRandomizer.getIngredients(ingredients, 5));
        ValidatableResponse createOrderResponse = orderClient.create(order, token);
        createOrderResponse.statusCode(SC_UNAUTHORIZED)
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .assertThat().body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Check create order")
    public void orderCanBeCreated() {
        order = new Order(IngredientRandomizer.getIngredients(ingredients, 5));
        ValidatableResponse createUserResponse = userClient.create(user);
        token = createUserResponse.extract().path("accessToken");
        ValidatableResponse createOrderResponse = orderClient.create(order, token);
        createOrderResponse.statusCode(SC_OK)
                .and()
                .assertThat().body("name", notNullValue())
                .and()
                .assertThat().body("order", notNullValue())
                .and()
                .assertThat().body("success", equalTo(true));

    }

    @Test
    @DisplayName("Check that order can not be created without ingredients")
    public void orderCanNotBeCreatedWithoutIngredients() {
        order = new Order(IngredientRandomizer.getIngredients(ingredients, 0));
        ValidatableResponse createUserResponse = userClient.create(user);
        token = createUserResponse.extract().path("accessToken");
        ValidatableResponse createOrderResponse = orderClient.create(order, token);
        createOrderResponse.statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .assertThat().body("message", equalTo("Ingredient ids must be provided"));

    }


    @Test
    @DisplayName("Check that order can not be created with incorrect hash of ingredient")
    public void orderCanNotBeCreatedWithIncorrectHashOfIngredient() {
        order = new Order(new ArrayList<>());
        order.getIngredients().add("123");
        ValidatableResponse createUserResponse = userClient.create(user);
        token = createUserResponse.extract().path("accessToken");
        ValidatableResponse createOrderResponse = orderClient.create(order, token);
        createOrderResponse.statusCode(SC_INTERNAL_SERVER_ERROR);
    }


    @After
    public void cleanUp() {
        if (token != null) {
            userClient.delete(token);
        }
    }


}
