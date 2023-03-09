import client.UserClient;
import generator.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pojo.User;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class ParameterizedEditUserTest {
    private final User user;
    private final String parameterName;
    private final String value;
    private UserClient userClient;
    private String token;


    public ParameterizedEditUserTest(User user, String parameterName, String value) {
        this.user = user;
        this.parameterName = parameterName;
        this.value = value;
    }

    @Parameterized.Parameters(name = "change {1} to {2}")
    public static Object[] getUserData() {
        return new Object[][]{
                {UserGenerator.getValidUser(), "email", "test123@test.ru"},
                {UserGenerator.getValidUser(), "password", "12345"},
                {UserGenerator.getValidUser(), "name", "jane"},
        };
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
        token = null;
    }

    @Test
    @DisplayName("Check that authorized user can be updated")
    public void authorizedUserCanBeUpdatedTest() {
        ValidatableResponse createUserResponse = userClient.create(user);
        token = createUserResponse.extract().path("accessToken");
        ValidatableResponse editUserResponse = userClient.edit(token, parameterName, value);
        editUserResponse.statusCode(SC_OK)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Check that unauthorized user can not be updated")
    public void unauthorizedUserCanNotBeUpdatedTest() {
        ValidatableResponse createUserResponse = userClient.create(user);
        ValidatableResponse editUserResponse = userClient.edit(token, parameterName, value);
        editUserResponse.statusCode(SC_UNAUTHORIZED)
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
