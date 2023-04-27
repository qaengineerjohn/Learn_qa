import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

public class ex8 {

    @Test
    public void ex8() throws InterruptedException {

        // 1) создавал задачу
        JsonPath response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        response.prettyPrint();

        // получаем токен
        String token = response.get("token");

        // 2)делал один запрос с token ДО того, как задача готова, убеждался в правильности поля status
        System.out.println("Created a new job with token: " + token);
        JsonPath response2 = RestAssured
                .given()
                .queryParam("token", token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        response2.prettyPrint();

        // получаем секунды для ожижания выполнения задачи
        int seconds = response.get("seconds");
        System.out.println("Wait for: " + seconds);

        // 3)ждал нужное количество секунд с помощью функции Thread.sleep() - для этого надо сделать import time
        Thread.sleep(seconds * 1000);

        //4) делал бы один запрос c token ПОСЛЕ того, как задача готова, убеждался в правильности поля status и наличии поля result
        JsonPath response3 = RestAssured
                .given()
                .queryParam("token", token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        response3.prettyPrint();

        String status = response3.get("status");

        if (status.equals("Job is ready")) {
            System.out.println("Result: " + response3.get("result"));
        }
    }
}
