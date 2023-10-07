package org.example.data.service.library;

import org.example.data.model.item.IItem;
import org.example.data.model.item.Item;

import java.util.List;

public interface IManageable {

    Item add(Item item);

    void remove(Item item);

    List<Item> listAvailable();

    List<Item> listBorrowed();
}
