package org.example;
import spark.Spark;
import static org.example.Handlers.*;
import static spark.Spark.*;



public class Main {

    public static void main(String[] args)  {
        port(8888);
        Spark.get("/average-exchange-rate", AverageExchangeRate());
        Spark.get("/minmax-average-value", MinMaxAverageValue());
        Spark.get("/major-difference", MaxDifference());

    }
}