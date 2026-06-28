package com.guty.siemlite.exception;

public class IocNotFoundException extends RuntimeException {

    public IocNotFoundException(Long id) {
        super("IOC not found with id: " + id);
    }

    public static class IncidentNotFoundException extends RuntimeException {

        public IncidentNotFoundException(Long id) {
            super("Incident not found with id: " + id);
        }
    }
}
