package com.iex.stocktrading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class StocktradingApplication {

    public static void main(String[] args) {
        SpringApplication.run(StocktradingApplication.class, args);
    }

}
