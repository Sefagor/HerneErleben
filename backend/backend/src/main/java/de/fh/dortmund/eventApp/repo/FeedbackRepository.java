package de.fh.dortmund.eventApp.repo;

import de.fh.dortmund.eventApp.entity.Feedback;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends ListCrudRepository<Feedback, Integer> {
}
