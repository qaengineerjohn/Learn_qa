import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class ex13 {

    private final String user_agent1 = "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.3";
    private final String user_agent2 = "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1";
    private final String user_agent3 = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
    private final String user_agent4 = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0";
    private final String user_agent5 = "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1";

    @ParameterizedTest
    @ValueSource(strings = {user_agent1, user_agent2, user_agent3, user_agent4, user_agent5})
    public void ex13(String user_agent) {

        Header header = new Header("User-Agent", user_agent);
        Response response = RestAssured
                .given()
                .header(header)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .andReturn();

        response.prettyPrint();

        String userAgent = response.jsonPath().getString("user_agent");
        String platform = response.jsonPath().getString("platform");
        String browser = response.jsonPath().getString("browser");
        String device = response.jsonPath().getString("device");


        if (userAgent.equals(user_agent1)) {
            System.out.println("1");
            assertEquals("Mobile", platform, "Platform value is incorrect");
            assertEquals("No", browser, "Browser value is incorrect");
            assertEquals("Android", device, "Device value is incorrect");

        } else if (userAgent.equals(user_agent2)) {
            System.out.println("2");
            assertEquals("Mobile", platform, "Platform value is incorrect");
            assertEquals("Chrome", browser, "Browser value is incorrect");
            assertEquals("Android", device, "Device value is incorrect");

        } else if (userAgent.equals(user_agent3)) {
            System.out.println("3");
            assertEquals("Googlebot", platform, "Platform value is incorrect");
            assertEquals("Unknown", browser, "Browser value is incorrect");
            assertEquals("Unknown", device, "Device value is incorrect");

        } else if (userAgent.equals(user_agent4)) {
            System.out.println("4");
            assertEquals("Web", platform, "Platform value is incorrect");
            assertEquals("Chrome", browser, "Browser value is incorrect");
            assertEquals("No", device, "Device value is incorrect");

        } else if (userAgent.equals(user_agent5)) {
            System.out.println("5");
            assertEquals("Mobile", platform, "Platform value is incorrect");
            assertEquals("No", browser, "Browser value is incorrect");
            assertEquals("iPhone", device, "Device value is incorrect");

        } else {
            System.out.println("Check your data in requests");
        }
    }
}

