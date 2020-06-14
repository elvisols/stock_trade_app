package com.iex.stocktrading.service.util;

import com.iex.stocktrading.exception.InsufficientStockException;
import com.iex.stocktrading.exception.UserNotFoundException;
import com.iex.stocktrading.model.IEXRecord;
import com.iex.stocktrading.model.Stock;
import com.iex.stocktrading.model.User;
import com.iex.stocktrading.model.UserStock;
import com.iex.stocktrading.model.dto.UserStockDTO;
import com.iex.stocktrading.model.dto.mapper.UserMapper;
import com.iex.stocktrading.model.dto.mapper.UserStockMapper;
import com.iex.stocktrading.security.SecurityUtils;
import com.iex.stocktrading.service.UserService;
import com.iex.stocktrading.service.UserStockService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Getter
@Setter
@Component
public class TradeFacade {

    private String symbol;
    private Integer shares;
    private final Agent agent;
    private final UserService userService;
    private final UserStockService usService;
    private final UserStockMapper userStockMapper;
    private final UserMapper userMapper;


    public TradeFacade(Agent agent, UserService userService, UserStockService usService, UserStockMapper userStockMapper, UserMapper userMapper) {
        this.agent = agent;
        this.userService = userService;
        this.usService = usService;
        this.userStockMapper = userStockMapper;
        this.userMapper = userMapper;
    }

    public String getSymbol() {
        return symbol;
    }

    public Integer getShares() {
        return shares;
    }

    public UserStockDTO buy() {
        IEXRecord iexRecord = IEXIntegrator.fetch(getSymbol());

        if(agent.haveEnoughMoney(getShares() * iexRecord.getLatestPrice())) {
            User user = userService.findByUsername(SecurityUtils.getCurrentUserLogin().get());

            Optional<UserStockDTO> userStock = usService.findByUserAndStock(SecurityUtils.getCurrentUserLogin().get(), getSymbol());
//            Optional<UserStockDTO> userStock = usService.findByUserAndStock(user.getId(), getSymbol());

            if(userStock.isPresent()) {
                // Update User Stock
                UserStock usrStk = userStockMapper.toEntity(userStock.get());
                usrStk.setShares(usrStk.getShares() + getShares());
                usrStk.setCurrentPrice(BigDecimal.valueOf(iexRecord.getLatestPrice()));

                return usService.save(userStockMapper.toDto(usrStk));
            } else {
                // Create new User Stock
                UserStockDTO userStockDTO = new UserStockDTO();
                userStockDTO.setCurrent_price(BigDecimal.valueOf(iexRecord.getLatestPrice()));
                userStockDTO.setShares(getShares());
                userStockDTO.setStock(new Stock(getSymbol()));
                userStockDTO.setUser(userMapper.toDto(user));

                return usService.save(userStockDTO);
            }
        }

        return new UserStockDTO();
    }

    public UserStockDTO sell() {
        // Get the latest stock price
        // Check if User shares >= shares
        // if Yes, update balance, and UserStock shares
        IEXRecord iexRecord = IEXIntegrator.fetch(getSymbol());

        if(agent.haveEnoughShares(getShares(), getSymbol(), iexRecord.getLatestPrice())) {
            User user = userService.findByUsername(SecurityUtils.getCurrentUserLogin().get());

            Optional<UserStockDTO> userStock = usService.findByUserAndStock(SecurityUtils.getCurrentUserLogin().get(), getSymbol());
//            Optional<UserStockDTO> userStock = usService.findByUserAndStock(user.getId(), getSymbol());

            if(userStock.isPresent()) {
                // Update User Stock
                UserStock usrStk = userStockMapper.toEntity(userStock.get());
                usrStk.setShares(usrStk.getShares() - getShares());
                usrStk.setCurrentPrice(BigDecimal.valueOf(iexRecord.getLatestPrice()));

                return usService.save(userStockMapper.toDto(usrStk));

            } else {
                throw new UserNotFoundException(getSymbol());
            }
        } else {
            throw new InsufficientStockException("stock");
        }
    }
}
