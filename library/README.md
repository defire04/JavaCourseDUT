# Laboratory work 1

# Library Management Program

This Java program is designed to manage a simple library. In the library, there are books, and each book has the
following properties:

- Title
- Author
- ISBN (International Standard Book Number)
- Year of Publication

## Program Functionality

The program offers the following functionalities:

### 1. Adding Books to the Library

Users can add new books to the library by providing the following information:

- Title
- Author
- ISBN
- Year of Publication

The program creates a new book entry in the library's catalog.

### 2. Displaying All Books

Users can view a list of all the books available in the library. The program presents a comprehensive list that includes
book titles, authors, ISBNs, and publication years.

### 3. Searching for Books by Title

Users can search for a specific book by its title. The program allows users to input the title of the book they are
looking for and provides the corresponding book details if found.

### 4. Deleting Books by ISBN

Users can remove a book from the library's catalog by specifying its ISBN. The program verifies the ISBN provided by the
user and deletes the corresponding book entry if it exists.

## Description of work

## Phase 1: Getting Started

### 1.1 Dependency Installation

The project had the following dependencies installed:

- **Lombok**: Used for simplifying class creation with automatic generation of getters, setters, and other methods.
- **Spring Web**: Used for creating RESTful web services and handling HTTP requests.
- **Spring Data JPA**: Used for interacting with the database and managing entities.
- **H2 Database**: An embedded database for development and testing.
- **ModelMapper**: Used for simplifying object mapping.

### 1.2 Project Creation

The project was created using Spring Initializr with the addition of the above-mentioned dependencies.

## Phase 2: Model Creation

Models were created to represent books and related entities. Here's an example of a book model:

```java

@Data
@Entity
@Table(name = "book")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book extends Item {
    private String name;
    private String author;
    private long ISBN;
    private int yearOfPublication;
}
```

## Phase 3: Repository Creation

Repositories were created to interact with the database. Here's an example of a book repository:

```java

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    Optional<Book> findByName(String name);

    Optional<Book> findByISBN(long isbn);

    boolean existsByISBN(long isbn);

    void deleteByISBN(long isbn);
}
```

## Phase 4: Exception Handling

Exceptions were created to handle errors. For example, BookNotFoundException:

```java
public class BookException extends RuntimeException{
    public BookException(String message) {
        super(message);
    }
}

public class BookNotFoundException extends BookException {
    public BookNotFoundException() {
        super("Book not found!");
    }
}

public class BookWithThisISBNAlreadyCreatedException extends BookException{
    public BookWithThisISBNAlreadyCreatedException() {
        super("Book with this ISBN already created!");
    }
}
```

## Phase 5: Service Creation

Services were created to perform business logic. An example service for working with books:

```java

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
```

## Phase 6: Mapper Creation

Mappers were created for mapping between entities and DTOs. An example mapper for books:

```java

@Service
public class BookMapper {
    private final ModelMapper modelMapper;
    private final FieldsUpdaterMapper fieldsUpdaterMapper;

    public BookMapper(ModelMapper modelMapper, FieldsUpdaterMapper fieldsUpdaterMapper) {
        this.modelMapper = modelMapper;
        this.fieldsUpdaterMapper = fieldsUpdaterMapper;

        modelMapper.createTypeMap(Book.class, BookDTO.class);
    }

    public Book toModel(BookDTO book) {
        return modelMapper.map(book, Book.class);
    }

    public BookDTO toDTO(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }

    public Book mapAndUpdate(Book oldUser, Book newUser) {
        return fieldsUpdaterMapper.updateFields(oldUser, newUser);
    }
}

```

### Phase 6.1: Create fields updater
```java

@Service
public class FieldsUpdaterMapper {
    public <T> T updateFields(T target, T source) {
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object sourceValue = field.get(source);
                Object targetValue = field.get(target);

                if (sourceValue != null && !sourceValue.equals(targetValue)) {

                    if (sourceValue.equals("")) {
                        field.set(target, targetValue);
                    } else {
                        field.set(target, sourceValue);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return target;
    }
}

```

## Phase 7: Controller Creation

Controllers were created to handle HTTP requests. An example controller for books:

```java
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
```

# Conclusion

Successfully created a simple library management project including models, repositories, exceptions, services, mappers and controllers to manage books and associated data. This project provides basic book inventory management functionality and can be extended to include additional functionality.
