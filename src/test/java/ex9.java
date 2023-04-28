import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ex9 {

    @Test
    public void ex9() {

        List<String> passwordsList = Arrays.asList(
                "password", "123456", "12345678", "qwerty", "abc123", "monkey", "123456789", "1234567", "letmein", "trustno1", "iloveyou", "dragon", "baseball", "adobe123[a]", "123123", "welcome", "admin", "1234567890", "solo", "master", "sunshine", "photoshop[a]", "ashley", "mustang", "bailey", "passw0rd", "shadow", "121212", "football", "michael", "654321", "jesus", "superman", "princess", "696969", "qazwsx", "ninja", "azerty", "loveme", "password1", "000000", "starwars"
        );

        for (String password : passwordsList) {
            Map<String, String> data = new HashMap<>();
            data.put("login", "super_admin");
            data.put("password", password);

            Response responseForCookies = RestAssured
                    .given()
                    .body(data)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();

            responseForCookies.prettyPrint();

            String cookies = responseForCookies.getCookie("auth_cookie");
            System.out.println(cookies);


            Map<String, String> cookiesData = new HashMap<>();
            data.put("auth_cookie", cookies);

            Response responseStatus = RestAssured
                    .given()
                    .body(data)
                    .cookies(cookiesData)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie");

            if (responseStatus.getBody().asString().equals("You are authorized")) {
                System.out.println("Found correct password: " + password);
                break;
            }else{
                System.out.println("Not correct password: " + password);
            }
        }
    }
}

