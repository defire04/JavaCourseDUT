package org.example.data.service.book;

import org.example.data.exception.book.BookNotFoundException;
import org.example.data.exception.book.BookWithThisISBNAlreadyCreatedException;
import org.example.data.mapper.item.book.BookMapper;
import org.example.data.model.item.book.Book;
import org.example.data.repository.item.book.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    public Book getByName(String bookName) {
        return bookRepository.findByName(bookName).orElseThrow(BookNotFoundException::new);
    }

    public Book getByISBN(long isbn) {
        return bookRepository.findByISBN(isbn).orElseThrow(BookNotFoundException::new);
    }

    public Book getById(int id) {
        return bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    }

    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Transactional
    public Book save(Book book) {
        if (checkExistByISBN(book.getISBN())) {
            throw new BookWithThisISBNAlreadyCreatedException();
        }

        return bookRepository.save(book);
    }

    @Transactional
    public Book update(Book updatedBook) {
        Book old = getById(updatedBook.getId());
        return bookMapper.mapAndUpdate(old, updatedBook);
    }

    @Transactional
    public void delete(Book book) {
        bookRepository.delete(book);
    }

    @Transactional
    public void deleteByISBN(long isbn) {
        if (!checkExistByISBN(isbn)) {
            throw new BookNotFoundException();
        }
        bookRepository.deleteByISBN(isbn);
    }

    private boolean checkExistByISBN(long isbn) {
        return bookRepository.existsByISBN(isbn);
    }
}
