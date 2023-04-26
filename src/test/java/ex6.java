import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class ex6 {

    @Test
    public void ex6() {
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        String answer = response.getHeader("Location");

        if (answer == null){
            System.out.println("The redirect URL in the response header is null");
        } else {
            System.out.println(answer);
        }
    }
}


