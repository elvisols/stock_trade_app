package com.iex.stocktrading.controller;

import com.iex.stocktrading.exception.UserNotFoundException;
import com.iex.stocktrading.helper.MapValidationErrorHandler;
import com.iex.stocktrading.helper.ResponseWrapper;
import com.iex.stocktrading.helper.UserValidator;
import com.iex.stocktrading.model.EActivity;
import com.iex.stocktrading.model.dto.NewUserDTO;
import com.iex.stocktrading.model.dto.TransactionDTO;
import com.iex.stocktrading.model.dto.UserDTO;
import com.iex.stocktrading.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
public class UsersController {

    private UserService userService;

    private MapValidationErrorHandler handle;

    private UserValidator userValidator;

    public UsersController(UserService userService, MapValidationErrorHandler handle, UserValidator userValidator) {
        this.userService = userService;
        this.handle = handle;
        this.userValidator = userValidator;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(Pageable pageable) {

        Page<UserDTO> userDTOs = userService.findAll(pageable);

        log.debug("Getting all users: {}", userDTOs);

        return new ResponseEntity<ResponseWrapper>(new ResponseWrapper(userDTOs), HttpStatus.OK);
    }

    @GetMapping("/transactions")
    public Page<TransactionDTO> getAllTransactions(@RequestParam(required = false, defaultValue = "all" ) EActivity activity, @RequestParam(required = false) Instant start, @RequestParam(required = false) Instant end, Pageable pageable) {

        return userService.getTransactionSummary(activity, start, end, pageable);
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        Optional<UserDTO> userDTO = userService.findOne(id);

        if (!userDTO.isPresent()) throw new UserNotFoundException(id.toString());

        return userDTO.get();
    }

    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody NewUserDTO user, BindingResult result){
        // Validate password match
        userValidator.validate(user, result);

        // Catch possible errors in new user request
        ResponseEntity<?> errorMap = handle.MapValidationService(result);
        if(errorMap != null) return errorMap;

        UserDTO newUser = userService.save(user);

        return  new ResponseEntity<UserDTO>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UserDTO user, BindingResult bindingResult) {
        ResponseEntity<?> errorMap = handle.MapValidationService(bindingResult);

        if(errorMap != null) return errorMap;

        user.setId(id);
        UserDTO p = userService.update(user);

        return new ResponseEntity<>(p, HttpStatus.OK);
    }


    @PostMapping("/fund-account/{amount}")
    public ResponseEntity<?> fundme(@PathVariable BigDecimal amount){

        UserDTO newUser = userService.deposit(amount);

        return  new ResponseEntity<UserDTO>(newUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.delete(id);

        return new ResponseEntity<String>("User deleted! :(", HttpStatus.OK);
    }

}
