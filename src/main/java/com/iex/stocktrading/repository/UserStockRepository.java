package com.iex.stocktrading.repository;

import com.iex.stocktrading.model.UserStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserStockRepository extends PagingAndSortingRepository<UserStock, Long> {

    Page<UserStock> findAllByUser_Username(String username, Pageable pageable);
}
