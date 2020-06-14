package com.iex.stocktrading.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String message) {
        super("Oops! Your shares balance = " + message + " is insufficient :(");
    }
}
