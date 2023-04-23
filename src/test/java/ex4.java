import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class ex4 {

    @Test
    public void ex4() {
        Response response = RestAssured
                .get(" https://playground.learnqa.ru/api/get_text")
                .andReturn();
        response.prettyPrint();
    }
}
