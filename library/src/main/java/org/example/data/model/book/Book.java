package org.example.data.model.book;


import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "book")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String author;
    private long ISBN;
    private int yearOfPublication;



}
