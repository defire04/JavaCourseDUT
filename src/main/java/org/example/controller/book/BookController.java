package org.example.controller.book;

import org.example.data.dto.book.BookDTO;
import org.example.data.mapper.book.BookMapper;
import org.example.data.service.book.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;


    public BookController(BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<BookDTO>> getAll() {
        return ResponseEntity.ok(bookService.getAll().stream().map(bookMapper::toDTO).toList());
    }

    @PostMapping("/add")
    public ResponseEntity<BookDTO> add(@RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookMapper.toDTO(bookService.save(bookMapper.toModel(bookDTO))));
    }

    @GetMapping("/findByName")
    public ResponseEntity<BookDTO> findByName(@RequestParam("name") String bookName) {
        return ResponseEntity.ok(bookMapper.toDTO(bookService.getByName(bookName)));
    }

    @DeleteMapping("/deleteByISBN")
    @ResponseStatus(HttpStatus.OK)
    public void deleteByISBN(@RequestParam("ISBN") long isbn) {
        bookService.deleteByISBN(isbn);
    }
}
