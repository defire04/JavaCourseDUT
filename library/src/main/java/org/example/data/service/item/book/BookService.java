package org.example.data.service.item.book;


import org.example.data.exception.item.book.BookNotFoundException;
import org.example.data.exception.item.book.BookWithThisISBNAlreadyCreatedException;
import org.example.data.model.item.book.Book;
import org.example.data.repository.item.book.BookRepository;
import org.example.data.service.item.BaseItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService extends BaseItemService<Book, BookRepository> {

    protected BookService(BookRepository repository) {
        super(repository);
    }

    public Book getByISBN(long isbn) {
        return getRepository().findByISBN(isbn).orElseThrow(BookNotFoundException::new);
    }

    @Transactional
    @Override
    public Book save(Book book) {
        if (checkExistByISBN(book.getISBN())) {
            throw new BookWithThisISBNAlreadyCreatedException();
        }

        return getRepository().save(book);
    }

    @Transactional
    public void deleteByISBN(long isbn) {
        if (!checkExistByISBN(isbn)) {
            throw new BookNotFoundException();
        }
        getRepository().deleteByISBN(isbn);
    }

    private boolean checkExistByISBN(long isbn) {
        return getRepository().existsByISBN(isbn);
    }
}
