package com.iex.stocktrading.repository;

import com.iex.stocktrading.model.Stock;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StockRepository extends PagingAndSortingRepository<Stock, String> {
}
