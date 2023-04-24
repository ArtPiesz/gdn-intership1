package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


import spark.Route;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
public class Handlers {

    public static Route AverageExchangeRate() {
        return (request, response) -> {
            String code = request.params("code");
            String dateParam = request.params("date");

            Date date;
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                date = dateFormat.parse(dateParam);

            } catch (ParseException e) {
                response.status(400); // Bad Request
                return "Invalid date format, try: yyyy-mm-dd";
            }

            HttpClient httpClient = HttpClient.newHttpClient();
            Gson gson = new Gson();

            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(new URI("http://api.nbp.pl/api/exchangerates/rates/a/" + code + "/" + dateParam + "/"))
                    .build();

            HttpResponse<String> getResponse;
            try {
                getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                response.status(500); // Internal Server Error
                return "Error while making API call";
            }

            if (getResponse.statusCode() != 200) {
                response.status(getResponse.statusCode());
                return "API returned error: " + getResponse.body();
            }

            Currency currency = gson.fromJson(getResponse.body(), Currency.class);

            if (currency.rates.length == 0) {
                response.status(404); // Not Found
                return "No exchange rate data found for the specified currency code and date";
            }


            JsonObject result = new JsonObject();
            result.addProperty("averageExchangeRate", currency.rates[0].getMid());

            response.type("application/json");
            return result.toString();
           // return gson.toJson(currency.rates[0].getMid());
        };
    }
    public static Route MinMaxAverageValue() {
        return (request, response) -> {
            String code = request.params("code");

            int quotations;
            try {
                quotations = Integer.parseInt(request.params("quotations"));
                if (quotations < 1 || quotations > 255) {
                    response.status(400); // Bad Request
                    return "Invalid quotations parameter";
                }
            } catch (NumberFormatException e) {
                response.status(400); // Bad Request
                return "Invalid quotations parameter";
            }

            HttpClient httpClient = HttpClient.newHttpClient();
            Gson gson = new Gson();

            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(new URI("http://api.nbp.pl/api/exchangerates/rates/a/" + code + "/last/" + quotations))
                    .build();

            HttpResponse<String> getResponse;
            try {
                getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                response.status(500); // Internal Server Error
                return "Error while making API call";
            }

            if (getResponse.statusCode() != 200) {
                response.status(getResponse.statusCode());
                return "API returned error: " + getResponse.body();
            }

            Currency currency = gson.fromJson(getResponse.body(), Currency.class);

            if (currency.rates.length == 0) {
                response.status(404); // Not Found
                return "No exchange rate data found for the specified currency code and quotations";
            }

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
            String code = request.params("code");

            int quotations;
            try {
                quotations = Integer.parseInt(request.params("quotations"));
                if (quotations < 1 || quotations > 255) {
                    response.status(400); // Bad Request
                    return "Invalid quotations parameter";
                }
            } catch (NumberFormatException e) {
                response.status(400); // Bad Request
                return "Invalid quotations parameter";
            }

            HttpClient httpClient = HttpClient.newHttpClient();
            Gson gson = new Gson();

            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(new URI("http://api.nbp.pl/api/exchangerates/rates/c/" + code + "/last/" + quotations))
                    .build();

            HttpResponse<String> getResponse;
            try {
                getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                response.status(500); // Internal Server Error
                return "Error while making API call";
            }

            if (getResponse.statusCode() != 200) {
                response.status(getResponse.statusCode());
                return "API returned error: " + getResponse.body();
            }

            Currency currency = gson.fromJson(getResponse.body(), Currency.class);

            if (currency.rates.length == 0) {
                response.status(404); // Not Found
                return "No exchange rate data found for the specified currency code and quotations";
            }

            float max = 0;
            for (int i = 0; i < currency.rates.length; i++) {
                if (max < currency.rates[i].getAsk() - currency.rates[i].getBid())
                    max = currency.rates[i].getAsk() - currency.rates[i].getBid();
            }

            JsonObject result = new JsonObject();
            result.addProperty("maxDifference", max);

            response.type("application/json");
            return result.toString();
        };
    }

}
