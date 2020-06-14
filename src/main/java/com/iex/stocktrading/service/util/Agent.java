package com.iex.stocktrading.service.util;

import com.iex.stocktrading.exception.InsufficientBalanceException;
import com.iex.stocktrading.exception.InsufficientSharesException;
import com.iex.stocktrading.exception.UserNotFoundException;
import com.iex.stocktrading.model.UserStock;
import com.iex.stocktrading.model.dto.UserDTO;
import com.iex.stocktrading.model.dto.UserStockDTO;
import com.iex.stocktrading.model.dto.mapper.UserMapper;
import com.iex.stocktrading.security.SecurityUtils;
import com.iex.stocktrading.service.UserService;
import com.iex.stocktrading.service.UserStockService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class Agent {

    private final UserService userService;
    private final UserStockService usService;
    private final UserMapper userMapper;

    public Agent(UserService userService, UserStockService usService, UserMapper userMapper) {
        this.userService = userService;
        this.usService = usService;
        this.userMapper = userMapper;
    }

    public double getCashInAccount() {

        Optional<String> u = SecurityUtils.getCurrentUserLogin();

        if(u.isPresent()) {
            UserDTO user = Optional.of(userMapper.toDto(userService.findByUsername(SecurityUtils.getCurrentUserLogin().get()))).get();
            return user.getBalance().doubleValue();
        } else {
            throw new UserNotFoundException("User");
        }

    }
    public double getSharesInAccount(String symbol) {

        Optional<String> u = SecurityUtils.getCurrentUserLogin();

        if(u.isPresent()) {
            UserStockDTO userStock = usService.findByUserAndStock(SecurityUtils.getCurrentUserLogin().get(), symbol).get();
            return userStock.getShares();
        } else {
            throw new UserNotFoundException("User");
        }
    }

    public void decreaseCashInAccount(double cashWithdrawn) {
        userService.withdraw(BigDecimal.valueOf(cashWithdrawn));
    }

    public void increaseCashInAccount(double cashDeposited) {
        userService.deposit(BigDecimal.valueOf(cashDeposited));
    }

    public boolean haveEnoughMoney(double cashToWithdrawal) {

        if(cashToWithdrawal > getCashInAccount()) {
            throw new InsufficientBalanceException(String.valueOf(getCashInAccount()) + ", {"+cashToWithdrawal+"}");
        } else {
            decreaseCashInAccount(cashToWithdrawal);
            return true;
        }
    }

    public boolean haveEnoughShares(double sharesToSell, String symbol, double currentPrice) {

        double current_shares = getSharesInAccount(symbol);

        if(sharesToSell > current_shares) {
            throw new InsufficientSharesException(String.valueOf(current_shares));
        } else {
            increaseCashInAccount(sharesToSell * currentPrice);
            return true;
        }
    }

}
