package org.example.data.exception.book;

public class BookNotFoundException extends BookException{
    public BookNotFoundException() {
        super("Book not found!");
    }
}
