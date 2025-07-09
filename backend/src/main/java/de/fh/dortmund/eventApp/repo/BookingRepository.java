package de.fh.dortmund.eventApp.repo;

import de.fh.dortmund.eventApp.entity.Booking;
import de.fh.dortmund.eventApp.entity.Event;
import de.fh.dortmund.eventApp.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository-Schnittstelle für Booking-Entitäten.
 */
@Repository
public interface BookingRepository extends ListCrudRepository<Booking, Long> {

    /**
     * Sucht eine Buchung anhand des Bestätigungscodes.
     */
    Optional<Booking> findByBookingConfirmationCode(String confirmationCode);

    /**
     * Gibt alle Buchungen sortiert zurück.
     */
    List<Booking> findAll(Sort sort);

    /**
     * Prüft, ob bereits eine Buchung für das gegebene Event und den gegebenen User existiert.
     * @param event das Event
     * @param user der User
     * @return true, falls eine Buchung existiert
     */
    boolean existsByEventAndUser(Event event, User user);
}
