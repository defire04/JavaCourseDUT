package org.example.data.repository.item;

import org.example.data.model.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface BaseItemRepository<I extends Item> extends JpaRepository<I, UUID> {
    List<I> findAllByIsBorrowedTrue();

    List<I> findAllByIsBorrowedFalse();
}
