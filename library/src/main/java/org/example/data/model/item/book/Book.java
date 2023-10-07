package org.example.data.model.item.book;


import jakarta.persistence.*;
import lombok.*;
import org.example.data.model.item.Item;

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
