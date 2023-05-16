package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.HashMap;
import java.util.Map;


public class UserRegisterTest extends BaseTestCase {

    @Test
    public void testCreateUserWithExistingEmail() {

        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response response = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertResponseCodeEquals(response, 400);
        Assertions.assertResponseTextEquals(response, "Users with email '" + email + "' already exists");
    }

    @Test
    public void testCreateUserSuccessfully() {

        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response response = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertResponseCodeEquals(response, 200);
        Assertions.assertJsonHasFields(response, "id");
        response.prettyPrint();
    }

    //  - Создание пользователя с некорректным email - без символа @
    @Test
    public void testCreateUserWithInvalidEmail() {

        String invalidEmail = "invalidemail.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", invalidEmail);
        userData = DataGenerator.getRegistrationData(userData);


        Response response = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        response.print();

        Assertions.assertResponseCodeEquals(response, 400);
        Assertions.assertResponseTextContains(response, "Invalid email format");
    }

    //  - Создание пользователя без указания одного из полей - с помощью @ParameterizedTest необходимо проверить, что отсутствие любого параметра не дает зарегистрировать пользователя
    @ParameterizedTest
    @ValueSource(strings = {"email","password","username","firstName","lastName"})
    public void testCreateUserWithoutField(String field){

        Map<String,String> userData = DataGenerator.getRegistrationData();

        // remove the specified field
        userData.remove(field);

        Response response = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        response.print();

        Assertions.assertResponseCodeEquals(response,400);
        Assertions.assertResponseTextContains(response,"The following required params are missed: " + field);
    }


    //  - Создание пользователя с очень коротким именем в один символ
    @Test
    public void testShortFirstName() {

        String invalidNameTest = "A";

        Map<String, String> userData = new HashMap<>();
        userData.put("firstName", invalidNameTest);
        userData = DataGenerator.getRegistrationData(userData);

        Response response = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        response.print();

        Assertions.assertResponseCodeEquals(response, 400);
        Assertions.assertResponseTextContains(response, "The value of 'firstName' field is too short");
    }

    // - Создание пользователя с очень длинным именем - длиннее 250 символов
    @Test
    public void testLongFirstName() {

        String invalidNameTest = "A".repeat(251);

        Map<String, String> userData = new HashMap<>();
        userData.put("firstName", invalidNameTest);
        userData = DataGenerator.getRegistrationData(userData);

        Response response = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        response.print();

        Assertions.assertResponseCodeEquals(response, 400);
        Assertions.assertResponseTextContains(response, "The value of 'firstName' field is too long");
    }


}// end class
