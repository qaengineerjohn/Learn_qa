import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class ex7 {

    @Test
    public void ex7() {

        String URL = "https://playground.learnqa.ru/api/long_redirect";
        int countRedirect = 0;

        do {
            Response response = given()
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(URL)
                    .andReturn();

            int statusCode = response.getStatusCode();
            if (statusCode == 200) {
                System.out.println("Успешный запрос!");
                System.out.println(statusCode);
                System.out.println(response.getHeader("Location"));
                break;
            } else {
                // Получаем URL для редиректа из нужного заголовка
                URL = response.getHeader("Location");
                countRedirect++;

                System.out.println(statusCode);
                System.out.println(response.getHeader("Location"));
            }
        } while (URL != null);

        System.out.println("Number of redirects: " + countRedirect);
    }
}
