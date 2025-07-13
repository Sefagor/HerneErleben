package de.fh.dortmund.eventApp.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "event")
@EqualsAndHashCode
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String herneID;

    @Column(nullable = false)
    private LocalDate eventDate;

    @Column(nullable = false)
    private Status status;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "location_id", nullable = false)
    private Location eventLocation;

    private int maxParticipant;

    @Column(nullable = false)
    private String eventDescription;

    @Column(nullable = false, unique = true)
    private String eventName;

    @Lob
    @Column(nullable = false)
    private String eventPhoto;


    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Feedback> feedback = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "event_category",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> category = new ArrayList<>();

    @Column(nullable = false)
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
