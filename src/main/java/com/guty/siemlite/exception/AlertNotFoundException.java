package com.guty.siemlite.exception;

public class AlertNotFoundException extends RuntimeException {

    public AlertNotFoundException(Long id) {
        super("Alert not found with id: " + id);
    }
}
