package com.iex.stocktrading.service;

import com.iex.stocktrading.exception.UserNotFoundException;
import com.iex.stocktrading.exception.UserStockNotFoundException;
import com.iex.stocktrading.model.UserStock;
import com.iex.stocktrading.model.dto.UserStockDTO;
import com.iex.stocktrading.model.dto.mapper.UserStockMapper;
import com.iex.stocktrading.repository.UserStockRepository;
import com.iex.stocktrading.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class UserStockServiceImpl implements UserStockService {

    private final UserStockRepository userStockRepository;

    private final UserStockMapper userStockMapper;

    public UserStockServiceImpl(UserStockRepository userStockRepository, UserStockMapper userStockMapper) {
        this.userStockRepository = userStockRepository;
        this.userStockMapper = userStockMapper;
    }

    @Override
    public UserStockDTO save(UserStockDTO userStockDTO) {

        log.debug("Request to save UserStock : {}", userStockDTO);

        UserStock userStock = userStockMapper.toEntity(userStockDTO);

        if(userStock.getId() != null) {
            // update record
            Optional<UserStock> uS = userStockRepository.findById(userStock.getId());

            if(uS.isPresent()) {
                UserStock tmp = uS.get();
                tmp.setShares(userStock.getShares() == null ? tmp.getShares() : userStock.getShares());
                tmp.setCurrentPrice(userStock.getCurrentPrice() == null ? tmp.getCurrentPrice() : userStock.getCurrentPrice());
                userStock = tmp;
            } else {
                throw new UserStockNotFoundException(userStock.getId().toString());
            }
        } else {
            // create new record
            userStock = userStockRepository.save(userStock);
        }

        return userStockMapper.toDto(userStock);
    }

    @Override
    public Page<UserStockDTO> findAllByUser(Pageable pageable) {

        Optional<String> u = SecurityUtils.getCurrentUserLogin();

        log.debug("Request to get all UserStocks by {}", u);

        if(u.isPresent()) {
            return userStockRepository.findAllByUser_Username(u.get(), pageable)
                    .map(userStockMapper::toDto);
        } else {
            throw new UserNotFoundException("User");
        }


    }

    @Override
    public Optional<UserStockDTO> findOne(Long id) {

        log.debug("Request to get UserStock : {}", id);

        return userStockRepository.findById(id)
                .map(userStockMapper::toDto);
    }

}
