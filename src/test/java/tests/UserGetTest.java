package tests;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


@Epic("Get cases")
@Feature("Get information")
public class UserGetTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    String cookie;
    String header;
    int userIdOnAuth;

    @Test
    @Description("Try to get data without authorization")
    @DisplayName("Get data without authorization")
    @Severity(value = SeverityLevel.CRITICAL)
    public void testGetDataNotAuth() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

        response.prettyPrint();

        Assertions.assertJsonHasNoFields(response, "userName");
        Assertions.assertJsonHasNoFields(response, "firstName");
        Assertions.assertJsonHasNoFields(response, "lastName");
        Assertions.assertJsonHasNoFields(response, "email");
    }

    @Test
    @Description("Get data")
    @DisplayName("Positive case for get data")
    @Severity(value = SeverityLevel.CRITICAL)
    public void testGetUserDetailsAuthAsSameUser() {

        Map<String, String> userData = new HashMap<>();
        userData.put("email", "vinkotov@example.com");
        userData.put("password", "1234");

        Response response = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        String header = this.getHeader(response, "x-csrf-token");
        String cookie = this.getCookie(response, "auth_sid");


        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

        responseUserData.prettyPrint();

        responseUserData.prettyPrint();
        String[] expectedFilds = {"username", "firstName", "lastName", "email"};
        Assertions.assertJsonHasFields(responseUserData, expectedFilds);
    }



    // нужно написать тест, который авторизовывается одним пользователем,
    // но получает данные другого (т.е. с другим ID).
    // И убедиться, что в этом случае запрос также получает только username,
    // так как мы не должны видеть остальные данные чужого пользователя.
    @Test
    @Description("Try to get data with authorization, and fetch data from another user")
    @DisplayName("Authorization And FetchData Of Another User")
    @Severity(value = SeverityLevel.CRITICAL)
    public void testAuthorizationAndFetchDataOfAnotherUser() {

        Map<String,String> authData = new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password","1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login",authData);
        this.cookie = this.getCookie(responseGetAuth,"auth_sid");
        this.header = this.getHeader(responseGetAuth,"x-csrf-token");

        responseGetAuth.prettyPrint();

        // Step 2: Get data of another user with data of first user
        Response userDataResponse = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/1",this.header, this.cookie);
        userDataResponse.prettyPrint();

        // Step 3: Verify that only username is returned
        Assertions.assertJsonByName(userDataResponse,"username","Lana");
    }
}//end class
