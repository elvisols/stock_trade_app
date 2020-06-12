package com.iex.stocktrading.service;

import com.iex.stocktrading.model.dto.NewUserDTO;
import com.iex.stocktrading.model.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * Get all the users.
     */
    Page<UserDTO> findAll(Pageable pageable);

    /**
     * Get the user by Id.
     */
    Optional<UserDTO> findOne(Long id);

    /**
     * Delete user by Id.
     */
    void delete(Long id);
    
}
