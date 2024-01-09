package org.example.controller;

import org.example.controller.book.BookController;
import org.example.data.dto.book.BookDTO;
import org.example.data.mapper.book.BookMapper;
import org.example.data.model.book.Book;
import org.example.data.service.book.BookService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookControllerTest {

    @InjectMocks
    private BookController bookController;
    @Mock
    private BookService bookService;
    @Mock
    private BookMapper bookMapper;
    @Test
    public void testGetAll() {
        List<Book> books = new ArrayList<>();
        books.add(new Book(1,"Book1", "author1", 1234L, 2023));
        books.add(new Book(2, "Book2", "author2", 12345L, 2023));

        when(bookService.getAll()).thenReturn(books);
        when(bookMapper.toDTO(any(Book.class))).thenAnswer(
                (Answer<BookDTO>) invocation -> {
                    Book book = (Book) invocation.getArguments()[0];
                    return new BookDTO(book.getName(), book.getAuthor(), book.getISBN(), book.getYearOfPublication());
                });

        ResponseEntity<List<BookDTO>> response = bookController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        assertEquals("Book1", response.getBody().get(0).getName());
        assertEquals("Book2", response.getBody().get(1).getName());
    }

    @Test
    public void testAdd() {
        BookDTO bookDTO =  BookDTO.builder()
                .name("NewBook")
                .ISBN(12345)
                .build();

        Book book =  Book.builder()
                .name("NewBook")
                .ISBN(12345)
                .build();

        when(bookMapper.toModel(bookDTO)).thenReturn(book);
        when(bookService.save(book)).thenReturn(book);
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        ResponseEntity<BookDTO> response = bookController.add(bookDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("NewBook", Objects.requireNonNull(response.getBody()).getName());
    }

    @Test
    public void testFindByName() {
        String bookName = "Book1";

        Book book =  Book.builder()
                .name(bookName)
                .build();

        BookDTO bookDTO =  BookDTO.builder()
                .name(bookName)
                .build();

        when(bookService.getByName(bookName)).thenReturn(book);
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        ResponseEntity<BookDTO> response = bookController.findByName(bookName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookName, Objects.requireNonNull(response.getBody()).getName());
    }

    @Test
    public void testDeleteByISBN() {
        long isbn = 1234567890L;

        bookController.deleteByISBN(isbn);

        verify(bookService).deleteByISBN(isbn);
    }
}