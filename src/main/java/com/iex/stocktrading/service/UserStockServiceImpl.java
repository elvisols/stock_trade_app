package com.iex.stocktrading.service;

import com.iex.stocktrading.exception.UserNotFoundException;
import com.iex.stocktrading.exception.UserStockNotFoundException;
import com.iex.stocktrading.model.EActivity;
import com.iex.stocktrading.model.Transaction;
import com.iex.stocktrading.model.UserStock;
import com.iex.stocktrading.model.dto.TransactionDTO;
import com.iex.stocktrading.model.dto.UserStockDTO;
import com.iex.stocktrading.model.dto.mapper.TransactionMapper;
import com.iex.stocktrading.model.dto.mapper.UserMapper;
import com.iex.stocktrading.model.dto.mapper.UserStockMapper;
import com.iex.stocktrading.repository.TransactionRepository;
import com.iex.stocktrading.repository.UserStockRepository;
import com.iex.stocktrading.security.SecurityUtils;
import com.iex.stocktrading.service.util.TradeFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Queue;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class UserStockServiceImpl implements UserStockService {

    private final UserStockRepository userStockRepository;

    private final UserStockMapper userStockMapper;

    private final UserMapper userMapper;

    private final TransactionRepository transactionRepository;

    private final TransactionMapper transactionMapper;


    @Autowired
    private TradeFacade tradeFacade;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    @Qualifier("transaction")
    private Queue queue;

    public UserStockServiceImpl(UserStockRepository userStockRepository, UserStockMapper userStockMapper, UserMapper userMapper, TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.userStockRepository = userStockRepository;
        this.userStockMapper = userStockMapper;
        this.userMapper = userMapper;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;

    }

    @Override
    public UserStockDTO save(UserStockDTO userStockDTO) {

        log.info("Request to save UserStock : {}", userStockDTO);

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
        }

        // create new record
        userStock = userStockRepository.save(userStock);

        log.info("returning pojo... {}", userStock);
        log.info("returning dto... {}", userStockMapper.toDto(userStock));

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

    @Override
    public Optional<UserStockDTO> findByUserAndStock(String user, String stock) {

        log.debug("Request to get UserAndStock : {}", user);

        return userStockRepository.findAllByUser_UsernameAndStock_Symbol(user, stock)
                .map(userStockMapper::toDto);
    }

    @Override
    public Optional<UserStockDTO> buy(String symbol, Integer shares) {

        tradeFacade.setSymbol(symbol);
        tradeFacade.setShares(shares);

        UserStockDTO userStockDTO = tradeFacade.buy();

        // log transaction
        Transaction trx = new Transaction();
        trx.setActivity(EActivity.buy);
        trx.setAmount(userStockDTO.getCurrent_price().multiply(BigDecimal.valueOf(shares)));
        trx.setShares(shares);
        trx.setUser(userMapper.toEntity(userStockDTO.getUser()));
        trx.setStock(userStockDTO.getStock());

        // async log
        this.jmsMessagingTemplate.convertAndSend(this.queue, trx);

        return Optional.of(userStockDTO);
    }

    @Override
    public Optional<UserStockDTO> sell(String symbol, Integer shares) {

        tradeFacade.setSymbol(symbol);
        tradeFacade.setShares(shares);

        UserStockDTO userStockDTO = tradeFacade.sell();

        // log transaction
        Transaction trx = new Transaction();
        trx.setActivity(EActivity.sell);
        trx.setAmount(userStockDTO.getCurrent_price().multiply(BigDecimal.valueOf(shares)));
        trx.setShares(shares);
        trx.setUser(userMapper.toEntity(userStockDTO.getUser()));
        trx.setStock(userStockDTO.getStock());

        // async log
        this.jmsMessagingTemplate.convertAndSend(this.queue, trx);

        return Optional.of(userStockDTO);
    }


    @Override
    public Page<TransactionDTO> getTransactionSummary(EActivity activity, Date from, Date to, Pageable pageable) {

        Optional<String> loginUser = SecurityUtils.getCurrentUserLogin();

        if(activity.compareTo(EActivity.all) == 0) {
            // fetch all transactions
            return transactionRepository.findAllByUser_UsernameAndTimestampBetween(loginUser.get(), from, to, pageable).map(transactionMapper::toDto);
        } else {
            // fetch transactions by activities performed.
            return transactionRepository.findAllByUser_UsernameAndActivityAndTimestampBetween(loginUser.get(), activity, from, to, pageable).map(transactionMapper::toDto);
        }
    }

}
