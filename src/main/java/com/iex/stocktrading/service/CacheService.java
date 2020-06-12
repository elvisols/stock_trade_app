package com.iex.stocktrading.service;

import com.iex.stocktrading.model.IEXRecord;
import com.iex.stocktrading.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CacheService {

    private RestTemplate restTemplate;

    public CacheService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable(value="fetchAllRecords", key="#constant")
    public List<User> fetchAllRecords(int constant) {

        log.info("getting records...");

        IEXRecord rec1 = restTemplate.getForObject("https://jsonmock.hackerrank.com/api/article_users/search?page=1", IEXRecord.class);
        IEXRecord rec2 = restTemplate.getForObject("https://jsonmock.hackerrank.com/api/article_users/search?page=2", IEXRecord.class);

        assert rec1 != null;
//        List<User> details = new ArrayList<>(rec1.getData());

        assert rec2 != null;
//        details.addAll(rec2.getData());

        return null;
    }
}
