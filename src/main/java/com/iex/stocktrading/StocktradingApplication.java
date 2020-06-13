package com.iex.stocktrading;

import com.iex.stocktrading.model.Stock;
import com.iex.stocktrading.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@EnableCaching
@EnableAspectJAutoProxy
@SpringBootApplication
public class StocktradingApplication implements CommandLineRunner {

    @Autowired
    private StockService stockService;

    @Autowired
    private RestTemplate restTemplate;

    public static void main(String[] args) {
        SpringApplication.run(StocktradingApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // Bootstrap available Stocks
        ResponseEntity<Stock[]> response = restTemplate.getForEntity("https://cloud-sse.iexapis.com/stable/ref-data/symbols?token=pk_50b799876c4541b89d194f994bdc064f", Stock[].class);

        Stock[] stocks = response.getBody();

        stockService.saveAll(Arrays.asList(stocks));

    }
}
