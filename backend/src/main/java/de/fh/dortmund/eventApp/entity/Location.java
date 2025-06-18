package de.fh.dortmund.eventApp.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
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

    private Location(Builder builder) {
        this.id = builder.id;
        this.city = builder.city;
        this.street = builder.street;
        ;
        this.houseNumber = builder.houseNumber;
        this.zip = builder.zip;
        this.events = builder.events;

    }

    public Location() {

    }

    public static class Builder {


        private int id;
        private String city;

        private String street;

        private int houseNumber;
        private int zip;

        private List<Event> events = new ArrayList<>();

        public Location build() {
            return new Location(this);
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder houseNumber(int houseNumber) {
            this.houseNumber = houseNumber;
            return this;
        }

        public Builder zip(int zip) {
            this.zip = zip;
            return this;
        }

        public Builder street(String street) {
            this.street = street;
            return this;
        }

        public Builder event(Event event) {
            this.events.add(event);
            return this;
        }

        public Builder events(List<Event> events) {
            this.events.addAll(events);
            return this;
        }

    }


}
