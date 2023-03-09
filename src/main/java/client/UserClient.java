package client;

import io.restassured.response.ValidatableResponse;
import pojo.User;
import pojo.UserCredentials;

import static io.restassured.RestAssured.given;

public class UserClient extends Client {

    public static final String USER_CREATE_PATH = "api/auth/register";
    public static final String USER_PATH = "api/auth/user";
    public static final String USER_LOGIN_PATH = "api/auth/login";


    public ValidatableResponse create(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(USER_CREATE_PATH)
                .then();
    }

    public ValidatableResponse delete(String token) {
        return given()
                .spec(getSpec())
                .header("Authorization", token)
                .when()
                .delete(USER_PATH)
                .then();
    }

    public ValidatableResponse login(UserCredentials userCredentials) {
        return given()
                .spec(getSpec())
                .body(userCredentials)
                .when()
                .post(USER_LOGIN_PATH)
                .then();
    }

    public ValidatableResponse edit(String token, String parameterName, String value) {
        if (token != null)
            return given()
                    .spec(getSpec())
                    .header("Authorization", token)
                    .body(String.format("{\"%s\": \"%s\"}", parameterName, value))
                    .when()
                    .patch(USER_PATH)
                    .then();
        else return given()
                .spec(getSpec())
                .body(String.format("{\"%s\": \"%s\"}", parameterName, value))
                .when()
                .patch(USER_PATH)
                .then();
    }


}
