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

import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrdersTest {


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
        order = new Order(IngredientRandomizer.getIngredients(ingredients, 5));
    }

    @Test
    @DisplayName("Check get orders by authorized user")
    public void getOrdersByAuthorizedUser() {
        ValidatableResponse userCreateResponse = userClient.create(user);
        token = userCreateResponse.extract().path("accessToken");
        ValidatableResponse createOrderResponse = orderClient.create(order, token);
        ValidatableResponse getOrdersResponse = orderClient.getOrders(token);
        getOrdersResponse.statusCode(SC_OK)
                .and()
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("orders", notNullValue())
                .and()
                .assertThat().body("total", notNullValue())
                .and()
                .assertThat().body("totalToday", notNullValue());

    }

    @Test
    @DisplayName("Check that order can not be get by unauthorized user")
    public void ordersCanNotBeGetByUnauthorizedUser() {
        ValidatableResponse getOrdersResponse = orderClient.getOrders(token);
        getOrdersResponse.statusCode(SC_UNAUTHORIZED)
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .assertThat().body("message", equalTo("You should be authorised"));
    }

    @After
    public void cleanUp() {
        if (token != null) {
            userClient.delete(token);
        }
    }


}
