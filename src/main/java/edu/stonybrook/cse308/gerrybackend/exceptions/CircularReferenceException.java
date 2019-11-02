package edu.stonybrook.cse308.gerrybackend.exceptions;

public class CircularReferenceException extends Exception {

    public CircularReferenceException(String errorMessage) {
        super(errorMessage);
    }
}
