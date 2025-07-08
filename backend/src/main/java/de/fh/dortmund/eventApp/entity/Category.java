package de.fh.dortmund.eventApp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "category")
    private List<Event> events = new ArrayList<>();

    @Column(nullable = false)
    private String name;
}
