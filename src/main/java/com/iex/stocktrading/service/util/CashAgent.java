package com.iex.stocktrading.service.util;

import com.iex.stocktrading.exception.InsufficientBalanceException;
import com.iex.stocktrading.exception.UserNotFoundException;
import com.iex.stocktrading.model.dto.UserDTO;
import com.iex.stocktrading.model.dto.mapper.UserMapper;
import com.iex.stocktrading.security.SecurityUtils;
import com.iex.stocktrading.service.UserService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class CashAgent {

    private final UserService userService;
    private final UserMapper userMapper;

    public CashAgent(UserService userService, UserMapper userMapper) {
        this.userService = userService;
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
    public double getSharesInAccount() {

//        Optional<String> u = SecurityUtils.getCurrentUserLogin();
//
//        if(u.isPresent()) {
//            UserStock user = usService.findByUsername(SecurityUtils.getCurrentUserLogin().get()).get());
//            return user.getAccount().getBalance().doubleValue();
//        } else {
//            throw new UserNotFoundException("User");
//        }

        return 0.0;
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

    public boolean haveEnoughShares(double sharesToSell) {

        if(sharesToSell > getCashInAccount()) {
            throw new InsufficientBalanceException(String.valueOf(getCashInAccount()));
        } else {
//            decreaseCashInAccount(cashToWithdrawal);
            return true;
        }
    }

    public void makeDeposit(double cashToDeposit) {
        increaseCashInAccount(cashToDeposit);
    }
}
