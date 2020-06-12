package com.iex.stocktrading.repository;


import com.iex.stocktrading.model.CustomHttpTrace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraceRepository extends PagingAndSortingRepository<CustomHttpTrace, String> {

    Page<CustomHttpTrace> findAllByStatus(Integer status, Pageable pageable);

}
