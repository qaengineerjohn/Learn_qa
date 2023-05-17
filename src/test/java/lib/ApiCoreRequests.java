package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {

    @Step("Make a Get-request with token and auth cookie")
    public Response makeGetRequest(String url, String token, String cookie){

        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token",token))
                .cookie("auth_sid",cookie)
                .get(url)
                .andReturn();
    }


    @Step("Make a Get-request with auth cookie")
    public Response makeGetRequestWithCookie(String url, String cookie){

        return given()
                .filter(new AllureRestAssured())
                .cookie("auth_sid",cookie)
                .get(url)
                .andReturn();
    }


    @Step("Make a Get-request with token ")
    public Response makeGetRequestWithToken(String url,String token){

        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token",token))
                .get(url)
                .andReturn();
    }


    @Step("Make a post-request with token and auth cookie")
    public Response makePostRequest(String url, Map<String,String> authData){

        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();
    }

    @Step("Make a PUT-request without token and auth cookie")
    public Response makePutRequest(String url,Map<String,String> authData){

        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .put(url)
                .andReturn();
    }

    @Step("Make a PUT - request with header and cookie")
    public Response makePutRequest (String url, String header, String cookie, Map<String,String> editData, String id){
        return given()
                .header(new Header("x-csrf-token", header))
                .cookie("auth_sid", cookie)
                .body(editData)
                .put(url+id)
                .andReturn();
    }

    @Step("Отправляет запрос на регистрацию нового пользователя")
    public static JsonPath generateUserRequest(Map<String, String> userData) {
        return given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();
    }



    @Step("Make a Get-request with token and auth cookie")
    public Response makeGetRequestWithTokenAndCookie(String url, String userId, String token, String cookie){
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .queryParam("user_id", userId)
                .post(url)
                .andReturn();
    }

    @Step("Make a Put-request without authorization")
    public Response makePutRequestWithoutAuth(String url, Map<String, String> authData){
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .put(url)
                .andReturn();
    }

} //end class
