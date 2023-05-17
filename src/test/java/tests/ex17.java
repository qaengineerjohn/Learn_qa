package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import lib.ApiCoreRequests;

@Epic("Edit cases")
@Feature("Editing")
public class ex17 extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("Generate user, login, edit and get")
    @DisplayName("Positive test for edit")
    public void testEditUnauthorized() {
        //Generate user
        Map<String,String> userData= DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        String userId = responseCreateAuth.jsonPath().get("id").toString();

        //edit
        String newName = "Changed name";
        Map <String,String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests.makePutRequestWithoutAuth("https://playground.learnqa.ru/api/user/"+userId,editData );
        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser, "Auth token not supplied");
    }


    @Test
    @Description("Try to edit without login")
    @DisplayName("Edit without login")
    public void testEditWithDifferentUser() {

        //Generate user1
        Map<String,String> userData= DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        String userId = responseCreateAuth.jsonPath().get("id").toString();
        System.out.println("создаем для авторизации  - "+userId);

        //Generate user2
        Map <String,String> userDataEdit= new HashMap<>();
        String unchangedFirstName = "Unchanged firstName";
        userDataEdit.put("firstName", unchangedFirstName);
        userDataEdit= DataGenerator.getRegistrationData(userDataEdit);
        Response responseCreateAuthForEdit = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userDataEdit);
        String userIdForEdit = responseCreateAuthForEdit.jsonPath().get("id").toString();
        System.out.println("создаем для авторизации  - "+userIdForEdit);

        //login1
        Map<String,String> authData=new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", authData);
        String ii= responseGetAuth.jsonPath().get("user_id").toString();
        System.out.println("авторизуемся как  - "+ii);

        //edit
        String newName = "Change name";
        Map <String,String> editData = new HashMap<>();
        editData.put("firstName", newName);
        System.out.println("https://playground.learnqa.ru/api/user/"+userIdForEdit);
        Response responseEditUser = apiCoreRequests.makePutRequest("https://playground.learnqa.ru/api/user/",
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"),
                editData,userIdForEdit );

        //login2
        Map<String,String> authData2=new HashMap<>();
        authData2.put("email", userDataEdit.get("email"));
        authData2.put("password", userDataEdit.get("password"));
        Response responseGetAuth2 = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", authData2);


        Response responseUserData = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/"+userIdForEdit,
                this.getHeader(responseGetAuth2, "x-csrf-token"),
                this.getCookie(responseGetAuth2, "auth_sid"));
        System.out.println(responseUserData.asString());
        Assertions.assertJsonByName(responseUserData, "firstName", unchangedFirstName);
    }




    @Test
    @Description("Try to edit using incorrect email")
    @DisplayName("Edit using incorrect email")
    public void testEditInvalidEmail() {
        //Generate user1
        Map<String,String> userData= DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        String userId = responseCreateAuth.jsonPath().get("id").toString();

        //login
        Map<String,String> authData=new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        responseGetAuth.prettyPrint();
        //edit
        String newEmail= DataGenerator.getRandomEmail();
        newEmail = newEmail.replace("@","");
        System.out.println(newEmail);
        Map <String,String> editData = new HashMap<>();
        editData.put("email", newEmail);

        Response responseEditUser = apiCoreRequests.makePutRequest("https://playground.learnqa.ru/api/user/",
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"),
                editData,userId);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser, "Invalid email format");
    }


    @Test
    @Description("Try to edit using short first name")
    @DisplayName("Edit using short first name")
    public void testEditInvalidFirstName() {

        //Generate user1
        Map<String,String> userData= DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        String userId = responseCreateAuth.jsonPath().get("id").toString();

        //login
        Map<String,String> authData=new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //edit
        String newFirstName= RandomStringUtils.randomAlphabetic(1);
        Map <String,String> editData = new HashMap<>();
        editData.put("firstName", newFirstName);

        Response responseEditUser = apiCoreRequests.makePutRequest("https://playground.learnqa.ru/api/user/",
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"),
                editData,userId);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertJsonByName(responseEditUser,"error", "Too short value for field firstName");
    }
}

