package org.example.data.model.item;

import jakarta.persistence.*;
import lombok.Data;
import org.example.data.model.BaseEntity;


@MappedSuperclass
@Data
public abstract class Item extends BaseEntity {

    private String title;
    private boolean isBorrowed;

}
