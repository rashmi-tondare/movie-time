package com.assignment.movietime.error;

/**
 * Created by Rashmi on 18/08/17.
 */

public class SingletonAlreadyInitializedException extends RuntimeException {
    public SingletonAlreadyInitializedException(String className) {
        super(className + " has already been initialized");
    }
}
