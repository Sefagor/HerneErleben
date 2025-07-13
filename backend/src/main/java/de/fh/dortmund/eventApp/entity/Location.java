package de.fh.dortmund.eventApp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "location")
public class Location {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)

    private String street;

    @Column(nullable = false)

    private int houseNumber;

    @Column(nullable = false)
    private int zip;

    @OneToMany(mappedBy = "eventLocation")
    private List<Event> events;


}
