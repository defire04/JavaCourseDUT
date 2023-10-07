package org.example.data.service.item.dvd;

import org.example.data.model.item.dvd.Dvd;
import org.example.data.repository.item.dvd.DvdRepository;
import org.example.data.service.item.BaseItemService;
import org.springframework.stereotype.Service;

@Service
public class DvdService extends BaseItemService<Dvd, DvdRepository> {

    public DvdService(DvdRepository repository) {
        super(repository);
    }

}
