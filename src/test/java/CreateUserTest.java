import client.UserClient;
import generator.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.User;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateUserTest {

    private User user;
    private UserClient userClient;
    private String token;

    @Before
    public void setUp() {

        userClient = new UserClient();
        token = null;
    }

    @Test
    @DisplayName("Check create user")
    public void userCanBeCreatedTest() {
        user = UserGenerator.getValidUser();
        ValidatableResponse createUserResponse = userClient.create(user);
        token = createUserResponse.extract().path("accessToken");
        createUserResponse.statusCode(SC_OK)
                .and()
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("user.email", equalTo(user.getEmail()))
                .and()
                .assertThat().body("user.name", equalTo(user.getName()))
                .and()
                .assertThat().body("accessToken", notNullValue())
                .and()
                .assertThat().body("refreshToken", notNullValue());


    }

    @Test
    @DisplayName("Check that the same user can not be created twice with the same input values")
    public void userCanNotBeCreatedTwiceWithTheSameInputValuesTest() {
        user = UserGenerator.getValidUser();
        ValidatableResponse firstCreateUserResponse = userClient.create(user);
        token = firstCreateUserResponse.extract().path("accessToken");
        ValidatableResponse secondCreateUserResponse = userClient.create(user);
        secondCreateUserResponse.statusCode(SC_FORBIDDEN)
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .assertThat().body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Check that user can not be created without sending email")
    public void userCanNotBeCreatedWithoutEmailTest() {
        user = UserGenerator.getUserWithoutEmail();
        ValidatableResponse createUserResponse = userClient.create(user);
        createUserResponse.statusCode(SC_FORBIDDEN)
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .assertThat().body("message", equalTo("Email, password and name are required fields"));

    }

    @After
    public void cleanUp() {
        if (token != null) {
            userClient.delete(token);
        }
    }

}
