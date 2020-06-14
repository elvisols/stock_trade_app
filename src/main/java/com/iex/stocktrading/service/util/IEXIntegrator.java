package com.iex.stocktrading.service.util;

import com.iex.stocktrading.model.IEXRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import static com.iex.stocktrading.config.Constants.IEX_TOKEN;

@Service
public class IEXIntegrator {

    private static RestTemplate restTemplate;

    public IEXIntegrator(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static IEXRecord fetch(String symbol) {

        return restTemplate.getForObject("https://cloud-sse.iexapis.com/stable/stock/" + symbol + "/quote?token=" + IEX_TOKEN, IEXRecord.class);
    }
}
