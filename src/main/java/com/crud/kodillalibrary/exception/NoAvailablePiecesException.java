package com.crud.kodillalibrary.exception;

public class NoAvailablePiecesException extends Exception {
    public NoAvailablePiecesException() {
        super("There are no available pieces");
    }
}
