package org.example.data.model.item.dvd;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.data.model.item.Item;

@Data
@Entity
@Table(name = "dvd")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Dvd extends Item {
    private int duration;
}
