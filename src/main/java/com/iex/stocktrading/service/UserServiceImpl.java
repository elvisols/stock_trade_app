package com.iex.stocktrading.service;

import com.iex.stocktrading.exception.UserNotFoundException;
import com.iex.stocktrading.model.EActivity;
import com.iex.stocktrading.model.Transaction;
import com.iex.stocktrading.model.User;
import com.iex.stocktrading.model.dto.NewUserDTO;
import com.iex.stocktrading.model.dto.TransactionDTO;
import com.iex.stocktrading.model.dto.UserDTO;
import com.iex.stocktrading.model.dto.mapper.NewUserMapper;
import com.iex.stocktrading.model.dto.mapper.TransactionMapper;
import com.iex.stocktrading.model.dto.mapper.UserMapper;
import com.iex.stocktrading.repository.StockRepository;
import com.iex.stocktrading.repository.TransactionRepository;
import com.iex.stocktrading.repository.UserRepository;
import com.iex.stocktrading.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final BCryptPasswordEncoder encoder;
    private final UserMapper userMapper;
    private final NewUserMapper newUserMapper;
    private final TransactionMapper transactionMapper;

    public UserServiceImpl(UserRepository userRepository, StockRepository stockRepository, BCryptPasswordEncoder encoder, UserMapper userMapper, NewUserMapper newUserMapper, TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.encoder = encoder;
        this.userMapper = userMapper;
        this.newUserMapper = newUserMapper;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public UserDTO save(NewUserDTO userDTO) {

        User user = newUserMapper.toEntity(userDTO);

        user.getAccount().setBalance(BigDecimal.ZERO);

        user.setPassword(encoder.encode(user.getPassword()));

        user.getAccount().setUser(user);

        log.info("User: {}", user);

        user = userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Override
    public UserDTO update(UserDTO userDTO) {

        User user = userMapper.toEntity(userDTO);

        Optional<User> u = this.findById(user.getId());

        if(u.isPresent()) {
            User tmp = u.get();
            tmp.setFullname(user.getFullname() == null ? tmp.getFullname() : user.getFullname());
            tmp.setEmail(user.getEmail() == null ? tmp.getEmail() : user.getEmail());
            tmp.setAge(user.getAge() == null ? tmp.getAge() : user.getAge());
            user = tmp;
        } else {
            throw new UserNotFoundException(user.getId().toString());
        }

        user = userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Override
    public UserDTO deposit(BigDecimal amount) {

        User user = null;

        Optional<String> u = SecurityUtils.getCurrentUserLogin();

        if(u.isPresent()) {
            user = this.findByUsername(SecurityUtils.getCurrentUserLogin().get());
            user.getAccount().setBalance(user.getAccount().getBalance().add(amount));
        } else {
            throw new UserNotFoundException("User");
        }

        log.info("About to deposit: " + user);

        user = userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Override
    public UserDTO withdraw(BigDecimal amount) {

        User user = null;

        Optional<String> u = SecurityUtils.getCurrentUserLogin();

        if(u.isPresent()) {
            user = this.findByUsername(SecurityUtils.getCurrentUserLogin().get());
            user.getAccount().setBalance(user.getAccount().getBalance().subtract(amount));
        } else {
            throw new UserNotFoundException("User");
        }

        log.info("About to withdraw: " + user);

        user = userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Override
    public Page<UserDTO> findAll(Pageable pageable) {
        log.info("Request to get all Users");

        return userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }

    @Override
    public Optional<UserDTO> findOne(Long id) {
        log.info("Request to get User: {}", id);

        return userRepository.findById(id)
                .map(userMapper::toDto);
    }

    @Override
    public User findByUsername(String username) {
        log.info("Request to get User: {}", username);

        return userRepository.findByUsername(username);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete User: {}", id);

        userRepository.deleteById(id);
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

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

}
