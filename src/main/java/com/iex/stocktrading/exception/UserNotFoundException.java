package com.iex.stocktrading.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super("Oops! " + message + " not found.");
    }
}
