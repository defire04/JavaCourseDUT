# Laboratory work 2

# Library Management Program

I improved the first version of the library program by adding several important features. Now, the librarian can easily expand the library by adding new books and DVDs, as well as remove outdated items. I have also implemented a reader registration system and a function for lending and returning items.

## Program Functionality

The program offers the following functionalities:

### 1. Create abstract class Item 

An abstract class "Item" was created with attributes "title," "uniqueID," and "isBorrowed,".

The creation of abstract methods "borrowItem" and "returnItem" in the "Item" class has been replaced by the introduction of the "IItem" interface, where these methods are defined. The "Item" class now implements the "IItem" interface, providing specific implementations of the "borrowItem" and "returnItem" methods.

### 2. Create item implementations

The "Book" and "DVD" classes were inherited from the "Item" class, which already had implementations of the "borrowItem" and "returnItem" methods. Therefore, there was no need to implement these methods again in the "Book" and "DVD" classes.

### 3. Create class Patron

A class "Patron" was created with attributes "name," "ID," and "borrowedItems," and methods for managing the list of borrowed items were implemented.

### 4. Create an interface IManageable and implementation

An interface "IManageable" was created with methods "add," "remove," "listAvailable," and "listBorrowed."

The "Library" class implements the "IManageable" interface and contains attributes "items" and "patrons." The class also implements methods for patron registration, lending, and returning items.

## Description of work

## Phase 1:  Class Item and IItem interface



```java
public interface IItem {
    void borrowItem();

    void returnItem();
}

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Item extends BaseEntity implements IItem {

    @Column(name = "title")
    private String title;
    @Column(name = "is_borrowed")
    private boolean isBorrowed;


    @ManyToOne
    @JoinColumn(name = "patron_id")
    private Patron patron;


    @Override
    public void borrowItem() {
        this.isBorrowed = true;
    }

    @Override
    public void returnItem() {
        this.isBorrowed = false;
    }
}

```


## Phase 2: Book and Dvd



```java

@Data
@Entity
@Table(name = "book")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Book extends Item {

    @Column(name = "author")
    private String author;

    @Column(name = "isbn", unique = true)
    private long ISBN;

    @Column(name = "year_of_publication")
    private int yearOfPublication;

    public Book(long ISBN) {
        this.ISBN = ISBN;
    }
}

@Data
@Entity
@Table(name = "dvd")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Dvd extends Item {
    @Column(name = "duration")
    private int duration;
}

```

## Phase 3: Repository 

Repositories were created to interact with the database.
First was BaseItemRepository:
```java

public interface BaseItemRepository<I extends Item> extends JpaRepository<I, UUID> {
    List<I> findAllByIsBorrowedTrue();

    List<I> findAllByIsBorrowedFalse();
}
```

Then was Book and Dvd repository which, extends BaseItemRepository
```java
@Repository
public interface BookRepository extends BaseItemRepository<Book> {

    Optional<Book> findByISBN(long isbn);

    boolean existsByISBN(long isbn);
    void deleteByISBN(long isbn);


}

@Repository
public interface DvdRepository extends BaseItemRepository<Dvd> {

}

```

Also PatronRepository

```java
@Repository
public interface PatronRepository extends JpaRepository<Patron, UUID> {

}

```

## Phase 4: Services

Each entity has its own repository and therefore must have its own service.

### Item services 4.1

First BaseItemService
```java
public abstract class BaseItemService <I extends Item, R extends BaseItemRepository<I>>{

    private final R repository;

    protected BaseItemService(R repository) {
        this.repository = repository;
    }

    protected R getRepository() {
        return repository;
    }

    public List<I> getAll() {
        return repository.findAll();
    }

    public List<I> getAllAvailable() {
        return repository.findAllByIsBorrowedFalse();
    }

    public List<I> getAllBorrowed() {
        return repository.findAllByIsBorrowedTrue();
    }


    public I getById(UUID id) {
        return repository.findById(id).orElseThrow(ItemNotFoundException::new);
    }

    @Transactional
    public I save(I dvd) {
        return repository.save(dvd);
    }

    @Transactional
    public void delete(I item) {
        repository.delete(item);
    }
}
```

Then Book and Dvd services, which extends BaseItemService

```java
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

@Service
public class DvdService extends BaseItemService<Dvd, DvdRepository> {

    public DvdService(DvdRepository repository) {
        super(repository);
    }

}
```
### Patron service 4.2

```java
@Service
public class PatronService {
    private final PatronRepository patronRepository;

    public PatronService(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
    }

    @Transactional
    public Patron save(Patron patron) {
        return patronRepository.save(patron);
    }

    @Transactional
    public void addItem(Patron patron, Item item) {
        patron.getBorrowedItems().add(item);
        patronRepository.save(patron);
    }

    @Transactional
    public void returnItem(Patron patron, Item item) {
        patron.getBorrowedItems().remove(item);
        patronRepository.save(patron);
    }
}
```
### Library service 4.3

First I create interface IManageable

```java

public interface IManageable {

    Item add(Item item);

    void remove(Item item);

    List<Item> listAvailable();

    List<Item> listBorrowed();
}

```

Then implements these methods in Library

```java
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
        item.borrowItem();
        patronService.addItem(patron, item);
    }

    //5. Повертати предмет у бібліотеку.
    public void returnItem(Patron patron, Item item) {
        item.returnItem();
        patronService.returnItem(patron, item);
    }
}

```

# Conclusion

I have successfully improved the system, providing librarians with an efficient tool for managing items such as books and DVDs, as well as clients. With the introduction of this new functionality, librarians now have an enhanced toolset for library management. For developers, this enhancement proves beneficial due to the use of abstract classes and interfaces, simplifying maintenance and potential future updates of the program. It is worth noting that all the required functionality for this laboratory work has been effectively covered by integration tests. Any functionality not described in the laboratory work has not been covered by unit tests.