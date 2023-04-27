import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class HelloWorldTest {

    @Test
    public void test_1() {

        Map<String,String> params = new HashMap<>();
        params.put("name","Ivan");

        JsonPath response = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();

        String answer = response.get("answer");

        if (answer == null){
            System.out.println("Need to check request/response");
        } else {
            System.out.println(answer);
        }
    }




    @Test
    public void test_2P(){

        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/check_type")
                .andReturn();

        response.prettyPrint();
    }
}
