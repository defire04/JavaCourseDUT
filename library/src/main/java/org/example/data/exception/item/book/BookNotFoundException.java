package org.example.data.exception.item.book;

public class BookNotFoundException extends BookException{
    public BookNotFoundException() {
        super("Book not found!");
    }
}
