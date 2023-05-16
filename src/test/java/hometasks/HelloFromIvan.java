package hometasks;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class HelloFromIvan {

    @Test
    public void HelloFromIvan() {
       System.out.println("Hello from Ivan");
    }



}

//
//    String status;
//        do {
//                //Make new request after waiting
//                JsonPath response4 = RestAssured
//                .given()
//                .queryParam("token", token)
//                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
//                .jsonPath();
//
//                response4.prettyPrint();
//
//                status = response4.get("status");
//                String result = response4.get("result");
//
//                System.out.println("Status after waiting: " + status);
//
//                if (status.equals("Job is ready")) {
//                System.out.println("Status: " + status);
//                System.out.println("Result: " + result);
//                break;
//                } else {
//                System.out.println("Job is NOT ready");
//
//                JsonPath response5 = RestAssured
//                .given()
//                .queryParam("token", token)
//                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
//                .jsonPath();
//
//                response5.prettyPrint();
//                status = response5.get("status");
//                System.out.println("else");
//                }
//                } while (status.equals("Job is NOT ready"));