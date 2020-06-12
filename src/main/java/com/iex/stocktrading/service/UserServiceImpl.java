package com.iex.stocktrading.service;

import com.iex.stocktrading.exception.UserNotFoundException;
import com.iex.stocktrading.model.User;
import com.iex.stocktrading.model.dto.NewUserDTO;
import com.iex.stocktrading.model.dto.UserDTO;
import com.iex.stocktrading.model.dto.mapper.NewUserMapper;
import com.iex.stocktrading.model.dto.mapper.UserMapper;
import com.iex.stocktrading.repository.StockRepository;
import com.iex.stocktrading.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final BCryptPasswordEncoder encoder;
    private final UserMapper userMapper;
    private final NewUserMapper newUserMapper;

    public UserServiceImpl(UserRepository userRepository, StockRepository stockRepository, BCryptPasswordEncoder encoder, UserMapper userMapper, NewUserMapper newUserMapper) {
        this.userRepository = userRepository;
        this.stockRepository = stockRepository;
        this.encoder = encoder;
        this.userMapper = userMapper;
        this.newUserMapper = newUserMapper;
    }

    @Override
    public UserDTO save(NewUserDTO userDTO) {
        log.info("Saving a new UserDTO...{}", userDTO);

        User user = newUserMapper.toEntity(userDTO);

        user.setPassword(encoder.encode(user.getPassword()));

//        log.info("Saving User...{} Hobbies: {}", user, user.getHobbies());

//        stockRepository.saveAll(user.getHobbies());

        user = userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        log.debug("Updating User: {}", userDTO);

        User user = userMapper.toEntity(userDTO);

        Optional<User> p = this.findById(user.getId());

        if(p.isPresent()) {
            User tmp = p.get();
//            tmp.setFirst_name(user.getFirst_name() == null ? tmp.getFirst_name() : user.getFirst_name());
//            tmp.setLast_name(user.getLast_name() == null ? tmp.getLast_name() : user.getLast_name());
//            tmp.setAge(user.getAge() == null ? tmp.getAge() : user.getAge());
//            tmp.setColor(user.getColor() == null ? tmp.getColor() : user.getColor());
//            tmp.setHobbies(user.getHobbies() == null ? tmp.getHobbies() : user.getHobbies());
            user = tmp;
        } else {
            throw new UserNotFoundException(user.getId().toString());
        }

        log.info("Updating User ... {}", user);

//        stockRepository.saveAll(user.getHobbies());

        user = userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Override
    public Page<UserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Users");

        return userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }

    @Override
    public Optional<UserDTO> findOne(Long id) {
        log.debug("Request to get User: {}", id);

        return userRepository.findById(id)
                .map(userMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete User: {}", id);

        userRepository.deleteById(id);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

}