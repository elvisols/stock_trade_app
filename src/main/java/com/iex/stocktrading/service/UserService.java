package com.iex.stocktrading.service;

import com.iex.stocktrading.model.EActivity;
import com.iex.stocktrading.model.Transaction;
import com.iex.stocktrading.model.User;
import com.iex.stocktrading.model.dto.NewUserDTO;
import com.iex.stocktrading.model.dto.TransactionDTO;
import com.iex.stocktrading.model.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

public interface UserService {

    /**
     * Create new user entity
     */
    UserDTO save(NewUserDTO userDTO);

    /**
     * Update user entity
     */
    UserDTO update(UserDTO userDTO);

    /**
     * Fund user account entity
     */
    UserDTO deposit(BigDecimal amount);

    /**
     * Deplete user account entity
     */
    UserDTO withdraw(BigDecimal amount);

    /**
     * Get all the users.
     */
    Page<UserDTO> findAll(Pageable pageable);

    /**
     * Get the user by Id.
     */
    Optional<UserDTO> findOne(Long id);

    /**
     * Get the user by Id.
     */
    User findByUsername(String username);

    /**
     * Delete user by Id.
     */
    void delete(Long id);

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
