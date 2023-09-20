package org.example.data.repository.user;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import org.example.data.model.BaseEntity;
import org.example.data.model.item.Item;
import org.example.data.model.user.Patron;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface PatronRepository extends JpaRepository<Patron, Integer> {

}
