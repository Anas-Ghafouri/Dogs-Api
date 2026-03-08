package com.polaris.exception;

public class DogNotFoundException extends RuntimeException {
    public DogNotFoundException(Long dogId) {
        super("Dog not found: " + dogId);
    }
}
