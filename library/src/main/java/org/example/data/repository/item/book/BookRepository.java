package org.example.data.repository.item.book;

import org.example.data.model.item.book.Book;
import org.example.data.repository.item.BaseItemRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends BaseItemRepository<Book> {

    Optional<Book> findByISBN(long isbn);

    boolean existsByISBN(long isbn);
    void deleteByISBN(long isbn);


}
