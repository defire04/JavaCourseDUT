package org.example.data.model.item.dvd;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.example.data.model.item.Item;

@Data
@Entity
@Table(name = "dvd")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Dvd extends Item {
    @Column(name = "duration")
    private int duration;
}
