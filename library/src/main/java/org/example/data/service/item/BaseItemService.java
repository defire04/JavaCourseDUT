package org.example.data.service.item;

import org.example.data.exception.item.ItemNotFoundException;
import org.example.data.model.item.Item;
import org.example.data.repository.item.BaseItemRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


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
