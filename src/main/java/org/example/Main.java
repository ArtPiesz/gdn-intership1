package org.example;
import spark.Spark;
import static org.example.Handlers.*;
import static spark.Spark.*;



public class Main {

    public static void main(String[] args)  {
        port(8888);
        Spark.get("/average-exchange-rate/:code/:date", AverageExchangeRate());
        Spark.get("/minmax-average-value/:code/:quotations", MinMaxAverageValue());
        Spark.get("/major-difference/:code/:quotations", MaxDifference());

    }
}