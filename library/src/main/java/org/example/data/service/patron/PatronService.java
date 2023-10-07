package org.example.data.service.patron;

import org.example.data.model.item.IItem;
import org.example.data.model.item.Item;
import org.example.data.model.user.Patron;
import org.example.data.repository.user.PatronRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
