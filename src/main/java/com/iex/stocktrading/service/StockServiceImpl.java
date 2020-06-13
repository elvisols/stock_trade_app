package com.iex.stocktrading.service;

import com.iex.stocktrading.model.Stock;
import com.iex.stocktrading.repository.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Stock.
 */
@Slf4j
@Service
@Transactional
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
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
    public Optional<Stock> findOne(String symbol) {

        log.debug("Request to get Stock : {}", symbol);

        return stockRepository.findById(symbol);
    }
}
