package de.fh.dortmund.eventApp.repo;

import de.fh.dortmund.eventApp.entity.Booking;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends ListCrudRepository<Booking, Long> {

    Optional<Booking> findByBookingConfirmationCode(String confirmationCode);

    List<Booking> findAll(Sort id);
}
