package org.example.data.exception.book;

public class BookWithThisISBNAlreadyCreatedException extends BookException{
    public BookWithThisISBNAlreadyCreatedException() {
        super("Book with this ISBN already created!");
    }
}
