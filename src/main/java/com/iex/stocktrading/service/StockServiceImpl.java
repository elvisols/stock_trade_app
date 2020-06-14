package com.iex.stocktrading.service;

import com.iex.stocktrading.model.IEXRecord;
import com.iex.stocktrading.model.Stock;
import com.iex.stocktrading.repository.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static com.iex.stocktrading.config.Constants.IEX_TOKEN;

/**
 * Service Implementation for managing Stock.
 */
@Slf4j
@Service
@Transactional
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    private final RestTemplate restTemplate;

    public StockServiceImpl(StockRepository stockRepository, RestTemplate restTemplate) {
        this.stockRepository = stockRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public void saveAll(List<Stock> stocks) {

        stockRepository.saveAll(stocks);
    }

    /**
     * Get all the stocks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Stock> findAll(Pageable pageable) {

        log.info("Request to get all Stocks");

        return stockRepository.findAll(pageable);
    }

    /**
     * Get a stock by symbol.
     *
     * @param symbol the symbol of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public IEXRecord findOne(String symbol) {

        log.debug("Request to get Stock : {}", symbol);

        return restTemplate.getForObject("https://cloud-sse.iexapis.com/stable/stock/" + symbol + "/quote?token=" + IEX_TOKEN, IEXRecord.class);

    }

}
