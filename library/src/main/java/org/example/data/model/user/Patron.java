package org.example.data.model.user;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import org.example.data.model.BaseEntity;
import org.example.data.model.item.Item;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "patron")
public class Patron extends BaseEntity {
    private String name;
    @OneToMany
    private List<Item> borrowedItems = new ArrayList<>();
}
