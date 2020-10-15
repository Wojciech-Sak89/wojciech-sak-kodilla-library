package com.crud.kodillalibrary.exception;

public class BookPieceNotFoundException extends RuntimeException {
    public BookPieceNotFoundException(int id) {
        super("BookPiece id not found: " + id);
    }
}
