package com.crud.kodillalibrary.exception;

public class BookTitleNotFoundException extends Exception {
    public BookTitleNotFoundException(int id) {
        super("BookTitle id not found : " + id);
    }

    public BookTitleNotFoundException(String title, String author) {
        super("Title: " + title + ", author: " + author + " not found");
    }
}
