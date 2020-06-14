package com.iex.stocktrading.exception;

public class InsufficientSharesException extends RuntimeException {

    public InsufficientSharesException(String message) {
        super("Oops! Your shares count " + message + " is insufficient :(");
    }
}
