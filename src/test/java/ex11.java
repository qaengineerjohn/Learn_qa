import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ex11 {

    @Test
    public void ex11() {

        Response response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

        String cookies = String.valueOf(response.getCookies());
        assertEquals("{HomeWork=hw_value}", cookies, "Cookies value is incorrect");
    }
}
