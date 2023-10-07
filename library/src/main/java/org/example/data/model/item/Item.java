package org.example.data.model.item;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.data.model.BaseEntity;
import org.example.data.model.user.Patron;


//@MappedSuperclass
@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Item extends BaseEntity implements IItem {

    @Column(name = "title")
    private String title;
    @Column(name = "is_borrowed")
    private boolean isBorrowed;


    @ManyToOne
    @JoinColumn(name = "patron_id")
    private Patron patron;


    @Override
    public void borrowItem() {
        this.isBorrowed = true;
    }

    @Override
    public void returnItem() {
        this.isBorrowed = false;
    }
}
