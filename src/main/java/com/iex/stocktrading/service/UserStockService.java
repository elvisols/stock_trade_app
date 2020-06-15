package com.iex.stocktrading.service;

import com.iex.stocktrading.model.EActivity;
import com.iex.stocktrading.model.dto.TransactionDTO;
import com.iex.stocktrading.model.dto.UserStockDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.Optional;

public interface UserStockService {

    UserStockDTO save(UserStockDTO userStockDTO);

    Page<UserStockDTO> findAllByUser(Pageable pageable);

    Optional<UserStockDTO> findOne(Long id);

    Optional<UserStockDTO> buy(String symbol, Integer shares);

    Optional<UserStockDTO> sell(String symbol, Integer shares);

    Optional<UserStockDTO> findByUserAndStock(String user, String stock);

    /**
     * Get transaction summary
     * @param activity
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    Page<TransactionDTO> getTransactionSummary(EActivity activity, Date start, Date end, Pageable pageable);

}
