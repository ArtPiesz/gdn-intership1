package org.example;


import com.google.gson.Gson;

import static spark.Spark.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static String AverageExchangeRate(String[] args ) throws Exception{

        HttpClient httpClient = HttpClient.newHttpClient() ;
        Gson gson = new Gson();

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI("http://api.nbp.pl/api/exchangerates/rates/a/"+args[0]+"/"+args[1]+"/"))
                .build();
        HttpResponse<String>  getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

        Currency currency = gson.fromJson(getResponse.body(), Currency.class);

       // System.out.println(currency.rates[0].getMid());
        return Float.toString(currency.rates[0].getMid());
    }
    public static String MinMaxAverageValue(String[] args) throws Exception{
        float min;
        float max;
        HttpClient httpClient = HttpClient.newHttpClient() ;
        Gson gson = new Gson();

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI("http://api.nbp.pl/api/exchangerates/rates/a/"+args[0]+"/last/"+args[1]))
                .build();
        HttpResponse<String>  getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

        Currency currency = gson.fromJson(getResponse.body(), Currency.class);
        Arrays.sort(currency.rates);
        // System.out.println(currency.rates[0].getMid());
        min = currency.rates[0].getMid();
        max = currency.rates[currency.rates.length-1].getMid();

        System.out.println(min);
        System.out.println(max);

        return Float.toString(currency.rates[0].getMid());

    }

    public static String MaxDifference(String[] args) throws Exception{
        float max = 0;

        HttpClient httpClient = HttpClient.newHttpClient() ;
        Gson gson = new Gson();

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI("http://api.nbp.pl/api/exchangerates/rates/c/"+args[0]+"/last/"+args[1]))
                .build();
        HttpResponse<String>  getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

        Currency currency = gson.fromJson(getResponse.body(), Currency.class);
        for(int i =0;i<currency.rates.length;i++){
            if(max < currency.rates[i].getAsk() - currency.rates[i].getBid())
                max = currency.rates[i].getAsk() - currency.rates[i].getBid();
        }
        System.out.println(max);
        return args[0];
    }



    public static void main(String[] args) throws Exception {
        //port(8888);
       // get("/hello", (req, res) -> "Hello World");
        Scanner sc = new Scanner(System.in);
       // System.out.print("Enter currency code and date as: CC/YYY-MM-DD.");
        System.out.print("Enter currency code and number of quotations as: CC/N.");//GBP/2023-01-02/ usd/2016-04-04/
        String [] input = sc.nextLine().split("/");

        System.out.println(MaxDifference(input));

    }
}