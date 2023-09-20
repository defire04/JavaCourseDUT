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
public class Book extends Item {

    private String name;
    private String author;
    private long ISBN;
    private int yearOfPublication;

}
