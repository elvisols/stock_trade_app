package com.iex.stocktrading.controller;

import com.iex.stocktrading.exception.UserStockNotFoundException;
import com.iex.stocktrading.helper.MapValidationErrorHandler;
import com.iex.stocktrading.helper.ResponseWrapper;
import com.iex.stocktrading.model.IEXRecord;
import com.iex.stocktrading.model.Stock;
import com.iex.stocktrading.model.dto.NewUserDTO;
import com.iex.stocktrading.model.dto.UserStockDTO;
import com.iex.stocktrading.service.StockService;
import com.iex.stocktrading.service.UserStockService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
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

    @GetMapping("/symbol/{stock_symbol}")
    public ResponseEntity<IEXRecord> getStockSymbols(@PathVariable String stock_symbol) {

        return  new ResponseEntity<IEXRecord>(stockService.findOne(stock_symbol), HttpStatus.OK);
    }

    @GetMapping("/symbols")
    public ResponseEntity<?> getStockSymbols(Pageable pageable) {

        return  new ResponseEntity<ResponseWrapper>(new ResponseWrapper(stockService.findAll(pageable)), HttpStatus.OK);
    }

    @PostMapping("/buy/{stock_symbol}")
    public ResponseEntity<?> buyStock(@PathVariable String stock_symbol, @Valid @RequestBody TradeRequest reqBody, BindingResult result) {

        // Catch possible errors in buy stock request
        ResponseEntity<?> errorMap = handle.MapValidationService(result);
        if(errorMap != null) return errorMap;

        Optional<UserStockDTO> newStock = usService.buy(stock_symbol, reqBody.getShares());

        System.out.println(newStock);

//        if(!newStock.isPresent()) throw new UserStockNotFoundException(stock_symbol);

        return  new ResponseEntity<UserStockDTO>(newStock.get(), HttpStatus.OK);
    }

    @PostMapping("/sell/{stock_symbol}")
    public ResponseEntity<?> sellStock(@PathVariable String stock_symbol, @Valid @RequestBody TradeRequest reqBody, BindingResult result){

        // Catch possible errors in sell stock request
        ResponseEntity<?> errorMap = handle.MapValidationService(result);
        if(errorMap != null) return errorMap;

        Optional<UserStockDTO> soldStock = usService.sell(stock_symbol, reqBody.getShares());

        if(!soldStock.isPresent()) throw new UserStockNotFoundException(stock_symbol);

        return  new ResponseEntity<UserStockDTO>(soldStock.get(), HttpStatus.OK);
    }

}

@Getter
@Setter
class TradeRequest {

    @NotNull(message = "Please specify how many number of shares you want to buy")
    @Digits(message = "Shares must be an integer value", integer = 11, fraction = 0)
    private Integer shares;

}
