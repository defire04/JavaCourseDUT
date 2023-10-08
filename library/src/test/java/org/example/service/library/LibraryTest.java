package org.example.service.library;

import org.example.data.exception.item.ItemNotFoundException;
import org.example.data.model.item.Item;
import org.example.data.model.item.book.Book;
import org.example.data.model.item.dvd.Dvd;
import org.example.data.model.user.Patron;
import org.example.data.service.item.book.BookService;
import org.example.data.service.item.dvd.DvdService;
import org.example.data.service.library.Library;
import org.example.data.service.patron.PatronService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class LibraryTest {

    @Autowired
    private Library library;

    @Autowired
    private PatronService patronService;

    @Autowired
    private BookService bookService;

    @Autowired
    private DvdService dvdService;


    @Test
    @Transactional
    public void testAddBook() {
        Book book = new Book(1L);

        Item addedItem = library.add(book);

        assertEquals(book.getTitle(), addedItem.getTitle());
        assertEquals(book.getAuthor(), ((Book) addedItem).getAuthor());
        assertEquals(book.getISBN(), ((Book) addedItem).getISBN());
    }

    @Test
    @Transactional
    public void testAddDvd() {
        Dvd dvd = new Dvd();
        dvd.setDuration(10);

        Item addedItem = library.add(dvd);

        assertEquals(dvd.getTitle(), addedItem.getTitle());
        assertEquals(dvd.getDuration(), ((Dvd) addedItem).getDuration());
    }

    @Test
    @Transactional
    public void testRemoveBook() {
        Book book = new Book(1L);

        Item addedItem = library.add(book);
        library.remove(addedItem);

        assertThrows(ItemNotFoundException.class, () -> bookService.getById(addedItem.getId()));
    }

    @Test
    @Transactional
    public void testRemoveDvd() {
        Dvd dvd = new Dvd();
        dvd.setDuration(10);

        Item addedItem = library.add(dvd);
        library.remove(addedItem);

        assertThrows(ItemNotFoundException.class, () -> dvdService.getById(addedItem.getId()));
    }

    @Test
    @Transactional
    public void testListAvailable() {
        Book book = new Book(1L);
        bookService.save(book);

        Dvd dvd = new Dvd();
        dvd.setDuration(10);
        dvdService.save(dvd);

        List<Item> availableItems = library.listAvailable();

        assertTrue(availableItems.contains(book));
        assertTrue(availableItems.contains(dvd));
    }

    @Test
    @Transactional
    public void testListBorrowed() {
        Book book = new Book(1L);
        bookService.save(book);

        Dvd dvd = new Dvd();
        dvd.setDuration(10);
        dvdService.save(dvd);

        Patron patron = new Patron();
        patron.setName("Dima");
        patronService.save(patron);

        library.lendItem(patron, book);
        library.lendItem(patron, dvd);

        List<Item> borrowedItems = library.listBorrowed();

        assertTrue(borrowedItems.contains(book));
        assertTrue(borrowedItems.contains(dvd));
    }

    @Test
    @Transactional
    public void testRegisterPatron() {
        Patron patron = new Patron();
        patron.setName("Dima");

        Patron registeredPatron = library.registerPatron(patron);

        assertNotNull(registeredPatron.getId());
        assertEquals(patron.getName(), registeredPatron.getName());
    }

    @Test
    @Transactional
    public void testLendItem() {
        Book book = new Book(1L);
        bookService.save(book);

        Patron patron = new Patron();
        patron.setName("Dima");
        patronService.save(patron);

        library.lendItem(patron, book);

        assertTrue(patron.getBorrowedItems().contains(book));
    }

    @Test
    @Transactional
    public void testReturnItem() {
        Book book = new Book(1L);
        bookService.save(book);

        Patron patron = new Patron();
        patron.setName("Dima");
        patronService.save(patron);

        library.lendItem(patron, book);

        library.returnItem(patron, book);

        assertFalse(patron.getBorrowedItems().contains(book));
    }

}

