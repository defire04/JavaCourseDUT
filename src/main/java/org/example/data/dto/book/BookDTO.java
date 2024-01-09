package org.example.data.dto.book;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class BookDTO {
    private String name;
    private String author;
    private long ISBN;
    private int yearOfPublication;
}
