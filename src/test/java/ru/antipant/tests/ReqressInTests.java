package ru.antipant.tests;

import org.junit.jupiter.api.Test;
import ru.antipant.models.AuthRequest;
import ru.antipant.models.AuthResponse;
import ru.antipant.models.UserRequest;
import ru.antipant.models.UserResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static ru.antipant.specs.Specs.request;
import static ru.antipant.specs.Specs.responseSpec;

public class ReqressInTests {

    String loginUrl = "/api/login",
            usersUrl = "/api/users",
            users2Url = "/api/users/2",
            listUsers = "/api/users?page=2",
            name = "morpheus",
            job1 = "leader",
            job2 = "zion resident",
            email = "eve.holt@reqres.in",
            password = "cityslicka",
            token = "QpwL5tke4Pnpja7X4",
            error = "Missing password";

    public ReqressInTests() {
    }

    @Test
    void loginTest() {

        AuthRequest authRequest = new AuthRequest(email, password);
        AuthResponse authResponse = given()
                .spec(request)
                .body(authRequest)
                .when()
                .post(loginUrl)
                .then()
                .spec(responseSpec)
                .extract().as(AuthResponse.class);
        org.assertj.core.api.Assertions.assertThat(authResponse.getToken()).contains(token);
    }

    @Test
    void missingPasswordTest() {

        AuthRequest authRequest = new AuthRequest(email);
        AuthResponse authResponse = given()
                .spec(request)
                .body(authRequest)
                .when()
                .post(loginUrl)
                .then()
                .statusCode(400)
                .extract().as(AuthResponse.class);
        org.assertj.core.api.Assertions.assertThat(authResponse.getError()).contains(error);
    }

    @Test
    void createTest() {

        UserRequest userRequest = new UserRequest(name, job1);
        UserResponse userResponse = given()
                .spec(request)
                .body(userRequest)
                .when()
                .post(usersUrl)
                .then()
                .statusCode(201)
                .extract().as(UserResponse.class);
        org.assertj.core.api.Assertions.assertThat(userResponse.getJob()).contains(userRequest.getJob());
        org.assertj.core.api.Assertions.assertThat(userResponse.getName()).contains(userRequest.getName());

    }

    @Test
    void updateTest() {

        UserRequest userRequest = new UserRequest(name, job2);
        UserResponse userResponse = given()
                .spec(request)
                .body(userRequest)
                .when()
                .put(users2Url)
                .then()
                .spec(responseSpec)
                .extract().as(UserResponse.class);
        org.assertj.core.api.Assertions.assertThat(userResponse.getJob()).contains(userRequest.getJob());
        org.assertj.core.api.Assertions.assertThat(userResponse.getName()).contains(userRequest.getName());

    }

    @Test
    void deleteTest() {

        given()
                .spec(request)
                .when()
                .delete(users2Url)
                .then()
                .statusCode(204);
    }

    @Test
    public void checkEmailAndFirstNameUsingGroovy() {
        given()
                .spec(request)
                .when()
                .get(listUsers)
                .then()
                .log().body()
                .body("data.findAll{it.email =~/.*?@reqres.in/}.email.flatten()",
                        hasItem("michael.lawson@reqres.in"))
                .body("data.findAll{it.first_name}.first_name.flatten()",
                        hasItem("Lindsay"));
    }
}