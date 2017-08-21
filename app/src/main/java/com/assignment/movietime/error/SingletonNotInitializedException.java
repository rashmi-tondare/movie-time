package com.assignment.movietime.error;

/**
 * Created by Rashmi on 18/08/17.
 */

public class SingletonNotInitializedException extends RuntimeException {
    public SingletonNotInitializedException(String className) {
        super("Please initialize " + className + " first");
    }
}
