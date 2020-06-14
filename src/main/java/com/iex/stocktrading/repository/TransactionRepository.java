package com.iex.stocktrading.repository;


import com.iex.stocktrading.model.CustomHttpTrace;
import com.iex.stocktrading.model.EActivity;
import com.iex.stocktrading.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {

    Page<Transaction> findAllByUser_UsernameAndTimestampBetween(String user, Date from, Date to, Pageable pageable);

    Page<Transaction> findAllByUser_UsernameAndActivityAndTimestampBetween(String user, EActivity activity, Date from, Date to, Pageable pageable);

}
