package com.iex.stocktrading.service;


import com.iex.stocktrading.model.IEXRecord;
import com.iex.stocktrading.model.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Stock.
 */
public interface StockService {

    /**
     * Save stocks.
     *
     * @param stocks the stocks to save
     */
    void saveAll(List<Stock> stocks);

    /**
     * Get all the stocks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Stock> findAll(Pageable pageable);
    
    /**
     * Get the a stock by symbol.
     *
     * @param symbol the stock id
     * @return the entity
     */
    IEXRecord findOne(String symbol);

}
