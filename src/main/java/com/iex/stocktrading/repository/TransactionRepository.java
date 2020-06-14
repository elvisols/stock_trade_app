package com.iex.stocktrading.repository;


import com.iex.stocktrading.model.CustomHttpTrace;
import com.iex.stocktrading.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {

    Page<Transaction> findAllByUser_Username(String user, Pageable pageable);

//    Page<Transaction> findAllByUser_UsernameAndTimestampBetween(String user, Pageable pageable);

}
