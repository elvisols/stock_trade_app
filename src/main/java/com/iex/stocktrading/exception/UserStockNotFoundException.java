package com.iex.stocktrading.exception;

public class UserStockNotFoundException extends RuntimeException {

    public UserStockNotFoundException(String message) {
        super("Oops! " + message + " not found.");
    }
}
