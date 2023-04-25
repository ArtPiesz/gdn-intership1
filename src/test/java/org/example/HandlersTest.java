package org.example;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import spark.Spark;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class HandlersTest {


    private static final String BASE_URL = "http://localhost:8888";

    private static HttpClient httpClient;

    @BeforeAll
    public static void setup() {
        Main.main(null);

        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    @AfterAll
    public static void tearDown() {
        Spark.stop();
    }

//basic tests
    @Test
    public void testAverageExchangeRate() throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/average-exchange-rate/GBP/2023-01-02"))
                .build();
        HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());

        assertEquals("{\"averageExchangeRate\":5.2768}", res.body());
        assertEquals(200, res.statusCode());
        assertEquals("application/json", res.headers().firstValue("Content-Type").get());
    }

    @Test
    public void testMinMaxAverageValue() throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/minmax-average-value/usd/10"))
                .build();
        HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());

        assertEquals("{\"min\":4.1649,\"max\":4.2713}", res.body());
        assertEquals(200, res.statusCode());
        assertEquals("application/json", res.headers().firstValue("Content-Type").get());
    }

    @Test
    public void testMaxDifference() throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/major-difference/usd/10"))
                .build();
        HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());

        assertEquals("{\"maxDifference\":0.08539963}", res.body());
        assertEquals(200, res.statusCode());
        assertEquals("application/json", res.headers().firstValue("Content-Type").get());
    }

    //invalid arguments tests
    @Test
    public void testInvalidPath() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/invalid-path"))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertTrue(response.body().contains("<html><body><h2>404 Not found</h2></body></html>"));
    }

    @Test
    public void testInvalidCurrencyCode() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/average-exchange-rate/abc/2022-01-01"))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertEquals("API returned error: 404 NotFound - Not Found - Brak danych", response.body());
    }

    @Test
    public void testInvalidDateFormat() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/average-exchange-rate/usd/2022/01/01"))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertEquals("<html><body><h2>404 Not found</h2></body></html>", response.body());
    }

    @Test
    public void testInvalidQuotations() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/minmax-average-value/usd/abc"))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        assertEquals("Invalid quotations parameter", response.body());
    }

    @Test
    public void testFutureDate() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/average-exchange-rate/usd/2025-01-01"))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        assertEquals("API returned error: 400 BadRequest - Błędny zakres dat / Invalid date range", response.body());
    }

    @Test
    public void testNegativeQuotations() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/minmax-average-value/usd/-10"))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        assertEquals("Invalid quotations parameter", response.body());
    }
}