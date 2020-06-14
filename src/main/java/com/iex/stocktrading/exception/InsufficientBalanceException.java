package com.iex.stocktrading.exception;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(String message) {
        super("Oops! Your account balance = " + message + " is insufficient :(");
    }
}
