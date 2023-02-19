import client.UserClient;
import generator.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.User;
import pojo.UserCredentials;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginUserTest {
    private User user;
    private UserClient userClient;
    private String token;

    @Before
    public void setUp() {
        user = UserGenerator.getValidUser();
        userClient = new UserClient();
        token = null;
    }

    @Test
    @DisplayName("Check that user can login")
    public void loginWithValidCredentials() {
        ValidatableResponse createUserResponse = userClient.create(user);
        token = createUserResponse.extract().path("accessToken");
        ValidatableResponse loginUserResponse = userClient.login(UserCredentials.from(user));
        loginUserResponse.statusCode(SC_OK)
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
    @DisplayName("Check that user can not login with invalid credentials")
    public void loginWithInvalidCredentials() {
        ValidatableResponse loginUserResponse = userClient.login(UserCredentials.from(user));
        loginUserResponse.statusCode(SC_UNAUTHORIZED)
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .assertThat().body("message", equalTo("email or password are incorrect"));
    }

    @After
    public void cleanUp() {
        if (token != null) {
            userClient.delete(token);
        }
    }
}
