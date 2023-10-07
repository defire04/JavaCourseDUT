package org.example.data.exception.item.book;

public class BookWithThisISBNAlreadyCreatedException extends BookException {
    public BookWithThisISBNAlreadyCreatedException() {
        super("Book with this ISBN already created!");
    }
}
