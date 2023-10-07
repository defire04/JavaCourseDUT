package org.example.data.service.library;

import org.example.data.model.item.IItem;
import org.example.data.model.item.Item;
import org.example.data.model.item.book.Book;
import org.example.data.model.item.dvd.Dvd;
import org.example.data.model.user.Patron;
import org.example.data.repository.user.PatronRepository;
import org.example.data.service.item.book.BookService;
import org.example.data.service.item.dvd.DvdService;
import org.example.data.service.patron.PatronService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Library implements IManageable {

    private final PatronService patronService;
    private final BookService bookService;
    private final DvdService dvdService;

    public Library(PatronService patronService, BookService bookService, DvdService dvdService) {
        this.patronService = patronService;
        this.bookService = bookService;
        this.dvdService = dvdService;
    }

    //1. Додавати предмети (книги, DVD) до бібліотеки.
    @Override
    public Item add(Item item) {

        if (item instanceof Book book) {
            return bookService.save(book);
        } else if (item instanceof Dvd dvd) {
            return dvdService.save(dvd);
        }

        throw new RuntimeException("Cant add item!");
    }

    //2. Видаляти предмети з бібліотеки.
    @Override
    public void remove(Item item) {

        if (item instanceof Book book) {
            bookService.delete(book);
        } else if (item instanceof Dvd dvd) {
            dvdService.delete(dvd);
        } else {
            throw new RuntimeException("Cant remove item!");
        }


    }

    //6. Показувати список доступних предметів.
    @Override
    public List<Item> listAvailable() {
        List<Item> availableItems = new ArrayList<>();
        availableItems.addAll(bookService.getAllAvailable());
        availableItems.addAll(dvdService.getAllAvailable());

        return availableItems;
    }

    //7. Показувати список взятих предметів та їхніх читачів
    @Override
    public List<Item> listBorrowed() {
        List<Item> borrowedItems = new ArrayList<>();
        borrowedItems.addAll(bookService.getAllBorrowed());
        borrowedItems.addAll(dvdService.getAllBorrowed());

        return borrowedItems;
    }

    //3. Реєструвати читача.
    public Patron registerPatron(Patron patron) {
        return patronService.save(patron);
    }

    //4. Видавати предмет читачеві.
    public void lendItem(Patron patron, Item item) {
        item.setBorrowed(true);
        patronService.addItem(patron, item);
    }

    //5. Повертати предмет у бібліотеку.
    public void returnItem(Patron patron, Item item) {
        item.setBorrowed(false);
        patronService.returnItem(patron, item);
    }


}

