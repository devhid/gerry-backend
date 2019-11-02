package edu.stonybrook.cse308.gerrybackend.exceptions;

public class MismatchedElectionException extends Exception {

    public MismatchedElectionException(String errorMessage){
        super(errorMessage);
    }
}
