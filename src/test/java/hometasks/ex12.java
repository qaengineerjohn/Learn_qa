package hometasks;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import lib.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ex12 {

    @Test
    public void ex12() {

        Response response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();

        Headers responseHeaders = response.headers();

        assertEquals("Some secret value", responseHeaders.getValue("x-secret-homework-header"), "x-secret-homework-header header value is incorrect");
    }
}
