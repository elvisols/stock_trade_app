package com.iex.stocktrading.controller;

import com.iex.stocktrading.exception.UserStockNotFoundException;
import com.iex.stocktrading.helper.MapValidationErrorHandler;
import com.iex.stocktrading.helper.ResponseWrapper;
import com.iex.stocktrading.model.Stock;
import com.iex.stocktrading.model.dto.NewUserDTO;
import com.iex.stocktrading.model.dto.UserStockDTO;
import com.iex.stocktrading.service.StockService;
import com.iex.stocktrading.service.UserStockService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/user-stocks")
public class UserStocksController {

    private UserStockService usService;
    private StockService stockService;

    private MapValidationErrorHandler handle;

    public UserStocksController(UserStockService usService, StockService stockService, MapValidationErrorHandler handle) {
        this.usService = usService;
        this.stockService = stockService;
        this.handle = handle;
    }

    @GetMapping
    public ResponseEntity<?> getAllStocksByUser(Pageable pageable) {//@RequestParam(defaultValue = "0") Long user,

        Page<UserStockDTO> userStocks = usService.findAllByUser(pageable);

        log.debug("Returning all user stocks: {}", userStocks);

        return new ResponseEntity<ResponseWrapper>(new ResponseWrapper(userStocks), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public UserStockDTO getUserStock(@PathVariable Long id) {
        Optional<UserStockDTO> userStock = usService.findOne(id);

        if (!userStock.isPresent()) throw new UserStockNotFoundException(id.toString());

        return userStock.get();
    }

    @GetMapping("/symbols")
    public ResponseEntity<?> getStockSymbols(Pageable pageable) {

        Page<Stock> stocks = stockService.findAll(pageable);

        return  new ResponseEntity<ResponseWrapper>(new ResponseWrapper(stocks), HttpStatus.OK);
    }

    @PostMapping("/buy/{stock_symbol}")
    public ResponseEntity<?> buyStock(@Valid @RequestBody BuyRequest reqBody, BindingResult result) {

        // Catch possible errors in buy stock request
        ResponseEntity<?> errorMap = handle.MapValidationService(result);
        if(errorMap != null) return errorMap;

        UserStockDTO newStock = null; //usService.save(user);

        return  new ResponseEntity<UserStockDTO>(newStock, HttpStatus.OK);
    }

    @PostMapping("/sell/{stock_symbol}")
    public ResponseEntity<?> sellStock(@Valid @RequestBody NewUserDTO user, BindingResult result){

        // Catch possible errors in sell stock request
        ResponseEntity<?> errorMap = handle.MapValidationService(result);
        if(errorMap != null) return errorMap;

        UserStockDTO newStock = null; //sService.save(user);

        return  new ResponseEntity<UserStockDTO>(newStock, HttpStatus.OK);
    }

}

@Getter
@Setter
class BuyRequest {

    @NotBlank(message = "***> Username cannot be blank")
    private String username;

    @NotBlank(message = "***> Password cannot be blank")
    private String password;

}

@Getter
@Setter
class SellRequest {

    @NotBlank(message = "***> Username cannot be blank")
    private String username;

    @NotBlank(message = "***> Password cannot be blank")
    private String password;

}