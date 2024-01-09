package org.example.service;

import jakarta.transaction.Transactional;
import org.example.data.exception.book.BookNotFoundException;
import org.example.data.model.book.Book;
import org.example.data.service.book.BookService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Test
    @Rollback
    @Transactional
    public void testSaveBook() {
        Book bookToSave = new Book();
        assertNotNull(bookService.save(bookToSave));
    }

    @Test
    @Rollback
    @Transactional
    public void testGetBookByName() {
        String bookName = "Sample Book";
        long isbn = 123456789L;
        bookService.save(Book.builder()
                .name(bookName)
                .ISBN(isbn)
                .build());

        Book retrievedByName = bookService.getByName(bookName);
        assertEquals(bookName, retrievedByName.getName());
    }

    @Test
    @Rollback
    @Transactional
    public void testGetBookByISBN() {
        long isbn = 123456789L;
        bookService.save(Book.builder()
                .ISBN(isbn)
                .build());
        Book retrievedByISBN = bookService.getByISBN(isbn);
        assertEquals(isbn, retrievedByISBN.getISBN());
    }

    @Test
    @Rollback
    @Transactional
    public void testGetBookById() {
        long isbn = 123456789L;
        Book bookToSave = Book.builder()
                .ISBN(isbn)
                .build();
        Book savedBook = bookService.save(bookToSave);

        int bookId = savedBook.getId();
        Book retrievedById = bookService.getById(bookId);
        assertEquals(bookId, retrievedById.getId());
    }

    @Test
    @Rollback
    @Transactional
    public void testUpdateBook() {
        long isbn = 123456789L;
        Book bookToSave = Book.builder()
                .ISBN(isbn)
                .build();

        Book savedBook = bookService.save(bookToSave);

        int bookId = savedBook.getId();
        Book updatedBook = new Book();
        updatedBook.setId(bookId);

        Book afterUpdate = bookService.update(updatedBook);
        assertEquals(updatedBook.getName(), afterUpdate.getName());
    }

    @Test
    @Rollback
    @Transactional
    public void testGetAllBooks() {

        for (int i = 0; i < 10; i++) {
            bookService.save(Book.builder()
                    .ISBN(i)
                    .build());
        }
        List<Book> allBooks = bookService.getAll();
        assertFalse(allBooks.isEmpty());
    }

    @Test
    @Rollback
    @Transactional
    public void testDeleteBook() {
        long isbn = 123456789L;
        Book bookToSave = Book.builder()
                .ISBN(isbn)
                .build();
        Book savedBook = bookService.save(bookToSave);
        bookService.delete(savedBook);

        assertThrows(BookNotFoundException.class, () -> bookService.getById(savedBook.getId()));
    }
}
