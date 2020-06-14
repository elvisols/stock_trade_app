package com.iex.stocktrading.repository;

import com.iex.stocktrading.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findByUsername(String username);
}
