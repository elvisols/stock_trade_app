package com.iex.stocktrading.service;

import com.iex.stocktrading.model.dto.UserStockDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserStockService {

    UserStockDTO save(UserStockDTO userStockDTO);

    Page<UserStockDTO> findAllByUser(Pageable pageable);

    Optional<UserStockDTO> findOne(Long id);
}
