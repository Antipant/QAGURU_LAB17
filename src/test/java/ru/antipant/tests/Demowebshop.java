package ru.antipant.tests;

import com.codeborne.selenide.WebDriverRunner;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import ru.antipant.config.Project;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static ru.antipant.helpers.AllureRestAssuredFilter.withCustomTemplates;

public class Demowebshop extends TestBase {

    static String userLogin = Project.config.userLogin(),
            userPassword = Project.config.userPassword(),
            authCookieName = "NOPCOMMERCE.AUTH",
            updateWish;

    @DisplayName("Проверка корректности отображения успешной авторизации")
    @Test
    void loginTest() {

        step("Получить куки авторизации из API метода и добавить их в браузер", () -> {
            String authCookieValue =
                    given()
                            .filter(withCustomTemplates())
                            .contentType("application/x-www-form-urlencoded")
                            .formParam("Email", userLogin)
                            .formParam("Password", userPassword)
                            .log().all()
                            .when()
                            .post("/login")
                            .then().log().all()
                            .statusCode(302)
                            .extract().cookie(authCookieName);
            step("Открыть легковесную страницу сайта", () ->
                    open("/Themes/DefaultClean/Content/images/logo.png"));

            step("Добавить куки в открытый браузер(с легковесной страницей)", () -> {
                Cookie authCookie = new Cookie(authCookieName, authCookieValue);
                WebDriverRunner.getWebDriver().manage().addCookie(authCookie);
            });
        });
        step("Открыть главную страницу сайта", () ->
                open(""));
        step("Проверка успешной авторизации по логину в личном кабинете", () -> {
            $(".account").shouldHave(text(userLogin));
        });
    }

    @DisplayName("Проверка корректности отображения вишлиста")
    @Test
    void addWish() {
        step("Получить куки авторизации из API метода", () -> {
            String authCookieValue =
                    given()
                            .filter(withCustomTemplates())
                            .contentType("application/x-www-form-urlencoded")
                            .formParam("Email", userLogin)
                            .formParam("Password", userPassword)
                            .log().all()
                            .when()
                            .post("/login")
                            .then().log().all()
                            .statusCode(302)
                            .extract().cookie(authCookieName);

            step("Добавить товар в вишлист (Апи метод)", () -> {
                updateWish = given()
                        .filter(withCustomTemplates())
                        .contentType("application/x-www-form-urlencoded")
                        .cookie(authCookieName, authCookieValue)
                        .log().all()
                        .when()
                        .post("/addproducttocart/details/66/2")
                        .then().log().all()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .extract().jsonPath().get("updatetopwishlistsectionhtml");
            });

            step("Открыть легковесную страницу сайта", () ->
                    open("/Themes/DefaultClean/Content/images/logo.png"));

            step("Добавить куки в открытый браузер(с легковесной страницей)", () -> {
                Cookie authCookie = new Cookie(authCookieName, authCookieValue);
                WebDriverRunner.getWebDriver().manage().addCookie(authCookie);
            });
        });
        step("Открыть страницу вишлист", () ->
                open("/wishlist"));
        step("Проверка вишлиста", () -> {
            $("span.wishlist-qty").shouldHave(text(updateWish));
        });
    }
}
