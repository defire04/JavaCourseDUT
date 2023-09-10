package org.example.data.mapper.book;

import org.example.data.dto.book.BookDTO;
import org.example.data.mapper.FieldsUpdaterMapper;
import org.example.data.model.book.Book;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
