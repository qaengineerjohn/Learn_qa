package tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


@Epic("Delete cases")
@Feature("Delete")
public class ex18 extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    //Первый - на попытку удалить пользователя по ID 2
    @Description("Try to delete user with id=2")
    @DisplayName("Delete user with id=2")
    @Test
    public void testDeleteUncialUser() {
        Map<String,String> authData=new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password","1234");
        Response responseGetAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", authData);
        String userId = responseGetAuth.jsonPath().get("user_id").toString();
        Response responseUserData = apiCoreRequests.makeDeleteRequest("https://playground.learnqa.ru/api/user/"+userId,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"));
        Assertions.assertResponseCodeEquals(responseUserData, 400);
        Assertions.assertResponseTextEquals(responseUserData, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }



    //Создать пользователя, авторизоваться из-под него, удалить, затем попробовать получить его данные по ID и убедиться, что пользователь действительно удален
    @Test
    @Description("Create user, login, delete and check")
    @DisplayName("Full test for delete")
    public void testDeleteFullPositiveCase() {
        //Generate user
        Map<String,String> userData= DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        //login
        Map<String,String> authData=new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", authData);
        String userId = responseGetAuth.jsonPath().get("user_id").toString();

        //delete
        Response responseDeleteUser = apiCoreRequests.makeDeleteRequest("https://playground.learnqa.ru/api/user/"+userId,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"));

        //Get
        Response responseGetDeleteUser  = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/"+userId,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid")
        );
        Assertions.assertResponseCodeEquals(responseGetDeleteUser, 404);
        Assertions.assertResponseTextEquals(responseGetDeleteUser, "User not found");
    }

    //негативный, попробовать удалить пользователя, будучи авторизованными другим пользователем
    @Test
    @Description("Try to delete user if you login as another user")
    @DisplayName("Negative test for delete as another user")
    public void testDeleteAsAnotherUser() {
        //Generate user1
        Map<String,String> userData= DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        String userId = responseCreateAuth.jsonPath().get("id").toString();

        //Generate user2
        Map<String,String> userDataDelete= DataGenerator.getRegistrationData();
        Response responseCreateAuthForDelete = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userDataDelete);
        String userIdForDelete = responseCreateAuthForDelete.jsonPath().get("id").toString();

        //login
        Map<String,String> authData=new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", authData);
        String userIdForLogin = responseGetAuth.jsonPath().get("user_id").toString();

        //delete
        Response responseDeleteUser = apiCoreRequests.makeDeleteRequest("https://playground.learnqa.ru/api/user/"+userIdForDelete,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"));
        System.out.println(responseDeleteUser.asString());

        //Get
        Response responseGetDeleteUser  = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/"+userIdForDelete,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid")
        );
        Assertions.assertResponseCodeEquals(responseGetDeleteUser, 200);
        Assertions.assertJsonByName(responseGetDeleteUser, "username", "learnqa");
    }
} //end class