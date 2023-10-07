package org.example.data.model.user;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.data.model.BaseEntity;
import org.example.data.model.item.Item;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "patron")
@EqualsAndHashCode(callSuper = true)
public class Patron extends BaseEntity {
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "patron")
    private List<Item> borrowedItems = new ArrayList<>();
}
