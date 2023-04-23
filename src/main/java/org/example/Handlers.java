package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import spark.Route;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
public class Handlers {
    public static Route AverageExchangeRate() {
        return (request, response) -> {
            String code = request.queryParams("code");
            String dateParam = request.queryParams("date");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = dateFormat.parse(dateParam);

            HttpClient httpClient = HttpClient.newHttpClient();
            Gson gson = new Gson();

            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(new URI("http://api.nbp.pl/api/exchangerates/rates/a/" + code + "/" + dateFormat.format(date) + "/"))
                    .build();
            HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

            Currency currency = gson.fromJson(getResponse.body(), Currency.class);

            response.type("application/json");
            return gson.toJson(currency.rates[0].getMid());
        };
    }
    public static Route MinMaxAverageValue() {
        return (request, response) -> {

            String code = request.queryParams("code");
            String quotations = request.queryParams("quotations");

            HttpClient httpClient = HttpClient.newHttpClient();
            Gson gson = new Gson();

            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(new URI("http://api.nbp.pl/api/exchangerates/rates/a/" + code + "/last/" + quotations))
                    .build();
            HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

            Currency currency = gson.fromJson(getResponse.body(), Currency.class);
            Arrays.sort(currency.rates);
            float min = currency.rates[0].getMid();
            float max = currency.rates[currency.rates.length - 1].getMid();

            JsonObject result = new JsonObject();
            result.addProperty("min", min);
            result.addProperty("max", max);

            response.type("application/json");
            return result.toString();
        };
    }

    public static Route MaxDifference() {
        return (request, response) -> {
            String code = request.queryParams("code");
            String quotations = request.queryParams("quotations");
            float max = 0;

            HttpClient httpClient = HttpClient.newHttpClient();
            Gson gson = new Gson();

            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(new URI("http://api.nbp.pl/api/exchangerates/rates/c/" + code + "/last/" + quotations))
                    .build();
            HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

            Currency currency = gson.fromJson(getResponse.body(), Currency.class);
            for (int i = 0; i < currency.rates.length; i++) {
                if (max < currency.rates[i].getAsk() - currency.rates[i].getBid())
                    max = currency.rates[i].getAsk() - currency.rates[i].getBid();
            }
            response.type("application/json");
            return Float.toString(max);
        };
    }

}
