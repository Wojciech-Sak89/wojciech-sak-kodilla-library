package com.crud.kodillalibrary.exception;

public class ReaderNotFoundException extends Exception {
    public ReaderNotFoundException(int id) {
        super("reader with id " + id + " not found");
    }
}
